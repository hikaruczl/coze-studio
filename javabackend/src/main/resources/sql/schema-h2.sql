-- ========================================
-- Coze Studio Database Schema (H2 - for testing)
-- ========================================

-- 工作流变量表
CREATE TABLE IF NOT EXISTS workflow_variables (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
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
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
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
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
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
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
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
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
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
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
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
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
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

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_workflow_variables_name ON workflow_variables(name);
CREATE INDEX IF NOT EXISTS idx_workflow_variables_user_id ON workflow_variables(user_id);
CREATE INDEX IF NOT EXISTS idx_workflow_variables_scope ON workflow_variables(scope);
CREATE INDEX IF NOT EXISTS idx_workflow_variables_workflow_id ON workflow_variables(workflow_id);

CREATE INDEX IF NOT EXISTS idx_conversation_variables_name ON conversation_variables(name);
CREATE INDEX IF NOT EXISTS idx_conversation_variables_user_id ON conversation_variables(user_id);
CREATE INDEX IF NOT EXISTS idx_conversation_variables_scope ON conversation_variables(scope);
CREATE INDEX IF NOT EXISTS idx_conversation_variables_conversation_id ON conversation_variables(conversation_id);

CREATE INDEX IF NOT EXISTS idx_prompt_categories_code ON prompt_categories(code);
CREATE INDEX IF NOT EXISTS idx_prompt_categories_parent_id ON prompt_categories(parent_id);

CREATE INDEX IF NOT EXISTS idx_prompt_templates_name ON prompt_templates(name);
CREATE INDEX IF NOT EXISTS idx_prompt_templates_user_id ON prompt_templates(user_id);
CREATE INDEX IF NOT EXISTS idx_prompt_templates_category ON prompt_templates(category);

CREATE INDEX IF NOT EXISTS idx_model_providers_code ON model_providers(code);
CREATE INDEX IF NOT EXISTS idx_model_providers_type ON model_providers(type);

CREATE INDEX IF NOT EXISTS idx_model_configurations_code ON model_configurations(code);
CREATE INDEX IF NOT EXISTS idx_model_configurations_provider_id ON model_configurations(provider_id);

CREATE INDEX IF NOT EXISTS idx_model_usage_records_model_id ON model_usage_records(model_id);
CREATE INDEX IF NOT EXISTS idx_model_usage_records_user_id ON model_usage_records(user_id);
CREATE INDEX IF NOT EXISTS idx_model_usage_records_usage_date ON model_usage_records(usage_date);
