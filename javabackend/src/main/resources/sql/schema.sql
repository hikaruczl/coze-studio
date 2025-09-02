-- ========================================
-- Coze Studio Database Schema (PostgreSQL)
-- ========================================

-- 创建数据库（如果不存在）
-- CREATE DATABASE coze_studio;
-- CREATE USER coze_user WITH PASSWORD 'coze_password';
-- GRANT ALL PRIVILEGES ON DATABASE coze_studio TO coze_user;

-- 使用数据库
-- \c coze_studio;

-- 创建扩展
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ========================================
-- 基础表结构
-- ========================================

-- 工作流变量表
CREATE TABLE IF NOT EXISTS workflow_variables (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    value TEXT,
    type VARCHAR(50) NOT NULL DEFAULT 'STRING',
    scope VARCHAR(50) NOT NULL DEFAULT 'EXECUTION',
    user_id BIGINT NOT NULL,
    workflow_id BIGINT,
    execution_id VARCHAR(100),
    session_id VARCHAR(100),
    node_id VARCHAR(100),
    description TEXT,
    is_sensitive BOOLEAN NOT NULL DEFAULT FALSE,
    is_readonly BOOLEAN NOT NULL DEFAULT FALSE,
    expires_at TIMESTAMP,
    access_count BIGINT NOT NULL DEFAULT 0,
    last_accessed_at TIMESTAMP,
    metadata TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted BOOLEAN NOT NULL DEFAULT FALSE
);

-- 对话变量表
CREATE TABLE IF NOT EXISTS conversation_variables (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    value TEXT,
    type VARCHAR(50) NOT NULL DEFAULT 'STRING',
    scope VARCHAR(50) NOT NULL DEFAULT 'CONVERSATION',
    user_id BIGINT NOT NULL,
    bot_id BIGINT,
    conversation_id BIGINT,
    session_id VARCHAR(100),
    message_id BIGINT,
    description TEXT,
    is_sensitive BOOLEAN NOT NULL DEFAULT FALSE,
    is_readonly BOOLEAN NOT NULL DEFAULT FALSE,
    expires_at TIMESTAMP,
    access_count BIGINT NOT NULL DEFAULT 0,
    last_accessed_at TIMESTAMP,
    metadata TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted BOOLEAN NOT NULL DEFAULT FALSE
);

-- 提示词分类表
CREATE TABLE IF NOT EXISTS prompt_categories (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    display_name VARCHAR(100),
    description VARCHAR(500),
    parent_id BIGINT,
    level INTEGER NOT NULL DEFAULT 1,
    path VARCHAR(500),
    sort_order INTEGER NOT NULL DEFAULT 0,
    icon VARCHAR(100),
    color VARCHAR(20),
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    is_system BOOLEAN NOT NULL DEFAULT FALSE,
    template_count BIGINT NOT NULL DEFAULT 0,
    metadata TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted BOOLEAN NOT NULL DEFAULT FALSE
);

-- 提示词模板表
CREATE TABLE IF NOT EXISTS prompt_templates (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    display_name VARCHAR(200),
    description VARCHAR(1000),
    content TEXT NOT NULL,
    type VARCHAR(50) NOT NULL,
    category VARCHAR(100),
    tags VARCHAR(500),
    language VARCHAR(10) DEFAULT 'zh-CN',
    version VARCHAR(20) NOT NULL DEFAULT '1.0.0',
    major_version INTEGER NOT NULL DEFAULT 1,
    minor_version INTEGER NOT NULL DEFAULT 0,
    patch_version INTEGER NOT NULL DEFAULT 0,
    parent_id BIGINT,
    root_id BIGINT,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    user_id BIGINT NOT NULL,
    is_public BOOLEAN NOT NULL DEFAULT FALSE,
    is_system BOOLEAN NOT NULL DEFAULT FALSE,
    is_default BOOLEAN NOT NULL DEFAULT FALSE,
    parameters TEXT,
    variables TEXT,
    config TEXT,
    metadata TEXT,
    usage_count BIGINT NOT NULL DEFAULT 0,
    last_used_at TIMESTAMP,
    rating DECIMAL(3,2),
    rating_count BIGINT NOT NULL DEFAULT 0,
    download_count BIGINT NOT NULL DEFAULT 0,
    favorite_count BIGINT NOT NULL DEFAULT 0,
    icon_url VARCHAR(500),
    screenshot_url VARCHAR(500),
    documentation_url VARCHAR(500),
    example_url VARCHAR(500),
    license VARCHAR(100),
    author VARCHAR(200),
    contributors TEXT,
    published_at TIMESTAMP,
    archived_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted BOOLEAN NOT NULL DEFAULT FALSE
);

-- 模型提供商表
CREATE TABLE IF NOT EXISTS model_providers (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    display_name VARCHAR(100),
    description VARCHAR(500),
    type VARCHAR(50) NOT NULL,
    base_url VARCHAR(500),
    api_version VARCHAR(20),
    auth_type VARCHAR(20) NOT NULL DEFAULT 'API_KEY',
    api_key VARCHAR(500),
    organization_id VARCHAR(100),
    project_id VARCHAR(100),
    region VARCHAR(50),
    auth_config TEXT,
    request_config TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    is_default BOOLEAN NOT NULL DEFAULT FALSE,
    priority INTEGER NOT NULL DEFAULT 100,
    rate_limit_config TEXT,
    retry_config TEXT,
    timeout_ms INTEGER NOT NULL DEFAULT 30000,
    connect_timeout_ms INTEGER NOT NULL DEFAULT 10000,
    read_timeout_ms INTEGER NOT NULL DEFAULT 30000,
    max_concurrent_requests INTEGER NOT NULL DEFAULT 10,
    health_check_url VARCHAR(500),
    health_check_interval INTEGER NOT NULL DEFAULT 300,
    last_health_check TIMESTAMP,
    health_status VARCHAR(20) DEFAULT 'UNKNOWN',
    supported_features TEXT,
    icon_url VARCHAR(500),
    documentation_url VARCHAR(500),
    website_url VARCHAR(500),
    contact_email VARCHAR(100),
    support_email VARCHAR(100),
    metadata TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted BOOLEAN NOT NULL DEFAULT FALSE
);

-- 模型配置表
CREATE TABLE IF NOT EXISTS model_configurations (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    display_name VARCHAR(100),
    description VARCHAR(1000),
    provider_id BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL,
    version VARCHAR(50),
    provider_model_id VARCHAR(200) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    is_default BOOLEAN NOT NULL DEFAULT FALSE,
    priority INTEGER NOT NULL DEFAULT 100,
    max_input_tokens INTEGER,
    max_output_tokens INTEGER,
    max_total_tokens INTEGER,
    supported_languages TEXT,
    supported_features TEXT,
    default_parameters TEXT,
    parameter_constraints TEXT,
    input_price_per_1k DECIMAL(10,6),
    output_price_per_1k DECIMAL(10,6),
    currency VARCHAR(10) DEFAULT 'USD',
    rate_limit_rpm INTEGER,
    rate_limit_tpm INTEGER,
    concurrency_limit INTEGER,
    performance_score DECIMAL(3,2),
    quality_score DECIMAL(3,2),
    latency_score INTEGER,
    availability_score DECIMAL(5,2),
    tags TEXT,
    use_cases TEXT,
    limitations TEXT,
    last_updated TIMESTAMP,
    release_date TIMESTAMP,
    deprecated_date TIMESTAMP,
    documentation_url VARCHAR(500),
    example_url VARCHAR(500),
    metadata TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted BOOLEAN NOT NULL DEFAULT FALSE
);

-- 模型使用记录表
CREATE TABLE IF NOT EXISTS model_usage_records (
    id BIGSERIAL PRIMARY KEY,
    model_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    session_id VARCHAR(100),
    request_id VARCHAR(100),
    application_id BIGINT,
    workflow_id BIGINT,
    conversation_id BIGINT,
    usage_type VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL,
    input_tokens INTEGER NOT NULL DEFAULT 0,
    output_tokens INTEGER NOT NULL DEFAULT 0,
    total_tokens INTEGER NOT NULL DEFAULT 0,
    request_start_time TIMESTAMP NOT NULL,
    request_end_time TIMESTAMP,
    response_time_ms BIGINT,
    cost DECIMAL(10,6),
    currency VARCHAR(10) DEFAULT 'USD',
    error_code VARCHAR(50),
    error_message VARCHAR(1000),
    request_parameters TEXT,
    response_metadata TEXT,
    usage_date TIMESTAMP NOT NULL,
    client_ip VARCHAR(45),
    user_agent VARCHAR(500),
    geo_info TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted BOOLEAN NOT NULL DEFAULT FALSE
);

-- ========================================
-- 索引创建
-- ========================================

-- 工作流变量索引
CREATE INDEX IF NOT EXISTS idx_workflow_variables_name ON workflow_variables(name);
CREATE INDEX IF NOT EXISTS idx_workflow_variables_user_id ON workflow_variables(user_id);
CREATE INDEX IF NOT EXISTS idx_workflow_variables_workflow_id ON workflow_variables(workflow_id);
CREATE INDEX IF NOT EXISTS idx_workflow_variables_execution_id ON workflow_variables(execution_id);
CREATE INDEX IF NOT EXISTS idx_workflow_variables_session_id ON workflow_variables(session_id);
CREATE INDEX IF NOT EXISTS idx_workflow_variables_scope ON workflow_variables(scope);
CREATE INDEX IF NOT EXISTS idx_workflow_variables_type ON workflow_variables(type);
CREATE INDEX IF NOT EXISTS idx_workflow_variables_expires_at ON workflow_variables(expires_at);

-- 对话变量索引
CREATE INDEX IF NOT EXISTS idx_conversation_variables_name ON conversation_variables(name);
CREATE INDEX IF NOT EXISTS idx_conversation_variables_user_id ON conversation_variables(user_id);
CREATE INDEX IF NOT EXISTS idx_conversation_variables_bot_id ON conversation_variables(bot_id);
CREATE INDEX IF NOT EXISTS idx_conversation_variables_conversation_id ON conversation_variables(conversation_id);
CREATE INDEX IF NOT EXISTS idx_conversation_variables_session_id ON conversation_variables(session_id);
CREATE INDEX IF NOT EXISTS idx_conversation_variables_scope ON conversation_variables(scope);
CREATE INDEX IF NOT EXISTS idx_conversation_variables_type ON conversation_variables(type);

-- 提示词分类索引
CREATE INDEX IF NOT EXISTS idx_prompt_category_code ON prompt_categories(code);
CREATE INDEX IF NOT EXISTS idx_prompt_category_parent_id ON prompt_categories(parent_id);
CREATE INDEX IF NOT EXISTS idx_prompt_category_sort_order ON prompt_categories(sort_order);
CREATE INDEX IF NOT EXISTS idx_prompt_category_enabled ON prompt_categories(enabled);

-- 提示词模板索引
CREATE INDEX IF NOT EXISTS idx_prompt_template_name ON prompt_templates(name);
CREATE INDEX IF NOT EXISTS idx_prompt_template_category ON prompt_templates(category);
CREATE INDEX IF NOT EXISTS idx_prompt_template_type ON prompt_templates(type);
CREATE INDEX IF NOT EXISTS idx_prompt_template_status ON prompt_templates(status);
CREATE INDEX IF NOT EXISTS idx_prompt_template_user_id ON prompt_templates(user_id);
CREATE INDEX IF NOT EXISTS idx_prompt_template_parent_id ON prompt_templates(parent_id);
CREATE INDEX IF NOT EXISTS idx_prompt_template_version ON prompt_templates(version);
CREATE INDEX IF NOT EXISTS idx_prompt_template_is_public ON prompt_templates(is_public);

-- 模型提供商索引
CREATE INDEX IF NOT EXISTS idx_model_provider_code ON model_providers(code);
CREATE INDEX IF NOT EXISTS idx_model_provider_name ON model_providers(name);
CREATE INDEX IF NOT EXISTS idx_model_provider_type ON model_providers(type);
CREATE INDEX IF NOT EXISTS idx_model_provider_status ON model_providers(status);
CREATE INDEX IF NOT EXISTS idx_model_provider_enabled ON model_providers(enabled);
CREATE INDEX IF NOT EXISTS idx_model_provider_priority ON model_providers(priority);

-- 模型配置索引
CREATE INDEX IF NOT EXISTS idx_model_config_code ON model_configurations(code);
CREATE INDEX IF NOT EXISTS idx_model_config_name ON model_configurations(name);
CREATE INDEX IF NOT EXISTS idx_model_config_provider_id ON model_configurations(provider_id);
CREATE INDEX IF NOT EXISTS idx_model_config_type ON model_configurations(type);
CREATE INDEX IF NOT EXISTS idx_model_config_status ON model_configurations(status);
CREATE INDEX IF NOT EXISTS idx_model_config_enabled ON model_configurations(enabled);
CREATE INDEX IF NOT EXISTS idx_model_config_priority ON model_configurations(priority);

-- 模型使用记录索引
CREATE INDEX IF NOT EXISTS idx_model_usage_model_id ON model_usage_records(model_id);
CREATE INDEX IF NOT EXISTS idx_model_usage_user_id ON model_usage_records(user_id);
CREATE INDEX IF NOT EXISTS idx_model_usage_session_id ON model_usage_records(session_id);
CREATE INDEX IF NOT EXISTS idx_model_usage_request_id ON model_usage_records(request_id);
CREATE INDEX IF NOT EXISTS idx_model_usage_status ON model_usage_records(status);
CREATE INDEX IF NOT EXISTS idx_model_usage_created_at ON model_usage_records(created_at);
CREATE INDEX IF NOT EXISTS idx_model_usage_date ON model_usage_records(usage_date);

-- ========================================
-- 外键约束
-- ========================================

-- 提示词分类外键
ALTER TABLE prompt_categories 
ADD CONSTRAINT fk_prompt_category_parent 
FOREIGN KEY (parent_id) REFERENCES prompt_categories(id);

-- 提示词模板外键
ALTER TABLE prompt_templates 
ADD CONSTRAINT fk_prompt_template_parent 
FOREIGN KEY (parent_id) REFERENCES prompt_templates(id);

ALTER TABLE prompt_templates 
ADD CONSTRAINT fk_prompt_template_root 
FOREIGN KEY (root_id) REFERENCES prompt_templates(id);

-- 模型配置外键
ALTER TABLE model_configurations 
ADD CONSTRAINT fk_model_config_provider 
FOREIGN KEY (provider_id) REFERENCES model_providers(id);

-- 模型使用记录外键
ALTER TABLE model_usage_records 
ADD CONSTRAINT fk_model_usage_model 
FOREIGN KEY (model_id) REFERENCES model_configurations(id);

-- ========================================
-- 触发器函数（更新时间戳）
-- ========================================

CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- 为所有表创建更新时间戳触发器
CREATE TRIGGER update_workflow_variables_updated_at BEFORE UPDATE ON workflow_variables FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_conversation_variables_updated_at BEFORE UPDATE ON conversation_variables FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_prompt_categories_updated_at BEFORE UPDATE ON prompt_categories FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_prompt_templates_updated_at BEFORE UPDATE ON prompt_templates FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_model_providers_updated_at BEFORE UPDATE ON model_providers FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_model_configurations_updated_at BEFORE UPDATE ON model_configurations FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_model_usage_records_updated_at BEFORE UPDATE ON model_usage_records FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
