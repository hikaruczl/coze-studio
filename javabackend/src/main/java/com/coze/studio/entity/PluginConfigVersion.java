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
import java.util.Map;

/**
 * 插件配置版本实体类
 * 管理插件配置的历史版本和变更记录
 *
 * @author coze-dev
 */
@Data
@EqualsAndHashCode(callSuper = true)
// 表名: plugin_config_versions
public class PluginConfigVersion {

    /**
     * 版本ID
     */
    private Long id;

    /**
     * 插件ID
     */
    private Long pluginId;

    /**
     * 版本号 (如: 1.0.0, 2.1.3)
     */
    private String version;

    /**
     * 版本标签 (如: v1.0, stable, beta)
     */
    private String versionTag;

    /**
     * 配置数据 (JSON格式)
     */
    private Map<String, Object> configData;

    /**
     * 变更说明
     */
    private String changeLog;

    /**
     * 变更类型 (MAJOR, MINOR, PATCH, HOTFIX)
     */
    private String changeType;

    /**
     * 变更原因
     */
    private String changeReason;

    /**
     * 版本状态 (DRAFT, PUBLISHED, ARCHIVED, DEPRECATED)
     */
    private String status;

    /**
     * 是否为稳定版本
     */
    private Boolean isStable;

    /**
     * 是否为默认版本
     */
    private Boolean isDefault;

    /**
     * 创建者ID
     */
    private Long createdBy;

    /**
     * 审核者ID
     */
    private Long reviewedBy;

    /**
     * 审核时间
     */
    private LocalDateTime reviewedAt;

    /**
     * 发布时间
     */
    private LocalDateTime publishedAt;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 版本比较结果
     */
    private String comparisonResult;

    /**
     * 回滚次数
     */
    private Integer rollbackCount;

    /**
     * 元数据 (JSON格式)
     */
    private Map<String, Object> metadata;

    // ==================== 构造函数 ====================

    public PluginConfigVersion() {
        this.status = "DRAFT";
        this.isStable = false;
        this.isDefault = false;
        this.rollbackCount = 0;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // ==================== 业务方法 ====================

    /**
     * 发布版本
     */
    public void publish() {
        this.status = "PUBLISHED";
        this.publishedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 归档版本
     */
    public void archive() {
        this.status = "ARCHIVED";
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 标记为稳定版本
     */
    public void markAsStable() {
        this.isStable = true;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 设置为默认版本
     */
    public void setAsDefault() {
        this.isDefault = true;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 取消默认版本
     */
    public void unsetAsDefault() {
        this.isDefault = false;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 审核版本
     */
    public void review(Long reviewerId, boolean approved) {
        this.reviewedBy = reviewerId;
        this.reviewedAt = LocalDateTime.now();
        this.status = approved ? "APPROVED" : "REJECTED";
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 增加回滚计数
     */
    public void incrementRollbackCount() {
        if (this.rollbackCount == null) {
            this.rollbackCount = 0;
        }
        this.rollbackCount++;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 检查是否可以编辑
     */
    public boolean canEdit() {
        return "DRAFT".equals(this.status) || "REJECTED".equals(this.status);
    }

    /**
     * 检查是否可以发布
     */
    public boolean canPublish() {
        return "DRAFT".equals(this.status) || "APPROVED".equals(this.status);
    }

    /**
     * 检查是否可以回滚
     */
    public boolean canRollback() {
        return "PUBLISHED".equals(this.status) && this.isStable != null && this.isStable;
    }

    /**
     * 获取版本显示名称
     */
    public String getDisplayName() {
        StringBuilder sb = new StringBuilder();
        if (this.versionTag != null && !this.versionTag.isEmpty()) {
            sb.append(this.versionTag).append(" ");
        }
        if (this.version != null) {
            sb.append("v").append(this.version);
        }
        if (this.isStable != null && this.isStable) {
            sb.append(" (稳定版)");
        }
        if (this.isDefault != null && this.isDefault) {
            sb.append(" (默认)");
        }
        return sb.toString().trim();
    }

    /**
     * 获取版本状态描述
     */
    public String getStatusDescription() {
        return switch (this.status) {
            case "DRAFT" -> "草稿";
            case "APPROVED" -> "已审核";
            case "PUBLISHED" -> "已发布";
            case "ARCHIVED" -> "已归档";
            case "REJECTED" -> "已拒绝";
            case "DEPRECATED" -> "已废弃";
            default -> "未知状态";
        };
    }

    /**
     * 获取变更类型描述
     */
    public String getChangeTypeDescription() {
        return switch (this.changeType) {
            case "MAJOR" -> "主要版本";
            case "MINOR" -> "次要版本";
            case "PATCH" -> "补丁版本";
            case "HOTFIX" -> "热修复";
            default -> "其他变更";
        };
    }

    // ==================== 静态方法 ====================

    /**
     * 创建新版本
     */
    public static PluginConfigVersion createNewVersion(Long pluginId, String version, Map<String, Object> configData, Long createdBy) {
        PluginConfigVersion newVersion = new PluginConfigVersion();
        newVersion.setPluginId(pluginId);
        newVersion.setVersion(version);
        newVersion.setConfigData(configData);
        newVersion.setCreatedBy(createdBy);
        newVersion.setStatus("DRAFT");
        newVersion.setIsStable(false);
        newVersion.setIsDefault(false);
        newVersion.setRollbackCount(0);
        return newVersion;
    }

    /**
     * 从现有配置创建新版本
     */
    public static PluginConfigVersion createFromExisting(PluginConfigVersion existing, String newVersion, Long createdBy) {
        PluginConfigVersion newVersionObj = new PluginConfigVersion();
        newVersionObj.setPluginId(existing.getPluginId());
        newVersionObj.setVersion(newVersion);
        newVersionObj.setConfigData(existing.getConfigData()); // 复制配置数据
        newVersionObj.setCreatedBy(createdBy);
        newVersionObj.setStatus("DRAFT");
        newVersionObj.setIsStable(false);
        newVersionObj.setIsDefault(false);
        newVersionObj.setRollbackCount(0);

        // 继承元数据
        if (existing.getMetadata() != null) {
            newVersionObj.setMetadata(new java.util.HashMap<>(existing.getMetadata()));
        }

        return newVersionObj;
    }
}
