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

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 工作流变量实体类
 * 用于存储工作流执行过程中的变量和状态信息
 * 
 * @author coze-dev
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("workflow_variables")
public class WorkflowVariable extends BaseEntity {

    /**
     * 变量名称
     */
    @TableField("name")
    private String name;

    /**
     * 变量值（JSON格式存储）
     */
    @TableField("value")
    private String value;

    /**
     * 变量类型
     * STRING, NUMBER, BOOLEAN, OBJECT, ARRAY, NULL
     */
    @TableField("type")
    private String type;

    /**
     * 变量作用域
     * GLOBAL - 全局变量，跨工作流共享
     * WORKFLOW - 工作流级别，同一工作流的所有执行共享
     * EXECUTION - 执行级别，单次执行内共享
     * SESSION - 会话级别，同一会话内共享
     * NODE - 节点级别，单个节点内使用
     */
    @TableField("scope")
    private String scope;

    /**
     * 变量所属用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 关联的工作流ID
     */
    @TableField("workflow_id")
    private Long workflowId;

    /**
     * 关联的执行ID
     */
    @TableField("execution_id")
    private String executionId;

    /**
     * 关联的会话ID
     */
    @TableField("session_id")
    private String sessionId;

    /**
     * 关联的节点ID
     */
    @TableField("node_id")
    private String nodeId;

    /**
     * 变量描述
     */
    @TableField("description")
    private String description;

    /**
     * 是否为敏感变量（如密码、密钥等）
     */
    @TableField("is_sensitive")
    private Boolean isSensitive;

    /**
     * 是否为系统变量
     */
    @TableField("is_system")
    private Boolean isSystem = false;

    /**
     * 是否为只读变量
     */
    @TableField("is_readonly")
    private Boolean isReadonly;

    /**
     * 是否已删除
     */
    @TableField("deleted")
    private Boolean deleted = false;

    /**
     * 标签
     */
    @TableField("tags")
    private String tags;

    /**
     * 优先级
     */
    @TableField("priority")
    private Integer priority;

    /**
     * 版本号
     */
    @TableField("version")
    private String version;

    /**
     * 过期时间
     */
    @TableField("expires_at")
    private LocalDateTime expiresAt;

    /**
     * 访问次数
     */
    @TableField("access_count")
    private Long accessCount;

    /**
     * 最后访问时间
     */
    @TableField("last_accessed_at")
    private LocalDateTime lastAccessedAt;

    /**
     * 元数据（JSON格式）
     */
    @TableField("metadata")
    private String metadata;

    /**
     * 变量类型枚举
     */
    public enum Type {
        STRING("STRING", "字符串"),
        NUMBER("NUMBER", "数字"),
        BOOLEAN("BOOLEAN", "布尔值"),
        OBJECT("OBJECT", "对象"),
        ARRAY("ARRAY", "数组"),
        NULL("NULL", "空值");

        private final String code;
        private final String description;

        Type(String code, String description) {
            this.code = code;
            this.description = description;
        }

        public String getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 变量作用域枚举
     */
    public enum Scope {
        GLOBAL("GLOBAL", "全局作用域"),
        WORKFLOW("WORKFLOW", "工作流作用域"),
        EXECUTION("EXECUTION", "执行作用域"),
        SESSION("SESSION", "会话作用域"),
        NODE("NODE", "节点作用域");

        private final String code;
        private final String description;

        Scope(String code, String description) {
            this.code = code;
            this.description = description;
        }

        public String getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 检查变量是否已过期
     */
    public boolean isExpired() {
        return expiresAt != null && expiresAt.isBefore(LocalDateTime.now());
    }

    /**
     * 检查是否为敏感变量
     */
    public boolean isSensitive() {
        return Boolean.TRUE.equals(isSensitive);
    }

    /**
     * 检查是否为只读变量
     */
    public boolean isReadonly() {
        return Boolean.TRUE.equals(isReadonly);
    }

    /**
     * 增加访问次数
     */
    public void incrementAccessCount() {
        if (accessCount == null) {
            accessCount = 0L;
        }
        accessCount++;
        lastAccessedAt = LocalDateTime.now();
    }

    /**
     * 检查作用域是否匹配
     */
    public boolean matchesScope(String targetScope) {
        return scope != null && scope.equals(targetScope);
    }

    /**
     * 检查是否在指定的工作流中
     */
    public boolean belongsToWorkflow(Long targetWorkflowId) {
        return workflowId != null && workflowId.equals(targetWorkflowId);
    }

    /**
     * 检查是否在指定的执行中
     */
    public boolean belongsToExecution(String targetExecutionId) {
        return executionId != null && executionId.equals(targetExecutionId);
    }

    /**
     * 检查是否在指定的会话中
     */
    public boolean belongsToSession(String targetSessionId) {
        return sessionId != null && sessionId.equals(targetSessionId);
    }

    /**
     * 检查是否属于指定用户
     */
    public boolean belongsToUser(Long targetUserId) {
        return userId != null && userId.equals(targetUserId);
    }

    /**
     * 更新访问信息
     */
    public void updateAccessInfo() {
        if (accessCount == null) {
            accessCount = 0L;
        }
        accessCount++;
        lastAccessedAt = java.time.LocalDateTime.now();
    }


    // Lombok生成的getter/setter方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(Long workflowId) {
        this.workflowId = workflowId;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isIsSensitive() {
        return isSensitive;
    }

    public void setIsSensitive(Boolean isSensitive) {
        this.isSensitive = isSensitive;
    }

    public Boolean isIsSystem() {
        return isSystem;
    }

    public void setIsSystem(Boolean isSystem) {
        this.isSystem = isSystem;
    }

    public Boolean isIsReadonly() {
        return isReadonly;
    }

    public void setIsReadonly(Boolean isReadonly) {
        this.isReadonly = isReadonly;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Long getAccessCount() {
        return accessCount;
    }

    public void setAccessCount(Long accessCount) {
        this.accessCount = accessCount;
    }

    public LocalDateTime getLastAccessedAt() {
        return lastAccessedAt;
    }

    public void setLastAccessedAt(LocalDateTime lastAccessedAt) {
        this.lastAccessedAt = lastAccessedAt;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }
}
