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
public class LlmNodeExecutor implements NodeExecutor {

    public static final String NODE_TYPE = "3"; // From node_meta.go: NodeTypeLLM
    private final ChatService chatService;
    private final ObjectMapper objectMapper;

    @Autowired
    public LlmNodeExecutor(ChatService chatService, ObjectMapper objectMapper) {
        this.chatService = chatService;
        this.objectMapper = objectMapper;
    }

    @Override
    public String getNodeType() {
        return NODE_TYPE;
    }

    @Override
    public Map<String, JsonNode> execute(NodeDto node, WorkflowExecutionContext context) {
        // A real implementation would parse the node.getData() to get the full prompt,
        // model parameters, and references to inputs from other nodes.

        // For this simplified example, we'll assume a simple text input named "prompt".
        // The real input logic is complex, involving resolving references like {{node-1.output}}
        JsonNode promptNode = context.getNodeOutput("some_previous_node_id").get("prompt");
        String prompt = (promptNode != null && promptNode.isTextual()) ? promptNode.asText() : "Tell me a joke.";

        String result = chatService.chat(prompt);

        ObjectNode output = objectMapper.createObjectNode();
        output.put("llm_response", result);

        return Collections.singletonMap("output", output);
    }
}
