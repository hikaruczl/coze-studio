-- ========================================
-- Coze Studio 初始化数据
-- ========================================

-- 插入默认提示词分类
INSERT INTO prompt_categories (code, name, display_name, description, level, path, sort_order, icon, color, enabled, is_system) VALUES
('ai_writing', 'AI写作', 'AI写作助手', '用于各种写作任务的AI提示词模板', 1, '/ai_writing', 1, 'edit', '#1890ff', true, true),
('code_generation', '代码生成', '代码生成助手', '用于代码生成和编程辅助的提示词模板', 1, '/code_generation', 2, 'code', '#52c41a', true, true),
('data_analysis', '数据分析', '数据分析助手', '用于数据分析和处理的提示词模板', 1, '/data_analysis', 3, 'bar-chart', '#fa8c16', true, true),
('translation', '翻译', '翻译助手', '用于多语言翻译的提示词模板', 1, '/translation', 4, 'global', '#722ed1', true, true),
('customer_service', '客服', '客服助手', '用于客户服务的提示词模板', 1, '/customer_service', 5, 'customer-service', '#eb2f96', true, true);

-- 插入子分类
INSERT INTO prompt_categories (code, name, display_name, description, parent_id, level, path, sort_order, icon, color, enabled, is_system) VALUES
('blog_writing', '博客写作', '博客文章写作', '专门用于博客文章创作的提示词', 1, 2, '/ai_writing/blog_writing', 1, 'file-text', '#1890ff', true, true),
('technical_writing', '技术写作', '技术文档写作', '用于技术文档和说明书写作的提示词', 1, 2, '/ai_writing/technical_writing', 2, 'book', '#1890ff', true, true),
('frontend_code', '前端代码', '前端开发', '用于前端开发的代码生成提示词', 2, 2, '/code_generation/frontend_code', 1, 'html5', '#52c41a', true, true),
('backend_code', '后端代码', '后端开发', '用于后端开发的代码生成提示词', 2, 2, '/code_generation/backend_code', 2, 'api', '#52c41a', true, true);

-- 插入默认提示词模板
INSERT INTO prompt_templates (name, display_name, description, content, type, category, tags, language, version, user_id, is_public, is_system, parameters, variables) VALUES
('blog_writer', '博客写作助手', '帮助用户创作高质量博客文章的AI助手', 
'你是一个专业的博客写作助手。请根据以下信息创作一篇博客文章：

主题：${topic}
目标受众：${audience}
文章长度：${length}
写作风格：${style}

要求：
1. 文章结构清晰，逻辑性强
2. 内容有吸引力和可读性
3. 包含实用的建议或见解
4. 适合目标受众的阅读水平

请开始创作：', 
'USER', 'blog_writing', '写作,博客,内容创作', 'zh-CN', '1.0.0', 1, true, true,
'{"temperature": 0.7, "max_tokens": 2000}',
'{"topic": "string", "audience": "string", "length": "string", "style": "string"}'),

('code_reviewer', '代码审查助手', '专业的代码审查和优化建议助手', 
'你是一个资深的代码审查专家。请对以下代码进行详细审查：

编程语言：${language}
代码内容：
```${language}
${code}
```

请从以下方面进行审查：
1. 代码质量和可读性
2. 性能优化建议
3. 安全性问题
4. 最佳实践建议
5. 潜在的bug或问题

请提供具体的改进建议：', 
'SYSTEM', 'backend_code', '代码审查,编程,质量', 'zh-CN', '1.0.0', 1, true, true,
'{"temperature": 0.3, "max_tokens": 1500}',
'{"language": "string", "code": "string"}'),

('data_analyst', '数据分析助手', '专业的数据分析和洞察生成助手', 
'你是一个专业的数据分析师。请分析以下数据并提供洞察：

数据类型：${data_type}
分析目标：${objective}
数据内容：${data}

请提供：
1. 数据概览和基本统计
2. 关键趋势和模式
3. 异常值分析
4. 业务洞察和建议
5. 可视化建议

分析结果：', 
'ASSISTANT', 'data_analysis', '数据分析,统计,洞察', 'zh-CN', '1.0.0', 1, true, true,
'{"temperature": 0.4, "max_tokens": 1800}',
'{"data_type": "string", "objective": "string", "data": "string"}');

-- 插入默认模型提供商
INSERT INTO model_providers (code, name, display_name, description, type, base_url, auth_type, status, enabled, is_default, priority, timeout_ms, max_concurrent_requests, supported_features) VALUES
('openai', 'OpenAI', 'OpenAI', 'OpenAI GPT系列模型提供商', 'OPENAI', 'https://api.openai.com/v1', 'API_KEY', 'ACTIVE', true, true, 1, 30000, 10, '["chat_completion", "text_generation", "embedding"]'),
('anthropic', 'Anthropic', 'Anthropic', 'Anthropic Claude系列模型提供商', 'ANTHROPIC', 'https://api.anthropic.com', 'API_KEY', 'ACTIVE', true, false, 2, 30000, 10, '["chat_completion", "text_generation"]'),
('google', 'Google', 'Google AI', 'Google Gemini/PaLM系列模型提供商', 'GOOGLE', 'https://generativelanguage.googleapis.com/v1', 'API_KEY', 'ACTIVE', true, false, 3, 30000, 10, '["chat_completion", "text_generation", "embedding"]');

-- 插入默认模型配置
INSERT INTO model_configurations (code, name, display_name, description, provider_id, type, provider_model_id, status, enabled, is_default, priority, max_input_tokens, max_output_tokens, input_price_per_1k, output_price_per_1k, currency, supported_features) VALUES
('gpt-4', 'GPT-4', 'GPT-4', 'OpenAI GPT-4 模型', 1, 'CHAT_COMPLETION', 'gpt-4', 'ACTIVE', true, true, 1, 8192, 4096, 0.03, 0.06, 'USD', '["chat_completion", "function_calling"]'),
('gpt-3.5-turbo', 'GPT-3.5 Turbo', 'GPT-3.5 Turbo', 'OpenAI GPT-3.5 Turbo 模型', 1, 'CHAT_COMPLETION', 'gpt-3.5-turbo', 'ACTIVE', true, false, 2, 4096, 4096, 0.001, 0.002, 'USD', '["chat_completion", "function_calling"]'),
('claude-3-opus', 'Claude 3 Opus', 'Claude 3 Opus', 'Anthropic Claude 3 Opus 模型', 2, 'CHAT_COMPLETION', 'claude-3-opus-20240229', 'ACTIVE', true, false, 3, 200000, 4096, 0.015, 0.075, 'USD', '["chat_completion"]'),
('gemini-pro', 'Gemini Pro', 'Gemini Pro', 'Google Gemini Pro 模型', 3, 'CHAT_COMPLETION', 'gemini-pro', 'ACTIVE', true, false, 4, 30720, 2048, 0.0005, 0.0015, 'USD', '["chat_completion"]');

-- 插入示例工作流变量
INSERT INTO workflow_variables (name, value, type, scope, user_id, description, is_sensitive, is_readonly, access_count) VALUES
('system_version', '1.0.0', 'STRING', 'GLOBAL', 1, '系统版本号', false, true, 0),
('default_language', 'zh-CN', 'STRING', 'GLOBAL', 1, '默认语言设置', false, false, 0),
('max_retry_count', '3', 'NUMBER', 'GLOBAL', 1, '最大重试次数', false, false, 0),
('debug_mode', 'false', 'BOOLEAN', 'GLOBAL', 1, '调试模式开关', false, false, 0);

-- 插入示例对话变量
INSERT INTO conversation_variables (name, value, type, scope, user_id, description, is_sensitive, is_readonly, access_count) VALUES
('user_name', 'Guest', 'STRING', 'CONVERSATION', 1, '用户名称', false, false, 0),
('conversation_context', '{}', 'OBJECT', 'CONVERSATION', 1, '对话上下文', false, false, 0),
('user_preferences', '{"language": "zh-CN", "theme": "light"}', 'OBJECT', 'CONVERSATION', 1, '用户偏好设置', false, false, 0);
