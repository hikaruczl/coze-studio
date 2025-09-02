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

import com.coze.studio.entity.KnowledgeBase;
import com.coze.studio.entity.enums.KnowledgeStatus;

import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * KnowledgeBase 数据访问层
 * 
 * @author coze-dev
 */
@Mapper
public interface KnowledgeBaseRepository {

    /**
     * 根据创建者ID查找知识库列表
     */
    List<KnowledgeBase> findByCreatorIdAndEnabledTrue(Long creatorId);

    /**
     * 根据空间ID查找知识库列表
     */
    List<KnowledgeBase> findBySpaceIdAndEnabledTrue(Long spaceId);

    /**
     * 根据应用ID查找知识库列表
     */
    List<KnowledgeBase> findByAppIdAndEnabledTrue(Long appId);

    /**
     * 根据状态查找知识库列表
     */
    List<KnowledgeBase> findByStatusAndEnabledTrue(KnowledgeStatus status);

    /**
     * 根据创建者ID和名称查找知识库
     */
    Optional<KnowledgeBase> findByCreatorIdAndNameAndEnabledTrue(Long creatorId, String name);

    /**
     * 统计创建者的知识库数量
     */
    long countByCreatorIdAndEnabledTrue(Long creatorId);

    /**
     * 查找启用状态的知识库
     */
    List<KnowledgeBase> findEnabledKnowledgeBases();

    // 基础CRUD方法
    KnowledgeBase save(KnowledgeBase entity);

    Optional<KnowledgeBase> findById(Long id);

    List<KnowledgeBase> findAll();

    boolean existsById(Long id);

    long count();

    void deleteById(Long id);

    // TODO: 查询方法需要在MyBatis XML中实现
}
