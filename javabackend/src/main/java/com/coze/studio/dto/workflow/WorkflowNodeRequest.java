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
 * 工作流节点请求 DTO
 * 
 * @author coze-dev
 */
@Data
public class WorkflowNodeRequest {

    /**
     * 节点唯一标识符
     */
    @NotBlank(message = "节点ID不能为空")
    @Size(max = 100, message = "节点ID长度不能超过100个字符")
    private String nodeId;

    /**
     * 节点名称
     */
    @NotBlank(message = "节点名称不能为空")
    @Size(max = 200, message = "节点名称长度不能超过200个字符")
    private String name;

    /**
     * 节点类型
     */
    @NotBlank(message = "节点类型不能为空")
    private String nodeType;

    /**
     * 节点分类
     */
    private String category;

    /**
     * 节点描述
     */
    @Size(max = 1000, message = "节点描述长度不能超过1000个字符")
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
     * 是否为起始节点
     */
    private Boolean isStartNode = false;

    /**
     * 是否为结束节点
     */
    private Boolean isEndNode = false;

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
    private Boolean enabled = true;

    /**
     * 排序序号
     */
    private Integer sortOrder = 0;
}
