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

package com.coze.studio.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coze.studio.entity.ConversationVariable;
import com.coze.studio.entity.WorkflowVariable;
import com.coze.studio.exception.BusinessException;
import com.coze.studio.exception.ErrorCode;
import com.coze.studio.mapper.ConversationVariableMapper;
import com.coze.studio.mapper.WorkflowVariableMapper;
import com.coze.studio.service.VariableManagementService;
import com.coze.studio.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 变量管理服务实现类
 *
 * @author coze-dev
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VariableManagementServiceImpl implements VariableManagementService {

    private final WorkflowVariableMapper workflowVariableMapper;
    private final ConversationVariableMapper conversationVariableMapper;

    // 变量引用的正则表达式模式
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\$\\{([^}]+)\\}");

    // ==================== 工作流变量管理 ====================

    @Override
    @Transactional
    public WorkflowVariable setWorkflowVariable(SetWorkflowVariableRequest request) {
        log.info("设置工作流变量: name={}, scope={}, userId={}",
                request.getName(), request.getScope(), request.getUserId());

        // 查找现有变量
        Optional<WorkflowVariable> existingVariable = findWorkflowVariableByScope(
                request.getName(), request.getScope(), request.getWorkflowId(),
                request.getExecutionId(), request.getSessionId(), request.getNodeId());

        WorkflowVariable variable;
        if (existingVariable.isPresent()) {
            // 更新现有变量
            variable = existingVariable.get();
            variable.setValue(JsonUtil.toJson(request.getValue()));
            variable.setType(determineValueType(request.getValue()));
            variable.setDescription(request.getDescription());
            variable.setExpiresAt(request.getExpiresAt());
            workflowVariableMapper.updateById(variable);
        } else {
            // 创建新变量
            variable = new WorkflowVariable();
            variable.setName(request.getName());
            variable.setValue(JsonUtil.toJson(request.getValue()));
            variable.setType(determineValueType(request.getValue()));
            variable.setScope(request.getScope());
            variable.setUserId(request.getUserId());
            variable.setWorkflowId(request.getWorkflowId());
            variable.setExecutionId(request.getExecutionId());
            variable.setSessionId(request.getSessionId());
            variable.setNodeId(request.getNodeId());
            variable.setDescription(request.getDescription());
            variable.setIsSensitive(request.getIsSensitive());
            variable.setIsReadonly(request.getIsReadonly());
            variable.setExpiresAt(request.getExpiresAt());
            variable.setAccessCount(0L);
            workflowVariableMapper.insert(variable);
        }

        return variable;
    }

    @Override
    public Optional<Object> getWorkflowVariable(GetWorkflowVariableRequest request) {
        log.debug("获取工作流变量: name={}, scope={}, userId={}",
                request.getName(), request.getScope(), request.getUserId());

        Optional<WorkflowVariable> variable = findWorkflowVariableByScope(
                request.getName(), request.getScope(), request.getWorkflowId(),
                request.getExecutionId(), request.getSessionId(), request.getNodeId());

        if (variable.isPresent()) {
            WorkflowVariable var = variable.get();

            // 检查是否过期
            if (var.isExpired()) {
                log.warn("变量已过期: name={}, expiresAt={}", var.getName(), var.getExpiresAt());
                return Optional.empty();
            }

            // 更新访问信息
            var.incrementAccessCount();
            workflowVariableMapper.updateAccessStats(var.getId(), LocalDateTime.now());

            // 返回解析后的值
            return Optional.of(JsonUtil.fromJson(var.getValue(), Object.class));
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public boolean deleteWorkflowVariable(DeleteWorkflowVariableRequest request) {
        log.info("删除工作流变量: name={}, scope={}, userId={}",
                request.getName(), request.getScope(), request.getUserId());

        Optional<WorkflowVariable> variable = findWorkflowVariableByScope(
                request.getName(), request.getScope(), request.getWorkflowId(),
                request.getExecutionId(), request.getSessionId(), request.getNodeId());

        if (variable.isPresent()) {
            WorkflowVariable var = variable.get();

            // 检查是否为只读变量
            if (var.isReadonly()) {
                throw new BusinessException(ErrorCode.VARIABLE_READONLY, "变量为只读，不能删除");
            }

            // 软删除：设置删除标记
            var.setDeleted(true);
            workflowVariableMapper.updateById(var);
            return true;
        }

        return false;
    }

    @Override
    public List<WorkflowVariable> getWorkflowVariables(GetWorkflowVariablesRequest request) {
        log.debug("获取工作流变量列表: scope={}, userId={}", request.getScope(), request.getUserId());

        List<WorkflowVariable> variables = new ArrayList<>();

        if (request.getScope() != null) {
            variables = workflowVariableMapper.findByScopeAndUserId(request.getScope(), request.getUserId());
        } else if (request.getWorkflowId() != null) {
            variables = workflowVariableMapper.findByWorkflowId(request.getWorkflowId());
        } else if (request.getExecutionId() != null) {
            variables = workflowVariableMapper.findByExecutionId(request.getExecutionId());
        } else if (request.getSessionId() != null) {
            variables = workflowVariableMapper.findBySessionId(request.getSessionId());
        } else if (request.getNodeId() != null) {
            variables = workflowVariableMapper.findByNodeId(request.getNodeId());
        } else if (request.getUserId() != null) {
            QueryWrapper<WorkflowVariable> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", request.getUserId())
                       .eq("deleted", false)
                       .orderByAsc("created_at");
            variables = workflowVariableMapper.selectList(queryWrapper);
        }

        return variables;
    }

    @Override
    @Transactional
    public void clearWorkflowVariables(ClearWorkflowVariablesRequest request) {
        log.info("清理工作流变量: scope={}, workflowId={}, userId={}",
                request.getScope(), request.getWorkflowId(), request.getUserId());

        if (request.getScope() != null) {
            workflowVariableMapper.deleteByScope(request.getScope(), request.getUserId());
        } else if (request.getWorkflowId() != null) {
            workflowVariableMapper.deleteByWorkflowId(request.getWorkflowId());
        } else if (request.getExecutionId() != null) {
            workflowVariableMapper.deleteByExecutionId(request.getExecutionId());
        } else if (request.getSessionId() != null) {
            workflowVariableMapper.deleteBySessionId(request.getSessionId());
        }
    }

    // ==================== 对话变量管理 ====================

    @Override
    @Transactional
    public ConversationVariable setConversationVariable(SetConversationVariableRequest request) {
        log.info("设置对话变量: name={}, scope={}, userId={}",
                request.getName(), request.getScope(), request.getUserId());

        // 查找现有变量
        Optional<ConversationVariable> existingVariable = findConversationVariableByScope(
                request.getName(), request.getScope(), request.getConversationId(),
                request.getSessionId(), request.getMessageId(), request.getBotId());

        ConversationVariable variable;
        if (existingVariable.isPresent()) {
            // 更新现有变量
            variable = existingVariable.get();
            variable.setValue(JsonUtil.toJson(request.getValue()));
            variable.setType(determineValueType(request.getValue()));
            variable.setDescription(request.getDescription());
            variable.setExpiresAt(request.getExpiresAt());
            conversationVariableMapper.updateById(variable);
        } else {
            // 创建新变量
            variable = new ConversationVariable();
            variable.setName(request.getName());
            variable.setValue(JsonUtil.toJson(request.getValue()));
            variable.setType(determineValueType(request.getValue()));
            variable.setScope(request.getScope());
            variable.setUserId(request.getUserId());
            variable.setBotId(request.getBotId());
            variable.setConversationId(request.getConversationId());
            variable.setSessionId(request.getSessionId());
            variable.setMessageId(request.getMessageId());
            variable.setDescription(request.getDescription());
            variable.setIsSensitive(request.getIsSensitive());
            variable.setIsReadonly(request.getIsReadonly());
            variable.setExpiresAt(request.getExpiresAt());
            variable.setAccessCount(0L);
            conversationVariableMapper.insert(variable);
        }

        return variable;
    }

    @Override
    public Optional<Object> getConversationVariable(GetConversationVariableRequest request) {
        log.debug("获取对话变量: name={}, scope={}, userId={}",
                request.getName(), request.getScope(), request.getUserId());

        Optional<ConversationVariable> variable = findConversationVariableByScope(
                request.getName(), request.getScope(), request.getConversationId(),
                request.getSessionId(), request.getMessageId(), request.getBotId());

        if (variable.isPresent()) {
            ConversationVariable var = variable.get();

            // 检查是否过期
            if (var.isExpired()) {
                log.warn("变量已过期: name={}, expiresAt={}", var.getName(), var.getExpiresAt());
                return Optional.empty();
            }

            // 更新访问信息
            var.incrementAccessCount();
            conversationVariableMapper.updateAccessStats(var.getId(), LocalDateTime.now());

            // 返回解析后的值
            return Optional.of(JsonUtil.fromJson(var.getValue(), Object.class));
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public boolean deleteConversationVariable(DeleteConversationVariableRequest request) {
        log.info("删除对话变量: name={}, scope={}, userId={}",
                request.getName(), request.getScope(), request.getUserId());

        Optional<ConversationVariable> variable = findConversationVariableByScope(
                request.getName(), request.getScope(), request.getConversationId(),
                request.getSessionId(), request.getMessageId(), request.getBotId());

        if (variable.isPresent()) {
            ConversationVariable var = variable.get();

            // 检查是否为只读变量
            if (var.isReadonly()) {
                throw new BusinessException(ErrorCode.VARIABLE_READONLY, "变量为只读，不能删除");
            }

            // 软删除：设置删除标记
            var.setDeleted(true);
            conversationVariableMapper.updateById(var);
            return true;
        }

        return false;
    }

    @Override
    public List<ConversationVariable> getConversationVariables(GetConversationVariablesRequest request) {
        log.debug("获取对话变量列表: scope={}, userId={}", request.getScope(), request.getUserId());

        List<ConversationVariable> variables = new ArrayList<>();

        if (request.getScope() != null) {
            variables = conversationVariableMapper.findByScopeAndUserId(request.getScope(), request.getUserId());
        } else if (request.getBotId() != null) {
            variables = conversationVariableMapper.findByBotId(request.getBotId());
        } else if (request.getConversationId() != null) {
            variables = conversationVariableMapper.findByConversationId(request.getConversationId());
        } else if (request.getSessionId() != null) {
            variables = conversationVariableMapper.findBySessionId(request.getSessionId());
        } else if (request.getMessageId() != null) {
            variables = conversationVariableMapper.findByMessageId(request.getMessageId());
        } else if (request.getUserId() != null) {
            QueryWrapper<ConversationVariable> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", request.getUserId())
                       .eq("deleted", false)
                       .orderByAsc("created_at");
            variables = conversationVariableMapper.selectList(queryWrapper);
        }

        return variables;
    }

    @Override
    @Transactional
    public void clearConversationVariables(ClearConversationVariablesRequest request) {
        log.info("清理对话变量: scope={}, conversationId={}, userId={}",
                request.getScope(), request.getConversationId(), request.getUserId());

        if (request.getScope() != null) {
            conversationVariableMapper.deleteByScope(request.getScope(), request.getUserId());
        } else if (request.getBotId() != null) {
            conversationVariableMapper.deleteByBotId(request.getBotId());
        } else if (request.getConversationId() != null) {
            conversationVariableMapper.deleteByConversationId(request.getConversationId());
        } else if (request.getSessionId() != null) {
            conversationVariableMapper.deleteBySessionId(request.getSessionId());
        }
    }

    // ==================== 变量解析和渲染 ====================

    @Override
    public String parseVariables(String text, VariableContext context) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        log.debug("解析变量: text={}, context={}", text, context);

        Matcher matcher = VARIABLE_PATTERN.matcher(text);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String variableName = matcher.group(1);
            Object value = getVariableValue(variableName, context);
            String replacement = value != null ? value.toString() : "${" + variableName + "}";
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(result);

        return result.toString();
    }

    @Override
    public String renderTemplate(String template, VariableContext context) {
        if (template == null || template.isEmpty()) {
            return template;
        }

        log.debug("渲染模板: template={}, context={}", template, context);

        // 使用parseVariables方法进行变量替换
        return parseVariables(template, context);
    }

    @Override
    public List<String> extractVariableReferences(String text) {
        if (text == null || text.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> variables = new ArrayList<>();
        Matcher matcher = VARIABLE_PATTERN.matcher(text);

        while (matcher.find()) {
            String variableName = matcher.group(1);
            if (!variables.contains(variableName)) {
                variables.add(variableName);
            }
        }

        return variables;
    }

    @Override
    public boolean validateVariableExpression(String expression) {
        if (expression == null || expression.isEmpty()) {
            return false;
        }

        // 检查变量名是否符合规范（字母、数字、下划线、点号）
        return expression.matches("^[a-zA-Z_][a-zA-Z0-9_.]*$");
    }

    // ==================== 变量统计和管理 ====================

    @Override
    public VariableStatistics getVariableStatistics(Long userId) {
        log.debug("获取变量统计: userId={}", userId);

        VariableStatistics statistics = new VariableStatistics();
        statistics.setUserId(userId);

        // 统计工作流变量
        long workflowVariableCount = workflowVariableMapper.countByUserId(userId);
        statistics.setWorkflowVariableCount(workflowVariableCount);

        // 统计对话变量
        long conversationVariableCount = conversationVariableMapper.countByUserId(userId);
        statistics.setConversationVariableCount(conversationVariableCount);

        // 统计总数
        statistics.setTotalVariableCount(workflowVariableCount + conversationVariableCount);

        // 获取详细统计
        Map<String, Object> workflowStats = workflowVariableMapper.getVariableStatistics(userId);
        Map<String, Object> conversationStats = conversationVariableMapper.getVariableStatistics(userId);

        // 处理统计数据
        Map<String, Long> scopeStats = new HashMap<>();
        Map<String, Long> typeStats = new HashMap<>();

        // 处理工作流变量统计
        if (workflowStats != null) {
            extractStatsFromMap(workflowStats, scopeStats, typeStats);
        }

        // 处理对话变量统计
        if (conversationStats != null) {
            extractStatsFromMap(conversationStats, scopeStats, typeStats);
        }

        statistics.setVariablesByScope(scopeStats);
        statistics.setVariablesByType(typeStats);

        return statistics;
    }

    @Override
    @Transactional
    public void cleanupExpiredVariables() {
        log.info("开始清理过期变量");

        LocalDateTime now = LocalDateTime.now();

        // 删除过期的工作流变量
        int deletedWorkflowVars = workflowVariableMapper.deleteExpiredVariables(now);

        // 删除过期的对话变量
        int deletedConversationVars = conversationVariableMapper.deleteExpiredVariables(now);

        log.info("清理完成: 工作流变量={}, 对话变量={}", deletedWorkflowVars, deletedConversationVars);
    }

    @Override
    @Transactional
    public void cleanupUnusedVariables(LocalDateTime threshold) {
        log.info("开始清理未使用变量: threshold={}", threshold);

        // 删除长时间未使用的变量（通过查询条件删除）
        QueryWrapper<WorkflowVariable> workflowQuery = new QueryWrapper<>();
        workflowQuery.lt("last_accessed_at", threshold).eq("deleted", false);
        List<WorkflowVariable> unusedWorkflowVars = workflowVariableMapper.selectList(workflowQuery);
        for (WorkflowVariable var : unusedWorkflowVars) {
            var.setDeleted(true);
            workflowVariableMapper.updateById(var);
        }

        QueryWrapper<ConversationVariable> conversationQuery = new QueryWrapper<>();
        conversationQuery.lt("last_accessed_at", threshold).eq("deleted", false);
        List<ConversationVariable> unusedConversationVars = conversationVariableMapper.selectList(conversationQuery);
        for (ConversationVariable var : unusedConversationVars) {
            var.setDeleted(true);
            conversationVariableMapper.updateById(var);
        }

        log.info("清理完成: 工作流变量={}, 对话变量={}",
                unusedWorkflowVars.size(), unusedConversationVars.size());
    }

    @Override
    public Map<String, Object> exportVariables(ExportVariablesRequest request) {
        log.info("导出变量: userId={}, includeWorkflow={}, includeConversation={}",
                request.getUserId(), request.isIncludeWorkflow(), request.isIncludeConversation());

        Map<String, Object> exportData = new HashMap<>();
        exportData.put("exportTime", LocalDateTime.now());
        exportData.put("userId", request.getUserId());

        if (request.isIncludeWorkflow()) {
            QueryWrapper<WorkflowVariable> workflowQuery = new QueryWrapper<>();
            workflowQuery.eq("user_id", request.getUserId()).eq("deleted", false);
            List<WorkflowVariable> workflowVariables = workflowVariableMapper.selectList(workflowQuery);
            exportData.put("workflowVariables", workflowVariables);
        }

        if (request.isIncludeConversation()) {
            QueryWrapper<ConversationVariable> conversationQuery = new QueryWrapper<>();
            conversationQuery.eq("user_id", request.getUserId()).eq("deleted", false);
            List<ConversationVariable> conversationVariables = conversationVariableMapper.selectList(conversationQuery);
            exportData.put("conversationVariables", conversationVariables);
        }

        return exportData;
    }

    @Override
    @Transactional
    public ImportVariablesResult importVariables(ImportVariablesRequest request) {
        log.info("导入变量: userId={}, overwrite={}", request.getUserId(), request.isOverwrite());

        ImportVariablesResult result = new ImportVariablesResult();
        result.setSuccess(true);
        result.setImportedWorkflowVariables(0);
        result.setImportedConversationVariables(0);
        result.setSkippedVariables(0);
        result.setErrors(new ArrayList<>());

        try {
            Map<String, Object> importData = JsonUtil.fromJson(request.getData(), Map.class);

            // 导入工作流变量（避免未经检查的泛型转换）
            if (importData.containsKey("workflowVariables")) {
                Object wfObj = importData.get("workflowVariables");
                if (wfObj instanceof java.util.List<?> wfList) {
                    for (Object o : wfList) {
                        if (o instanceof java.util.Map<?, ?> raw) {
                            java.util.Map<String, Object> varData = new java.util.HashMap<>();
                            for (java.util.Map.Entry<?, ?> entry : raw.entrySet()) {
                                varData.put(String.valueOf(entry.getKey()), entry.getValue());
                            }
                            try {
                                importWorkflowVariable(varData, request.getUserId(), request.isOverwrite());
                                result.setImportedWorkflowVariables(result.getImportedWorkflowVariables() + 1);
                            } catch (Exception e) {
                                result.getErrors().add("导入工作流变量失败: " + varData.get("name") + " - " + e.getMessage());
                                result.setSkippedVariables(result.getSkippedVariables() + 1);
                            }
                        }
                    }
                }
            }

            // 导入对话变量（避免未经检查的泛型转换）
            if (importData.containsKey("conversationVariables")) {
                Object cvObj = importData.get("conversationVariables");
                if (cvObj instanceof java.util.List<?> cvList) {
                    for (Object o : cvList) {
                        if (o instanceof java.util.Map<?, ?> raw) {
                            java.util.Map<String, Object> varData = new java.util.HashMap<>();
                            for (java.util.Map.Entry<?, ?> entry : raw.entrySet()) {
                                varData.put(String.valueOf(entry.getKey()), entry.getValue());
                            }
                            try {
                                importConversationVariable(varData, request.getUserId(), request.isOverwrite());
                                result.setImportedConversationVariables(result.getImportedConversationVariables() + 1);
                            } catch (Exception e) {
                                result.getErrors().add("导入对话变量失败: " + varData.get("name") + " - " + e.getMessage());
                                result.setSkippedVariables(result.getSkippedVariables() + 1);
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            log.error("导入变量失败", e);
            result.setSuccess(false);
            result.getErrors().add("导入失败: " + e.getMessage());
        }

        return result;
    }

    // ==================== 私有辅助方法 ====================

    /**
     * 从统计Map中提取作用域和类型统计信息
     */
    private void extractStatsFromMap(Map<String, Object> statsMap, Map<String, Long> scopeStats, Map<String, Long> typeStats) {
        // 从 MyBatis 返回的 Map 中提取统计信息
        // 根据 XML 查询的结果结构来解析
        if (statsMap.containsKey("global_count")) {
            scopeStats.put("GLOBAL", ((Number) statsMap.get("global_count")).longValue());
        }
        if (statsMap.containsKey("workflow_count")) {
            scopeStats.put("WORKFLOW", ((Number) statsMap.get("workflow_count")).longValue());
        }
        if (statsMap.containsKey("execution_count")) {
            scopeStats.put("EXECUTION", ((Number) statsMap.get("execution_count")).longValue());
        }
        if (statsMap.containsKey("session_count")) {
            scopeStats.put("SESSION", ((Number) statsMap.get("session_count")).longValue());
        }
        if (statsMap.containsKey("node_count")) {
            scopeStats.put("NODE", ((Number) statsMap.get("node_count")).longValue());
        }
        if (statsMap.containsKey("bot_count")) {
            scopeStats.put("BOT", ((Number) statsMap.get("bot_count")).longValue());
        }
        if (statsMap.containsKey("conversation_count")) {
            scopeStats.put("CONVERSATION", ((Number) statsMap.get("conversation_count")).longValue());
        }
        if (statsMap.containsKey("message_count")) {
            scopeStats.put("MESSAGE", ((Number) statsMap.get("message_count")).longValue());
        }

        // 类型统计
        if (statsMap.containsKey("string_count")) {
            typeStats.put("STRING", ((Number) statsMap.get("string_count")).longValue());
        }
        if (statsMap.containsKey("number_count")) {
            typeStats.put("NUMBER", ((Number) statsMap.get("number_count")).longValue());
        }
        if (statsMap.containsKey("boolean_count")) {
            typeStats.put("BOOLEAN", ((Number) statsMap.get("boolean_count")).longValue());
        }
        if (statsMap.containsKey("object_count")) {
            typeStats.put("OBJECT", ((Number) statsMap.get("object_count")).longValue());
        }
        if (statsMap.containsKey("array_count")) {
            typeStats.put("ARRAY", ((Number) statsMap.get("array_count")).longValue());
        }
    }

    /**
     * 根据作用域查找工作流变量
     */
    private Optional<WorkflowVariable> findWorkflowVariableByScope(String name, String scope,
                                                                 Long workflowId, String executionId,
                                                                 String sessionId, String nodeId) {
        QueryWrapper<WorkflowVariable> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name)
                   .eq("scope", scope)
                   .eq("deleted", false);

        switch (scope) {
            case "GLOBAL":
                // 全局变量不需要额外条件
                break;
            case "WORKFLOW":
                queryWrapper.eq("workflow_id", workflowId);
                break;
            case "EXECUTION":
                queryWrapper.eq("execution_id", executionId);
                break;
            case "SESSION":
                queryWrapper.eq("session_id", sessionId);
                break;
            case "NODE":
                queryWrapper.eq("node_id", nodeId);
                break;
            default:
                return Optional.empty();
        }

        WorkflowVariable variable = workflowVariableMapper.selectOne(queryWrapper);
        return Optional.ofNullable(variable);
    }

    /**
     * 根据作用域查找对话变量
     */
    private Optional<ConversationVariable> findConversationVariableByScope(String name, String scope,
                                                                          Long conversationId, String sessionId,
                                                                          Long messageId, Long botId) {
        QueryWrapper<ConversationVariable> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name)
                   .eq("scope", scope)
                   .eq("deleted", false);

        switch (scope) {
            case "GLOBAL":
                // 全局变量不需要额外条件
                break;
            case "BOT":
                queryWrapper.eq("bot_id", botId);
                break;
            case "CONVERSATION":
                queryWrapper.eq("conversation_id", conversationId);
                break;
            case "SESSION":
                queryWrapper.eq("session_id", sessionId);
                break;
            case "MESSAGE":
                queryWrapper.eq("message_id", messageId);
                break;
            default:
                return Optional.empty();
        }

        ConversationVariable variable = conversationVariableMapper.selectOne(queryWrapper);
        return Optional.ofNullable(variable);
    }

    /**
     * 确定值的类型
     */
    private String determineValueType(Object value) {
        if (value == null) {
            return "NULL";
        } else if (value instanceof String) {
            return "STRING";
        } else if (value instanceof Number) {
            return "NUMBER";
        } else if (value instanceof Boolean) {
            return "BOOLEAN";
        } else if (value instanceof List) {
            return "ARRAY";
        } else if (value instanceof Map) {
            return "OBJECT";
        } else {
            return "STRING"; // 默认为字符串
        }
    }

    /**
     * 获取变量值
     */
    private Object getVariableValue(String variableName, VariableContext context) {
        // 首先尝试从工作流变量中获取
        if (context.getWorkflowId() != null || context.getExecutionId() != null ||
            context.getSessionId() != null || context.getNodeId() != null) {

            GetWorkflowVariableRequest request = new GetWorkflowVariableRequest();
            request.setName(variableName);
            request.setUserId(context.getUserId());
            request.setWorkflowId(context.getWorkflowId());
            request.setExecutionId(context.getExecutionId());
            request.setSessionId(context.getSessionId());
            request.setNodeId(context.getNodeId());

            Optional<Object> value = getWorkflowVariable(request);
            if (value.isPresent()) {
                return value.get();
            }
        }

        // 然后尝试从对话变量中获取
        if (context.getBotId() != null || context.getConversationId() != null ||
            context.getSessionId() != null || context.getMessageId() != null) {

            GetConversationVariableRequest request = new GetConversationVariableRequest();
            request.setName(variableName);
            request.setUserId(context.getUserId());
            request.setBotId(context.getBotId());
            request.setConversationId(context.getConversationId());
            request.setSessionId(context.getSessionId());
            request.setMessageId(context.getMessageId());

            Optional<Object> value = getConversationVariable(request);
            if (value.isPresent()) {
                return value.get();
            }
        }

        return null;
    }

    /**
     * 导入工作流变量
     */
    private void importWorkflowVariable(Map<String, Object> varData, Long userId, boolean overwrite) {
        String name = (String) varData.get("name");
        String scope = (String) varData.get("scope");

        // 检查是否已存在
        QueryWrapper<WorkflowVariable> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name)
                   .eq("scope", scope)
                   .eq("user_id", userId)
                   .eq("deleted", false);

        WorkflowVariable existing = workflowVariableMapper.selectOne(queryWrapper);

        if (existing != null && !overwrite) {
            throw new BusinessException(ErrorCode.VARIABLE_ALREADY_EXISTS, "变量已存在: " + name);
        }

        WorkflowVariable variable = existing != null ? existing : new WorkflowVariable();
        variable.setName(name);
        variable.setValue((String) varData.get("value"));
        variable.setType((String) varData.get("type"));
        variable.setScope(scope);
        variable.setUserId(userId);
        variable.setWorkflowId((Long) varData.get("workflowId"));
        variable.setExecutionId((String) varData.get("executionId"));
        variable.setSessionId((String) varData.get("sessionId"));
        variable.setNodeId((String) varData.get("nodeId"));
        variable.setDescription((String) varData.get("description"));
        variable.setIsSensitive((Boolean) varData.get("isSensitive"));
        variable.setIsReadonly((Boolean) varData.get("isReadonly"));
        variable.setAccessCount(0L);

        if (existing != null) {
            workflowVariableMapper.updateById(variable);
        } else {
            workflowVariableMapper.insert(variable);
        }
    }

    /**
     * 导入对话变量
     */
    private void importConversationVariable(Map<String, Object> varData, Long userId, boolean overwrite) {
        String name = (String) varData.get("name");
        String scope = (String) varData.get("scope");

        // 检查是否已存在
        QueryWrapper<ConversationVariable> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name)
                   .eq("scope", scope)
                   .eq("user_id", userId)
                   .eq("deleted", false);

        ConversationVariable existing = conversationVariableMapper.selectOne(queryWrapper);

        if (existing != null && !overwrite) {
            throw new BusinessException(ErrorCode.VARIABLE_ALREADY_EXISTS, "变量已存在: " + name);
        }

        ConversationVariable variable = existing != null ? existing : new ConversationVariable();
        variable.setName(name);
        variable.setValue((String) varData.get("value"));
        variable.setType((String) varData.get("type"));
        variable.setScope(scope);
        variable.setUserId(userId);
        variable.setBotId((Long) varData.get("botId"));
        variable.setConversationId((Long) varData.get("conversationId"));
        variable.setSessionId((String) varData.get("sessionId"));
        variable.setMessageId((Long) varData.get("messageId"));
        variable.setDescription((String) varData.get("description"));
        variable.setIsSensitive((Boolean) varData.get("isSensitive"));
        variable.setIsReadonly((Boolean) varData.get("isReadonly"));
        variable.setAccessCount(0L);

        if (existing != null) {
            conversationVariableMapper.updateById(variable);
        } else {
            conversationVariableMapper.insert(variable);
        }
    }
}