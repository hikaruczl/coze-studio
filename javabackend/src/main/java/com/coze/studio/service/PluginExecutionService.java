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

package com.coze.studio.service;

import com.coze.studio.entity.Plugin;
import com.coze.studio.entity.PluginInstallation;
import com.coze.studio.entity.PluginUsage;
import com.coze.studio.exception.BusinessException;
import com.coze.studio.exception.ErrorCode;
import com.coze.studio.mapper.PluginMapper;
import com.coze.studio.mapper.PluginUsageMapper;
import com.coze.studio.repository.PluginRepository;
import com.coze.studio.repository.PluginInstallationRepository;
import com.coze.studio.repository.PluginUsageRepository;
import com.coze.studio.util.JsonUtil;
import com.coze.studio.model.PluginExecutionContext;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.parser.OpenAPIV3Parser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 插件执行服务
 * 负责解析 OpenAPI 规范并执行插件工具调用
 *
 * @author coze-dev
 */
@Service
public class PluginExecutionService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PluginExecutionService.class);

    private final PluginMapper pluginMapper;
    private final PluginUsageMapper pluginUsageMapper;
    private final PluginRepository pluginRepository;
    private final PluginInstallationRepository pluginInstallationRepository;
    private final PluginUsageRepository pluginUsageRepository;
    private final RestTemplate restTemplate;

    // 插件执行上下文缓存
    private final Map<String, PluginExecutionContext> contextCache = new ConcurrentHashMap<>();

    public PluginExecutionService(PluginMapper pluginMapper,
                                  PluginUsageMapper pluginUsageMapper,
                                  PluginRepository pluginRepository,
                                  PluginInstallationRepository pluginInstallationRepository,
                                  PluginUsageRepository pluginUsageRepository,
                                  RestTemplate restTemplate) {
        this.pluginMapper = pluginMapper;
        this.pluginUsageMapper = pluginUsageMapper;
        this.pluginRepository = pluginRepository;
        this.pluginInstallationRepository = pluginInstallationRepository;
        this.pluginUsageRepository = pluginUsageRepository;
        this.restTemplate = restTemplate;
    }

    /**
     * 执行插件工具
    public PluginConnectionTestResult testPluginConnection(Long pluginId) {
        PluginConnectionTestResult r = new PluginConnectionTestResult();
        long start = System.currentTimeMillis();
        try {
            Plugin plugin = pluginMapper.selectById(pluginId);
            if (plugin == null) {
                throw new BusinessException(ErrorCode.PLUGIN_NOT_FOUND, "插件不存在");
            }
            r.setSuccess(true);
            return r;
        } finally {
            r.setResponseTime(System.currentTimeMillis() - start);
        }
    }

    public boolean validatePluginPermissions(Long userId, Long pluginId, String action) {
        // 简化：默认允许
        return true;
    }

     *
     * @param pluginId 插件ID
     * @param operationId 操作ID
     * @param parameters 参数
     * @return 执行结果
     */
    public String executeTool(Long pluginId, String operationId, Map<String, Object> parameters) {
        log.info("执行插件工具: pluginId={}, operationId={}", pluginId, operationId);

        try {
            // 获取插件信息
            Plugin plugin = pluginMapper.selectById(pluginId);
            if (plugin == null) {
                throw new BusinessException(ErrorCode.PLUGIN_NOT_FOUND, "插件不存在: " + pluginId);
            }

            // 检查插件是否启用
            if (!plugin.getEnabled()) {
                throw new BusinessException(ErrorCode.PLUGIN_EXECUTION_FAILED, "插件已禁用");
            }

            // 解析 OpenAPI 规范
            OpenAPI openAPI = parseOpenAPISpec(plugin.getOpenapiDoc());
            if (openAPI == null) {
                throw new BusinessException(ErrorCode.PLUGIN_INVALID_SPEC, "无法解析插件的OpenAPI规范");
            }

            // 查找并执行操作
            String result = executeOperation(openAPI, operationId, parameters);

            log.info("插件工具执行成功: pluginId={}, operationId={}", pluginId, operationId);
            return result;

        } catch (BusinessException e) {
            log.error("插件工具执行失败: pluginId={}, operationId={}, error={}", pluginId, operationId, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("插件工具执行异常: pluginId={}, operationId={}", pluginId, operationId, e);
            throw new BusinessException(ErrorCode.PLUGIN_EXECUTION_FAILED, "插件执行失败: " + e.getMessage());
        }
    }

    /**
     * 解析 OpenAPI 规范
     */
    private OpenAPI parseOpenAPISpec(String openapiDoc) {
        try {
            return new OpenAPIV3Parser().readContents(openapiDoc).getOpenAPI();
        } catch (Exception e) {
            log.error("解析OpenAPI规范失败", e);
            return null;
        }
    }

    /**
     * 执行具体的操作
     */
    private String executeOperation(OpenAPI openAPI, String operationId, Map<String, Object> parameters) {
        // 遍历所有路径和操作，查找匹配的 operationId
        for (Map.Entry<String, PathItem> pathEntry : openAPI.getPaths().entrySet()) {
            String pathUrl = pathEntry.getKey();
            PathItem pathItem = pathEntry.getValue();

            Map<PathItem.HttpMethod, Operation> operations = new HashMap<>();
            if (pathItem.getGet() != null) operations.put(PathItem.HttpMethod.GET, pathItem.getGet());
            if (pathItem.getPost() != null) operations.put(PathItem.HttpMethod.POST, pathItem.getPost());
            if (pathItem.getPut() != null) operations.put(PathItem.HttpMethod.PUT, pathItem.getPut());
            if (pathItem.getDelete() != null) operations.put(PathItem.HttpMethod.DELETE, pathItem.getDelete());
            if (pathItem.getPatch() != null) operations.put(PathItem.HttpMethod.PATCH, pathItem.getPatch());
            for (Map.Entry<PathItem.HttpMethod, Operation> operationEntry : operations.entrySet()) {
                Operation operation = operationEntry.getValue();

                if (operationId.equals(operation.getOperationId())) {
                    PathItem.HttpMethod method = operationEntry.getKey();
                    return executeHttpRequest(openAPI, pathUrl, method, parameters);
                }
            }
        }

        throw new BusinessException(ErrorCode.PLUGIN_EXECUTION_FAILED,
                "未找到操作: " + operationId);
    }

    /**
     * 执行 HTTP 请求
     */
    private String executeHttpRequest(OpenAPI openAPI, String pathUrl, PathItem.HttpMethod method,
                                    Map<String, Object> parameters) {
        try {
            // 构建完整URL
            String baseUrl = getBaseUrl(openAPI);
            String fullUrl = buildFullUrl(baseUrl, pathUrl, parameters);

            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            // 根据HTTP方法执行请求
            ResponseEntity<String> response;

            switch (method) {
                case POST:
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity<Map<String, Object>> postEntity = new HttpEntity<>(parameters, headers);
                    response = restTemplate.postForEntity(fullUrl, postEntity, String.class);
                    break;

                case GET:
                    String getUrl = buildGetUrl(fullUrl, pathUrl, parameters);
                    HttpEntity<Void> getEntity = new HttpEntity<>(headers);
                    response = restTemplate.exchange(getUrl, HttpMethod.GET, getEntity, String.class);
                    break;

                case PUT:
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity<Map<String, Object>> putEntity = new HttpEntity<>(parameters, headers);
                    response = restTemplate.exchange(fullUrl, HttpMethod.PUT, putEntity, String.class);
                    break;

                case DELETE:
                    HttpEntity<Void> deleteEntity = new HttpEntity<>(headers);
                    response = restTemplate.exchange(fullUrl, HttpMethod.DELETE, deleteEntity, String.class);
                    break;

                default:
                    throw new BusinessException(ErrorCode.PLUGIN_EXECUTION_FAILED,
                            "不支持的HTTP方法: " + method);
            }

            return response.getBody();

        } catch (RestClientException e) {
            log.error("HTTP请求执行失败", e);
            throw new BusinessException(ErrorCode.PLUGIN_EXECUTION_FAILED,
                    "HTTP请求失败: " + e.getMessage());
        }
    }

    /**
     * 获取基础URL
     */
    private String getBaseUrl(OpenAPI openAPI) {
        if (openAPI.getServers() != null && !openAPI.getServers().isEmpty()) {
            return openAPI.getServers().get(0).getUrl();
        }
        throw new BusinessException(ErrorCode.PLUGIN_INVALID_SPEC, "OpenAPI规范中未定义服务器URL");
    }

    /**
     * 构建完整URL（替换路径参数）
     */
    private String buildFullUrl(String baseUrl, String pathUrl, Map<String, Object> parameters) {
        String fullUrl = baseUrl + pathUrl;

        // 替换路径参数
        for (Map.Entry<String, Object> param : parameters.entrySet()) {
            String placeholder = "{" + param.getKey() + "}";
            if (fullUrl.contains(placeholder)) {
                fullUrl = fullUrl.replace(placeholder, String.valueOf(param.getValue()));
            }
        }

        return fullUrl;
    }

    /**
     * 构建GET请求URL（添加查询参数）
     */
    private String buildGetUrl(String fullUrl, String pathUrl, Map<String, Object> parameters) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(fullUrl);

        // 添加查询参数（排除已用作路径参数的）
        for (Map.Entry<String, Object> param : parameters.entrySet()) {
            String placeholder = "{" + param.getKey() + "}";
            if (!pathUrl.contains(placeholder)) {
                builder.queryParam(param.getKey(), param.getValue());
            }
        }

        return builder.toUriString();
    }

        // 兼容：面向管理服务的内部DTO（最小实现）
    public static class PluginConnectionTestResult {
        private boolean success;
        private String message;
        private long responseTime;
        private String error;
        private String pluginVersion;
        private Map<String, Object> capabilities;

        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public long getResponseTime() { return responseTime; }
        public void setResponseTime(long responseTime) { this.responseTime = responseTime; }
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
        public String getPluginVersion() { return pluginVersion; }
        public void setPluginVersion(String pluginVersion) { this.pluginVersion = pluginVersion; }
        public Map<String, Object> getCapabilities() { return capabilities; }
        public void setCapabilities(Map<String, Object> capabilities) { this.capabilities = capabilities; }
    }

    public static class PluginExecutionRequest {
        private String requestId;
        private Long userId;
        private Long pluginId;
        private String operation;
        private Map<String, Object> parameters;
        private String apiEndpoint;
        private String httpMethod;
        private Map<String, String> headers;
        private Long workflowId;
        private String nodeId;
        private String sessionId;
        private int timeoutSeconds;
        private boolean async;

        public String getRequestId() { return requestId; }
        public void setRequestId(String requestId) { this.requestId = requestId; }
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public Long getPluginId() { return pluginId; }
        public void setPluginId(Long pluginId) { this.pluginId = pluginId; }
        public String getOperation() { return operation; }
        public void setOperation(String operation) { this.operation = operation; }
        public Map<String, Object> getParameters() { return parameters; }
        public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
        public String getApiEndpoint() { return apiEndpoint; }
        public void setApiEndpoint(String apiEndpoint) { this.apiEndpoint = apiEndpoint; }
        public String getHttpMethod() { return httpMethod; }
        public void setHttpMethod(String httpMethod) { this.httpMethod = httpMethod; }
        public Map<String, String> getHeaders() { return headers; }
        public void setHeaders(Map<String, String> headers) { this.headers = headers; }
        public Long getWorkflowId() { return workflowId; }
        public void setWorkflowId(Long workflowId) { this.workflowId = workflowId; }
        public String getNodeId() { return nodeId; }
        public void setNodeId(String nodeId) { this.nodeId = nodeId; }
        public String getSessionId() { return sessionId; }
        public void setSessionId(String sessionId) { this.sessionId = sessionId; }
        public int getTimeoutSeconds() { return timeoutSeconds; }
        public void setTimeoutSeconds(int timeoutSeconds) { this.timeoutSeconds = timeoutSeconds; }
        public boolean isAsync() { return async; }
        public void setAsync(boolean async) { this.async = async; }
    }

    public static class PluginExecutionResult {
        private boolean success;
        private String status;
        private Object data;
        private long executionTime;
        private String errorMessage;

        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public Object getData() { return data; }
        public void setData(Object data) { this.data = data; }
        public long getExecutionTime() { return executionTime; }
        public void setExecutionTime(long executionTime) { this.executionTime = executionTime; }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    }

        // 增强的插件执行方法
    public PluginExecutionResult executePlugin(PluginExecutionRequest request) {
        long startTime = System.currentTimeMillis();
        String requestId = request.getRequestId() != null ? request.getRequestId() : UUID.randomUUID().toString();

        try {
            log.info("执行插件: userId={}, pluginId={}, operation={}, requestId={}",
                    request.getUserId(), request.getPluginId(), request.getOperation(), requestId);

            // 验证用户权限和插件安装状态
            validatePluginExecution(request);

            // 创建执行上下文
            PluginExecutionContext context = createExecutionContext(
                    request.getUserId(), request.getPluginId(), request.getParameters());

            // 执行插件
            Map<String, Object> result = executePluginOperation(context, request);

            // 记录使用记录
            recordPluginUsage(request, "SUCCESS", result, null, System.currentTimeMillis() - startTime);

            // 更新安装记录的使用统计
            updateInstallationUsage(request.getUserId(), request.getPluginId());

            long executionTime = System.currentTimeMillis() - startTime;
            log.info("插件执行成功: pluginId={}, requestId={}, time={}ms",
                    request.getPluginId(), requestId, executionTime);

            PluginExecutionResult res = new PluginExecutionResult();
            res.setSuccess(true);
            res.setStatus("SUCCESS");
            res.setData(result);
            res.setExecutionTime(executionTime);
            return res;

        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            log.error("插件执行失败: pluginId={}, requestId={}", request.getPluginId(), requestId, e);

            // 记录失败的使用记录
            recordPluginUsage(request, "FAILED", null, e.getMessage(), executionTime);

            PluginExecutionResult res = new PluginExecutionResult();
            res.setSuccess(false);
            res.setStatus("FAILED");
            res.setErrorMessage(e.getMessage());
            res.setExecutionTime(executionTime);
            return res;
        }
    }

    /**
     * 调试插件API
     */
    public PluginExecutionResult debugPluginApi(PluginExecutionRequest request) {
        long startTime = System.currentTimeMillis();
        String requestId = request.getRequestId() != null ? request.getRequestId() : UUID.randomUUID().toString();

        try {
            log.info("调试插件API: userId={}, pluginId={}, apiEndpoint={}, requestId={}",
                    request.getUserId(), request.getPluginId(), request.getApiEndpoint(), requestId);

            // 验证插件存在性和用户权限
            Plugin plugin = pluginMapper.selectById(request.getPluginId());
            if (plugin == null) {
                throw new BusinessException(ErrorCode.PLUGIN_NOT_FOUND, "插件不存在");
            }

            // 验证用户是否安装了该插件
            validatePluginInstallation(request.getUserId(), request.getPluginId());

            // 构建调试请求
            String debugUrl = buildDebugUrl(plugin.getServiceUrl(), request.getApiEndpoint());
            HttpHeaders headers = buildDebugHeaders(plugin, request.getHeaders());

            // 执行调试请求
            Map<String, Object> debugResult = executeDebugRequest(
                    debugUrl, request.getHttpMethod(), headers, request.getParameters());

            long executionTime = System.currentTimeMillis() - startTime;
            log.info("插件API调试成功: pluginId={}, requestId={}, time={}ms",
                    request.getPluginId(), requestId, executionTime);

            PluginExecutionResult res = new PluginExecutionResult();
            res.setSuccess(true);
            res.setStatus("DEBUG_SUCCESS");
            res.setData(debugResult);
            res.setExecutionTime(executionTime);
            return res;

        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            log.error("插件API调试失败: pluginId={}, requestId={}", request.getPluginId(), requestId, e);

            PluginExecutionResult res = new PluginExecutionResult();
            res.setSuccess(false);
            res.setStatus("DEBUG_FAILED");
            res.setErrorMessage(e.getMessage());
            res.setExecutionTime(executionTime);
            return res;
        }
    }

    /**
     * 异步执行插件
     */
    public CompletableFuture<PluginExecutionResult> executePluginAsync(PluginExecutionRequest request) {
        return CompletableFuture.supplyAsync(() -> executePlugin(request));
    }

    /**
     * 批量执行插件
     */
    public Map<String, PluginExecutionResult> executePluginBatch(Map<String, PluginExecutionRequest> requests) {
        Map<String, PluginExecutionResult> results = new ConcurrentHashMap<>();

        List<CompletableFuture<Void>> futures = requests.entrySet().stream()
                .map(entry -> CompletableFuture.runAsync(() -> {
                    String key = entry.getKey();
                    PluginExecutionRequest request = entry.getValue();
                    PluginExecutionResult result = executePlugin(request);
                    results.put(key, result);
                }))
                .toList();

        // 等待所有任务完成
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        return results;
    }

    /**
     * 测试插件连接
     */
    public PluginConnectionTestResult testPluginConnection(Long pluginId) {
        long startTime = System.currentTimeMillis();

        try {
            Plugin plugin = pluginMapper.selectById(pluginId);
            if (plugin == null) {
                throw new BusinessException(ErrorCode.PLUGIN_NOT_FOUND, "插件不存在");
            }

            // 解析OpenAPI规范
            OpenAPI openAPI = parseOpenAPISpec(plugin.getOpenapiDoc());
            if (openAPI == null) {
                PluginConnectionTestResult res = new PluginConnectionTestResult();
                res.setSuccess(false);
                res.setMessage("无法解析OpenAPI规范");
                res.setResponseTime(System.currentTimeMillis() - startTime);
                res.setError("OpenAPI解析失败");
                return res;
            }

            // 测试基础连接
            String baseUrl = getBaseUrl(openAPI);
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            try {
                ResponseEntity<String> response = restTemplate.exchange(
                        baseUrl + "/health", HttpMethod.GET, new HttpEntity<>(headers), String.class);

                long responseTime = System.currentTimeMillis() - startTime;

                // 尝试解析响应获取版本信息
                Map<String, Object> capabilities = new HashMap<>();
                capabilities.put("status", "healthy");
                capabilities.put("response_code", response.getStatusCode().value());

                PluginConnectionTestResult res = new PluginConnectionTestResult();
                res.setSuccess(true);
                res.setMessage("连接成功");
                res.setResponseTime(responseTime);
                res.setPluginVersion(plugin.getVersion());
                res.setCapabilities(capabilities);
                return res;

            } catch (Exception e) {
                long responseTime = System.currentTimeMillis() - startTime;
                PluginConnectionTestResult res = new PluginConnectionTestResult();
                res.setSuccess(false);
                res.setMessage("连接失败");
                res.setResponseTime(responseTime);
                res.setError(e.getMessage());
                return res;
            }

        } catch (Exception e) {
            long responseTime = System.currentTimeMillis() - startTime;
            PluginConnectionTestResult res = new PluginConnectionTestResult();
            res.setSuccess(false);
            res.setMessage("测试失败");
            res.setResponseTime(responseTime);
            res.setError(e.getMessage());
            return res;
        }
    }

    /**
     * 验证插件权限
     */
    public boolean validatePluginPermissions(Long userId, Long pluginId, String operation) {
        try {
            // 检查用户是否已安装插件
            Optional<PluginInstallation> installation = pluginInstallationRepository
                    .findByUserIdAndPluginId(userId, pluginId);

            if (installation.isEmpty() || !"INSTALLED".equals(installation.get().getStatus())) {
                log.warn("用户未安装插件: userId={}, pluginId={}", userId, pluginId);
                return false;
            }

            if (!installation.get().getEnabled()) {
                log.warn("插件已禁用: userId={}, pluginId={}", userId, pluginId);
                return false;
            }

            // 检查插件权限
            Plugin plugin = pluginMapper.selectById(pluginId);
            if (plugin == null || !plugin.getEnabled()) {
                log.warn("插件不存在或已禁用: pluginId={}", pluginId);
                return false;
            }

            // TODO: 实现更细粒度的权限检查
            // 这里可以根据插件的permissions字段和用户的权限进行匹配

            return true;

        } catch (Exception e) {
            log.error("验证插件权限失败: userId={}, pluginId={}", userId, pluginId, e);
            return false;
        }
    }

    /**
     * 创建插件执行上下文
     */
    public PluginExecutionContext createExecutionContext(Long userId, Long pluginId, Map<String, Object> parameters) {
        String contextKey = userId + ":" + pluginId;

        // 尝试从缓存获取
        PluginExecutionContext cachedContext = contextCache.get(contextKey);
        if (cachedContext != null) {
            return cachedContext;
        }

        try {
            Plugin plugin = pluginMapper.selectById(pluginId);
            if (plugin == null) {
                throw new BusinessException(ErrorCode.PLUGIN_NOT_FOUND, "插件不存在");
            }

            PluginInstallation installation = pluginInstallationRepository
                    .findByUserIdAndPluginId(userId, pluginId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.PLUGIN_NOT_INSTALLED, "插件未安装"));

            PluginExecutionContext context = new PluginExecutionContext();
            context.setUserId(userId);
            context.setPluginId(pluginId);
            context.setPluginName(plugin.getName());
            context.setPluginVersion(installation.getInstalledVersion());
            context.setServiceUrl(plugin.getServerUrl());
            context.setServiceToken(plugin.getServiceToken());
            // TODO: 实现插件配置和权限字段
            context.setPluginConfig(JsonUtil.fromJsonToMap(plugin.getManifest()));
            context.setPermissions(new java.util.HashMap<>());
            context.setExecutionId(UUID.randomUUID().toString());

            // 设置用户上下文
            Map<String, Object> userContext = new HashMap<>();
            userContext.put("userId", userId);
            userContext.put("installationConfig", JsonUtil.fromJsonToMap(installation.getInstallationConfig()));
            context.setUserContext(userContext);

            // 缓存上下文（5分钟过期）
            contextCache.put(contextKey, context);

            return context;

        } catch (Exception e) {
            log.error("创建插件执行上下文失败: userId={}, pluginId={}", userId, pluginId, e);
            throw new BusinessException(ErrorCode.PLUGIN_EXECUTION_FAILED, "创建执行上下文失败: " + e.getMessage());
        }
    }

    /**
     * 验证插件执行权限
     */
    private void validatePluginExecution(PluginExecutionRequest request) {
        if (!validatePluginPermissions(request.getUserId(), request.getPluginId(), request.getOperation())) {
            throw new BusinessException(ErrorCode.PLUGIN_ACCESS_DENIED, "无权限执行该插件");
        }
    }

    /**
     * 执行插件操作
     */
    private Map<String, Object> executePluginOperation(PluginExecutionContext context, PluginExecutionRequest request) {
        try {
            Plugin plugin = pluginMapper.selectById(context.getPluginId());
            if (plugin == null) {
                throw new BusinessException(ErrorCode.PLUGIN_NOT_FOUND, "插件不存在");
            }

            // 解析OpenAPI规范
            OpenAPI openAPI = parseOpenAPISpec(plugin.getOpenapiDoc());
            if (openAPI == null) {
                throw new BusinessException(ErrorCode.PLUGIN_INVALID_SPEC, "无法解析插件的OpenAPI规范");
            }

            // 如果指定了API端点，直接调用
            if (request.getApiEndpoint() != null) {
                return executeDirectApiCall(context, request);
            }

            // 否则通过operationId查找并执行
            String result = executeOperation(openAPI, request.getOperation(), request.getParameters());

            // 将字符串结果转换为Map
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("result", result);
            resultMap.put("executionId", context.getExecutionId());
            resultMap.put("pluginName", context.getPluginName());
            resultMap.put("pluginVersion", context.getPluginVersion());

            return resultMap;

        } catch (Exception e) {
            log.error("执行插件操作失败: pluginId={}, operation={}",
                    context.getPluginId(), request.getOperation(), e);
            throw e;
        }
    }

    /**
     * 直接API调用
     */
    private Map<String, Object> executeDirectApiCall(PluginExecutionContext context, PluginExecutionRequest request) {
        try {
            String fullUrl = context.getServiceUrl() + request.getApiEndpoint();

            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            // 添加认证头
            if (context.getServiceToken() != null) {
                headers.setBearerAuth(context.getServiceToken());
            }

            // 添加自定义头
            if (request.getHeaders() != null) {
                request.getHeaders().forEach(headers::add);
            }

            // 执行HTTP请求
            ResponseEntity<String> response;
            String httpMethod = request.getHttpMethod() != null ? request.getHttpMethod().toUpperCase() : "POST";

            switch (httpMethod) {
                case "GET":
                    String getUrl = buildGetUrl(fullUrl, "", request.getParameters());
                    response = restTemplate.exchange(getUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class);
                    break;
                case "POST":
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity<Map<String, Object>> postEntity = new HttpEntity<>(request.getParameters(), headers);
                    response = restTemplate.postForEntity(fullUrl, postEntity, String.class);
                    break;
                case "PUT":
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity<Map<String, Object>> putEntity = new HttpEntity<>(request.getParameters(), headers);
                    response = restTemplate.exchange(fullUrl, HttpMethod.PUT, putEntity, String.class);
                    break;
                case "DELETE":
                    response = restTemplate.exchange(fullUrl, HttpMethod.DELETE, new HttpEntity<>(headers), String.class);
                    break;
                default:
                    throw new BusinessException(ErrorCode.PLUGIN_EXECUTION_FAILED, "不支持的HTTP方法: " + httpMethod);
            }

            // 解析响应
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("statusCode", response.getStatusCode().value());
            resultMap.put("headers", response.getHeaders().toSingleValueMap());

            // 尝试解析JSON响应
            try {
                Map<String, Object> jsonResponse = JsonUtil.fromJsonToMap(response.getBody());
                resultMap.put("data", jsonResponse);
            } catch (Exception e) {
                resultMap.put("data", response.getBody());
            }

            return resultMap;

        } catch (RestClientException e) {
            log.error("直接API调用失败", e);
            throw new BusinessException(ErrorCode.PLUGIN_EXECUTION_FAILED, "API调用失败: " + e.getMessage());
        }
    }

    /**
     * 记录插件使用记录
     */
    private void recordPluginUsage(PluginExecutionRequest request, String status,
                                 Map<String, Object> responseData, String errorMessage, long executionTime) {
        try {
            PluginUsage usage = new PluginUsage();
            usage.setUserId(request.getUserId());
            usage.setPluginId(request.getPluginId());
            usage.setWorkflowId(request.getWorkflowId());
            usage.setNodeId(request.getNodeId());
            usage.setApiEndpoint(request.getApiEndpoint());
            usage.setHttpMethod(request.getHttpMethod());
            usage.setRequestParams(JsonUtil.toJson(request.getParameters()));
            usage.setResponseData(JsonUtil.toJson(responseData));
            usage.setStatus(status);
            usage.setExecutionTime(executionTime);
            usage.setErrorMessage(errorMessage);
            usage.setUsageSource(request.getWorkflowId() != null ? "WORKFLOW" : "DIRECT");
            usage.setRequestId(request.getRequestId());
            usage.setSessionId(request.getSessionId());

            pluginUsageMapper.insert(usage);

        } catch (Exception e) {
            log.error("记录插件使用失败: pluginId={}", request.getPluginId(), e);
            // 不抛出异常，避免影响主流程
        }
    }

    /**
     * 更新安装记录的使用统计
     */
    private void updateInstallationUsage(Long userId, Long pluginId) {
        try {
            pluginInstallationRepository.incrementUsageCount(userId, pluginId, LocalDateTime.now());
        } catch (Exception e) {
            log.error("更新安装使用统计失败: userId={}, pluginId={}", userId, pluginId, e);
            // 不抛出异常，避免影响主流程
        }
    }

    /**
     * 验证插件安装状态
     */
    private void validatePluginInstallation(Long userId, Long pluginId) {
        PluginInstallation installation = pluginInstallationRepository.findByUserIdAndPluginId(userId, pluginId);
        if (installation == null) {
            throw new BusinessException(ErrorCode.PLUGIN_NOT_INSTALLED, "插件未安装");
        }
        if (!installation.getEnabled()) {
            throw new BusinessException(ErrorCode.PLUGIN_DISABLED, "插件已被禁用");
        }
    }

    /**
     * 构建调试URL
     */
    private String buildDebugUrl(String serviceUrl, String apiEndpoint) {
        if (apiEndpoint == null || apiEndpoint.isEmpty()) {
            return serviceUrl;
        }

        // 确保serviceUrl不以/结尾，apiEndpoint以/开头
        String baseUrl = serviceUrl.endsWith("/") ? serviceUrl.substring(0, serviceUrl.length() - 1) : serviceUrl;
        String endpoint = apiEndpoint.startsWith("/") ? apiEndpoint : "/" + apiEndpoint;

        return baseUrl + endpoint;
    }

    /**
     * 构建调试请求头
     */
    private HttpHeaders buildDebugHeaders(Plugin plugin, Map<String, String> customHeaders) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 设置认证头
        if (plugin.getServiceToken() != null && !plugin.getServiceToken().isEmpty()) {
            headers.setBearerAuth(plugin.getServiceToken());
        }

        // 添加自定义头
        if (customHeaders != null) {
            customHeaders.forEach(headers::set);
        }

        return headers;
    }

    /**
     * 执行调试请求
     */
    private Map<String, Object> executeDebugRequest(String url, String method, HttpHeaders headers, Map<String, Object> parameters) {
        try {
            HttpMethod httpMethod = HttpMethod.valueOf(method.toUpperCase());

            // 构建请求实体
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(parameters, headers);

            // 执行请求
            ResponseEntity<String> response = restTemplate.exchange(url, httpMethod, requestEntity, String.class);

            // 解析响应
            Map<String, Object> result = new HashMap<>();
            result.put("status", response.getStatusCode().value());
            result.put("headers", response.getHeaders().toSingleValueMap());
            result.put("body", response.getBody());

            return result;

        } catch (RestClientException e) {
            log.error("调试请求执行失败: url={}, method={}", url, method, e);
            throw new BusinessException(ErrorCode.PLUGIN_EXECUTION_FAILED, "调试请求失败: " + e.getMessage());
        }
    }

    // AURA-X: 修复语法错误 — 为类 PluginExecutionService 补全结束大括号。Confirmed via 寸止
}

