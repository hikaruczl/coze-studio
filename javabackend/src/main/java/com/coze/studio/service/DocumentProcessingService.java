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
import java.util.concurrent.CompletableFuture;

/**
 * 文档处理服务接口
 * 
 * @author coze-dev
 */
public interface DocumentProcessingService {

    /**
     * 处理文档
     */
    DocumentProcessingResult processDocument(Long documentId, DocumentProcessingOptions options);

    /**
     * 异步处理文档
     */
    CompletableFuture<DocumentProcessingResult> processDocumentAsync(Long documentId, DocumentProcessingOptions options);

    /**
     * 批量处理文档
     */
    Map<Long, DocumentProcessingResult> processDocumentsBatch(List<Long> documentIds, DocumentProcessingOptions options);

    /**
     * 解析文档内容
     */
    DocumentParseResult parseDocument(Long documentId);

    /**
     * 切分文档为切片
     */
    DocumentSplitResult splitDocument(Long documentId, SplitOptions options);

    /**
     * 向量化文档切片
     */
    VectorizationResult vectorizeDocument(Long documentId, VectorizationOptions options);

    /**
     * 提取文档元数据
     */
    MetadataExtractionResult extractMetadata(Long documentId);

    /**
     * 获取处理进度
     */
    ProcessingProgress getProcessingProgress(Long documentId);

    /**
     * 取消文档处理
     */
    void cancelDocumentProcessing(Long documentId);

    /**
     * 重新处理文档
     */
    DocumentProcessingResult reprocessDocument(Long documentId, DocumentProcessingOptions options);

    /**
     * 文档处理选项
     */
    class DocumentProcessingOptions {
        private boolean enableParsing = true;
        private boolean enableSplitting = true;
        private boolean enableVectorization = true;
        private boolean enableMetadataExtraction = true;
        private SplitOptions splitOptions;
        private VectorizationOptions vectorizationOptions;
        private Map<String, Object> customOptions;

        // Getters and setters
        public boolean isEnableParsing() { return enableParsing; }
        public void setEnableParsing(boolean enableParsing) { this.enableParsing = enableParsing; }
        
        public boolean isEnableSplitting() { return enableSplitting; }
        public void setEnableSplitting(boolean enableSplitting) { this.enableSplitting = enableSplitting; }
        
        public boolean isEnableVectorization() { return enableVectorization; }
        public void setEnableVectorization(boolean enableVectorization) { this.enableVectorization = enableVectorization; }
        
        public boolean isEnableMetadataExtraction() { return enableMetadataExtraction; }
        public void setEnableMetadataExtraction(boolean enableMetadataExtraction) { this.enableMetadataExtraction = enableMetadataExtraction; }
        
        public SplitOptions getSplitOptions() { return splitOptions; }
        public void setSplitOptions(SplitOptions splitOptions) { this.splitOptions = splitOptions; }
        
        public VectorizationOptions getVectorizationOptions() { return vectorizationOptions; }
        public void setVectorizationOptions(VectorizationOptions vectorizationOptions) { this.vectorizationOptions = vectorizationOptions; }
        
        public Map<String, Object> getCustomOptions() { return customOptions; }
        public void setCustomOptions(Map<String, Object> customOptions) { this.customOptions = customOptions; }
    }

    /**
     * 切分选项
     */
    class SplitOptions {
        private String strategy = "RECURSIVE"; // RECURSIVE, SEMANTIC, FIXED_SIZE
        private int chunkSize = 1000;
        private int chunkOverlap = 200;
        private String separator = "\n\n";
        private boolean preserveStructure = true;
        private int minChunkSize = 100;
        private int maxChunkSize = 2000;

        // Getters and setters
        public String getStrategy() { return strategy; }
        public void setStrategy(String strategy) { this.strategy = strategy; }
        
        public int getChunkSize() { return chunkSize; }
        public void setChunkSize(int chunkSize) { this.chunkSize = chunkSize; }
        
        public int getChunkOverlap() { return chunkOverlap; }
        public void setChunkOverlap(int chunkOverlap) { this.chunkOverlap = chunkOverlap; }
        
        public String getSeparator() { return separator; }
        public void setSeparator(String separator) { this.separator = separator; }
        
        public boolean isPreserveStructure() { return preserveStructure; }
        public void setPreserveStructure(boolean preserveStructure) { this.preserveStructure = preserveStructure; }
        
        public int getMinChunkSize() { return minChunkSize; }
        public void setMinChunkSize(int minChunkSize) { this.minChunkSize = minChunkSize; }
        
        public int getMaxChunkSize() { return maxChunkSize; }
        public void setMaxChunkSize(int maxChunkSize) { this.maxChunkSize = maxChunkSize; }
    }

    /**
     * 向量化选项
     */
    class VectorizationOptions {
        private String embeddingModel = "text-embedding-ada-002";
        private int embeddingDimension = 1536;
        private String vectorStoreType = "DEFAULT";
        private Long vectorStoreId;
        private boolean generateSummary = true;
        private boolean extractKeywords = true;
        private Map<String, Object> modelParams;

        // Getters and setters
        public String getEmbeddingModel() { return embeddingModel; }
        public void setEmbeddingModel(String embeddingModel) { this.embeddingModel = embeddingModel; }
        
        public int getEmbeddingDimension() { return embeddingDimension; }
        public void setEmbeddingDimension(int embeddingDimension) { this.embeddingDimension = embeddingDimension; }
        
        public String getVectorStoreType() { return vectorStoreType; }
        public void setVectorStoreType(String vectorStoreType) { this.vectorStoreType = vectorStoreType; }
        
        public Long getVectorStoreId() { return vectorStoreId; }
        public void setVectorStoreId(Long vectorStoreId) { this.vectorStoreId = vectorStoreId; }
        
        public boolean isGenerateSummary() { return generateSummary; }
        public void setGenerateSummary(boolean generateSummary) { this.generateSummary = generateSummary; }
        
        public boolean isExtractKeywords() { return extractKeywords; }
        public void setExtractKeywords(boolean extractKeywords) { this.extractKeywords = extractKeywords; }
        
        public Map<String, Object> getModelParams() { return modelParams; }
        public void setModelParams(Map<String, Object> modelParams) { this.modelParams = modelParams; }
    }

    /**
     * 文档处理结果
     */
    class DocumentProcessingResult {
        private boolean success;
        private String status;
        private Long documentId;
        private int totalSlices;
        private int processedSlices;
        private int vectorizedSlices;
        private long processingTime;
        private String error;
        private Map<String, Object> metadata;
        private List<String> warnings;

        public DocumentProcessingResult(boolean success, String status, Long documentId) {
            this.success = success;
            this.status = status;
            this.documentId = documentId;
        }

        // Getters and setters
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public Long getDocumentId() { return documentId; }
        public void setDocumentId(Long documentId) { this.documentId = documentId; }
        
        public int getTotalSlices() { return totalSlices; }
        public void setTotalSlices(int totalSlices) { this.totalSlices = totalSlices; }
        
        public int getProcessedSlices() { return processedSlices; }
        public void setProcessedSlices(int processedSlices) { this.processedSlices = processedSlices; }
        
        public int getVectorizedSlices() { return vectorizedSlices; }
        public void setVectorizedSlices(int vectorizedSlices) { this.vectorizedSlices = vectorizedSlices; }
        
        public long getProcessingTime() { return processingTime; }
        public void setProcessingTime(long processingTime) { this.processingTime = processingTime; }
        
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
        
        public Map<String, Object> getMetadata() { return metadata; }
        public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
        
        public List<String> getWarnings() { return warnings; }
        public void setWarnings(List<String> warnings) { this.warnings = warnings; }
    }

    /**
     * 文档解析结果
     */
    class DocumentParseResult {
        private boolean success;
        private String content;
        private String contentType;
        private Map<String, Object> metadata;
        private String error;
        private long parseTime;

        public DocumentParseResult(boolean success, String content, String error, long parseTime) {
            this.success = success;
            this.content = content;
            this.error = error;
            this.parseTime = parseTime;
        }

        // Getters and setters
        public boolean isSuccess() { return success; }
        public String getContent() { return content; }
        public String getContentType() { return contentType; }
        public void setContentType(String contentType) { this.contentType = contentType; }
        public Map<String, Object> getMetadata() { return metadata; }
        public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
        public String getError() { return error; }
        public long getParseTime() { return parseTime; }
    }

    /**
     * 文档切分结果
     */
    class DocumentSplitResult {
        private boolean success;
        private int sliceCount;
        private List<DocumentSliceInfo> slices;
        private String error;
        private long splitTime;

        public DocumentSplitResult(boolean success, int sliceCount, String error, long splitTime) {
            this.success = success;
            this.sliceCount = sliceCount;
            this.error = error;
            this.splitTime = splitTime;
        }

        // Getters and setters
        public boolean isSuccess() { return success; }
        public int getSliceCount() { return sliceCount; }
        public List<DocumentSliceInfo> getSlices() { return slices; }
        public void setSlices(List<DocumentSliceInfo> slices) { this.slices = slices; }
        public String getError() { return error; }
        public long getSplitTime() { return splitTime; }
    }

    /**
     * 向量化结果
     */
    class VectorizationResult {
        private boolean success;
        private int vectorizedCount;
        private int failedCount;
        private String error;
        private long vectorizationTime;
        private Map<String, Object> statistics;

        public VectorizationResult(boolean success, int vectorizedCount, int failedCount, String error, long vectorizationTime) {
            this.success = success;
            this.vectorizedCount = vectorizedCount;
            this.failedCount = failedCount;
            this.error = error;
            this.vectorizationTime = vectorizationTime;
        }

        // Getters and setters
        public boolean isSuccess() { return success; }
        public int getVectorizedCount() { return vectorizedCount; }
        public int getFailedCount() { return failedCount; }
        public String getError() { return error; }
        public long getVectorizationTime() { return vectorizationTime; }
        public Map<String, Object> getStatistics() { return statistics; }
        public void setStatistics(Map<String, Object> statistics) { this.statistics = statistics; }
    }

    /**
     * 元数据提取结果
     */
    class MetadataExtractionResult {
        private boolean success;
        private Map<String, Object> metadata;
        private String error;
        private long extractionTime;

        public MetadataExtractionResult(boolean success, Map<String, Object> metadata, String error, long extractionTime) {
            this.success = success;
            this.metadata = metadata;
            this.error = error;
            this.extractionTime = extractionTime;
        }

        // Getters and setters
        public boolean isSuccess() { return success; }
        public Map<String, Object> getMetadata() { return metadata; }
        public String getError() { return error; }
        public long getExtractionTime() { return extractionTime; }
    }

    /**
     * 处理进度
     */
    class ProcessingProgress {
        private String status;
        private int progress; // 0-100
        private String currentStep;
        private String message;
        private long estimatedTimeRemaining;

        // Getters and setters
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public int getProgress() { return progress; }
        public void setProgress(int progress) { this.progress = progress; }
        
        public String getCurrentStep() { return currentStep; }
        public void setCurrentStep(String currentStep) { this.currentStep = currentStep; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        
        public long getEstimatedTimeRemaining() { return estimatedTimeRemaining; }
        public void setEstimatedTimeRemaining(long estimatedTimeRemaining) { this.estimatedTimeRemaining = estimatedTimeRemaining; }
    }

    /**
     * 文档切片信息
     */
    class DocumentSliceInfo {
        private Long sliceId;
        private String content;
        private int position;
        private int charCount;
        private String summary;
        private List<String> keywords;

        // Getters and setters
        public Long getSliceId() { return sliceId; }
        public void setSliceId(Long sliceId) { this.sliceId = sliceId; }
        
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        
        public int getPosition() { return position; }
        public void setPosition(int position) { this.position = position; }
        
        public int getCharCount() { return charCount; }
        public void setCharCount(int charCount) { this.charCount = charCount; }
        
        public String getSummary() { return summary; }
        public void setSummary(String summary) { this.summary = summary; }
        
        public List<String> getKeywords() { return keywords; }
        public void setKeywords(List<String> keywords) { this.keywords = keywords; }
    }
}
