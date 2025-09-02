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

import com.coze.studio.entity.WorkflowTemplateFavorite;

import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 工作流模板收藏数据访问接口
 * 
 * @author coze-dev
 */
@Mapper
public interface WorkflowTemplateFavoriteRepository {

    /**
     * 查询用户是否收藏了指定模板
     */
    boolean existsByUserIdAndTemplateId(Long userId, Long templateId);

    /**
     * 根据用户ID和模板ID查询收藏记录
     */
    Optional<WorkflowTemplateFavorite> findByUserIdAndTemplateId(Long userId, Long templateId);

    /**
     * 查询用户的收藏列表
     */
    // Page<WorkflowTemplateFavorite> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 查询模板的收藏列表
     */
    // Page<WorkflowTemplateFavorite> findByTemplateIdOrderByCreatedAtDesc(Long templateId, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 统计模板的收藏数量
     */
    long countByTemplateId(Long templateId);

    /**
     * 统计用户的收藏数量
     */
    long countByUserId(Long userId);

    /**
     * 删除用户对模板的收藏
     */
    void deleteByUserIdAndTemplateId(Long userId, Long templateId);

    /**
     * 查询指定时间范围内的收藏记录
     */
        List<WorkflowTemplateFavorite> findFavoritesBetween(@Param("startTime") LocalDateTime startTime, 
                                                       @Param("endTime") LocalDateTime endTime);

    /**
     * 查询热门收藏模板（按收藏数量排序）
     */
        // List<Object[]> findPopularTemplatesByFavorites(@Param("startTime") LocalDateTime startTime, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 查询用户最近收藏的模板
     */
        List<WorkflowTemplateFavorite> findRecentFavoritesByUser(@Param("userId") Long userId, 
                                                            @Param("startTime") LocalDateTime startTime);

    /**
     * 批量删除模板的所有收藏记录
     */
    void deleteByTemplateId(Long templateId);

    /**
     * 批量删除用户的所有收藏记录
     */
    void deleteByUserId(Long userId);

    // 基础CRUD方法
    WorkflowTemplateFavorite save(WorkflowTemplateFavorite entity);
    
    Optional<WorkflowTemplateFavorite> findById(Long id);
    
    List<WorkflowTemplateFavorite> findAll();
    
    boolean existsById(Long id);
    
    long count();
    
    void deleteById(Long id);

}