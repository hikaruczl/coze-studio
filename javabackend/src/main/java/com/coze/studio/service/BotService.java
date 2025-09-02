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

package com.coze.studio.service;

import com.coze.studio.dto.bot.*;
import com.coze.studio.dto.common.PageResponse;
import com.coze.studio.entity.Bot;
import org.springframework.data.domain.Pageable;

/**
 * Bot 服务接口
 * 
 * @author coze-dev
 */
public interface BotService {

    /**
     * 创建 Bot
     */
    BotResponse createBot(Long userId, CreateBotRequest request);

    /**
     * 更新 Bot
     */
    BotResponse updateBot(Long userId, Long botId, UpdateBotRequest request);

    /**
     * 根据ID获取 Bot
     */
    BotResponse getBotById(Long botId);

    /**
     * 根据ID获取 Bot 实体
     */
    Bot getBotEntityById(Long botId);

    /**
     * 删除 Bot
     */
    void deleteBot(Long userId, Long botId);

    /**
     * 分页查询 Bot 列表
     */
    PageResponse<BotResponse> getBots(BotQueryRequest request, Pageable pageable);

    /**
     * 获取用户的 Bot 列表
     */
    PageResponse<BotResponse> getUserBots(Long userId, Pageable pageable);

    /**
     * 发布 Bot
     */
    BotResponse publishBot(Long userId, Long botId, PublishBotRequest request);

    /**
     * 下线 Bot
     */
    BotResponse unpublishBot(Long userId, Long botId);

    /**
     * 启用/禁用 Bot
     */
    BotResponse toggleBotStatus(Long userId, Long botId, boolean enabled);

    /**
     * 复制 Bot
     */
    BotResponse cloneBot(Long userId, Long botId, String newName);

    /**
     * 检查 Bot 名称是否可用
     */
    boolean isBotNameAvailable(Long userId, String name);

    /**
     * 检查用户是否有 Bot 的访问权限
     */
    boolean hasAccessToBot(Long userId, Long botId);

    /**
     * 检查用户是否是 Bot 的所有者
     */
    boolean isBotOwner(Long userId, Long botId);

    /**
     * 获取已发布的 Bot 列表
     */
    PageResponse<BotResponse> getPublishedBots(Pageable pageable);

    /**
     * 根据连接器ID获取 Bot 列表
     */
    PageResponse<BotResponse> getBotsByConnector(Long connectorId, Pageable pageable);

    /**
     * 统计用户的 Bot 数量
     */
    long countUserBots(Long userId);

    /**
     * 获取热门 Bot 列表
     */
    PageResponse<BotResponse> getPopularBots(Pageable pageable);
}
