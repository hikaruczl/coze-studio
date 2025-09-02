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

package com.coze.studio.dto.conversation;

import com.coze.studio.dto.message.MessageResponse;
import com.coze.studio.entity.Conversation;
import com.coze.studio.entity.enums.ConversationStatus;
import com.coze.studio.entity.enums.Scene;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 对话响应 DTO
 *
 * @author coze-dev
 */
@Data
@lombok.NoArgsConstructor

public class ConversationResponse {

    /**
     * 对话ID
     */
    private Long id;

    /**
     * 会话ID
     */
    private Long sectionId;

    /**
     * Bot ID
     */
    private Long agentId;

    /**
     * Bot 名称
     */
    private String agentName;

    /**
     * 连接器ID
     */
    private Long connectorId;

    /**
     * 创建者ID
     */
    private Long creatorId;

    /**
     * 创建者用户名
     */
    private String creatorUsername;

    /**
     * 场景类型
     */
    private Scene scene;

    /**
     * 对话状态
     */
    private ConversationStatus status;

    /**
     * 对话标题
     */
    private String title;

    /**
     * 对话摘要
     */
    private String summary;

    /**
     * 消息总数
     */
    private Integer messageCount;

    /**
     * 是否置顶
     */
    private Boolean pinned;

    /**
     * 扩展信息
     */
    private Map<String, Object> ext;

    /**
     * 最近的消息列表（可选）
     */
    private List<MessageResponse> recentMessages;

    /**
     * 最后一条消息
     */
    private MessageResponse lastMessage;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 从 Conversation 实体转换为 ConversationResponse
     */
    public static ConversationResponse fromConversation(Conversation conversation) {
        ConversationResponse response = new ConversationResponse();
        response.setId(conversation.getId());
        response.setSectionId(conversation.getSectionId());
        response.setAgentId(conversation.getAgentId());
        response.setConnectorId(conversation.getConnectorId());
        response.setCreatorId(conversation.getCreatorId());
        response.setScene(conversation.getScene());
        response.setStatus(conversation.getStatus());
        response.setTitle(conversation.getTitle());
        response.setSummary(conversation.getSummary());
        response.setMessageCount(conversation.getMessageCount());
        response.setPinned(conversation.isPinned());
        response.setExt(parseExtension(conversation.getExt()));
        response.setCreatedAt(conversation.getCreatedAt());
        response.setUpdatedAt(conversation.getUpdatedAt());
        return response;
    }

    /**
     * 从 Conversation 实体转换为 ConversationResponse（包含额外信息）
     */
    public static ConversationResponse fromConversation(Conversation conversation,
                                                       String agentName,
                                                       String creatorUsername) {
        ConversationResponse response = fromConversation(conversation);
        response.setAgentName(agentName);
        response.setCreatorUsername(creatorUsername);
        return response;
    }

    /**
     * 解析扩展信息
     */
    private static Map<String, Object> parseExtension(String ext) {
        // TODO: 实现 JSON 字符串解析为 Map
        return null;
    }
}
