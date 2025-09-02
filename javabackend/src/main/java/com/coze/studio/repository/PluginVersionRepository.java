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

import com.coze.studio.entity.PluginVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 插件版本数据访问层
 *
 * @author coze-dev
 */
@Repository
public interface PluginVersionRepository extends JpaRepository<PluginVersion, Long> {

    /**
     * 根据插件ID和版本号查找版本
     */
    Optional<PluginVersion> findByPluginIdAndVersion(Long pluginId, String version);

    /**
     * 根据插件ID查找所有版本
     */
    List<PluginVersion> findByPluginIdOrderByCreatedAtDesc(Long pluginId);

    /**
     * 根据插件ID查找最新版本
     */
    Optional<PluginVersion> findByPluginIdAndIsLatestTrue(Long pluginId);

    /**
     * 根据插件ID查找已发布版本
     */
    List<PluginVersion> findByPluginIdAndStatusOrderByPublishedAtDesc(Long pluginId, String status);

    /**
     * 根据插件ID和状态查找版本
     */
    @Query("SELECT v FROM PluginVersion v WHERE v.pluginId = :pluginId AND v.status = :status ORDER BY v.createdAt DESC")
    List<PluginVersion> findByPluginIdAndStatus(@Param("pluginId") Long pluginId, @Param("status") String status);

    /**
     * 根据插件ID查找稳定版本
     */
    List<PluginVersion> findByPluginIdAndIsStableTrueOrderByCreatedAtDesc(Long pluginId);

    /**
     * 根据插件ID统计版本数量
     */
    long countByPluginId(Long pluginId);

    /**
     * 根据插件ID和状态统计版本数量
     */
    long countByPluginIdAndStatus(Long pluginId, String status);

    /**
     * 根据插件ID删除所有版本
     */
    void deleteByPluginId(Long pluginId);
}
