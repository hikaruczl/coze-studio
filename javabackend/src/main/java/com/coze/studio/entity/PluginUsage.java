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

package com.coze.studio.entity;

// JPA注解已移除，改用MyBatis
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 插件使用记录实体
 * 
 * @author coze-dev
 */
@Data
@EqualsAndHashCode(callSuper = true)
// 表名: plugin_usage
// 索引信息已移至数据库DDL脚本
public class PluginUsage extends BaseEntity {

    /**
     * 用户ID
     */
        private Long userId;

    /**
     * 插件ID
     */
        private Long pluginId;

    /**
     * 工作流ID（如果在工作流中使用）
     */
        private Long workflowId;

    /**
     * 工作流节点ID（如果在工作流节点中使用）
     */
        private String nodeId;

    /**
     * 调用的API端点
     */
        private String apiEndpoint;

    /**
     * HTTP方法
     */
        private String httpMethod;

    /**
     * 请求参数（JSON格式）
     */
        private String requestParams;

    /**
     * 响应数据（JSON格式）
     */
        private String responseData;

    /**
     * 执行状态：SUCCESS-成功, FAILED-失败, TIMEOUT-超时
     */
        private String status;

    /**
     * 执行时间（毫秒）
     */
        private Long executionTime;

    /**
     * 错误信息
     */
        private String errorMessage;

    /**
     * 错误代码
     */
        private String errorCode;

    /**
     * 使用来源：WORKFLOW-工作流, DIRECT-直接调用, TEST-测试
     */
        private String usageSource = "DIRECT";

    /**
     * 客户端IP
     */
        private String clientIp;

    /**
     * 用户代理
     */
        private String userAgent;

    /**
     * 会话ID
     */
        private String sessionId;

    /**
     * 请求ID（用于追踪）
     */
        private String requestId;

    /**
     * 计费信息（JSON格式）
     */
        private String billingInfo;


    // Lombok生成的getter/setter方法
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPluginId() {
        return pluginId;
    }

    public void setPluginId(Long pluginId) {
        this.pluginId = pluginId;
    }

    public Long getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(Long workflowId) {
        this.workflowId = workflowId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getApiEndpoint() {
        return apiEndpoint;
    }

    public void setApiEndpoint(String apiEndpoint) {
        this.apiEndpoint = apiEndpoint;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(String requestParams) {
        this.requestParams = requestParams;
    }

    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Long executionTime) {
        this.executionTime = executionTime;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getUsageSource() {
        return usageSource;
    }

    public void setUsageSource(String usageSource) {
        this.usageSource = usageSource;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getBillingInfo() {
        return billingInfo;
    }

    public void setBillingInfo(String billingInfo) {
        this.billingInfo = billingInfo;
    }
}
