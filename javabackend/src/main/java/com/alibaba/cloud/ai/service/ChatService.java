package com.alibaba.cloud.ai.service;

import com.alibaba.cloud.ai.model.Bot;
import com.alibaba.cloud.ai.model.Plugin;
import com.alibaba.cloud.ai.repository.BotRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.parser.OpenAPIV3Parser;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.alibaba.dashscope.DashscopeAiChatModel;
import org.springframework.ai.alibaba.dashscope.DashscopeAiChatOptions;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final DashscopeAiChatModel chatModel;
    private final VectorStore vectorStore;
    private final BotRepository botRepository;
    private final PluginExecutionService pluginExecutionService;
    private final ObjectMapper objectMapper;

    private record ToolInfo(Long pluginId, String operationId) {}

    @Autowired
    public ChatService(DashscopeAiChatModel chatModel, VectorStore vectorStore, BotRepository botRepository, PluginExecutionService pluginExecutionService, ObjectMapper objectMapper) {
        this.chatModel = chatModel;
        this.vectorStore = vectorStore;
        this.botRepository = botRepository;
        this.pluginExecutionService = pluginExecutionService;
        this.objectMapper = objectMapper;
    }

    public String chat(Long botId, String message) {
        Bot bot = botRepository.findById(botId)
                .orElseThrow(() -> new IllegalArgumentException("Bot not found with id: " + botId));

        // 1. RAG logic
        List<Document> similarDocuments = this.vectorStore.similaritySearch(SearchRequest.query(message).withTopK(2));
        String documentsContent = similarDocuments.stream().map(Document::getContent).collect(Collectors.joining("\n"));
        String initialUserMessage = """
                Based on the following information, please answer my query.
                If the information is not relevant, answer based on your own knowledge.

                CONTEXT:
                %s

                QUERY:
                %s
                """.formatted(documentsContent, message);

        List<Message> conversation = new ArrayList<>(List.of(new org.springframework.ai.chat.messages.UserMessage(initialUserMessage)));

        // 2. Discover tools from plugins
        Map<String, ToolInfo> availableTools = new HashMap<>();
        for (Plugin plugin : bot.getPlugins()) {
            if (plugin.getOpenapiDoc() == null || plugin.getOpenapiDoc().isEmpty() || !plugin.getPublished()) {
                continue;
            }
            try {
                OpenAPI openAPI = new OpenAPIV3Parser().readContents(plugin.getOpenapiDoc()).getOpenAPI();
                if (openAPI != null && openAPI.getPaths() != null) {
                    for (PathItem path : openAPI.getPaths().values()) {
                        for (Operation operation : path.getOperations().values()) {
                            if (operation.getOperationId() != null) {
                                availableTools.put(operation.getOperationId(), new ToolInfo(plugin.getId(), operation.getOperationId()));
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Failed to parse OpenAPI spec for plugin " + plugin.getId() + ": " + e.getMessage());
            }
        }

        // 3. Call the model with tools
        DashscopeAiChatOptions chatOptions = DashscopeAiChatOptions.builder()
                .withTools(availableTools.keySet().stream().map(DashscopeAiChatOptions.Tool::new).collect(Collectors.toSet()))
                .build();

        ChatResponse response = chatModel.call(new Prompt(conversation, chatOptions));

        // 4. Handle tool calls
        while (true) {
            Generation generation = response.getResult();
            if (generation.getOutput().getToolCalls() != null && !generation.getOutput().getToolCalls().isEmpty()) {
                AssistantMessage assistantMessage = generation.getOutput();
                conversation.add(assistantMessage); // Add AI's response to conversation history

                List<ToolResponseMessage> toolResponses = assistantMessage.getToolCalls().stream().map(toolCall -> {
                    ToolInfo toolInfo = availableTools.get(toolCall.getName());
                    if (toolInfo == null) {
                        return new ToolResponseMessage("Tool " + toolCall.getName() + " not found.", toolCall.getId());
                    }
                    try {
                        Map<String, Object> args = objectMapper.readValue(toolCall.getArguments(), new TypeReference<>() {});
                        String result = pluginExecutionService.executeTool(toolInfo.pluginId(), toolInfo.operationId(), args);
                        return new ToolResponseMessage(result, toolCall.getId());
                    } catch (Exception e) {
                        return new ToolResponseMessage("Error executing tool " + toolCall.getName() + ": " + e.getMessage(), toolCall.getId());
                    }
                }).collect(Collectors.toList());

                conversation.addAll(toolResponses); // Add tool responses to conversation
                response = chatModel.call(new Prompt(conversation, chatOptions)); // Call model again with tool responses
            } else {
                // No more tool calls, return the final response
                return generation.getOutput().getContent();
            }
        }
    }
}
