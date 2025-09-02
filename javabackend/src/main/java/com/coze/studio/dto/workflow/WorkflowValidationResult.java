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

package com.coze.studio.dto.workflow;

import lombok.Data;

import java.util.List;

/**
 * 工作流验证结果 DTO
 * 
 * @author coze-dev
 */
@Data
public class WorkflowValidationResult {

    /**
     * 是否验证通过
     */
    private Boolean isValid;

    /**
     * 验证错误列表
     */
    private List<ValidationError> errors;

    /**
     * 验证警告列表
     */
    private List<ValidationWarning> warnings;

    /**
     * 验证信息列表
     */
    private List<ValidationInfo> infos;

    /**
     * 验证错误
     */
    @Data
    public static class ValidationError {
        private String code;
        private String message;
        private String nodeId;
        private String connectionId;
        private String field;
        private Object value;
    }

    /**
     * 验证警告
     */
    @Data
    public static class ValidationWarning {
        private String code;
        private String message;
        private String nodeId;
        private String connectionId;
        private String field;
        private Object value;
    }

    /**
     * 验证信息
     */
    @Data
    public static class ValidationInfo {
        private String code;
        private String message;
        private String nodeId;
        private String connectionId;
        private String field;
        private Object value;
    }
}
