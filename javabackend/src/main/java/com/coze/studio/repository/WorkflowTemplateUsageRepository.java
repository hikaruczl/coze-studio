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

import com.coze.studio.entity.WorkflowTemplateUsage;

import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 工作流模板使用记录数据访问接口
 * 
 * @author coze-dev
 */
@Mapper
public interface WorkflowTemplateUsageRepository {

    /**
     * 查询模板的使用记录
     */
    // Page<WorkflowTemplateUsage> findByTemplateIdOrderByCreatedAtDesc(Long templateId, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 查询用户的使用记录
     */
    // Page<WorkflowTemplateUsage> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 统计模板的使用次数
     */
    long countByTemplateId(Long templateId);

    /**
     * 统计用户的使用次数
     */
    long countByUserId(Long userId);

    /**
     * 统计指定类型的使用次数
     */
    long countByTemplateIdAndUsageType(Long templateId, String usageType);

    /**
     * 查询指定时间范围内的使用记录
     */
        List<WorkflowTemplateUsage> findUsageBetween(@Param("templateId") Long templateId,
                                                @Param("startTime") LocalDateTime startTime, 
                                                @Param("endTime") LocalDateTime endTime);

    /**
     * 统计指定时间范围内的使用次数
     */
    long countUsageBetween(@Param("templateId") Long templateId,
                          @Param("startTime") LocalDateTime startTime,
                          @Param("endTime") LocalDateTime endTime);

    /**
     * 统计指定时间之后的使用次数
     */
    int countByTemplateIdAndUsedAtAfter(@Param("templateId") Long templateId,
                                       @Param("usedAt") LocalDateTime usedAt);

    /**
     * 按使用类型统计
     */
    List<Object[]> countByUsageType(@Param("templateId") Long templateId);

    /**
     * 按使用来源统计
     */
    List<Object[]> countByUsageSource(@Param("templateId") Long templateId);

    /**
     * 查询热门模板（按使用次数排序）
     */
        // List<Object[]> findPopularTemplatesByUsage(@Param("startTime") LocalDateTime startTime, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 查询用户最近使用的模板
     */
        List<WorkflowTemplateUsage> findRecentUsageByUser(@Param("userId") Long userId, 
                                                     @Param("startTime") LocalDateTime startTime);

    /**
     * 查询模板的日使用统计
     */
    List<Object[]> getDailyUsageStats(@Param("templateId") Long templateId, @Param("startTime") LocalDateTime startTime);

    /**
     * 查询模板的月使用统计
     */
    List<Object[]> getMonthlyUsageStats(@Param("templateId") Long templateId, @Param("startTime") LocalDateTime startTime);

    /**
     * 查询活跃用户（按使用次数排序）
     */
        // List<Object[]> findActiveUsers(@Param("startTime") LocalDateTime startTime, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 删除模板的所有使用记录
     */
    void deleteByTemplateId(Long templateId);

    /**
     * 删除用户的所有使用记录
     */
    void deleteByUserId(Long userId);

    /**
     * 查询指定工作流的创建记录
     */
    List<WorkflowTemplateUsage> findByCreatedWorkflowId(Long workflowId);

    /**
     * 统计从模板创建的工作流数量
     */
    long countCreatedWorkflows(@Param("templateId") Long templateId);

    // 基础CRUD方法
    WorkflowTemplateUsage save(WorkflowTemplateUsage entity);
    
    Optional<WorkflowTemplateUsage> findById(Long id);
    
    List<WorkflowTemplateUsage> findAll();
    
    boolean existsById(Long id);
    
    long count();
    
    void deleteById(Long id);

}