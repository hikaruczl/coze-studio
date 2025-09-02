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
 * 模型配置实体类
 * 存储AI模型的配置信息和参数模板
 *
 * @author coze-dev
 */
@Data
@EqualsAndHashCode(callSuper = true)
// 表名: model_configs
public class ModelConfig extends BaseEntity {

    /**
     * 模型ID
     */
    private Long modelId;

    /**
     * 模型名称
     */
    private String name;

    /**
     * 模型图标URI
     */
    private String iconUri;

    /**
     * 模型图标URL
     */
    private String iconUrl;

    /**
     * 模型描述（JSON格式，支持多语言）
     */
    private String description;

    /**
     * 默认参数配置（JSON格式）
     */
    private String defaultParameters;

    /**
     * 元数据信息（JSON格式）
     */
    private String meta;

    /**
     * 连接配置（JSON格式）
     */
    private String connectionConfig;

    /**
     * 状态（0: 禁用, 1: 启用）
     */
    private Integer status = 1;

    /**
     * 是否为模板配置
     */
    private Boolean isTemplate = false;

    /**
     * 模板分类
     */
    private String templateCategory;

    /**
     * 创建者ID
     */
    private Long creatorId;

    /**
     * 更新者ID
     */
    private Long updaterId;

    // 构造函数
    public ModelConfig() {
    }

    public ModelConfig(String name, String templateCategory) {
        this.name = name;
        this.templateCategory = templateCategory;
        this.isTemplate = true;
        this.status = 1;
    }

    // 业务方法
    public boolean isEnabled() {
        return status != null && status == 1;
    }

    public void enable() {
        this.status = 1;
    }

    public void disable() {
        this.status = 0;
    }

    public boolean isTemplate() {
        return isTemplate != null && isTemplate;
    }

    public void markAsTemplate() {
        this.isTemplate = true;
    }

    public void unmarkAsTemplate() {
        this.isTemplate = false;
    }
}
