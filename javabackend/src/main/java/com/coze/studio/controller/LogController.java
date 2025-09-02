/*
 * Copyright 2025 coze-dev Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.coze.studio.controller;

import com.coze.studio.dto.common.ApiResponse;
import com.coze.studio.dto.common.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * 日志聚合控制器
 * 提供系统日志的收集、聚合、查询和分析功能
 *
 * @author coze-dev
 */
@Slf4j
@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
@Tag(name = "日志聚合", description = "系统日志收集、聚合、查询和分析接口")
public class LogController {

    // 日志存储队列（内存存储，用于演示）
    private final Queue<LogEntry> logStorage = new ConcurrentLinkedQueue<>();
    private static final int MAX_LOG_ENTRIES = 10000; // 最大存储条数

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostConstruct
    public void init() {
        log.info("日志聚合控制器初始化完成");

        // 添加一些示例日志
        addSampleLogs();
    }

    @Operation(summary = "获取日志列表", description = "分页获取系统日志")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<LogEntry>>> getLogs(
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String logger,
            @RequestParam(required = false) String message,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @PageableDefault(size = 50) Pageable pageable) {

        log.debug("获取日志列表: level={}, logger={}, message={}, startTime={}, endTime={}",
                level, logger, message, startTime, endTime);

        List<LogEntry> filteredLogs = new ArrayList<>(logStorage);

        // 应用过滤条件
        if (level != null && !level.isEmpty()) {
            filteredLogs = filteredLogs.stream()
                    .filter(log -> level.equalsIgnoreCase(log.getLevel()))
                    .collect(Collectors.toList());
        }

        if (logger != null && !logger.isEmpty()) {
            filteredLogs = filteredLogs.stream()
                    .filter(log -> log.getLogger() != null && log.getLogger().contains(logger))
                    .collect(Collectors.toList());
        }

        if (message != null && !message.isEmpty()) {
            filteredLogs = filteredLogs.stream()
                    .filter(log -> log.getMessage() != null && log.getMessage().contains(message))
                    .collect(Collectors.toList());
        }

        // 时间范围过滤
        if (startTime != null || endTime != null) {
            filteredLogs = filteredLogs.stream()
                    .filter(log -> isInTimeRange(log.getTimestamp(), startTime, endTime))
                    .collect(Collectors.toList());
        }

        // 分页
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), filteredLogs.size());

        List<LogEntry> pageContent = start < filteredLogs.size() ?
                filteredLogs.subList(start, end) : new ArrayList<>();

        PageResponse<LogEntry> pageResponse = new PageResponse<>();
        pageResponse.setItems(pageContent);
        pageResponse.setTotalElements(filteredLogs.size());
        pageResponse.setTotalPages((int) Math.ceil((double) filteredLogs.size() / pageable.getPageSize()));
        pageResponse.setPage(pageable.getPageNumber() + 1);
        pageResponse.setSize(pageable.getPageSize());
        pageResponse.setHasNext(end < filteredLogs.size());
        pageResponse.setHasPrevious(pageable.getPageNumber() > 0);

        return ResponseEntity.ok(ApiResponse.success(pageResponse));
    }

    @Operation(summary = "获取日志统计", description = "获取日志的统计信息")
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<LogStats>> getLogStats(
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {

        log.debug("获取日志统计: startTime={}, endTime={}", startTime, endTime);

        List<LogEntry> filteredLogs = new ArrayList<>(logStorage);

        // 时间范围过滤
        if (startTime != null || endTime != null) {
            filteredLogs = filteredLogs.stream()
                    .filter(log -> isInTimeRange(log.getTimestamp(), startTime, endTime))
                    .collect(Collectors.toList());
        }

        LogStats stats = new LogStats();
        stats.setTotalLogs(filteredLogs.size());

        // 按级别统计
        Map<String, Long> levelStats = filteredLogs.stream()
                .collect(Collectors.groupingBy(LogEntry::getLevel, Collectors.counting()));
        stats.setLevelStats(levelStats);

        // 按日志器统计
        Map<String, Long> loggerStats = filteredLogs.stream()
                .filter(log -> log.getLogger() != null)
                .collect(Collectors.groupingBy(LogEntry::getLogger, Collectors.counting()));
        stats.setLoggerStats(loggerStats);

        // 时间分布统计
        Map<String, Long> timeStats = new LinkedHashMap<>();
        // 这里可以按小时/天等维度统计
        stats.setTimeStats(timeStats);

        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @Operation(summary = "获取日志详情", description = "根据ID获取单条日志详情")
    @GetMapping("/{logId}")
    public ResponseEntity<ApiResponse<LogEntry>> getLogDetail(
            @Parameter(description = "日志ID") @PathVariable String logId) {

        log.debug("获取日志详情: logId={}", logId);

        LogEntry logEntry = logStorage.stream()
                .filter(log -> logId.equals(log.getId()))
                .findFirst()
                .orElse(null);

        if (logEntry == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(ApiResponse.success(logEntry));
    }

    @Operation(summary = "搜索日志", description = "全文搜索日志内容")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<LogEntry>>> searchLogs(
            @RequestParam String query,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String logger,
            @PageableDefault(size = 20) Pageable pageable) {

        log.debug("搜索日志: query={}, level={}, logger={}", query, level, logger);

        List<LogEntry> searchResults = logStorage.stream()
                .filter(log -> matchesQuery(log, query, level, logger))
                .collect(Collectors.toList());

        // 分页
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), searchResults.size());

        List<LogEntry> pageContent = start < searchResults.size() ?
                searchResults.subList(start, end) : new ArrayList<>();

        PageResponse<LogEntry> pageResponse = new PageResponse<>();
        pageResponse.setItems(pageContent);
        pageResponse.setTotalElements(searchResults.size());
        pageResponse.setTotalPages((int) Math.ceil((double) searchResults.size() / pageable.getPageSize()));
        pageResponse.setPage(pageable.getPageNumber() + 1);
        pageResponse.setSize(pageable.getPageSize());
        pageResponse.setHasNext(end < searchResults.size());
        pageResponse.setHasPrevious(pageable.getPageNumber() > 0);

        return ResponseEntity.ok(ApiResponse.success(pageResponse));
    }

    @Operation(summary = "获取错误日志", description = "获取所有错误级别的日志")
    @GetMapping("/errors")
    public ResponseEntity<ApiResponse<PageResponse<LogEntry>>> getErrorLogs(
            @PageableDefault(size = 50) Pageable pageable) {

        log.debug("获取错误日志");

        List<LogEntry> errorLogs = logStorage.stream()
                .filter(log -> "ERROR".equalsIgnoreCase(log.getLevel()) ||
                              "FATAL".equalsIgnoreCase(log.getLevel()))
                .collect(Collectors.toList());

        // 分页
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), errorLogs.size());

        List<LogEntry> pageContent = start < errorLogs.size() ?
                errorLogs.subList(start, end) : new ArrayList<>();

        PageResponse<LogEntry> pageResponse = new PageResponse<>();
        pageResponse.setItems(pageContent);
        pageResponse.setTotalElements(errorLogs.size());
        pageResponse.setTotalPages((int) Math.ceil((double) errorLogs.size() / pageable.getPageSize()));
        pageResponse.setPage(pageable.getPageNumber() + 1);
        pageResponse.setSize(pageable.getPageSize());
        pageResponse.setHasNext(end < errorLogs.size());
        pageResponse.setHasPrevious(pageable.getPageNumber() > 0);

        return ResponseEntity.ok(ApiResponse.success(pageResponse));
    }

    @Operation(summary = "获取最近日志", description = "获取最近N条日志")
    @GetMapping("/recent")
    public ResponseEntity<ApiResponse<List<LogEntry>>> getRecentLogs(
            @RequestParam(defaultValue = "100") int count) {

        log.debug("获取最近日志: count={}", count);

        List<LogEntry> recentLogs = new ArrayList<>(logStorage);
        Collections.reverse(recentLogs); // 反转获取最新的

        List<LogEntry> result = recentLogs.stream()
                .limit(Math.min(count, recentLogs.size()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation(summary = "导出日志", description = "导出日志数据")
    @GetMapping("/export")
    public ResponseEntity<ApiResponse<LogExportData>> exportLogs(
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(defaultValue = "json") String format) {

        log.debug("导出日志: level={}, startTime={}, endTime={}, format={}", level, startTime, endTime, format);

        List<LogEntry> filteredLogs = new ArrayList<>(logStorage);

        // 应用过滤条件
        if (level != null && !level.isEmpty()) {
            filteredLogs = filteredLogs.stream()
                    .filter(log -> level.equalsIgnoreCase(log.getLevel()))
                    .collect(Collectors.toList());
        }

        if (startTime != null || endTime != null) {
            filteredLogs = filteredLogs.stream()
                    .filter(log -> isInTimeRange(log.getTimestamp(), startTime, endTime))
                    .collect(Collectors.toList());
        }

        LogExportData exportData = new LogExportData();
        exportData.setFormat(format);
        exportData.setTotalLogs(filteredLogs.size());
        exportData.setExportedAt(LocalDateTime.now().format(formatter));
        exportData.setFilters(Map.of(
                "level", level != null ? level : "all",
                "startTime", startTime != null ? startTime : "none",
                "endTime", endTime != null ? endTime : "none"
        ));

        // 这里可以实现实际的日志导出逻辑
        exportData.setData(filteredLogs);

        return ResponseEntity.ok(ApiResponse.success(exportData));
    }

    // ==================== 私有方法 ====================

    /**
     * 添加示例日志数据
     */
    private void addSampleLogs() {
        String[] levels = {"INFO", "WARN", "ERROR", "DEBUG"};
        String[] loggers = {"com.coze.studio.controller.PluginController",
                           "com.coze.studio.service.CacheService",
                           "com.coze.studio.config.RedisConfig",
                           "com.coze.studio.controller.HealthController"};
        String[] messages = {
                "插件配置更新成功",
                "缓存命中率统计",
                "Redis连接池初始化",
                "健康检查执行完成",
                "用户权限验证失败",
                "数据库连接超时",
                "插件执行异常",
                "配置文件加载完成"
        };

        Random random = new Random();
        LocalDateTime now = LocalDateTime.now();

        for (int i = 0; i < 1000; i++) {
            LogEntry logEntry = new LogEntry();
            logEntry.setId("log_" + i);
            logEntry.setLevel(levels[random.nextInt(levels.length)]);
            logEntry.setLogger(loggers[random.nextInt(loggers.length)]);
            logEntry.setMessage(messages[random.nextInt(messages.length)]);
            logEntry.setTimestamp(now.minusMinutes(random.nextInt(1440)).format(formatter)); // 随机过去24小时
            logEntry.setThread("http-nio-8080-exec-" + random.nextInt(10));
            logEntry.setHost("localhost");

            // 添加一些额外的上下文信息
            Map<String, Object> context = new HashMap<>();
            context.put("userId", "user_" + random.nextInt(100));
            context.put("requestId", "req_" + random.nextInt(1000));
            context.put("sessionId", "sess_" + random.nextInt(500));
            logEntry.setContext(context);

            logStorage.add(logEntry);
        }

        log.info("已添加 {} 条示例日志", logStorage.size());
    }

    /**
     * 检查时间范围
     */
    private boolean isInTimeRange(String timestamp, String startTime, String endTime) {
        if (timestamp == null) return true;

        try {
            LocalDateTime logTime = LocalDateTime.parse(timestamp, formatter);

            if (startTime != null) {
                LocalDateTime start = LocalDateTime.parse(startTime, formatter);
                if (logTime.isBefore(start)) return false;
            }

            if (endTime != null) {
                LocalDateTime end = LocalDateTime.parse(endTime, formatter);
                if (logTime.isAfter(end)) return false;
            }

            return true;
        } catch (Exception e) {
            return true; // 解析失败时不过滤
        }
    }

    /**
     * 检查是否匹配搜索查询
     */
    private boolean matchesQuery(LogEntry log, String query, String level, String logger) {
        // 级别过滤
        if (level != null && !level.equalsIgnoreCase(log.getLevel())) {
            return false;
        }

        // 日志器过滤
        if (logger != null && (log.getLogger() == null || !log.getLogger().contains(logger))) {
            return false;
        }

        // 内容搜索
        if (query != null && !query.isEmpty()) {
            String lowerQuery = query.toLowerCase();
            return (log.getMessage() != null && log.getMessage().toLowerCase().contains(lowerQuery)) ||
                   (log.getLogger() != null && log.getLogger().toLowerCase().contains(lowerQuery)) ||
                   (log.getThread() != null && log.getThread().toLowerCase().contains(lowerQuery));
        }

        return true;
    }

    // ==================== 数据传输对象 ====================

    public static class LogEntry {
        private String id;
        private String timestamp;
        private String level;
        private String logger;
        private String message;
        private String thread;
        private String host;
        private Map<String, Object> context;

        // Getters and setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getTimestamp() { return timestamp; }
        public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
        public String getLevel() { return level; }
        public void setLevel(String level) { this.level = level; }
        public String getLogger() { return logger; }
        public void setLogger(String logger) { this.logger = logger; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public String getThread() { return thread; }
        public void setThread(String thread) { this.thread = thread; }
        public String getHost() { return host; }
        public void setHost(String host) { this.host = host; }
        public Map<String, Object> getContext() { return context; }
        public void setContext(Map<String, Object> context) { this.context = context; }
    }

    public static class LogStats {
        private int totalLogs;
        private Map<String, Long> levelStats;
        private Map<String, Long> loggerStats;
        private Map<String, Long> timeStats;

        // Getters and setters
        public int getTotalLogs() { return totalLogs; }
        public void setTotalLogs(int totalLogs) { this.totalLogs = totalLogs; }
        public Map<String, Long> getLevelStats() { return levelStats; }
        public void setLevelStats(Map<String, Long> levelStats) { this.levelStats = levelStats; }
        public Map<String, Long> getLoggerStats() { return loggerStats; }
        public void setLoggerStats(Map<String, Long> loggerStats) { this.loggerStats = loggerStats; }
        public Map<String, Long> getTimeStats() { return timeStats; }
        public void setTimeStats(Map<String, Long> timeStats) { this.timeStats = timeStats; }
    }

    public static class LogExportData {
        private String format;
        private int totalLogs;
        private String exportedAt;
        private Map<String, String> filters;
        private List<LogEntry> data;

        // Getters and setters
        public String getFormat() { return format; }
        public void setFormat(String format) { this.format = format; }
        public int getTotalLogs() { return totalLogs; }
        public void setTotalLogs(int totalLogs) { this.totalLogs = totalLogs; }
        public String getExportedAt() { return exportedAt; }
        public void setExportedAt(String exportedAt) { this.exportedAt = exportedAt; }
        public Map<String, String> getFilters() { return filters; }
        public void setFilters(Map<String, String> filters) { this.filters = filters; }
        public List<LogEntry> getData() { return data; }
        public void setData(List<LogEntry> data) { this.data = data; }
    }
}
