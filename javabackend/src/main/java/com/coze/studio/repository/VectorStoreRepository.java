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

import com.coze.studio.entity.VectorStore;

import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 向量存储数据访问接口
 * 
 * @author coze-dev
 */
@Mapper
public interface VectorStoreRepository {

    /**
     * 根据名称查询向量存储
     */
    Optional<VectorStore> findByNameAndEnabledTrue(String name);

    /**
     * 根据类型查询向量存储
     */
    List<VectorStore> findByStoreTypeAndEnabledTrueOrderByCreatedAtDesc(String storeType);

    /**
     * 查询默认向量存储
     */
    Optional<VectorStore> findByIsDefaultTrueAndEnabledTrue();

    /**
     * 查询启用的向量存储
     */
    List<VectorStore> findByEnabledTrueOrderByCreatedAtDesc();

    /**
     * 分页查询向量存储
     */
    // Page<VectorStore> findByEnabledTrueOrderByCreatedAtDesc(Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 根据状态查询向量存储
     */
    List<VectorStore> findByStatusAndEnabledTrueOrderByCreatedAtDesc(String status);

    /**
     * 根据健康状态查询向量存储
     */
    List<VectorStore> findByHealthStatusAndEnabledTrueOrderByLastHealthCheckDesc(String healthStatus);

    /**
     * 根据创建者查询向量存储
     */
    List<VectorStore> findByCreatorIdAndEnabledTrueOrderByCreatedAtDesc(Long creatorId);

    /**
     * 检查名称是否存在
     */
    boolean existsByNameAndEnabledTrue(String name);

    /**
     * 查询需要健康检查的向量存储
     */
    List<VectorStore> findStoresNeedingHealthCheck(@Param("checkTime") LocalDateTime checkTime);

    /**
     * 查询高负载的向量存储
     */
    // List<VectorStore> findHighLoadStores(Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 查询性能最佳的向量存储
     */
        // List<VectorStore> findBestPerformingStores(Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 更新向量数量
     */
            void updateVectorCount(@Param("storeId") Long storeId, @Param("currentVectors") Long currentVectors);

    /**
     * 增加向量数量
     */
            void incrementVectorCount(@Param("storeId") Long storeId, @Param("increment") Long increment);

    /**
     * 减少向量数量
     */
            void decrementVectorCount(@Param("storeId") Long storeId, @Param("decrement") Long decrement);

    /**
     * 更新健康检查结果
     */
            void updateHealthCheck(@Param("storeId") Long storeId,
                          @Param("checkTime") LocalDateTime checkTime,
                          @Param("healthStatus") String healthStatus,
                          @Param("statusMessage") String statusMessage);

    /**
     * 更新查询统计
     */
            void updateQueryStats(@Param("storeId") Long storeId,
                         @Param("successIncrement") Long successIncrement,
                         @Param("newAvgLatency") Double newAvgLatency);

    /**
     * 设置默认向量存储
     */
            void setDefaultStore(@Param("storeId") Long storeId);

    /**
     * 更新存储状态
     */
            void updateStatus(@Param("storeId") Long storeId, @Param("status") String status, @Param("statusMessage") String statusMessage);

    /**
     * 查询向量存储统计信息
     */
    Object[] getVectorStoreStatistics();

    /**
     * 查询存储类型统计
     */
    List<Object[]> countByStoreType();

    /**
     * 查询存储状态统计
     */
    List<Object[]> countByStatus();

    /**
     * 查询健康状态统计
     */
    List<Object[]> countByHealthStatus();

    /**
     * 查询容量使用率
     */
        List<Object[]> getCapacityUsage();

    /**
     * 查询性能指标
     */
        List<Object[]> getPerformanceMetrics();

    /**
     * 查询需要维护的向量存储
     */
    List<VectorStore> findStoresNeedingMaintenance(@Param("latencyThreshold") Double latencyThreshold);

    /**
     * 软删除向量存储
     */
            void softDelete(@Param("storeId") Long storeId);

    /**
     * 恢复向量存储
     */
            void restore(@Param("storeId") Long storeId);
}
