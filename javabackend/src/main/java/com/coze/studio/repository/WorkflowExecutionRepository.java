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

import com.coze.studio.entity.WorkflowExecution;

import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * WorkflowExecution 数据访问层
 * 
 * @author coze-dev
 */
@Mapper
public interface WorkflowExecutionRepository {

    /**
     * 根据执行ID查找执行记录
     */
    Optional<WorkflowExecution> findByExecutionId(String executionId);

    /**
     * 根据状态查找执行记录
     */
    List<WorkflowExecution> findByStatusOrderByStartTimeDesc(String status);

    /**
     * 根据工作流ID和状态查找执行记录
     */
    List<WorkflowExecution> findByWorkflowIdAndStatusOrderByStartTimeDesc(Long workflowId, String status);

    List<WorkflowExecution> findRunningExecutions();

        /**
     * 统计工作流的执行次数
     */
    long countByWorkflowId(Long workflowId);

    /**
     * 统计工作流的成功执行次数
     */
    long countByWorkflowIdAndStatus(Long workflowId, String status);

        /**
     * 根据对话ID查找执行记录
     */
    List<WorkflowExecution> findByConversationIdOrderByStartTimeDesc(Long conversationId);

    /**
     * 根据消息ID查找执行记录
     */
    Optional<WorkflowExecution> findByMessageId(Long messageId);

    // TODO: 查询方法需要在MyBatis XML中实现

    // 基础CRUD方法
    WorkflowExecution save(WorkflowExecution entity);
    
    Optional<WorkflowExecution> findById(Long id);
    
    List<WorkflowExecution> findAll();
    
    boolean existsById(Long id);
    
    long count();
    
    void deleteById(Long id);


    // 自定义查询方法
    List<WorkflowExecution> findByStatusIn(List<String> statuses);
    List<WorkflowExecution> findByWorkflowIdOrderByCreatedAtDesc(Long workflowId);
    List<WorkflowExecution> findTop50ByWorkflowIdAndStatusOrderByCreatedAtDesc(Long workflowId, String status);
    List<WorkflowExecution> findExecutionsBetween(java.time.LocalDateTime start, java.time.LocalDateTime end);
    void delete(WorkflowExecution execution);
    Double getAverageExecutionTime(Long workflowId);

}