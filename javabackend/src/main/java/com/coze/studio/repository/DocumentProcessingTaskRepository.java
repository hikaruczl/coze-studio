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

import com.coze.studio.entity.DocumentProcessingTask;

import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 文档处理任务数据访问接口
 * 
 * @author coze-dev
 */
@Mapper
public interface DocumentProcessingTaskRepository {

    /**
     * 根据文档ID查询任务
     */
    List<DocumentProcessingTask> findByDocumentIdOrderByCreatedAtDesc(Long documentId);

    /**
     * 根据状态查询任务
     */
    List<DocumentProcessingTask> findByStatusOrderByPriorityAscCreatedAtAsc(String status);

    /**
     * 根据任务类型查询任务
     */
    List<DocumentProcessingTask> findByTaskTypeAndStatusOrderByPriorityAscCreatedAtAsc(String taskType, String status);

    /**
     * 查询待处理的任务（按优先级排序）
     */
        // List<DocumentProcessingTask> findPendingTasks(Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 查询正在运行的任务
     */
    List<DocumentProcessingTask> findByStatusOrderByStartedAtAsc(String status);

    /**
     * 查询失败的任务
     */
    List<DocumentProcessingTask> findByStatusAndRetryCountLessThanOrderByCreatedAtAsc(String status, Integer maxRetries);

    /**
     * 查询需要重试的任务
     */
        List<DocumentProcessingTask> findTasksForRetry(@Param("now") LocalDateTime now);

    /**
     * 查询超时的任务
     */
        List<DocumentProcessingTask> findTimeoutTasks(@Param("timeoutTime") LocalDateTime timeoutTime);

    /**
     * 根据执行节点查询任务
     */
    List<DocumentProcessingTask> findByExecutorNodeAndStatusOrderByStartedAtAsc(String executorNode, String status);

    /**
     * 根据外部任务ID查询任务
     */
    Optional<DocumentProcessingTask> findByExternalTaskId(String externalTaskId);

    /**
     * 查询子任务
     */
    List<DocumentProcessingTask> findByParentTaskIdOrderByCreatedAtAsc(Long parentTaskId);

    /**
     * 统计任务数量
     */
    long countByStatus(String status);

    /**
     * 统计文档的任务数量
     */
    long countByDocumentIdAndStatus(Long documentId, String status);

    /**
     * 统计任务类型数量
     */
    List<Object[]> countByTaskType();

    /**
     * 统计任务状态数量
     */
    List<Object[]> countByStatus();

    /**
     * 查询指定时间范围内的任务
     */
        List<DocumentProcessingTask> findTasksBetween(@Param("startTime") LocalDateTime startTime,
                                                 @Param("endTime") LocalDateTime endTime);

    /**
     * 查询长时间运行的任务
     */
        List<DocumentProcessingTask> findLongRunningTasks(@Param("threshold") LocalDateTime threshold);

    /**
     * 更新任务状态
     */
            void updateTaskProgress(@Param("taskId") Long taskId,
                           @Param("status") String status,
                           @Param("progress") Integer progress,
                           @Param("progressMessage") String progressMessage);

    /**
     * 开始任务
     */
            void startTask(@Param("taskId") Long taskId,
                   @Param("startedAt") LocalDateTime startedAt,
                   @Param("executorNode") String executorNode);

    /**
     * 完成任务
     */
            void completeTask(@Param("taskId") Long taskId,
                     @Param("completedAt") LocalDateTime completedAt,
                     @Param("processingTime") Long processingTime,
                     @Param("taskResult") String taskResult);

    /**
     * 失败任务
     */
            void failTask(@Param("taskId") Long taskId,
                  @Param("errorMessage") String errorMessage,
                  @Param("errorStack") String errorStack,
                  @Param("nextRetryAt") LocalDateTime nextRetryAt);

    /**
     * 取消任务
     */
            void cancelTask(@Param("taskId") Long taskId);

    /**
     * 批量取消任务
     */
    void cancelTasksByDocument(@Param("documentId") Long documentId);

    /**
     * 重置任务状态
     */
            void resetTask(@Param("taskId") Long taskId);

    /**
     * 清理已完成的任务
     */
            void cleanupCompletedTasks(@Param("cutoffTime") LocalDateTime cutoffTime);

    /**
     * 查询任务统计信息
     */
    Object[] getTaskStatistics();

    /**
     * 查询文档的任务统计
     */
    Object[] getDocumentTaskStatistics(@Param("documentId") Long documentId);

    /**
     * 查询任务性能指标
     */
    List<Object[]> getTaskPerformanceMetrics();

    /**
     * 查询执行节点统计
     */
    List<Object[]> getExecutorNodeStatistics();
}
