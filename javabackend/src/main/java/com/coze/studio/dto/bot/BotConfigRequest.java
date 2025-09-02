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

import java.util.List;
import java.util.Map;

/**
 * Bot 配置请求 DTO
 * 
 * @author coze-dev
 */
@Data
public class BotConfigRequest {

    /**
     * 系统提示词
     */
    private String systemPrompt;

    /**
     * 模型配置
     */
    private ModelConfig modelConfig;

    /**
     * 插件配置列表
     */
    private List<PluginConfig> plugins;

    /**
     * 知识库配置列表
     */
    private List<KnowledgeConfig> knowledgeBases;

    /**
     * 工作流配置
     */
    private WorkflowConfig workflowConfig;

    /**
     * 其他配置
     */
    private Map<String, Object> additionalConfig;

    /**
     * 模型配置
     */
    @Data
    public static class ModelConfig {
        /**
         * 模型名称
         */
        private String modelName;

        /**
         * 温度参数
         */
        private Double temperature;

        /**
         * 最大令牌数
         */
        private Integer maxTokens;

        /**
         * Top P 参数
         */
        private Double topP;

        /**
         * 频率惩罚
         */
        private Double frequencyPenalty;

        /**
         * 存在惩罚
         */
        private Double presencePenalty;
    }

    /**
     * 插件配置
     */
    @Data
    public static class PluginConfig {
        /**
         * 插件ID
         */
        private Long pluginId;

        /**
         * 是否启用
         */
        private Boolean enabled;

        /**
         * 插件配置参数
         */
        private Map<String, Object> config;
    }

    /**
     * 知识库配置
     */
    @Data
    public static class KnowledgeConfig {
        /**
         * 知识库ID
         */
        private Long knowledgeBaseId;

        /**
         * 是否启用
         */
        private Boolean enabled;

        /**
         * 检索配置
         */
        private Map<String, Object> retrievalConfig;
    }

    /**
     * 工作流配置
     */
    @Data
    public static class WorkflowConfig {
        /**
         * 工作流ID
         */
        private Long workflowId;

        /**
         * 是否启用
         */
        private Boolean enabled;

        /**
         * 工作流参数
         */
        private Map<String, Object> parameters;
    }
}
