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

package com.coze.studio.dto.workflow;

import lombok.Data;

import java.util.Map;

/**
 * 执行工作流请求 DTO
 * 
 * @author coze-dev
 */
@Data
public class ExecuteWorkflowRequest {

    /**
     * 工作流ID
     */
    private Long workflowId;

    /**
     * 输入参数
     */
    private Map<String, Object> inputData;

    /**
     * 执行模式（SYNC/ASYNC）
     */
    private String executionMode = "SYNC";

    /**
     * 触发类型
     */
    private String triggerType = "MANUAL";

    /**
     * 触发来源
     */
    private String triggerSource;

    /**
     * 执行上下文
     */
    private Map<String, Object> context;

    /**
     * 执行配置
     */
    private Map<String, Object> executionConfig;

    /**
     * 关联的对话ID
     */
    private Long conversationId;

    /**
     * 关联的消息ID
     */
    private Long messageId;

    /**
     * 关联的应用ID
     */
    private Long appId;

    /**
     * 超时时间（秒）
     */
    private Integer timeoutSeconds;

    /**
     * 是否启用调试模式
     */
    private Boolean debugMode = false;

    /**
     * 回调URL（异步执行时使用）
     */
    private String callbackUrl;
}
