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
 * 插件版本实体类
 * 管理插件的版本信息和历史
 *
 * @author coze-dev
 */
@Data
@EqualsAndHashCode(callSuper = true)
// 表名: plugin_versions
public class PluginVersion extends BaseEntity {

    /**
     * 插件ID
     */
    private Long pluginId;

    /**
     * 版本号
     */
    private String version;

    /**
     * 版本标题
     */
    private String title;

    /**
     * 版本描述
     */
    private String description;

    /**
     * 发布说明
     */
    private String releaseNotes;

    /**
     * 是否为最新版本
     */
    private Boolean isLatest = false;

    /**
     * 是否为稳定版本
     */
    private Boolean isStable = true;

    /**
     * 最低兼容版本
     */
    private String minCompatibleVersion;

    /**
     * 版本状态
     * DRAFT: 草稿, PUBLISHED: 已发布, DEPRECATED: 已弃用, ARCHIVED: 已归档
     */
    private String status = "DRAFT";

    /**
     * 发布者ID
     */
    private Long publisherId;

    /**
     * 发布时间
     */
    private java.time.LocalDateTime publishedAt;

    /**
     * 审核状态
     */
    private String reviewStatus;

    /**
     * 审核意见
     */
    private String reviewComment;

    /**
     * 审核人ID
     */
    private Long reviewerId;

    /**
     * 审核时间
     */
    private java.time.LocalDateTime reviewedAt;

    /**
     * 版本文件信息（JSON格式）
     */
    private String versionFiles;

    /**
     * 依赖信息（JSON格式）
     */
    private String dependencies;

    /**
     * 配置模板（JSON格式）
     */
    private String configTemplate;

    /**
     * 下载次数
     */
    private Long downloadCount = 0L;

    /**
     * 安装次数
     */
    private Long installCount = 0L;

    // 构造函数
    public PluginVersion() {
    }

    public PluginVersion(Long pluginId, String version) {
        this.pluginId = pluginId;
        this.version = version;
        this.status = "DRAFT";
        this.isStable = true;
    }

    // 业务方法
    public boolean isPublished() {
        return "PUBLISHED".equals(this.status);
    }

    public boolean isDeprecated() {
        return "DEPRECATED".equals(this.status);
    }

    public void publish() {
        this.status = "PUBLISHED";
        this.publishedAt = java.time.LocalDateTime.now();
    }

    public void deprecate() {
        this.status = "DEPRECATED";
        this.isLatest = false;
    }

    public void archive() {
        this.status = "ARCHIVED";
        this.isLatest = false;
    }

    public void markAsLatest() {
        this.isLatest = true;
    }

    public void unmarkAsLatest() {
        this.isLatest = false;
    }
}
