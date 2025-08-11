package com.alibaba.cloud.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class DebugApiRequest {
    @JsonProperty("plugin_id")
    private Long pluginId;

    @JsonProperty("operation_id")
    private String operationId;

    private Map<String, Object> parameters;
}
