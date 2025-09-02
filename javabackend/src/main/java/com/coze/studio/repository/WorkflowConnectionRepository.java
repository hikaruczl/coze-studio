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

import com.coze.studio.entity.WorkflowConnection;
import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * WorkflowConnection 数据访问层
 * 
 * @author coze-dev
 */
@Mapper
public interface WorkflowConnectionRepository {

    /**
     * 根据工作流ID查找所有连接
     */
    List<WorkflowConnection> findByWorkflowIdAndEnabledTrueOrderByPriority(Long workflowId);

    /**
     * 根据工作流ID和连接ID查找连接
     */
    Optional<WorkflowConnection> findByWorkflowIdAndConnectionIdAndEnabledTrue(Long workflowId, String connectionId);

    /**
     * 根据源节点ID查找所有出站连接
     */
    List<WorkflowConnection> findByWorkflowIdAndSourceNodeIdAndEnabledTrueOrderByPriority(Long workflowId, String sourceNodeId);

    /**
     * 根据目标节点ID查找所有入站连接
     */
    List<WorkflowConnection> findByWorkflowIdAndTargetNodeIdAndEnabledTrueOrderByPriority(Long workflowId, String targetNodeId);

    /**
     * 根据连接类型查找连接
     */
    List<WorkflowConnection> findByWorkflowIdAndConnectionTypeAndEnabledTrue(Long workflowId, String connectionType);

    /**
     * 查找两个节点之间的连接
     */
        List<WorkflowConnection> findConnectionsBetweenNodes(@Param("workflowId") Long workflowId,
                                                        @Param("sourceNodeId") String sourceNodeId,
                                                        @Param("targetNodeId") String targetNodeId);

    /**
     * 查找节点的所有连接（包括入站和出站）
     */
    List<WorkflowConnection> findConnectionsByNodeId(@Param("workflowId") Long workflowId,
                                                    @Param("nodeId") String nodeId);

    /**
     * 统计工作流的连接数量
     */
    long countByWorkflowIdAndEnabledTrue(Long workflowId);

    /**
     * 根据工作流ID删除所有连接
     */
    void deleteByWorkflowId(Long workflowId);

    /**
     * 查找循环连接（检测环路）
     */
        List<WorkflowConnection> findPotentialCycles(@Param("workflowId") Long workflowId,
                                               @Param("nodeIds") List<String> nodeIds);

    /**
     * 查找条件连接
     */
        List<WorkflowConnection> findConditionalConnections(@Param("workflowId") Long workflowId);

    // 基础CRUD方法
    WorkflowConnection save(WorkflowConnection entity);
    
    Optional<WorkflowConnection> findById(Long id);
    
    List<WorkflowConnection> findAll();
    
    boolean existsById(Long id);
    
    long count();
    
    void deleteById(Long id);

}