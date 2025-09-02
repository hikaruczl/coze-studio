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
 * 消息状态枚举
 * 
 * @author coze-dev
 */
public enum MessageStatus {
    
    /**
     * 发送中
     */
    SENDING,
    
    /**
     * 已发送
     */
    SENT,
    
    /**
     * 已接收
     */
    RECEIVED,
    
    /**
     * 处理中
     */
    PROCESSING,
    
    /**
     * 处理完成
     */
    COMPLETED,
    
    /**
     * 发送失败
     */
    FAILED,
    
    /**
     * 已删除
     */
    DELETED
}
