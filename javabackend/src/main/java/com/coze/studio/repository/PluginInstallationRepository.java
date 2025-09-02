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

import com.coze.studio.entity.PluginInstallation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

// TODO: 分页方法需要在MyBatis XML中实现
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 插件安装记录数据访问接口
 * 
 * @author coze-dev
 */
@Mapper
public interface PluginInstallationRepository {

    /**
     * 查询用户是否已安装指定插件
     */
    boolean existsByUserIdAndPluginIdAndStatus(Long userId, Long pluginId, String status);

    /**
     * 根据用户ID和插件ID查询安装记录
     */
    Optional<PluginInstallation> findByUserIdAndPluginId(Long userId, Long pluginId);

    /**
     * 查询用户的已安装插件列表
     * TODO: 分页查询需要在MyBatis XML中实现
     */
    // Page<PluginInstallation> findByUserIdAndStatusOrderByInstalledAtDesc(Long userId, String status, Pageable pageable);

    /**
     * 查询用户的所有插件安装记录
     * TODO: 分页查询需要在MyBatis XML中实现
     */
    // Page<PluginInstallation> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    /**
     * 查询插件的安装记录
     * TODO: 分页查询需要在MyBatis XML中实现
     */
    // Page<PluginInstallation> findByPluginIdOrderByInstalledAtDesc(Long pluginId, Pageable pageable);

    /**
     * 统计插件的安装数量
     */
    long countByPluginIdAndStatus(Long pluginId, String status);

    /**
     * 统计用户的已安装插件数量
     */
    long countByUserIdAndStatus(Long userId, String status);

    /**
     * 查询最近安装的插件
     */
    List<PluginInstallation> findTop10ByUserIdAndStatusOrderByInstalledAtDesc(Long userId, String status);

    /**
     * 查询指定时间范围内的安装记录
     */
        List<PluginInstallation> findInstallationsBetween(@Param("startTime") LocalDateTime startTime, 
                                                      @Param("endTime") LocalDateTime endTime);

    /**
     * 查询热门插件（按安装数量排序）
     */
        // TODO: 分页方法需要在MyBatis XML中实现

    /**
     * 查询活跃用户（按安装插件数量排序）
     */
        // TODO: 分页方法需要在MyBatis XML中实现

    /**
     * 更新使用次数
     */
            void incrementUsageCount(@Param("userId") Long userId, @Param("pluginId") Long pluginId, 
                           @Param("lastUsedAt") LocalDateTime lastUsedAt);

    /**
     * 查询需要清理的安装记录（已卸载且超过指定天数）
     */
        List<PluginInstallation> findInstallationsForCleanup(@Param("cutoffTime") LocalDateTime cutoffTime);

    /**
     * 查询失败的安装记录
     */
    List<PluginInstallation> findByStatusAndCreatedAtAfterOrderByCreatedAtDesc(String status, LocalDateTime after);

    /**
     * 批量更新插件状态
     */
            void updateStatusByPluginId(@Param("pluginId") Long pluginId, @Param("oldStatus") String oldStatus, @Param("newStatus") String newStatus);

    /**
     * 删除用户的所有安装记录
     */
    void deleteByUserId(Long userId);

    /**
     * 删除插件的所有安装记录
     */
    void deleteByPluginId(Long pluginId);

    /**
     * 查询插件的安装统计
     */
    Object[] getInstallationStatistics(@Param("pluginId") Long pluginId);

    /**
     * 查询用户的安装统计
     */
    Object[] getUserInstallationStatistics(@Param("userId") Long userId);
}
