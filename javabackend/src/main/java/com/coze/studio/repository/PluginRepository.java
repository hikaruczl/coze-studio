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

import com.coze.studio.entity.Plugin;
import com.coze.studio.entity.enums.PluginType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * Plugin 数据访问层
 * 
 * @author coze-dev
 */
@Mapper
public interface PluginRepository {

    /**
     * 根据开发者ID查找插件列表
     */
    List<Plugin> findByDeveloperIdAndEnabledTrue(Long developerId);

    /**
     * 根据开发者ID分页查找插件列表
     * TODO: 分页查询需要在MyBatis XML中实现
     */
    // Page<Plugin> findByDeveloperIdAndEnabledTrue(Long developerId, Pageable pageable);

    /**
     * 根据空间ID查找插件列表
     */
    List<Plugin> findBySpaceIdAndEnabledTrue(Long spaceId);

    /**
     * 根据插件类型查找插件列表
     */
    List<Plugin> findByTypeAndEnabledTrue(PluginType type);

    /**
     * 根据名称模糊查询插件
     * TODO: 分页查询需要在MyBatis XML中实现
     */
    // Page<Plugin> findByNameContainingIgnoreCaseAndEnabledTrue(String name, Pageable pageable);

    /**
     * 根据开发者ID和名称查找插件
     */
    Optional<Plugin> findByDeveloperIdAndNameAndEnabledTrue(Long developerId, String name);

    /**
     * 查找已发布的插件
     */
    List<Plugin> findByPublishedTrueAndEnabledTrue();

    /**
     * 根据服务器URL查找插件
     */
    Optional<Plugin> findByServerUrlAndEnabledTrue(String serverUrl);

    /**
     * 统计开发者的插件数量
     */
    long countByDeveloperIdAndEnabledTrue(Long developerId);

    /**
     * 查找内置插件
     */
        List<Plugin> findBuiltinPlugins();

    /**
     * 根据插件类型和发布状态查找插件
     */
    List<Plugin> findByTypeAndPublished(@Param("type") PluginType type, @Param("published") Boolean published);

    // 基础CRUD方法
    Plugin save(Plugin entity);

    Optional<Plugin> findById(Long id);

    List<Plugin> findAll();

    boolean existsById(Long id);

    long count();

    void deleteById(Long id);
}
