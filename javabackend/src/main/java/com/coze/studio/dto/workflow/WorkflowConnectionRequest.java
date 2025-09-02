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

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Map;

/**
 * 工作流连接请求 DTO
 * 
 * @author coze-dev
 */
@Data
public class WorkflowConnectionRequest {

    /**
     * 连接唯一标识符
     */
    @NotBlank(message = "连接ID不能为空")
    @Size(max = 100, message = "连接ID长度不能超过100个字符")
    private String connectionId;

    /**
     * 源节点ID
     */
    @NotBlank(message = "源节点ID不能为空")
    private String sourceNodeId;

    /**
     * 源节点输出端口
     */
    private String sourcePort;

    /**
     * 目标节点ID
     */
    @NotBlank(message = "目标节点ID不能为空")
    private String targetNodeId;

    /**
     * 目标节点输入端口
     */
    private String targetPort;

    /**
     * 连接类型
     */
    private String connectionType = "DEFAULT";

    /**
     * 连接条件
     */
    private Map<String, Object> condition;

    /**
     * 连接标签
     */
    @Size(max = 200, message = "连接标签长度不能超过200个字符")
    private String label;

    /**
     * 连接描述
     */
    @Size(max = 1000, message = "连接描述长度不能超过1000个字符")
    private String description;

    /**
     * 连接样式
     */
    private Map<String, Object> style;

    /**
     * 连接路径点
     */
    private Map<String, Object> pathPoints;

    /**
     * 是否启用
     */
    private Boolean enabled = true;

    /**
     * 优先级
     */
    private Integer priority = 0;

    /**
     * 扩展属性
     */
    private Map<String, Object> properties;
}
