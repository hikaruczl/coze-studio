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

import com.coze.studio.entity.enums.DocumentStatus;
import com.coze.studio.entity.enums.DocumentType;
// JPA注解已移除，改用MyBatis
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文档实体类
 * 表示知识库中的一个文档
 * 
 * @author coze-dev
 */
@Data
@EqualsAndHashCode(callSuper = true)
// 表名: documents
public class Document extends BaseEntity {

    /**
     * 所属知识库
     */
            private KnowledgeBase knowledgeBase;

    /**
     * 文档名称
     */
        private String name;

    /**
     * 文档类型
     */
            private DocumentType type;

    /**
     * 文档URI（存储路径）
     */
        private String uri;

    /**
     * 文档URL（如果是网页链接）
     */
        private String url;

    /**
     * 文档大小（字节）
     */
        private Long size;

    /**
     * 切片数量
     */
        private Long sliceCount;

    /**
     * 字符数量
     */
        private Long charCount;

    /**
     * 文件扩展名
     */
        private String fileExtension;

    /**
     * 文档状态
     */
            private DocumentStatus status;

    /**
     * 状态消息
     */
        private String statusMsg;

    /**
     * 是否启用
     */
        private Boolean enabled = true;

    /**
     * 文档内容摘要
     */
        private String summary;

    /**
     * 获取标题（兼容方法）
     */
    public String getTitle() {
        return name;
    }

    /**
     * 设置标题（兼容方法）
     */
    public void setTitle(String title) {
        this.name = title;
    }


    // Lombok生成的getter/setter方法
    public KnowledgeBase getKnowledgeBase() {
        return knowledgeBase;
    }

    public void setKnowledgeBase(KnowledgeBase knowledgeBase) {
        this.knowledgeBase = knowledgeBase;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DocumentType getType() {
        return type;
    }

    public void setType(DocumentType type) {
        this.type = type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getSliceCount() {
        return sliceCount;
    }

    public void setSliceCount(Long sliceCount) {
        this.sliceCount = sliceCount;
    }

    public Long getCharCount() {
        return charCount;
    }

    public void setCharCount(Long charCount) {
        this.charCount = charCount;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public DocumentStatus getStatus() {
        return status;
    }

    public void setStatus(DocumentStatus status) {
        this.status = status;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
