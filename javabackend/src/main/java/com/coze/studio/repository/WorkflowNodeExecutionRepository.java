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

import com.coze.studio.entity.WorkflowNodeExecution;
import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * WorkflowNodeExecution 数据访问层
 * 
 * @author coze-dev
 */
@Mapper
public interface WorkflowNodeExecutionRepository {

    /**
     * 根据执行ID查找所有节点执行记录
     */
    List<WorkflowNodeExecution> findByExecutionIdOrderByExecutionOrder(Long executionId);

    /**
     * 根据执行ID和节点ID查找节点执行记录
     */
    Optional<WorkflowNodeExecution> findByExecutionIdAndNodeId(Long executionId, String nodeId);

    /**
     * 根据执行ID和状态查找节点执行记录
     */
    List<WorkflowNodeExecution> findByExecutionIdAndStatusOrderByExecutionOrder(Long executionId, String status);

    /**
     * 根据节点类型查找执行记录
     */
    List<WorkflowNodeExecution> findByNodeTypeAndStatusOrderByStartTimeDesc(String nodeType, String status);

    /**
     * 查找正在执行的节点
     */
        List<WorkflowNodeExecution> findRunningNodesByExecution(@Param("executionId") Long executionId);

    /**
     * 查找失败的节点执行记录
     */
        List<WorkflowNodeExecution> findFailedNodesByExecution(@Param("executionId") Long executionId);

    /**
     * 统计执行中的节点数量
     */
    long countByExecutionIdAndStatus(Long executionId, String status);

    /**
     * 查找最后执行的节点
     */
        List<WorkflowNodeExecution> findLastExecutedNodes(@Param("executionId") Long executionId);

    /**
     * 查找重试次数最多的节点
     */
        List<WorkflowNodeExecution> findMostRetriedNodes(@Param("executionId") Long executionId);

    /**
     * 计算节点平均执行时间
     */
    Double getAverageNodeExecutionTime(@Param("nodeType") String nodeType);

    /**
     * 查找超时的节点执行
     */
        List<WorkflowNodeExecution> findTimeoutNodeExecutions(@Param("timeoutTime") LocalDateTime timeoutTime);

    /**
     * 根据执行ID删除所有节点执行记录
     */
    void deleteByExecutionId(Long executionId);

    /**
     * 统计节点执行状态分布
     */
    List<Object[]> countNodeExecutionsByStatus(@Param("executionId") Long executionId);

    /**
     * 查找执行时间最长的节点
     */
        List<WorkflowNodeExecution> findLongestRunningNodes(@Param("executionId") Long executionId);

    // 基础CRUD方法
    WorkflowNodeExecution save(WorkflowNodeExecution entity);
    
    Optional<WorkflowNodeExecution> findById(Long id);
    
    List<WorkflowNodeExecution> findAll();
    
    boolean existsById(Long id);
    
    long count();
    
    void deleteById(Long id);


    // 自定义查询方法
    List<WorkflowNodeExecution> findByExecutionIdOrderByCreatedAtDesc(Long executionId);
    List<WorkflowNodeExecution> findByWorkflowIdOrderByCreatedAtDesc(Long workflowId);
    List<WorkflowNodeExecution> findTop100ByWorkflowIdOrderByCreatedAtDesc(Long workflowId);

}