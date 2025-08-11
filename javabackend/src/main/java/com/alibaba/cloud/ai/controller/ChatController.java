package com.alibaba.cloud.ai.controller;

import com.alibaba.cloud.ai.dto.ChatRequest;
import com.alibaba.cloud.ai.dto.ChatResponse;
import com.alibaba.cloud.ai.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/conversation")
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest chatRequest) {
        String responseMessage = chatService.chat(chatRequest.getQuery());
        return new ChatResponse(responseMessage);
    }
}
