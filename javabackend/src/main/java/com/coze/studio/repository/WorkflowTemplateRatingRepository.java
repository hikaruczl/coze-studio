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

import com.coze.studio.entity.WorkflowTemplateRating;

import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 工作流模板评价数据访问接口
 * 
 * @author coze-dev
 */
@Mapper
public interface WorkflowTemplateRatingRepository {

    /**
     * 查询用户是否已评价指定模板
     */
    boolean existsByUserIdAndTemplateId(Long userId, Long templateId);

    /**
     * 根据用户ID和模板ID查询评价记录
     */
    Optional<WorkflowTemplateRating> findByUserIdAndTemplateId(Long userId, Long templateId);

    /**
     * 查询模板的评价列表
     */
    // Page<WorkflowTemplateRating> findByTemplateIdOrderByCreatedAtDesc(Long templateId, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 查询用户的评价列表
     */
    // Page<WorkflowTemplateRating> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 统计模板的评价数量
     */
    long countByTemplateId(Long templateId);

    /**
     * 统计用户的评价数量
     */
    long countByUserId(Long userId);

    /**
     * 计算模板的平均评分
     */
    Double calculateAverageRating(@Param("templateId") Long templateId);

    /**
     * 查询模板的评分分布
     */
    List<Object[]> getRatingDistribution(@Param("templateId") Long templateId);

    /**
     * 查询指定评分的评价
     */
    // Page<WorkflowTemplateRating> findByTemplateIdAndRatingOrderByCreatedAtDesc(Long templateId, Integer rating, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 查询有评论的评价
     */
        // Page<WorkflowTemplateRating> findRatingsWithComments(@Param("templateId") Long templateId, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 查询指定时间范围内的评价
     */
        List<WorkflowTemplateRating> findRatingsBetween(@Param("templateId") Long templateId,
                                                   @Param("startTime") LocalDateTime startTime, 
                                                   @Param("endTime") LocalDateTime endTime);

    /**
     * 查询最新的评价
     */
        // List<WorkflowTemplateRating> findLatestRatings(@Param("templateId") Long templateId, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 查询高评分的评价
     */
        // Page<WorkflowTemplateRating> findHighRatings(@Param("templateId") Long templateId, @Param("minRating") Integer minRating, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 查询低评分的评价
     */
        // Page<WorkflowTemplateRating> findLowRatings(@Param("templateId") Long templateId, @Param("maxRating") Integer maxRating, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 删除模板的所有评价
     */
    void deleteByTemplateId(Long templateId);

    /**
     * 删除用户的所有评价
     */
    void deleteByUserId(Long userId);

    /**
     * 查询模板评价的统计信息
     */
    Object[] getRatingStatistics(@Param("templateId") Long templateId);

    /**
     * 查询用户的评价统计
     */
    Object[] getUserRatingStatistics(@Param("userId") Long userId);

    // 基础CRUD方法
    WorkflowTemplateRating save(WorkflowTemplateRating entity);
    
    Optional<WorkflowTemplateRating> findById(Long id);
    
    List<WorkflowTemplateRating> findAll();
    
    boolean existsById(Long id);
    
    long count();
    
    void deleteById(Long id);

}