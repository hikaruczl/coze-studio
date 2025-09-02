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

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 发布 Bot 请求 DTO
 * 
 * @author coze-dev
 */
@Data
public class PublishBotRequest {

    /**
     * 版本号
     */
    @NotBlank(message = "版本号不能为空")
    @Size(max = 50, message = "版本号长度不能超过50个字符")
    private String version;

    /**
     * 版本描述
     */
    @Size(max = 500, message = "版本描述长度不能超过500个字符")
    private String versionDesc;

    /**
     * 发布说明
     */
    @Size(max = 1000, message = "发布说明长度不能超过1000个字符")
    private String releaseNotes;
}
