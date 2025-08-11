package com.alibaba.cloud.ai.workflow;

import com.alibaba.cloud.ai.dto.NodeDto;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

public interface NodeExecutor {

    /**
     * Executes the logic for a specific workflow node.
     *
     * @param node      The node to execute.
     * @param context   The execution context containing the state of the workflow.
     * @return A map of output values produced by the node.
     */
    Map<String, JsonNode> execute(NodeDto node, WorkflowExecutionContext context);

    /**
     * Returns the type of node this executor is responsible for.
     * This should match the 'type' field in the workflow canvas JSON.
     * @return The node type identifier.
     */
    String getNodeType();
}
