package com.alibaba.cloud.ai.workflow;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WorkflowExecutionContext {

    private final Map<String, Map<String, JsonNode>> nodeOutputs = new ConcurrentHashMap<>();

    public void addNodeOutput(String nodeId, Map<String, JsonNode> outputs) {
        this.nodeOutputs.put(nodeId, outputs);
    }

    public Map<String, JsonNode> getNodeOutput(String nodeId) {
        return this.nodeOutputs.get(nodeId);
    }

    public Map<String, Map<String, JsonNode>> getAllOutputs() {
        return Map.copyOf(this.nodeOutputs);
    }
}
