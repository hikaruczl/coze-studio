package com.coze.studio.model;

import java.util.Map;

/**
 * 占位实现：插件执行上下文
 * 后续可逐步完善业务字段与校验逻辑
 */
public class PluginExecutionContext {
    private Long userId;
    private Long pluginId;
    private String pluginName;
    private String pluginVersion;
    private String serviceUrl;
    private String serviceToken;
    private Map<String, Object> pluginConfig;
    private Map<String, String> permissions;
    private String executionId;
    private Map<String, Object> userContext;

    public PluginExecutionContext() {}

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getPluginId() { return pluginId; }
    public void setPluginId(Long pluginId) { this.pluginId = pluginId; }

    public String getPluginName() { return pluginName; }
    public void setPluginName(String pluginName) { this.pluginName = pluginName; }

    public String getPluginVersion() { return pluginVersion; }
    public void setPluginVersion(String pluginVersion) { this.pluginVersion = pluginVersion; }

    public String getServiceUrl() { return serviceUrl; }
    public void setServiceUrl(String serviceUrl) { this.serviceUrl = serviceUrl; }

    public String getServiceToken() { return serviceToken; }
    public void setServiceToken(String serviceToken) { this.serviceToken = serviceToken; }

    public Map<String, Object> getPluginConfig() { return pluginConfig; }
    public void setPluginConfig(Map<String, Object> pluginConfig) { this.pluginConfig = pluginConfig; }

    public Map<String, String> getPermissions() { return permissions; }
    public void setPermissions(Map<String, String> permissions) { this.permissions = permissions; }

    public String getExecutionId() { return executionId; }
    public void setExecutionId(String executionId) { this.executionId = executionId; }

    public Map<String, Object> getUserContext() { return userContext; }
    public void setUserContext(Map<String, Object> userContext) { this.userContext = userContext; }
}

