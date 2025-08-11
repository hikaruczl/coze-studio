package com.alibaba.cloud.ai.dto;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Map;

public class TestRunWorkflowRequest {

    private Long workflowId;
    private Map<String, JsonNode> inputs;

    public Long getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(Long workflowId) {
        this.workflowId = workflowId;
    }

    public Map<String, JsonNode> getInputs() {
        return inputs;
    }

    public void setInputs(Map<String, JsonNode> inputs) {
        this.inputs = inputs;
    }
}
