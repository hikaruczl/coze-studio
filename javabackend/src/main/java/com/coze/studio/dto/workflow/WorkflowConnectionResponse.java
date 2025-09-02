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

import com.coze.studio.entity.WorkflowConnection;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 工作流连接响应 DTO
 *
 * @author coze-dev
 */
@Data
@lombok.NoArgsConstructor

public class WorkflowConnectionResponse {

    /**
     * 连接数据库ID
     */
    private Long id;

    /**
     * 工作流ID
     */
    private Long workflowId;

    /**
     * 连接唯一标识符
     */
    private String connectionId;

    /**
     * 源节点ID
     */
    private String sourceNodeId;

    /**
     * 源节点输出端口
     */
    private String sourcePort;

    /**
     * 目标节点ID
     */
    private String targetNodeId;

    /**
     * 目标节点输入端口
     */
    private String targetPort;

    /**
     * 连接类型
     */
    private String connectionType;

    /**
     * 连接条件
     */
    private Map<String, Object> condition;

    /**
     * 连接标签
     */
    private String label;

    /**
     * 连接描述
     */
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
    private Boolean enabled;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 扩展属性
     */
    private Map<String, Object> properties;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 从 WorkflowConnection 实体转换为 WorkflowConnectionResponse
     */
    public static WorkflowConnectionResponse fromWorkflowConnection(WorkflowConnection connection) {
        WorkflowConnectionResponse response = new WorkflowConnectionResponse();
        response.setId(connection.getId());
        response.setWorkflowId(connection.getWorkflowId());
        response.setConnectionId(connection.getConnectionId());
        response.setSourceNodeId(connection.getSourceNodeId());
        response.setSourcePort(connection.getSourcePort());
        response.setTargetNodeId(connection.getTargetNodeId());
        response.setTargetPort(connection.getTargetPort());
        response.setConnectionType(connection.getConnectionType());
        response.setLabel(connection.getLabel());
        response.setDescription(connection.getDescription());
        response.setEnabled(connection.isEnabled());
        response.setPriority(connection.getPriority());
        response.setCreatedAt(connection.getCreatedAt());
        response.setUpdatedAt(connection.getUpdatedAt());

        // TODO: 解析JSON字段
        // response.setCondition(parseJsonToMap(connection.getCondition()));
        // response.setStyle(parseJsonToMap(connection.getStyle()));
        // response.setPathPoints(parseJsonToMap(connection.getPathPoints()));
        // response.setProperties(parseJsonToMap(connection.getProperties()));

        return response;
    }
}
