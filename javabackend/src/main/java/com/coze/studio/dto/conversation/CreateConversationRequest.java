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

import com.coze.studio.entity.enums.Scene;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Map;

/**
 * 创建对话请求 DTO
 * 
 * @author coze-dev
 */
@Data
public class CreateConversationRequest {

    /**
     * Bot ID
     */
    @NotNull(message = "Bot ID不能为空")
    private Long agentId;

    /**
     * 连接器ID
     */
    private Long connectorId;

    /**
     * 场景类型
     */
    private Scene scene = Scene.CHAT;

    /**
     * 对话标题
     */
    @Size(max = 200, message = "对话标题长度不能超过200个字符")
    private String title;

    /**
     * 扩展信息
     */
    private Map<String, Object> ext;

    /**
     * 初始消息内容
     */
    @Size(max = 10000, message = "初始消息内容长度不能超过10000个字符")
    private String initialMessage;
}
