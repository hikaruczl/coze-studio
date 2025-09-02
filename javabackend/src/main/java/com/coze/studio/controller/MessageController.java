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
import com.coze.studio.dto.message.*;
import com.coze.studio.security.UserPrincipal;
import com.coze.studio.service.MessageService;
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

import java.util.List;

/**
 * 消息管理控制器
 * 处理消息相关的操作
 * 
 * @author coze-dev
 */
@Tag(name = "消息管理", description = "消息管理相关接口")
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    /**
     * 发送消息
     */
    @Operation(summary = "发送消息", description = "在对话中发送一条消息")
    @PostMapping
    public ResponseEntity<ApiResponse<MessageResponse>> sendMessage(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody SendMessageRequest request) {
        MessageResponse message = messageService.sendMessage(userPrincipal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("消息发送成功", message));
    }

    /**
     * 获取消息详情
     */
    @Operation(summary = "获取消息详情", description = "根据ID获取消息的详细信息")
    @GetMapping("/{messageId}")
    public ResponseEntity<ApiResponse<MessageResponse>> getMessageById(@PathVariable Long messageId) {
        MessageResponse message = messageService.getMessageById(messageId);
        return ResponseEntity.ok(ApiResponse.success(message));
    }

    /**
     * 删除消息
     */
    @Operation(summary = "删除消息", description = "删除指定的消息")
    @DeleteMapping("/{messageId}")
    public ResponseEntity<ApiResponse<String>> deleteMessage(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long messageId) {
        messageService.deleteMessage(userPrincipal.getId(), messageId);
        return ResponseEntity.ok(ApiResponse.success("消息删除成功"));
    }

    /**
     * 分页查询消息列表
     */
    @Operation(summary = "查询消息列表", description = "分页查询消息列表")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<MessageResponse>>> getMessages(
            MessageQueryRequest queryRequest,
            @PageableDefault(size = 50, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable) {
        PageResponse<MessageResponse> messages = messageService.getMessages(queryRequest, pageable);
        return ResponseEntity.ok(ApiResponse.success(messages));
    }

    /**
     * 获取对话的消息列表
     */
    @Operation(summary = "获取对话消息", description = "获取指定对话的所有消息")
    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<ApiResponse<List<MessageResponse>>> getConversationMessages(
            @PathVariable Long conversationId) {
        List<MessageResponse> messages = messageService.getConversationMessages(conversationId);
        return ResponseEntity.ok(ApiResponse.success(messages));
    }

    /**
     * 分页获取对话的消息列表
     */
    @Operation(summary = "分页获取对话消息", description = "分页获取指定对话的消息列表")
    @GetMapping("/conversation/{conversationId}/page")
    public ResponseEntity<ApiResponse<PageResponse<MessageResponse>>> getConversationMessagesPage(
            @PathVariable Long conversationId,
            @PageableDefault(size = 50, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable) {
        PageResponse<MessageResponse> messages = messageService.getConversationMessages(conversationId, pageable);
        return ResponseEntity.ok(ApiResponse.success(messages));
    }

    /**
     * 获取对话的最近消息
     */
    @Operation(summary = "获取最近消息", description = "获取对话的最近N条消息")
    @GetMapping("/conversation/{conversationId}/recent")
    public ResponseEntity<ApiResponse<List<MessageResponse>>> getRecentMessages(
            @PathVariable Long conversationId,
            @RequestParam(defaultValue = "10") int limit) {
        List<MessageResponse> messages = messageService.getRecentMessages(conversationId, limit);
        return ResponseEntity.ok(ApiResponse.success(messages));
    }

    /**
     * 标记消息为已读
     */
    @Operation(summary = "标记消息已读", description = "标记指定消息为已读")
    @PostMapping("/{messageId}/read")
    public ResponseEntity<ApiResponse<String>> markMessageAsRead(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long messageId) {
        messageService.markMessageAsRead(userPrincipal.getId(), messageId);
        return ResponseEntity.ok(ApiResponse.success("消息已标记为已读"));
    }

    /**
     * 批量标记消息为已读
     */
    @Operation(summary = "批量标记已读", description = "批量标记多条消息为已读")
    @PostMapping("/batch-read")
    public ResponseEntity<ApiResponse<String>> markMessagesAsRead(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody Long[] messageIds) {
        messageService.markMessagesAsRead(userPrincipal.getId(), messageIds);
        return ResponseEntity.ok(ApiResponse.success("消息已批量标记为已读"));
    }

    /**
     * 标记对话中所有消息为已读
     */
    @Operation(summary = "标记对话已读", description = "标记对话中所有消息为已读")
    @PostMapping("/conversation/{conversationId}/read-all")
    public ResponseEntity<ApiResponse<String>> markConversationMessagesAsRead(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long conversationId) {
        messageService.markConversationMessagesAsRead(userPrincipal.getId(), conversationId);
        return ResponseEntity.ok(ApiResponse.success("对话中所有消息已标记为已读"));
    }

    /**
     * 获取未读消息数量
     */
    @Operation(summary = "获取未读消息数", description = "获取对话中未读消息的数量")
    @GetMapping("/conversation/{conversationId}/unread-count")
    public ResponseEntity<ApiResponse<Long>> getUnreadMessageCount(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long conversationId) {
        long count = messageService.getUnreadMessageCount(userPrincipal.getId(), conversationId);
        return ResponseEntity.ok(ApiResponse.success(count));
    }

    /**
     * 重新生成消息
     */
    @Operation(summary = "重新生成消息", description = "重新生成AI消息")
    @PostMapping("/{messageId}/regenerate")
    public ResponseEntity<ApiResponse<MessageResponse>> regenerateMessage(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long messageId) {
        MessageResponse message = messageService.regenerateMessage(userPrincipal.getId(), messageId);
        return ResponseEntity.ok(ApiResponse.success("消息重新生成成功", message));
    }

    /**
     * 编辑消息
     */
    @Operation(summary = "编辑消息", description = "编辑用户消息的内容")
    @PutMapping("/{messageId}")
    public ResponseEntity<ApiResponse<MessageResponse>> editMessage(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long messageId,
            @RequestParam String newContent) {
        MessageResponse message = messageService.editMessage(userPrincipal.getId(), messageId, newContent);
        return ResponseEntity.ok(ApiResponse.success("消息编辑成功", message));
    }

    /**
     * 回复消息
     */
    @Operation(summary = "回复消息", description = "回复指定的消息")
    @PostMapping("/{messageId}/reply")
    public ResponseEntity<ApiResponse<MessageResponse>> replyToMessage(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long messageId,
            @Valid @RequestBody SendMessageRequest request) {
        MessageResponse message = messageService.replyToMessage(userPrincipal.getId(), messageId, request);
        return ResponseEntity.ok(ApiResponse.success("回复发送成功", message));
    }

    /**
     * 获取消息的回复列表
     */
    @Operation(summary = "获取消息回复", description = "获取指定消息的所有回复")
    @GetMapping("/{messageId}/replies")
    public ResponseEntity<ApiResponse<List<MessageResponse>>> getMessageReplies(@PathVariable Long messageId) {
        List<MessageResponse> replies = messageService.getMessageReplies(messageId);
        return ResponseEntity.ok(ApiResponse.success(replies));
    }

    /**
     * 搜索消息
     */
    @Operation(summary = "搜索消息", description = "根据关键词搜索消息")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<MessageResponse>>> searchMessages(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam String keyword,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<MessageResponse> messages = messageService.searchMessages(userPrincipal.getId(), keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(messages));
    }

    /**
     * 获取包含工具调用的消息
     */
    @Operation(summary = "获取工具调用消息", description = "获取包含工具调用的消息列表")
    @GetMapping("/conversation/{conversationId}/tool-calls")
    public ResponseEntity<ApiResponse<PageResponse<MessageResponse>>> getMessagesWithToolCalls(
            @PathVariable Long conversationId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<MessageResponse> messages = messageService.getMessagesWithToolCalls(conversationId, pageable);
        return ResponseEntity.ok(ApiResponse.success(messages));
    }

    /**
     * 统计消息数量
     */
    @Operation(summary = "统计消息数量", description = "统计对话中的消息数量")
    @GetMapping("/conversation/{conversationId}/count")
    public ResponseEntity<ApiResponse<Long>> countConversationMessages(@PathVariable Long conversationId) {
        long count = messageService.countConversationMessages(conversationId);
        return ResponseEntity.ok(ApiResponse.success(count));
    }

    /**
     * 批量删除消息
     */
    @Operation(summary = "批量删除消息", description = "批量删除多条消息")
    @DeleteMapping("/batch")
    public ResponseEntity<ApiResponse<String>> batchDeleteMessages(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody Long[] messageIds) {
        messageService.batchDeleteMessages(userPrincipal.getId(), messageIds);
        return ResponseEntity.ok(ApiResponse.success("批量删除成功"));
    }
}
