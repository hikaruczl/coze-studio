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
 * 工作流版本响应 DTO
 * 
 * @author coze-dev
 */
@Data
public class WorkflowVersionResponse {

    /**
     * 版本ID
     */
    private Long id;

    /**
     * 工作流ID
     */
    private Long workflowId;

    /**
     * 版本号
     */
    private String version;

    /**
     * 版本描述
     */
    private String description;

    /**
     * 版本状态
     */
    private String status;

    /**
     * 是否为当前版本
     */
    private Boolean isCurrent;

    /**
     * 工作流定义快照
     */
    private Map<String, Object> workflowSnapshot;

    /**
     * 变更日志
     */
    private String changelog;

    /**
     * 创建者ID
     */
    private Long creatorId;

    /**
     * 创建者用户名
     */
    private String creatorUsername;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 发布时间
     */
    private LocalDateTime publishedAt;

    /**
     * 标签
     */
    private String tag;

    /**
     * 分支名称
     */
    private String branch;

    /**
     * 父版本
     */
    private String parentVersion;

    /**
     * 版本大小（字节）
     */
    private Long size;

    /**
     * 校验和
     */
    private String checksum;
}
