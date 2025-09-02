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
 * 代码执行服务接口
 * 
 * @author coze-dev
 */
public interface CodeExecutionService {

    /**
     * 执行Python代码
     */
    CodeExecutionResult executePython(String code, Map<String, Object> variables, int timeoutSeconds);

    /**
     * 执行JavaScript代码
     */
    CodeExecutionResult executeJavaScript(String code, Map<String, Object> variables, int timeoutSeconds);

    /**
     * 执行SQL查询
     */
    CodeExecutionResult executeSQL(String sql, Map<String, Object> parameters, String dataSourceName);

    /**
     * 代码执行结果封装类
     */
    class CodeExecutionResult {
        private boolean success;
        private Object result;
        private String output;
        private String error;
        private long executionTime;
        private Map<String, Object> variables;

        public CodeExecutionResult(boolean success, Object result, String output, String error, 
                                 long executionTime, Map<String, Object> variables) {
            this.success = success;
            this.result = result;
            this.output = output;
            this.error = error;
            this.executionTime = executionTime;
            this.variables = variables;
        }

        // Getters
        public boolean isSuccess() { return success; }
        public Object getResult() { return result; }
        public String getOutput() { return output; }
        public String getError() { return error; }
        public long getExecutionTime() { return executionTime; }
        public Map<String, Object> getVariables() { return variables; }
    }
}
