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

import com.coze.studio.entity.enums.ConversationStatus;
import com.coze.studio.entity.enums.Scene;
// JPA注解已移除，改用MyBatis
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 对话实体类
 * 表示一次完整的对话会话
 * 
 * @author coze-dev
 */
@Data
@EqualsAndHashCode(callSuper = true)
// 表名: conversations
public class Conversation extends BaseEntity {

    /**
     * 会话ID
     */
        private Long sectionId;

    /**
     * 代理ID（Bot ID）
     */
        private Long agentId;

    /**
     * 连接器ID
     */
        private Long connectorId;

    /**
     * 创建者ID
     */
        private Long creatorId;

    /**
     * 场景类型
     */
            private Scene scene;

    /**
     * 对话状态
     */
            private ConversationStatus status;

    /**
     * 扩展信息（JSON格式）
     */
        private String ext;

    /**
     * 对话标题
     */
        private String title;

    /**
     * 对话摘要
     */
        private String summary;

    /**
     * 消息列表
     */
        private List<Message> messages;

    /**
     * 消息总数
     */
        private Integer messageCount = 0;

    /**
     * 是否置顶
     */
        private Boolean pinned = false;


    // Lombok生成的getter/setter方法
    public Long getSectionId() {
        return sectionId;
    }

    public void setSectionId(Long sectionId) {
        this.sectionId = sectionId;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public Long getConnectorId() {
        return connectorId;
    }

    public void setConnectorId(Long connectorId) {
        this.connectorId = connectorId;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public ConversationStatus getStatus() {
        return status;
    }

    public void setStatus(ConversationStatus status) {
        this.status = status;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Integer getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(Integer messageCount) {
        this.messageCount = messageCount;
    }

    public Boolean isPinned() {
        return pinned;
    }

    public void setPinned(Boolean pinned) {
        this.pinned = pinned;
    }
}
