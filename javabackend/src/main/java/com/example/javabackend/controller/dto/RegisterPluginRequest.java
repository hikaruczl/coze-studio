package com.example.javabackend.controller.dto;

import com.example.javabackend.model.PluginType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RegisterPluginRequest {
    @JsonProperty("ai_plugin")
    private String aiPlugin;
    private String openapi;
    @JsonProperty("client_id")
    private String clientId;
    @JsonProperty("client_secret")
    private String clientSecret;
    @JsonProperty("service_token")
    private String serviceToken;
    @JsonProperty("plugin_type")
    private PluginType pluginType;
    @JsonProperty("space_id")
    private Long spaceId;
    @JsonProperty("import_from_file")
    private boolean importFromFile;
    @JsonProperty("project_id")
    private Long projectId;
    private String name;
    private String description;
}
