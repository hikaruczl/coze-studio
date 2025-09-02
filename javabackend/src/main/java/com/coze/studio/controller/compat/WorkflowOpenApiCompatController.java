/*
 * Copyright 2025 coze-dev Authors
 */
package com.coze.studio.controller.compat;

import com.coze.studio.dto.common.ApiResponse;
import com.coze.studio.dto.common.PageResponse;
import com.coze.studio.dto.workflow.ExecuteWorkflowRequest;
import com.coze.studio.dto.workflow.WorkflowExecutionResponse;
import com.coze.studio.service.WorkflowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import jakarta.validation.Valid;
import java.io.IOException;

/**
 * {{ AURA-X: Add - 兼容 /v1/workflow* 开放API，包含 run/stream_run/stream_resume/get_run_history。Confirmed via 寸止 }}
 */
@Slf4j
@RestController
@RequestMapping("/v1/workflow")
@Validated
@RequiredArgsConstructor
@Tag(name = "兼容层", description = "提供与 Go 后端 /v1/workflow 开放接口兼容的端点")
public class WorkflowOpenApiCompatController {

    private final WorkflowService workflowService;

    @Operation(summary = "执行工作流(同步)")
    @PostMapping("/run")
    public ResponseEntity<ApiResponse<WorkflowExecutionResponse>> run(
            @Valid @RequestBody ExecuteWorkflowRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        log.info("[Compat] /v1/workflow/run userId={}, workflowId={}", userId, request.getWorkflowId());
        WorkflowExecutionResponse resp = workflowService.executeWorkflow(userId, request.getWorkflowId(), request);
        return ResponseEntity.ok(ApiResponse.success(resp));
    }

    @Operation(summary = "执行工作流(流式)")
    @PostMapping(value = "/stream_run", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SseEmitter> streamRun(
            @Valid @RequestBody ExecuteWorkflowRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        log.info("[Compat] /v1/workflow/stream_run userId={}, workflowId={}", userId, request.getWorkflowId());
        // 占位：当前先同步执行后以一次性事件返回，后续可改为逐步节点输出
        SseEmitter emitter = new SseEmitter(0L);
        try {
            emitter.send(SseEmitter.event().name("start").data("{}"));
            WorkflowExecutionResponse resp = workflowService.executeWorkflow(userId, request.getWorkflowId(), request);
            emitter.send(SseEmitter.event().name("message").data(resp));
            emitter.send(SseEmitter.event().name("end").data("{}"));
            emitter.complete();
        } catch (Exception e) {
            safeError(emitter, e);
        }
        return ResponseEntity.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(emitter);
    }

    @Operation(summary = "恢复流式执行")
    @PostMapping(value = "/stream_resume")
    public ResponseEntity<SseEmitter> streamResume(
            @RequestParam("executionId") String executionId,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        log.info("[Compat] /v1/workflow/stream_resume userId={}, executionId={}", userId, executionId);
        SseEmitter emitter = new SseEmitter(0L);
        try {
            emitter.send(SseEmitter.event().name("start").data("{}"));
            WorkflowExecutionResponse resp = workflowService.getWorkflowExecution(executionId);
            emitter.send(SseEmitter.event().name("message").data(resp));
            emitter.send(SseEmitter.event().name("end").data("{}"));
            emitter.complete();
        } catch (Exception e) {
            safeError(emitter, e);
        }
        return ResponseEntity.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(emitter);
    }

    @Operation(summary = "获取执行历史")
    @PostMapping("/get_run_history")
    public ResponseEntity<ApiResponse<PageResponse<WorkflowExecutionResponse>>> getRunHistory(
            @RequestParam("workflowId") Long workflowId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        log.info("[Compat] /v1/workflow/get_run_history userId={}, workflowId={}", userId, workflowId);
        PageResponse<WorkflowExecutionResponse> resp = workflowService.getWorkflowExecutions(workflowId, PageRequest.of(Math.max(0, page-1), size));
        return ResponseEntity.ok(ApiResponse.success(resp));
    }

    private void safeError(SseEmitter emitter, Exception e) {
        try {
            emitter.send(SseEmitter.event().name("error").data(e.getMessage()));
        } catch (IOException ignored) {}
        emitter.completeWithError(e);
    }

    private Long getUserId(UserDetails userDetails) {
        try { return Long.parseLong(userDetails.getUsername()); } catch (Exception e) { return 1L; }
    }
}

