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

import com.coze.studio.entity.WorkflowTemplate;

import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 工作流模板数据访问接口
 * 
 * @author coze-dev
 */
@Mapper
public interface WorkflowTemplateRepository {

    // TODO: 分页方法需要在MyBatis XML中实现

    // TODO: 分页方法需要在MyBatis XML中实现


    /**
     * 根据源工作流ID查询模板
     */
    List<WorkflowTemplate> findBySourceWorkflowIdAndIsDeletedFalse(Long sourceWorkflowId);

    /**
     * 检查模板名称是否存在（同一创建者下）
     */
    boolean existsByCreatorIdAndNameAndIsDeletedFalse(Long creatorId, String name);

    /**
     * 检查模板名称是否存在（同一创建者下）- 简化版本
     */
    boolean existsByNameAndCreatorId(String name, Long creatorId);

    // TODO: 查询方法需要在MyBatis XML中实现

    // 基础CRUD方法
    WorkflowTemplate save(WorkflowTemplate entity);
    
    Optional<WorkflowTemplate> findById(Long id);
    
    List<WorkflowTemplate> findAll();
    
    boolean existsById(Long id);
    
    long count();
    
    void deleteById(Long id);

}