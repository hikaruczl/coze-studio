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

package com.coze.studio.service;

import java.util.List;
import java.util.Map;

/**
 * RAG (Retrieval-Augmented Generation) 服务接口
 * 
 * @author coze-dev
 */
public interface RAGService {

    /**
     * 执行RAG查询
     */
    RAGResult query(RAGRequest request);

    /**
     * 检索相关文档
     */
    RetrievalResult retrieve(RetrievalRequest request);

    /**
     * 生成增强回答
     */
    GenerationResult generate(GenerationRequest request);

    /**
     * 重排序检索结果
     */
    RerankResult rerank(RerankRequest request);

    /**
     * 查询扩展
     */
    QueryExpansionResult expandQuery(String query, QueryExpansionOptions options);

    /**
     * 获取查询建议
     */
    List<String> getQuerySuggestions(String partialQuery, Long knowledgeBaseId, int maxSuggestions);

    /**
     * RAG请求
     */
    class RAGRequest {
        private String query;
        private Long knowledgeBaseId;
        private Long userId;
        private String sessionId;
        private Long conversationId;
        private RetrievalOptions retrievalOptions;
        private GenerationOptions generationOptions;
        private Map<String, Object> context;

        // Getters and setters
        public String getQuery() { return query; }
        public void setQuery(String query) { this.query = query; }
        
        public Long getKnowledgeBaseId() { return knowledgeBaseId; }
        public void setKnowledgeBaseId(Long knowledgeBaseId) { this.knowledgeBaseId = knowledgeBaseId; }
        
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        
        public String getSessionId() { return sessionId; }
        public void setSessionId(String sessionId) { this.sessionId = sessionId; }
        
        public Long getConversationId() { return conversationId; }
        public void setConversationId(Long conversationId) { this.conversationId = conversationId; }
        
        public RetrievalOptions getRetrievalOptions() { return retrievalOptions; }
        public void setRetrievalOptions(RetrievalOptions retrievalOptions) { this.retrievalOptions = retrievalOptions; }
        
        public GenerationOptions getGenerationOptions() { return generationOptions; }
        public void setGenerationOptions(GenerationOptions generationOptions) { this.generationOptions = generationOptions; }
        
        public Map<String, Object> getContext() { return context; }
        public void setContext(Map<String, Object> context) { this.context = context; }
    }

    /**
     * 检索选项
     */
    class RetrievalOptions {
        private int topK = 5;
        private double similarityThreshold = 0.7;
        private String searchType = "HYBRID"; // SIMILARITY, KEYWORD, HYBRID
        private boolean useRerank = true;
        private String rerankModel = "bge-reranker-large";
        private boolean enableQueryExpansion = true;
        private Map<String, Object> filter;
        private List<String> includeFields;
        private List<String> excludeFields;

        // Getters and setters
        public int getTopK() { return topK; }
        public void setTopK(int topK) { this.topK = topK; }
        
        public double getSimilarityThreshold() { return similarityThreshold; }
        public void setSimilarityThreshold(double similarityThreshold) { this.similarityThreshold = similarityThreshold; }
        
        public String getSearchType() { return searchType; }
        public void setSearchType(String searchType) { this.searchType = searchType; }
        
        public boolean isUseRerank() { return useRerank; }
        public void setUseRerank(boolean useRerank) { this.useRerank = useRerank; }
        
        public String getRerankModel() { return rerankModel; }
        public void setRerankModel(String rerankModel) { this.rerankModel = rerankModel; }
        
        public boolean isEnableQueryExpansion() { return enableQueryExpansion; }
        public void setEnableQueryExpansion(boolean enableQueryExpansion) { this.enableQueryExpansion = enableQueryExpansion; }
        
        public Map<String, Object> getFilter() { return filter; }
        public void setFilter(Map<String, Object> filter) { this.filter = filter; }
        
        public List<String> getIncludeFields() { return includeFields; }
        public void setIncludeFields(List<String> includeFields) { this.includeFields = includeFields; }
        
        public List<String> getExcludeFields() { return excludeFields; }
        public void setExcludeFields(List<String> excludeFields) { this.excludeFields = excludeFields; }
    }

    /**
     * 生成选项
     */
    class GenerationOptions {
        private String model = "gpt-3.5-turbo";
        private double temperature = 0.7;
        private int maxTokens = 1000;
        private String systemPrompt;
        private boolean includeSource = true;
        private boolean includeCitation = true;
        private String responseFormat = "TEXT"; // TEXT, JSON, MARKDOWN
        private Map<String, Object> modelParams;

        // Getters and setters
        public String getModel() { return model; }
        public void setModel(String model) { this.model = model; }
        
        public double getTemperature() { return temperature; }
        public void setTemperature(double temperature) { this.temperature = temperature; }
        
        public int getMaxTokens() { return maxTokens; }
        public void setMaxTokens(int maxTokens) { this.maxTokens = maxTokens; }
        
        public String getSystemPrompt() { return systemPrompt; }
        public void setSystemPrompt(String systemPrompt) { this.systemPrompt = systemPrompt; }
        
        public boolean isIncludeSource() { return includeSource; }
        public void setIncludeSource(boolean includeSource) { this.includeSource = includeSource; }
        
        public boolean isIncludeCitation() { return includeCitation; }
        public void setIncludeCitation(boolean includeCitation) { this.includeCitation = includeCitation; }
        
        public String getResponseFormat() { return responseFormat; }
        public void setResponseFormat(String responseFormat) { this.responseFormat = responseFormat; }
        
        public Map<String, Object> getModelParams() { return modelParams; }
        public void setModelParams(Map<String, Object> modelParams) { this.modelParams = modelParams; }
    }

    /**
     * RAG结果
     */
    class RAGResult {
        private boolean success;
        private String answer;
        private List<RetrievedDocument> sources;
        private String error;
        private long totalTime;
        private long retrievalTime;
        private long generationTime;
        private Map<String, Object> metadata;
        private List<String> citations;

        public RAGResult(boolean success, String answer, List<RetrievedDocument> sources, String error) {
            this.success = success;
            this.answer = answer;
            this.sources = sources;
            this.error = error;
        }

        // Getters and setters
        public boolean isSuccess() { return success; }
        public String getAnswer() { return answer; }
        public List<RetrievedDocument> getSources() { return sources; }
        public String getError() { return error; }
        public long getTotalTime() { return totalTime; }
        public void setTotalTime(long totalTime) { this.totalTime = totalTime; }
        public long getRetrievalTime() { return retrievalTime; }
        public void setRetrievalTime(long retrievalTime) { this.retrievalTime = retrievalTime; }
        public long getGenerationTime() { return generationTime; }
        public void setGenerationTime(long generationTime) { this.generationTime = generationTime; }
        public Map<String, Object> getMetadata() { return metadata; }
        public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
        public List<String> getCitations() { return citations; }
        public void setCitations(List<String> citations) { this.citations = citations; }
    }

    /**
     * 检索请求
     */
    class RetrievalRequest {
        private String query;
        private Long knowledgeBaseId;
        private RetrievalOptions options;

        public RetrievalRequest(String query, Long knowledgeBaseId, RetrievalOptions options) {
            this.query = query;
            this.knowledgeBaseId = knowledgeBaseId;
            this.options = options;
        }

        // Getters and setters
        public String getQuery() { return query; }
        public Long getKnowledgeBaseId() { return knowledgeBaseId; }
        public RetrievalOptions getOptions() { return options; }
    }

    /**
     * 检索结果
     */
    class RetrievalResult {
        private boolean success;
        private List<RetrievedDocument> documents;
        private String error;
        private long retrievalTime;
        private Map<String, Object> metadata;

        public RetrievalResult(boolean success, List<RetrievedDocument> documents, String error, long retrievalTime) {
            this.success = success;
            this.documents = documents;
            this.error = error;
            this.retrievalTime = retrievalTime;
        }

        // Getters and setters
        public boolean isSuccess() { return success; }
        public List<RetrievedDocument> getDocuments() { return documents; }
        public String getError() { return error; }
        public long getRetrievalTime() { return retrievalTime; }
        public Map<String, Object> getMetadata() { return metadata; }
        public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    }

    /**
     * 检索到的文档
     */
    class RetrievedDocument {
        private Long sliceId;
        private String content;
        private double score;
        private String documentTitle;
        private String documentUrl;
        private Map<String, Object> metadata;
        private String summary;
        private List<String> keywords;

        // Getters and setters
        public Long getSliceId() { return sliceId; }
        public void setSliceId(Long sliceId) { this.sliceId = sliceId; }
        
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        
        public double getScore() { return score; }
        public void setScore(double score) { this.score = score; }
        
        public String getDocumentTitle() { return documentTitle; }
        public void setDocumentTitle(String documentTitle) { this.documentTitle = documentTitle; }
        
        public String getDocumentUrl() { return documentUrl; }
        public void setDocumentUrl(String documentUrl) { this.documentUrl = documentUrl; }
        
        public Map<String, Object> getMetadata() { return metadata; }
        public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
        
        public String getSummary() { return summary; }
        public void setSummary(String summary) { this.summary = summary; }
        
        public List<String> getKeywords() { return keywords; }
        public void setKeywords(List<String> keywords) { this.keywords = keywords; }
    }

    /**
     * 生成请求
     */
    class GenerationRequest {
        private String query;
        private List<RetrievedDocument> context;
        private GenerationOptions options;

        public GenerationRequest(String query, List<RetrievedDocument> context, GenerationOptions options) {
            this.query = query;
            this.context = context;
            this.options = options;
        }

        // Getters and setters
        public String getQuery() { return query; }
        public List<RetrievedDocument> getContext() { return context; }
        public GenerationOptions getOptions() { return options; }
    }

    /**
     * 生成结果
     */
    class GenerationResult {
        private boolean success;
        private String answer;
        private List<String> citations;
        private String error;
        private long generationTime;
        private Map<String, Object> metadata;

        public GenerationResult(boolean success, String answer, String error, long generationTime) {
            this.success = success;
            this.answer = answer;
            this.error = error;
            this.generationTime = generationTime;
        }

        // Getters and setters
        public boolean isSuccess() { return success; }
        public String getAnswer() { return answer; }
        public List<String> getCitations() { return citations; }
        public void setCitations(List<String> citations) { this.citations = citations; }
        public String getError() { return error; }
        public long getGenerationTime() { return generationTime; }
        public Map<String, Object> getMetadata() { return metadata; }
        public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    }

    /**
     * 重排序请求
     */
    class RerankRequest {
        private String query;
        private List<RetrievedDocument> documents;
        private String model = "bge-reranker-large";
        private int topK;

        public RerankRequest(String query, List<RetrievedDocument> documents, int topK) {
            this.query = query;
            this.documents = documents;
            this.topK = topK;
        }

        // Getters and setters
        public String getQuery() { return query; }
        public List<RetrievedDocument> getDocuments() { return documents; }
        public String getModel() { return model; }
        public void setModel(String model) { this.model = model; }
        public int getTopK() { return topK; }
    }

    /**
     * 重排序结果
     */
    class RerankResult {
        private boolean success;
        private List<RetrievedDocument> rerankedDocuments;
        private String error;
        private long rerankTime;

        public RerankResult(boolean success, List<RetrievedDocument> rerankedDocuments, String error, long rerankTime) {
            this.success = success;
            this.rerankedDocuments = rerankedDocuments;
            this.error = error;
            this.rerankTime = rerankTime;
        }

        // Getters and setters
        public boolean isSuccess() { return success; }
        public List<RetrievedDocument> getRerankedDocuments() { return rerankedDocuments; }
        public String getError() { return error; }
        public long getRerankTime() { return rerankTime; }
    }

    /**
     * 查询扩展选项
     */
    class QueryExpansionOptions {
        private String method = "LLM"; // LLM, WORDNET, EMBEDDING
        private int maxExpansions = 3;
        private String model = "gpt-3.5-turbo";
        private Map<String, Object> parameters;

        // Getters and setters
        public String getMethod() { return method; }
        public void setMethod(String method) { this.method = method; }
        
        public int getMaxExpansions() { return maxExpansions; }
        public void setMaxExpansions(int maxExpansions) { this.maxExpansions = maxExpansions; }
        
        public String getModel() { return model; }
        public void setModel(String model) { this.model = model; }
        
        public Map<String, Object> getParameters() { return parameters; }
        public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
    }

    /**
     * 查询扩展结果
     */
    class QueryExpansionResult {
        private boolean success;
        private String originalQuery;
        private List<String> expandedQueries;
        private String error;
        private long expansionTime;

        public QueryExpansionResult(boolean success, String originalQuery, List<String> expandedQueries, String error, long expansionTime) {
            this.success = success;
            this.originalQuery = originalQuery;
            this.expandedQueries = expandedQueries;
            this.error = error;
            this.expansionTime = expansionTime;
        }

        // Getters and setters
        public boolean isSuccess() { return success; }
        public String getOriginalQuery() { return originalQuery; }
        public List<String> getExpandedQueries() { return expandedQueries; }
        public String getError() { return error; }
        public long getExpansionTime() { return expansionTime; }
    }
}
