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

package com.coze.studio.dto.monitor;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 执行日志条目DTO
 * 
 * @author coze-dev
 */
@Data
public class ExecutionLogEntry {
    
    private String executionId;
    private String nodeId;
    private String type;
    private String message;
    private LocalDateTime timestamp;
    private Object data;
    
    public ExecutionLogEntry() {}
    
    public ExecutionLogEntry(String executionId, String nodeId, String type, String message, LocalDateTime timestamp, Object data) {
        this.executionId = executionId;
        this.nodeId = nodeId;
        this.type = type;
        this.message = message;
        this.timestamp = timestamp;
        this.data = data;
    }
    
    // Lombok生成的getter/setter方法
    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
