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

import com.coze.studio.entity.ConversationVariable;
import com.coze.studio.entity.WorkflowVariable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 变量管理服务接口
 * 提供工作流和对话变量的统一管理功能
 * 
 * @author coze-dev
 */
public interface VariableManagementService {

    // ==================== 工作流变量管理 ====================

    /**
     * 设置工作流变量
     */
    WorkflowVariable setWorkflowVariable(SetWorkflowVariableRequest request);

    /**
     * 获取工作流变量
     */
    Optional<Object> getWorkflowVariable(GetWorkflowVariableRequest request);

    /**
     * 删除工作流变量
     */
    boolean deleteWorkflowVariable(DeleteWorkflowVariableRequest request);

    /**
     * 获取工作流变量列表
     */
    List<WorkflowVariable> getWorkflowVariables(GetWorkflowVariablesRequest request);

    /**
     * 清理工作流变量
     */
    void clearWorkflowVariables(ClearWorkflowVariablesRequest request);

    // ==================== 对话变量管理 ====================

    /**
     * 设置对话变量
     */
    ConversationVariable setConversationVariable(SetConversationVariableRequest request);

    /**
     * 获取对话变量
     */
    Optional<Object> getConversationVariable(GetConversationVariableRequest request);

    /**
     * 删除对话变量
     */
    boolean deleteConversationVariable(DeleteConversationVariableRequest request);

    /**
     * 获取对话变量列表
     */
    List<ConversationVariable> getConversationVariables(GetConversationVariablesRequest request);

    /**
     * 清理对话变量
     */
    void clearConversationVariables(ClearConversationVariablesRequest request);

    // ==================== 变量解析和渲染 ====================

    /**
     * 解析文本中的变量引用
     */
    String parseVariables(String text, VariableContext context);

    /**
     * 渲染模板（替换变量占位符）
     */
    String renderTemplate(String template, VariableContext context);

    /**
     * 提取文本中的变量引用
     */
    List<String> extractVariableReferences(String text);

    /**
     * 验证变量表达式
     */
    boolean validateVariableExpression(String expression);

    // ==================== 变量统计和管理 ====================

    /**
     * 获取变量统计信息
     */
    VariableStatistics getVariableStatistics(Long userId);

    /**
     * 清理过期变量
     */
    void cleanupExpiredVariables();

    /**
     * 清理未使用的变量
     */
    void cleanupUnusedVariables(LocalDateTime threshold);

    /**
     * 导出变量
     */
    Map<String, Object> exportVariables(ExportVariablesRequest request);

    /**
     * 导入变量
     */
    ImportVariablesResult importVariables(ImportVariablesRequest request);

    // ==================== 请求和响应类 ====================

    /**
     * 设置工作流变量请求
     */
    class SetWorkflowVariableRequest {
        private String name;
        private Object value;
        private String scope;
        private Long workflowId;
        private String executionId;
        private String sessionId;
        private String nodeId;
        private Long userId;
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
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
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
     * 获取工作流变量请求
     */
    class GetWorkflowVariableRequest {
        private String name;
        private String scope;
        private Long workflowId;
        private String executionId;
        private String sessionId;
        private String nodeId;
        private Long userId;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
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
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
    }

    /**
     * 删除工作流变量请求
     */
    class DeleteWorkflowVariableRequest {
        private String name;
        private String scope;
        private Long workflowId;
        private String executionId;
        private String sessionId;
        private String nodeId;
        private Long userId;

        // Getters and setters (省略具体实现)
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
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
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
    }

    /**
     * 获取工作流变量列表请求
     */
    class GetWorkflowVariablesRequest {
        private String scope;
        private Long workflowId;
        private String executionId;
        private String sessionId;
        private String nodeId;
        private Long userId;
        private Boolean includeSystem = false;
        private Boolean includeSensitive = false;

        // Getters and setters (省略具体实现)
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
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public Boolean getIncludeSystem() { return includeSystem; }
        public void setIncludeSystem(Boolean includeSystem) { this.includeSystem = includeSystem; }
        public Boolean getIncludeSensitive() { return includeSensitive; }
        public void setIncludeSensitive(Boolean includeSensitive) { this.includeSensitive = includeSensitive; }
    }

    /**
     * 清理工作流变量请求
     */
    class ClearWorkflowVariablesRequest {
        private String scope;
        private Long workflowId;
        private String executionId;
        private String sessionId;
        private String nodeId;
        private Long userId;
        private Boolean includeSystem = false;

        // Getters and setters (省略具体实现)
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
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public Boolean getIncludeSystem() { return includeSystem; }
        public void setIncludeSystem(Boolean includeSystem) { this.includeSystem = includeSystem; }
    }

    // 对话变量相关请求类（结构类似，省略详细实现）
    class SetConversationVariableRequest {
        private String name;
        private Object value;
        private String scope;
        private Long conversationId;
        private String sessionId;
        private Long messageId;
        private Long botId;
        private Long userId;
        private String source;
        private String description;
        private Boolean isSystem = false;
        private Boolean isReadonly = false;
        private Boolean isSensitive = false;
        private LocalDateTime expiresAt;
        private String tags;
        private Map<String, Object> metadata;
        private Integer priority = 0;

        // Getters and setters (省略具体实现)
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
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
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

    class GetConversationVariableRequest {
        private String name;
        private String scope;
        private Long conversationId;
        private String sessionId;
        private Long messageId;
        private Long botId;
        private Long userId;

        // Getters and setters (省略具体实现)
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
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
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
    }

    class DeleteConversationVariableRequest {
        private String name;
        private String scope;
        private Long conversationId;
        private String sessionId;
        private Long messageId;
        private Long botId;
        private Long userId;

        // Getters and setters (省略具体实现)
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
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
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
    }

    class GetConversationVariablesRequest {
        private String scope;
        private Long conversationId;
        private String sessionId;
        private Long messageId;
        private Long botId;
        private Long userId;
        private String source;
        private Boolean includeSystem = false;
        private Boolean includeSensitive = false;

        // Getters and setters (省略具体实现)
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
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getSource() { return source; }
        public void setSource(String source) { this.source = source; }
        public Boolean getIncludeSystem() { return includeSystem; }
        public void setIncludeSystem(Boolean includeSystem) { this.includeSystem = includeSystem; }
        public Boolean getIncludeSensitive() { return includeSensitive; }
        public void setIncludeSensitive(Boolean includeSensitive) { this.includeSensitive = includeSensitive; }
    }

    class ClearConversationVariablesRequest {
        private String scope;
        private Long conversationId;
        private String sessionId;
        private Long messageId;
        private Long botId;
        private Long userId;
        private String source;
        private Boolean includeSystem = false;

        // Getters and setters (省略具体实现)
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
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getSource() { return source; }
        public void setSource(String source) { this.source = source; }
        public Boolean getIncludeSystem() { return includeSystem; }
        public void setIncludeSystem(Boolean includeSystem) { this.includeSystem = includeSystem; }
    }

    /**
     * 变量上下文
     */
    class VariableContext {
        private Long userId;
        private Long workflowId;
        private String executionId;
        private String sessionId;
        private String nodeId;
        private Long conversationId;
        private Long messageId;
        private Long botId;
        private Map<String, Object> additionalContext;

        // Getters and setters (省略具体实现)
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
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
     * 变量统计信息
     */
    class VariableStatistics {
        private Long userId;
        private Map<String, Long> workflowVariablesByScope;
        private Map<String, Long> conversationVariablesByScope;
        private long totalWorkflowVariables;
        private long totalConversationVariables;
        private long workflowVariableCount;
        private long conversationVariableCount;
        private long totalVariableCount;
        private long expiredVariables;
        private long unusedVariables;
        private Map<String, Long> variablesByScope;
        private Map<String, Long> variablesByType;
        private Map<String, Object> additionalStats;

        // Getters and setters
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public Map<String, Long> getWorkflowVariablesByScope() { return workflowVariablesByScope; }
        public void setWorkflowVariablesByScope(Map<String, Long> workflowVariablesByScope) { this.workflowVariablesByScope = workflowVariablesByScope; }
        public Map<String, Long> getConversationVariablesByScope() { return conversationVariablesByScope; }
        public void setConversationVariablesByScope(Map<String, Long> conversationVariablesByScope) { this.conversationVariablesByScope = conversationVariablesByScope; }
        public long getTotalWorkflowVariables() { return totalWorkflowVariables; }
        public void setTotalWorkflowVariables(long totalWorkflowVariables) { this.totalWorkflowVariables = totalWorkflowVariables; }
        public long getTotalConversationVariables() { return totalConversationVariables; }
        public void setTotalConversationVariables(long totalConversationVariables) { this.totalConversationVariables = totalConversationVariables; }
        public long getWorkflowVariableCount() { return workflowVariableCount; }
        public void setWorkflowVariableCount(long workflowVariableCount) { this.workflowVariableCount = workflowVariableCount; }
        public long getConversationVariableCount() { return conversationVariableCount; }
        public void setConversationVariableCount(long conversationVariableCount) { this.conversationVariableCount = conversationVariableCount; }
        public long getTotalVariableCount() { return totalVariableCount; }
        public void setTotalVariableCount(long totalVariableCount) { this.totalVariableCount = totalVariableCount; }
        public long getExpiredVariables() { return expiredVariables; }
        public void setExpiredVariables(long expiredVariables) { this.expiredVariables = expiredVariables; }
        public long getUnusedVariables() { return unusedVariables; }
        public void setUnusedVariables(long unusedVariables) { this.unusedVariables = unusedVariables; }
        public Map<String, Long> getVariablesByScope() { return variablesByScope; }
        public void setVariablesByScope(Map<String, Long> variablesByScope) { this.variablesByScope = variablesByScope; }
        public Map<String, Long> getVariablesByType() { return variablesByType; }
        public void setVariablesByType(Map<String, Long> variablesByType) { this.variablesByType = variablesByType; }
        public Map<String, Object> getAdditionalStats() { return additionalStats; }
        public void setAdditionalStats(Map<String, Object> additionalStats) { this.additionalStats = additionalStats; }
    }

    /**
     * 导出变量请求
     */
    class ExportVariablesRequest {
        private Long userId;
        private String scope;
        private Long workflowId;
        private Long conversationId;
        private Boolean includeSystem = false;
        private Boolean includeSensitive = false;
        private Boolean includeWorkflow = true;
        private Boolean includeConversation = true;
        private String format = "JSON"; // JSON, CSV, YAML

        // Getters and setters
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
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
        public Boolean isIncludeWorkflow() { return includeWorkflow; }
        public void setIncludeWorkflow(Boolean includeWorkflow) { this.includeWorkflow = includeWorkflow; }
        public Boolean isIncludeConversation() { return includeConversation; }
        public void setIncludeConversation(Boolean includeConversation) { this.includeConversation = includeConversation; }
        public String getFormat() { return format; }
        public void setFormat(String format) { this.format = format; }
    }

    /**
     * 导入变量请求
     */
    class ImportVariablesRequest {
        private Long userId;
        private Map<String, Object> variables;
        private String data; // JSON 字符串格式的数据
        private String scope;
        private Long workflowId;
        private Long conversationId;
        private Boolean overwriteExisting = false;
        private Boolean overwrite = false; // 别名
        private Boolean validateBeforeImport = true;

        // Getters and setters
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public Map<String, Object> getVariables() { return variables; }
        public void setVariables(Map<String, Object> variables) { this.variables = variables; }
        public String getData() { return data; }
        public void setData(String data) { this.data = data; }
        public String getScope() { return scope; }
        public void setScope(String scope) { this.scope = scope; }
        public Long getWorkflowId() { return workflowId; }
        public void setWorkflowId(Long workflowId) { this.workflowId = workflowId; }
        public Long getConversationId() { return conversationId; }
        public void setConversationId(Long conversationId) { this.conversationId = conversationId; }
        public Boolean getOverwriteExisting() { return overwriteExisting; }
        public void setOverwriteExisting(Boolean overwriteExisting) { this.overwriteExisting = overwriteExisting; }
        public Boolean isOverwrite() { return overwrite != null ? overwrite : overwriteExisting; }
        public void setOverwrite(Boolean overwrite) { this.overwrite = overwrite; }
        public Boolean getValidateBeforeImport() { return validateBeforeImport; }
        public void setValidateBeforeImport(Boolean validateBeforeImport) { this.validateBeforeImport = validateBeforeImport; }
    }

    /**
     * 导入变量结果
     */
    class ImportVariablesResult {
        private boolean success;
        private int importedCount;
        private int skippedCount;
        private int failedCount;
        private int importedWorkflowVariables;
        private int importedConversationVariables;
        private int skippedVariables;
        private List<String> errors;
        private List<String> warnings;

        // 无参构造函数
        public ImportVariablesResult() {
        }

        public ImportVariablesResult(boolean success, int importedCount, int skippedCount, int failedCount) {
            this.success = success;
            this.importedCount = importedCount;
            this.skippedCount = skippedCount;
            this.failedCount = failedCount;
        }

        // Getters and setters
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public int getImportedCount() { return importedCount; }
        public void setImportedCount(int importedCount) { this.importedCount = importedCount; }
        public int getSkippedCount() { return skippedCount; }
        public void setSkippedCount(int skippedCount) { this.skippedCount = skippedCount; }
        public int getFailedCount() { return failedCount; }
        public void setFailedCount(int failedCount) { this.failedCount = failedCount; }
        public int getImportedWorkflowVariables() { return importedWorkflowVariables; }
        public void setImportedWorkflowVariables(int importedWorkflowVariables) { this.importedWorkflowVariables = importedWorkflowVariables; }
        public int getImportedConversationVariables() { return importedConversationVariables; }
        public void setImportedConversationVariables(int importedConversationVariables) { this.importedConversationVariables = importedConversationVariables; }
        public int getSkippedVariables() { return skippedVariables; }
        public void setSkippedVariables(int skippedVariables) { this.skippedVariables = skippedVariables; }
        public List<String> getErrors() { return errors; }
        public void setErrors(List<String> errors) { this.errors = errors; }
        public List<String> getWarnings() { return warnings; }
        public void setWarnings(List<String> warnings) { this.warnings = warnings; }
    }
}
