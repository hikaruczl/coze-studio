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
 * 工作流模板实体
 * 
 * @author coze-dev
 */
@Data
@EqualsAndHashCode(callSuper = true)
// 表名: workflow_templates
// 索引信息已移至数据库DDL脚本
public class WorkflowTemplate extends BaseEntity {

    /**
     * 模板名称
     */
        private String name;

    /**
     * 模板描述
     */
        private String description;

    /**
     * 模板分类
     */
        private String category;

    /**
     * 标签（JSON格式）
     */
        private String tags;

    /**
     * 图标URL
     */
        private String iconUrl;

    /**
     * 是否公开
     */
        private Boolean isPublic = false;

    /**
     * 模板状态：DRAFT-草稿, PUBLISHED-已发布, ARCHIVED-已归档
     */
        private String status = "DRAFT";

    /**
     * 创建者ID
     */
        private Long creatorId;

    /**
     * 源工作流ID
     */
        private Long sourceWorkflowId;

    /**
     * 关联的工作流ID
     */
        private Long workflowId;

    /**
     * 工作流定义（JSON格式）
     */
        private String workflowDefinition;

    /**
     * 使用次数
     */
        private Integer usageCount = 0;

    /**
     * 收藏次数
     */
        private Integer favoriteCount = 0;

    /**
     * 平均评分
     */
        private Double averageRating = 0.0;

    /**
     * 评分次数
     */
        private Integer ratingCount = 0;

    /**
     * 版本号
     */
        private String version = "1.0.0";

    /**
     * 最小兼容版本
     */
        private String minVersion;

    /**
     * 模板配置（JSON格式）
     */
        private String templateConfig;

    /**
     * 预览图URL
     */
        private String previewUrl;

    /**
     * 文档URL
     */
        private String documentationUrl;

    /**
     * 许可证
     */
        private String license;

    /**
     * 作者信息
     */
        private String authorInfo;

    /**
     * 依赖信息（JSON格式）
     */
        private String dependencies;

    /**
     * 变更日志
     */
        private String changelog;

    /**
     * 是否已删除
     */
        private Boolean isDeleted = false;

    /**
     * 是否启用
     */
        private Boolean enabled = true;


    // Lombok生成的getter/setter方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Boolean isIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getSourceWorkflowId() {
        return sourceWorkflowId;
    }

    public void setSourceWorkflowId(Long sourceWorkflowId) {
        this.sourceWorkflowId = sourceWorkflowId;
    }

    public Long getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(Long workflowId) {
        this.workflowId = workflowId;
    }

    public String getWorkflowDefinition() {
        return workflowDefinition;
    }

    public void setWorkflowDefinition(String workflowDefinition) {
        this.workflowDefinition = workflowDefinition;
    }

    public Integer getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(Integer usageCount) {
        this.usageCount = usageCount;
    }

    public Integer getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(Integer favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Integer ratingCount) {
        this.ratingCount = ratingCount;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMinVersion() {
        return minVersion;
    }

    public void setMinVersion(String minVersion) {
        this.minVersion = minVersion;
    }

    public String getTemplateConfig() {
        return templateConfig;
    }

    public void setTemplateConfig(String templateConfig) {
        this.templateConfig = templateConfig;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public String getDocumentationUrl() {
        return documentationUrl;
    }

    public void setDocumentationUrl(String documentationUrl) {
        this.documentationUrl = documentationUrl;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getAuthorInfo() {
        return authorInfo;
    }

    public void setAuthorInfo(String authorInfo) {
        this.authorInfo = authorInfo;
    }

    public String getDependencies() {
        return dependencies;
    }

    public void setDependencies(String dependencies) {
        this.dependencies = dependencies;
    }

    public String getChangelog() {
        return changelog;
    }

    public void setChangelog(String changelog) {
        this.changelog = changelog;
    }

    public Boolean isIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
