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

import java.util.List;
import java.util.Optional;

import com.coze.studio.entity.PluginUsage;

import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 插件使用记录数据访问接口
 * 
 * @author coze-dev
 */
@Mapper
public interface PluginUsageRepository {

    /**
     * 查询插件的使用记录
     */
    // Page<PluginUsage> findByPluginIdOrderByCreatedAtDesc(Long pluginId, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 查询用户的使用记录
     */
    // Page<PluginUsage> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 查询工作流中的插件使用记录
     */
    // Page<PluginUsage> findByWorkflowIdOrderByCreatedAtDesc(Long workflowId, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 统计插件的使用次数
     */
    long countByPluginId(Long pluginId);

    /**
     * 统计用户的使用次数
     */
    long countByUserId(Long userId);

    /**
     * 统计指定状态的使用次数
     */
    long countByPluginIdAndStatus(Long pluginId, String status);

    /**
     * 查询指定时间范围内的使用记录
     */
        List<PluginUsage> findUsageBetween(@Param("pluginId") Long pluginId,
                                      @Param("startTime") LocalDateTime startTime, 
                                      @Param("endTime") LocalDateTime endTime);

    /**
     * 统计指定时间范围内的使用次数
     */
        long countUsageBetween(@Param("pluginId") Long pluginId,
                          @Param("startTime") LocalDateTime startTime, 
                          @Param("endTime") LocalDateTime endTime);

    /**
     * 按使用来源统计
     */
    List<Object[]> countByUsageSource(@Param("pluginId") Long pluginId);

    /**
     * 按API端点统计
     */
    // List<Object[]> countByApiEndpoint(@Param("pluginId") Long pluginId, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 查询热门插件（按使用次数排序）
     */
        // List<Object[]> findPopularPluginsByUsage(@Param("startTime") LocalDateTime startTime, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 查询用户最近使用的插件
     */
        List<PluginUsage> findRecentUsageByUser(@Param("userId") Long userId, 
                                           @Param("startTime") LocalDateTime startTime);

    /**
     * 查询插件的日使用统计
     */
    List<Object[]> getDailyUsageStats(@Param("pluginId") Long pluginId, @Param("startTime") LocalDateTime startTime);

    /**
     * 查询插件的小时使用统计
     */
    List<Object[]> getHourlyUsageStats(@Param("pluginId") Long pluginId, @Param("startTime") LocalDateTime startTime);

    /**
     * 查询活跃用户（按使用次数排序）
     */
        // List<Object[]> findActiveUsers(@Param("startTime") LocalDateTime startTime, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 查询失败的使用记录
     */
    // List<PluginUsage> findByPluginIdAndStatusOrderByCreatedAtDesc(Long pluginId, String status, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 查询执行时间较长的记录
     */
        // List<PluginUsage> findSlowExecutions(@Param("pluginId") Long pluginId, @Param("threshold") Long threshold, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 删除插件的所有使用记录
     */
    void deleteByPluginId(Long pluginId);

    /**
     * 删除用户的所有使用记录
     */
    void deleteByUserId(Long userId);

    /**
     * 查询插件的使用统计
     */
    Object[] getUsageStatistics(@Param("pluginId") Long pluginId);

    /**
     * 查询用户的使用统计
     */
    Object[] getUserUsageStatistics(@Param("userId") Long userId);

    /**
     * 查询错误频率较高的插件
     */
    List<Object[]> findHighErrorRatePlugins(@Param("startTime") LocalDateTime startTime, 
                                           @Param("minUsage") long minUsage, 
                                           @Param("minFailureRate") double minFailureRate);
}
