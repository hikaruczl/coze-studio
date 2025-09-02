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

package com.coze.studio.entity.enums;

/**
 * 消息类型枚举
 * 
 * @author coze-dev
 */
public enum MessageType {
    
    /**
     * 普通消息
     */
    NORMAL,
    
    /**
     * 系统消息
     */
    SYSTEM,
    
    /**
     * 工具调用消息
     */
    TOOL_CALL,
    
    /**
     * 工具响应消息
     */
    TOOL_RESPONSE,
    
    /**
     * 错误消息
     */
    ERROR,
    
    /**
     * 通知消息
     */
    NOTIFICATION
}
