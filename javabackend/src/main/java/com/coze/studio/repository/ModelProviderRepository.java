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

import com.coze.studio.entity.ModelProvider;

import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 模型提供商数据访问接口
 * 
 * @author coze-dev
 */
@Mapper
public interface ModelProviderRepository {

    /**
     * 根据代码查找提供商
     */
    Optional<ModelProvider> findByCode(String code);

    /**
     * 根据名称查找提供商
     */
    Optional<ModelProvider> findByName(String name);

    /**
     * 查找所有启用的提供商
     */
    List<ModelProvider> findByEnabledTrueOrderByPriorityAscCreatedAtAsc();

    /**
     * 根据类型查找提供商
     */
    List<ModelProvider> findByTypeAndEnabledTrueOrderByPriorityAscCreatedAtAsc(String type);

    /**
     * 根据状态查找提供商
     */
    List<ModelProvider> findByStatusAndEnabledTrueOrderByPriorityAscCreatedAtAsc(String status);

    /**
     * 查找活跃的提供商
     */
        List<ModelProvider> findActiveProviders();

    /**
     * 查找默认提供商
     */
    Optional<ModelProvider> findByIsDefaultTrueAndEnabledTrue();

    /**
     * 查找健康的提供商
     */
        List<ModelProvider> findHealthyProviders();

    /**
     * 查找需要健康检查的提供商
     */

    /**
     * 分页查询提供商
     */
    // TODO: 分页方法需要在MyBatis XML中实现

    /**
     * 根据类型分页查询
     */
    // TODO: 分页方法需要在MyBatis XML中实现

    /**
     * 根据状态分页查询
     */
    // TODO: 分页方法需要在MyBatis XML中实现

    /**
     * 搜索提供商
     */
        // TODO: 分页方法需要在MyBatis XML中实现

    /**
     * 高级搜索
     */
    // TODO: 分页方法需要在MyBatis XML中实现

    /**
     * 统计提供商数量
     */
    long countByEnabledTrue();

    /**
     * 按类型统计
     */
    long countByTypeAndEnabledTrue(String type);

    /**
     * 按状态统计
     */
    long countByStatusAndEnabledTrue(String status);

    /**
     * 统计健康的提供商数量
     */
    long countHealthyProviders();

    /**
     * 更新健康状态
     */
            /**
     * 批量更新健康状态
     */
            /**
     * 更新优先级
     */
            /**
     * 批量更新状态
     */
            /**
     * 批量启用/禁用
     */
            /**
     * 设置默认提供商
     */
            /**
     * 检查代码是否存在
     */
    boolean existsByCode(String code);

    /**
     * 检查名称是否存在
     */
    boolean existsByName(String name);

    /**
     * 检查是否存在默认提供商
     */
    boolean existsByIsDefaultTrueAndEnabledTrue();

    /**
     * 获取提供商统计信息
     */
    List<Object[]> getProviderStatistics();

    /**
     * 获取健康状态统计
     */
    List<Object[]> getHealthStatusStatistics();

    /**
     * 获取状态统计
     */
    List<Object[]> getStatusStatistics();

    /**
     * 查找最近更新的提供商
     */
        // TODO: 分页方法需要在MyBatis XML中实现

    /**
     * 查找长时间未检查的提供商
     */
        /**
     * 查找指定时间范围内创建的提供商
     */
        /**
     * 查找支持指定功能的提供商
     */
        /**
     * 获取最高优先级的提供商
     */
        Optional<ModelProvider> findHighestPriorityProvider();

    /**
     * 获取负载均衡的提供商列表
     */
    List<ModelProvider> findProvidersForLoadBalancing();
}
