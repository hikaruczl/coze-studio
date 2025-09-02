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

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 更新 Bot 请求 DTO
 * 
 * @author coze-dev
 */
@Data
public class UpdateBotRequest {

    /**
     * Bot 名称
     */
    @Size(min = 1, max = 100, message = "Bot名称长度必须在1-100个字符之间")
    private String name;

    /**
     * Bot 描述
     */
    @Size(max = 1000, message = "Bot描述长度不能超过1000个字符")
    private String description;

    /**
     * 图标URI
     */
    private String iconUri;

    /**
     * 连接器ID列表
     */
    private List<Long> connectorIds;

    /**
     * 场景类型
     */
    private String scene;

    /**
     * 版本描述
     */
    @Size(max = 500, message = "版本描述长度不能超过500个字符")
    private String versionDesc;

    /**
     * 是否启用
     */
    private Boolean enabled;
}
