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
import com.coze.studio.entity.ConversationVariable;
import com.coze.studio.entity.WorkflowVariable;
import com.coze.studio.service.VariableManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 变量管理API控制器
 * 
 * @author coze-dev
 */
@Slf4j
@RestController
@RequestMapping("/api/variables")
@RequiredArgsConstructor
@Validated
@Tag(name = "变量管理", description = "工作流和对话变量的管理接口")
public class VariableController {

    private final VariableManagementService variableManagementService;

    // ==================== 工作流变量管理 ====================

    @Operation(summary = "设置工作流变量", description = "创建或更新工作流变量")
    @PostMapping("/workflow")
    public ResponseEntity<ApiResponse<WorkflowVariable>> setWorkflowVariable(
            @Valid @RequestBody SetWorkflowVariableRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("设置工作流变量: name={}, scope={}, userId={}", 
                request.getName(), request.getScope(), userDetails.getUsername());
        
        VariableManagementService.SetWorkflowVariableRequest serviceRequest = 
                new VariableManagementService.SetWorkflowVariableRequest();
        serviceRequest.setName(request.getName());
        serviceRequest.setValue(request.getValue());
        serviceRequest.setScope(request.getScope());
        serviceRequest.setWorkflowId(request.getWorkflowId());
        serviceRequest.setExecutionId(request.getExecutionId());
        serviceRequest.setSessionId(request.getSessionId());
        serviceRequest.setNodeId(request.getNodeId());
        serviceRequest.setUserId(getUserId(userDetails));
        serviceRequest.setDescription(request.getDescription());
        serviceRequest.setIsSystem(request.getIsSystem());
        serviceRequest.setIsReadonly(request.getIsReadonly());
        serviceRequest.setIsSensitive(request.getIsSensitive());
        serviceRequest.setExpiresAt(request.getExpiresAt());
        serviceRequest.setTags(request.getTags());
        serviceRequest.setMetadata(request.getMetadata());
        serviceRequest.setPriority(request.getPriority());
        
        WorkflowVariable variable = variableManagementService.setWorkflowVariable(serviceRequest);
        
        return ResponseEntity.ok(ApiResponse.success(variable));
    }

    @Operation(summary = "获取工作流变量", description = "根据名称和作用域获取工作流变量")
    @GetMapping("/workflow/{name}")
    public ResponseEntity<ApiResponse<Object>> getWorkflowVariable(
            @Parameter(description = "变量名称") @PathVariable @NotBlank String name,
            @RequestParam @NotBlank String scope,
            @RequestParam(required = false) Long workflowId,
            @RequestParam(required = false) String executionId,
            @RequestParam(required = false) String sessionId,
            @RequestParam(required = false) String nodeId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("获取工作流变量: name={}, scope={}, userId={}", 
                name, scope, userDetails.getUsername());
        
        VariableManagementService.GetWorkflowVariableRequest request = 
                new VariableManagementService.GetWorkflowVariableRequest();
        request.setName(name);
        request.setScope(scope);
        request.setWorkflowId(workflowId);
        request.setExecutionId(executionId);
        request.setSessionId(sessionId);
        request.setNodeId(nodeId);
        request.setUserId(getUserId(userDetails));
        
        Optional<Object> variable = variableManagementService.getWorkflowVariable(request);
        
        return ResponseEntity.ok(ApiResponse.success(variable.orElse(null)));
    }

    @Operation(summary = "删除工作流变量", description = "删除指定的工作流变量")
    @DeleteMapping("/workflow/{name}")
    public ResponseEntity<ApiResponse<Boolean>> deleteWorkflowVariable(
            @Parameter(description = "变量名称") @PathVariable @NotBlank String name,
            @RequestParam @NotBlank String scope,
            @RequestParam(required = false) Long workflowId,
            @RequestParam(required = false) String executionId,
            @RequestParam(required = false) String sessionId,
            @RequestParam(required = false) String nodeId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("删除工作流变量: name={}, scope={}, userId={}", 
                name, scope, userDetails.getUsername());
        
        VariableManagementService.DeleteWorkflowVariableRequest request = 
                new VariableManagementService.DeleteWorkflowVariableRequest();
        request.setName(name);
        request.setScope(scope);
        request.setWorkflowId(workflowId);
        request.setExecutionId(executionId);
        request.setSessionId(sessionId);
        request.setNodeId(nodeId);
        request.setUserId(getUserId(userDetails));
        
        boolean deleted = variableManagementService.deleteWorkflowVariable(request);
        
        return ResponseEntity.ok(ApiResponse.success(deleted));
    }

    @Operation(summary = "获取工作流变量列表", description = "获取指定作用域的工作流变量列表")
    @GetMapping("/workflow")
    public ResponseEntity<ApiResponse<List<WorkflowVariable>>> getWorkflowVariables(
            @RequestParam(required = false) String scope,
            @RequestParam(required = false) Long workflowId,
            @RequestParam(required = false) String executionId,
            @RequestParam(required = false) String sessionId,
            @RequestParam(required = false) String nodeId,
            @RequestParam(defaultValue = "false") Boolean includeSystem,
            @RequestParam(defaultValue = "false") Boolean includeSensitive,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("获取工作流变量列表: scope={}, workflowId={}, userId={}", 
                scope, workflowId, userDetails.getUsername());
        
        VariableManagementService.GetWorkflowVariablesRequest request = 
                new VariableManagementService.GetWorkflowVariablesRequest();
        request.setScope(scope);
        request.setWorkflowId(workflowId);
        request.setExecutionId(executionId);
        request.setSessionId(sessionId);
        request.setNodeId(nodeId);
        request.setUserId(getUserId(userDetails));
        request.setIncludeSystem(includeSystem);
        request.setIncludeSensitive(includeSensitive);
        
        List<WorkflowVariable> variables = variableManagementService.getWorkflowVariables(request);
        
        return ResponseEntity.ok(ApiResponse.success(variables));
    }

    @Operation(summary = "清理工作流变量", description = "清理指定作用域的工作流变量")
    @PostMapping("/workflow/clear")
    public ResponseEntity<ApiResponse<String>> clearWorkflowVariables(
            @RequestParam(required = false) String scope,
            @RequestParam(required = false) Long workflowId,
            @RequestParam(required = false) String executionId,
            @RequestParam(required = false) String sessionId,
            @RequestParam(required = false) String nodeId,
            @RequestParam(defaultValue = "false") Boolean includeSystem,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("清理工作流变量: scope={}, workflowId={}, userId={}", 
                scope, workflowId, userDetails.getUsername());
        
        VariableManagementService.ClearWorkflowVariablesRequest request = 
                new VariableManagementService.ClearWorkflowVariablesRequest();
        request.setScope(scope);
        request.setWorkflowId(workflowId);
        request.setExecutionId(executionId);
        request.setSessionId(sessionId);
        request.setNodeId(nodeId);
        request.setUserId(getUserId(userDetails));
        request.setIncludeSystem(includeSystem);
        
        variableManagementService.clearWorkflowVariables(request);
        
        return ResponseEntity.ok(ApiResponse.success("工作流变量已清理"));
    }

    // ==================== 对话变量管理 ====================

    @Operation(summary = "设置对话变量", description = "创建或更新对话变量")
    @PostMapping("/conversation")
    public ResponseEntity<ApiResponse<ConversationVariable>> setConversationVariable(
            @Valid @RequestBody SetConversationVariableRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("设置对话变量: name={}, scope={}, userId={}", 
                request.getName(), request.getScope(), userDetails.getUsername());
        
        VariableManagementService.SetConversationVariableRequest serviceRequest = 
                new VariableManagementService.SetConversationVariableRequest();
        serviceRequest.setName(request.getName());
        serviceRequest.setValue(request.getValue());
        serviceRequest.setScope(request.getScope());
        serviceRequest.setConversationId(request.getConversationId());
        serviceRequest.setSessionId(request.getSessionId());
        serviceRequest.setMessageId(request.getMessageId());
        serviceRequest.setBotId(request.getBotId());
        serviceRequest.setUserId(getUserId(userDetails));
        serviceRequest.setSource(request.getSource());
        serviceRequest.setDescription(request.getDescription());
        serviceRequest.setIsSystem(request.getIsSystem());
        serviceRequest.setIsReadonly(request.getIsReadonly());
        serviceRequest.setIsSensitive(request.getIsSensitive());
        serviceRequest.setExpiresAt(request.getExpiresAt());
        serviceRequest.setTags(request.getTags());
        serviceRequest.setMetadata(request.getMetadata());
        serviceRequest.setPriority(request.getPriority());
        
        ConversationVariable variable = variableManagementService.setConversationVariable(serviceRequest);
        
        return ResponseEntity.ok(ApiResponse.success(variable));
    }

    @Operation(summary = "获取对话变量", description = "根据名称和作用域获取对话变量")
    @GetMapping("/conversation/{name}")
    public ResponseEntity<ApiResponse<Object>> getConversationVariable(
            @Parameter(description = "变量名称") @PathVariable @NotBlank String name,
            @RequestParam @NotBlank String scope,
            @RequestParam(required = false) Long conversationId,
            @RequestParam(required = false) String sessionId,
            @RequestParam(required = false) Long messageId,
            @RequestParam(required = false) Long botId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("获取对话变量: name={}, scope={}, userId={}", 
                name, scope, userDetails.getUsername());
        
        VariableManagementService.GetConversationVariableRequest request = 
                new VariableManagementService.GetConversationVariableRequest();
        request.setName(name);
        request.setScope(scope);
        request.setConversationId(conversationId);
        request.setSessionId(sessionId);
        request.setMessageId(messageId);
        request.setBotId(botId);
        request.setUserId(getUserId(userDetails));
        
        Optional<Object> variable = variableManagementService.getConversationVariable(request);
        
        return ResponseEntity.ok(ApiResponse.success(variable.orElse(null)));
    }

    @Operation(summary = "删除对话变量", description = "删除指定的对话变量")
    @DeleteMapping("/conversation/{name}")
    public ResponseEntity<ApiResponse<Boolean>> deleteConversationVariable(
            @Parameter(description = "变量名称") @PathVariable @NotBlank String name,
            @RequestParam @NotBlank String scope,
            @RequestParam(required = false) Long conversationId,
            @RequestParam(required = false) String sessionId,
            @RequestParam(required = false) Long messageId,
            @RequestParam(required = false) Long botId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("删除对话变量: name={}, scope={}, userId={}", 
                name, scope, userDetails.getUsername());
        
        VariableManagementService.DeleteConversationVariableRequest request = 
                new VariableManagementService.DeleteConversationVariableRequest();
        request.setName(name);
        request.setScope(scope);
        request.setConversationId(conversationId);
        request.setSessionId(sessionId);
        request.setMessageId(messageId);
        request.setBotId(botId);
        request.setUserId(getUserId(userDetails));
        
        boolean deleted = variableManagementService.deleteConversationVariable(request);
        
        return ResponseEntity.ok(ApiResponse.success(deleted));
    }

    @Operation(summary = "获取对话变量列表", description = "获取指定作用域的对话变量列表")
    @GetMapping("/conversation")
    public ResponseEntity<ApiResponse<List<ConversationVariable>>> getConversationVariables(
            @RequestParam(required = false) String scope,
            @RequestParam(required = false) Long conversationId,
            @RequestParam(required = false) String sessionId,
            @RequestParam(required = false) Long messageId,
            @RequestParam(required = false) Long botId,
            @RequestParam(required = false) String source,
            @RequestParam(defaultValue = "false") Boolean includeSystem,
            @RequestParam(defaultValue = "false") Boolean includeSensitive,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("获取对话变量列表: scope={}, conversationId={}, userId={}",
                scope, conversationId, userDetails.getUsername());

        VariableManagementService.GetConversationVariablesRequest request =
                new VariableManagementService.GetConversationVariablesRequest();
        request.setScope(scope);
        request.setConversationId(conversationId);
        request.setSessionId(sessionId);
        request.setMessageId(messageId);
        request.setBotId(botId);
        request.setUserId(getUserId(userDetails));
        request.setSource(source);
        request.setIncludeSystem(includeSystem);
        request.setIncludeSensitive(includeSensitive);

        List<ConversationVariable> variables = variableManagementService.getConversationVariables(request);

        return ResponseEntity.ok(ApiResponse.success(variables));
    }

    @Operation(summary = "清理对话变量", description = "清理指定作用域的对话变量")
    @PostMapping("/conversation/clear")
    public ResponseEntity<ApiResponse<String>> clearConversationVariables(
            @RequestParam(required = false) String scope,
            @RequestParam(required = false) Long conversationId,
            @RequestParam(required = false) String sessionId,
            @RequestParam(required = false) Long messageId,
            @RequestParam(required = false) Long botId,
            @RequestParam(required = false) String source,
            @RequestParam(defaultValue = "false") Boolean includeSystem,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("清理对话变量: scope={}, conversationId={}, userId={}",
                scope, conversationId, userDetails.getUsername());

        VariableManagementService.ClearConversationVariablesRequest request =
                new VariableManagementService.ClearConversationVariablesRequest();
        request.setScope(scope);
        request.setConversationId(conversationId);
        request.setSessionId(sessionId);
        request.setMessageId(messageId);
        request.setBotId(botId);
        request.setUserId(getUserId(userDetails));
        request.setSource(source);
        request.setIncludeSystem(includeSystem);

        variableManagementService.clearConversationVariables(request);

        return ResponseEntity.ok(ApiResponse.success("对话变量已清理"));
    }

    // ==================== 变量解析和工具 ====================

    @Operation(summary = "解析变量", description = "解析文本中的变量引用")
    @PostMapping("/parse")
    public ResponseEntity<ApiResponse<String>> parseVariables(
            @Valid @RequestBody ParseVariablesRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("解析变量: userId={}", userDetails.getUsername());

        VariableManagementService.VariableContext context =
                new VariableManagementService.VariableContext();
        context.setUserId(getUserId(userDetails));
        context.setWorkflowId(request.getWorkflowId());
        context.setExecutionId(request.getExecutionId());
        context.setSessionId(request.getSessionId());
        context.setNodeId(request.getNodeId());
        context.setConversationId(request.getConversationId());
        context.setMessageId(request.getMessageId());
        context.setBotId(request.getBotId());
        context.setAdditionalContext(request.getAdditionalContext());

        String result = variableManagementService.parseVariables(request.getText(), context);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation(summary = "渲染模板", description = "渲染模板，替换变量占位符")
    @PostMapping("/render")
    public ResponseEntity<ApiResponse<String>> renderTemplate(
            @Valid @RequestBody RenderTemplateRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("渲染模板: userId={}", userDetails.getUsername());

        VariableManagementService.VariableContext context =
                new VariableManagementService.VariableContext();
        context.setUserId(getUserId(userDetails));
        context.setWorkflowId(request.getWorkflowId());
        context.setExecutionId(request.getExecutionId());
        context.setSessionId(request.getSessionId());
        context.setNodeId(request.getNodeId());
        context.setConversationId(request.getConversationId());
        context.setMessageId(request.getMessageId());
        context.setBotId(request.getBotId());
        context.setAdditionalContext(request.getAdditionalContext());

        String result = variableManagementService.renderTemplate(request.getTemplate(), context);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation(summary = "提取变量引用", description = "提取文本中的变量引用")
    @PostMapping("/extract")
    public ResponseEntity<ApiResponse<List<String>>> extractVariableReferences(
            @Valid @RequestBody ExtractVariablesRequest request) {

        log.info("提取变量引用");

        List<String> variables = variableManagementService.extractVariableReferences(request.getText());

        return ResponseEntity.ok(ApiResponse.success(variables));
    }

    @Operation(summary = "验证变量表达式", description = "验证变量表达式的格式")
    @PostMapping("/validate")
    public ResponseEntity<ApiResponse<Boolean>> validateVariableExpression(
            @Valid @RequestBody ValidateVariableRequest request) {

        log.info("验证变量表达式: expression={}", request.getExpression());

        boolean valid = variableManagementService.validateVariableExpression(request.getExpression());

        return ResponseEntity.ok(ApiResponse.success(valid));
    }

    // ==================== 变量统计和管理 ====================

    @Operation(summary = "获取变量统计", description = "获取变量统计信息")
    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<VariableManagementService.VariableStatistics>> getVariableStatistics(
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("获取变量统计: userId={}", userDetails.getUsername());

        VariableManagementService.VariableStatistics statistics =
                variableManagementService.getVariableStatistics(getUserId(userDetails));

        return ResponseEntity.ok(ApiResponse.success(statistics));
    }

    @Operation(summary = "清理过期变量", description = "清理所有过期的变量")
    @PostMapping("/cleanup/expired")
    public ResponseEntity<ApiResponse<String>> cleanupExpiredVariables() {

        log.info("清理过期变量");

        variableManagementService.cleanupExpiredVariables();

        return ResponseEntity.ok(ApiResponse.success("过期变量已清理"));
    }

    @Operation(summary = "清理未使用变量", description = "清理长时间未使用的变量")
    @PostMapping("/cleanup/unused")
    public ResponseEntity<ApiResponse<String>> cleanupUnusedVariables(
            @RequestParam(defaultValue = "30") int daysThreshold) {

        log.info("清理未使用变量: daysThreshold={}", daysThreshold);

        LocalDateTime threshold = LocalDateTime.now().minusDays(daysThreshold);
        variableManagementService.cleanupUnusedVariables(threshold);

        return ResponseEntity.ok(ApiResponse.success("未使用变量已清理"));
    }

    @Operation(summary = "导出变量", description = "导出变量数据")
    @PostMapping("/export")
    public ResponseEntity<ApiResponse<Map<String, Object>>> exportVariables(
            @Valid @RequestBody ExportVariablesRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("导出变量: userId={}, scope={}", userDetails.getUsername(), request.getScope());

        VariableManagementService.ExportVariablesRequest serviceRequest =
                new VariableManagementService.ExportVariablesRequest();
        serviceRequest.setUserId(getUserId(userDetails));
        serviceRequest.setScope(request.getScope());
        serviceRequest.setWorkflowId(request.getWorkflowId());
        serviceRequest.setConversationId(request.getConversationId());
        serviceRequest.setIncludeSystem(request.getIncludeSystem());
        serviceRequest.setIncludeSensitive(request.getIncludeSensitive());
        serviceRequest.setFormat(request.getFormat());

        Map<String, Object> exportData = variableManagementService.exportVariables(serviceRequest);

        return ResponseEntity.ok(ApiResponse.success(exportData));
    }

    @Operation(summary = "导入变量", description = "导入变量数据")
    @PostMapping("/import")
    public ResponseEntity<ApiResponse<VariableManagementService.ImportVariablesResult>> importVariables(
            @Valid @RequestBody ImportVariablesRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("导入变量: userId={}, scope={}", userDetails.getUsername(), request.getScope());

        VariableManagementService.ImportVariablesRequest serviceRequest =
                new VariableManagementService.ImportVariablesRequest();
        serviceRequest.setUserId(getUserId(userDetails));
        serviceRequest.setVariables(request.getVariables());
        serviceRequest.setScope(request.getScope());
        serviceRequest.setWorkflowId(request.getWorkflowId());
        serviceRequest.setConversationId(request.getConversationId());
        serviceRequest.setOverwriteExisting(request.getOverwriteExisting());
        serviceRequest.setValidateBeforeImport(request.getValidateBeforeImport());

        VariableManagementService.ImportVariablesResult result =
                variableManagementService.importVariables(serviceRequest);

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

    // ==================== 请求DTO类 ====================

    /**
     * 设置工作流变量请求
     */
    public static class SetWorkflowVariableRequest {
        @NotBlank(message = "变量名不能为空")
        private String name;
        private Object value;
        @NotBlank(message = "作用域不能为空")
        private String scope;
        private Long workflowId;
        private String executionId;
        private String sessionId;
        private String nodeId;
        private String description;
        private Boolean isSystem = false;
        private Boolean isReadonly = false;
        private Boolean isSensitive = false;
        private LocalDateTime expiresAt;
        private String tags;
        private Map<String, Object> metadata;
        private Integer priority = 0;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Object getValue() { return value; }
        public void setValue(Object value) { this.value = value; }
        public String getScope() { return scope; }
        public void setScope(String scope) { this.scope = scope; }
        public Long getWorkflowId() { return workflowId; }
        public void setWorkflowId(Long workflowId) { this.workflowId = workflowId; }
        public String getExecutionId() { return executionId; }
        public void setExecutionId(String executionId) { this.executionId = executionId; }
        public String getSessionId() { return sessionId; }
        public void setSessionId(String sessionId) { this.sessionId = sessionId; }
        public String getNodeId() { return nodeId; }
        public void setNodeId(String nodeId) { this.nodeId = nodeId; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Boolean getIsSystem() { return isSystem; }
        public void setIsSystem(Boolean isSystem) { this.isSystem = isSystem; }
        public Boolean getIsReadonly() { return isReadonly; }
        public void setIsReadonly(Boolean isReadonly) { this.isReadonly = isReadonly; }
        public Boolean getIsSensitive() { return isSensitive; }
        public void setIsSensitive(Boolean isSensitive) { this.isSensitive = isSensitive; }
        public LocalDateTime getExpiresAt() { return expiresAt; }
        public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
        public String getTags() { return tags; }
        public void setTags(String tags) { this.tags = tags; }
        public Map<String, Object> getMetadata() { return metadata; }
        public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
        public Integer getPriority() { return priority; }
        public void setPriority(Integer priority) { this.priority = priority; }
    }

    /**
     * 设置对话变量请求
     */
    public static class SetConversationVariableRequest {
        @NotBlank(message = "变量名不能为空")
        private String name;
        private Object value;
        @NotBlank(message = "作用域不能为空")
        private String scope;
        private Long conversationId;
        private String sessionId;
        private Long messageId;
        private Long botId;
        private String source;
        private String description;
        private Boolean isSystem = false;
        private Boolean isReadonly = false;
        private Boolean isSensitive = false;
        private LocalDateTime expiresAt;
        private String tags;
        private Map<String, Object> metadata;
        private Integer priority = 0;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Object getValue() { return value; }
        public void setValue(Object value) { this.value = value; }
        public String getScope() { return scope; }
        public void setScope(String scope) { this.scope = scope; }
        public Long getConversationId() { return conversationId; }
        public void setConversationId(Long conversationId) { this.conversationId = conversationId; }
        public String getSessionId() { return sessionId; }
        public void setSessionId(String sessionId) { this.sessionId = sessionId; }
        public Long getMessageId() { return messageId; }
        public void setMessageId(Long messageId) { this.messageId = messageId; }
        public Long getBotId() { return botId; }
        public void setBotId(Long botId) { this.botId = botId; }
        public String getSource() { return source; }
        public void setSource(String source) { this.source = source; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Boolean getIsSystem() { return isSystem; }
        public void setIsSystem(Boolean isSystem) { this.isSystem = isSystem; }
        public Boolean getIsReadonly() { return isReadonly; }
        public void setIsReadonly(Boolean isReadonly) { this.isReadonly = isReadonly; }
        public Boolean getIsSensitive() { return isSensitive; }
        public void setIsSensitive(Boolean isSensitive) { this.isSensitive = isSensitive; }
        public LocalDateTime getExpiresAt() { return expiresAt; }
        public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
        public String getTags() { return tags; }
        public void setTags(String tags) { this.tags = tags; }
        public Map<String, Object> getMetadata() { return metadata; }
        public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
        public Integer getPriority() { return priority; }
        public void setPriority(Integer priority) { this.priority = priority; }
    }

    /**
     * 解析变量请求
     */
    public static class ParseVariablesRequest {
        @NotBlank(message = "文本不能为空")
        private String text;
        private Long workflowId;
        private String executionId;
        private String sessionId;
        private String nodeId;
        private Long conversationId;
        private Long messageId;
        private Long botId;
        private Map<String, Object> additionalContext;

        // Getters and setters
        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
        public Long getWorkflowId() { return workflowId; }
        public void setWorkflowId(Long workflowId) { this.workflowId = workflowId; }
        public String getExecutionId() { return executionId; }
        public void setExecutionId(String executionId) { this.executionId = executionId; }
        public String getSessionId() { return sessionId; }
        public void setSessionId(String sessionId) { this.sessionId = sessionId; }
        public String getNodeId() { return nodeId; }
        public void setNodeId(String nodeId) { this.nodeId = nodeId; }
        public Long getConversationId() { return conversationId; }
        public void setConversationId(Long conversationId) { this.conversationId = conversationId; }
        public Long getMessageId() { return messageId; }
        public void setMessageId(Long messageId) { this.messageId = messageId; }
        public Long getBotId() { return botId; }
        public void setBotId(Long botId) { this.botId = botId; }
        public Map<String, Object> getAdditionalContext() { return additionalContext; }
        public void setAdditionalContext(Map<String, Object> additionalContext) { this.additionalContext = additionalContext; }
    }

    /**
     * 渲染模板请求
     */
    public static class RenderTemplateRequest {
        @NotBlank(message = "模板不能为空")
        private String template;
        private Long workflowId;
        private String executionId;
        private String sessionId;
        private String nodeId;
        private Long conversationId;
        private Long messageId;
        private Long botId;
        private Map<String, Object> additionalContext;

        // Getters and setters
        public String getTemplate() { return template; }
        public void setTemplate(String template) { this.template = template; }
        public Long getWorkflowId() { return workflowId; }
        public void setWorkflowId(Long workflowId) { this.workflowId = workflowId; }
        public String getExecutionId() { return executionId; }
        public void setExecutionId(String executionId) { this.executionId = executionId; }
        public String getSessionId() { return sessionId; }
        public void setSessionId(String sessionId) { this.sessionId = sessionId; }
        public String getNodeId() { return nodeId; }
        public void setNodeId(String nodeId) { this.nodeId = nodeId; }
        public Long getConversationId() { return conversationId; }
        public void setConversationId(Long conversationId) { this.conversationId = conversationId; }
        public Long getMessageId() { return messageId; }
        public void setMessageId(Long messageId) { this.messageId = messageId; }
        public Long getBotId() { return botId; }
        public void setBotId(Long botId) { this.botId = botId; }
        public Map<String, Object> getAdditionalContext() { return additionalContext; }
        public void setAdditionalContext(Map<String, Object> additionalContext) { this.additionalContext = additionalContext; }
    }

    /**
     * 提取变量请求
     */
    public static class ExtractVariablesRequest {
        @NotBlank(message = "文本不能为空")
        private String text;

        // Getters and setters
        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
    }

    /**
     * 验证变量请求
     */
    public static class ValidateVariableRequest {
        @NotBlank(message = "表达式不能为空")
        private String expression;

        // Getters and setters
        public String getExpression() { return expression; }
        public void setExpression(String expression) { this.expression = expression; }
    }

    /**
     * 导出变量请求
     */
    public static class ExportVariablesRequest {
        private String scope;
        private Long workflowId;
        private Long conversationId;
        private Boolean includeSystem = false;
        private Boolean includeSensitive = false;
        private String format = "JSON";

        // Getters and setters
        public String getScope() { return scope; }
        public void setScope(String scope) { this.scope = scope; }
        public Long getWorkflowId() { return workflowId; }
        public void setWorkflowId(Long workflowId) { this.workflowId = workflowId; }
        public Long getConversationId() { return conversationId; }
        public void setConversationId(Long conversationId) { this.conversationId = conversationId; }
        public Boolean getIncludeSystem() { return includeSystem; }
        public void setIncludeSystem(Boolean includeSystem) { this.includeSystem = includeSystem; }
        public Boolean getIncludeSensitive() { return includeSensitive; }
        public void setIncludeSensitive(Boolean includeSensitive) { this.includeSensitive = includeSensitive; }
        public String getFormat() { return format; }
        public void setFormat(String format) { this.format = format; }
    }

    /**
     * 导入变量请求
     */
    public static class ImportVariablesRequest {
        @NotNull(message = "变量数据不能为空")
        private Map<String, Object> variables;
        private String scope;
        private Long workflowId;
        private Long conversationId;
        private Boolean overwriteExisting = false;
        private Boolean validateBeforeImport = true;

        // Getters and setters
        public Map<String, Object> getVariables() { return variables; }
        public void setVariables(Map<String, Object> variables) { this.variables = variables; }
        public String getScope() { return scope; }
        public void setScope(String scope) { this.scope = scope; }
        public Long getWorkflowId() { return workflowId; }
        public void setWorkflowId(Long workflowId) { this.workflowId = workflowId; }
        public Long getConversationId() { return conversationId; }
        public void setConversationId(Long conversationId) { this.conversationId = conversationId; }
        public Boolean getOverwriteExisting() { return overwriteExisting; }
        public void setOverwriteExisting(Boolean overwriteExisting) { this.overwriteExisting = overwriteExisting; }
        public Boolean getValidateBeforeImport() { return validateBeforeImport; }
        public void setValidateBeforeImport(Boolean validateBeforeImport) { this.validateBeforeImport = validateBeforeImport; }
    }
}
