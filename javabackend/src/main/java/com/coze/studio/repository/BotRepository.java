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

import com.coze.studio.entity.Bot;
import com.coze.studio.entity.enums.PublishStatus;

import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * Bot 数据访问层
 * 
 * @author coze-dev
 */
@Mapper
public interface BotRepository {

    /**
     * 根据所有者ID查找Bot列表
     */
    List<Bot> findByOwnerIdAndEnabledTrue(Long ownerId);

    /**
     * 根据所有者ID分页查找Bot列表
     */
    // Page<Bot> findByOwnerIdAndEnabledTrue(Long ownerId, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 根据空间ID查找Bot列表
     */
    List<Bot> findBySpaceIdAndEnabledTrue(Long spaceId);

    /**
     * 根据发布状态查找Bot列表
     */
    List<Bot> findByPublishStatusAndEnabledTrue(PublishStatus publishStatus);

    /**
     * 根据名称模糊查询Bot
     */
    // Page<Bot> findByNameContainingIgnoreCaseAndEnabledTrue(String name, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 根据所有者ID和名称查找Bot
     */
    Optional<Bot> findByOwnerIdAndNameAndEnabledTrue(Long ownerId, String name);

    /**
     * 统计所有者的Bot数量
     */
    long countByOwnerIdAndEnabledTrue(Long ownerId);

    /**
     * 查找已发布的Bot
     */
        List<Bot> findPublishedBots();

    /**
     * 根据连接器ID查找Bot
     */
        List<Bot> findByConnectorId(@Param("connectorId") Long connectorId);

    // 基础CRUD方法
    Bot save(Bot entity);
    
    Optional<Bot> findById(Long id);
    
    List<Bot> findAll();
    
    boolean existsById(Long id);
    
    long count();
    
    void deleteById(Long id);

}