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

package com.coze.studio.service;

import com.coze.studio.dto.common.PageResponse;
import com.coze.studio.dto.message.*;
import com.coze.studio.entity.Message;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 消息服务接口
 *
 * @author coze-dev
 */
public interface MessageService {

    /**
     * 发送消息
     */
    MessageResponse sendMessage(Long userId, SendMessageRequest request);

    /**
     * 发送消息（流式响应）
     */
    void sendMessageStream(Long userId, SendMessageRequest request, MessageStreamCallback callback);

    /**
     * 根据ID获取消息
     */
    MessageResponse getMessageById(Long messageId);

    /**
     * 根据ID获取消息实体
     */
    Message getMessageEntityById(Long messageId);

    /**
     * 删除消息
     */
    void deleteMessage(Long userId, Long messageId);

    /**
     * 分页查询消息列表
     */
    PageResponse<MessageResponse> getMessages(MessageQueryRequest request, Pageable pageable);

    /**
     * 获取对话的消息列表
     */
    List<MessageResponse> getConversationMessages(Long conversationId);

    /**
     * 获取对话的消息列表（分页）
     */
    PageResponse<MessageResponse> getConversationMessages(Long conversationId, Pageable pageable);

    /**
     * 获取对话的最近消息
     */
    List<MessageResponse> getRecentMessages(Long conversationId, int limit);

    /**
     * 标记消息为已读
     */
    void markMessageAsRead(Long userId, Long messageId);

    /**
     * 批量标记消息为已读
     */
    void markMessagesAsRead(Long userId, Long[] messageIds);

    /**
     * 标记对话中所有消息为已读
     */
    void markConversationMessagesAsRead(Long userId, Long conversationId);

    // 兼容：清除上下文使用的简化接口
    default void markConversationAsRead(Long conversationId) {
        // 建议调用方迁移到 markConversationMessagesAsRead(userId, conversationId)
    }

    /**
     * 获取未读消息数量
     */
    long getUnreadMessageCount(Long userId, Long conversationId);

    /**
     * 重新生成消息
     */
    MessageResponse regenerateMessage(Long userId, Long messageId);

    /**
     * 编辑消息
     */
    MessageResponse editMessage(Long userId, Long messageId, String newContent);

    /**
     * 回复消息
     */
    MessageResponse replyToMessage(Long userId, Long parentMessageId, SendMessageRequest request);

    /**
     * 获取消息的回复列表
     */
    List<MessageResponse> getMessageReplies(Long messageId);

    /**
     * 搜索消息
     */
    PageResponse<MessageResponse> searchMessages(Long userId, String keyword, Pageable pageable);

    /**
     * 获取包含工具调用的消息
     */
    PageResponse<MessageResponse> getMessagesWithToolCalls(Long conversationId, Pageable pageable);

    /**
     * 统计对话的消息数量
     */
    long countConversationMessages(Long conversationId);

    /**
     * 统计用户的消息数量
     */
    long countUserMessages(Long userId);

    /**
     * 检查用户是否有消息的访问权限
     */
    boolean hasAccessToMessage(Long userId, Long messageId);

    /**
     * 批量删除消息
     */
    void batchDeleteMessages(Long userId, Long[] messageIds);

    /**
     * 消息流式回调接口
     */
    interface MessageStreamCallback {
        void onStart(MessageResponse message);
        void onContent(String content);
        void onToolCall(MessageResponse.ToolCall toolCall);
        void onComplete(MessageResponse message);
        void onError(Exception error);
    }
}
