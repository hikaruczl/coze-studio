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
import com.coze.studio.service.CacheService;
import com.coze.studio.service.CacheInvalidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.PostConstruct;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 健康检查控制器
 * 提供系统健康状态检查和监控信息
 *
 * @author coze-dev
 */
@Slf4j
@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
@Tag(name = "健康检查", description = "系统健康检查和监控接口")
public class HealthController implements HealthIndicator {

    private final CacheService cacheService;
    private final CacheInvalidationService cacheInvalidationService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private long startTime;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostConstruct
    public void init() {
        this.startTime = System.currentTimeMillis();
        log.info("健康检查控制器初始化完成");
    }

    @Operation(summary = "系统健康检查", description = "检查系统整体健康状态")
    @GetMapping
    public ResponseEntity<ApiResponse<HealthStatus>> health() {
        HealthStatus status = new HealthStatus();

        // 基本信息
        status.setStatus("UP");
        status.setTimestamp(LocalDateTime.now().format(formatter));
        status.setUptime(System.currentTimeMillis() - startTime);

        // 组件健康检查
        Map<String, ComponentHealth> components = new HashMap<>();

        // JVM 健康检查
        components.put("jvm", checkJvmHealth());

        // 数据库健康检查
        components.put("database", checkDatabaseHealth());

        // Redis 健康检查
        components.put("redis", checkRedisHealth());

        // 缓存系统健康检查
        components.put("cache", checkCacheHealth());

        // 磁盘空间检查
        components.put("disk", checkDiskHealth());

        status.setComponents(components);

        // 计算整体状态
        boolean allHealthy = components.values().stream()
                .allMatch(component -> "UP".equals(component.getStatus()));

        if (!allHealthy) {
            status.setStatus("DOWN");
        }

        log.debug("健康检查完成: status={}", status.getStatus());
        return ResponseEntity.ok(ApiResponse.success(status));
    }

    @Operation(summary = "详细健康检查", description = "获取详细的健康检查信息")
    @GetMapping("/detailed")
    public ResponseEntity<ApiResponse<DetailedHealthStatus>> detailedHealth() {
        DetailedHealthStatus status = new DetailedHealthStatus();

        // 基本信息
        status.setStatus("UP");
        status.setTimestamp(LocalDateTime.now().format(formatter));
        status.setVersion(getSystemVersion());

        // 系统信息
        status.setSystemInfo(getSystemInfo());

        // 性能指标
        status.setMetrics(getSystemMetrics());

        // 缓存统计
        status.setCacheStats(cacheService.getCacheStats());

        // 缓存失效统计
        status.setInvalidationStats(cacheInvalidationService.getInvalidationStats());

        log.debug("详细健康检查完成");
        return ResponseEntity.ok(ApiResponse.success(status));
    }

    @Operation(summary = "组件健康检查", description = "检查指定组件的健康状态")
    @GetMapping("/component")
    public ResponseEntity<ApiResponse<ComponentHealth>> checkComponent(
            @RequestParam String component) {

        ComponentHealth health = switch (component.toLowerCase()) {
            case "jvm" -> checkJvmHealth();
            case "database" -> checkDatabaseHealth();
            case "redis" -> checkRedisHealth();
            case "cache" -> checkCacheHealth();
            case "disk" -> checkDiskHealth();
            default -> {
                ComponentHealth unknown = new ComponentHealth();
                unknown.setStatus("UNKNOWN");
                unknown.setMessage("未知组件: " + component);
                yield unknown;
            }
        };

        log.debug("组件健康检查: component={}, status={}", component, health.getStatus());
        return ResponseEntity.ok(ApiResponse.success(health));
    }

    @Operation(summary = "性能监控", description = "获取系统性能监控数据")
    @GetMapping("/metrics")
    public ResponseEntity<ApiResponse<SystemMetrics>> getMetrics() {
        SystemMetrics metrics = getSystemMetrics();
        return ResponseEntity.ok(ApiResponse.success(metrics));
    }

    @Operation(summary = "缓存监控", description = "获取缓存系统监控数据")
    @GetMapping("/cache-stats")
    public ResponseEntity<ApiResponse<CacheService.CacheStats>> getCacheStats() {
        CacheService.CacheStats stats = cacheService.getCacheStats();
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    // ==================== Spring Boot Actuator 集成 ====================

    @Override
    public Health health() {
        Health.Builder builder = Health.up();

        // 检查各个组件
        if (!checkDatabaseHealth().getStatus().equals("UP")) {
            builder.down().withDetail("database", "Database connection failed");
        }

        if (!checkRedisHealth().getStatus().equals("UP")) {
            builder.down().withDetail("redis", "Redis connection failed");
        }

        if (!checkCacheHealth().getStatus().equals("UP")) {
            builder.down().withDetail("cache", "Cache system unhealthy");
        }

        return builder.build();
    }

    // ==================== 私有检查方法 ====================

    private ComponentHealth checkJvmHealth() {
        ComponentHealth health = new ComponentHealth();
        Runtime runtime = Runtime.getRuntime();

        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        double memoryUsage = (double) usedMemory / totalMemory * 100;

        Map<String, Object> details = new HashMap<>();
        details.put("totalMemory", String.format("%.2f MB", totalMemory / 1024.0 / 1024.0));
        details.put("freeMemory", String.format("%.2f MB", freeMemory / 1024.0 / 1024.0));
        details.put("usedMemory", String.format("%.2f MB", usedMemory / 1024.0 / 1024.0));
        details.put("memoryUsage", String.format("%.2f%%", memoryUsage));

        health.setStatus(memoryUsage < 90 ? "UP" : "WARNING");
        health.setDetails(details);
        health.setResponseTime(System.currentTimeMillis());

        return health;
    }

    private ComponentHealth checkDatabaseHealth() {
        ComponentHealth health = new ComponentHealth();
        health.setResponseTime(System.currentTimeMillis());

        try {
            // 检查数据库连接
            Connection connection = jdbcTemplate.getDataSource().getConnection();
            connection.close();

            health.setStatus("UP");
            health.setMessage("Database connection successful");

        } catch (Exception e) {
            health.setStatus("DOWN");
            health.setMessage("Database connection failed: " + e.getMessage());
            log.error("Database health check failed", e);
        }

        return health;
    }

    private ComponentHealth checkRedisHealth() {
        ComponentHealth health = new ComponentHealth();
        health.setResponseTime(System.currentTimeMillis());

        try {
            // 检查Redis连接
            String pong = redisTemplate.getConnectionFactory()
                    .getConnection()
                    .ping();

            if ("PONG".equals(pong)) {
                health.setStatus("UP");
                health.setMessage("Redis connection successful");
            } else {
                health.setStatus("DOWN");
                health.setMessage("Redis ping failed");
            }

        } catch (Exception e) {
            health.setStatus("DOWN");
            health.setMessage("Redis connection failed: " + e.getMessage());
            log.error("Redis health check failed", e);
        }

        return health;
    }

    private ComponentHealth checkCacheHealth() {
        ComponentHealth health = new ComponentHealth();
        health.setResponseTime(System.currentTimeMillis());

        try {
            // 测试缓存读写
            String testKey = "health_check_test_" + System.currentTimeMillis();
            String testValue = "test_value";

            cacheService.put(testKey, testValue, 10, 10);
            String retrievedValue = cacheService.get(testKey, String.class);

            if (testValue.equals(retrievedValue)) {
                health.setStatus("UP");
                health.setMessage("Cache system healthy");
            } else {
                health.setStatus("WARNING");
                health.setMessage("Cache read/write test failed");
            }

            // 清理测试数据
            cacheService.evict(testKey);

        } catch (Exception e) {
            health.setStatus("DOWN");
            health.setMessage("Cache system error: " + e.getMessage());
            log.error("Cache health check failed", e);
        }

        return health;
    }

    private ComponentHealth checkDiskHealth() {
        ComponentHealth health = new ComponentHealth();
        health.setResponseTime(System.currentTimeMillis());

        try {
            java.io.File root = new java.io.File("/");
            long totalSpace = root.getTotalSpace();
            long freeSpace = root.getFreeSpace();
            long usedSpace = totalSpace - freeSpace;
            double usagePercent = (double) usedSpace / totalSpace * 100;

            Map<String, Object> details = new HashMap<>();
            details.put("totalSpace", String.format("%.2f GB", totalSpace / 1024.0 / 1024.0 / 1024.0));
            details.put("freeSpace", String.format("%.2f GB", freeSpace / 1024.0 / 1024.0 / 1024.0));
            details.put("usedSpace", String.format("%.2f GB", usedSpace / 1024.0 / 1024.0 / 1024.0));
            details.put("usagePercent", String.format("%.2f%%", usagePercent));

            health.setStatus(usagePercent < 90 ? "UP" : "WARNING");
            health.setDetails(details);

        } catch (Exception e) {
            health.setStatus("UNKNOWN");
            health.setMessage("Cannot check disk space: " + e.getMessage());
            log.error("Disk health check failed", e);
        }

        return health;
    }

    private String getSystemVersion() {
        try {
            return getClass().getPackage().getImplementationVersion();
        } catch (Exception e) {
            return "1.0.0"; // 默认版本
        }
    }

    private Map<String, Object> getSystemInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("javaVersion", System.getProperty("java.version"));
        info.put("osName", System.getProperty("os.name"));
        info.put("osVersion", System.getProperty("os.version"));
        info.put("processors", Runtime.getRuntime().availableProcessors());
        return info;
    }

    private SystemMetrics getSystemMetrics() {
        SystemMetrics metrics = new SystemMetrics();
        Runtime runtime = Runtime.getRuntime();

        metrics.setTotalMemory(runtime.totalMemory());
        metrics.setFreeMemory(runtime.freeMemory());
        metrics.setUsedMemory(runtime.totalMemory() - runtime.freeMemory());
        metrics.setMaxMemory(runtime.maxMemory());
        metrics.setAvailableProcessors(runtime.availableProcessors());
        metrics.setThreadCount(Thread.activeCount());
        metrics.setUptime(System.currentTimeMillis() - startTime);

        return metrics;
    }

    // ==================== 数据传输对象 ====================

    public static class HealthStatus {
        private String status;
        private String timestamp;
        private long uptime;
        private Map<String, ComponentHealth> components;

        // Getters and setters
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getTimestamp() { return timestamp; }
        public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
        public long getUptime() { return uptime; }
        public void setUptime(long uptime) { this.uptime = uptime; }
        public Map<String, ComponentHealth> getComponents() { return components; }
        public void setComponents(Map<String, ComponentHealth> components) { this.components = components; }
    }

    public static class ComponentHealth {
        private String status;
        private String message;
        private long responseTime;
        private Map<String, Object> details;

        public ComponentHealth() {
            this.details = new HashMap<>();
        }

        // Getters and setters
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public long getResponseTime() { return responseTime; }
        public void setResponseTime(long responseTime) { this.responseTime = responseTime; }
        public Map<String, Object> getDetails() { return details; }
        public void setDetails(Map<String, Object> details) { this.details = details; }
    }

    public static class DetailedHealthStatus {
        private String status;
        private String timestamp;
        private String version;
        private Map<String, Object> systemInfo;
        private SystemMetrics metrics;
        private CacheService.CacheStats cacheStats;
        private CacheInvalidationService.InvalidationStats invalidationStats;

        // Getters and setters
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getTimestamp() { return timestamp; }
        public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        public Map<String, Object> getSystemInfo() { return systemInfo; }
        public void setSystemInfo(Map<String, Object> systemInfo) { this.systemInfo = systemInfo; }
        public SystemMetrics getMetrics() { return metrics; }
        public void setMetrics(SystemMetrics metrics) { this.metrics = metrics; }
        public CacheService.CacheStats getCacheStats() { return cacheStats; }
        public void setCacheStats(CacheService.CacheStats cacheStats) { this.cacheStats = cacheStats; }
        public CacheInvalidationService.InvalidationStats getInvalidationStats() { return invalidationStats; }
        public void setInvalidationStats(CacheInvalidationService.InvalidationStats invalidationStats) { this.invalidationStats = invalidationStats; }
    }

    public static class SystemMetrics {
        private long totalMemory;
        private long freeMemory;
        private long usedMemory;
        private long maxMemory;
        private int availableProcessors;
        private int threadCount;
        private long uptime;

        // Getters and setters
        public long getTotalMemory() { return totalMemory; }
        public void setTotalMemory(long totalMemory) { this.totalMemory = totalMemory; }
        public long getFreeMemory() { return freeMemory; }
        public void setFreeMemory(long freeMemory) { this.freeMemory = freeMemory; }
        public long getUsedMemory() { return usedMemory; }
        public void setUsedMemory(long usedMemory) { this.usedMemory = usedMemory; }
        public long getMaxMemory() { return maxMemory; }
        public void setMaxMemory(long maxMemory) { this.maxMemory = maxMemory; }
        public int getAvailableProcessors() { return availableProcessors; }
        public void setAvailableProcessors(int availableProcessors) { this.availableProcessors = availableProcessors; }
        public int getThreadCount() { return threadCount; }
        public void setThreadCount(int threadCount) { this.threadCount = threadCount; }
        public long getUptime() { return uptime; }
        public void setUptime(long uptime) { this.uptime = uptime; }
    }
}
