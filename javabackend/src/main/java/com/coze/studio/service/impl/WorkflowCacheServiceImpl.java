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

package com.coze.studio.service.impl;

import com.coze.studio.service.WorkflowCacheService;
import com.coze.studio.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 工作流缓存服务实现类
 * 
 * @author coze-dev
 */
@Slf4j
@Service
public class WorkflowCacheServiceImpl implements WorkflowCacheService {

    // 内存缓存存储
    private final Map<String, CacheEntry<Map<String, Object>>> nodeResultCache = new ConcurrentHashMap<>();
    private final Map<Long, CacheEntry<WorkflowDefinitionCache>> workflowDefinitionCache = new ConcurrentHashMap<>();
    private final Map<Long, CacheEntry<ExecutionPlan>> executionPlanCache = new ConcurrentHashMap<>();

    // 缓存统计
    private final AtomicLong totalHits = new AtomicLong(0);
    private final AtomicLong totalMisses = new AtomicLong(0);
    private final AtomicLong evictionCount = new AtomicLong(0);

    // 缓存配置
    private static final long DEFAULT_TTL_SECONDS = 3600; // 1小时
    private static final long WORKFLOW_DEFINITION_TTL = 7200; // 2小时
    private static final long EXECUTION_PLAN_TTL = 1800; // 30分钟
    private static final int MAX_CACHE_SIZE = 10000;

    @Override
    public void cacheNodeResult(String cacheKey, Map<String, Object> result, long ttlSeconds) {
        try {
            long expirationTime = System.currentTimeMillis() + (ttlSeconds * 1000);
            CacheEntry<Map<String, Object>> entry = new CacheEntry<>(result, expirationTime);
            
            // 检查缓存大小限制
            if (nodeResultCache.size() >= MAX_CACHE_SIZE) {
                evictOldestEntries(nodeResultCache, MAX_CACHE_SIZE / 2);
            }
            
            nodeResultCache.put(cacheKey, entry);
            log.debug("缓存节点执行结果: key={}, ttl={}s", cacheKey, ttlSeconds);
            
        } catch (Exception e) {
            log.error("缓存节点执行结果失败: key={}", cacheKey, e);
        }
    }

    @Override
    public Optional<Map<String, Object>> getCachedNodeResult(String cacheKey) {
        try {
            CacheEntry<Map<String, Object>> entry = nodeResultCache.get(cacheKey);
            
            if (entry == null) {
                totalMisses.incrementAndGet();
                log.debug("缓存未命中: key={}", cacheKey);
                return Optional.empty();
            }
            
            if (entry.isExpired()) {
                nodeResultCache.remove(cacheKey);
                totalMisses.incrementAndGet();
                log.debug("缓存已过期: key={}", cacheKey);
                return Optional.empty();
            }
            
            totalHits.incrementAndGet();
            log.debug("缓存命中: key={}", cacheKey);
            return Optional.of(entry.getValue());
            
        } catch (Exception e) {
            log.error("获取缓存节点执行结果失败: key={}", cacheKey, e);
            totalMisses.incrementAndGet();
            return Optional.empty();
        }
    }

    @Override
    public String generateNodeCacheKey(String nodeId, String nodeType, Map<String, Object> inputData) {
        try {
            // 生成基于节点ID、类型和输入数据的缓存键
            StringBuilder keyBuilder = new StringBuilder();
            keyBuilder.append("node:").append(nodeId).append(":").append(nodeType);
            
            // 对输入数据进行哈希处理
            if (inputData != null && !inputData.isEmpty()) {
                String inputJson = JsonUtil.toJson(inputData);
                String inputHash = generateHash(inputJson);
                keyBuilder.append(":").append(inputHash);
            }
            
            return keyBuilder.toString();
            
        } catch (Exception e) {
            log.error("生成节点缓存键失败: nodeId={}, nodeType={}", nodeId, nodeType, e);
            return "node:" + nodeId + ":" + nodeType + ":" + System.currentTimeMillis();
        }
    }

    @Override
    public void cacheWorkflowDefinition(Long workflowId, WorkflowDefinitionCache definition) {
        try {
            long expirationTime = System.currentTimeMillis() + (WORKFLOW_DEFINITION_TTL * 1000);
            CacheEntry<WorkflowDefinitionCache> entry = new CacheEntry<>(definition, expirationTime);
            
            workflowDefinitionCache.put(workflowId, entry);
            log.debug("缓存工作流定义: workflowId={}", workflowId);
            
        } catch (Exception e) {
            log.error("缓存工作流定义失败: workflowId={}", workflowId, e);
        }
    }

    @Override
    public Optional<WorkflowDefinitionCache> getCachedWorkflowDefinition(Long workflowId) {
        try {
            CacheEntry<WorkflowDefinitionCache> entry = workflowDefinitionCache.get(workflowId);
            
            if (entry == null || entry.isExpired()) {
                if (entry != null) {
                    workflowDefinitionCache.remove(workflowId);
                }
                totalMisses.incrementAndGet();
                return Optional.empty();
            }
            
            totalHits.incrementAndGet();
            return Optional.of(entry.getValue());
            
        } catch (Exception e) {
            log.error("获取缓存工作流定义失败: workflowId={}", workflowId, e);
            totalMisses.incrementAndGet();
            return Optional.empty();
        }
    }

    @Override
    public void cacheExecutionPlan(Long workflowId, ExecutionPlan plan) {
        try {
            long expirationTime = System.currentTimeMillis() + (EXECUTION_PLAN_TTL * 1000);
            CacheEntry<ExecutionPlan> entry = new CacheEntry<>(plan, expirationTime);
            
            executionPlanCache.put(workflowId, entry);
            log.debug("缓存执行计划: workflowId={}", workflowId);
            
        } catch (Exception e) {
            log.error("缓存执行计划失败: workflowId={}", workflowId, e);
        }
    }

    @Override
    public Optional<ExecutionPlan> getCachedExecutionPlan(Long workflowId) {
        try {
            CacheEntry<ExecutionPlan> entry = executionPlanCache.get(workflowId);
            
            if (entry == null || entry.isExpired()) {
                if (entry != null) {
                    executionPlanCache.remove(workflowId);
                }
                totalMisses.incrementAndGet();
                return Optional.empty();
            }
            
            totalHits.incrementAndGet();
            return Optional.of(entry.getValue());
            
        } catch (Exception e) {
            log.error("获取缓存执行计划失败: workflowId={}", workflowId, e);
            totalMisses.incrementAndGet();
            return Optional.empty();
        }
    }

    @Override
    public void clearWorkflowCache(Long workflowId) {
        try {
            // 清除工作流定义缓存
            workflowDefinitionCache.remove(workflowId);
            
            // 清除执行计划缓存
            executionPlanCache.remove(workflowId);
            
            // 清除相关的节点结果缓存
            String workflowPrefix = "workflow:" + workflowId + ":";
            nodeResultCache.entrySet().removeIf(entry -> entry.getKey().startsWith(workflowPrefix));
            
            log.info("清除工作流缓存: workflowId={}", workflowId);
            
        } catch (Exception e) {
            log.error("清除工作流缓存失败: workflowId={}", workflowId, e);
        }
    }

    @Override
    public void clearNodeCache(String nodeId) {
        try {
            String nodePrefix = "node:" + nodeId + ":";
            nodeResultCache.entrySet().removeIf(entry -> entry.getKey().startsWith(nodePrefix));
            
            log.info("清除节点缓存: nodeId={}", nodeId);
            
        } catch (Exception e) {
            log.error("清除节点缓存失败: nodeId={}", nodeId, e);
        }
    }

    @Override
    public void clearAllCache() {
        try {
            nodeResultCache.clear();
            workflowDefinitionCache.clear();
            executionPlanCache.clear();
            
            // 重置统计信息
            totalHits.set(0);
            totalMisses.set(0);
            evictionCount.set(0);
            
            log.info("清除所有缓存");
            
        } catch (Exception e) {
            log.error("清除所有缓存失败", e);
        }
    }

    @Override
    public CacheStatistics getCacheStatistics() {
        CacheStatistics stats = new CacheStatistics();
        
        stats.setNodeResultCacheSize(nodeResultCache.size());
        stats.setWorkflowDefinitionCacheSize(workflowDefinitionCache.size());
        stats.setExecutionPlanCacheSize(executionPlanCache.size());
        stats.setTotalCacheSize(stats.getNodeResultCacheSize() + 
                               stats.getWorkflowDefinitionCacheSize() + 
                               stats.getExecutionPlanCacheSize());
        
        long hits = totalHits.get();
        long misses = totalMisses.get();
        long total = hits + misses;
        stats.setTotalHits(hits);
        stats.setTotalMisses(misses);
        stats.setHitRate(total > 0 ? (double) hits / total : 0.0);
        stats.setEvictionCount(evictionCount.get());
        
        return stats;
    }

    /**
     * 生成哈希值
     */
    private String generateHash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            log.error("生成哈希值失败", e);
            return String.valueOf(input.hashCode());
        }
    }

    /**
     * 清理最旧的缓存条目
     */
    private <T> void evictOldestEntries(Map<String, CacheEntry<T>> cache, int targetSize) {
        if (cache.size() <= targetSize) {
            return;
        }
        
        // 简单的LRU实现：移除最旧的条目
        cache.entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e1.getValue().getCreationTime(), e2.getValue().getCreationTime()))
                .limit(cache.size() - targetSize)
                .map(Map.Entry::getKey)
                .forEach(cache::remove);
        
        evictionCount.addAndGet(cache.size() - targetSize);
    }

    /**
     * 缓存条目
     */
    private static class CacheEntry<T> {
        private final T value;
        private final long expirationTime;
        private final long creationTime;

        public CacheEntry(T value, long expirationTime) {
            this.value = value;
            this.expirationTime = expirationTime;
            this.creationTime = System.currentTimeMillis();
        }

        public T getValue() {
            return value;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expirationTime;
        }

        public long getCreationTime() {
            return creationTime;
        }
    }
}
