/*
 * Copyright 2025 coze-dev Authors
 */
package com.coze.studio.controller.compat;

import com.coze.studio.dto.common.ApiResponse;
import com.coze.studio.service.LLMService;
import com.coze.studio.service.MessageService;
import com.coze.studio.service.LLMService.LLMMessage;
import com.coze.studio.service.LLMService.LLMRequest;
import com.coze.studio.service.LLMService.LLMResponse;
import com.coze.studio.dto.message.SendMessageRequest;
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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {{ AURA-X: Add - 兼容 /v3/chat 流式聊天端点，确保与 Go 后端行为对齐（SSE）。Confirmed via 寸止 }}
 * 实现说明：
 * - 路由: POST /v3/chat
 * - 请求体：对齐 Go 的 ChatV3Request，最小字段采用 SendMessageRequest 近似承载
 * - 流式：使用 Spring SseEmitter 输出 data: 片段\n\n
 */
@Slf4j
@RestController
@RequestMapping("/v3")
@Validated
@RequiredArgsConstructor
@Tag(name = "兼容层", description = "提供与 Go 后端 /v3 接口兼容的端点")
public class ChatV3CompatController {

    private final LLMService llmService;
    private final MessageService messageService;

    public static class ChatV3Request {
        // 最小兼容字段（可扩展）：
        public Long bot_id; // 与 Go 一致
        public String query; // 文本内容
        public List<Map<String, Object>> messages; // 历史消息（可选）
        public String model; // 指定模型（可选）
        public Boolean stream = true; // 是否流式
    }

    @Operation(summary = "Chat V3 (SSE)", description = "兼容 /v3/chat，默认以 SSE 流式返回")
    @PostMapping(value = "/chat", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SseEmitter> chatV3(
            @Valid @RequestBody ChatV3Request request,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        log.info("[Compat] /v3/chat: userId={}, botId={}, model={}", userId, request.bot_id, request.model);

        SseEmitter emitter = new SseEmitter(0L);
        try {
            // 开场事件
            emitter.send(SseEmitter.event().name("start").data("{" + "\"ok\":true}"));

            // 构造 LLM 请求
            LLMRequest llmReq = new LLMRequest();
            llmReq.setModel(request.model != null ? request.model : "default");
            List<LLMMessage> msgs = new ArrayList<>();
            if (request.messages != null) {
                for (Map<String, Object> m : request.messages) {
                    String role = String.valueOf(m.getOrDefault("role", "user"));
                    String content = String.valueOf(m.getOrDefault("content", ""));
                    msgs.add(new LLMMessage(role, content));
                }
            }
            if (request.query != null) {
                msgs.add(new LLMMessage("user", request.query));
            }
            llmReq.setMessages(msgs);

            // 触发流式回调
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
            log.error("/v3/chat 处理异常", e);
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
        return "{\"error\":\"" + escape(msg) + "\"}";
    }

    private static String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private Long getUserId(UserDetails userDetails) {
        try { return Long.parseLong(userDetails.getUsername()); } catch (Exception e) { return 1L; }
    }
}

