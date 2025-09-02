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

import java.util.List;
import java.util.Map;

/**
 * 创建工作流请求 DTO
 *
 * @author coze-dev
 */
@Data
public class CreateWorkflowRequest {

    /**
     * 工作流名称
     */
    @NotBlank(message = "工作流名称不能为空")
    @Size(min = 1, max = 200, message = "工作流名称长度必须在1-200个字符之间")
    private String name;

    /**
     * 工作流描述
     */
    @Size(max = 1000, message = "工作流描述长度不能超过1000个字符")
    private String description;

    /**
     * 工作流类型
     */
    private String type = "STANDARD";

    /**
     * 工作流图标URL
     */
    private String iconUrl;

    /**
     * 工作流标签
     */
    private List<String> tags;

    /**
     * 是否为模板
     */
    private Boolean isTemplate = false;

    /**
     * 模板分类
     */
    private String templateCategory;

    /**
     * 输入参数定义
     */
    private Map<String, Object> inputSchema;

    /**
     * 输出参数定义
     */
    private Map<String, Object> outputSchema;

    /**
     * 工作流配置
     */
    private Map<String, Object> config;

    /**
     * 工作流节点列表
     */
    private List<WorkflowNodeRequest> nodes;

    /**
     * 工作流连接列表
     */
    private List<WorkflowConnectionRequest> connections;

    /**
     * 画布配置
     */
    private Map<String, Object> canvasConfig;
    /** 占位：兼容 setCategory 调用，无业务影响 */
    public void setCategory(String category) {
        this.templateCategory = category;
    }

}
