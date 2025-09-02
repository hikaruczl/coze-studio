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

package com.coze.studio.dto.bot;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Bot 统计信息响应 DTO
 * 
 * @author coze-dev
 */
@Data
public class BotStatsResponse {

    /**
     * Bot ID
     */
    private Long botId;

    /**
     * Bot 名称
     */
    private String botName;

    /**
     * 总对话数
     */
    private Long totalConversations;

    /**
     * 总消息数
     */
    private Long totalMessages;

    /**
     * 活跃用户数
     */
    private Long activeUsers;

    /**
     * 今日对话数
     */
    private Long todayConversations;

    /**
     * 今日消息数
     */
    private Long todayMessages;

    /**
     * 本周对话数
     */
    private Long weekConversations;

    /**
     * 本周消息数
     */
    private Long weekMessages;

    /**
     * 本月对话数
     */
    private Long monthConversations;

    /**
     * 本月消息数
     */
    private Long monthMessages;

    /**
     * 平均响应时间（毫秒）
     */
    private Double avgResponseTime;

    /**
     * 用户满意度评分
     */
    private Double satisfactionScore;

    /**
     * 最后使用时间
     */
    private LocalDateTime lastUsedAt;

    /**
     * 统计时间
     */
    private LocalDateTime statsTime;
}
