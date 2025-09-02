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

import com.coze.studio.entity.Bot;
import com.coze.studio.entity.enums.PublishStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Bot 响应 DTO
 * 
 * @author coze-dev
 */
@Data
public class BotResponse {

    /**
     * Bot ID
     */
    private Long id;

    /**
     * 空间ID
     */
    private Long spaceId;

    /**
     * 图标URI
     */
    private String iconUri;

    /**
     * Bot名称
     */
    private String name;

    /**
     * Bot描述
     */
    private String description;

    /**
     * 所有者ID
     */
    private Long ownerId;

    /**
     * 所有者用户名
     */
    private String ownerUsername;

    /**
     * 连接器ID列表
     */
    private List<Long> connectorIds;

    /**
     * 版本号
     */
    private String version;

    /**
     * 版本描述
     */
    private String versionDesc;

    /**
     * 发布记录ID
     */
    private Long publishRecordId;

    /**
     * 发布状态
     */
    private PublishStatus publishStatus;

    /**
     * 发布时间
     */
    private LocalDateTime publishedAt;

    /**
     * 场景类型
     */
    private String scene;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 从 Bot 实体转换为 BotResponse
     */
    public static BotResponse fromBot(Bot bot) {
        BotResponse response = new BotResponse();
        response.setId(bot.getId());
        response.setSpaceId(bot.getSpaceId());
        response.setIconUri(bot.getIconUri());
        response.setName(bot.getName());
        response.setDescription(bot.getDescription());
        response.setOwnerId(bot.getOwnerId());
        response.setConnectorIds(bot.getConnectorIds());
        response.setVersion(bot.getVersion());
        response.setVersionDesc(bot.getVersionDesc());
        response.setPublishRecordId(bot.getPublishRecordId());
        response.setPublishStatus(bot.getPublishStatus());
        response.setPublishedAt(bot.getPublishedAt());
        response.setScene(bot.getScene());
        response.setEnabled(bot.getEnabled());
        response.setCreatedAt(bot.getCreatedAt());
        response.setUpdatedAt(bot.getUpdatedAt());
        return response;
    }

    /**
     * 从 Bot 实体转换为 BotResponse（包含所有者信息）
     */
    public static BotResponse fromBot(Bot bot, String ownerUsername) {
        BotResponse response = fromBot(bot);
        response.setOwnerUsername(ownerUsername);
        return response;
    }
}
