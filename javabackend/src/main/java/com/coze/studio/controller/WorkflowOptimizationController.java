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
import com.coze.studio.service.WorkflowOptimizationService;
import com.coze.studio.service.WorkflowOptimizationService.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 工作流优化控制器
 * 提供工作流性能分析和优化相关接口
 * 
 * @author coze-dev
 */
@Tag(name = "工作流优化", description = "工作流性能分析和优化相关接口")
@RestController
@RequestMapping("/api/workflows/optimization")
@RequiredArgsConstructor
public class WorkflowOptimizationController {

    private final WorkflowOptimizationService workflowOptimizationService;

    /**
     * 分析工作流性能
     */
    @Operation(summary = "分析工作流性能", description = "获取指定工作流的性能分析报告")
    @GetMapping("/{workflowId}/performance")
    public ResponseEntity<ApiResponse<WorkflowPerformanceAnalysis>> analyzeWorkflowPerformance(
            @PathVariable Long workflowId) {
        WorkflowPerformanceAnalysis analysis = workflowOptimizationService.analyzeWorkflowPerformance(workflowId);
        return ResponseEntity.ok(ApiResponse.success(analysis));
    }

    /**
     * 获取优化建议
     */
    @Operation(summary = "获取优化建议", description = "获取工作流的性能优化建议")
    @GetMapping("/{workflowId}/suggestions")
    public ResponseEntity<ApiResponse<List<OptimizationSuggestion>>> getOptimizationSuggestions(
            @PathVariable Long workflowId) {
        List<OptimizationSuggestion> suggestions = workflowOptimizationService.getOptimizationSuggestions(workflowId);
        return ResponseEntity.ok(ApiResponse.success(suggestions));
    }

    /**
     * 应用优化建议
     */
    @Operation(summary = "应用优化建议", description = "应用指定的优化建议到工作流")
    @PostMapping("/{workflowId}/apply")
    public ResponseEntity<ApiResponse<WorkflowOptimizationResult>> applyOptimization(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long workflowId,
            @Parameter(description = "优化类型") @RequestParam String optimizationType,
            @RequestBody(required = false) Map<String, Object> parameters) {
        
        WorkflowOptimizationResult result = workflowOptimizationService.applyOptimization(
                workflowId, optimizationType, parameters);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    /**
     * 预测执行时间
     */
    @Operation(summary = "预测执行时间", description = "基于历史数据预测工作流执行时间")
    @PostMapping("/{workflowId}/predict-time")
    public ResponseEntity<ApiResponse<ExecutionTimePrediction>> predictExecutionTime(
            @PathVariable Long workflowId,
            @RequestBody(required = false) Map<String, Object> inputData) {
        
        ExecutionTimePrediction prediction = workflowOptimizationService.predictExecutionTime(workflowId, inputData);
        return ResponseEntity.ok(ApiResponse.success(prediction));
    }

    /**
     * 获取性能趋势
     */
    @Operation(summary = "获取性能趋势", description = "获取工作流的性能趋势数据")
    @GetMapping("/{workflowId}/trends")
    public ResponseEntity<ApiResponse<PerformanceTrends>> getPerformanceTrends(
            @PathVariable Long workflowId,
            @Parameter(description = "时间范围(天)") @RequestParam(defaultValue = "30") int days) {
        
        PerformanceTrends trends = getPerformanceTrendsData(workflowId, days);
        return ResponseEntity.ok(ApiResponse.success(trends));
    }

    /**
     * 批量分析多个工作流
     */
    @Operation(summary = "批量性能分析", description = "批量分析多个工作流的性能")
    @PostMapping("/batch-analyze")
    public ResponseEntity<ApiResponse<List<WorkflowPerformanceAnalysis>>> batchAnalyzeWorkflows(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody List<Long> workflowIds) {
        
        List<WorkflowPerformanceAnalysis> analyses = workflowIds.stream()
                .map(workflowOptimizationService::analyzeWorkflowPerformance)
                .toList();
        
        return ResponseEntity.ok(ApiResponse.success(analyses));
    }

    /**
     * 获取系统性能概览
     */
    @Operation(summary = "获取系统性能概览", description = "获取整个系统的性能概览")
    @GetMapping("/system-overview")
    public ResponseEntity<ApiResponse<SystemPerformanceOverview>> getSystemPerformanceOverview(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        SystemPerformanceOverview overview = generateSystemOverview();
        return ResponseEntity.ok(ApiResponse.success(overview));
    }

    /**
     * 获取性能趋势数据
     */
    private PerformanceTrends getPerformanceTrendsData(Long workflowId, int days) {
        // TODO: 实现性能趋势数据获取逻辑
        PerformanceTrends trends = new PerformanceTrends();
        trends.setWorkflowId(workflowId);
        trends.setDays(days);
        
        // 模拟趋势数据
        trends.setExecutionTimes(generateMockExecutionTimes(days));
        trends.setSuccessRates(generateMockSuccessRates(days));
        trends.setThroughput(generateMockThroughput(days));
        
        return trends;
    }

    /**
     * 生成系统性能概览
     */
    private SystemPerformanceOverview generateSystemOverview() {
        // TODO: 实现系统性能概览生成逻辑
        SystemPerformanceOverview overview = new SystemPerformanceOverview();
        overview.setTotalWorkflows(100);
        overview.setActiveWorkflows(25);
        overview.setAverageExecutionTime(5.2);
        overview.setSystemSuccessRate(0.96);
        overview.setDailyExecutions(1250);
        overview.setResourceUtilization(0.68);
        
        return overview;
    }

    /**
     * 生成模拟执行时间数据
     */
    private Map<String, Double> generateMockExecutionTimes(int days) {
        // TODO: 实现真实的执行时间趋势数据
        return Map.of(
                "day_1", 5.2,
                "day_7", 4.8,
                "day_14", 5.1,
                "day_30", 4.9
        );
    }

    /**
     * 生成模拟成功率数据
     */
    private Map<String, Double> generateMockSuccessRates(int days) {
        // TODO: 实现真实的成功率趋势数据
        return Map.of(
                "day_1", 0.96,
                "day_7", 0.94,
                "day_14", 0.97,
                "day_30", 0.95
        );
    }

    /**
     * 生成模拟吞吐量数据
     */
    private Map<String, Integer> generateMockThroughput(int days) {
        // TODO: 实现真实的吞吐量趋势数据
        return Map.of(
                "day_1", 1250,
                "day_7", 1180,
                "day_14", 1320,
                "day_30", 1290
        );
    }

    /**
     * 性能趋势数据
     */
    public static class PerformanceTrends {
        private Long workflowId;
        private int days;
        private Map<String, Double> executionTimes;
        private Map<String, Double> successRates;
        private Map<String, Integer> throughput;

        // Getters and setters
        public Long getWorkflowId() { return workflowId; }
        public void setWorkflowId(Long workflowId) { this.workflowId = workflowId; }
        
        public int getDays() { return days; }
        public void setDays(int days) { this.days = days; }
        
        public Map<String, Double> getExecutionTimes() { return executionTimes; }
        public void setExecutionTimes(Map<String, Double> executionTimes) { this.executionTimes = executionTimes; }
        
        public Map<String, Double> getSuccessRates() { return successRates; }
        public void setSuccessRates(Map<String, Double> successRates) { this.successRates = successRates; }
        
        public Map<String, Integer> getThroughput() { return throughput; }
        public void setThroughput(Map<String, Integer> throughput) { this.throughput = throughput; }
    }

    /**
     * 系统性能概览
     */
    public static class SystemPerformanceOverview {
        private int totalWorkflows;
        private int activeWorkflows;
        private double averageExecutionTime;
        private double systemSuccessRate;
        private int dailyExecutions;
        private double resourceUtilization;

        // Getters and setters
        public int getTotalWorkflows() { return totalWorkflows; }
        public void setTotalWorkflows(int totalWorkflows) { this.totalWorkflows = totalWorkflows; }
        
        public int getActiveWorkflows() { return activeWorkflows; }
        public void setActiveWorkflows(int activeWorkflows) { this.activeWorkflows = activeWorkflows; }
        
        public double getAverageExecutionTime() { return averageExecutionTime; }
        public void setAverageExecutionTime(double averageExecutionTime) { this.averageExecutionTime = averageExecutionTime; }
        
        public double getSystemSuccessRate() { return systemSuccessRate; }
        public void setSystemSuccessRate(double systemSuccessRate) { this.systemSuccessRate = systemSuccessRate; }
        
        public int getDailyExecutions() { return dailyExecutions; }
        public void setDailyExecutions(int dailyExecutions) { this.dailyExecutions = dailyExecutions; }
        
        public double getResourceUtilization() { return resourceUtilization; }
        public void setResourceUtilization(double resourceUtilization) { this.resourceUtilization = resourceUtilization; }
    }
}
