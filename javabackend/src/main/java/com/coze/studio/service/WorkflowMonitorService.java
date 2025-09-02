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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 工作流监控服务接口
 * 
 * @author coze-dev
 */
public interface WorkflowMonitorService {

    /**
     * 获取工作流执行状态
     */
    WorkflowExecutionStatus getExecutionStatus(String executionId);

    /**
     * 获取实时执行日志
     */
    List<ExecutionLogEntry> getExecutionLogs(String executionId, int limit);

    /**
     * 获取执行性能指标
     */
    ExecutionMetrics getExecutionMetrics(String executionId);

    /**
     * 获取活跃执行列表
     */
    List<WorkflowExecutionStatus> getActiveExecutions();

    /**
     * 订阅执行状态变更
     */
    void subscribeExecutionUpdates(String executionId, ExecutionUpdateCallback callback);

    /**
     * 取消订阅执行状态变更
     */
    void unsubscribeExecutionUpdates(String executionId, ExecutionUpdateCallback callback);

    /**
     * 记录执行事件
     */
    void recordExecutionEvent(String executionId, ExecutionEvent event);

    /**
     * 工作流执行状态
     */
    class WorkflowExecutionStatus {
        private String executionId;
        private Long workflowId;
        private String status;
        private String currentNodeId;
        private int executedNodes;
        private int totalNodes;
        private double progress;
        private LocalDateTime startTime;
        private LocalDateTime lastUpdateTime;
        private Long duration;
        private Map<String, Object> currentData;
        private String errorMessage;

        // Constructors, getters and setters
        public WorkflowExecutionStatus() {}

        public String getExecutionId() { return executionId; }
        public void setExecutionId(String executionId) { this.executionId = executionId; }
        
        public Long getWorkflowId() { return workflowId; }
        public void setWorkflowId(Long workflowId) { this.workflowId = workflowId; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public String getCurrentNodeId() { return currentNodeId; }
        public void setCurrentNodeId(String currentNodeId) { this.currentNodeId = currentNodeId; }
        
        public int getExecutedNodes() { return executedNodes; }
        public void setExecutedNodes(int executedNodes) { this.executedNodes = executedNodes; }
        
        public int getTotalNodes() { return totalNodes; }
        public void setTotalNodes(int totalNodes) { this.totalNodes = totalNodes; }
        
        public double getProgress() { return progress; }
        public void setProgress(double progress) { this.progress = progress; }
        
        public LocalDateTime getStartTime() { return startTime; }
        public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
        
        public LocalDateTime getLastUpdateTime() { return lastUpdateTime; }
        public void setLastUpdateTime(LocalDateTime lastUpdateTime) { this.lastUpdateTime = lastUpdateTime; }
        
        public Long getDuration() { return duration; }
        public void setDuration(Long duration) { this.duration = duration; }
        
        public Map<String, Object> getCurrentData() { return currentData; }
        public void setCurrentData(Map<String, Object> currentData) { this.currentData = currentData; }
        
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    }

    /**
     * 执行日志条目
     */
    class ExecutionLogEntry {
        private String executionId;
        private String nodeId;
        private String level;
        private String message;
        private LocalDateTime timestamp;
        private Map<String, Object> data;

        public ExecutionLogEntry(String executionId, String nodeId, String level, String message, 
                               LocalDateTime timestamp, Map<String, Object> data) {
            this.executionId = executionId;
            this.nodeId = nodeId;
            this.level = level;
            this.message = message;
            this.timestamp = timestamp;
            this.data = data;
        }

        // Getters
        public String getExecutionId() { return executionId; }
        public String getNodeId() { return nodeId; }
        public String getLevel() { return level; }
        public String getMessage() { return message; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public Map<String, Object> getData() { return data; }
    }

    /**
     * 执行性能指标
     */
    class ExecutionMetrics {
        private String executionId;
        private long totalExecutionTime;
        private Map<String, Long> nodeExecutionTimes;
        private Map<String, Integer> nodeExecutionCounts;
        private long memoryUsage;
        private int parallelBranches;
        private double averageNodeTime;

        // Constructors, getters and setters
        public ExecutionMetrics() {}

        public String getExecutionId() { return executionId; }
        public void setExecutionId(String executionId) { this.executionId = executionId; }
        
        public long getTotalExecutionTime() { return totalExecutionTime; }
        public void setTotalExecutionTime(long totalExecutionTime) { this.totalExecutionTime = totalExecutionTime; }
        
        public Map<String, Long> getNodeExecutionTimes() { return nodeExecutionTimes; }
        public void setNodeExecutionTimes(Map<String, Long> nodeExecutionTimes) { this.nodeExecutionTimes = nodeExecutionTimes; }
        
        public Map<String, Integer> getNodeExecutionCounts() { return nodeExecutionCounts; }
        public void setNodeExecutionCounts(Map<String, Integer> nodeExecutionCounts) { this.nodeExecutionCounts = nodeExecutionCounts; }
        
        public long getMemoryUsage() { return memoryUsage; }
        public void setMemoryUsage(long memoryUsage) { this.memoryUsage = memoryUsage; }
        
        public int getParallelBranches() { return parallelBranches; }
        public void setParallelBranches(int parallelBranches) { this.parallelBranches = parallelBranches; }
        
        public double getAverageNodeTime() { return averageNodeTime; }
        public void setAverageNodeTime(double averageNodeTime) { this.averageNodeTime = averageNodeTime; }
    }

    /**
     * 执行事件
     */
    class ExecutionEvent {
        private String type;
        private String nodeId;
        private String message;
        private Map<String, Object> data;
        private LocalDateTime timestamp;

        public ExecutionEvent(String type, String nodeId, String message, Map<String, Object> data) {
            this.type = type;
            this.nodeId = nodeId;
            this.message = message;
            this.data = data;
            this.timestamp = LocalDateTime.now();
        }

        // Getters
        public String getType() { return type; }
        public String getNodeId() { return nodeId; }
        public String getMessage() { return message; }
        public Map<String, Object> getData() { return data; }
        public LocalDateTime getTimestamp() { return timestamp; }
    }

    /**
     * 执行更新回调接口
     */
    interface ExecutionUpdateCallback {
        void onStatusUpdate(WorkflowExecutionStatus status);
        void onLogUpdate(ExecutionLogEntry logEntry);
        void onMetricsUpdate(ExecutionMetrics metrics);
        void onError(String executionId, Exception error);
    }
}
