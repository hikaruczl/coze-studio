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

/**
 * 工作流查询请求 DTO
 * 
 * @author coze-dev
 */
@Data
public class WorkflowQueryRequest {

    /**
     * 关键词（工作流名称、描述）
     */
    private String keyword;

    /**
     * 创建者ID
     */
    private Long creatorId;

    /**
     * 工作流状态
     */
    private String status;

    /**
     * 工作流类型
     */
    private String type;

    /**
     * 是否为模板
     */
    private Boolean isTemplate;

    /**
     * 模板分类
     */
    private String templateCategory;

    /**
     * 标签
     */
    private String tag;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 最小执行次数
     */
    private Long minExecutionCount;

    /**
     * 最大执行次数
     */
    private Long maxExecutionCount;
}
