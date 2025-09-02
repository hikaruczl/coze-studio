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

import com.coze.studio.entity.enums.ContentType;
import com.coze.studio.entity.enums.MessageType;
import com.coze.studio.entity.enums.RoleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Map;

/**
 * 发送消息请求 DTO
 * 
 * @author coze-dev
 */
@Data
public class SendMessageRequest {

    /**
     * 对话ID
     */
    @NotNull(message = "对话ID不能为空")
    private Long conversationId;

    /**
     * 消息内容
     */
    @NotBlank(message = "消息内容不能为空")
    @Size(max = 10000, message = "消息内容长度不能超过10000个字符")
    private String content;

    /**
     * 内容类型
     */
    private ContentType contentType = ContentType.TEXT;

    /**
     * 角色类型
     */
    private RoleType role = RoleType.USER;

    /**
     * 消息类型
     */
    private MessageType messageType = MessageType.NORMAL;

    /**
     * 发送者名称
     */
    @Size(max = 100, message = "发送者名称长度不能超过100个字符")
    private String name;

    /**
     * 用户ID（可选，用于标识特定用户）
     */
    private String userId;

    /**
     * 父消息ID（用于回复消息）
     */
    private Long parentMessageId;

    /**
     * 扩展信息
     */
    private Map<String, Object> ext;

    /**
     * 是否流式响应
     */
    private Boolean stream = false;

    /**
     * 是否包含工具调用
     */
    private Boolean includeTools = true;
}
