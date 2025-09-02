/*
 * Copyright 2025 coze-dev Authors
 */
package com.coze.studio.controller.compat;

import com.coze.studio.dto.common.ApiResponse;
import com.coze.studio.dto.message.SendMessageRequest;
import com.coze.studio.service.LLMService;
import com.coze.studio.service.MessageService;
import com.coze.studio.service.LLMService.LLMMessage;
import com.coze.studio.service.LLMService.LLMRequest;
import com.coze.studio.service.LLMService.LLMResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import jakarta.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * {{ AURA-X: Add - 兼容 /api/conversation/chat 流式端点。Confirmed via 寸止 }}
 * - 路由：POST /api/conversation/chat
 * - 行为：SSE 流式输出 message 事件
 */
@Slf4j
@RestController
@RequestMapping("/api/conversation")
@Validated
@RequiredArgsConstructor
@Tag(name = "兼容层", description = "提供与 Go 后端 /api/conversation/chat 接口兼容的端点")
public class ConversationCompatController {

    private final LLMService llmService;
    private final MessageService messageService;

    public static class AgentRunRequest {
        public Long bot_id;
        public String content; // 文本内容
        public String scene;   // 会话场景
    }

    @Operation(summary = "AgentRun (SSE)", description = "兼容 /api/conversation/chat，SSE 流式返回")
    @PostMapping(value = "/chat", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SseEmitter> agentRun(
            @Valid @RequestBody AgentRunRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        log.info("[Compat] /api/conversation/chat: userId={}, botId={}, scene={}", userId, request.bot_id, request.scene);

        SseEmitter emitter = new SseEmitter(0L);
        try {
            emitter.send(SseEmitter.event().name("start").data("{\"ok\":true}"));

            LLMRequest llmReq = new LLMRequest();
            llmReq.setModel("default");
            List<LLMMessage> msgs = new ArrayList<>();
            msgs.add(new LLMMessage("user", request.content != null ? request.content : ""));
            llmReq.setMessages(msgs);

            llmService.generateTextStream(llmReq, new LLMService.LLMStreamCallback() {
                @Override public void onStart() {
                    safeSend(emitter, SseEmitter.event().name("message").data("{\"type\":\"start\"}"));
                }
                @Override public void onContent(String content) {
                    safeSend(emitter, SseEmitter.event().name("message").data(content));
                }
                @Override public void onComplete(LLMResponse response) {
                    safeSend(emitter, SseEmitter.event().name("end").data("{\"ok\":true}"));
                    emitter.complete();
                }
                @Override public void onError(Exception error) {
                    safeSend(emitter, SseEmitter.event().name("error").data(errJson(error)));
                    emitter.completeWithError(error);
                }
            });
        } catch (Exception e) {
            log.error("/api/conversation/chat 异常", e);
            try { emitter.send(SseEmitter.event().name("error").data(errJson(e))); } catch (IOException ignore) {}
            emitter.completeWithError(e);
        }
        return ResponseEntity.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(emitter);
    }

    private static void safeSend(SseEmitter emitter, SseEmitter.SseEventBuilder event) {
        try { emitter.send(event); } catch (IOException ignored) {}
    }

    private static String errJson(Exception e) {
        String msg = e.getMessage() == null ? "error" : e.getMessage();
        return "{\"error\":\"" + msg.replace("\"", "\\\"") + "\"}";
    }

    private Long getUserId(UserDetails userDetails) {
        try { return Long.parseLong(userDetails.getUsername()); } catch (Exception e) { return 1L; }
    }
}

