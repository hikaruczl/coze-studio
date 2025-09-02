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
import com.coze.studio.dto.conversation.*;
import com.coze.studio.entity.Conversation;
import org.springframework.data.domain.Pageable;

/**
 * 对话服务接口
 * 
 * @author coze-dev
 */
public interface ConversationService {

    /**
     * 创建对话
     */
    ConversationResponse createConversation(Long userId, CreateConversationRequest request);

    /**
     * 更新对话
     */
    ConversationResponse updateConversation(Long userId, Long conversationId, UpdateConversationRequest request);

    /**
     * 根据ID获取对话
     */
    ConversationResponse getConversationById(Long conversationId);

    /**
     * 根据ID获取对话实体
     */
    Conversation getConversationEntityById(Long conversationId);

    /**
     * 删除对话
     */
    void deleteConversation(Long userId, Long conversationId);

    /**
     * 分页查询对话列表
     */
    PageResponse<ConversationResponse> getConversations(ConversationQueryRequest request, Pageable pageable);

    /**
     * 获取用户的对话列表
     */
    PageResponse<ConversationResponse> getUserConversations(Long userId, Pageable pageable);

    /**
     * 获取Bot的对话列表
     */
    PageResponse<ConversationResponse> getBotConversations(Long botId, Pageable pageable);

    /**
     * 置顶/取消置顶对话
     */
    ConversationResponse toggleConversationPin(Long userId, Long conversationId, boolean pinned);

    /**
     * 结束对话
     */
    ConversationResponse endConversation(Long userId, Long conversationId);

    /**
     * 暂停对话
     */
    ConversationResponse pauseConversation(Long userId, Long conversationId);

    /**
     * 恢复对话
     */
    ConversationResponse resumeConversation(Long userId, Long conversationId);

    /**
     * 生成对话摘要
     */
    String generateConversationSummary(Long conversationId);

    /**
     * 更新对话摘要
     */
    ConversationResponse updateConversationSummary(Long conversationId, String summary);

    /**
     * 检查用户是否有对话的访问权限
     */
    boolean hasAccessToConversation(Long userId, Long conversationId);

    /**
     * 检查用户是否是对话的创建者
     */
    boolean isConversationCreator(Long userId, Long conversationId);

    /**
     * 统计用户的对话数量
     */
    long countUserConversations(Long userId);

    /**
     * 统计Bot的对话数量
     */
    long countBotConversations(Long botId);

    /**
     * 获取活跃对话列表
     */
    PageResponse<ConversationResponse> getActiveConversations(Pageable pageable);

    /**
     * 获取最近的对话列表
     */
    PageResponse<ConversationResponse> getRecentConversations(Long userId, Pageable pageable);

    /**
     * 清理过期对话
     */
    void cleanupExpiredConversations(int daysOld);

    /**
     * 批量删除对话
     */
    void batchDeleteConversations(Long userId, Long[] conversationIds);
}
