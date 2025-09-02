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

import com.coze.studio.entity.enums.PublishStatus;
import lombok.Data;

/**
 * Bot 查询请求 DTO
 * 
 * @author coze-dev
 */
@Data
public class BotQueryRequest {

    /**
     * 关键词（Bot名称、描述）
     */
    private String keyword;

    /**
     * 所有者ID
     */
    private Long ownerId;

    /**
     * 空间ID
     */
    private Long spaceId;

    /**
     * 发布状态
     */
    private PublishStatus publishStatus;

    /**
     * 场景类型
     */
    private String scene;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 连接器ID
     */
    private Long connectorId;
}
