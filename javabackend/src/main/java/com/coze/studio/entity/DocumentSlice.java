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
 * 文档切片实体类
 * 表示文档的一个切片，用于向量检索
 * 
 * @author coze-dev
 */
@Data
@EqualsAndHashCode(callSuper = true)
// 表名: document_slices
// 索引信息已移至数据库DDL脚本
public class DocumentSlice extends BaseEntity {

    /**
     * 所属文档
     */
            private Document document;

    /**
     * 所属知识库ID（冗余字段，便于查询）
     */
        private Long knowledgeBaseId;

    /**
     * 切片内容
     */
        private String content;

    /**
     * 切片在文档中的位置
     */
        private Integer position;

    /**
     * 切片字符数
     */
        private Integer charCount;

    /**
     * 切片token数
     */
        private Integer tokenCount;

    /**
     * 向量ID（在向量数据库中的ID）
     */
        private String vectorId;

    /**
     * 向量嵌入（JSON格式存储）
     */
        private String embedding;

    /**
     * 切片摘要
     */
        private String summary;

    /**
     * 切片关键词（JSON格式）
     */
        private String keywords;

    /**
     * 切片元数据（JSON格式）
     */
        private String metadata;

    /**
     * 是否启用
     */
        private Boolean enabled = true;

    /**
     * 处理状态：PENDING-待处理, PROCESSING-处理中, COMPLETED-已完成, FAILED-失败
     */
        private String processingStatus = "PENDING";

    /**
     * 处理错误信息
     */
        private String errorMessage;

    /**
     * 向量化时间
     */
        private java.time.LocalDateTime vectorizedAt;

    /**
     * 使用的嵌入模型
     */
        private String embeddingModel;

    /**
     * 嵌入维度
     */
        private Integer embeddingDimension;


    // Lombok生成的getter/setter方法
    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public Long getKnowledgeBaseId() {
        return knowledgeBaseId;
    }

    public void setKnowledgeBaseId(Long knowledgeBaseId) {
        this.knowledgeBaseId = knowledgeBaseId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getCharCount() {
        return charCount;
    }

    public void setCharCount(Integer charCount) {
        this.charCount = charCount;
    }

    public Integer getTokenCount() {
        return tokenCount;
    }

    public void setTokenCount(Integer tokenCount) {
        this.tokenCount = tokenCount;
    }

    public String getVectorId() {
        return vectorId;
    }

    public void setVectorId(String vectorId) {
        this.vectorId = vectorId;
    }

    public String getEmbedding() {
        return embedding;
    }

    public void setEmbedding(String embedding) {
        this.embedding = embedding;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getProcessingStatus() {
        return processingStatus;
    }

    public void setProcessingStatus(String processingStatus) {
        this.processingStatus = processingStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getEmbeddingModel() {
        return embeddingModel;
    }

    public void setEmbeddingModel(String embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    public Integer getEmbeddingDimension() {
        return embeddingDimension;
    }

    public void setEmbeddingDimension(Integer embeddingDimension) {
        this.embeddingDimension = embeddingDimension;
    }
}
