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

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * 数据库配置类
 * 支持多环境数据库配置
 * 
 * @author coze-dev
 */
@Configuration
public class DatabaseConfig {

    /**
     * 开发环境配置
     * 使用 H2 内存数据库，便于开发和测试
     */
    @Configuration
    @Profile("dev")
    static class DevelopmentDatabaseConfig {
        // H2 配置通过 application-dev.properties 文件配置
    }

    /**
     * 生产环境配置
     * 使用 MySQL 数据库
     */
    @Configuration
    @Profile("prod")
    static class ProductionDatabaseConfig {
        // MySQL 配置通过 application-prod.properties 文件配置
    }

    /**
     * 测试环境配置
     * 使用 H2 内存数据库
     */
    @Configuration
    @Profile("test")
    static class TestDatabaseConfig {
        // H2 配置通过 application-test.properties 文件配置
    }
}
