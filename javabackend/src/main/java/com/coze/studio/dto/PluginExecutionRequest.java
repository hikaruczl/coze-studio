package com.coze.studio.dto;

import java.util.Map;

/**
 * 占位实现：插件执行请求
 * 字段根据 PluginExecutionService 使用点最小覆盖
 */
public class PluginExecutionRequest {
    private String requestId;
    private Long userId;
    private Long pluginId;
    private Long workflowId; // 可为空
    private String nodeId;   // 可为空

    private String operation;    // operationId
    private Map<String, Object> parameters;

    // 直接 API 模式相关
    private String apiEndpoint;  // 可为空
    private String httpMethod;   // 可为空，POST/GET/PUT/DELETE
    private Map<String, String> headers; // 可为空

    private String sessionId; // 记录用

    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getPluginId() { return pluginId; }
    public void setPluginId(Long pluginId) { this.pluginId = pluginId; }

    public Long getWorkflowId() { return workflowId; }
    public void setWorkflowId(Long workflowId) { this.workflowId = workflowId; }

    public String getNodeId() { return nodeId; }
    public void setNodeId(String nodeId) { this.nodeId = nodeId; }

    public String getOperation() { return operation; }
    public void setOperation(String operation) { this.operation = operation; }

    public Map<String, Object> getParameters() { return parameters; }
    public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }

    public String getApiEndpoint() { return apiEndpoint; }
    public void setApiEndpoint(String apiEndpoint) { this.apiEndpoint = apiEndpoint; }

    public String getHttpMethod() { return httpMethod; }
    public void setHttpMethod(String httpMethod) { this.httpMethod = httpMethod; }

    public Map<String, String> getHeaders() { return headers; }
    public void setHeaders(Map<String, String> headers) { this.headers = headers; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
}

