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
import com.coze.studio.entity.Document;
import com.coze.studio.service.DocumentProcessingService;
import com.coze.studio.service.DocumentService;
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
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 文档管理API控制器
 * 
 * @author coze-dev
 */
@Slf4j
@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@Validated
@Tag(name = "文档管理", description = "文档的上传、管理和处理接口")
public class DocumentController {

    private final DocumentService documentService;
    private final DocumentProcessingService documentProcessingService;

    @Operation(summary = "上传文档", description = "上传文档到指定知识库")
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<Document>> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("knowledgeBaseId") Long knowledgeBaseId,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "autoProcess", defaultValue = "true") boolean autoProcess,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("上传文档: filename={}, knowledgeBaseId={}, userId={}", 
                file.getOriginalFilename(), knowledgeBaseId, userDetails.getUsername());
        
        Document document = documentService.uploadDocument(
                file, knowledgeBaseId, getUserId(userDetails), title, description);
        
        // 自动处理文档
        if (autoProcess) {
            DocumentProcessingService.DocumentProcessingOptions options = 
                    new DocumentProcessingService.DocumentProcessingOptions();
            documentProcessingService.processDocumentAsync(document.getId(), options);
        }
        
        return ResponseEntity.ok(ApiResponse.success(document));
    }

    @Operation(summary = "批量上传文档", description = "批量上传多个文档")
    @PostMapping("/batch-upload")
    public ResponseEntity<ApiResponse<List<Document>>> batchUploadDocuments(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("knowledgeBaseId") Long knowledgeBaseId,
            @RequestParam(value = "autoProcess", defaultValue = "true") boolean autoProcess,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("批量上传文档: count={}, knowledgeBaseId={}, userId={}", 
                files.length, knowledgeBaseId, userDetails.getUsername());
        
        List<Document> documents = documentService.batchUploadDocuments(
                files, knowledgeBaseId, getUserId(userDetails));
        
        // 自动处理文档
        if (autoProcess) {
            DocumentProcessingService.DocumentProcessingOptions options = 
                    new DocumentProcessingService.DocumentProcessingOptions();
            List<Long> documentIds = documents.stream().map(Document::getId).toList();
            documentProcessingService.processDocumentsBatch(documentIds, options);
        }
        
        return ResponseEntity.ok(ApiResponse.success(documents));
    }

    @Operation(summary = "获取文档列表", description = "分页获取知识库中的文档列表")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<Document>>> getDocuments(
            @RequestParam(required = false) Long knowledgeBaseId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String fileType,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("获取文档列表: knowledgeBaseId={}, keyword={}, status={}, userId={}", 
                knowledgeBaseId, keyword, status, userDetails.getUsername());
        
        PageResponse<Document> documents = documentService.getDocuments(
                knowledgeBaseId, getUserId(userDetails), keyword, status, fileType, pageable);
        
        return ResponseEntity.ok(ApiResponse.success(documents));
    }

    @Operation(summary = "获取文档详情", description = "根据ID获取文档详细信息")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Document>> getDocument(
            @Parameter(description = "文档ID") @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("获取文档详情: id={}, userId={}", id, userDetails.getUsername());
        
        Document document = documentService.getDocumentById(id, getUserId(userDetails));
        
        return ResponseEntity.ok(ApiResponse.success(document));
    }

    @Operation(summary = "更新文档", description = "更新文档信息")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Document>> updateDocument(
            @Parameter(description = "文档ID") @PathVariable Long id,
            @Valid @RequestBody com.coze.studio.dto.document.UpdateDocumentRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("更新文档: id={}, userId={}", id, userDetails.getUsername());
        
        Document document = documentService.updateDocument(
                id, getUserId(userDetails), request);
        
        return ResponseEntity.ok(ApiResponse.success(document));
    }

    @Operation(summary = "删除文档", description = "删除指定的文档")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteDocument(
            @Parameter(description = "文档ID") @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("删除文档: id={}, userId={}", id, userDetails.getUsername());
        
        documentService.deleteDocument(id, getUserId(userDetails));
        
        return ResponseEntity.ok(ApiResponse.success("文档删除成功"));
    }

    @Operation(summary = "处理文档", description = "手动触发文档处理")
    @PostMapping("/{id}/process")
    public ResponseEntity<ApiResponse<DocumentProcessingService.DocumentProcessingResult>> processDocument(
            @Parameter(description = "文档ID") @PathVariable Long id,
            @Valid @RequestBody ProcessDocumentRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("处理文档: id={}, userId={}", id, userDetails.getUsername());
        
        DocumentProcessingService.DocumentProcessingOptions options = 
                new DocumentProcessingService.DocumentProcessingOptions();
        options.setEnableParsing(request.isEnableParsing());
        options.setEnableSplitting(request.isEnableSplitting());
        options.setEnableVectorization(request.isEnableVectorization());
        options.setEnableMetadataExtraction(request.isEnableMetadataExtraction());
        options.setSplitOptions(request.getSplitOptions());
        options.setVectorizationOptions(request.getVectorizationOptions());
        
        DocumentProcessingService.DocumentProcessingResult result = 
                documentProcessingService.processDocument(id, options);
        
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation(summary = "异步处理文档", description = "异步处理文档")
    @PostMapping("/{id}/process-async")
    public ResponseEntity<ApiResponse<String>> processDocumentAsync(
            @Parameter(description = "文档ID") @PathVariable Long id,
            @Valid @RequestBody ProcessDocumentRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("异步处理文档: id={}, userId={}", id, userDetails.getUsername());
        
        DocumentProcessingService.DocumentProcessingOptions options = 
                new DocumentProcessingService.DocumentProcessingOptions();
        options.setEnableParsing(request.isEnableParsing());
        options.setEnableSplitting(request.isEnableSplitting());
        options.setEnableVectorization(request.isEnableVectorization());
        options.setEnableMetadataExtraction(request.isEnableMetadataExtraction());
        options.setSplitOptions(request.getSplitOptions());
        options.setVectorizationOptions(request.getVectorizationOptions());
        
        CompletableFuture<DocumentProcessingService.DocumentProcessingResult> future = 
                documentProcessingService.processDocumentAsync(id, options);
        
        return ResponseEntity.ok(ApiResponse.success("文档处理已开始，请通过进度接口查询处理状态"));
    }

    @Operation(summary = "获取处理进度", description = "获取文档处理进度")
    @GetMapping("/{id}/progress")
    public ResponseEntity<ApiResponse<DocumentProcessingService.ProcessingProgress>> getProcessingProgress(
            @Parameter(description = "文档ID") @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("获取处理进度: id={}, userId={}", id, userDetails.getUsername());
        
        DocumentProcessingService.ProcessingProgress progress = 
                documentProcessingService.getProcessingProgress(id);
        
        return ResponseEntity.ok(ApiResponse.success(progress));
    }

    @Operation(summary = "取消文档处理", description = "取消正在进行的文档处理")
    @PostMapping("/{id}/cancel-processing")
    public ResponseEntity<ApiResponse<String>> cancelDocumentProcessing(
            @Parameter(description = "文档ID") @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("取消文档处理: id={}, userId={}", id, userDetails.getUsername());
        
        documentProcessingService.cancelDocumentProcessing(id);
        
        return ResponseEntity.ok(ApiResponse.success("文档处理已取消"));
    }

    @Operation(summary = "重新处理文档", description = "重新处理文档")
    @PostMapping("/{id}/reprocess")
    public ResponseEntity<ApiResponse<DocumentProcessingService.DocumentProcessingResult>> reprocessDocument(
            @Parameter(description = "文档ID") @PathVariable Long id,
            @Valid @RequestBody ProcessDocumentRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("重新处理文档: id={}, userId={}", id, userDetails.getUsername());
        
        DocumentProcessingService.DocumentProcessingOptions options = 
                new DocumentProcessingService.DocumentProcessingOptions();
        options.setEnableParsing(request.isEnableParsing());
        options.setEnableSplitting(request.isEnableSplitting());
        options.setEnableVectorization(request.isEnableVectorization());
        options.setEnableMetadataExtraction(request.isEnableMetadataExtraction());
        options.setSplitOptions(request.getSplitOptions());
        options.setVectorizationOptions(request.getVectorizationOptions());
        
        DocumentProcessingService.DocumentProcessingResult result = 
                documentProcessingService.reprocessDocument(id, options);
        
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation(summary = "获取文档切片", description = "获取文档的切片列表")
    @GetMapping("/{id}/slices")
    public ResponseEntity<ApiResponse<PageResponse<Map<String, Object>>>> getDocumentSlices(
            @Parameter(description = "文档ID") @PathVariable Long id,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("获取文档切片: id={}, userId={}", id, userDetails.getUsername());
        
        PageResponse<Map<String, Object>> slices = documentService.getDocumentSlices(
                id, getUserId(userDetails), pageable);
        
        return ResponseEntity.ok(ApiResponse.success(slices));
    }

    @Operation(summary = "获取文档统计", description = "获取文档的统计信息")
    @GetMapping("/{id}/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDocumentStats(
            @Parameter(description = "文档ID") @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("获取文档统计: id={}, userId={}", id, userDetails.getUsername());
        
        Map<String, Object> stats = documentService.getDocumentStats(
                id, getUserId(userDetails));
        
        return ResponseEntity.ok(ApiResponse.success(stats));
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

    // UpdateDocumentRequest 已移至 com.coze.studio.dto.document.UpdateDocumentRequest

    /**
     * 处理文档请求
     */
    public static class ProcessDocumentRequest {
        private boolean enableParsing = true;
        private boolean enableSplitting = true;
        private boolean enableVectorization = true;
        private boolean enableMetadataExtraction = true;
        private DocumentProcessingService.SplitOptions splitOptions;
        private DocumentProcessingService.VectorizationOptions vectorizationOptions;

        // Getters and setters
        public boolean isEnableParsing() { return enableParsing; }
        public void setEnableParsing(boolean enableParsing) { this.enableParsing = enableParsing; }
        
        public boolean isEnableSplitting() { return enableSplitting; }
        public void setEnableSplitting(boolean enableSplitting) { this.enableSplitting = enableSplitting; }
        
        public boolean isEnableVectorization() { return enableVectorization; }
        public void setEnableVectorization(boolean enableVectorization) { this.enableVectorization = enableVectorization; }
        
        public boolean isEnableMetadataExtraction() { return enableMetadataExtraction; }
        public void setEnableMetadataExtraction(boolean enableMetadataExtraction) { this.enableMetadataExtraction = enableMetadataExtraction; }
        
        public DocumentProcessingService.SplitOptions getSplitOptions() { return splitOptions; }
        public void setSplitOptions(DocumentProcessingService.SplitOptions splitOptions) { this.splitOptions = splitOptions; }
        
        public DocumentProcessingService.VectorizationOptions getVectorizationOptions() { return vectorizationOptions; }
        public void setVectorizationOptions(DocumentProcessingService.VectorizationOptions vectorizationOptions) { this.vectorizationOptions = vectorizationOptions; }
    }
}
