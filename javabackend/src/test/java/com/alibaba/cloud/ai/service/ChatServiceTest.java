package com.alibaba.cloud.ai.service;

import com.alibaba.cloud.ai.model.Bot;
import com.alibaba.cloud.ai.model.Plugin;
import com.alibaba.cloud.ai.repository.BotRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.alibaba.dashscope.DashscopeAiChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.vectorstore.VectorStore;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChatServiceTest {

    @Mock
    private DashscopeAiChatModel chatModel;

    @Mock
    private VectorStore vectorStore;

    @Mock
    private BotRepository botRepository;

    @Mock
    private PluginExecutionService pluginExecutionService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ChatService chatService;

    private Bot testBot;

    @BeforeEach
    void setUp() {
        testBot = new Bot();
        testBot.setId(1L);
        testBot.setName("Test Bot");
        testBot.setPlugins(Collections.emptyList());
    }

    @Test
    void testChat_simpleResponse_noTools() {
        // Arrange
        String userMessage = "Hello";
        String expectedResponse = "Hello there! How can I help you?";
        when(botRepository.findById(1L)).thenReturn(Optional.of(testBot));
        when(vectorStore.similaritySearch(any(SearchRequest.class))).thenReturn(Collections.emptyList());

        ChatResponse chatResponse = new ChatResponse(Collections.singletonList(new Generation(expectedResponse)));
        when(chatModel.call(any(Prompt.class))).thenReturn(chatResponse);

        // Act
        String actualResponse = chatService.chat(1L, userMessage);

        // Assert
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testChat_withOnePluginTool_noFunctionCall() {
        // Arrange
        String userMessage = "What is the capital of France?";
        String expectedResponse = "The capital of France is Paris.";

        Plugin plugin = new Plugin();
        plugin.setId(101L);
        plugin.setPublished(true);
        // A simple OpenAPI spec with one tool
        plugin.setOpenapiDoc("openapi: 3.0.0\ninfo:\n  title: Test API\n  version: 1.0.0\nservers:\n  - url: http://localhost\npaths:\n  /test:\n    get:\n      operationId: testTool\n      summary: A test tool");
        testBot.setPlugins(Collections.singletonList(plugin));

        when(botRepository.findById(1L)).thenReturn(Optional.of(testBot));
        when(vectorStore.similaritySearch(any(SearchRequest.class))).thenReturn(Collections.emptyList());

        ChatResponse chatResponse = new ChatResponse(Collections.singletonList(new Generation(expectedResponse)));
        when(chatModel.call(any(Prompt.class))).thenReturn(chatResponse);

        // Act
        String actualResponse = chatService.chat(1L, userMessage);

        // Assert
        assertEquals(expectedResponse, actualResponse);
    }

    // More complex tests would be needed to simulate the multi-turn function calling loop,
    // which would involve mocking the sequence of responses from the chatModel.
}
