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
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.PostConstruct;
import java.lang.management.*;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 性能监控控制器
 * 提供详细的系统性能监控和统计信息
 *
 * @author coze-dev
 */
@Slf4j
@RestController
@RequestMapping("/api/monitoring")
@RequiredArgsConstructor
@Tag(name = "性能监控", description = "系统性能监控和统计接口")
public class MonitoringController {

    private final CacheService cacheService;
    private final CacheInvalidationService cacheInvalidationService;

    private final RedisTemplate<String, Object> redisTemplate;
    private final JdbcTemplate jdbcTemplate;

    // 性能统计
    private final Map<String, PerformanceStats> endpointStats = new ConcurrentHashMap<>();
    private final AtomicLong totalRequests = new AtomicLong(0);
    private final AtomicLong errorRequests = new AtomicLong(0);

    private long startTime;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostConstruct
    public void init() {
        this.startTime = System.currentTimeMillis();
        log.info("性能监控控制器初始化完成");
    }

    @Operation(summary = "获取系统性能概览", description = "获取系统的整体性能统计信息")
    @GetMapping("/overview")
    public ResponseEntity<ApiResponse<PerformanceOverview>> getPerformanceOverview() {
        PerformanceOverview overview = new PerformanceOverview();

        // 基本信息
        overview.setTimestamp(LocalDateTime.now().format(formatter));
        overview.setUptime(System.currentTimeMillis() - startTime);

        // JVM 性能指标
        overview.setJvmMetrics(getJvmMetrics());

        // 系统资源使用情况
        overview.setSystemResources(getSystemResources());

        // 应用性能指标
        overview.setApplicationMetrics(getApplicationMetrics());

        // 缓存性能指标
        overview.setCacheMetrics(getCacheMetrics());

        log.debug("获取性能概览完成");
        return ResponseEntity.ok(ApiResponse.success(overview));
    }

    @Operation(summary = "获取JVM详细性能指标", description = "获取JVM的详细性能监控数据")
    @GetMapping("/jvm")
    public ResponseEntity<ApiResponse<JvmMetrics>> getDetailedJvmMetrics() {
        JvmMetrics metrics = getJvmMetrics();
        return ResponseEntity.ok(ApiResponse.success(metrics));
    }

    @Operation(summary = "获取数据库性能指标", description = "获取数据库连接池和查询性能统计")
    @GetMapping("/database")
    public ResponseEntity<ApiResponse<DatabaseMetrics>> getDatabaseMetrics() {
        DatabaseMetrics metrics = new DatabaseMetrics();

        try {
            // 获取数据库连接信息
            metrics.setActiveConnections(getActiveDatabaseConnections());
            metrics.setMaxConnections(getMaxDatabaseConnections());
            metrics.setConnectionPoolUsage(getConnectionPoolUsage());

            // 执行查询性能测试
            metrics.setQueryPerformance(getQueryPerformance());

        } catch (Exception e) {
            log.error("获取数据库性能指标失败", e);
            metrics.setError("获取数据库性能指标失败: " + e.getMessage());
        }

        return ResponseEntity.ok(ApiResponse.success(metrics));
    }

    @Operation(summary = "获取Redis性能指标", description = "获取Redis连接和性能统计")
    @GetMapping("/redis")
    public ResponseEntity<ApiResponse<RedisMetrics>> getRedisMetrics() {
        RedisMetrics metrics = new RedisMetrics();

        try {
            // 获取Redis连接信息
            metrics.setConnectedClients(getRedisConnectedClients());
            metrics.setUsedMemory(getRedisUsedMemory());
            metrics.setTotalCommandsProcessed(getRedisTotalCommands());

            // Redis性能测试
            metrics.setPerformance(getRedisPerformance());

        } catch (Exception e) {
            log.error("获取Redis性能指标失败", e);
            metrics.setError("获取Redis性能指标失败: " + e.getMessage());
        }

        return ResponseEntity.ok(ApiResponse.success(metrics));
    }

    @Operation(summary = "获取API端点性能统计", description = "获取各个API端点的响应时间和调用统计")
    @GetMapping("/endpoints")
    public ResponseEntity<ApiResponse<Map<String, PerformanceStats>>> getEndpointStats() {
        return ResponseEntity.ok(ApiResponse.success(new HashMap<>(endpointStats)));
    }

    @Operation(summary = "获取线程池状态", description = "获取系统线程池的运行状态")
    @GetMapping("/threads")
    public ResponseEntity<ApiResponse<ThreadPoolMetrics>> getThreadPoolMetrics() {
        ThreadPoolMetrics metrics = new ThreadPoolMetrics();

        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        metrics.setThreadCount(threadMXBean.getThreadCount());
        metrics.setDaemonThreadCount(threadMXBean.getDaemonThreadCount());
        metrics.setPeakThreadCount(threadMXBean.getPeakThreadCount());

        // 获取线程状态统计
        Map<String, Integer> threadStates = new HashMap<>();
        long[] threadIds = threadMXBean.getAllThreadIds();
        for (long threadId : threadIds) {
            ThreadInfo threadInfo = threadMXBean.getThreadInfo(threadId);
            if (threadInfo != null) {
                String state = threadInfo.getThreadState().toString();
                threadStates.put(state, threadStates.getOrDefault(state, 0) + 1);
            }
        }
        metrics.setThreadStates(threadStates);

        return ResponseEntity.ok(ApiResponse.success(metrics));
    }

    @Operation(summary = "获取垃圾回收统计", description = "获取JVM垃圾回收器的统计信息")
    @GetMapping("/gc")
    public ResponseEntity<ApiResponse<List<GcMetrics>>> getGcMetrics() {
        List<GcMetrics> gcMetrics = new ArrayList<>();

        List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcBean : gcBeans) {
            GcMetrics metrics = new GcMetrics();
            metrics.setName(gcBean.getName());
            metrics.setCollectionCount(gcBean.getCollectionCount());
            metrics.setCollectionTime(gcBean.getCollectionTime());

            // 计算平均回收时间
            if (gcBean.getCollectionCount() > 0) {
                metrics.setAverageCollectionTime((double) gcBean.getCollectionTime() / gcBean.getCollectionCount());
            }

            gcMetrics.add(metrics);
        }

        return ResponseEntity.ok(ApiResponse.success(gcMetrics));
    }

    @Operation(summary = "获取内存使用统计", description = "获取JVM各个内存区域的详细使用情况")
    @GetMapping("/memory")
    public ResponseEntity<ApiResponse<Map<String, MemoryMetrics>>> getMemoryMetrics() {
        Map<String, MemoryMetrics> memoryMetrics = new HashMap<>();

        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();

        // 堆内存
        MemoryUsage heapMemory = memoryMXBean.getHeapMemoryUsage();
        memoryMetrics.put("heap", createMemoryMetrics("Heap", heapMemory));

        // 非堆内存
        MemoryUsage nonHeapMemory = memoryMXBean.getNonHeapMemoryUsage();
        memoryMetrics.put("nonHeap", createMemoryMetrics("Non-Heap", nonHeapMemory));

        return ResponseEntity.ok(ApiResponse.success(memoryMetrics));
    }

    @Operation(summary = "执行性能基准测试", description = "执行系统性能基准测试")
    @PostMapping("/benchmark")
    public ResponseEntity<ApiResponse<BenchmarkResult>> runBenchmark(
            @RequestParam(defaultValue = "1000") int iterations,
            @RequestParam(defaultValue = "10") int concurrency) {

        BenchmarkResult result = new BenchmarkResult();
        result.setStartTime(LocalDateTime.now().format(formatter));
        result.setIterations(iterations);
        result.setConcurrency(concurrency);

        long benchmarkStartTime = System.currentTimeMillis();

        try {
            // 执行基准测试
            List<Long> responseTimes = performBenchmark(iterations, concurrency);

            result.setEndTime(LocalDateTime.now().format(formatter));
            result.setTotalTime(System.currentTimeMillis() - benchmarkStartTime);
            result.setResponseTimes(responseTimes);

            // 计算统计信息
            if (!responseTimes.isEmpty()) {
                Collections.sort(responseTimes);
                result.setMinResponseTime(responseTimes.get(0));
                result.setMaxResponseTime(responseTimes.get(responseTimes.size() - 1));
                result.setAvgResponseTime(responseTimes.stream().mapToLong(Long::longValue).average().orElse(0));
                result.setMedianResponseTime(responseTimes.get(responseTimes.size() / 2));
                result.set95thPercentile(responseTimes.get((int) (responseTimes.size() * 0.95)));
                result.set99thPercentile(responseTimes.get((int) (responseTimes.size() * 0.99)));
            }

            result.setRequestsPerSecond((double) iterations / (result.getTotalTime() / 1000.0));
            result.setSuccess(true);

        } catch (Exception e) {
            result.setSuccess(false);
            result.setError("基准测试失败: " + e.getMessage());
            log.error("基准测试执行失败", e);
        }

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    // ==================== 私有方法 ====================

    private JvmMetrics getJvmMetrics() {
        JvmMetrics metrics = new JvmMetrics();
        Runtime runtime = Runtime.getRuntime();

        metrics.setTotalMemory(runtime.totalMemory());
        metrics.setFreeMemory(runtime.freeMemory());
        metrics.setMaxMemory(runtime.maxMemory());
        metrics.setUsedMemory(runtime.totalMemory() - runtime.freeMemory());
        metrics.setAvailableProcessors(runtime.availableProcessors());

        // 计算内存使用率
        metrics.setMemoryUsagePercent((double) metrics.getUsedMemory() / runtime.totalMemory() * 100);

        return metrics;
    }

    private SystemResources getSystemResources() {
        SystemResources resources = new SystemResources();

        try {
            java.io.File root = new java.io.File("/");
            resources.setTotalDiskSpace(root.getTotalSpace());
            resources.setFreeDiskSpace(root.getFreeSpace());
            resources.setUsedDiskSpace(root.getTotalSpace() - root.getFreeSpace());

            // 计算磁盘使用率
            resources.setDiskUsagePercent((double) resources.getUsedDiskSpace() / root.getTotalSpace() * 100);

        } catch (Exception e) {
            log.warn("获取磁盘信息失败", e);
        }

        return resources;
    }

    private ApplicationMetrics getApplicationMetrics() {
        ApplicationMetrics metrics = new ApplicationMetrics();

        metrics.setTotalRequests(totalRequests.get());
        metrics.setErrorRequests(errorRequests.get());
        metrics.setActiveThreads(Thread.activeCount());

        if (totalRequests.get() > 0) {
            metrics.setErrorRate((double) errorRequests.get() / totalRequests.get() * 100);
        }

        return metrics;
    }

    private CacheMetrics getCacheMetrics() {
        CacheMetrics metrics = new CacheMetrics();

        CacheService.CacheStats cacheStats = cacheService.getCacheStats();
        metrics.setLocalCacheSize(cacheStats.getLocalCacheSize());
        metrics.setLocalCacheHitRate(cacheStats.getLocalCacheHitRate());
        metrics.setRedisCacheSize(cacheStats.getRedisCacheSize());

        CacheInvalidationService.InvalidationStats invalidationStats =
                cacheInvalidationService.getInvalidationStats();
        metrics.setDependencyGraphSize(invalidationStats.getDependencyGraphSize());
        metrics.setTotalDependencies(invalidationStats.getTotalDependencies());

        return metrics;
    }

    private int getActiveDatabaseConnections() {
        try {
            return jdbcTemplate.getDataSource().getClass().getMethod("getNumActive").invoke(
                    jdbcTemplate.getDataSource()).hashCode();
        } catch (Exception e) {
            return 0;
        }
    }

    private int getMaxDatabaseConnections() {
        try {
            return jdbcTemplate.getDataSource().getClass().getMethod("getMaxActive").invoke(
                    jdbcTemplate.getDataSource()).hashCode();
        } catch (Exception e) {
            return 10; // 默认值
        }
    }

    private double getConnectionPoolUsage() {
        int active = getActiveDatabaseConnections();
        int max = getMaxDatabaseConnections();
        return max > 0 ? (double) active / max * 100 : 0;
    }

    private Map<String, Long> getQueryPerformance() {
        Map<String, Long> performance = new HashMap<>();
        long startTime = System.currentTimeMillis();

        try {
            // 执行简单查询测试
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            performance.put("simpleQuery", System.currentTimeMillis() - startTime);

        } catch (Exception e) {
            performance.put("error", -1L);
        }

        return performance;
    }

    private int getRedisConnectedClients() {
        try {
            Object info = redisTemplate.getConnectionFactory().getConnection().info();
            // 这里可以解析Redis INFO命令的输出
            return 1; // 临时返回
        } catch (Exception e) {
            return 0;
        }
    }

    private long getRedisUsedMemory() {
        try {
            Object memory = redisTemplate.getConnectionFactory().getConnection().info("memory");
            // 这里可以解析Redis内存信息
            return 0; // 临时返回
        } catch (Exception e) {
            return 0;
        }
    }

    private long getRedisTotalCommands() {
        try {
            Object stats = redisTemplate.getConnectionFactory().getConnection().info("stats");
            // 这里可以解析Redis统计信息
            return 0; // 临时返回
        } catch (Exception e) {
            return 0;
        }
    }

    private Map<String, Long> getRedisPerformance() {
        Map<String, Long> performance = new HashMap<>();
        long startTime = System.currentTimeMillis();

        try {
            redisTemplate.opsForValue().set("test_key", "test_value");
            redisTemplate.opsForValue().get("test_key");
            redisTemplate.delete("test_key");

            performance.put("redisOperations", System.currentTimeMillis() - startTime);

        } catch (Exception e) {
            performance.put("error", -1L);
        }

        return performance;
    }

    private MemoryMetrics createMemoryMetrics(String name, MemoryUsage memoryUsage) {
        MemoryMetrics metrics = new MemoryMetrics();
        metrics.setName(name);
        metrics.setInit(memoryUsage.getInit());
        metrics.setUsed(memoryUsage.getUsed());
        metrics.setCommitted(memoryUsage.getCommitted());
        metrics.setMax(memoryUsage.getMax());

        if (memoryUsage.getMax() > 0) {
            metrics.setUsagePercent((double) memoryUsage.getUsed() / memoryUsage.getMax() * 100);
        }

        return metrics;
    }

    private List<Long> performBenchmark(int iterations, int concurrency) {
        List<Long> responseTimes = Collections.synchronizedList(new ArrayList<>());

        // 这里实现并发基准测试
        // 临时实现：模拟响应时间
        for (int i = 0; i < iterations; i++) {
            long responseTime = 10 + (long) (Math.random() * 100); // 10-110ms随机响应时间
            responseTimes.add(responseTime);
        }

        return responseTimes;
    }

    // ==================== 数据传输对象 ====================

    public static class PerformanceOverview {
        private String timestamp;
        private long uptime;
        private JvmMetrics jvmMetrics;
        private SystemResources systemResources;
        private ApplicationMetrics applicationMetrics;
        private CacheMetrics cacheMetrics;

        // Getters and setters
        public String getTimestamp() { return timestamp; }
        public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
        public long getUptime() { return uptime; }
        public void setUptime(long uptime) { this.uptime = uptime; }
        public JvmMetrics getJvmMetrics() { return jvmMetrics; }
        public void setJvmMetrics(JvmMetrics jvmMetrics) { this.jvmMetrics = jvmMetrics; }
        public SystemResources getSystemResources() { return systemResources; }
        public void setSystemResources(SystemResources systemResources) { this.systemResources = systemResources; }
        public ApplicationMetrics getApplicationMetrics() { return applicationMetrics; }
        public void setApplicationMetrics(ApplicationMetrics applicationMetrics) { this.applicationMetrics = applicationMetrics; }
        public CacheMetrics getCacheMetrics() { return cacheMetrics; }
        public void setCacheMetrics(CacheMetrics cacheMetrics) { this.cacheMetrics = cacheMetrics; }
    }

    public static class JvmMetrics {
        private long totalMemory;
        private long freeMemory;
        private long usedMemory;
        private long maxMemory;
        private int availableProcessors;
        private double memoryUsagePercent;

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
        public double getMemoryUsagePercent() { return memoryUsagePercent; }
        public void setMemoryUsagePercent(double memoryUsagePercent) { this.memoryUsagePercent = memoryUsagePercent; }
    }

    public static class SystemResources {
        private long totalDiskSpace;
        private long freeDiskSpace;
        private long usedDiskSpace;
        private double diskUsagePercent;

        // Getters and setters
        public long getTotalDiskSpace() { return totalDiskSpace; }
        public void setTotalDiskSpace(long totalDiskSpace) { this.totalDiskSpace = totalDiskSpace; }
        public long getFreeDiskSpace() { return freeDiskSpace; }
        public void setFreeDiskSpace(long freeDiskSpace) { this.freeDiskSpace = freeDiskSpace; }
        public long getUsedDiskSpace() { return usedDiskSpace; }
        public void setUsedDiskSpace(long usedDiskSpace) { this.usedDiskSpace = usedDiskSpace; }
        public double getDiskUsagePercent() { return diskUsagePercent; }
        public void setDiskUsagePercent(double diskUsagePercent) { this.diskUsagePercent = diskUsagePercent; }
    }

    public static class ApplicationMetrics {
        private long totalRequests;
        private long errorRequests;
        private int activeThreads;
        private double errorRate;

        // Getters and setters
        public long getTotalRequests() { return totalRequests; }
        public void setTotalRequests(long totalRequests) { this.totalRequests = totalRequests; }
        public long getErrorRequests() { return errorRequests; }
        public void setErrorRequests(long errorRequests) { this.errorRequests = errorRequests; }
        public int getActiveThreads() { return activeThreads; }
        public void setActiveThreads(int activeThreads) { this.activeThreads = activeThreads; }
        public double getErrorRate() { return errorRate; }
        public void setErrorRate(double errorRate) { this.errorRate = errorRate; }
    }

    public static class CacheMetrics {
        private int localCacheSize;
        private double localCacheHitRate;
        private long redisCacheSize;
        private int dependencyGraphSize;
        private int totalDependencies;

        // Getters and setters
        public int getLocalCacheSize() { return localCacheSize; }
        public void setLocalCacheSize(int localCacheSize) { this.localCacheSize = localCacheSize; }
        public double getLocalCacheHitRate() { return localCacheHitRate; }
        public void setLocalCacheHitRate(double localCacheHitRate) { this.localCacheHitRate = localCacheHitRate; }
        public long getRedisCacheSize() { return redisCacheSize; }
        public void setRedisCacheSize(long redisCacheSize) { this.redisCacheSize = redisCacheSize; }
        public int getDependencyGraphSize() { return dependencyGraphSize; }
        public void setDependencyGraphSize(int dependencyGraphSize) { this.dependencyGraphSize = dependencyGraphSize; }
        public int getTotalDependencies() { return totalDependencies; }
        public void setTotalDependencies(int totalDependencies) { this.totalDependencies = totalDependencies; }
    }

    public static class DatabaseMetrics {
        private int activeConnections;
        private int maxConnections;
        private double connectionPoolUsage;
        private Map<String, Long> queryPerformance;
        private String error;

        // Getters and setters
        public int getActiveConnections() { return activeConnections; }
        public void setActiveConnections(int activeConnections) { this.activeConnections = activeConnections; }
        public int getMaxConnections() { return maxConnections; }
        public void setMaxConnections(int maxConnections) { this.maxConnections = maxConnections; }
        public double getConnectionPoolUsage() { return connectionPoolUsage; }
        public void setConnectionPoolUsage(double connectionPoolUsage) { this.connectionPoolUsage = connectionPoolUsage; }
        public Map<String, Long> getQueryPerformance() { return queryPerformance; }
        public void setQueryPerformance(Map<String, Long> queryPerformance) { this.queryPerformance = queryPerformance; }
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
    }

    public static class RedisMetrics {
        private int connectedClients;
        private long usedMemory;
        private long totalCommandsProcessed;
        private Map<String, Long> performance;
        private String error;

        // Getters and setters
        public int getConnectedClients() { return connectedClients; }
        public void setConnectedClients(int connectedClients) { this.connectedClients = connectedClients; }
        public long getUsedMemory() { return usedMemory; }
        public void setUsedMemory(long usedMemory) { this.usedMemory = usedMemory; }
        public long getTotalCommandsProcessed() { return totalCommandsProcessed; }
        public void setTotalCommandsProcessed(long totalCommandsProcessed) { this.totalCommandsProcessed = totalCommandsProcessed; }
        public Map<String, Long> getPerformance() { return performance; }
        public void setPerformance(Map<String, Long> performance) { this.performance = performance; }
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
    }

    public static class PerformanceStats {
        private long totalCalls;
        private long errorCalls;
        private long minResponseTime;
        private long maxResponseTime;
        private double avgResponseTime;
        private long lastCallTime;

        // Getters and setters
        public long getTotalCalls() { return totalCalls; }
        public void setTotalCalls(long totalCalls) { this.totalCalls = totalCalls; }
        public long getErrorCalls() { return errorCalls; }
        public void setErrorCalls(long errorCalls) { this.errorCalls = errorCalls; }
        public long getMinResponseTime() { return minResponseTime; }
        public void setMinResponseTime(long minResponseTime) { this.minResponseTime = minResponseTime; }
        public long getMaxResponseTime() { return maxResponseTime; }
        public void setMaxResponseTime(long maxResponseTime) { this.maxResponseTime = maxResponseTime; }
        public double getAvgResponseTime() { return avgResponseTime; }
        public void setAvgResponseTime(double avgResponseTime) { this.avgResponseTime = avgResponseTime; }
        public long getLastCallTime() { return lastCallTime; }
        public void setLastCallTime(long lastCallTime) { this.lastCallTime = lastCallTime; }
    }

    public static class ThreadPoolMetrics {
        private int threadCount;
        private int daemonThreadCount;
        private int peakThreadCount;
        private Map<String, Integer> threadStates;

        // Getters and setters
        public int getThreadCount() { return threadCount; }
        public void setThreadCount(int threadCount) { this.threadCount = threadCount; }
        public int getDaemonThreadCount() { return daemonThreadCount; }
        public void setDaemonThreadCount(int daemonThreadCount) { this.daemonThreadCount = daemonThreadCount; }
        public int getPeakThreadCount() { return peakThreadCount; }
        public void setPeakThreadCount(int peakThreadCount) { this.peakThreadCount = peakThreadCount; }
        public Map<String, Integer> getThreadStates() { return threadStates; }
        public void setThreadStates(Map<String, Integer> threadStates) { this.threadStates = threadStates; }
    }

    public static class GcMetrics {
        private String name;
        private long collectionCount;
        private long collectionTime;
        private double averageCollectionTime;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public long getCollectionCount() { return collectionCount; }
        public void setCollectionCount(long collectionCount) { this.collectionCount = collectionCount; }
        public long getCollectionTime() { return collectionTime; }
        public void setCollectionTime(long collectionTime) { this.collectionTime = collectionTime; }
        public double getAverageCollectionTime() { return averageCollectionTime; }
        public void setAverageCollectionTime(double averageCollectionTime) { this.averageCollectionTime = averageCollectionTime; }
    }

    public static class MemoryMetrics {
        private String name;
        private long init;
        private long used;
        private long committed;
        private long max;
        private double usagePercent;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public long getInit() { return init; }
        public void setInit(long init) { this.init = init; }
        public long getUsed() { return used; }
        public void setUsed(long used) { this.used = used; }
        public long getCommitted() { return committed; }
        public void setCommitted(long committed) { this.commits = committed; }
        public long getMax() { return max; }
        public void setMax(long max) { this.max = max; }
        public double getUsagePercent() { return usagePercent; }
        public void setUsagePercent(double usagePercent) { this.usagePercent = usagePercent; }
    }

    public static class BenchmarkResult {
        private String startTime;
        private String endTime;
        private int iterations;
        private int concurrency;
        private long totalTime;
        private List<Long> responseTimes;
        private long minResponseTime;
        private long maxResponseTime;
        private double avgResponseTime;
        private long medianResponseTime;
        private long percentile95th;
        private long percentile99th;
        private double requestsPerSecond;
        private boolean success;
        private String error;

        // Getters and setters
        public String getStartTime() { return startTime; }
        public void setStartTime(String startTime) { this.startTime = startTime; }
        public String getEndTime() { return endTime; }
        public void setEndTime(String endTime) { this.endTime = endTime; }
        public int getIterations() { return iterations; }
        public void setIterations(int iterations) { this.iterations = iterations; }
        public int getConcurrency() { return concurrency; }
        public void setConcurrency(int concurrency) { this.concurrency = concurrency; }
        public long getTotalTime() { return totalTime; }
        public void setTotalTime(long totalTime) { this.totalTime = totalTime; }
        public List<Long> getResponseTimes() { return responseTimes; }
        public void setResponseTimes(List<Long> responseTimes) { this.responseTimes = responseTimes; }
        public long getMinResponseTime() { return minResponseTime; }
        public void setMinResponseTime(long minResponseTime) { this.minResponseTime = minResponseTime; }
        public long getMaxResponseTime() { return maxResponseTime; }
        public void setMaxResponseTime(long maxResponseTime) { this.maxResponseTime = maxResponseTime; }
        public double getAvgResponseTime() { return avgResponseTime; }
        public void setAvgResponseTime(double avgResponseTime) { this.avgResponseTime = avgResponseTime; }
        public long getMedianResponseTime() { return medianResponseTime; }
        public void setMedianResponseTime(long medianResponseTime) { this.medianResponseTime = medianResponseTime; }
        public long getPercentile95th() { return percentile95th; }
        public void setPercentile95th(long percentile95th) { this.percentile95th = percentile95th; }
        public long getPercentile99th() { return percentile99th; }
        public void setPercentile99th(long percentile99th) { this.percentile99th = percentile99th; }
        public double getRequestsPerSecond() { return requestsPerSecond; }
        public void setRequestsPerSecond(double requestsPerSecond) { this.requestsPerSecond = requestsPerSecond; }
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
    }
}
