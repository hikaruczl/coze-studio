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

import com.coze.studio.entity.ConversationVariable;

import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 对话变量数据访问接口
 * 
 * @author coze-dev
 */
@Mapper
public interface ConversationVariableRepository {

    /**
     * 根据名称和作用域查找变量
     */
    Optional<ConversationVariable> findByNameAndScopeAndEnabledTrue(String name, String scope);

    /**
     * 根据名称、作用域和对话ID查找变量
     */
    Optional<ConversationVariable> findByNameAndScopeAndConversationIdAndEnabledTrue(String name, String scope, Long conversationId);

    /**
     * 根据名称、作用域和会话ID查找变量
     */
    Optional<ConversationVariable> findByNameAndScopeAndSessionIdAndEnabledTrue(String name, String scope, String sessionId);

    /**
     * 根据名称、作用域和Bot ID查找变量
     */
    Optional<ConversationVariable> findByNameAndScopeAndBotIdAndEnabledTrue(String name, String scope, Long botId);

    /**
     * 根据名称、作用域和消息ID查找变量
     */
    Optional<ConversationVariable> findByNameAndScopeAndMessageIdAndEnabledTrue(String name, String scope, Long messageId);

    /**
     * 根据作用域查找所有变量
     */
    List<ConversationVariable> findByScopeAndEnabledTrueOrderByPriorityAscCreatedAtAsc(String scope);

    /**
     * 根据对话ID查找所有变量
     */
    List<ConversationVariable> findByConversationIdAndEnabledTrueOrderByPriorityAscCreatedAtAsc(Long conversationId);

    /**
     * 根据会话ID查找所有变量
     */
    List<ConversationVariable> findBySessionIdAndEnabledTrueOrderByPriorityAscCreatedAtAsc(String sessionId);

    /**
     * 根据Bot ID查找所有变量
     */
    List<ConversationVariable> findByBotIdAndEnabledTrueOrderByPriorityAscCreatedAtAsc(Long botId);

    /**
     * 根据消息ID查找所有变量
     */
    List<ConversationVariable> findByMessageIdAndEnabledTrueOrderByPriorityAscCreatedAtAsc(Long messageId);

    /**
     * 根据用户ID查找所有变量
     */
    // Page<ConversationVariable> findByUserIdAndEnabledTrueOrderByCreatedAtDesc(Long userId, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 根据用户ID和作用域查找变量
     */
    List<ConversationVariable> findByUserIdAndScopeAndEnabledTrueOrderByPriorityAscCreatedAtAsc(Long userId, String scope);

    /**
     * 查找系统变量
     */
    List<ConversationVariable> findByIsSystemTrueAndEnabledTrueOrderByPriorityAscCreatedAtAsc();

    /**
     * 查找敏感变量
     */
    List<ConversationVariable> findByIsSensitiveTrueAndEnabledTrueOrderByCreatedAtDesc();

    /**
     * 查找已过期的变量
     */
        List<ConversationVariable> findExpiredVariables(@Param("now") LocalDateTime now);

    /**
     * 根据标签查找变量
     */
        List<ConversationVariable> findByTagsContaining(@Param("tag") String tag);

    /**
     * 根据名称模糊查找变量
     */
    List<ConversationVariable> findByNameContainingIgnoreCaseAndEnabledTrueOrderByPriorityAscCreatedAtAsc(String name);

    /**
     * 根据来源查找变量
     */
    List<ConversationVariable> findBySourceAndEnabledTrueOrderByCreatedAtDesc(String source);

    /**
     * 统计变量数量
     */
    long countByScope(String scope);

    /**
     * 统计对话变量数量
     */
    long countByConversationIdAndEnabled(Long conversationId, Boolean enabled);

    /**
     * 统计Bot变量数量
     */
    long countByBotIdAndEnabled(Long botId, Boolean enabled);

    /**
     * 统计用户变量数量
     */
    long countByUserIdAndEnabled(Long userId, Boolean enabled);

    /**
     * 更新变量值
     */
            void updateVariableValue(@Param("id") Long id, @Param("value") String value, @Param("type") String type, 
                            @Param("accessTime") LocalDateTime accessTime);

    /**
     * 更新访问信息
     */
            void updateAccessInfo(@Param("id") Long id, @Param("accessTime") LocalDateTime accessTime);

    /**
     * 批量禁用变量
     */
            void disableVariablesByScope(@Param("scope") String scope);

    /**
     * 批量禁用对话变量
     */
            void disableVariablesByConversationId(@Param("conversationId") Long conversationId);

    /**
     * 批量禁用会话变量
     */
            void disableVariablesBySessionId(@Param("sessionId") String sessionId);

    /**
     * 批量禁用Bot变量
     */
            void disableVariablesByBotId(@Param("botId") Long botId);

    /**
     * 删除过期变量
     */
            void deleteExpiredVariables(@Param("now") LocalDateTime now);

    /**
     * 删除禁用的变量
     */
            void deleteDisabledVariables(@Param("cutoffTime") LocalDateTime cutoffTime);

    /**
     * 获取变量统计信息
     */
    List<Object[]> getVariableStatistics();

    /**
     * 获取用户变量统计
     */
    List<Object[]> getUserVariableStatistics(@Param("userId") Long userId);

    /**
     * 获取Bot变量统计
     */
    List<Object[]> getBotVariableStatistics(@Param("botId") Long botId);

    /**
     * 获取最近访问的变量
     */
        // List<ConversationVariable> findRecentlyAccessedVariables(@Param("userId") Long userId, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 获取热门变量（按访问次数排序）
     */
        // List<ConversationVariable> findPopularVariables(Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 查找相似名称的变量
     */
        List<ConversationVariable> findVariablesByNamePattern(@Param("pattern") String pattern);

    /**
     * 查找指定时间范围内创建的变量
     */
        List<ConversationVariable> findVariablesCreatedBetween(@Param("startTime") LocalDateTime startTime, 
                                                          @Param("endTime") LocalDateTime endTime);

    /**
     * 查找长时间未访问的变量
     */
        List<ConversationVariable> findUnusedVariables(@Param("threshold") LocalDateTime threshold);

    /**
     * 根据对话ID和变量来源查找变量
     */
        List<ConversationVariable> findByConversationIdAndSource(@Param("conversationId") Long conversationId, @Param("source") String source);

    /**
     * 查找对话中的用户输入变量
     */
        List<ConversationVariable> findUserInputVariables(@Param("conversationId") Long conversationId);

    /**
     * 查找对话中的Bot输出变量
     */
        List<ConversationVariable> findBotOutputVariables(@Param("conversationId") Long conversationId);

    // 基础CRUD方法
    ConversationVariable save(ConversationVariable entity);
    
    Optional<ConversationVariable> findById(Long id);
    
    List<ConversationVariable> findAll();
    
    boolean existsById(Long id);
    
    long count();
    
    void deleteById(Long id);

}