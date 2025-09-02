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

import com.coze.studio.entity.WorkflowVariable;

import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 工作流变量数据访问接口
 * 
 * @author coze-dev
 */
@Mapper
public interface WorkflowVariableRepository {

    /**
     * 根据名称和作用域查找变量
     */
    Optional<WorkflowVariable> findByNameAndScopeAndEnabledTrue(String name, String scope);

    /**
     * 根据名称、作用域和工作流ID查找变量
     */
    Optional<WorkflowVariable> findByNameAndScopeAndWorkflowIdAndEnabledTrue(String name, String scope, Long workflowId);

    /**
     * 根据名称、作用域和执行ID查找变量
     */
    Optional<WorkflowVariable> findByNameAndScopeAndExecutionIdAndEnabledTrue(String name, String scope, String executionId);

    /**
     * 根据名称、作用域和会话ID查找变量
     */
    Optional<WorkflowVariable> findByNameAndScopeAndSessionIdAndEnabledTrue(String name, String scope, String sessionId);

    /**
     * 根据名称、作用域和节点ID查找变量
     */
    Optional<WorkflowVariable> findByNameAndScopeAndNodeIdAndEnabledTrue(String name, String scope, String nodeId);

    /**
     * 根据作用域查找所有变量
     */
    List<WorkflowVariable> findByScopeAndEnabledTrueOrderByPriorityAscCreatedAtAsc(String scope);

    /**
     * 根据工作流ID查找所有变量
     */
    List<WorkflowVariable> findByWorkflowIdAndEnabledTrueOrderByPriorityAscCreatedAtAsc(Long workflowId);

    /**
     * 根据执行ID查找所有变量
     */
    List<WorkflowVariable> findByExecutionIdAndEnabledTrueOrderByPriorityAscCreatedAtAsc(String executionId);

    /**
     * 根据会话ID查找所有变量
     */
    List<WorkflowVariable> findBySessionIdAndEnabledTrueOrderByPriorityAscCreatedAtAsc(String sessionId);

    /**
     * 根据节点ID查找所有变量
     */
    List<WorkflowVariable> findByNodeIdAndEnabledTrueOrderByPriorityAscCreatedAtAsc(String nodeId);

    /**
     * 根据用户ID查找所有变量
     */
    // Page<WorkflowVariable> findByUserIdAndEnabledTrueOrderByCreatedAtDesc(Long userId, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 根据用户ID和作用域查找变量
     */
    List<WorkflowVariable> findByUserIdAndScopeAndEnabledTrueOrderByPriorityAscCreatedAtAsc(Long userId, String scope);

    /**
     * 查找系统变量
     */
    List<WorkflowVariable> findByIsSystemTrueAndEnabledTrueOrderByPriorityAscCreatedAtAsc();

    /**
     * 查找敏感变量
     */
    List<WorkflowVariable> findByIsSensitiveTrueAndEnabledTrueOrderByCreatedAtDesc();

    /**
     * 查找已过期的变量
     */
        List<WorkflowVariable> findExpiredVariables(@Param("now") LocalDateTime now);

    /**
     * 根据标签查找变量
     */
        List<WorkflowVariable> findByTagsContaining(@Param("tag") String tag);

    /**
     * 根据名称模糊查找变量
     */
    List<WorkflowVariable> findByNameContainingIgnoreCaseAndEnabledTrueOrderByPriorityAscCreatedAtAsc(String name);

    /**
     * 统计变量数量
     */
    long countByScope(String scope);

    /**
     * 统计工作流变量数量
     */
    long countByWorkflowIdAndEnabled(Long workflowId, Boolean enabled);

    /**
     * 统计执行变量数量
     */
    long countByExecutionIdAndEnabled(String executionId, Boolean enabled);

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
     * 批量禁用工作流变量
     */
            void disableVariablesByWorkflowId(@Param("workflowId") Long workflowId);

    /**
     * 批量禁用执行变量
     */
            void disableVariablesByExecutionId(@Param("executionId") String executionId);

    /**
     * 批量禁用会话变量
     */
            void disableVariablesBySessionId(@Param("sessionId") String sessionId);

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
     * 获取最近访问的变量
     */
        // List<WorkflowVariable> findRecentlyAccessedVariables(@Param("userId") Long userId, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 获取热门变量（按访问次数排序）
     */
        // List<WorkflowVariable> findPopularVariables(Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 查找相似名称的变量
     */
        List<WorkflowVariable> findVariablesByNamePattern(@Param("pattern") String pattern);

    /**
     * 查找指定时间范围内创建的变量
     */
        List<WorkflowVariable> findVariablesCreatedBetween(@Param("startTime") LocalDateTime startTime, 
                                                      @Param("endTime") LocalDateTime endTime);

    /**
     * 查找长时间未访问的变量
     */
        List<WorkflowVariable> findUnusedVariables(@Param("threshold") LocalDateTime threshold);

    // 基础CRUD方法
    WorkflowVariable save(WorkflowVariable entity);
    
    Optional<WorkflowVariable> findById(Long id);
    
    List<WorkflowVariable> findAll();
    
    boolean existsById(Long id);
    
    long count();
    
    void deleteById(Long id);

}