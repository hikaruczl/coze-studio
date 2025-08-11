package com.alibaba.cloud.ai.service;

import com.alibaba.cloud.ai.model.Bot;
import com.alibaba.cloud.ai.model.Plugin;
import com.alibaba.cloud.ai.repository.BotRepository;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.parser.OpenAPIV3Parser;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.function.FunctionCallbackContext;
import org.springframework.ai.model.function.FunctionCallingOptions;
import org.springframework.ai.alibaba.dashscope.DashscopeAiChatModel;
import org.springframework.ai.alibaba.dashscope.DashscopeAiChatOptions;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final DashscopeAiChatModel chatModel;
    private final VectorStore vectorStore;
    private final BotRepository botRepository;
    private final FunctionCallbackContext functionCallbackContext;


    @Autowired
    public ChatService(DashscopeAiChatModel chatModel, VectorStore vectorStore, BotRepository botRepository, ApplicationContext context) {
        this.chatModel = chatModel;
        this.vectorStore = vectorStore;
        this.botRepository = botRepository;
        this.functionCallbackContext = FunctionCallbackContext.builder(context)
                .build();
    }

    public String chat(Long botId, String message) {

        Bot bot = botRepository.findById(botId)
                .orElseThrow(() -> new IllegalArgumentException("Bot not found with id: " + botId));

        // RAG logic
        List<Document> similarDocuments = this.vectorStore.similaritySearch(SearchRequest.query(message).withTopK(2));
        String documentsContent = similarDocuments.stream().map(Document::getContent).collect(Collectors.joining("\n"));
        String promptTemplate = """
                Based on the following information, please answer the user's query.
                If the information is not relevant, answer based on your own knowledge.

                CONTEXT:
                {context}

                QUERY:
                {query}
                """;
        String finalPrompt = promptTemplate.replace("{context}", documentsContent).replace("{query}", message);

        // Dynamic Tool/Function Calling
        Set<String> functionNames = new HashSet<>();
        for (Plugin plugin : bot.getPlugins()) {
            if (plugin.getOpenapi() == null || plugin.getOpenapi().isEmpty()) {
                continue;
            }
            try {
                OpenAPI openAPI = new OpenAPIV3Parser().readContents(plugin.getOpenapi()).getOpenAPI();
                if (openAPI != null && openAPI.getPaths() != null) {
                    for (PathItem path : openAPI.getPaths().values()) {
                        for (Operation operation : path.getOperations().values()) {
                            if (operation.getOperationId() != null) {
                                // We need to register the function with a bean name.
                                // Let's assume the bean name is the operationId + "Tool"
                                functionNames.add(operation.getOperationId() + "Tool");
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Failed to parse OpenAPI spec for plugin " + plugin.getId() + ": " + e.getMessage());
            }
        }

        // Add the hardcoded weather function for now
        functionNames.add("weatherService");


        DashscopeAiChatOptions chatOptions = DashscopeAiChatOptions.builder()
                .withFunctions(functionNames)
                .build();

        ChatResponse response = chatModel.call(new Prompt(finalPrompt, chatOptions));

        // This is a simplified version. A full implementation would loop until the response is not a function call.
        return response.getResult().getOutput().getContent();
    }
}
