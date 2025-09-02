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
 * 工作流节点实体类
 * 表示工作流中的单个节点
 * 
 * @author coze-dev
 */
@Data
@EqualsAndHashCode(callSuper = true)
// 表名: workflow_nodes
// 索引信息已移至数据库DDL脚本
public class WorkflowNode extends BaseEntity {

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
     * 节点配置（JSON格式）
     */
    private String config;

    /**
     * 输入参数定义（JSON格式）
     */
    private String inputSchema;

    /**
     * 输出参数定义（JSON格式）
     */
    private String outputSchema;

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
    private String status = "ACTIVE";

    /**
     * 是否为起始节点
     */
    private Boolean isStartNode = false;

    /**
     * 是否为结束节点
     */
    private Boolean isEndNode = false;

    /**
     * 节点版本
     */
    private String version = "1.0.0";

    /**
     * 节点图标
     */
    private String iconUrl;

    /**
     * 节点颜色
     */
    private String color;

    /**
     * 扩展属性（JSON格式）
     */
    private String properties;

    /**
     * 是否启用
     */
    private Boolean enabled = true;

    /**
     * 排序序号
     */
    private Integer sortOrder = 0;

    /**
     * 获取位置信息（JSON格式）
     */
    public String getPosition() {
        if (positionX != null && positionY != null) {
            return String.format("{\"x\":%d,\"y\":%d}", positionX, positionY);
        }
        return null;
    }

    /**
     * 设置位置信息（JSON格式）
     */
    public void setPosition(String position) {
        if (position != null && !position.isEmpty()) {
            try {
                // 简单解析JSON格式的位置信息
                String[] parts = position.replace("{", "").replace("}", "").split(",");
                for (String part : parts) {
                    String[] keyValue = part.split(":");
                    if (keyValue.length == 2) {
                        String key = keyValue[0].trim().replace("\"", "");
                        int value = Integer.parseInt(keyValue[1].trim());
                        if ("x".equals(key)) {
                            this.positionX = value;
                        } else if ("y".equals(key)) {
                            this.positionY = value;
                        }
                    }
                }
            } catch (Exception e) {
                // 忽略解析错误
            }
        }
    }


    // Lombok生成的getter/setter方法
    public Long getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(Long workflowId) {
        this.workflowId = workflowId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getInputSchema() {
        return inputSchema;
    }

    public void setInputSchema(String inputSchema) {
        this.inputSchema = inputSchema;
    }

    public String getOutputSchema() {
        return outputSchema;
    }

    public void setOutputSchema(String outputSchema) {
        this.outputSchema = outputSchema;
    }

    public Integer getPositionX() {
        return positionX;
    }

    public void setPositionX(Integer positionX) {
        this.positionX = positionX;
    }

    public Integer getPositionY() {
        return positionY;
    }

    public void setPositionY(Integer positionY) {
        this.positionY = positionY;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean isIsStartNode() {
        return isStartNode;
    }

    public void setIsStartNode(Boolean isStartNode) {
        this.isStartNode = isStartNode;
    }

    public Boolean isIsEndNode() {
        return isEndNode;
    }

    public void setIsEndNode(Boolean isEndNode) {
        this.isEndNode = isEndNode;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
}
