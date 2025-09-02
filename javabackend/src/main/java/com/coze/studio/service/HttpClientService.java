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

import java.util.Map;

/**
 * HTTP客户端服务接口
 * 
 * @author coze-dev
 */
public interface HttpClientService {

    /**
     * 发送GET请求
     */
    HttpResponse get(String url, Map<String, String> headers, Map<String, Object> params);

    /**
     * 发送POST请求
     */
    HttpResponse post(String url, Map<String, String> headers, Object body);

    /**
     * 发送PUT请求
     */
    HttpResponse put(String url, Map<String, String> headers, Object body);

    /**
     * 发送DELETE请求
     */
    HttpResponse delete(String url, Map<String, String> headers);

    /**
     * 发送PATCH请求
     */
    HttpResponse patch(String url, Map<String, String> headers, Object body);

    /**
     * HTTP响应封装类
     */
    class HttpResponse {
        private int statusCode;
        private String body;
        private Map<String, String> headers;
        private long responseTime;

        public HttpResponse(int statusCode, String body, Map<String, String> headers, long responseTime) {
            this.statusCode = statusCode;
            this.body = body;
            this.headers = headers;
            this.responseTime = responseTime;
        }

        // Getters
        public int getStatusCode() { return statusCode; }
        public String getBody() { return body; }
        public Map<String, String> getHeaders() { return headers; }
        public long getResponseTime() { return responseTime; }

        public boolean isSuccess() {
            return statusCode >= 200 && statusCode < 300;
        }
    }
}
