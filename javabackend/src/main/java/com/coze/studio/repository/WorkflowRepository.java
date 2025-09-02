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

import com.coze.studio.entity.Workflow;

import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Workflow 数据访问层
 *
 * @author coze-dev
 */
@Mapper
public interface WorkflowRepository {

    /**
     * 根据空间ID查找工作流列表
     */
    List<Workflow> findBySpaceIdAndEnabledTrue(Long spaceId);

    /**
     * 根据应用ID查找工作流列表
     */
    List<Workflow> findByAppIdAndEnabledTrue(Long appId);

    /**
     * 根据创建者ID和名称查找工作流
     */
    Optional<Workflow> findByCreatorIdAndNameAndEnabledTrue(Long creatorId, String name);

    /**
     * 查找已发布的工作流
     */
    List<Workflow> findByPublishedTrueAndEnabledTrue();

    /**
     * 统计创建者的工作流数量
     */
    long countByCreatorIdAndEnabledTrue(Long creatorId);


    /**
     * 根据版本查找工作流
     */
    List<Workflow> findByVersionAndEnabledTrue(String version);


    // 基础CRUD方法
    Workflow save(Workflow entity);

    Optional<Workflow> findById(Long id);

    List<Workflow> findAll();

    boolean existsById(Long id);

    long count();

    void deleteById(Long id);


    // 自定义查询方法
    List<Workflow> findByCreatorIdAndEnabledTrueOrderByUpdatedAtDesc(Long creatorId);
    List<Workflow> findByCreatorIdAndEnabledTrueOrderByCreatedAtDesc(Long creatorId);
    List<Workflow> findByStatusAndEnabledTrueOrderByCreatedAtDesc(String status);
    List<Workflow> findByEnabledTrueOrderByCreatedAtDesc();

    long countByCreatorId(Long creatorId);
    Optional<Workflow> findByIdAndCreatorIdAndEnabledTrue(Long id, Long creatorId);
    Double getAverageExecutionTime(Long workflowId);

}