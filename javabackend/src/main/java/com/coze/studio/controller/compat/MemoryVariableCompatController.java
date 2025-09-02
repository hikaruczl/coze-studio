/*
 * Copyright 2025 coze-dev Authors
 */
package com.coze.studio.controller.compat;

import com.coze.studio.dto.common.ApiResponse;
import com.coze.studio.entity.ConversationVariable;
import com.coze.studio.entity.WorkflowVariable;
import com.coze.studio.service.VariableManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * {{ AURA-X: Add - 兼容 /api/memory/variable/* 到 VariableManagementService. Confirmed via 寸止 }}
 */
@Slf4j
@RestController
@RequestMapping("/api/memory/variable")
@Validated
@RequiredArgsConstructor
@Tag(name = "兼容层", description = "内存变量兼容接口")
public class MemoryVariableCompatController {

    private final VariableManagementService variableService;

    // upsert -> 对齐：设置对话变量
    @Operation(summary = "Upsert 内存变量(对话)")
    @PostMapping("/upsert")
    public ResponseEntity<ApiResponse<ConversationVariable>> upsertConversation(
            @RequestParam String name,
            @RequestParam(required = false) Long conversationId,
            @RequestParam(required = false) String sessionId,
            @RequestParam(required = false) Long messageId,
            @RequestParam(required = false) Long botId,
            @RequestParam String scope,
            @RequestParam(required = false) String value,
            @AuthenticationPrincipal UserDetails userDetails) {
        VariableManagementService.SetConversationVariableRequest req = new VariableManagementService.SetConversationVariableRequest();
        req.setName(name); req.setScope(scope); req.setConversationId(conversationId); req.setSessionId(sessionId); req.setMessageId(messageId); req.setBotId(botId); req.setValue(value); req.setUserId(getUserId(userDetails));
        ConversationVariable v = variableService.setConversationVariable(req);
        return ResponseEntity.ok(ApiResponse.success(v));
    }

    @Operation(summary = "获取内存变量(对话)")
    @PostMapping("/get")
    public ResponseEntity<ApiResponse<Object>> getConversation(
            @RequestParam String name,
            @RequestParam String scope,
            @RequestParam(required = false) Long conversationId,
            @RequestParam(required = false) String sessionId,
            @RequestParam(required = false) Long messageId,
            @RequestParam(required = false) Long botId,
            @AuthenticationPrincipal UserDetails userDetails) {
        VariableManagementService.GetConversationVariableRequest req = new VariableManagementService.GetConversationVariableRequest();
        req.setName(name); req.setScope(scope); req.setConversationId(conversationId); req.setSessionId(sessionId); req.setMessageId(messageId); req.setBotId(botId); req.setUserId(getUserId(userDetails));
        Optional<Object> v = variableService.getConversationVariable(req);
        return ResponseEntity.ok(ApiResponse.success(v.orElse(null)));
    }

    @Operation(summary = "删除内存变量(对话)")
    @PostMapping("/delete")
    public ResponseEntity<ApiResponse<Boolean>> deleteConversation(
            @RequestParam String name,
            @RequestParam String scope,
            @RequestParam(required = false) Long conversationId,
            @RequestParam(required = false) String sessionId,
            @RequestParam(required = false) Long messageId,
            @RequestParam(required = false) Long botId,
            @AuthenticationPrincipal UserDetails userDetails) {
        VariableManagementService.DeleteConversationVariableRequest req = new VariableManagementService.DeleteConversationVariableRequest();
        req.setName(name); req.setScope(scope); req.setConversationId(conversationId); req.setSessionId(sessionId); req.setMessageId(messageId); req.setBotId(botId); req.setUserId(getUserId(userDetails));
        boolean ok = variableService.deleteConversationVariable(req);
        return ResponseEntity.ok(ApiResponse.success(ok));
    }

    private Long getUserId(UserDetails userDetails) { try { return Long.parseLong(userDetails.getUsername()); } catch (Exception e) { return 1L; } }
}

