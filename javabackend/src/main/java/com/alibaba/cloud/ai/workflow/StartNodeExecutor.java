package com.alibaba.cloud.ai.workflow;

import com.alibaba.cloud.ai.dto.NodeDto;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
public class StartNodeExecutor implements NodeExecutor {

    public static final String NODE_TYPE = "1"; // From node_meta.go: NodeTypeEntry

    @Override
    public String getNodeType() {
        return NODE_TYPE;
    }

    @Override
    public Map<String, JsonNode> execute(NodeDto node, WorkflowExecutionContext context) {
        // The start node's job is to populate the context with the initial inputs of the workflow.
        // For now, we assume the initial inputs are already in the context when the execution starts.
        // The outputs of the start node are effectively the initial inputs to the workflow.
        // In a real implementation, we would parse the `node.getData()` to map initial
        // request parameters to named outputs.

        // For this implementation, we'll assume the initial inputs are placed under the start node's ID.
        Map<String, JsonNode> initialInputs = context.getNodeOutput(node.getId());
        return initialInputs != null ? initialInputs : Collections.emptyMap();
    }
}
