package com.coze.studio.dto;

import java.util.Map;

/**
 * 占位实现：插件执行结果
 */
public class PluginExecutionResult {
    private boolean success;
    private String status; // SUCCESS/FAILED
    private Map<String, Object> data; // 结果数据
    private String errorMessage; // 失败时错误信息
    private long executionTime; // 毫秒

    public PluginExecutionResult() {}

    public PluginExecutionResult(boolean success, String status, Map<String, Object> data, String errorMessage, long executionTime) {
        this.success = success;
        this.status = status;
        this.data = data;
        this.errorMessage = errorMessage;
        this.executionTime = executionTime;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Map<String, Object> getData() { return data; }
    public void setData(Map<String, Object> data) { this.data = data; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public long getExecutionTime() { return executionTime; }
    public void setExecutionTime(long executionTime) { this.executionTime = executionTime; }
}

