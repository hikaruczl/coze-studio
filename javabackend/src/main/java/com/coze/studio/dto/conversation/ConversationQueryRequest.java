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

import com.coze.studio.entity.enums.ConversationStatus;
import com.coze.studio.entity.enums.Scene;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 对话查询请求 DTO
 * 
 * @author coze-dev
 */
@Data
public class ConversationQueryRequest {

    /**
     * 关键词（标题、摘要）
     */
    private String keyword;

    /**
     * Bot ID
     */
    private Long agentId;

    /**
     * 创建者ID
     */
    private Long creatorId;

    /**
     * 对话状态
     */
    private ConversationStatus status;

    /**
     * 场景类型
     */
    private Scene scene;

    /**
     * 是否置顶
     */
    private Boolean pinned;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 最小消息数
     */
    private Integer minMessageCount;

    /**
     * 最大消息数
     */
    private Integer maxMessageCount;
}
