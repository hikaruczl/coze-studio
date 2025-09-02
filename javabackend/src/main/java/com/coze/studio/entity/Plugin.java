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

import com.coze.studio.entity.enums.PluginType;
// JPA注解已移除，改用MyBatis
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 插件实体类
 * 表示一个可执行的插件
 * 
 * @author coze-dev
 */
@Data
@EqualsAndHashCode(callSuper = true)
// 表名: plugins
public class Plugin extends BaseEntity {

    /**
     * 插件名称
     */
        private String name;

    /**
     * 插件描述
     */
        private String description;

    /**
     * 插件标签（逗号分隔）
     */
        private String tags;

    /**
     * 开发者ID
     */
        private Long developerId;

    /**
     * 空间ID
     */
        private Long spaceId;

    /**
     * 图标URI
     */
        private String iconUri;

    /**
     * 服务器URL
     */
        private String serverUrl;

    /**
     * 版本号
     */
        private String version;

    /**
     * OpenAPI 规范文档
     * 存储完整的 OpenAPI 规范（JSON 或 YAML 格式）
     */
            private String openapiDoc;

    /**
     * 插件清单
     * 存储插件的元数据信息（JSON 格式）
     */
            private String manifest;

    /**
     * 插件类型
     */
            private PluginType type;

    /**
     * 是否已发布
     */
        private Boolean published = false;

    /**
     * 是否启用
     */
        private Boolean enabled = true;

    /**
     * 客户端ID（OAuth认证用）
     */
        private String clientId;

    /**
     * 客户端密钥（OAuth认证用）
     */
        private String clientSecret;

    /**
     * 服务令牌
     */
    private String serviceToken;

    /**
     * 审核状态
     */
    private String reviewStatus;

    /**
     * 插件分类
     */
    private String category;

    /**
     * 图标URL
     */
    private String iconUrl;

    /**
     * 使用次数
     */
    private Long usageCount = 0L;

    /**
     * 安装次数
     */
    private Long installCount = 0L;

    /**
     * 平均评分
     */
    private Double averageRating = 0.0;

    /**
     * 评分数量
     */
    private Integer ratingCount = 0;

    /**
     * 文档URL
     */
    private String documentationUrl;

    /**
     * 支持URL
     */
    private String supportUrl;

    /**
     * 许可证
     */
    private String license;

    /**
     * 是否私有
     */
    private Boolean isPrivate = false;

    /**
     * 插件状态
     */
    private String status;

    /**
     * 插件类型（字符串形式）
     */
    private String pluginType;

    /**
     * 当前版本ID（指向PluginVersion表）
     */
    private Long currentVersionId;

    /**
     * 最新版本号
     */
    private String latestVersion;

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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Long getDeveloperId() {
        return developerId;
    }

    public void setDeveloperId(Long developerId) {
        this.developerId = developerId;
    }

    public Long getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Long spaceId) {
        this.spaceId = spaceId;
    }

    public String getIconUri() {
        return iconUri;
    }

    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getOpenapiDoc() {
        return openapiDoc;
    }

    public void setOpenapiDoc(String openapiDoc) {
        this.openapiDoc = openapiDoc;
    }

    public String getManifest() {
        return manifest;
    }

    public void setManifest(String manifest) {
        this.manifest = manifest;
    }

    public PluginType getType() {
        return type;
    }

    public void setType(PluginType type) {
        this.type = type;
    }

    public Boolean isPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getServiceToken() {
        return serviceToken;
    }

    public void setServiceToken(String serviceToken) {
        this.serviceToken = serviceToken;
    }

    public String getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(String reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Long getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(Long usageCount) {
        this.usageCount = usageCount;
    }

    public Long getInstallCount() {
        return installCount;
    }

    public void setInstallCount(Long installCount) {
        this.installCount = installCount;
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

    public String getDocumentationUrl() {
        return documentationUrl;
    }

    public void setDocumentationUrl(String documentationUrl) {
        this.documentationUrl = documentationUrl;
    }

    public String getSupportUrl() {
        return supportUrl;
    }

    public void setSupportUrl(String supportUrl) {
        this.supportUrl = supportUrl;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPluginType() {
        return pluginType;
    }

    public void setPluginType(String pluginType) {
        this.pluginType = pluginType;
    }

    // 兼容性方法
    public Long getCreatorId() {
        return getDeveloperId();
    }

    public void setCreatorId(Long creatorId) {
        setDeveloperId(creatorId);
    }

    public String getServiceUrl() {
        return getServerUrl();
    }

    public void setServiceUrl(String serviceUrl) {
        setServerUrl(serviceUrl);
    }
}
