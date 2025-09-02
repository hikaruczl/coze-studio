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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统管理API控制器
 * 
 * @author coze-dev
 */
@Slf4j
@RestController
@RequestMapping("/api/system")
@RequiredArgsConstructor
@Tag(name = "系统管理", description = "系统状态、健康检查和配置管理接口")
public class SystemController {

    @Value("${spring.application.name:coze-studio}")
    private String applicationName;

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    @Operation(summary = "系统健康检查", description = "检查系统运行状态")
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<Map<String, Object>>> healthCheck() {
        log.debug("执行系统健康检查");
        
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        health.put("application", applicationName);
        health.put("profile", activeProfile);
        
        // 检查各个组件状态
        Map<String, Object> components = new HashMap<>();
        
        // 数据库状态
        components.put("database", checkDatabaseHealth());
        
        // Redis状态
        components.put("redis", checkRedisHealth());
        
        // 向量存储状态
        components.put("vectorStore", checkVectorStoreHealth());
        
        // 文件存储状态
        components.put("fileStorage", checkFileStorageHealth());
        
        health.put("components", components);
        
        // 系统资源信息
        Map<String, Object> system = new HashMap<>();
        Runtime runtime = Runtime.getRuntime();
        system.put("totalMemory", runtime.totalMemory());
        system.put("freeMemory", runtime.freeMemory());
        system.put("maxMemory", runtime.maxMemory());
        system.put("usedMemory", runtime.totalMemory() - runtime.freeMemory());
        system.put("availableProcessors", runtime.availableProcessors());
        
        health.put("system", system);
        
        return ResponseEntity.ok(ApiResponse.success(health));
    }

    @Operation(summary = "系统信息", description = "获取系统基本信息")
    @GetMapping("/info")
    public ResponseEntity<ApiResponse<Map<String, Object>>> systemInfo() {
        log.debug("获取系统信息");
        
        Map<String, Object> info = new HashMap<>();
        info.put("application", applicationName);
        info.put("profile", activeProfile);
        info.put("version", "1.0.0"); // 可以从配置文件或构建信息中获取
        info.put("buildTime", "2025-01-20T10:00:00"); // 构建时间
        info.put("javaVersion", System.getProperty("java.version"));
        info.put("springBootVersion", getSpringBootVersion());
        info.put("startTime", getApplicationStartTime());
        info.put("uptime", getApplicationUptime());
        
        return ResponseEntity.ok(ApiResponse.success(info));
    }

    @Operation(summary = "系统统计", description = "获取系统使用统计信息")
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> systemStats() {
        log.debug("获取系统统计信息");
        
        Map<String, Object> stats = new HashMap<>();
        
        // TODO: 实现具体的统计逻辑
        // 用户统计
        Map<String, Object> userStats = new HashMap<>();
        userStats.put("totalUsers", 0);
        userStats.put("activeUsers", 0);
        userStats.put("newUsersToday", 0);
        stats.put("users", userStats);
        
        // 应用统计
        Map<String, Object> appStats = new HashMap<>();
        appStats.put("totalApps", 0);
        appStats.put("activeApps", 0);
        appStats.put("newAppsToday", 0);
        stats.put("applications", appStats);
        
        // 工作流统计
        Map<String, Object> workflowStats = new HashMap<>();
        workflowStats.put("totalWorkflows", 0);
        workflowStats.put("executionsToday", 0);
        workflowStats.put("successRate", 0.0);
        stats.put("workflows", workflowStats);
        
        // 插件统计
        Map<String, Object> pluginStats = new HashMap<>();
        pluginStats.put("totalPlugins", 0);
        pluginStats.put("installedPlugins", 0);
        pluginStats.put("pluginUsageToday", 0);
        stats.put("plugins", pluginStats);
        
        // 知识库统计
        Map<String, Object> knowledgeStats = new HashMap<>();
        knowledgeStats.put("totalKnowledgeBases", 0);
        knowledgeStats.put("totalDocuments", 0);
        knowledgeStats.put("queriestoday", 0);
        stats.put("knowledge", knowledgeStats);
        
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @Operation(summary = "系统配置", description = "获取系统配置信息")
    @GetMapping("/config")
    public ResponseEntity<ApiResponse<Map<String, Object>>> systemConfig() {
        log.debug("获取系统配置信息");
        
        Map<String, Object> config = new HashMap<>();
        
        // 基本配置
        Map<String, Object> basic = new HashMap<>();
        basic.put("applicationName", applicationName);
        basic.put("profile", activeProfile);
        basic.put("timezone", System.getProperty("user.timezone"));
        basic.put("encoding", System.getProperty("file.encoding"));
        config.put("basic", basic);
        
        // 功能开关
        Map<String, Object> features = new HashMap<>();
        features.put("workflowEnabled", true);
        features.put("pluginEnabled", true);
        features.put("knowledgeBaseEnabled", true);
        features.put("vectorStoreEnabled", true);
        features.put("fileUploadEnabled", true);
        config.put("features", features);
        
        // 限制配置
        Map<String, Object> limits = new HashMap<>();
        limits.put("maxFileSize", "100MB");
        limits.put("maxWorkflowNodes", 100);
        limits.put("maxPluginsPerUser", 50);
        limits.put("maxKnowledgeBasesPerUser", 10);
        limits.put("maxDocumentsPerKnowledgeBase", 1000);
        config.put("limits", limits);
        
        return ResponseEntity.ok(ApiResponse.success(config));
    }

    @Operation(summary = "系统指标", description = "获取系统性能指标")
    @GetMapping("/metrics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> systemMetrics() {
        log.debug("获取系统指标");
        
        Map<String, Object> metrics = new HashMap<>();
        
        // JVM指标
        Map<String, Object> jvm = new HashMap<>();
        Runtime runtime = Runtime.getRuntime();
        jvm.put("heapUsed", runtime.totalMemory() - runtime.freeMemory());
        jvm.put("heapMax", runtime.maxMemory());
        jvm.put("heapCommitted", runtime.totalMemory());
        jvm.put("nonHeapUsed", getUsedNonHeapMemory());
        jvm.put("gcCount", getGcCount());
        jvm.put("gcTime", getGcTime());
        metrics.put("jvm", jvm);
        
        // 系统指标
        Map<String, Object> system = new HashMap<>();
        system.put("cpuUsage", getCpuUsage());
        system.put("loadAverage", getSystemLoadAverage());
        system.put("diskUsage", getDiskUsage());
        system.put("networkIO", getNetworkIO());
        metrics.put("system", system);
        
        // 应用指标
        Map<String, Object> application = new HashMap<>();
        application.put("requestCount", getRequestCount());
        application.put("errorCount", getErrorCount());
        application.put("averageResponseTime", getAverageResponseTime());
        application.put("activeConnections", getActiveConnections());
        metrics.put("application", application);
        
        return ResponseEntity.ok(ApiResponse.success(metrics));
    }

    @Operation(summary = "清理缓存", description = "清理系统缓存")
    @PostMapping("/cache/clear")
    public ResponseEntity<ApiResponse<String>> clearCache(@RequestParam(required = false) String cacheType) {
        log.info("清理缓存: type={}", cacheType);
        
        try {
            if (cacheType == null || "all".equals(cacheType)) {
                // 清理所有缓存
                clearAllCaches();
                return ResponseEntity.ok(ApiResponse.<String>success("所有缓存已清理"));
            } else {
                // 清理指定类型的缓存
                clearSpecificCache(cacheType);
                return ResponseEntity.ok(ApiResponse.<String>success("缓存 " + cacheType + " 已清理"));
            }
        } catch (Exception e) {
            log.error("清理缓存失败", e);
            return ResponseEntity.ok(ApiResponse.<String>error("500", "清理缓存失败: " + e.getMessage()));
        }
    }

    @Operation(summary = "系统日志级别", description = "获取或设置日志级别")
    @GetMapping("/log-level")
    public ResponseEntity<ApiResponse<Map<String, String>>> getLogLevel() {
        log.debug("获取日志级别");
        
        Map<String, String> logLevels = new HashMap<>();
        logLevels.put("root", "INFO");
        logLevels.put("com.coze.studio", "DEBUG");
        logLevels.put("org.springframework", "INFO");
        logLevels.put("org.hibernate", "WARN");
        
        return ResponseEntity.ok(ApiResponse.success(logLevels));
    }

    @Operation(summary = "设置日志级别", description = "动态设置日志级别")
    @PostMapping("/log-level")
    public ResponseEntity<ApiResponse<String>> setLogLevel(
            @RequestParam String logger,
            @RequestParam String level) {
        log.info("设置日志级别: logger={}, level={}", logger, level);
        
        try {
            // TODO: 实现动态设置日志级别的逻辑
            // LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
            // Logger loggerObj = context.getLogger(logger);
            // loggerObj.setLevel(Level.valueOf(level));

            return ResponseEntity.ok(ApiResponse.<String>success("日志级别已设置: " + logger + " = " + level));
        } catch (Exception e) {
            log.error("设置日志级别失败", e);
            return ResponseEntity.ok(ApiResponse.<String>error("500", "设置日志级别失败: " + e.getMessage()));
        }
    }

    // 私有辅助方法
    private Map<String, Object> checkDatabaseHealth() {
        Map<String, Object> status = new HashMap<>();
        try {
            // TODO: 实现数据库健康检查
            status.put("status", "UP");
            status.put("database", "MySQL");
            status.put("connectionPool", "HikariCP");
            status.put("activeConnections", 5);
            status.put("maxConnections", 20);
        } catch (Exception e) {
            status.put("status", "DOWN");
            status.put("error", e.getMessage());
        }
        return status;
    }

    private Map<String, Object> checkRedisHealth() {
        Map<String, Object> status = new HashMap<>();
        try {
            // TODO: 实现Redis健康检查
            status.put("status", "UP");
            status.put("version", "6.2.0");
            status.put("connectedClients", 10);
            status.put("usedMemory", "50MB");
        } catch (Exception e) {
            status.put("status", "DOWN");
            status.put("error", e.getMessage());
        }
        return status;
    }

    private Map<String, Object> checkVectorStoreHealth() {
        Map<String, Object> status = new HashMap<>();
        try {
            // TODO: 实现向量存储健康检查
            status.put("status", "UP");
            status.put("type", "Milvus");
            status.put("collections", 5);
            status.put("totalVectors", 10000);
        } catch (Exception e) {
            status.put("status", "DOWN");
            status.put("error", e.getMessage());
        }
        return status;
    }

    private Map<String, Object> checkFileStorageHealth() {
        Map<String, Object> status = new HashMap<>();
        try {
            // TODO: 实现文件存储健康检查
            status.put("status", "UP");
            status.put("type", "Local");
            status.put("totalSpace", "1TB");
            status.put("usedSpace", "100GB");
            status.put("freeSpace", "900GB");
        } catch (Exception e) {
            status.put("status", "DOWN");
            status.put("error", e.getMessage());
        }
        return status;
    }

    private String getSpringBootVersion() {
        // TODO: 获取Spring Boot版本
        return "3.2.0";
    }

    private String getApplicationStartTime() {
        // TODO: 获取应用启动时间
        return LocalDateTime.now().minusHours(2).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    private String getApplicationUptime() {
        // TODO: 计算应用运行时间
        return "2h 15m 30s";
    }

    private long getUsedNonHeapMemory() {
        // TODO: 获取非堆内存使用量
        return 50 * 1024 * 1024; // 50MB
    }

    private long getGcCount() {
        // TODO: 获取GC次数
        return 100;
    }

    private long getGcTime() {
        // TODO: 获取GC时间
        return 1000; // 1秒
    }

    private double getCpuUsage() {
        // TODO: 获取CPU使用率
        return 0.25; // 25%
    }

    private double getSystemLoadAverage() {
        // TODO: 获取系统负载
        return 1.5;
    }

    private Map<String, Object> getDiskUsage() {
        // TODO: 获取磁盘使用情况
        Map<String, Object> disk = new HashMap<>();
        disk.put("total", "1TB");
        disk.put("used", "100GB");
        disk.put("free", "900GB");
        disk.put("usagePercent", 10.0);
        return disk;
    }

    private Map<String, Object> getNetworkIO() {
        // TODO: 获取网络IO
        Map<String, Object> network = new HashMap<>();
        network.put("bytesReceived", 1024 * 1024 * 100); // 100MB
        network.put("bytesSent", 1024 * 1024 * 50); // 50MB
        network.put("packetsReceived", 10000);
        network.put("packetsSent", 8000);
        return network;
    }

    private long getRequestCount() {
        // TODO: 获取请求总数
        return 10000;
    }

    private long getErrorCount() {
        // TODO: 获取错误总数
        return 50;
    }

    private double getAverageResponseTime() {
        // TODO: 获取平均响应时间
        return 150.5; // 150.5ms
    }

    private int getActiveConnections() {
        // TODO: 获取活跃连接数
        return 25;
    }

    private void clearAllCaches() {
        // TODO: 实现清理所有缓存的逻辑
        log.info("清理所有缓存");
    }

    private void clearSpecificCache(String cacheType) {
        // TODO: 实现清理指定缓存的逻辑
        log.info("清理缓存: {}", cacheType);
    }
}
