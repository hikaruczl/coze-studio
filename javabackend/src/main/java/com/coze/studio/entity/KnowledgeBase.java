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

import com.coze.studio.entity.enums.KnowledgeStatus;
// JPA注解已移除，改用MyBatis
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 知识库实体类
 * 表示一个知识库，包含多个文档
 * 
 * @author coze-dev
 */
@Data
@EqualsAndHashCode(callSuper = true)
// 表名: knowledge_bases
public class KnowledgeBase extends BaseEntity {

    /**
     * 知识库名称
     */
        private String name;

    /**
     * 知识库描述
     */
        private String description;

    /**
     * 图标URI
     */
        private String iconUri;

    /**
     * 创建者ID
     */
        private Long creatorId;

    /**
     * 空间ID
     */
        private Long spaceId;

    /**
     * 关联的应用ID
     */
        private Long appId;

    /**
     * 知识库状态
     */
            private KnowledgeStatus status;

    /**
     * 关联的文档列表
     */
        private List<Document> documents;

    /**
     * 是否启用
     */
        private Boolean enabled = true;

    /**
     * 文档总数
     */
        private Integer documentCount = 0;

    /**
     * 总字符数
     */
        private Long totalCharCount = 0L;


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

    public String getIconUri() {
        return iconUri;
    }

    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Long spaceId) {
        this.spaceId = spaceId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public KnowledgeStatus getStatus() {
        return status;
    }

    public void setStatus(KnowledgeStatus status) {
        this.status = status;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getDocumentCount() {
        return documentCount;
    }

    public void setDocumentCount(Integer documentCount) {
        this.documentCount = documentCount;
    }

    public Long getTotalCharCount() {
        return totalCharCount;
    }

    public void setTotalCharCount(Long totalCharCount) {
        this.totalCharCount = totalCharCount;
    }
}
