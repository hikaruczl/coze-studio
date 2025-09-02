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

package com.coze.studio.dto.workflow;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 工作流统计信息响应 DTO
 * 
 * @author coze-dev
 */
@Data
public class WorkflowStatsResponse {

    /**
     * 工作流ID
     */
    private Long workflowId;

    /**
     * 工作流名称
     */
    private String workflowName;

    /**
     * 总执行次数
     */
    private Long totalExecutions;

    /**
     * 成功执行次数
     */
    private Long successExecutions;

    /**
     * 失败执行次数
     */
    private Long failedExecutions;

    /**
     * 取消执行次数
     */
    private Long cancelledExecutions;

    /**
     * 成功率
     */
    private Double successRate;

    /**
     * 平均执行时间（毫秒）
     */
    private Double averageExecutionTime;

    /**
     * 最短执行时间（毫秒）
     */
    private Long minExecutionTime;

    /**
     * 最长执行时间（毫秒）
     */
    private Long maxExecutionTime;

    /**
     * 今日执行次数
     */
    private Long todayExecutions;

    /**
     * 本周执行次数
     */
    private Long weekExecutions;

    /**
     * 本月执行次数
     */
    private Long monthExecutions;

    /**
     * 最后执行时间
     */
    private LocalDateTime lastExecutedAt;

    /**
     * 节点数量
     */
    private Integer nodeCount;

    /**
     * 连接数量
     */
    private Integer connectionCount;

    /**
     * 执行状态分布
     */
    private Map<String, Long> statusDistribution;

    /**
     * 节点类型分布
     */
    private Map<String, Long> nodeTypeDistribution;

    /**
     * 每日执行趋势
     */
    private Map<String, Long> dailyExecutionTrend;

    /**
     * 错误类型分布
     */
    private Map<String, Long> errorTypeDistribution;

    /**
     * 统计时间
     */
    private LocalDateTime statsTime;
}
