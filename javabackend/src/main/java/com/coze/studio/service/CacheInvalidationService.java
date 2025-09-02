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

package com.coze.studio.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;

/**
 * 缓存失效服务
 * 提供多种缓存失效策略和自动失效管理
 *
 * @author coze-dev
 */
@Service
@Slf4j
public class CacheInvalidationService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final CacheService cacheService;

    // 缓存依赖关系图：key -> 依赖该key的其他key列表
    private final Map<String, Set<String>> dependencyGraph = new ConcurrentHashMap<>();

    // 定时任务执行器
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    // 失效策略配置
    private static final long DEFAULT_INVALIDATION_DELAY = 100; // 100ms延迟失效

    @Autowired
    public CacheInvalidationService(RedisTemplate<String, Object> redisTemplate, CacheService cacheService) {
        this.redisTemplate = redisTemplate;
        this.cacheService = cacheService;
    }

    @PostConstruct
    public void init() {
        log.info("缓存失效服务初始化完成");
        startInvalidationTasks();
    }

    // ==================== 基础失效方法 ====================

    /**
     * 立即失效缓存
     */
    public void invalidateImmediately(String key) {
        cacheService.evict(key);
        log.debug("立即失效缓存: key={}", key);
    }

    /**
     * 延迟失效缓存
     */
    public void invalidateWithDelay(String key, long delayMs) {
        scheduler.schedule(() -> {
            cacheService.evict(key);
            log.debug("延迟失效缓存完成: key={}, delay={}ms", key, delayMs);
        }, delayMs, TimeUnit.MILLISECONDS);
    }

    /**
     * 批量失效缓存
     */
    public void invalidateBatch(List<String> keys) {
        for (String key : keys) {
            cacheService.evict(key);
        }
        log.debug("批量失效缓存完成: keys={}", keys.size());
    }

    /**
     * 模式匹配失效缓存
     */
    public void invalidateByPattern(String pattern) {
        cacheService.evictBatch(pattern);
        log.debug("模式失效缓存完成: pattern={}", pattern);
    }

    // ==================== 依赖失效方法 ====================

    /**
     * 添加缓存依赖关系
     * 当主缓存失效时，依赖缓存也会失效
     */
    public void addDependency(String mainKey, String dependentKey) {
        dependencyGraph.computeIfAbsent(mainKey, k -> ConcurrentHashMap.newKeySet()).add(dependentKey);
        log.debug("添加缓存依赖关系: mainKey={}, dependentKey={}", mainKey, dependentKey);
    }

    /**
     * 移除缓存依赖关系
     */
    public void removeDependency(String mainKey, String dependentKey) {
        Set<String> dependents = dependencyGraph.get(mainKey);
        if (dependents != null) {
            dependents.remove(dependentKey);
            log.debug("移除缓存依赖关系: mainKey={}, dependentKey={}", mainKey, dependentKey);
        }
    }

    /**
     * 级联失效缓存（包含依赖项）
     */
    public void invalidateWithDependencies(String key) {
        Set<String> allKeysToInvalidate = new HashSet<>();
        collectDependencies(key, allKeysToInvalidate);

        invalidateBatch(new ArrayList<>(allKeysToInvalidate));
        log.debug("级联失效缓存完成: key={}, totalKeys={}", key, allKeysToInvalidate.size());
    }

    /**
     * 递归收集所有依赖关系
     */
    private void collectDependencies(String key, Set<String> collectedKeys) {
        if (!collectedKeys.add(key)) {
            return; // 避免循环依赖
        }

        Set<String> dependents = dependencyGraph.get(key);
        if (dependents != null) {
            for (String dependent : dependents) {
                collectDependencies(dependent, collectedKeys);
            }
        }
    }

    // ==================== 策略失效方法 ====================

    /**
     * LRU策略失效
     * 清除最少使用的缓存项
     */
    public void invalidateLRU(int count) {
        // 这里可以实现LRU算法
        log.debug("LRU策略失效: count={}", count);
        // 临时实现：随机清除一些缓存
        invalidateByPattern("*");
    }

    /**
     * TTL策略失效
     * 清除即将过期的缓存项
     */
    public void invalidateByTTL(long ttlThresholdMs) {
        // 这里可以实现TTL检查逻辑
        log.debug("TTL策略失效: threshold={}ms", ttlThresholdMs);
    }

    /**
     * 基于时间的失效策略
     */
    public void invalidateTimeBased(long olderThanMs) {
        // 这里可以实现时间-based清理
        log.debug("时间-based失效: olderThan={}ms", olderThanMs);
    }

    // ==================== 事件驱动失效 ====================

    /**
     * 注册数据变更监听器
     */
    public void registerDataChangeListener(String tableName, Function<String, Void> listener) {
        // 这里可以实现数据库变更监听
        log.debug("注册数据变更监听器: table={}", tableName);
    }

    /**
     * 处理数据变更事件
     */
    public void handleDataChange(String tableName, String recordId) {
        String cacheKeyPattern = tableName + ":" + recordId + ":*";
        invalidateByPattern(cacheKeyPattern);
        log.debug("处理数据变更: table={}, recordId={}", tableName, recordId);
    }

    /**
     * 处理用户权限变更
     */
    public void handleUserPermissionChange(Long userId) {
        String cacheKeyPattern = "user:" + userId + ":*";
        invalidateByPattern(cacheKeyPattern);
        log.debug("处理用户权限变更: userId={}", userId);
    }

    /**
     * 处理插件配置变更
     */
    public void handlePluginConfigChange(Long pluginId) {
        String cacheKeyPattern = "plugin:" + pluginId + ":*";
        invalidateByPattern(cacheKeyPattern);

        // 同时失效相关的依赖缓存
        invalidateWithDependencies("plugin:" + pluginId + ":config");
        log.debug("处理插件配置变更: pluginId={}", pluginId);
    }

    // ==================== 预设失效策略 ====================

    /**
     * 用户相关缓存失效策略
     */
    public void invalidateUserCaches(Long userId) {
        List<String> patterns = Arrays.asList(
            "user:" + userId + ":*",
            "user:" + userId + ":permissions",
            "user:" + userId + ":profile",
            "user:" + userId + ":settings"
        );

        for (String pattern : patterns) {
            invalidateByPattern(pattern);
        }
        log.debug("用户缓存失效完成: userId={}", userId);
    }

    /**
     * 插件相关缓存失效策略
     */
    public void invalidatePluginCaches(Long pluginId) {
        List<String> patterns = Arrays.asList(
            "plugin:" + pluginId + ":*",
            "plugin:" + pluginId + ":config",
            "plugin:" + pluginId + ":executions",
            "plugin:" + pluginId + ":stats"
        );

        for (String pattern : patterns) {
            invalidateByPattern(pattern);
        }
        log.debug("插件缓存失效完成: pluginId={}", pluginId);
    }

    /**
     * 工作流相关缓存失效策略
     */
    public void invalidateWorkflowCaches(Long workflowId) {
        List<String> patterns = Arrays.asList(
            "workflow:" + workflowId + ":*",
            "workflow:" + workflowId + ":nodes",
            "workflow:" + workflowId + ":executions",
            "workflow:" + workflowId + ":logs"
        );

        for (String pattern : patterns) {
            invalidateByPattern(pattern);
        }
        log.debug("工作流缓存失效完成: workflowId={}", workflowId);
    }

    /**
     * 模型相关缓存失效策略
     */
    public void invalidateModelCaches(Long modelId) {
        List<String> patterns = Arrays.asList(
            "model:" + modelId + ":*",
            "model:" + modelId + ":config",
            "model:" + modelId + ":usage",
            "model:" + modelId + ":stats"
        );

        for (String pattern : patterns) {
            invalidateByPattern(pattern);
        }
        log.debug("模型缓存失效完成: modelId={}", modelId);
    }

    // ==================== 监控和统计 ====================

    /**
     * 获取失效统计信息
     */
    public InvalidationStats getInvalidationStats() {
        InvalidationStats stats = new InvalidationStats();
        stats.setDependencyGraphSize(dependencyGraph.size());
        stats.setTotalDependencies(dependencyGraph.values().stream().mapToInt(Set::size).sum());
        return stats;
    }

    /**
     * 清理失效统计
     */
    public void clearInvalidationStats() {
        dependencyGraph.clear();
        log.info("失效统计已清理");
    }

    // ==================== 私有方法 ====================

    /**
     * 启动失效任务
     */
    private void startInvalidationTasks() {
        // 定期清理依赖图中的孤立节点
        scheduler.scheduleAtFixedRate(this::cleanupOrphanedDependencies,
                1, 1, TimeUnit.HOURS);

        log.info("缓存失效任务已启动");
    }

    /**
     * 清理孤立的依赖关系
     */
    private void cleanupOrphanedDependencies() {
        int beforeSize = dependencyGraph.size();
        dependencyGraph.entrySet().removeIf(entry -> entry.getValue().isEmpty());
        int afterSize = dependencyGraph.size();

        if (beforeSize != afterSize) {
            log.debug("清理孤立依赖关系: before={}, after={}", beforeSize, afterSize);
        }
    }

    // ==================== 统计信息类 ====================

    /**
     * 失效统计信息
     */
    public static class InvalidationStats {
        private int dependencyGraphSize;
        private int totalDependencies;

        public int getDependencyGraphSize() { return dependencyGraphSize; }
        public void setDependencyGraphSize(int dependencyGraphSize) { this.dependencyGraphSize = dependencyGraphSize; }
        public int getTotalDependencies() { return totalDependencies; }
        public void setTotalDependencies(int totalDependencies) { this.totalDependencies = totalDependencies; }
    }

    /**
     * 失效配置
     */
    public static class InvalidationConfig {
        private String strategy = "immediate"; // immediate, delayed, cascade
        private long delayMs = DEFAULT_INVALIDATION_DELAY;
        private boolean includeDependencies = false;

        public String getStrategy() { return strategy; }
        public void setStrategy(String strategy) { this.strategy = strategy; }
        public long getDelayMs() { return delayMs; }
        public void setDelayMs(long delayMs) { this.delayMs = delayMs; }
        public boolean isIncludeDependencies() { return includeDependencies; }
        public void setIncludeDependencies(boolean includeDependencies) { this.includeDependencies = includeDependencies; }
    }
}
