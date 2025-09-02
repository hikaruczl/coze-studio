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

import com.coze.studio.entity.ModelConfiguration;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 模型配置数据访问接口
 * 
 * @author coze-dev
 */
@Mapper
public interface ModelConfigurationRepository {

    /**
     * 根据代码查找模型
     */
    Optional<ModelConfiguration> findByCode(String code);

    /**
     * 根据名称查找模型
     */
    Optional<ModelConfiguration> findByName(String name);

    /**
     * 根据提供商ID查找模型
     */
    List<ModelConfiguration> findByProviderIdAndEnabledTrueOrderByPriorityAscCreatedAtAsc(Long providerId);

    /**
     * 根据类型查找模型
     */
    List<ModelConfiguration> findByTypeAndEnabledTrueOrderByPriorityAscCreatedAtAsc(String type);

    /**
     * 根据状态查找模型
     */
    List<ModelConfiguration> findByStatusAndEnabledTrueOrderByPriorityAscCreatedAtAsc(String status);

    /**
     * 查找所有启用的模型
     */
    List<ModelConfiguration> findByEnabledTrueOrderByPriorityAscCreatedAtAsc();

    /**
     * 查找活跃的模型
     */
    List<ModelConfiguration> findActiveModels();

    /**
     * 查找默认模型
     */
    Optional<ModelConfiguration> findByIsDefaultTrueAndEnabledTrue();

    /**
     * 根据类型查找默认模型
     */
    Optional<ModelConfiguration> findByTypeAndIsDefaultTrueAndEnabledTrue(String type);

    /**
     * 分页查询模型
     */

    /**
     * 根据提供商分页查询
     */

    /**
     * 根据类型分页查询
     */

    /**
     * 根据状态分页查询
     */

    /**
     * 搜索模型
     */

    /**
     * 高级搜索
     */

    /**
     * 根据价格范围查找模型
     */

    /**
     * 根据性能评分查找模型
     */

    /**
     * 根据质量评分查找模型
     */

    /**
     * 查找支持指定功能的模型
     */

    /**
     * 查找支持指定语言的模型
     */

    /**
     * 根据token限制查找模型
     */

    /**
     * 统计模型数量
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
     * 按提供商统计
     */
    long countByProviderIdAndEnabledTrue(Long providerId);

    // TODO: 更新和查询方法需要在MyBatis XML中实现

    /**
     * 检查代码是否存在
     */
    boolean existsByCode(String code);

    /**
     * 检查名称是否存在
     */
    boolean existsByName(String name);

    /**
     * 检查提供商模型ID是否存在
     */
    boolean existsByProviderModelId(String providerModelId);

    /**
     * 查找即将弃用的模型
     */
    List<ModelConfiguration> findDeprecatedModels();
}
