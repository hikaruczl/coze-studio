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
 * 对话变量实体类
 * 用于存储对话过程中的上下文变量和状态信息
 *
 * @author coze-dev
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("conversation_variables")
public class ConversationVariable extends BaseEntity {

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
     * GLOBAL - 全局变量，跨对话共享
     * BOT - Bot级别，同一Bot的所有对话共享
     * CONVERSATION - 对话级别，单个对话内共享
     * SESSION - 会话级别，同一会话内共享
     * MESSAGE - 消息级别，单条消息内使用
     */
    @TableField("scope")
    private String scope;

    /**
     * 关联的对话ID
     */
    @TableField("conversation_id")
    private Long conversationId;

    /**
     * 关联的会话ID
     */
    @TableField("session_id")
    private String sessionId;

    /**
     * 关联的消息ID
     */
    @TableField("message_id")
    private Long messageId;

    /**
     * 关联的Bot ID
     */
    @TableField("bot_id")
    private Long botId;

    /**
     * 变量所属用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 变量描述
     */
    @TableField("description")
    private String description;

    /**
     * 是否为敏感变量（如用户信息、密钥等）
     */
    @TableField("is_sensitive")
    private Boolean isSensitive = false;

    /**
     * 是否为只读变量
     */
    @TableField("is_readonly")
    private Boolean isReadonly = false;

    /**
     * 是否为系统变量
     */
    @TableField("is_system")
    private Boolean isSystem = false;

    /**
     * 是否已删除
     */
    @TableField("deleted")
    private Boolean deleted = false;

    /**
     * 变量过期时间
     */
    @TableField("expires_at")
    private LocalDateTime expiresAt;

    /**
     * 变量最后访问时间
     */
    @TableField("last_accessed_at")
    private LocalDateTime lastAccessedAt;

    /**
     * 变量访问次数
     */
    @TableField("access_count")
    private Long accessCount = 0L;

    /**
     * 变量元数据（JSON格式）
     */
    @TableField("metadata")
    private String metadata;

    /**
     * 标签
     */
    @TableField("tags")
    private String tags;

    /**
     * 版本号
     */
    @TableField("version")
    private Integer version;

    /**
     * 父变量ID
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 变量优先级（数字越小优先级越高）
     */
    @TableField("priority")
    private Integer priority = 0;

    /**
     * 是否启用
     */
    @TableField("enabled")
    private Boolean enabled = true;

    /**
     * 变量来源
     * USER_INPUT - 用户输入
     * BOT_OUTPUT - Bot输出
     * SYSTEM - 系统生成
     * EXTERNAL - 外部接口
     */
    @TableField("source")
    private String source;

    /**
     * 变量作用域枚举
     */
    public enum Scope {
        GLOBAL("GLOBAL", "全局变量"),
        BOT("BOT", "Bot变量"),
        CONVERSATION("CONVERSATION", "对话变量"),
        SESSION("SESSION", "会话变量"),
        MESSAGE("MESSAGE", "消息变量");

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
     * 变量来源枚举
     */
    public enum Source {
        USER_INPUT("USER_INPUT", "用户输入"),
        BOT_OUTPUT("BOT_OUTPUT", "Bot输出"),
        SYSTEM("SYSTEM", "系统生成"),
        EXTERNAL("EXTERNAL", "外部接口");

        private final String code;
        private final String description;

        Source(String code, String description) {
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
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }

    /**
     * 更新最后访问时间和访问次数
     */
    public void updateAccessInfo() {
        this.lastAccessedAt = LocalDateTime.now();
        this.accessCount++;
    }

    /**
     * 检查是否为全局变量
     */
    public boolean isGlobal() {
        return Scope.GLOBAL.getCode().equals(this.scope);
    }

    /**
     * 检查是否为Bot变量
     */
    public boolean isBotScope() {
        return Scope.BOT.getCode().equals(this.scope);
    }

    /**
     * 检查是否为对话变量
     */
    public boolean isConversationScope() {
        return Scope.CONVERSATION.getCode().equals(this.scope);
    }

    /**
     * 检查是否为会话变量
     */
    public boolean isSessionScope() {
        return Scope.SESSION.getCode().equals(this.scope);
    }

    /**
     * 检查是否为消息变量
     */
    public boolean isMessageScope() {
        return Scope.MESSAGE.getCode().equals(this.scope);
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

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getBotId() {
        return botId;
    }

    public void setBotId(Long botId) {
        this.botId = botId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Boolean isIsSensitive() {
        return isSensitive;
    }

    public void setIsSensitive(Boolean isSensitive) {
        this.isSensitive = isSensitive;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public LocalDateTime getLastAccessedAt() {
        return lastAccessedAt;
    }

    public void setLastAccessedAt(LocalDateTime lastAccessedAt) {
        this.lastAccessedAt = lastAccessedAt;
    }

    public Long getAccessCount() {
        return accessCount;
    }

    public void setAccessCount(Long accessCount) {
        this.accessCount = accessCount;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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
     * 检查是否在指定的对话中
     */
    public boolean belongsToConversation(Long targetConversationId) {
        return conversationId != null && conversationId.equals(targetConversationId);
    }

    /**
     * 检查是否在指定的会话中
     */
    public boolean belongsToSession(String targetSessionId) {
        return sessionId != null && sessionId.equals(targetSessionId);
    }

    /**
     * 检查是否属于指定Bot
     */
    public boolean belongsToBot(Long targetBotId) {
        return botId != null && botId.equals(targetBotId);
    }

    /**
     * 检查是否属于指定用户
     */
    public boolean belongsToUser(Long targetUserId) {
        return userId != null && userId.equals(targetUserId);
    }
}
