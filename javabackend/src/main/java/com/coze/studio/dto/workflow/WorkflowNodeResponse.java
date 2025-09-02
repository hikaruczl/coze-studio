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

import com.coze.studio.entity.WorkflowNode;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 工作流节点响应 DTO
 *
 * @author coze-dev
 */
@Data
@lombok.NoArgsConstructor

public class WorkflowNodeResponse {

    /**
     * 节点数据库ID
     */
    private Long id;

    /**
     * 工作流ID
     */
    private Long workflowId;

    /**
     * 节点唯一标识符
     */
    private String nodeId;

    /**
     * 节点名称
     */
    private String name;

    /**
     * 节点类型
     */
    private String nodeType;

    /**
     * 节点分类
     */
    private String category;

    /**
     * 节点描述
     */
    private String description;

    /**
     * 节点配置
     */
    private Map<String, Object> config;

    /**
     * 输入参数定义
     */
    private Map<String, Object> inputSchema;

    /**
     * 输出参数定义
     */
    private Map<String, Object> outputSchema;

    /**
     * 节点在画布上的X坐标
     */
    private Integer positionX;

    /**
     * 节点在画布上的Y坐标
     */
    private Integer positionY;

    /**
     * 节点宽度
     */
    private Integer width;

    /**
     * 节点高度
     */
    private Integer height;

    /**
     * 节点状态
     */
    private String status;

    /**
     * 是否为起始节点
     */
    private Boolean isStartNode;

    /**
     * 是否为结束节点
     */
    private Boolean isEndNode;

    /**
     * 节点版本
     */
    private String version;

    /**
     * 节点图标
     */
    private String iconUrl;

    /**
     * 节点颜色
     */
    private String color;

    /**
     * 扩展属性
     */
    private Map<String, Object> properties;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 排序序号
     */
    private Integer sortOrder;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 从 WorkflowNode 实体转换为 WorkflowNodeResponse
     */
    public static WorkflowNodeResponse fromWorkflowNode(WorkflowNode node) {
        WorkflowNodeResponse response = new WorkflowNodeResponse();
        response.setId(node.getId());
        response.setWorkflowId(node.getWorkflowId());
        response.setNodeId(node.getNodeId());
        response.setName(node.getName());
        response.setNodeType(node.getNodeType());
        response.setCategory(node.getCategory());
        response.setDescription(node.getDescription());
        response.setPositionX(node.getPositionX());
        response.setPositionY(node.getPositionY());
        response.setWidth(node.getWidth());
        response.setHeight(node.getHeight());
        response.setStatus(node.getStatus());
        response.setIsStartNode(node.isIsStartNode());
        response.setIsEndNode(node.isIsEndNode());
        response.setVersion(node.getVersion());
        response.setIconUrl(node.getIconUrl());
        response.setColor(node.getColor());
        response.setEnabled(node.isEnabled());
        response.setSortOrder(node.getSortOrder());
        response.setCreatedAt(node.getCreatedAt());
        response.setUpdatedAt(node.getUpdatedAt());

        // TODO: 解析JSON字段
        // response.setConfig(parseJsonToMap(node.getConfig()));
        // response.setInputSchema(parseJsonToMap(node.getInputSchema()));
        // response.setOutputSchema(parseJsonToMap(node.getOutputSchema()));
        // response.setProperties(parseJsonToMap(node.getProperties()));

        return response;
    }
}
