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

import com.coze.studio.entity.enums.PublishStatus;
// JPA注解已移除，改用MyBatis
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Bot 实体类
 * 表示一个 AI 机器人应用
 * 
 * @author coze-dev
 */
@Data
@EqualsAndHashCode(callSuper = true)
// 表名: bots
public class Bot extends BaseEntity {

    /**
     * 空间ID
     */
        private Long spaceId;

    /**
     * 图标URI
     */
        private String iconUri;

    /**
     * Bot名称
     */
        private String name;

    /**
     * Bot描述
     */
        private String description;

    /**
     * 所有者ID
     */
        private Long ownerId;

    /**
     * 连接器ID列表
     */
        private List<Long> connectorIds;

    /**
     * 版本号
     */
        private String version;

    /**
     * 版本描述
     */
        private String versionDesc;

    /**
     * 发布记录ID
     */
        private Long publishRecordId;

    /**
     * 发布状态
     */
            private PublishStatus publishStatus;

    /**
     * 发布时间
     */
        private LocalDateTime publishedAt;

    /**
     * 场景类型
     */
        private String scene;

    /**
     * 是否启用
     */
        private Boolean enabled = true;


    // Lombok生成的getter/setter方法
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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public List<Long> getConnectorIds() {
        return connectorIds;
    }

    public void setConnectorIds(List<Long> connectorIds) {
        this.connectorIds = connectorIds;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersionDesc() {
        return versionDesc;
    }

    public void setVersionDesc(String versionDesc) {
        this.versionDesc = versionDesc;
    }

    public Long getPublishRecordId() {
        return publishRecordId;
    }

    public void setPublishRecordId(Long publishRecordId) {
        this.publishRecordId = publishRecordId;
    }

    public PublishStatus getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(PublishStatus publishStatus) {
        this.publishStatus = publishStatus;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
