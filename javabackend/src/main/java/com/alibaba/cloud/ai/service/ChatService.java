package com.alibaba.cloud.ai.service;

import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.function.FunctionCallingOptions;
import org.springframework.ai.alibaba.dashscope.DashscopeAiChatModel;
import org.springframework.ai.alibaba.dashscope.DashscopeAiChatOptions;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final DashscopeAiChatModel chatModel;
    private final VectorStore vectorStore;

    @Autowired
    public ChatService(DashscopeAiChatModel chatModel, VectorStore vectorStore) {
        this.chatModel = chatModel;
        this.vectorStore = vectorStore;
    }

    public String chat(String message) {
        try {
            // RAG logic remains the same
            List<Document> similarDocuments = this.vectorStore.similaritySearch(SearchRequest.query(message).withTopK(2));
            String documentsContent = similarDocuments.stream().map(Document::getContent).collect(Collectors.joining("\n"));
            String promptTemplate = """
                    Based on the following information, please answer the user's query.
                    If the information is not relevant, answer based on your own knowledge.
                    If the user asks about the weather, use the 'weatherFunction' tool.

                    CONTEXT:
                    {context}

                    QUERY:
                    {query}
                    """;
            String finalPrompt = promptTemplate.replace("{context}", documentsContent).replace("{query}", message);

            // Enable tool calling in the prompt options
            DashscopeAiChatOptions chatOptions = DashscopeAiChatOptions.builder()
                    .withFunction("weatherFunction")
                    .build();

            // Call the model with the prompt and options
            return chatModel.call(new Prompt(finalPrompt, chatOptions))
                    .getResult()
                    .getOutput()
                    .getContent();

        } catch (Exception e) {
            // Fallback logic remains the same
            if (e.getMessage() != null && e.getMessage().contains("API-key")) {
                return "Error: The DashScope API key is not configured. Please set 'spring.ai.alibaba.dashscope.api-key' in your application.properties file.";
            }
            if(e.getMessage() != null && e.getMessage().contains("Cannot connect to")) {
                // If vector store fails, just call the model with tool-calling enabled but without RAG context.
                DashscopeAiChatOptions chatOptions = DashscopeAiChatOptions.builder()
                        .withFunction("weatherFunction")
                        .build();
                return chatModel.call(new Prompt(message, chatOptions))
                        .getResult()
                        .getOutput()
                        .getContent();
            }
            // Generic fallback
            return chatModel.call(message);
        }
    }
}
