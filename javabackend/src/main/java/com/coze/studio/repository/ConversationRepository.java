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

import com.coze.studio.entity.Conversation;
import com.coze.studio.entity.enums.ConversationStatus;
import com.coze.studio.entity.enums.Scene;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Conversation 数据访问层
 * 
 * @author coze-dev
 */
@Mapper
public interface ConversationRepository {

    /**
     * 根据创建者ID查找对话列表
     * TODO: 分页查询需要在MyBatis XML中实现
     */
    // Page<Conversation> findByCreatorIdOrderByUpdatedAtDesc(Long creatorId, Pageable pageable);

    /**
     * 根据代理ID查找对话列表
     * TODO: 分页查询需要在MyBatis XML中实现
     */
    // Page<Conversation> findByAgentIdOrderByUpdatedAtDesc(Long agentId, Pageable pageable);

    /**
     * 根据创建者ID和状态查找对话列表
     */
    List<Conversation> findByCreatorIdAndStatus(Long creatorId, ConversationStatus status);

    /**
     * 根据场景类型查找对话列表
     */
    List<Conversation> findByScene(Scene scene);

    /**
     * 根据创建者ID和代理ID查找对话列表
     * TODO: 分页查询需要在MyBatis XML中实现
     */
    // Page<Conversation> findByCreatorIdAndAgentIdOrderByUpdatedAtDesc(Long creatorId, Long agentId, Pageable pageable);

    /**
     * 查找置顶的对话
     */
    List<Conversation> findByCreatorIdAndPinnedTrueOrderByUpdatedAtDesc(Long creatorId);

    /**
     * 统计创建者的对话数量
     */
    long countByCreatorId(Long creatorId);

    /**
     * 统计代理的对话数量
     */
    long countByAgentId(Long agentId);

    /**
     * 查找活跃的对话
     */
        List<Conversation> findActiveConversations();

    /**
     * 根据时间范围查找对话
     */
        List<Conversation> findByCreatedAtBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 查找包含特定关键词的对话
     * TODO: 需要在MyBatis XML中实现
     */
    // Page<Conversation> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    /**
     * 根据创建者ID查找最近的对话
     * TODO: 需要在MyBatis XML中实现
     */
    // List<Conversation> findRecentConversations(@Param("creatorId") Long creatorId, Pageable pageable);

    /**
     * 查找需要清理的对话（超过指定天数且状态为已结束）
     * TODO: 需要在MyBatis XML中实现
     */
    // List<Conversation> findConversationsToCleanup(@Param("cutoffTime") LocalDateTime cutoffTime);

    // 基础CRUD方法
    Conversation save(Conversation entity);
    
    Optional<Conversation> findById(Long id);
    
    List<Conversation> findAll();
    
    boolean existsById(Long id);
    
    long count();
    
    void deleteById(Long id);


    // 自定义查询方法
    List<Conversation> findByCreatorIdOrderByUpdatedAtDesc(Long creatorId);
    List<Conversation> findByAgentIdOrderByUpdatedAtDesc(Long agentId);
    List<Conversation> findRecentConversations(Long userId);
    List<Conversation> findConversationsToCleanup(java.time.LocalDateTime cutoffTime);

}