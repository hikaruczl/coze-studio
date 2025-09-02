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

package com.coze.studio.exception;

import lombok.Getter;

/**
 * 错误码枚举
 * 定义系统中所有的错误码和错误信息
 *
 * @author coze-dev
 */
@Getter
public enum ErrorCode {

    // 通用错误码 (1000-1999)
    SUCCESS("0", "成功"),
    SYSTEM_ERROR("1000", "系统内部错误"),
    INVALID_PARAMETER("1001", "参数错误"),
    RESOURCE_NOT_FOUND("1002", "资源不存在"),
    UNAUTHORIZED("1003", "未授权访问"),
    FORBIDDEN("1004", "访问被禁止"),
    METHOD_NOT_ALLOWED("1005", "请求方法不被允许"),
    UNSUPPORTED_MEDIA_TYPE("1006", "不支持的媒体类型"),
    FILE_TOO_LARGE("1007", "上传文件过大"),
    NOT_FOUND("1008", "资源未找到"),
    INTERNAL_SERVER_ERROR("1009", "服务器内部错误"),

    // 用户相关错误码 (2000-2999)
    USER_NOT_FOUND("2000", "用户不存在"),
    USER_ALREADY_EXISTS("2001", "用户已存在"),
    INVALID_CREDENTIALS("2002", "用户名或密码错误"),
    USER_DISABLED("2003", "用户已被禁用"),
    TOKEN_EXPIRED("2004", "令牌已过期"),
    TOKEN_INVALID("2005", "令牌无效"),

    // 应用相关错误码 (3000-3999)
    BOT_NOT_FOUND("3000", "Bot不存在"),
    BOT_ALREADY_EXISTS("3001", "Bot已存在"),
    BOT_PUBLISH_FAILED("3002", "Bot发布失败"),

    // 对话相关错误码 (4000-4999)
    CONVERSATION_NOT_FOUND("4000", "对话不存在"),
    MESSAGE_SEND_FAILED("4001", "消息发送失败"),

    // 插件相关错误码 (5000-5999)
    PLUGIN_NOT_FOUND("5000", "插件不存在"),
    PLUGIN_EXECUTION_FAILED("5001", "插件执行失败"),
    PLUGIN_INVALID_SPEC("5002", "插件规范无效"),

    // 知识库相关错误码 (6000-6999)
    KNOWLEDGE_BASE_NOT_FOUND("6000", "知识库不存在"),
    DOCUMENT_UPLOAD_FAILED("6001", "文档上传失败"),
    DOCUMENT_PARSE_FAILED("6002", "文档解析失败"),

    // 工作流相关错误码 (7000-7999)
    WORKFLOW_NOT_FOUND("7000", "工作流不存在"),
    WORKFLOW_EXECUTION_FAILED("7001", "工作流执行失败"),
    WORKFLOW_INVALID_DEFINITION("7002", "工作流定义无效"),
    WORKFLOW_VALIDATION_FAILED("7003", "工作流验证失败"),
    WORKFLOW_NO_START_NODE("7004", "工作流没有开始节点"),
    WORKFLOW_NODE_NOT_FOUND("7005", "工作流节点不存在"),
    EXECUTION_NOT_FOUND("7006", "执行记录不存在"),
    WORKFLOW_COPY_FAILED("7007", "工作流复制失败"),

    // 模板相关错误码 (7100-7199)
    TEMPLATE_NAME_EXISTS("7100", "模板名称已存在"),
    TEMPLATE_NOT_FOUND("7101", "模板不存在"),
    TEMPLATE_ACCESS_DENIED("7102", "无权访问模板"),
    TEMPLATE_INVALID_DEFINITION("7103", "模板定义无效"),
    TEMPLATE_ALREADY_PUBLISHED("7104", "模板已发布"),
    TEMPLATE_ALREADY_FAVORITED("7105", "模板已收藏"),
    TEMPLATE_INVALID_FORMAT("7106", "模板格式无效"),
    TEMPLATE_IMPORT_FAILED("7107", "模板导入失败"),

    // 变量相关错误码 (7200-7299)
    VARIABLE_READONLY("7200", "变量为只读"),
    VARIABLE_SYSTEM("7201", "系统变量不可修改/删除"),
    VARIABLE_ALREADY_EXISTS("7202", "变量已存在"),

    // RAG/Embedding/LLM 相关错误码 (7300-7399)
    EMBEDDING_GENERATION_FAILED("7300", "向量生成失败"),
    VECTOR_SEARCH_FAILED("7301", "向量搜索失败"),
    LLM_CALL_FAILED("7302", "大模型调用失败"),

    // 通用错误码
    NOT_IMPLEMENTED("9999", "功能未实现"),

    // 插件管理相关错误码 (8000-8099)
    PLUGIN_NAME_EXISTS("8000", "插件名称已存在"),
    PLUGIN_ALREADY_PUBLISHED("8001", "插件已发布"),
    PLUGIN_ACCESS_DENIED("8002", "无权访问该插件"),
    PLUGIN_ALREADY_INSTALLED("8003", "插件已安装"),
    PLUGIN_NOT_INSTALLED("8004", "插件未安装"),
    PLUGIN_INSTALLATION_FAILED("8005", "插件安装失败"),
    PLUGIN_UNINSTALLATION_FAILED("8006", "插件卸载失败"),
    INVALID_RATING("8007", "评分无效"),
    DOCUMENT_NOT_FOUND("8008","文档不存在"),
    DOCUMENT_CONTENT_EMPTY("8009","文档内容为空"),
    NO_SLICES_FOUND("8010","未找到切片"),

    // 权限相关错误码 (9000-9099)
    ACCESS_DENIED("9000", "访问被拒绝"),

    // 分类相关错误码 (9100-9199)
    CATEGORY_CODE_EXISTS("9100", "分类代码已存在"),
    CATEGORY_NAME_EXISTS("9101", "分类名称已存在"),
    CATEGORY_NOT_FOUND("9102", "分类不存在"),
    CATEGORY_HAS_CHILDREN("9103", "分类下存在子分类"),
    CATEGORY_HAS_TEMPLATES("9104", "分类下存在模板");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    // 手动添加 getter 方法以确保编译通过
    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
