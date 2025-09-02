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

import java.time.LocalDateTime;

/**
 * 提示词模板实体类
 * 用于管理AI提示词模板的创建、版本控制和使用
 * 
 * @author coze-dev
 */
@Data
@EqualsAndHashCode(callSuper = true)
// 表名: prompt_templates
// 索引信息已移至数据库DDL脚本
public class PromptTemplate extends BaseEntity {

    /**
     * 模板名称
     */
        private String name;

    /**
     * 模板显示名称
     */
        private String displayName;

    /**
     * 模板描述
     */
        private String description;

    /**
     * 模板内容
     */
        private String content;

    /**
     * 模板类型
     * SYSTEM - 系统提示词
     * USER - 用户提示词
     * ASSISTANT - 助手提示词
     * FUNCTION - 函数调用提示词
     * WORKFLOW - 工作流提示词
     * CONVERSATION - 对话提示词
     */
        private String type;

    /**
     * 模板分类
     */
        private String category;

    /**
     * 模板标签（JSON数组格式）
     */
        private String tags;

    /**
     * 模板语言
     */
        private String language = "zh-CN";

    /**
     * 模板版本号
     */
        private String version = "1.0.0";

    /**
     * 主版本号（用于版本管理）
     */
        private Integer majorVersion = 1;

    /**
     * 次版本号
     */
        private Integer minorVersion = 0;

    /**
     * 修订版本号
     */
        private Integer patchVersion = 0;

    /**
     * 父模板ID（用于版本继承）
     */
        private Long parentId;

    /**
     * 根模板ID（版本链的根节点）
     */
        private Long rootId;

    /**
     * 模板状态
     * DRAFT - 草稿
     * ACTIVE - 活跃
     * DEPRECATED - 已弃用
     * ARCHIVED - 已归档
     */
        private String status = "DRAFT";

    /**
     * 模板所属用户ID
     */
        private Long userId;

    /**
     * 是否公开模板
     */
        private Boolean isPublic = false;

    /**
     * 是否为系统模板
     */
        private Boolean isSystem = false;

    /**
     * 是否为默认模板
     */
        private Boolean isDefault = false;

    /**
     * 模板参数定义（JSON格式）
     */
        private String parameters;

    /**
     * 模板变量定义（JSON格式）
     */
        private String variables;

    /**
     * 模板配置（JSON格式）
     */
        private String config;

    /**
     * 模板元数据（JSON格式）
     */
        private String metadata;

    /**
     * 使用次数
     */
        private Long usageCount = 0L;

    /**
     * 最后使用时间
     */
        private LocalDateTime lastUsedAt;

    /**
     * 评分（1-5星）
     */
        private Double rating;

    /**
     * 评分次数
     */
        private Long ratingCount = 0L;

    /**
     * 下载次数
     */
        private Long downloadCount = 0L;

    /**
     * 收藏次数
     */
        private Long favoriteCount = 0L;

    /**
     * 模板图标URL
     */
        private String iconUrl;

    /**
     * 模板截图URL
     */
        private String screenshotUrl;

    /**
     * 文档URL
     */
        private String documentationUrl;

    /**
     * 示例URL
     */
        private String exampleUrl;

    /**
     * 许可证
     */
        private String license;

    /**
     * 作者信息
     */
        private String author;

    /**
     * 贡献者信息（JSON格式）
     */
        private String contributors;

    /**
     * 发布时间
     */
        private LocalDateTime publishedAt;

    /**
     * 归档时间
     */
        private LocalDateTime archivedAt;

    /**
     * 模板类型枚举
     */
    public enum Type {
        SYSTEM("SYSTEM", "系统提示词"),
        USER("USER", "用户提示词"),
        ASSISTANT("ASSISTANT", "助手提示词"),
        FUNCTION("FUNCTION", "函数调用提示词"),
        WORKFLOW("WORKFLOW", "工作流提示词"),
        CONVERSATION("CONVERSATION", "对话提示词");

        private final String code;
        private final String description;

        Type(String code, String description) {
            this.code = code;
            this.description = description;
        }

        public String getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 模板状态枚举
     */
    public enum Status {
        DRAFT("DRAFT", "草稿"),
        ACTIVE("ACTIVE", "活跃"),
        DEPRECATED("DEPRECATED", "已弃用"),
        ARCHIVED("ARCHIVED", "已归档");

        private final String code;
        private final String description;

        Status(String code, String description) {
            this.code = code;
            this.description = description;
        }

        public String getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 更新使用统计
     */
    public void updateUsageStats() {
        this.usageCount++;
        this.lastUsedAt = LocalDateTime.now();
    }

    /**
     * 更新评分
     */
    public void updateRating(double newRating) {
        if (this.rating == null) {
            this.rating = newRating;
            this.ratingCount = 1L;
        } else {
            double totalScore = this.rating * this.ratingCount + newRating;
            this.ratingCount++;
            this.rating = totalScore / this.ratingCount;
        }
    }

    /**
     * 增加下载次数
     */
    public void incrementDownloadCount() {
        this.downloadCount++;
    }

    /**
     * 增加收藏次数
     */
    public void incrementFavoriteCount() {
        this.favoriteCount++;
    }

    /**
     * 减少收藏次数
     */
    public void decrementFavoriteCount() {
        if (this.favoriteCount > 0) {
            this.favoriteCount--;
        }
    }

    /**
     * 检查是否为草稿状态
     */
    public boolean isDraft() {
        return Status.DRAFT.getCode().equals(this.status);
    }

    /**
     * 检查是否为活跃状态
     */
    public boolean isActive() {
        return Status.ACTIVE.getCode().equals(this.status);
    }

    /**
     * 检查是否已弃用
     */
    public boolean isDeprecated() {
        return Status.DEPRECATED.getCode().equals(this.status);
    }

    /**
     * 检查是否已归档
     */
    public boolean isArchived() {
        return Status.ARCHIVED.getCode().equals(this.status);
    }

    /**
     * 发布模板
     */
    public void publish() {
        this.status = Status.ACTIVE.getCode();
        this.publishedAt = LocalDateTime.now();
    }

    /**
     * 弃用模板
     */
    public void deprecate() {
        this.status = Status.DEPRECATED.getCode();
    }

    /**
     * 归档模板
     */
    public void archive() {
        this.status = Status.ARCHIVED.getCode();
        this.archivedAt = LocalDateTime.now();
    }

    /**
     * 获取完整版本号
     */
    public String getFullVersion() {
        return String.format("%d.%d.%d", majorVersion, minorVersion, patchVersion);
    }

    /**
     * 设置版本号
     */
    public void setVersionNumbers(String version) {
        this.version = version;
        String[] parts = version.split("\\.");
        if (parts.length >= 1) {
            this.majorVersion = Integer.parseInt(parts[0]);
        }
        if (parts.length >= 2) {
            this.minorVersion = Integer.parseInt(parts[1]);
        }
        if (parts.length >= 3) {
            this.patchVersion = Integer.parseInt(parts[2]);
        }
    }

    /**
     * 创建新版本
     */
    public void createNewVersion(String versionType) {
        switch (versionType.toLowerCase()) {
            case "major":
                this.majorVersion++;
                this.minorVersion = 0;
                this.patchVersion = 0;
                break;
            case "minor":
                this.minorVersion++;
                this.patchVersion = 0;
                break;
            case "patch":
            default:
                this.patchVersion++;
                break;
        }
        this.version = getFullVersion();
    }


    // Lombok生成的getter/setter方法
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getMajorVersion() {
        return majorVersion;
    }

    public void setMajorVersion(Integer majorVersion) {
        this.majorVersion = majorVersion;
    }

    public Integer getMinorVersion() {
        return minorVersion;
    }

    public void setMinorVersion(Integer minorVersion) {
        this.minorVersion = minorVersion;
    }

    public Integer getPatchVersion() {
        return patchVersion;
    }

    public void setPatchVersion(Integer patchVersion) {
        this.patchVersion = patchVersion;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getRootId() {
        return rootId;
    }

    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean isIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Boolean isIsSystem() {
        return isSystem;
    }

    public void setIsSystem(Boolean isSystem) {
        this.isSystem = isSystem;
    }

    public Boolean isIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getVariables() {
        return variables;
    }

    public void setVariables(String variables) {
        this.variables = variables;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public Long getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(Long usageCount) {
        this.usageCount = usageCount;
    }

    public LocalDateTime getLastUsedAt() {
        return lastUsedAt;
    }

    public void setLastUsedAt(LocalDateTime lastUsedAt) {
        this.lastUsedAt = lastUsedAt;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Long getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Long ratingCount) {
        this.ratingCount = ratingCount;
    }

    public Long getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Long downloadCount) {
        this.downloadCount = downloadCount;
    }

    public Long getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(Long favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getScreenshotUrl() {
        return screenshotUrl;
    }

    public void setScreenshotUrl(String screenshotUrl) {
        this.screenshotUrl = screenshotUrl;
    }

    public String getDocumentationUrl() {
        return documentationUrl;
    }

    public void setDocumentationUrl(String documentationUrl) {
        this.documentationUrl = documentationUrl;
    }

    public String getExampleUrl() {
        return exampleUrl;
    }

    public void setExampleUrl(String exampleUrl) {
        this.exampleUrl = exampleUrl;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContributors() {
        return contributors;
    }

    public void setContributors(String contributors) {
        this.contributors = contributors;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public LocalDateTime getArchivedAt() {
        return archivedAt;
    }

    public void setArchivedAt(LocalDateTime archivedAt) {
        this.archivedAt = archivedAt;
    }
}
