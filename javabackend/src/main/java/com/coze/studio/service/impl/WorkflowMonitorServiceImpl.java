package com.coze.studio.service.impl;

import com.coze.studio.entity.WorkflowExecution;
import com.coze.studio.entity.WorkflowNodeExecution;
import com.coze.studio.repository.WorkflowExecutionRepository;
import com.coze.studio.repository.WorkflowNodeExecutionRepository;
import com.coze.studio.service.WorkflowMonitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class WorkflowMonitorServiceImpl implements WorkflowMonitorService {

    @Autowired
    private WorkflowExecutionRepository workflowExecutionRepository;

    @Autowired
    private WorkflowNodeExecutionRepository workflowNodeExecutionRepository;

    // 内存中的执行状态缓存和订阅管理
    private final Map<String, WorkflowExecutionStatus> executionStatusCache = new ConcurrentHashMap<>();
    private final Map<String, List<ExecutionUpdateCallback>> subscriptions = new ConcurrentHashMap<>();

    @Override
    public WorkflowExecutionStatus getExecutionStatus(String executionId) {
        log.debug("获取工作流执行状态: executionId={}", executionId);

        // 先从缓存获取
        WorkflowExecutionStatus cachedStatus = executionStatusCache.get(executionId);
        if (cachedStatus != null) {
            return cachedStatus;
        }

        // 从数据库获取
        Optional<WorkflowExecution> executionOpt = workflowExecutionRepository.findByExecutionId(executionId);
        if (executionOpt.isPresent()) {
            WorkflowExecution execution = executionOpt.get();
            WorkflowExecutionStatus status = convertToStatus(execution);

            // 缓存状态
            executionStatusCache.put(executionId, status);
            return status;
        }

        return null;
    }

    @Override
    public List<ExecutionLogEntry> getExecutionLogs(String executionId, int limit) {
        log.debug("获取执行日志: executionId={}, limit={}", executionId, limit);

        // 首先获取 WorkflowExecution 来获取数据库中的 ID
        Optional<WorkflowExecution> executionOpt = workflowExecutionRepository.findByExecutionId(executionId);
        if (!executionOpt.isPresent()) {
            return new ArrayList<>();
        }

        Long dbExecutionId = executionOpt.get().getId();

        // 使用数据库中的执行ID查找节点执行记录，按创建时间倒序排序
        List<WorkflowNodeExecution> nodeExecutions = workflowNodeExecutionRepository
                .findByExecutionIdOrderByCreatedAtDesc(dbExecutionId);

        List<ExecutionLogEntry> logs = new ArrayList<>();
        int count = 0;
        for (WorkflowNodeExecution nodeExecution : nodeExecutions) {
            if (count >= limit) break;

            // 使用接口中定义的内部类构造函数
            ExecutionLogEntry logEntry = new ExecutionLogEntry(
                executionId,
                nodeExecution.getNodeId(),
                "INFO",
                "节点执行: " + nodeExecution.getStatus(),
                nodeExecution.getStartTime(),
                Map.of(
                    "nodeId", nodeExecution.getNodeId(),
                    "status", nodeExecution.getStatus(),
                    "duration", nodeExecution.getDuration()
                )
            );

            logs.add(logEntry);
            count++;
        }

        return logs;
    }

    @Override
    public ExecutionMetrics getExecutionMetrics(String executionId) {
        log.debug("获取执行性能指标: executionId={}", executionId);

        Optional<WorkflowExecution> executionOpt = workflowExecutionRepository.findByExecutionId(executionId);
        if (!executionOpt.isPresent()) {
            return null;
        }

        WorkflowExecution execution = executionOpt.get();
        Long dbExecutionId = execution.getId();

        // 使用数据库中的执行ID查找节点执行记录
        List<WorkflowNodeExecution> nodeExecutions = workflowNodeExecutionRepository
                .findByExecutionIdOrderByCreatedAtDesc(dbExecutionId);

        ExecutionMetrics metrics = new ExecutionMetrics();
        metrics.setExecutionId(executionId);
        metrics.setTotalExecutionTime(execution.getDuration() != null ? execution.getDuration() : 0L);

        Map<String, Long> nodeExecutionTimes = new HashMap<>();
        Map<String, Integer> nodeExecutionCounts = new HashMap<>();
        long totalNodeTime = 0;

        for (WorkflowNodeExecution nodeExecution : nodeExecutions) {
            String nodeId = nodeExecution.getNodeId();
            Long duration = nodeExecution.getDuration();

            if (duration != null) {
                nodeExecutionTimes.put(nodeId, duration);
                totalNodeTime += duration;
            }

            nodeExecutionCounts.merge(nodeId, 1, Integer::sum);
        }

        metrics.setNodeExecutionTimes(nodeExecutionTimes);
        metrics.setNodeExecutionCounts(nodeExecutionCounts);
        metrics.setAverageNodeTime(nodeExecutions.isEmpty() ? 0 : (double) totalNodeTime / nodeExecutions.size());

        return metrics;
    }

    @Override
    public List<WorkflowExecutionStatus> getActiveExecutions() {
        log.debug("获取活跃执行列表");

        List<WorkflowExecution> activeExecutions = workflowExecutionRepository
                .findByStatusIn(Arrays.asList("RUNNING", "PAUSED"));

        return activeExecutions.stream()
                .map(this::convertToStatus)
                .toList();
    }

    @Override
    public void subscribeExecutionUpdates(String executionId, ExecutionUpdateCallback callback) {
        log.debug("订阅执行状态更新: executionId={}", executionId);

        subscriptions.computeIfAbsent(executionId, k -> new ArrayList<>()).add(callback);
    }

    @Override
    public void unsubscribeExecutionUpdates(String executionId, ExecutionUpdateCallback callback) {
        log.debug("取消订阅执行状态更新: executionId={}", executionId);

        List<ExecutionUpdateCallback> callbacks = subscriptions.get(executionId);
        if (callbacks != null) {
            callbacks.remove(callback);
            if (callbacks.isEmpty()) {
                subscriptions.remove(executionId);
            }
        }
    }

    @Override
    public void recordExecutionEvent(String executionId, ExecutionEvent event) {
        log.debug("记录执行事件: executionId={}, eventType={}", executionId, event.getType());

        // 更新缓存中的状态
        WorkflowExecutionStatus status = executionStatusCache.get(executionId);
        if (status != null) {
            status.setLastUpdateTime(event.getTimestamp());

            // 根据事件类型更新状态
            switch (event.getType()) {
                case "NODE_START":
                    status.setCurrentNodeId(event.getNodeId());
                    break;
                case "NODE_COMPLETE":
                    // 可以更新进度等
                    break;
                case "WORKFLOW_COMPLETE":
                    status.setStatus("COMPLETED");
                    break;
                case "WORKFLOW_ERROR":
                    status.setStatus("FAILED");
                    status.setErrorMessage(event.getMessage());
                    break;
            }
        }

        // 通知订阅者
        List<ExecutionUpdateCallback> callbacks = subscriptions.get(executionId);
        if (callbacks != null) {
            for (ExecutionUpdateCallback callback : callbacks) {
                try {
                    if (status != null) {
                        callback.onStatusUpdate(status);
                    }
                } catch (Exception e) {
                    log.error("执行状态更新回调失败: executionId={}", executionId, e);
                }
            }
        }
    }

    /**
     * 将 WorkflowExecution 转换为 WorkflowExecutionStatus
     */
    private WorkflowExecutionStatus convertToStatus(WorkflowExecution execution) {
        WorkflowExecutionStatus status = new WorkflowExecutionStatus();
        status.setExecutionId(execution.getExecutionId());
        status.setWorkflowId(execution.getWorkflowId());
        status.setStatus(execution.getStatus());
        status.setStartTime(execution.getStartTime());
        status.setLastUpdateTime(execution.getUpdatedAt());
        status.setDuration(execution.getDuration());
        status.setErrorMessage(execution.getErrorMessage());

        // 计算进度 - 基于已完成的节点数
        List<WorkflowNodeExecution> nodeExecutions = workflowNodeExecutionRepository
                .findByExecutionIdOrderByCreatedAtDesc(execution.getId());

        int totalNodes = nodeExecutions.size();
        int completedNodes = (int) nodeExecutions.stream()
                .filter(ne -> "COMPLETED".equals(ne.getStatus()))
                .count();

        status.setTotalNodes(totalNodes);
        status.setExecutedNodes(completedNodes);
        status.setProgress(totalNodes > 0 ? (double) completedNodes / totalNodes : 0.0);

        return status;
    }
}