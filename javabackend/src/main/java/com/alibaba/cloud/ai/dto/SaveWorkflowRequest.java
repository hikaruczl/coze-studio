package com.alibaba.cloud.ai.dto;

import com.fasterxml.jackson.databind.JsonNode;

public class SaveWorkflowRequest {

    private Long id;
    private String name;
    private String description;
    private JsonNode canvas; // Accept as a raw JSON object

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public JsonNode getCanvas() {
        return canvas;
    }

    public void setCanvas(JsonNode canvas) {
        this.canvas = canvas;
    }
}
