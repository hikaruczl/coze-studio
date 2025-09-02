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
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Map;

/**
 * 更新对话请求 DTO
 * 
 * @author coze-dev
 */
@Data
public class UpdateConversationRequest {

    /**
     * 对话标题
     */
    @Size(max = 200, message = "对话标题长度不能超过200个字符")
    private String title;

    /**
     * 对话摘要
     */
    @Size(max = 1000, message = "对话摘要长度不能超过1000个字符")
    private String summary;

    /**
     * 对话状态
     */
    private ConversationStatus status;

    /**
     * 是否置顶
     */
    private Boolean pinned;

    /**
     * 扩展信息
     */
    private Map<String, Object> ext;
}
