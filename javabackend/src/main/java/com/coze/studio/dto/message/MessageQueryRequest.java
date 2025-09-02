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

import com.coze.studio.entity.enums.MessageStatus;
import com.coze.studio.entity.enums.MessageType;
import com.coze.studio.entity.enums.RoleType;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 消息查询请求 DTO
 * 
 * @author coze-dev
 */
@Data
public class MessageQueryRequest {

    /**
     * 对话ID
     */
    private Long conversationId;

    /**
     * 关键词（消息内容）
     */
    private String keyword;

    /**
     * 角色类型
     */
    private RoleType role;

    /**
     * 消息状态
     */
    private MessageStatus status;

    /**
     * 消息类型
     */
    private MessageType messageType;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 运行ID
     */
    private Long runId;

    /**
     * 是否包含工具调用
     */
    private Boolean hasToolCalls;

    /**
     * 是否已读
     */
    private Boolean isRead;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;
}
