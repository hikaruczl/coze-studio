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
 * 工作流节点类型枚举
 * 
 * @author coze-dev
 */
public enum WorkflowNodeType {

    /**
     * 开始节点
     */
    START("START"),

    /**
     * 结束节点
     */
    END("END"),
    
    /**
     * 条件节点
     */
    CONDITION("CONDITION"),

    /**
     * 动作节点
     */
    ACTION("ACTION"),

    /**
     * 循环节点
     */
    LOOP("LOOP"),

    /**
     * 并行节点
     */
    PARALLEL("PARALLEL"),

    /**
     * 合并节点
     */
    MERGE("MERGE"),

    /**
     * 延迟节点
     */
    DELAY("DELAY"),

    /**
     * 脚本节点
     */
    SCRIPT("SCRIPT"),

    /**
     * HTTP请求节点
     */
    HTTP_REQUEST("HTTP_REQUEST"),

    /**
     * 数据库操作节点
     */
    DATABASE("DATABASE"),

    /**
     * 文件操作节点
     */
    FILE_OPERATION("FILE_OPERATION"),

    /**
     * 邮件发送节点
     */
    EMAIL("EMAIL"),

    /**
     * 消息通知节点
     */
    NOTIFICATION("NOTIFICATION"),

    /**
     * AI模型调用节点
     */
    AI_MODEL("AI_MODEL"),

    /**
     * 插件调用节点
     */
    PLUGIN("PLUGIN"),

    /**
     * 子工作流节点
     */
    SUB_WORKFLOW("SUB_WORKFLOW"),

    /**
     * 变量设置节点
     */
    VARIABLE_SET("VARIABLE_SET"),

    /**
     * 数据转换节点
     */
    DATA_TRANSFORM("DATA_TRANSFORM"),

    /**
     * 错误处理节点
     */
    ERROR_HANDLER("ERROR_HANDLER");

    private final String value;

    WorkflowNodeType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static WorkflowNodeType fromValue(String value) {
        for (WorkflowNodeType type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown WorkflowNodeType: " + value);
    }
}
