package com.alibaba.cloud.ai.workflow;

import com.alibaba.cloud.ai.dto.NodeDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
public class EndNodeExecutor implements NodeExecutor {

    public static final String NODE_TYPE = "2"; // From node_meta.go: NodeTypeExit
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String getNodeType() {
        return NODE_TYPE;
    }

    @Override
    public Map<String, JsonNode> execute(NodeDto node, WorkflowExecutionContext context) {
        // The "End" node gathers its inputs and presents them as the final workflow output.
        // The real implementation would parse node.getData() to figure out which inputs to collect.
        // For this simplified version, we'll just collect all inputs from the context that are wired to this node.
        // For now, we return a simple success message.
        ObjectNode result = objectMapper.createObjectNode();
        result.put("status", "Workflow Finished Successfully");

        // A more complete implementation would be:
        // result.set("data", context.getNodeOutput("some_previous_node_id"));

        return Collections.singletonMap("final_output", result);
    }
}
