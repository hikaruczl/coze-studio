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

package com.coze.studio.dto.message;

import com.coze.studio.entity.Message;
import com.coze.studio.entity.enums.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 消息响应 DTO
 *
 * @author coze-dev
 */
@lombok.NoArgsConstructor

@Data
public class MessageResponse {

    /**
     * 消息ID
     */
    private Long id;

    /**
     * 对话ID
     */
    private Long conversationId;

    /**
     * 运行ID
     */
    private Long runId;

    /**
     * Bot ID
     */
    private Long agentId;

    /**
     * 会话ID
     */
    private Long sectionId;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 内容类型
     */
    private ContentType contentType;

    /**
     * 角色类型
     */
    private RoleType role;

    /**
     * 发送者名称
     */
    private String name;

    /**
     * 消息状态
     */
    private MessageStatus status;

    /**
     * 消息类型
     */
    private MessageType messageType;

    /**
     * 模型内容
     */
    private String modelContent;

    /**
     * 消息位置
     */
    private Integer position;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 父消息ID
     */
    private Long parentMessageId;

    /**
     * 子消息列表（回复消息）
     */
    private List<MessageResponse> replies;

    /**
     * 工具调用信息
     */
    private List<ToolCall> toolCalls;

    /**
     * 是否已读
     */
    private Boolean isRead;

    /**
     * 扩展信息
     */
    private Map<String, Object> ext;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 从 Message 实体转换为 MessageResponse
     */
    public static MessageResponse fromMessage(Message message) {
        MessageResponse response = new MessageResponse();
        response.setId(message.getId());
        response.setConversationId(message.getConversation().getId());
        response.setRunId(message.getRunId());
        response.setAgentId(message.getAgentId());
        response.setSectionId(message.getSectionId());
        response.setContent(message.getContent());
        response.setContentType(message.getContentType());
        response.setRole(message.getRole());
        response.setName(message.getName());
        response.setStatus(message.getStatus());
        response.setMessageType(message.getMessageType());
        response.setModelContent(message.getModelContent());
        response.setPosition(message.getPosition());
        response.setUserId(message.getUserId());
        response.setParentMessageId(message.getParentMessageId());
        response.setIsRead(message.getIsRead());
        response.setExt(parseExtension(message.getExt()));
        response.setToolCalls(parseToolCalls(message.getToolCalls()));
        response.setCreatedAt(message.getCreatedAt());
        response.setUpdatedAt(message.getUpdatedAt());
        return response;
    }

    /**
     * 解析扩展信息
     */
    private static Map<String, Object> parseExtension(String ext) {
        // TODO: 实现 JSON 字符串解析为 Map
        return null;
    }

    /**
     * 解析工具调用信息
     */
    private static List<ToolCall> parseToolCalls(String toolCalls) {
        // TODO: 实现 JSON 字符串解析为 ToolCall 列表
        return null;
    }

    /**
     * 工具调用信息
     */
    @Data
    public static class ToolCall {
        private String id;
        private String type;
        private String name;
        private Map<String, Object> arguments;
        private String result;
        private String status;
    }
}
