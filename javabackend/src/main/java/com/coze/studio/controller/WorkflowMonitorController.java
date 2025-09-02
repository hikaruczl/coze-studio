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
import com.coze.studio.security.UserPrincipal;
import com.coze.studio.service.WorkflowMonitorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 工作流监控控制器
 * 提供工作流执行监控和调试功能
 * 
 * @author coze-dev
 */
@Tag(name = "工作流监控", description = "工作流执行监控和调试相关接口")
@RestController
@RequestMapping("/api/workflows/monitor")
@RequiredArgsConstructor
public class WorkflowMonitorController {

    private final WorkflowMonitorService workflowMonitorService;

    /**
     * 获取工作流执行状态
     */
    @Operation(summary = "获取执行状态", description = "获取指定工作流执行的实时状态")
    @GetMapping("/executions/{executionId}/status")
    public ResponseEntity<ApiResponse<WorkflowMonitorService.WorkflowExecutionStatus>> getExecutionStatus(
            @PathVariable String executionId) {
        WorkflowMonitorService.WorkflowExecutionStatus status = workflowMonitorService.getExecutionStatus(executionId);
        return ResponseEntity.ok(ApiResponse.success(status));
    }

    /**
     * 获取执行日志
     */
    @Operation(summary = "获取执行日志", description = "获取指定工作流执行的详细日志")
    @GetMapping("/executions/{executionId}/logs")
    public ResponseEntity<ApiResponse<List<WorkflowMonitorService.ExecutionLogEntry>>> getExecutionLogs(
            @PathVariable String executionId,
            @RequestParam(defaultValue = "100") int limit) {
        List<WorkflowMonitorService.ExecutionLogEntry> logs = workflowMonitorService.getExecutionLogs(executionId, limit);
        return ResponseEntity.ok(ApiResponse.success(logs));
    }

    /**
     * 获取执行性能指标
     */
    @Operation(summary = "获取性能指标", description = "获取指定工作流执行的性能指标")
    @GetMapping("/executions/{executionId}/metrics")
    public ResponseEntity<ApiResponse<WorkflowMonitorService.ExecutionMetrics>> getExecutionMetrics(
            @PathVariable String executionId) {
        WorkflowMonitorService.ExecutionMetrics metrics = workflowMonitorService.getExecutionMetrics(executionId);
        return ResponseEntity.ok(ApiResponse.success(metrics));
    }

    /**
     * 获取活跃执行列表
     */
    @Operation(summary = "获取活跃执行", description = "获取当前正在执行的工作流列表")
    @GetMapping("/executions/active")
    public ResponseEntity<ApiResponse<List<WorkflowMonitorService.WorkflowExecutionStatus>>> getActiveExecutions(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<WorkflowMonitorService.WorkflowExecutionStatus> activeExecutions = workflowMonitorService.getActiveExecutions();
        return ResponseEntity.ok(ApiResponse.success(activeExecutions));
    }

    /**
     * 获取系统监控概览
     */
    @Operation(summary = "获取系统概览", description = "获取工作流系统的整体监控概览")
    @GetMapping("/overview")
    public ResponseEntity<ApiResponse<SystemOverview>> getSystemOverview(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        List<WorkflowMonitorService.WorkflowExecutionStatus> activeExecutions = workflowMonitorService.getActiveExecutions();
        
        SystemOverview overview = new SystemOverview();
        overview.setActiveExecutions(activeExecutions.size());
        overview.setRunningExecutions((int) activeExecutions.stream().filter(e -> "RUNNING".equals(e.getStatus())).count());
        overview.setPausedExecutions((int) activeExecutions.stream().filter(e -> "PAUSED".equals(e.getStatus())).count());
        
        // 计算系统资源使用情况
        Runtime runtime = Runtime.getRuntime();
        overview.setMemoryUsage(runtime.totalMemory() - runtime.freeMemory());
        overview.setMaxMemory(runtime.maxMemory());
        overview.setMemoryUsagePercentage((double) overview.getMemoryUsage() / overview.getMaxMemory() * 100);
        
        return ResponseEntity.ok(ApiResponse.success(overview));
    }

    /**
     * 系统监控概览
     */
    public static class SystemOverview {
        private int activeExecutions;
        private int runningExecutions;
        private int pausedExecutions;
        private long memoryUsage;
        private long maxMemory;
        private double memoryUsagePercentage;

        // Getters and setters
        public int getActiveExecutions() { return activeExecutions; }
        public void setActiveExecutions(int activeExecutions) { this.activeExecutions = activeExecutions; }
        
        public int getRunningExecutions() { return runningExecutions; }
        public void setRunningExecutions(int runningExecutions) { this.runningExecutions = runningExecutions; }
        
        public int getPausedExecutions() { return pausedExecutions; }
        public void setPausedExecutions(int pausedExecutions) { this.pausedExecutions = pausedExecutions; }
        
        public long getMemoryUsage() { return memoryUsage; }
        public void setMemoryUsage(long memoryUsage) { this.memoryUsage = memoryUsage; }
        
        public long getMaxMemory() { return maxMemory; }
        public void setMaxMemory(long maxMemory) { this.maxMemory = maxMemory; }
        
        public double getMemoryUsagePercentage() { return memoryUsagePercentage; }
        public void setMemoryUsagePercentage(double memoryUsagePercentage) { this.memoryUsagePercentage = memoryUsagePercentage; }
    }
}
