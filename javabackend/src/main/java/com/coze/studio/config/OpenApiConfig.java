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

package com.coze.studio.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * OpenAPI 3.0 配置类
 * 配置 Swagger UI 和 API 文档
 * 
 * @author coze-dev
 */
@Configuration
public class OpenApiConfig {

    @Value("${spring.application.name:Coze Studio}")
    private String applicationName;

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .servers(serverList())
                .tags(tagList())
                .components(securityComponents())
                .addSecurityItem(securityRequirement());
    }

    /**
     * API 基本信息
     */
    private Info apiInfo() {
        return new Info()
                .title("Coze Studio API")
                .description("Coze Studio - 智能工作流自动化平台 API 文档")
                .version("1.0.0")
                .contact(new Contact()
                        .name("Coze Studio Team")
                        .email("support@coze-studio.com")
                        .url("https://github.com/coze-dev/coze-studio"))
                .license(new License()
                        .name("Apache License 2.0")
                        .url("https://www.apache.org/licenses/LICENSE-2.0"));
    }

    /**
     * 服务器列表
     */
    private List<Server> serverList() {
        return Arrays.asList(
                new Server()
                        .url("http://localhost:" + serverPort)
                        .description("本地开发环境"),
                new Server()
                        .url("https://api-dev.coze-studio.com")
                        .description("开发环境"),
                new Server()
                        .url("https://api-staging.coze-studio.com")
                        .description("测试环境"),
                new Server()
                        .url("https://api.coze-studio.com")
                        .description("生产环境")
        );
    }

    /**
     * API 标签分组
     */
    private List<Tag> tagList() {
        return Arrays.asList(
                new Tag()
                        .name("用户管理")
                        .description("用户注册、登录、个人信息管理"),
                new Tag()
                        .name("应用管理")
                        .description("Bot应用的创建、配置和管理"),
                new Tag()
                        .name("对话管理")
                        .description("对话会话和消息处理"),
                new Tag()
                        .name("工作流管理")
                        .description("工作流的创建、编辑、执行和管理"),
                new Tag()
                        .name("工作流模板")
                        .description("工作流模板的管理和使用"),
                new Tag()
                        .name("插件管理")
                        .description("插件的注册、安装、配置和执行"),
                new Tag()
                        .name("知识库管理")
                        .description("知识库的创建、文档管理和RAG查询"),
                new Tag()
                        .name("文档管理")
                        .description("文档的上传、处理和管理"),
                new Tag()
                        .name("向量存储管理")
                        .description("向量存储的配置和操作"),
                new Tag()
                        .name("系统管理")
                        .description("系统状态、配置和监控")
        );
    }

    /**
     * 安全组件配置
     */
    private Components securityComponents() {
        return new Components()
                .addSecuritySchemes("bearerAuth", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("JWT Bearer Token 认证"))
                .addSecuritySchemes("apiKey", new SecurityScheme()
                        .type(SecurityScheme.Type.APIKEY)
                        .in(SecurityScheme.In.HEADER)
                        .name("X-API-Key")
                        .description("API Key 认证"));
    }

    /**
     * 安全要求配置
     */
    private SecurityRequirement securityRequirement() {
        return new SecurityRequirement()
                .addList("bearerAuth")
                .addList("apiKey");
    }
}
