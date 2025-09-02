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
import com.coze.studio.entity.KnowledgeBase;
import com.coze.studio.service.KnowledgeBaseService;
import com.coze.studio.service.RAGService;
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
 * 知识库管理API控制器
 * 
 * @author coze-dev
 */
@Slf4j
@RestController
@RequestMapping("/api/knowledge-bases")
@RequiredArgsConstructor
@Validated
@Tag(name = "知识库管理", description = "知识库的创建、管理和查询接口")
public class KnowledgeBaseController {

    private final KnowledgeBaseService knowledgeBaseService;
    private final RAGService ragService;

    @Operation(summary = "创建知识库", description = "创建新的知识库")
    @PostMapping
    public ResponseEntity<ApiResponse<KnowledgeBase>> createKnowledgeBase(
            @Valid @RequestBody com.coze.studio.dto.knowledgebase.CreateKnowledgeBaseRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("创建知识库: name={}, userId={}", request.getName(), userDetails.getUsername());
        
        KnowledgeBase knowledgeBase = knowledgeBaseService.createKnowledgeBase(
                getUserId(userDetails), request);
        
        return ResponseEntity.ok(ApiResponse.success(knowledgeBase));
    }

    @Operation(summary = "获取知识库列表", description = "分页获取用户的知识库列表")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<KnowledgeBase>>> getKnowledgeBases(
            @PageableDefault(size = 20) Pageable pageable,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("获取知识库列表: userId={}, keyword={}, status={}", 
                userDetails.getUsername(), keyword, status);
        
        PageResponse<KnowledgeBase> knowledgeBases = knowledgeBaseService.getKnowledgeBases(
                getUserId(userDetails), keyword, status, pageable);
        
        return ResponseEntity.ok(ApiResponse.success(knowledgeBases));
    }

    @Operation(summary = "获取知识库详情", description = "根据ID获取知识库详细信息")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<KnowledgeBase>> getKnowledgeBase(
            @Parameter(description = "知识库ID") @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("获取知识库详情: id={}, userId={}", id, userDetails.getUsername());
        
        KnowledgeBase knowledgeBase = knowledgeBaseService.getKnowledgeBaseById(
                id, getUserId(userDetails));
        
        return ResponseEntity.ok(ApiResponse.success(knowledgeBase));
    }

    @Operation(summary = "更新知识库", description = "更新知识库信息")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<KnowledgeBase>> updateKnowledgeBase(
            @Parameter(description = "知识库ID") @PathVariable Long id,
            @Valid @RequestBody com.coze.studio.dto.knowledgebase.UpdateKnowledgeBaseRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("更新知识库: id={}, userId={}", id, userDetails.getUsername());
        
        KnowledgeBase knowledgeBase = knowledgeBaseService.updateKnowledgeBase(
                id, getUserId(userDetails), request);
        
        return ResponseEntity.ok(ApiResponse.success(knowledgeBase));
    }

    @Operation(summary = "删除知识库", description = "删除指定的知识库")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteKnowledgeBase(
            @Parameter(description = "知识库ID") @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("删除知识库: id={}, userId={}", id, userDetails.getUsername());
        
        knowledgeBaseService.deleteKnowledgeBase(id, getUserId(userDetails));
        
        return ResponseEntity.ok(ApiResponse.success("知识库删除成功"));
    }

    @Operation(summary = "RAG查询", description = "在知识库中进行RAG查询")
    @PostMapping("/{id}/query")
    public ResponseEntity<ApiResponse<RAGService.RAGResult>> ragQuery(
            @Parameter(description = "知识库ID") @PathVariable Long id,
            @Valid @RequestBody RAGQueryRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("RAG查询: knowledgeBaseId={}, query={}, userId={}", 
                id, request.getQuery(), userDetails.getUsername());
        
        RAGService.RAGRequest ragRequest = new RAGService.RAGRequest();
        ragRequest.setQuery(request.getQuery());
        ragRequest.setKnowledgeBaseId(id);
        ragRequest.setUserId(getUserId(userDetails));
        ragRequest.setSessionId(request.getSessionId());
        ragRequest.setConversationId(request.getConversationId());
        ragRequest.setRetrievalOptions(request.getRetrievalOptions());
        ragRequest.setGenerationOptions(request.getGenerationOptions());
        ragRequest.setContext(request.getContext());
        
        RAGService.RAGResult result = ragService.query(ragRequest);
        
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation(summary = "文档检索", description = "在知识库中检索相关文档")
    @PostMapping("/{id}/retrieve")
    public ResponseEntity<ApiResponse<RAGService.RetrievalResult>> retrieve(
            @Parameter(description = "知识库ID") @PathVariable Long id,
            @Valid @RequestBody RetrievalRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("文档检索: knowledgeBaseId={}, query={}, userId={}", 
                id, request.getQuery(), userDetails.getUsername());
        
        RAGService.RetrievalRequest retrievalRequest = new RAGService.RetrievalRequest(
                request.getQuery(), id, request.getOptions());
        
        RAGService.RetrievalResult result = ragService.retrieve(retrievalRequest);
        
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation(summary = "获取查询建议", description = "根据部分查询文本获取查询建议")
    @GetMapping("/{id}/suggestions")
    public ResponseEntity<ApiResponse<List<String>>> getQuerySuggestions(
            @Parameter(description = "知识库ID") @PathVariable Long id,
            @RequestParam @NotBlank String query,
            @RequestParam(defaultValue = "5") int maxSuggestions,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("获取查询建议: knowledgeBaseId={}, query={}, userId={}", 
                id, query, userDetails.getUsername());
        
        List<String> suggestions = ragService.getQuerySuggestions(query, id, maxSuggestions);
        
        return ResponseEntity.ok(ApiResponse.success(suggestions));
    }

    @Operation(summary = "获取知识库统计", description = "获取知识库的统计信息")
    @GetMapping("/{id}/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getKnowledgeBaseStats(
            @Parameter(description = "知识库ID") @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("获取知识库统计: id={}, userId={}", id, userDetails.getUsername());
        
        Map<String, Object> stats = knowledgeBaseService.getKnowledgeBaseStats(
                id, getUserId(userDetails));
        
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    /**
     * 从UserDetails中提取用户ID
     */
    private Long getUserId(UserDetails userDetails) {
        // 这里需要根据实际的UserDetails实现来提取用户ID
        // 假设用户名就是用户ID，实际实现中可能需要查询数据库
        try {
            return Long.parseLong(userDetails.getUsername());
        } catch (NumberFormatException e) {
            // 如果用户名不是数字，可能需要通过其他方式获取用户ID
            return 1L; // 临时返回默认值
        }
    }

    // CreateKnowledgeBaseRequest 和 UpdateKnowledgeBaseRequest 已移至 com.coze.studio.dto.knowledgebase 包

    /**
     * RAG查询请求
     */
    public static class RAGQueryRequest {
        @NotBlank(message = "查询内容不能为空")
        private String query;
        
        private String sessionId;
        private Long conversationId;
        private RAGService.RetrievalOptions retrievalOptions;
        private RAGService.GenerationOptions generationOptions;
        private Map<String, Object> context;

        // Getters and setters
        public String getQuery() { return query; }
        public void setQuery(String query) { this.query = query; }
        
        public String getSessionId() { return sessionId; }
        public void setSessionId(String sessionId) { this.sessionId = sessionId; }
        
        public Long getConversationId() { return conversationId; }
        public void setConversationId(Long conversationId) { this.conversationId = conversationId; }
        
        public RAGService.RetrievalOptions getRetrievalOptions() { return retrievalOptions; }
        public void setRetrievalOptions(RAGService.RetrievalOptions retrievalOptions) { this.retrievalOptions = retrievalOptions; }
        
        public RAGService.GenerationOptions getGenerationOptions() { return generationOptions; }
        public void setGenerationOptions(RAGService.GenerationOptions generationOptions) { this.generationOptions = generationOptions; }
        
        public Map<String, Object> getContext() { return context; }
        public void setContext(Map<String, Object> context) { this.context = context; }
    }

    /**
     * 检索请求
     */
    public static class RetrievalRequest {
        @NotBlank(message = "查询内容不能为空")
        private String query;
        
        private RAGService.RetrievalOptions options;

        // Getters and setters
        public String getQuery() { return query; }
        public void setQuery(String query) { this.query = query; }
        
        public RAGService.RetrievalOptions getOptions() { return options; }
        public void setOptions(RAGService.RetrievalOptions options) { this.options = options; }
    }
}
