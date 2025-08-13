package com.alibaba.cloud.ai.dto;

import com.alibaba.cloud.ai.model.PluginType;
import lombok.Data;

@Data
public class UpdatePluginRequest {
    private String name;
    private String description;
    private PluginType type;
    private String openapiDoc;
}
