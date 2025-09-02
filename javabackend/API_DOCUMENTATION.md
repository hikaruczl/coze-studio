# Coze Studio API 文档

## 概述

Coze Studio 是一个智能工作流自动化平台，提供完整的 REST API 接口，支持用户管理、应用创建、工作流设计、插件管理、知识库构建等功能。

## API 基本信息

- **Base URL**: `http://localhost:8080/api`
- **API 版本**: v1.0.0
- **认证方式**: JWT Bearer Token / API Key
- **响应格式**: JSON
- **字符编码**: UTF-8

## 认证方式

### JWT Bearer Token
```http
Authorization: Bearer <your-jwt-token>
```

### API Key
```http
X-API-Key: <your-api-key>
```

## 统一响应格式

所有 API 接口都使用统一的响应格式：

```json
{
  "code": "SUCCESS",
  "message": "操作成功",
  "data": {},
  "timestamp": 1642694400000
}
```

### 响应字段说明

| 字段 | 类型 | 说明 |
|------|------|------|
| code | String | 响应码，SUCCESS 表示成功 |
| message | String | 响应消息 |
| data | Object | 响应数据，可为 null |
| timestamp | Long | 时间戳 |

## API 接口分组

### 1. 用户管理 (`/api/users`)

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/register` | 用户注册 |
| POST | `/login` | 用户登录 |
| GET | `/profile` | 获取用户信息 |
| PUT | `/profile` | 更新用户信息 |
| POST | `/logout` | 用户登出 |

### 2. 应用管理 (`/api/applications`)

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/` | 创建应用 |
| GET | `/` | 获取应用列表 |
| GET | `/{id}` | 获取应用详情 |
| PUT | `/{id}` | 更新应用 |
| DELETE | `/{id}` | 删除应用 |
| POST | `/{id}/publish` | 发布应用 |

### 3. 对话管理 (`/api/conversations`)

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/` | 创建对话 |
| GET | `/` | 获取对话列表 |
| GET | `/{id}` | 获取对话详情 |
| POST | `/{id}/messages` | 发送消息 |
| GET | `/{id}/messages` | 获取消息历史 |

### 4. 工作流管理 (`/api/workflows`)

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/` | 创建工作流 |
| GET | `/` | 获取工作流列表 |
| GET | `/{id}` | 获取工作流详情 |
| PUT | `/{id}` | 更新工作流 |
| DELETE | `/{id}` | 删除工作流 |
| POST | `/{id}/execute` | 执行工作流 |
| POST | `/{id}/execute-async` | 异步执行工作流 |
| GET | `/executions/{executionId}/status` | 获取执行状态 |
| POST | `/executions/{executionId}/stop` | 停止执行 |

### 5. 工作流模板 (`/api/workflow-templates`)

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | `/` | 获取模板列表 |
| GET | `/{id}` | 获取模板详情 |
| POST | `/{id}/use` | 使用模板创建工作流 |
| GET | `/categories` | 获取模板分类 |
| GET | `/popular` | 获取热门模板 |

### 6. 插件管理 (`/api/plugins`)

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/` | 注册插件 |
| GET | `/` | 获取插件列表 |
| GET | `/search` | 搜索插件 |
| GET | `/popular` | 获取热门插件 |
| GET | `/{id}` | 获取插件详情 |
| PUT | `/{id}` | 更新插件 |
| DELETE | `/{id}` | 删除插件 |
| POST | `/{id}/install` | 安装插件 |
| POST | `/{id}/uninstall` | 卸载插件 |
| POST | `/{id}/execute` | 执行插件 |
| POST | `/{id}/test` | 测试插件 |
| POST | `/{id}/rate` | 评价插件 |
| GET | `/installed` | 获取已安装插件 |

### 7. 知识库管理 (`/api/knowledge-bases`)

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/` | 创建知识库 |
| GET | `/` | 获取知识库列表 |
| GET | `/{id}` | 获取知识库详情 |
| PUT | `/{id}` | 更新知识库 |
| DELETE | `/{id}` | 删除知识库 |
| POST | `/{id}/query` | RAG 查询 |
| POST | `/{id}/retrieve` | 文档检索 |
| GET | `/{id}/suggestions` | 获取查询建议 |
| GET | `/{id}/stats` | 获取知识库统计 |

### 8. 文档管理 (`/api/documents`)

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/upload` | 上传文档 |
| POST | `/batch-upload` | 批量上传文档 |
| GET | `/` | 获取文档列表 |
| GET | `/{id}` | 获取文档详情 |
| PUT | `/{id}` | 更新文档 |
| DELETE | `/{id}` | 删除文档 |
| POST | `/{id}/process` | 处理文档 |
| POST | `/{id}/process-async` | 异步处理文档 |
| GET | `/{id}/progress` | 获取处理进度 |
| POST | `/{id}/cancel-processing` | 取消处理 |
| GET | `/{id}/slices` | 获取文档切片 |

### 9. 向量存储管理 (`/api/vector-stores`)

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/` | 创建向量存储 |
| GET | `/` | 获取向量存储列表 |
| GET | `/{id}` | 获取向量存储详情 |
| PUT | `/{id}` | 更新向量存储 |
| DELETE | `/{id}` | 删除向量存储 |
| POST | `/{id}/vectors` | 存储向量 |
| POST | `/{id}/vectors/batch` | 批量存储向量 |
| POST | `/{id}/search` | 相似度搜索 |
| POST | `/{id}/hybrid-search` | 混合搜索 |
| DELETE | `/{id}/vectors/{vectorId}` | 删除向量 |
| GET | `/{id}/stats` | 获取存储统计 |
| GET | `/{id}/health` | 健康检查 |

### 10. 系统管理 (`/api/system`)

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | `/health` | 系统健康检查 |
| GET | `/info` | 系统信息 |
| GET | `/stats` | 系统统计 |
| GET | `/config` | 系统配置 |
| GET | `/metrics` | 系统指标 |
| POST | `/cache/clear` | 清理缓存 |
| GET | `/log-level` | 获取日志级别 |
| POST | `/log-level` | 设置日志级别 |

## 错误码说明

| 错误码 | HTTP状态码 | 说明 |
|--------|------------|------|
| SUCCESS | 200 | 操作成功 |
| INVALID_PARAMETER | 400 | 参数错误 |
| UNAUTHORIZED | 401 | 未认证 |
| FORBIDDEN | 403 | 无权限 |
| NOT_FOUND | 404 | 资源不存在 |
| METHOD_NOT_ALLOWED | 405 | 方法不允许 |
| CONFLICT | 409 | 资源冲突 |
| UNSUPPORTED_MEDIA_TYPE | 415 | 不支持的媒体类型 |
| INTERNAL_SERVER_ERROR | 500 | 服务器内部错误 |

## 分页参数

对于支持分页的接口，使用以下参数：

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| page | Integer | 0 | 页码（从0开始） |
| size | Integer | 20 | 每页大小 |
| sort | String | - | 排序字段，格式：field,direction |

### 分页响应格式

```json
{
  "code": "SUCCESS",
  "message": "操作成功",
  "data": {
    "content": [],
    "totalElements": 100,
    "totalPages": 5,
    "size": 20,
    "number": 0,
    "first": true,
    "last": false,
    "numberOfElements": 20
  },
  "timestamp": 1642694400000
}
```

## 请求示例

### 创建工作流

```bash
curl -X POST "http://localhost:8080/api/workflows" \
  -H "Authorization: Bearer <your-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "数据处理工作流",
    "description": "自动化数据处理流程",
    "category": "数据处理",
    "definition": {
      "nodes": [...],
      "edges": [...]
    }
  }'
```

### RAG 查询

```bash
curl -X POST "http://localhost:8080/api/knowledge-bases/1/query" \
  -H "Authorization: Bearer <your-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "query": "如何使用工作流？",
    "retrievalOptions": {
      "topK": 5,
      "searchType": "HYBRID"
    }
  }'
```

## SDK 支持

我们提供以下语言的 SDK：

- **JavaScript/TypeScript**: `@coze-studio/sdk-js`
- **Python**: `coze-studio-sdk`
- **Java**: `coze-studio-sdk-java`
- **Go**: `github.com/coze-dev/coze-studio-sdk-go`

## 限制说明

- **请求频率限制**: 每分钟最多 1000 次请求
- **文件上传限制**: 单个文件最大 100MB
- **并发执行限制**: 每个用户最多同时执行 10 个工作流
- **知识库文档限制**: 每个知识库最多 1000 个文档

## 联系我们

- **文档**: https://docs.coze-studio.com
- **GitHub**: https://github.com/coze-dev/coze-studio
- **邮箱**: support@coze-studio.com
- **社区**: https://community.coze-studio.com

## 更新日志

### v1.0.0 (2025-01-20)
- 初始版本发布
- 完整的 API 接口实现
- 支持工作流、插件、知识库等核心功能
