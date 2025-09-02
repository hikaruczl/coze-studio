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

package com.coze.studio.repository;

import java.util.List;
import java.util.Optional;

import com.coze.studio.entity.Message;
import com.coze.studio.entity.enums.MessageStatus;
import com.coze.studio.entity.enums.MessageType;
import com.coze.studio.entity.enums.RoleType;

import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Message 数据访问层
 * 
 * @author coze-dev
 */
@Mapper
public interface MessageRepository {

    /**
     * 根据对话ID查找消息列表（按位置排序）
     */
    List<Message> findByConversationIdOrderByPositionAsc(Long conversationId);

    /**
     * 根据对话ID分页查找消息列表
     */
    // Page<Message> findByConversationIdOrderByCreatedAtDesc(Long conversationId, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 根据对话ID和角色类型查找消息列表
     */
    List<Message> findByConversationIdAndRoleOrderByPositionAsc(Long conversationId, RoleType role);

    /**
     * 根据对话ID和消息状态查找消息列表
     */
    List<Message> findByConversationIdAndStatus(Long conversationId, MessageStatus status);

    /**
     * 根据对话ID和消息类型查找消息列表
     */
    List<Message> findByConversationIdAndMessageType(Long conversationId, MessageType messageType);

    /**
     * 根据用户ID查找消息列表
     */
    // Page<Message> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 根据运行ID查找消息列表
     */
    List<Message> findByRunIdOrderByPositionAsc(Long runId);

    /**
     * 统计对话的消息数量
     */
    long countByConversationId(Long conversationId);

    /**
     * 统计对话中未读消息数量
     */
    long countByConversationIdAndIsReadFalse(Long conversationId);

    /**
     * 查找对话中的最后一条消息
     */
        // List<Message> findLastMessageByConversationId(@Param("conversationId") Long conversationId, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 根据父消息ID查找回复消息
     */
    List<Message> findByParentMessageIdOrderByCreatedAtAsc(Long parentMessageId);

    /**
     * 查找包含工具调用的消息
     */
        List<Message> findMessagesWithToolCalls();

    /**
     * 根据时间范围查找消息
     */
        List<Message> findByCreatedAtBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 查找包含特定关键词的消息
     */
        // Page<Message> findByContentContaining(@Param("keyword") String keyword, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 根据对话ID查找最近的N条消息
     */
        // List<Message> findRecentMessagesByConversationId(@Param("conversationId") Long conversationId, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 查找失败的消息
     */
    List<Message> findByStatusOrderByCreatedAtDesc(MessageStatus status);

    // 基础CRUD方法
    Message save(Message entity);
    
    Optional<Message> findById(Long id);
    
    List<Message> findAll();
    
    boolean existsById(Long id);
    
    long count();
    
    void deleteById(Long id);

}