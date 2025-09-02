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

import java.util.Map;
import java.util.Optional;

/**
 * 工作流缓存服务接口
 * 
 * @author coze-dev
 */
public interface WorkflowCacheService {

    /**
     * 缓存节点执行结果
     */
    void cacheNodeResult(String cacheKey, Map<String, Object> result, long ttlSeconds);

    /**
     * 获取缓存的节点执行结果
     */
    Optional<Map<String, Object>> getCachedNodeResult(String cacheKey);

    /**
     * 生成节点缓存键
     */
    String generateNodeCacheKey(String nodeId, String nodeType, Map<String, Object> inputData);

    /**
     * 缓存工作流定义
     */
    void cacheWorkflowDefinition(Long workflowId, WorkflowDefinitionCache definition);

    /**
     * 获取缓存的工作流定义
     */
    Optional<WorkflowDefinitionCache> getCachedWorkflowDefinition(Long workflowId);

    /**
     * 缓存执行计划
     */
    void cacheExecutionPlan(Long workflowId, ExecutionPlan plan);

    /**
     * 获取缓存的执行计划
     */
    Optional<ExecutionPlan> getCachedExecutionPlan(Long workflowId);

    /**
     * 清除工作流相关缓存
     */
    void clearWorkflowCache(Long workflowId);

    /**
     * 清除节点相关缓存
     */
    void clearNodeCache(String nodeId);

    /**
     * 清除所有缓存
     */
    void clearAllCache();

    /**
     * 获取缓存统计信息
     */
    CacheStatistics getCacheStatistics();

    /**
     * 工作流定义缓存
     */
    class WorkflowDefinitionCache {
        private Long workflowId;
        private String name;
        private String description;
        private Map<String, Object> nodes;
        private Map<String, Object> connections;
        private Map<String, Object> metadata;
        private long cacheTime;

        // Constructors, getters and setters
        public WorkflowDefinitionCache() {}

        public WorkflowDefinitionCache(Long workflowId, String name, String description, 
                                     Map<String, Object> nodes, Map<String, Object> connections, 
                                     Map<String, Object> metadata) {
            this.workflowId = workflowId;
            this.name = name;
            this.description = description;
            this.nodes = nodes;
            this.connections = connections;
            this.metadata = metadata;
            this.cacheTime = System.currentTimeMillis();
        }

        public Long getWorkflowId() { return workflowId; }
        public void setWorkflowId(Long workflowId) { this.workflowId = workflowId; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public Map<String, Object> getNodes() { return nodes; }
        public void setNodes(Map<String, Object> nodes) { this.nodes = nodes; }
        
        public Map<String, Object> getConnections() { return connections; }
        public void setConnections(Map<String, Object> connections) { this.connections = connections; }
        
        public Map<String, Object> getMetadata() { return metadata; }
        public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
        
        public long getCacheTime() { return cacheTime; }
        public void setCacheTime(long cacheTime) { this.cacheTime = cacheTime; }
    }

    /**
     * 执行计划
     */
    class ExecutionPlan {
        private Long workflowId;
        private String[] executionOrder;
        private Map<String, String[]> parallelGroups;
        private Map<String, Object> optimizations;
        private long planTime;

        // Constructors, getters and setters
        public ExecutionPlan() {}

        public ExecutionPlan(Long workflowId, String[] executionOrder, 
                           Map<String, String[]> parallelGroups, Map<String, Object> optimizations) {
            this.workflowId = workflowId;
            this.executionOrder = executionOrder;
            this.parallelGroups = parallelGroups;
            this.optimizations = optimizations;
            this.planTime = System.currentTimeMillis();
        }

        public Long getWorkflowId() { return workflowId; }
        public void setWorkflowId(Long workflowId) { this.workflowId = workflowId; }
        
        public String[] getExecutionOrder() { return executionOrder; }
        public void setExecutionOrder(String[] executionOrder) { this.executionOrder = executionOrder; }
        
        public Map<String, String[]> getParallelGroups() { return parallelGroups; }
        public void setParallelGroups(Map<String, String[]> parallelGroups) { this.parallelGroups = parallelGroups; }
        
        public Map<String, Object> getOptimizations() { return optimizations; }
        public void setOptimizations(Map<String, Object> optimizations) { this.optimizations = optimizations; }
        
        public long getPlanTime() { return planTime; }
        public void setPlanTime(long planTime) { this.planTime = planTime; }
    }

    /**
     * 缓存统计信息
     */
    class CacheStatistics {
        private long totalCacheSize;
        private long nodeResultCacheSize;
        private long workflowDefinitionCacheSize;
        private long executionPlanCacheSize;
        private double hitRate;
        private long totalHits;
        private long totalMisses;
        private long evictionCount;

        // Constructors, getters and setters
        public CacheStatistics() {}

        public long getTotalCacheSize() { return totalCacheSize; }
        public void setTotalCacheSize(long totalCacheSize) { this.totalCacheSize = totalCacheSize; }
        
        public long getNodeResultCacheSize() { return nodeResultCacheSize; }
        public void setNodeResultCacheSize(long nodeResultCacheSize) { this.nodeResultCacheSize = nodeResultCacheSize; }
        
        public long getWorkflowDefinitionCacheSize() { return workflowDefinitionCacheSize; }
        public void setWorkflowDefinitionCacheSize(long workflowDefinitionCacheSize) { this.workflowDefinitionCacheSize = workflowDefinitionCacheSize; }
        
        public long getExecutionPlanCacheSize() { return executionPlanCacheSize; }
        public void setExecutionPlanCacheSize(long executionPlanCacheSize) { this.executionPlanCacheSize = executionPlanCacheSize; }
        
        public double getHitRate() { return hitRate; }
        public void setHitRate(double hitRate) { this.hitRate = hitRate; }
        
        public long getTotalHits() { return totalHits; }
        public void setTotalHits(long totalHits) { this.totalHits = totalHits; }
        
        public long getTotalMisses() { return totalMisses; }
        public void setTotalMisses(long totalMisses) { this.totalMisses = totalMisses; }
        
        public long getEvictionCount() { return evictionCount; }
        public void setEvictionCount(long evictionCount) { this.evictionCount = evictionCount; }
    }
}
