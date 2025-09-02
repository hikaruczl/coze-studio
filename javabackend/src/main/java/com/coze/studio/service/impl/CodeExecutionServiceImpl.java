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

import com.coze.studio.service.CodeExecutionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 代码执行服务实现类
 * 
 * @author coze-dev
 */
@Slf4j
@Service
public class CodeExecutionServiceImpl implements CodeExecutionService {

    private final ScriptEngineManager scriptEngineManager;
    private final ExecutorService executorService;

    public CodeExecutionServiceImpl() {
        this.scriptEngineManager = new ScriptEngineManager();
        this.executorService = Executors.newCachedThreadPool();
    }

    @Override
    public CodeExecutionResult executePython(String code, Map<String, Object> variables, int timeoutSeconds) {
        // 注意：这里使用JavaScript引擎作为示例，实际生产环境中需要集成Python执行环境
        log.warn("Python代码执行当前使用JavaScript引擎模拟，生产环境需要集成真实的Python执行器");
        return executeJavaScript(code, variables, timeoutSeconds);
    }

    @Override
    public CodeExecutionResult executeJavaScript(String code, Map<String, Object> variables, int timeoutSeconds) {
        long startTime = System.currentTimeMillis();
        
        try {
            ScriptEngine engine = scriptEngineManager.getEngineByName("javascript");
            if (engine == null) {
                return new CodeExecutionResult(false, null, "", 
                    "JavaScript引擎不可用", 0, variables);
            }

            // 设置变量
            if (variables != null) {
                variables.forEach(engine::put);
            }

            // 使用Future来实现超时控制
            Future<Object> future = executorService.submit(() -> {
                try {
                    return engine.eval(code);
                } catch (ScriptException e) {
                    throw new RuntimeException(e);
                }
            });

            Object result;
            try {
                result = future.get(timeoutSeconds, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                future.cancel(true);
                return new CodeExecutionResult(false, null, "", 
                    "代码执行超时", System.currentTimeMillis() - startTime, variables);
            }

            // 获取更新后的变量
            Map<String, Object> updatedVariables = new HashMap<>();
            if (variables != null) {
                variables.keySet().forEach(key -> {
                    Object value = engine.get(key);
                    updatedVariables.put(key, value);
                });
            }

            long executionTime = System.currentTimeMillis() - startTime;
            return new CodeExecutionResult(true, result, String.valueOf(result), 
                null, executionTime, updatedVariables);

        } catch (Exception e) {
            log.error("JavaScript代码执行失败", e);
            long executionTime = System.currentTimeMillis() - startTime;
            return new CodeExecutionResult(false, null, "", 
                e.getMessage(), executionTime, variables);
        }
    }

    @Override
    public CodeExecutionResult executeSQL(String sql, Map<String, Object> parameters, String dataSourceName) {
        long startTime = System.currentTimeMillis();
        
        try {
            // TODO: 实现SQL执行逻辑
            // 这里需要根据dataSourceName获取对应的数据源，然后执行SQL
            log.info("执行SQL: {}, 参数: {}, 数据源: {}", sql, parameters, dataSourceName);
            
            // 模拟SQL执行结果
            Map<String, Object> result = new HashMap<>();
            result.put("affected_rows", 1);
            result.put("execution_time", System.currentTimeMillis() - startTime);
            
            long executionTime = System.currentTimeMillis() - startTime;
            return new CodeExecutionResult(true, result, "SQL执行成功", 
                null, executionTime, parameters);

        } catch (Exception e) {
            log.error("SQL执行失败: {}", sql, e);
            long executionTime = System.currentTimeMillis() - startTime;
            return new CodeExecutionResult(false, null, "", 
                e.getMessage(), executionTime, parameters);
        }
    }
}
