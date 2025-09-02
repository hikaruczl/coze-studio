package com.coze.studio.dto;

import java.util.Map;

/**
 * 占位实现：插件连接测试结果
 */
public class PluginConnectionTestResult {
    private boolean success;
    private String message;
    private long responseTime;
    private String pluginVersion; // 可为空
    private Map<String, Object> capabilities; // 可为空
    private String error; // 可为空

    public PluginConnectionTestResult() {}

    public PluginConnectionTestResult(boolean success, String message, long responseTime,
                                      String pluginVersion, Map<String, Object> capabilities, String error) {
        this.success = success;
        this.message = message;
        this.responseTime = responseTime;
        this.pluginVersion = pluginVersion;
        this.capabilities = capabilities;
        this.error = error;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public long getResponseTime() { return responseTime; }
    public void setResponseTime(long responseTime) { this.responseTime = responseTime; }

    public String getPluginVersion() { return pluginVersion; }
    public void setPluginVersion(String pluginVersion) { this.pluginVersion = pluginVersion; }

    public Map<String, Object> getCapabilities() { return capabilities; }
    public void setCapabilities(Map<String, Object> capabilities) { this.capabilities = capabilities; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}

