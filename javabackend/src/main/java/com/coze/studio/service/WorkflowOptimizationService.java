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

import java.util.List;
import java.util.Map;

/**
 * 工作流优化服务接口
 * 
 * @author coze-dev
 */
public interface WorkflowOptimizationService {

    /**
     * 分析工作流性能
     */
    WorkflowPerformanceAnalysis analyzeWorkflowPerformance(Long workflowId);

    /**
     * 获取优化建议
     */
    List<OptimizationSuggestion> getOptimizationSuggestions(Long workflowId);

    /**
     * 应用优化建议
     */
    WorkflowOptimizationResult applyOptimization(Long workflowId, String optimizationType, Map<String, Object> parameters);

    /**
     * 预测执行时间
     */
    ExecutionTimePrediction predictExecutionTime(Long workflowId, Map<String, Object> inputData);

    /**
     * 工作流性能分析结果
     */
    class WorkflowPerformanceAnalysis {
        private Long workflowId;
        private double averageExecutionTime;
        private double medianExecutionTime;
        private double p95ExecutionTime;
        private double successRate;
        private Map<String, Double> nodePerformance;
        private List<String> bottleneckNodes;
        private Map<String, Object> resourceUsage;
        private List<String> performanceIssues;

        // Constructors, getters and setters
        public WorkflowPerformanceAnalysis() {}

        public Long getWorkflowId() { return workflowId; }
        public void setWorkflowId(Long workflowId) { this.workflowId = workflowId; }
        
        public double getAverageExecutionTime() { return averageExecutionTime; }
        public void setAverageExecutionTime(double averageExecutionTime) { this.averageExecutionTime = averageExecutionTime; }
        
        public double getMedianExecutionTime() { return medianExecutionTime; }
        public void setMedianExecutionTime(double medianExecutionTime) { this.medianExecutionTime = medianExecutionTime; }
        
        public double getP95ExecutionTime() { return p95ExecutionTime; }
        public void setP95ExecutionTime(double p95ExecutionTime) { this.p95ExecutionTime = p95ExecutionTime; }
        
        public double getSuccessRate() { return successRate; }
        public void setSuccessRate(double successRate) { this.successRate = successRate; }
        
        public Map<String, Double> getNodePerformance() { return nodePerformance; }
        public void setNodePerformance(Map<String, Double> nodePerformance) { this.nodePerformance = nodePerformance; }
        
        public List<String> getBottleneckNodes() { return bottleneckNodes; }
        public void setBottleneckNodes(List<String> bottleneckNodes) { this.bottleneckNodes = bottleneckNodes; }
        
        public Map<String, Object> getResourceUsage() { return resourceUsage; }
        public void setResourceUsage(Map<String, Object> resourceUsage) { this.resourceUsage = resourceUsage; }
        
        public List<String> getPerformanceIssues() { return performanceIssues; }
        public void setPerformanceIssues(List<String> performanceIssues) { this.performanceIssues = performanceIssues; }
    }

    /**
     * 优化建议
     */
    class OptimizationSuggestion {
        private String type;
        private String title;
        private String description;
        private String severity;
        private double expectedImprovement;
        private Map<String, Object> parameters;
        private boolean autoApplicable;

        public OptimizationSuggestion(String type, String title, String description, String severity, 
                                    double expectedImprovement, Map<String, Object> parameters, boolean autoApplicable) {
            this.type = type;
            this.title = title;
            this.description = description;
            this.severity = severity;
            this.expectedImprovement = expectedImprovement;
            this.parameters = parameters;
            this.autoApplicable = autoApplicable;
        }

        // Getters
        public String getType() { return type; }
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getSeverity() { return severity; }
        public double getExpectedImprovement() { return expectedImprovement; }
        public Map<String, Object> getParameters() { return parameters; }
        public boolean isAutoApplicable() { return autoApplicable; }
    }

    /**
     * 优化结果
     */
    class WorkflowOptimizationResult {
        private boolean success;
        private String optimizationType;
        private String description;
        private Map<String, Object> changes;
        private double estimatedImprovement;
        private String error;

        public WorkflowOptimizationResult(boolean success, String optimizationType, String description, 
                                        Map<String, Object> changes, double estimatedImprovement, String error) {
            this.success = success;
            this.optimizationType = optimizationType;
            this.description = description;
            this.changes = changes;
            this.estimatedImprovement = estimatedImprovement;
            this.error = error;
        }

        // Getters
        public boolean isSuccess() { return success; }
        public String getOptimizationType() { return optimizationType; }
        public String getDescription() { return description; }
        public Map<String, Object> getChanges() { return changes; }
        public double getEstimatedImprovement() { return estimatedImprovement; }
        public String getError() { return error; }
    }

    /**
     * 执行时间预测
     */
    class ExecutionTimePrediction {
        private Long workflowId;
        private double predictedTime;
        private double confidence;
        private Map<String, Double> nodeTimePredictions;
        private String predictionModel;
        private Map<String, Object> factors;

        // Constructors, getters and setters
        public ExecutionTimePrediction() {}

        public Long getWorkflowId() { return workflowId; }
        public void setWorkflowId(Long workflowId) { this.workflowId = workflowId; }
        
        public double getPredictedTime() { return predictedTime; }
        public void setPredictedTime(double predictedTime) { this.predictedTime = predictedTime; }
        
        public double getConfidence() { return confidence; }
        public void setConfidence(double confidence) { this.confidence = confidence; }
        
        public Map<String, Double> getNodeTimePredictions() { return nodeTimePredictions; }
        public void setNodeTimePredictions(Map<String, Double> nodeTimePredictions) { this.nodeTimePredictions = nodeTimePredictions; }
        
        public String getPredictionModel() { return predictionModel; }
        public void setPredictionModel(String predictionModel) { this.predictionModel = predictionModel; }
        
        public Map<String, Object> getFactors() { return factors; }
        public void setFactors(Map<String, Object> factors) { this.factors = factors; }
    }
}
