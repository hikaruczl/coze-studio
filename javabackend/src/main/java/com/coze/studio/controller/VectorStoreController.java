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

package com.coze.studio.controller;

import com.coze.studio.dto.common.ApiResponse;
import com.coze.studio.dto.common.PageResponse;
import com.coze.studio.entity.VectorStore;
import com.coze.studio.service.VectorStoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 向量存储管理API控制器
 * 
 * @author coze-dev
 */
@Slf4j
@RestController
@RequestMapping("/api/vector-stores")
@RequiredArgsConstructor
@Validated
@Tag(name = "向量存储管理", description = "向量存储的配置、管理和操作接口")
public class VectorStoreController {

    private final VectorStoreService vectorStoreService;

    @Operation(summary = "创建向量存储", description = "创建新的向量存储配置")
    @PostMapping
    public ResponseEntity<ApiResponse<String>> createVectorStore(
            @Valid @RequestBody CreateVectorStoreRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("创建向量存储: name={}, type={}, userId={}", 
                request.getName(), request.getStoreType(), userDetails.getUsername());
        
        // TODO: 实现向量存储创建逻辑
        // VectorStore vectorStore = vectorStoreService.createVectorStore(getUserId(userDetails), request);
        
        // TODO: 实现向量存储创建逻辑
        // VectorStore vectorStore = vectorStoreService.createVectorStore(request);
        return ResponseEntity.ok(ApiResponse.success("向量存储创建成功"));
    }

    @Operation(summary = "获取向量存储列表", description = "分页获取向量存储列表")
    @GetMapping
    public ResponseEntity<ApiResponse<String>> getVectorStores(
            @RequestParam(required = false) String storeType,
            @RequestParam(required = false) String status,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("获取向量存储列表: storeType={}, status={}, userId={}", 
                storeType, status, userDetails.getUsername());
        
        // TODO: 实现向量存储列表查询
        // PageResponse<VectorStore> vectorStores = vectorStoreService.getVectorStores(storeType, status, pageable);
        
        // TODO: 实现向量存储列表查询逻辑
        // PageResponse<VectorStore> vectorStores = vectorStoreService.getVectorStores(page, size);
        return ResponseEntity.ok(ApiResponse.success("向量存储列表查询成功"));
    }

    @Operation(summary = "获取向量存储详情", description = "根据ID获取向量存储详细信息")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> getVectorStore(
            @Parameter(description = "向量存储ID") @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("获取向量存储详情: id={}, userId={}", id, userDetails.getUsername());
        
        // TODO: 实现向量存储详情查询
        // VectorStore vectorStore = vectorStoreService.getVectorStoreById(id);
        
        return ResponseEntity.ok(ApiResponse.success("向量存储详情查询成功"));
    }

    @Operation(summary = "更新向量存储", description = "更新向量存储配置")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> updateVectorStore(
            @Parameter(description = "向量存储ID") @PathVariable Long id,
            @Valid @RequestBody UpdateVectorStoreRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("更新向量存储: id={}, userId={}", id, userDetails.getUsername());
        
        // TODO: 实现向量存储更新逻辑
        // VectorStore vectorStore = vectorStoreService.updateVectorStore(id, request);
        
        return ResponseEntity.ok(ApiResponse.success("向量存储更新成功"));
    }

    @Operation(summary = "删除向量存储", description = "删除指定的向量存储")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteVectorStore(
            @Parameter(description = "向量存储ID") @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("删除向量存储: id={}, userId={}", id, userDetails.getUsername());
        
        // TODO: 实现向量存储删除逻辑
        // vectorStoreService.deleteVectorStore(id);
        
        return ResponseEntity.ok(ApiResponse.success("向量存储删除成功"));
    }

    @Operation(summary = "存储向量", description = "向指定存储中添加向量")
    @PostMapping("/{id}/vectors")
    public ResponseEntity<ApiResponse<VectorStoreService.VectorStoreResult>> storeVector(
            @Parameter(description = "向量存储ID") @PathVariable Long id,
            @Valid @RequestBody StoreVectorRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("存储向量: storeId={}, vectorId={}, userId={}", 
                id, request.getVectorId(), userDetails.getUsername());
        
        VectorStoreService.VectorStoreResult result = vectorStoreService.storeVector(
                request.getVectorId(), request.getVector(), request.getMetadata(), id);
        
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation(summary = "批量存储向量", description = "批量向指定存储中添加向量")
    @PostMapping("/{id}/vectors/batch")
    public ResponseEntity<ApiResponse<VectorStoreService.BatchVectorStoreResult>> storeVectorsBatch(
            @Parameter(description = "向量存储ID") @PathVariable Long id,
            @Valid @RequestBody BatchStoreVectorRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("批量存储向量: storeId={}, count={}, userId={}", 
                id, request.getVectors().size(), userDetails.getUsername());
        
        VectorStoreService.BatchVectorStoreResult result = vectorStoreService.storeVectorsBatch(
                request.getVectors(), id);
        
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation(summary = "相似度搜索", description = "在向量存储中进行相似度搜索")
    @PostMapping("/{id}/search")
    public ResponseEntity<ApiResponse<VectorStoreService.VectorSearchResult>> similaritySearch(
            @Parameter(description = "向量存储ID") @PathVariable Long id,
            @Valid @RequestBody VectorSearchRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("相似度搜索: storeId={}, topK={}, userId={}", 
                id, request.getTopK(), userDetails.getUsername());
        
        VectorStoreService.VectorSearchOptions options = new VectorStoreService.VectorSearchOptions();
        options.setFilter(request.getFilter());
        options.setIncludeVector(request.isIncludeVector());
        options.setIncludeMetadata(request.isIncludeMetadata());
        options.setMetricType(request.getMetricType());
        options.setScoreThreshold(request.getScoreThreshold());
        options.setSelectFields(request.getSelectFields());
        
        VectorStoreService.VectorSearchResult result = vectorStoreService.similaritySearch(
                request.getQueryVector(), request.getTopK(), id, options);
        
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation(summary = "混合搜索", description = "在向量存储中进行混合搜索")
    @PostMapping("/{id}/hybrid-search")
    public ResponseEntity<ApiResponse<VectorStoreService.VectorSearchResult>> hybridSearch(
            @Parameter(description = "向量存储ID") @PathVariable Long id,
            @Valid @RequestBody HybridSearchRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("混合搜索: storeId={}, queryText={}, topK={}, userId={}", 
                id, request.getQueryText(), request.getTopK(), userDetails.getUsername());
        
        VectorStoreService.HybridSearchOptions options = new VectorStoreService.HybridSearchOptions();
        options.setFilter(request.getFilter());
        options.setIncludeVector(request.isIncludeVector());
        options.setIncludeMetadata(request.isIncludeMetadata());
        options.setMetricType(request.getMetricType());
        options.setScoreThreshold(request.getScoreThreshold());
        options.setVectorWeight(request.getVectorWeight());
        options.setTextWeight(request.getTextWeight());
        options.setTextSearchType(request.getTextSearchType());
        options.setTextSearchParams(request.getTextSearchParams());
        
        VectorStoreService.VectorSearchResult result = vectorStoreService.hybridSearch(
                request.getQueryText(), request.getQueryVector(), request.getTopK(), id, options);
        
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation(summary = "删除向量", description = "从向量存储中删除指定向量")
    @DeleteMapping("/{id}/vectors/{vectorId}")
    public ResponseEntity<ApiResponse<Boolean>> deleteVector(
            @Parameter(description = "向量存储ID") @PathVariable Long id,
            @Parameter(description = "向量ID") @PathVariable String vectorId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("删除向量: storeId={}, vectorId={}, userId={}", id, vectorId, userDetails.getUsername());
        
        boolean result = vectorStoreService.deleteVector(vectorId, id);
        
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation(summary = "批量删除向量", description = "从向量存储中批量删除向量")
    @DeleteMapping("/{id}/vectors/batch")
    public ResponseEntity<ApiResponse<VectorStoreService.BatchDeleteResult>> deleteVectorsBatch(
            @Parameter(description = "向量存储ID") @PathVariable Long id,
            @Valid @RequestBody BatchDeleteVectorRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("批量删除向量: storeId={}, count={}, userId={}", 
                id, request.getVectorIds().size(), userDetails.getUsername());
        
        VectorStoreService.BatchDeleteResult result = vectorStoreService.deleteVectorsBatch(
                request.getVectorIds(), id);
        
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation(summary = "获取向量", description = "从向量存储中获取指定向量")
    @GetMapping("/{id}/vectors/{vectorId}")
    public ResponseEntity<ApiResponse<VectorStoreService.VectorData>> getVector(
            @Parameter(description = "向量存储ID") @PathVariable Long id,
            @Parameter(description = "向量ID") @PathVariable String vectorId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("获取向量: storeId={}, vectorId={}, userId={}", id, vectorId, userDetails.getUsername());
        
        VectorStoreService.VectorData vectorData = vectorStoreService.getVector(vectorId, id);
        
        return ResponseEntity.ok(ApiResponse.success(vectorData));
    }

    @Operation(summary = "检查向量存在", description = "检查向量是否存在于存储中")
    @GetMapping("/{id}/vectors/{vectorId}/exists")
    public ResponseEntity<ApiResponse<Boolean>> vectorExists(
            @Parameter(description = "向量存储ID") @PathVariable Long id,
            @Parameter(description = "向量ID") @PathVariable String vectorId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("检查向量存在: storeId={}, vectorId={}, userId={}", id, vectorId, userDetails.getUsername());
        
        boolean exists = vectorStoreService.vectorExists(vectorId, id);
        
        return ResponseEntity.ok(ApiResponse.success(exists));
    }

    @Operation(summary = "获取存储统计", description = "获取向量存储的统计信息")
    @GetMapping("/{id}/stats")
    public ResponseEntity<ApiResponse<VectorStoreService.VectorStoreStats>> getVectorStoreStats(
            @Parameter(description = "向量存储ID") @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("获取存储统计: storeId={}, userId={}", id, userDetails.getUsername());
        
        VectorStoreService.VectorStoreStats stats = vectorStoreService.getVectorStoreStats(id);
        
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @Operation(summary = "健康检查", description = "检查向量存储的健康状态")
    @GetMapping("/{id}/health")
    public ResponseEntity<ApiResponse<VectorStoreService.HealthCheckResult>> healthCheck(
            @Parameter(description = "向量存储ID") @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("健康检查: storeId={}, userId={}", id, userDetails.getUsername());
        
        VectorStoreService.HealthCheckResult result = vectorStoreService.healthCheck(id);
        
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation(summary = "创建索引", description = "为向量存储创建索引")
    @PostMapping("/{id}/indexes")
    public ResponseEntity<ApiResponse<Boolean>> createIndex(
            @Parameter(description = "向量存储ID") @PathVariable Long id,
            @Valid @RequestBody CreateIndexRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("创建索引: storeId={}, indexName={}, userId={}", 
                id, request.getIndexName(), userDetails.getUsername());
        
        VectorStoreService.IndexConfig indexConfig = new VectorStoreService.IndexConfig();
        indexConfig.setIndexName(request.getIndexName());
        indexConfig.setIndexType(request.getIndexType());
        indexConfig.setParameters(request.getParameters());
        indexConfig.setMetricType(request.getMetricType());
        
        boolean result = vectorStoreService.createIndex(id, indexConfig);
        
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation(summary = "删除索引", description = "删除向量存储的索引")
    @DeleteMapping("/{id}/indexes/{indexName}")
    public ResponseEntity<ApiResponse<Boolean>> deleteIndex(
            @Parameter(description = "向量存储ID") @PathVariable Long id,
            @Parameter(description = "索引名称") @PathVariable String indexName,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("删除索引: storeId={}, indexName={}, userId={}", id, indexName, userDetails.getUsername());
        
        boolean result = vectorStoreService.deleteIndex(id, indexName);
        
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    /**
     * 从UserDetails中提取用户ID
     */
    private Long getUserId(UserDetails userDetails) {
        try {
            return Long.parseLong(userDetails.getUsername());
        } catch (NumberFormatException e) {
            return 1L; // 临时返回默认值
        }
    }

    // 请求DTO类定义
    public static class CreateVectorStoreRequest {
        @NotBlank(message = "存储名称不能为空")
        private String name;
        @NotBlank(message = "存储类型不能为空")
        private String storeType;
        @NotNull(message = "连接配置不能为空")
        private Map<String, Object> connectionConfig;
        private Map<String, Object> indexConfig;
        private Map<String, Object> embeddingConfig;
        private Integer defaultDimension;
        private String defaultMetric;
        private String description;
        private Long maxVectors;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getStoreType() { return storeType; }
        public void setStoreType(String storeType) { this.storeType = storeType; }
        public Map<String, Object> getConnectionConfig() { return connectionConfig; }
        public void setConnectionConfig(Map<String, Object> connectionConfig) { this.connectionConfig = connectionConfig; }
        public Map<String, Object> getIndexConfig() { return indexConfig; }
        public void setIndexConfig(Map<String, Object> indexConfig) { this.indexConfig = indexConfig; }
        public Map<String, Object> getEmbeddingConfig() { return embeddingConfig; }
        public void setEmbeddingConfig(Map<String, Object> embeddingConfig) { this.embeddingConfig = embeddingConfig; }
        public Integer getDefaultDimension() { return defaultDimension; }
        public void setDefaultDimension(Integer defaultDimension) { this.defaultDimension = defaultDimension; }
        public String getDefaultMetric() { return defaultMetric; }
        public void setDefaultMetric(String defaultMetric) { this.defaultMetric = defaultMetric; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Long getMaxVectors() { return maxVectors; }
        public void setMaxVectors(Long maxVectors) { this.maxVectors = maxVectors; }
    }

    public static class UpdateVectorStoreRequest {
        private String name;
        private Map<String, Object> connectionConfig;
        private Map<String, Object> indexConfig;
        private Map<String, Object> embeddingConfig;
        private String description;
        private Long maxVectors;

        // Getters and setters (省略具体实现)
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Map<String, Object> getConnectionConfig() { return connectionConfig; }
        public void setConnectionConfig(Map<String, Object> connectionConfig) { this.connectionConfig = connectionConfig; }
        public Map<String, Object> getIndexConfig() { return indexConfig; }
        public void setIndexConfig(Map<String, Object> indexConfig) { this.indexConfig = indexConfig; }
        public Map<String, Object> getEmbeddingConfig() { return embeddingConfig; }
        public void setEmbeddingConfig(Map<String, Object> embeddingConfig) { this.embeddingConfig = embeddingConfig; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Long getMaxVectors() { return maxVectors; }
        public void setMaxVectors(Long maxVectors) { this.maxVectors = maxVectors; }
    }

    public static class StoreVectorRequest {
        @NotBlank(message = "向量ID不能为空")
        private String vectorId;
        @NotNull(message = "向量数据不能为空")
        private float[] vector;
        private Map<String, Object> metadata;

        // Getters and setters
        public String getVectorId() { return vectorId; }
        public void setVectorId(String vectorId) { this.vectorId = vectorId; }
        public float[] getVector() { return vector; }
        public void setVector(float[] vector) { this.vector = vector; }
        public Map<String, Object> getMetadata() { return metadata; }
        public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    }

    public static class BatchStoreVectorRequest {
        @NotNull(message = "向量列表不能为空")
        private List<VectorStoreService.VectorData> vectors;

        // Getters and setters
        public List<VectorStoreService.VectorData> getVectors() { return vectors; }
        public void setVectors(List<VectorStoreService.VectorData> vectors) { this.vectors = vectors; }
    }

    public static class VectorSearchRequest {
        @NotNull(message = "查询向量不能为空")
        private float[] queryVector;
        @NotNull(message = "返回数量不能为空")
        private Integer topK;
        private Map<String, Object> filter;
        private boolean includeVector = false;
        private boolean includeMetadata = true;
        private String metricType = "COSINE";
        private Double scoreThreshold;
        private List<String> selectFields;

        // Getters and setters
        public float[] getQueryVector() { return queryVector; }
        public void setQueryVector(float[] queryVector) { this.queryVector = queryVector; }
        public Integer getTopK() { return topK; }
        public void setTopK(Integer topK) { this.topK = topK; }
        public Map<String, Object> getFilter() { return filter; }
        public void setFilter(Map<String, Object> filter) { this.filter = filter; }
        public boolean isIncludeVector() { return includeVector; }
        public void setIncludeVector(boolean includeVector) { this.includeVector = includeVector; }
        public boolean isIncludeMetadata() { return includeMetadata; }
        public void setIncludeMetadata(boolean includeMetadata) { this.includeMetadata = includeMetadata; }
        public String getMetricType() { return metricType; }
        public void setMetricType(String metricType) { this.metricType = metricType; }
        public Double getScoreThreshold() { return scoreThreshold; }
        public void setScoreThreshold(Double scoreThreshold) { this.scoreThreshold = scoreThreshold; }
        public List<String> getSelectFields() { return selectFields; }
        public void setSelectFields(List<String> selectFields) { this.selectFields = selectFields; }
    }

    public static class HybridSearchRequest extends VectorSearchRequest {
        @NotBlank(message = "查询文本不能为空")
        private String queryText;
        private double vectorWeight = 0.7;
        private double textWeight = 0.3;
        private String textSearchType = "BM25";
        private Map<String, Object> textSearchParams;

        // Getters and setters
        public String getQueryText() { return queryText; }
        public void setQueryText(String queryText) { this.queryText = queryText; }
        public double getVectorWeight() { return vectorWeight; }
        public void setVectorWeight(double vectorWeight) { this.vectorWeight = vectorWeight; }
        public double getTextWeight() { return textWeight; }
        public void setTextWeight(double textWeight) { this.textWeight = textWeight; }
        public String getTextSearchType() { return textSearchType; }
        public void setTextSearchType(String textSearchType) { this.textSearchType = textSearchType; }
        public Map<String, Object> getTextSearchParams() { return textSearchParams; }
        public void setTextSearchParams(Map<String, Object> textSearchParams) { this.textSearchParams = textSearchParams; }
    }

    public static class BatchDeleteVectorRequest {
        @NotNull(message = "向量ID列表不能为空")
        private List<String> vectorIds;

        // Getters and setters
        public List<String> getVectorIds() { return vectorIds; }
        public void setVectorIds(List<String> vectorIds) { this.vectorIds = vectorIds; }
    }

    public static class CreateIndexRequest {
        @NotBlank(message = "索引名称不能为空")
        private String indexName;
        private String indexType = "HNSW";
        private Map<String, Object> parameters;
        private String metricType = "COSINE";

        // Getters and setters
        public String getIndexName() { return indexName; }
        public void setIndexName(String indexName) { this.indexName = indexName; }
        public String getIndexType() { return indexType; }
        public void setIndexType(String indexType) { this.indexType = indexType; }
        public Map<String, Object> getParameters() { return parameters; }
        public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
        public String getMetricType() { return metricType; }
        public void setMetricType(String metricType) { this.metricType = metricType; }
    }
}
