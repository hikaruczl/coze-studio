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

package com.coze.studio.service.impl;

import com.coze.studio.service.HttpClientService;
import com.coze.studio.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * HTTP客户端服务实现类
 * 
 * @author coze-dev
 */
@Slf4j
@Service
public class HttpClientServiceImpl implements HttpClientService {

    private final RestTemplate restTemplate;

    public HttpClientServiceImpl() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public HttpResponse get(String url, Map<String, String> headers, Map<String, Object> params) {
        long startTime = System.currentTimeMillis();
        
        try {
            // 构建URL参数
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
            if (params != null) {
                params.forEach((key, value) -> builder.queryParam(key, value));
            }
            
            // 构建请求头
            HttpHeaders httpHeaders = new HttpHeaders();
            if (headers != null) {
                headers.forEach(httpHeaders::set);
            }
            
            HttpEntity<?> entity = new HttpEntity<>(httpHeaders);
            
            ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(), 
                HttpMethod.GET, 
                entity, 
                String.class
            );
            
            long responseTime = System.currentTimeMillis() - startTime;
            Map<String, String> responseHeaders = extractHeaders(response.getHeaders());
            
            return new HttpResponse(
                response.getStatusCode().value(),
                response.getBody(),
                responseHeaders,
                responseTime
            );
            
        } catch (Exception e) {
            log.error("HTTP GET请求失败: url={}", url, e);
            long responseTime = System.currentTimeMillis() - startTime;
            return new HttpResponse(500, "Request failed: " + e.getMessage(), new HashMap<>(), responseTime);
        }
    }

    @Override
    public HttpResponse post(String url, Map<String, String> headers, Object body) {
        return executeRequest(url, HttpMethod.POST, headers, body);
    }

    @Override
    public HttpResponse put(String url, Map<String, String> headers, Object body) {
        return executeRequest(url, HttpMethod.PUT, headers, body);
    }

    @Override
    public HttpResponse delete(String url, Map<String, String> headers) {
        return executeRequest(url, HttpMethod.DELETE, headers, null);
    }

    @Override
    public HttpResponse patch(String url, Map<String, String> headers, Object body) {
        return executeRequest(url, HttpMethod.PATCH, headers, body);
    }

    private HttpResponse executeRequest(String url, HttpMethod method, Map<String, String> headers, Object body) {
        long startTime = System.currentTimeMillis();
        
        try {
            // 构建请求头
            HttpHeaders httpHeaders = new HttpHeaders();
            if (headers != null) {
                headers.forEach(httpHeaders::set);
            }
            
            // 设置Content-Type
            if (body != null && !httpHeaders.containsKey(HttpHeaders.CONTENT_TYPE)) {
                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            }
            
            // 序列化请求体
            String requestBody = null;
            if (body != null) {
                if (body instanceof String) {
                    requestBody = (String) body;
                } else {
                    requestBody = JsonUtil.toJson(body);
                }
            }
            
            HttpEntity<String> entity = new HttpEntity<>(requestBody, httpHeaders);
            
            ResponseEntity<String> response = restTemplate.exchange(
                url, 
                method, 
                entity, 
                String.class
            );
            
            long responseTime = System.currentTimeMillis() - startTime;
            Map<String, String> responseHeaders = extractHeaders(response.getHeaders());
            
            return new HttpResponse(
                response.getStatusCode().value(),
                response.getBody(),
                responseHeaders,
                responseTime
            );
            
        } catch (Exception e) {
            log.error("HTTP {}请求失败: url={}", method, url, e);
            long responseTime = System.currentTimeMillis() - startTime;
            return new HttpResponse(500, "Request failed: " + e.getMessage(), new HashMap<>(), responseTime);
        }
    }

    private Map<String, String> extractHeaders(HttpHeaders httpHeaders) {
        Map<String, String> headers = new HashMap<>();
        httpHeaders.forEach((key, values) -> {
            if (!values.isEmpty()) {
                headers.put(key, values.get(0));
            }
        });
        return headers;
    }
}
