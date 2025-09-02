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

package com.coze.studio.entity;

// JPA注解已移除，改用MyBatis
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 工作流连接实体类
 * 表示工作流节点之间的连接关系
 * 
 * @author coze-dev
 */
@Data
@EqualsAndHashCode(callSuper = true)
// 表名: workflow_connections
// 索引信息已移至数据库DDL脚本
public class WorkflowConnection extends BaseEntity {

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
    private String connectionType = "DEFAULT";

    /**
     * 连接条件（JSON格式）
     */
    private String condition;

    /**
     * 连接标签
     */
    private String label;

    /**
     * 连接描述
     */
    private String description;

    /**
     * 连接样式（JSON格式）
     */
    private String style;

    /**
     * 连接路径点（JSON格式）
     */
    private String pathPoints;

    /**
     * 是否启用
     */
    private Boolean enabled = true;

    /**
     * 优先级（用于多条件分支）
     */
    private Integer priority = 0;

    /**
     * 扩展属性（JSON格式）
     */
    private String properties;

    /**
     * 源节点连接点
     */
    private String sourceHandle;

    /**
     * 目标节点连接点
     */
    private String targetHandle;

    /**
     * 获取源节点连接点
     */
    public String getSourceHandle() {
        return sourceHandle;
    }

    /**
     * 设置源节点连接点
     */
    public void setSourceHandle(String sourceHandle) {
        this.sourceHandle = sourceHandle;
    }

    /**
     * 获取目标节点连接点
     */
    public String getTargetHandle() {
        return targetHandle;
    }

    /**
     * 设置目标节点连接点
     */
    public void setTargetHandle(String targetHandle) {
        this.targetHandle = targetHandle;
    }


    // Lombok生成的getter/setter方法
    public Long getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(Long workflowId) {
        this.workflowId = workflowId;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    public String getSourceNodeId() {
        return sourceNodeId;
    }

    public void setSourceNodeId(String sourceNodeId) {
        this.sourceNodeId = sourceNodeId;
    }

    public String getSourcePort() {
        return sourcePort;
    }

    public void setSourcePort(String sourcePort) {
        this.sourcePort = sourcePort;
    }

    public String getTargetNodeId() {
        return targetNodeId;
    }

    public void setTargetNodeId(String targetNodeId) {
        this.targetNodeId = targetNodeId;
    }

    public String getTargetPort() {
        return targetPort;
    }

    public void setTargetPort(String targetPort) {
        this.targetPort = targetPort;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getPathPoints() {
        return pathPoints;
    }

    public void setPathPoints(String pathPoints) {
        this.pathPoints = pathPoints;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }


}
