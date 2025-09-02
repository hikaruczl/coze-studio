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

import com.coze.studio.entity.KnowledgeQuery;

import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 知识库查询记录数据访问接口
 * 
 * @author coze-dev
 */
@Mapper
public interface KnowledgeQueryRepository {

    /**
     * 根据用户ID查询查询记录
     */
    // Page<KnowledgeQuery> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 根据知识库ID查询查询记录
     */
    // Page<KnowledgeQuery> findByKnowledgeBaseIdOrderByCreatedAtDesc(Long knowledgeBaseId, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 根据会话ID查询查询记录
     */
    List<KnowledgeQuery> findBySessionIdOrderByCreatedAtAsc(String sessionId);

    /**
     * 根据对话ID查询查询记录
     */
    List<KnowledgeQuery> findByConversationIdOrderByCreatedAtAsc(Long conversationId);

    /**
     * 统计用户的查询次数
     */
    long countByUserId(Long userId);

    /**
     * 统计知识库的查询次数
     */
    long countByKnowledgeBaseId(Long knowledgeBaseId);

    /**
     * 统计指定状态的查询次数
     */
    long countByKnowledgeBaseIdAndStatus(Long knowledgeBaseId, String status);

    /**
     * 查询指定时间范围内的查询记录
     */
        List<KnowledgeQuery> findQueriesBetween(@Param("knowledgeBaseId") Long knowledgeBaseId,
                                           @Param("startTime") LocalDateTime startTime,
                                           @Param("endTime") LocalDateTime endTime);

    /**
     * 统计指定时间范围内的查询次数
     */
        long countQueriesBetween(@Param("knowledgeBaseId") Long knowledgeBaseId,
                            @Param("startTime") LocalDateTime startTime,
                            @Param("endTime") LocalDateTime endTime);

    /**
     * 按查询类型统计
     */
    List<Object[]> countByQueryType(@Param("knowledgeBaseId") Long knowledgeBaseId);

    /**
     * 按查询来源统计
     */
    List<Object[]> countByQuerySource(@Param("knowledgeBaseId") Long knowledgeBaseId);

    /**
     * 查询热门查询文本
     */
        // List<Object[]> findPopularQueries(@Param("knowledgeBaseId") Long knowledgeBaseId, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 查询慢查询
     */
        // List<KnowledgeQuery> findSlowQueries(@Param("threshold") Long threshold, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 查询失败的查询
     */
    // List<KnowledgeQuery> findByKnowledgeBaseIdAndStatusOrderByCreatedAtDesc(Long knowledgeBaseId, String status, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 查询用户最近的查询
     */
        List<KnowledgeQuery> findRecentQueriesByUser(@Param("userId") Long userId,
                                                 @Param("since") LocalDateTime since);

    /**
     * 查询知识库的日查询统计
     */
    List<Object[]> getDailyQueryStats(@Param("knowledgeBaseId") Long knowledgeBaseId, @Param("startTime") LocalDateTime startTime);

    /**
     * 查询知识库的小时查询统计
     */
    List<Object[]> getHourlyQueryStats(@Param("knowledgeBaseId") Long knowledgeBaseId, @Param("startTime") LocalDateTime startTime);

    /**
     * 查询活跃用户（按查询次数排序）
     */
        // List<Object[]> findActiveUsers(@Param("knowledgeBaseId") Long knowledgeBaseId, @Param("startTime") LocalDateTime startTime, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 删除知识库的所有查询记录
     */
    void deleteByKnowledgeBaseId(Long knowledgeBaseId);

    /**
     * 删除用户的所有查询记录
     */
    void deleteByUserId(Long userId);

    /**
     * 查询知识库的查询统计
     */
    Object[] getQueryStatistics(@Param("knowledgeBaseId") Long knowledgeBaseId);

    /**
     * 查询用户的查询统计
     */
    Object[] getUserQueryStatistics(@Param("userId") Long userId);

    /**
     * 查询查询频率较高的知识库
     */
    List<Object[]> findHighQueryVolumeKnowledgeBases(@Param("startTime") LocalDateTime startTime,
                                                     @Param("minQueries") long minQueries);

    /**
     * 查询相似的查询文本
     */
    // TODO: 查询方法需要在MyBatis XML中实现
    List<KnowledgeQuery> findSimilarQueries(@Param("knowledgeBaseId") Long knowledgeBaseId,
                                           @Param("queryText") String queryText);

    // 基础CRUD方法
    KnowledgeQuery save(KnowledgeQuery entity);
    
    Optional<KnowledgeQuery> findById(Long id);
    
    List<KnowledgeQuery> findAll();
    
    boolean existsById(Long id);
    
    long count();
    
    void deleteById(Long id);

}