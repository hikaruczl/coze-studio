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

import com.coze.studio.entity.PluginRating;

import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 插件评价数据访问接口
 * 
 * @author coze-dev
 */
@Mapper
public interface PluginRatingRepository {

    /**
     * 查询用户是否已评价指定插件
     */
    boolean existsByUserIdAndPluginId(Long userId, Long pluginId);

    /**
     * 根据用户ID和插件ID查询评价记录
     */
    Optional<PluginRating> findByUserIdAndPluginId(Long userId, Long pluginId);

    /**
     * 查询插件的评价列表
     */
    // Page<PluginRating> findByPluginIdAndStatusOrderByCreatedAtDesc(Long pluginId, String status, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 查询用户的评价列表
     */
    // Page<PluginRating> findByUserIdAndStatusOrderByCreatedAtDesc(Long userId, String status, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 统计插件的评价数量
     */
    long countByPluginIdAndStatus(Long pluginId, String status);

    /**
     * 统计用户的评价数量
     */
    long countByUserIdAndStatus(Long userId, String status);

    /**
     * 计算插件的平均评分
     */
    Double calculateAverageRating(@Param("pluginId") Long pluginId);

    /**
     * 查询插件的评分分布
     */
    List<Object[]> getRatingDistribution(@Param("pluginId") Long pluginId);

    /**
     * 查询指定评分的评价
     */
    // Page<PluginRating> findByPluginIdAndRatingAndStatusOrderByCreatedAtDesc(Long pluginId, Integer rating, String status, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 查询有评论的评价
     */
        // Page<PluginRating> findRatingsWithComments(@Param("pluginId") Long pluginId, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 查询指定时间范围内的评价
     */
        List<PluginRating> findRatingsBetween(@Param("pluginId") Long pluginId,
                                         @Param("startTime") LocalDateTime startTime, 
                                         @Param("endTime") LocalDateTime endTime);

    /**
     * 查询最新的评价
     */
        // List<PluginRating> findLatestRatings(@Param("pluginId") Long pluginId, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 查询高评分的评价
     */
        // Page<PluginRating> findHighRatings(@Param("pluginId") Long pluginId, @Param("minRating") Integer minRating, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 查询低评分的评价
     */
        // Page<PluginRating> findLowRatings(@Param("pluginId") Long pluginId, @Param("maxRating") Integer maxRating, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 查询有帮助的评价（按有帮助投票数排序）
     */
        // Page<PluginRating> findHelpfulRatings(@Param("pluginId") Long pluginId, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 删除插件的所有评价
     */
    void deleteByPluginId(Long pluginId);

    /**
     * 删除用户的所有评价
     */
    void deleteByUserId(Long userId);

    /**
     * 查询插件评价的统计信息
     */
    Object[] getRatingStatistics(@Param("pluginId") Long pluginId);

    /**
     * 查询用户的评价统计
     */
    Object[] getUserRatingStatistics(@Param("userId") Long userId);

    /**
     * 查询需要管理员回复的评价
     */
        // List<PluginRating> findRatingsNeedingAdminReply(Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 查询最近的负面评价
     */
        List<PluginRating> findRecentNegativeRatings(@Param("pluginId") Long pluginId, @Param("since") LocalDateTime since);
}
