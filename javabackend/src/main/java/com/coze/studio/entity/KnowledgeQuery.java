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
 * 知识库查询记录实体类
 * 记录用户对知识库的查询历史和结果
 * 
 * @author coze-dev
 */
@Data
@EqualsAndHashCode(callSuper = true)
// 表名: knowledge_queries
// 索引信息已移至数据库DDL脚本
public class KnowledgeQuery extends BaseEntity {

    /**
     * 用户ID
     */
        private Long userId;

    /**
     * 知识库ID
     */
        private Long knowledgeBaseId;

    /**
     * 查询文本
     */
        private String queryText;

    /**
     * 查询类型：SIMILARITY-相似度搜索, KEYWORD-关键词搜索, HYBRID-混合搜索
     */
        private String queryType = "SIMILARITY";

    /**
     * 查询参数（JSON格式）
     */
        private String queryParams;

    /**
     * 返回的切片数量
     */
        private Integer resultCount;

    /**
     * 查询结果（JSON格式）
     */
        private String queryResults;

    /**
     * 查询耗时（毫秒）
     */
        private Long queryTime;

    /**
     * 查询状态：SUCCESS-成功, FAILED-失败, TIMEOUT-超时
     */
        private String status = "SUCCESS";

    /**
     * 错误信息
     */
        private String errorMessage;

    /**
     * 会话ID（用于关联对话）
     */
        private String sessionId;

    /**
     * 对话ID（用于关联对话）
     */
        private Long conversationId;

    /**
     * 消息ID（用于关联消息）
     */
        private Long messageId;

    /**
     * 查询来源：CHAT-对话, API-API调用, WORKFLOW-工作流
     */
        private String querySource = "CHAT";

    /**
     * 客户端IP
     */
        private String clientIp;

    /**
     * 用户代理
     */
        private String userAgent;

    /**
     * 查询向量（JSON格式）
     */
        private String queryVector;

    /**
     * 使用的嵌入模型
     */
        private String embeddingModel;

    /**
     * 相似度阈值
     */
        private Double similarityThreshold;

    /**
     * 最大返回数量
     */
        private Integer maxResults;

    /**
     * 是否使用重排序
     */
        private Boolean useRerank = false;

    /**
     * 重排序模型
     */
        private String rerankModel;

    /**
     * 查询扩展信息（JSON格式）
     */
        private String queryExpansion;


    // Lombok生成的getter/setter方法
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getKnowledgeBaseId() {
        return knowledgeBaseId;
    }

    public void setKnowledgeBaseId(Long knowledgeBaseId) {
        this.knowledgeBaseId = knowledgeBaseId;
    }

    public String getQueryText() {
        return queryText;
    }

    public void setQueryText(String queryText) {
        this.queryText = queryText;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public String getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(String queryParams) {
        this.queryParams = queryParams;
    }

    public Integer getResultCount() {
        return resultCount;
    }

    public void setResultCount(Integer resultCount) {
        this.resultCount = resultCount;
    }

    public String getQueryResults() {
        return queryResults;
    }

    public void setQueryResults(String queryResults) {
        this.queryResults = queryResults;
    }

    public Long getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(Long queryTime) {
        this.queryTime = queryTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getQuerySource() {
        return querySource;
    }

    public void setQuerySource(String querySource) {
        this.querySource = querySource;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getQueryVector() {
        return queryVector;
    }

    public void setQueryVector(String queryVector) {
        this.queryVector = queryVector;
    }

    public String getEmbeddingModel() {
        return embeddingModel;
    }

    public void setEmbeddingModel(String embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    public Double getSimilarityThreshold() {
        return similarityThreshold;
    }

    public void setSimilarityThreshold(Double similarityThreshold) {
        this.similarityThreshold = similarityThreshold;
    }

    public Integer getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }

    public Boolean isUseRerank() {
        return useRerank;
    }

    public void setUseRerank(Boolean useRerank) {
        this.useRerank = useRerank;
    }

    public String getRerankModel() {
        return rerankModel;
    }

    public void setRerankModel(String rerankModel) {
        this.rerankModel = rerankModel;
    }

    public String getQueryExpansion() {
        return queryExpansion;
    }

    public void setQueryExpansion(String queryExpansion) {
        this.queryExpansion = queryExpansion;
    }
}
