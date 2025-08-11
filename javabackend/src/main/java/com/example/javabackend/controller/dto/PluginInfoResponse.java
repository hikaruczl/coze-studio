package com.example.javabackend.controller.dto;

import com.example.javabackend.model.Plugin;
import com.example.javabackend.model.PluginType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PluginInfoResponse {
    private Long id;
    private String name;
    private String description;
    private PluginType type;
    private String openapi;

    public PluginInfoResponse(Plugin plugin) {
        this.id = plugin.getId();
        this.name = plugin.getName();
        this.description = plugin.getDescription();
        this.type = plugin.getType();
        this.openapi = plugin.getOpenapi();
    }
}
