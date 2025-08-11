package com.alibaba.cloud.ai.workflow;

import com.alibaba.cloud.ai.dto.NodeDto;
import com.alibaba.cloud.ai.service.ChatService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
public class PluginNodeExecutor implements NodeExecutor {

    public static final String NODE_TYPE = "4"; // From node_meta.go: NodeTypePlugin
    private final ChatService chatService;
    private final ObjectMapper objectMapper;

    @Autowired
    public PluginNodeExecutor(ChatService chatService, ObjectMapper objectMapper) {
        this.chatService = chatService;
        this.objectMapper = objectMapper;
    }

    @Override
    public String getNodeType() {
        return NODE_TYPE;
    }

    @Override
    public Map<String, JsonNode> execute(NodeDto node, WorkflowExecutionContext context) {
        // In a real implementation, node.getData() would specify which plugin/tool to call
        // and how to map the inputs to its parameters.

        // For this simplified example, we'll assume the input is a natural language query
        // that is intended to trigger the 'weatherFunction' tool.
        JsonNode promptNode = context.getNodeOutput("some_previous_node_id").get("query_for_plugin");
        String prompt = (promptNode != null && promptNode.isTextual()) ? promptNode.asText() : "What is the weather in Beijing?";

        // The ChatService is already configured to use the weatherFunction.
        // By sending a prompt that mentions weather, we expect the AI to call the tool.
        String result = chatService.chat(prompt);

        ObjectNode output = objectMapper.createObjectNode();
        output.put("plugin_response", result);

        return Collections.singletonMap("output", output);
    }
}
