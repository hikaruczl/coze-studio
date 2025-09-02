/*
 * Copyright 2025 coze-dev Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.coze.studio.controller;

import com.coze.studio.dto.common.ApiResponse;
import com.coze.studio.dto.common.PageResponse;
import com.coze.studio.dto.conversation.*;
import com.coze.studio.security.UserPrincipal;
import com.coze.studio.service.ConversationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 对话管理控制器
 * 处理对话相关的 CRUD 操作
 * 
 * @author coze-dev
 */
@Tag(name = "对话管理", description = "对话管理相关接口")
@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;

    /**
     * 创建对话
     */
    @Operation(summary = "创建对话", description = "创建一个新的对话")
    @PostMapping
    public ResponseEntity<ApiResponse<ConversationResponse>> createConversation(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody CreateConversationRequest request) {
        ConversationResponse conversation = conversationService.createConversation(userPrincipal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("对话创建成功", conversation));
    }

    /**
     * 更新对话
     */
    @Operation(summary = "更新对话", description = "更新对话的基本信息")
    @PutMapping("/{conversationId}")
    public ResponseEntity<ApiResponse<ConversationResponse>> updateConversation(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long conversationId,
            @Valid @RequestBody UpdateConversationRequest request) {
        ConversationResponse conversation = conversationService.updateConversation(
                userPrincipal.getId(), conversationId, request);
        return ResponseEntity.ok(ApiResponse.success("对话更新成功", conversation));
    }

    /**
     * 获取对话详情
     */
    @Operation(summary = "获取对话详情", description = "根据ID获取对话的详细信息")
    @GetMapping("/{conversationId}")
    public ResponseEntity<ApiResponse<ConversationResponse>> getConversationById(@PathVariable Long conversationId) {
        ConversationResponse conversation = conversationService.getConversationById(conversationId);
        return ResponseEntity.ok(ApiResponse.success(conversation));
    }

    /**
     * 删除对话
     */
    @Operation(summary = "删除对话", description = "删除指定的对话")
    @DeleteMapping("/{conversationId}")
    public ResponseEntity<ApiResponse<String>> deleteConversation(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long conversationId) {
        conversationService.deleteConversation(userPrincipal.getId(), conversationId);
        return ResponseEntity.ok(ApiResponse.success("对话删除成功"));
    }

    /**
     * 分页查询对话列表
     */
    @Operation(summary = "查询对话列表", description = "分页查询对话列表")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ConversationResponse>>> getConversations(
            ConversationQueryRequest queryRequest,
            @PageableDefault(size = 20, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<ConversationResponse> conversations = conversationService.getConversations(queryRequest, pageable);
        return ResponseEntity.ok(ApiResponse.success(conversations));
    }

    /**
     * 获取当前用户的对话列表
     */
    @Operation(summary = "获取我的对话列表", description = "获取当前用户的对话列表")
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<PageResponse<ConversationResponse>>> getMyConversations(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PageableDefault(size = 20, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<ConversationResponse> conversations = conversationService.getUserConversations(
                userPrincipal.getId(), pageable);
        return ResponseEntity.ok(ApiResponse.success(conversations));
    }

    /**
     * 获取Bot的对话列表
     */
    @Operation(summary = "获取Bot对话列表", description = "获取指定Bot的对话列表")
    @GetMapping("/bot/{botId}")
    public ResponseEntity<ApiResponse<PageResponse<ConversationResponse>>> getBotConversations(
            @PathVariable Long botId,
            @PageableDefault(size = 20, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<ConversationResponse> conversations = conversationService.getBotConversations(botId, pageable);
        return ResponseEntity.ok(ApiResponse.success(conversations));
    }

    /**
     * 置顶/取消置顶对话
     */
    @Operation(summary = "置顶对话", description = "置顶或取消置顶对话")
    @PostMapping("/{conversationId}/pin")
    public ResponseEntity<ApiResponse<ConversationResponse>> toggleConversationPin(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long conversationId,
            @RequestParam boolean pinned) {
        ConversationResponse conversation = conversationService.toggleConversationPin(
                userPrincipal.getId(), conversationId, pinned);
        return ResponseEntity.ok(ApiResponse.success(pinned ? "对话已置顶" : "对话已取消置顶", conversation));
    }

    /**
     * 结束对话
     */
    @Operation(summary = "结束对话", description = "结束指定的对话")
    @PostMapping("/{conversationId}/end")
    public ResponseEntity<ApiResponse<ConversationResponse>> endConversation(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long conversationId) {
        ConversationResponse conversation = conversationService.endConversation(userPrincipal.getId(), conversationId);
        return ResponseEntity.ok(ApiResponse.success("对话已结束", conversation));
    }

    /**
     * 暂停对话
     */
    @Operation(summary = "暂停对话", description = "暂停指定的对话")
    @PostMapping("/{conversationId}/pause")
    public ResponseEntity<ApiResponse<ConversationResponse>> pauseConversation(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long conversationId) {
        ConversationResponse conversation = conversationService.pauseConversation(userPrincipal.getId(), conversationId);
        return ResponseEntity.ok(ApiResponse.success("对话已暂停", conversation));
    }

    /**
     * 恢复对话
     */
    @Operation(summary = "恢复对话", description = "恢复暂停的对话")
    @PostMapping("/{conversationId}/resume")
    public ResponseEntity<ApiResponse<ConversationResponse>> resumeConversation(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long conversationId) {
        ConversationResponse conversation = conversationService.resumeConversation(userPrincipal.getId(), conversationId);
        return ResponseEntity.ok(ApiResponse.success("对话已恢复", conversation));
    }

    /**
     * 生成对话摘要
     */
    @Operation(summary = "生成对话摘要", description = "为对话生成AI摘要")
    @PostMapping("/{conversationId}/summary")
    public ResponseEntity<ApiResponse<String>> generateConversationSummary(@PathVariable Long conversationId) {
        String summary = conversationService.generateConversationSummary(conversationId);
        return ResponseEntity.ok(ApiResponse.success("摘要生成成功", summary));
    }

    /**
     * 统计对话数量
     */
    @Operation(summary = "统计对话数量", description = "统计当前用户的对话数量")
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> countMyConversations(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        long count = conversationService.countUserConversations(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success(count));
    }

    /**
     * 获取活跃对话列表
     */
    @Operation(summary = "获取活跃对话", description = "获取系统中活跃的对话列表")
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<PageResponse<ConversationResponse>>> getActiveConversations(
            @PageableDefault(size = 20, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<ConversationResponse> conversations = conversationService.getActiveConversations(pageable);
        return ResponseEntity.ok(ApiResponse.success(conversations));
    }

    /**
     * 获取最近对话列表
     */
    @Operation(summary = "获取最近对话", description = "获取用户最近的对话列表")
    @GetMapping("/recent")
    public ResponseEntity<ApiResponse<PageResponse<ConversationResponse>>> getRecentConversations(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PageableDefault(size = 10, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<ConversationResponse> conversations = conversationService.getRecentConversations(
                userPrincipal.getId(), pageable);
        return ResponseEntity.ok(ApiResponse.success(conversations));
    }

    /**
     * 批量删除对话
     */
    @Operation(summary = "批量删除对话", description = "批量删除多个对话")
    @DeleteMapping("/batch")
    public ResponseEntity<ApiResponse<String>> batchDeleteConversations(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody Long[] conversationIds) {
        conversationService.batchDeleteConversations(userPrincipal.getId(), conversationIds);
        return ResponseEntity.ok(ApiResponse.success("批量删除成功"));
    }
}
