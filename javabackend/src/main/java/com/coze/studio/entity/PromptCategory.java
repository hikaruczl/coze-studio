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
 * 提示词分类实体类
 * 用于管理提示词模板的分类和组织
 * 
 * @author coze-dev
 */
@Data
@EqualsAndHashCode(callSuper = true)
// 表名: prompt_categories
// 索引信息已移至数据库DDL脚本
public class PromptCategory extends BaseEntity {

    /**
     * 分类代码（唯一标识）
     */
        private String code;

    /**
     * 分类名称
     */
        private String name;

    /**
     * 分类显示名称
     */
        private String displayName;

    /**
     * 分类描述
     */
        private String description;

    /**
     * 父分类ID
     */
        private Long parentId;

    /**
     * 分类层级
     */
        private Integer level = 1;

    /**
     * 分类路径（用于快速查询）
     */
        private String path;

    /**
     * 排序顺序
     */
        private Integer sortOrder = 0;

    /**
     * 分类图标
     */
        private String icon;

    /**
     * 分类颜色
     */
        private String color;

    /**
     * 是否启用
     */
        private Boolean enabled = true;

    /**
     * 是否为系统分类
     */
        private Boolean isSystem = false;

    /**
     * 模板数量
     */
        private Long templateCount = 0L;

    /**
     * 分类元数据（JSON格式）
     */
        private String metadata;

    /**
     * 检查是否为根分类
     */
    public boolean isRoot() {
        return parentId == null;
    }

    /**
     * 检查是否为叶子分类
     */
    public boolean isLeaf() {
        return templateCount > 0;
    }

    /**
     * 增加模板数量
     */
    public void incrementTemplateCount() {
        this.templateCount++;
    }

    /**
     * 减少模板数量
     */
    public void decrementTemplateCount() {
        if (this.templateCount > 0) {
            this.templateCount--;
        }
    }

    /**
     * 构建分类路径
     */
    public void buildPath(String parentPath) {
        if (parentPath == null || parentPath.isEmpty()) {
            this.path = this.code;
        } else {
            this.path = parentPath + "/" + this.code;
        }
    }

    /**
     * 设置层级
     */
    public void setLevelFromPath() {
        if (this.path != null) {
            this.level = this.path.split("/").length;
        }
    }


    // Lombok生成的getter/setter方法
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean isIsSystem() {
        return isSystem;
    }

    public void setIsSystem(Boolean isSystem) {
        this.isSystem = isSystem;
    }

    public Long getTemplateCount() {
        return templateCount;
    }

    public void setTemplateCount(Long templateCount) {
        this.templateCount = templateCount;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }
}
