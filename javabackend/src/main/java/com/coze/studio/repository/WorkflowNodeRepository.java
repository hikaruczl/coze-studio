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

import com.coze.studio.entity.WorkflowNode;
import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * WorkflowNode 数据访问层
 * 
 * @author coze-dev
 */
@Mapper
public interface WorkflowNodeRepository {

    /**
     * 根据工作流ID查找所有节点
     */
    List<WorkflowNode> findByWorkflowIdAndEnabledTrueOrderBySortOrder(Long workflowId);

    /**
     * 根据工作流ID和节点ID查找节点
     */
    Optional<WorkflowNode> findByWorkflowIdAndNodeIdAndEnabledTrue(Long workflowId, String nodeId);

    /**
     * 根据工作流ID和节点类型查找节点
     */
    List<WorkflowNode> findByWorkflowIdAndNodeTypeAndEnabledTrue(Long workflowId, String nodeType);

    /**
     * 查找工作流的开始节点
     */
        List<WorkflowNode> findStartNodesByWorkflowId(@Param("workflowId") Long workflowId);

    /**
     * 查找工作流的结束节点
     */
        List<WorkflowNode> findEndNodesByWorkflowId(@Param("workflowId") Long workflowId);

    /**
     * 根据节点类型查找节点
     */
    List<WorkflowNode> findByNodeTypeAndEnabledTrue(String nodeType);

    /**
     * 根据节点分类查找节点
     */
    List<WorkflowNode> findByCategoryAndEnabledTrue(String category);

    /**
     * 统计工作流的节点数量
     */
    long countByWorkflowIdAndEnabledTrue(Long workflowId);

    /**
     * 根据工作流ID删除所有节点
     */
    void deleteByWorkflowId(Long workflowId);

    /**
     * 查找指定位置范围内的节点
     */
        List<WorkflowNode> findNodesInArea(@Param("workflowId") Long workflowId,
                                      @Param("minX") Integer minX, @Param("maxX") Integer maxX,
                                      @Param("minY") Integer minY, @Param("maxY") Integer maxY);

    /**
     * 查找工作流中的所有节点ID
     */
        List<String> findNodeIdsByWorkflowId(@Param("workflowId") Long workflowId);

    // 基础CRUD方法
    WorkflowNode save(WorkflowNode entity);
    
    Optional<WorkflowNode> findById(Long id);
    
    List<WorkflowNode> findAll();
    
    boolean existsById(Long id);
    
    long count();
    
    void deleteById(Long id);

}