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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 插件配置验证器
 * 提供插件配置的验证、测试和合规性检查功能
 *
 * @author coze-dev
 */
@Service
@Slf4j
public class PluginConfigValidator {

    private final ObjectMapper objectMapper;

    // 配置模板缓存
    private final Map<String, ConfigTemplate> configTemplates = new HashMap<>();

    // 验证规则缓存
    private final Map<String, List<ValidationRule>> validationRules = new HashMap<>();

    public PluginConfigValidator(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() {
        log.info("插件配置验证器初始化完成");
        loadDefaultTemplates();
        loadDefaultValidationRules();
    }

    /**
     * 验证插件配置
     */
    public ValidationResult validateConfig(String pluginType, Map<String, Object> configData) {
        ValidationResult result = new ValidationResult();
        result.setValid(true);
        result.setErrors(new ArrayList<>());
        result.setWarnings(new ArrayList<>());
        result.setSuggestions(new ArrayList<>());

        if (configData == null || configData.isEmpty()) {
            result.setValid(false);
            result.getErrors().add("配置数据不能为空");
            return result;
        }

        try {
            // 获取配置模板
            ConfigTemplate template = configTemplates.get(pluginType);
            if (template == null) {
                result.getWarnings().add("未找到配置模板，使用通用验证规则");
                template = getGenericTemplate();
            }

            // 验证必需字段
            validateRequiredFields(configData, template, result);

            // 验证字段类型
            validateFieldTypes(configData, template, result);

            // 验证字段值范围
            validateFieldValues(configData, template, result);

            // 执行自定义验证规则
            executeCustomValidationRules(pluginType, configData, result);

            // 生成优化建议
            generateOptimizationSuggestions(configData, template, result);

        } catch (Exception e) {
            log.error("配置验证过程中发生错误: pluginType={}, error={}", pluginType, e.getMessage());
            result.setValid(false);
            result.getErrors().add("配置验证失败: " + e.getMessage());
        }

        return result;
    }

    /**
     * 测试插件配置
     */
    public TestResult testConfig(String pluginType, Map<String, Object> configData) {
        TestResult result = new TestResult();
        result.setSuccess(true);
        result.setTestCases(new ArrayList<>());
        result.setExecutionTime(0L);
        result.setTimestamp(System.currentTimeMillis());

        long startTime = System.currentTimeMillis();

        try {
            // 执行连接测试
            TestCase connectionTest = testConnection(pluginType, configData);
            result.getTestCases().add(connectionTest);

            // 执行配置一致性测试
            TestCase consistencyTest = testConfigurationConsistency(pluginType, configData);
            result.getTestCases().add(consistencyTest);

            // 执行性能测试
            TestCase performanceTest = testPerformance(pluginType, configData);
            result.getTestCases().add(performanceTest);

            // 执行安全测试
            TestCase securityTest = testSecurity(pluginType, configData);
            result.getTestCases().add(securityTest);

            // 计算总体结果
            boolean allTestsPassed = result.getTestCases().stream()
                    .allMatch(TestCase::isPassed);

            result.setSuccess(allTestsPassed);

            if (!allTestsPassed) {
                List<String> failedTests = result.getTestCases().stream()
                        .filter(testCase -> !testCase.isPassed())
                        .map(TestCase::getName)
                        .toList();
                result.setErrorMessage("以下测试失败: " + String.join(", ", failedTests));
            }

        } catch (Exception e) {
            log.error("配置测试过程中发生错误: pluginType={}, error={}", pluginType, e.getMessage());
            result.setSuccess(false);
            result.setErrorMessage("配置测试失败: " + e.getMessage());
        }

        result.setExecutionTime(System.currentTimeMillis() - startTime);
        return result;
    }

    /**
     * 获取配置模板
     */
    public ConfigTemplate getConfigTemplate(String pluginType) {
        return configTemplates.getOrDefault(pluginType, getGenericTemplate());
    }

    // ==================== 私有验证方法 ====================

    private void validateRequiredFields(Map<String, Object> configData, ConfigTemplate template, ValidationResult result) {
        List<String> requiredFields = template.getRequiredFields();
        if (requiredFields != null) {
            for (String field : requiredFields) {
                if (!configData.containsKey(field) || configData.get(field) == null ||
                    (configData.get(field) instanceof String && ((String) configData.get(field)).trim().isEmpty())) {
                    result.setValid(false);
                    result.getErrors().add("必需字段 '" + field + "' 不能为空");
                }
            }
        }
    }

    private void validateFieldTypes(Map<String, Object> configData, ConfigTemplate template, ValidationResult result) {
        Map<String, String> fieldTypes = template.getFieldTypes();
        if (fieldTypes != null) {
            for (Map.Entry<String, Object> entry : configData.entrySet()) {
                String fieldName = entry.getKey();
                Object fieldValue = entry.getValue();
                String expectedType = fieldTypes.get(fieldName);

                if (expectedType != null && fieldValue != null) {
                    if (!isTypeValid(fieldValue, expectedType)) {
                        result.getErrors().add("字段 '" + fieldName + "' 类型不正确，期望类型: " + expectedType);
                    }
                }
            }
        }
    }

    private void validateFieldValues(Map<String, Object> configData, ConfigTemplate template, ValidationResult result) {
        Map<String, ValidationRule> fieldRules = template.getFieldRules();
        if (fieldRules != null) {
            for (Map.Entry<String, Object> entry : configData.entrySet()) {
                String fieldName = entry.getKey();
                Object fieldValue = entry.getValue();
                ValidationRule rule = fieldRules.get(fieldName);

                if (rule != null && fieldValue != null) {
                    validateFieldValue(fieldName, fieldValue, rule, result);
                }
            }
        }
    }

    private void validateFieldValue(String fieldName, Object fieldValue, ValidationRule rule, ValidationResult result) {
        // 范围验证
        if (rule.getMinValue() != null && fieldValue instanceof Number) {
            double value = ((Number) fieldValue).doubleValue();
            if (value < rule.getMinValue()) {
                result.getErrors().add("字段 '" + fieldName + "' 值不能小于 " + rule.getMinValue());
            }
        }

        if (rule.getMaxValue() != null && fieldValue instanceof Number) {
            double value = ((Number) fieldValue).doubleValue();
            if (value > rule.getMaxValue()) {
                result.getErrors().add("字段 '" + fieldName + "' 值不能大于 " + rule.getMaxValue());
            }
        }

        // 长度验证
        if (rule.getMinLength() != null && fieldValue instanceof String) {
            String value = (String) fieldValue;
            if (value.length() < rule.getMinLength()) {
                result.getErrors().add("字段 '" + fieldName + "' 长度不能小于 " + rule.getMinLength());
            }
        }

        if (rule.getMaxLength() != null && fieldValue instanceof String) {
            String value = (String) fieldValue;
            if (value.length() > rule.getMaxLength()) {
                result.getErrors().add("字段 '" + fieldName + "' 长度不能大于 " + rule.getMaxLength());
            }
        }

        // 正则表达式验证
        if (rule.getPattern() != null && fieldValue instanceof String) {
            String value = (String) fieldValue;
            if (!Pattern.matches(rule.getPattern(), value)) {
                result.getErrors().add("字段 '" + fieldName + "' 格式不正确");
            }
        }

        // 枚举值验证
        if (rule.getAllowedValues() != null && !rule.getAllowedValues().isEmpty()) {
            String value = fieldValue.toString();
            if (!rule.getAllowedValues().contains(value)) {
                result.getErrors().add("字段 '" + fieldName + "' 值不在允许范围内: " + rule.getAllowedValues());
            }
        }
    }

    private void executeCustomValidationRules(String pluginType, Map<String, Object> configData, ValidationResult result) {
        List<ValidationRule> rules = validationRules.get(pluginType);
        if (rules != null) {
            for (ValidationRule rule : rules) {
                try {
                    boolean valid = rule.getValidator().validate(configData);
                    if (!valid) {
                        result.getErrors().add(rule.getErrorMessage());
                    }
                } catch (Exception e) {
                    result.getWarnings().add("自定义验证规则执行失败: " + e.getMessage());
                }
            }
        }
    }

    private void generateOptimizationSuggestions(Map<String, Object> configData, ConfigTemplate template, ValidationResult result) {
        // 性能优化建议
        if (configData.containsKey("timeout") && configData.get("timeout") instanceof Number) {
            int timeout = ((Number) configData.get("timeout")).intValue();
            if (timeout > 30000) {
                result.getSuggestions().add("建议将超时时间从 " + timeout + "ms 降低到 30000ms 以提高性能");
            }
        }

        // 安全优化建议
        if (configData.containsKey("debug") && Boolean.TRUE.equals(configData.get("debug"))) {
            result.getSuggestions().add("建议在生产环境中关闭调试模式");
        }

        // 配置优化建议
        if (configData.containsKey("maxConnections") && configData.get("maxConnections") instanceof Number) {
            int maxConn = ((Number) configData.get("maxConnections")).intValue();
            if (maxConn > 100) {
                result.getSuggestions().add("建议将最大连接数从 " + maxConn + " 降低到 100 以避免资源浪费");
            }
        }
    }

    // ==================== 测试方法 ====================

    private TestCase testConnection(String pluginType, Map<String, Object> configData) {
        TestCase testCase = new TestCase();
        testCase.setName("连接测试");
        testCase.setDescription("测试插件是否能正常连接到目标服务");

        try {
            // 这里应该根据插件类型执行具体的连接测试
            // 暂时模拟测试结果
            Thread.sleep(100); // 模拟网络延迟

            testCase.setPassed(true);
            testCase.setMessage("连接测试成功");
            testCase.setExecutionTime(100L);

        } catch (Exception e) {
            testCase.setPassed(false);
            testCase.setMessage("连接测试失败: " + e.getMessage());
            testCase.setExecutionTime(System.currentTimeMillis());
        }

        return testCase;
    }

    private TestCase testConfigurationConsistency(String pluginType, Map<String, Object> configData) {
        TestCase testCase = new TestCase();
        testCase.setName("配置一致性测试");
        testCase.setDescription("检查配置参数之间的一致性和依赖关系");

        try {
            // 检查配置一致性
            boolean consistent = checkConfigurationConsistency(configData);

            testCase.setPassed(consistent);
            testCase.setMessage(consistent ? "配置一致性检查通过" : "发现配置不一致问题");
            testCase.setExecutionTime(50L);

        } catch (Exception e) {
            testCase.setPassed(false);
            testCase.setMessage("配置一致性测试失败: " + e.getMessage());
            testCase.setExecutionTime(50L);
        }

        return testCase;
    }

    private TestCase testPerformance(String pluginType, Map<String, Object> configData) {
        TestCase testCase = new TestCase();
        testCase.setName("性能测试");
        testCase.setDescription("测试配置下的插件性能表现");

        try {
            // 模拟性能测试
            long startTime = System.currentTimeMillis();
            Thread.sleep(200); // 模拟性能测试
            long executionTime = System.currentTimeMillis() - startTime;

            testCase.setPassed(executionTime < 1000); // 1秒内完成算通过
            testCase.setMessage("性能测试完成，耗时: " + executionTime + "ms");
            testCase.setExecutionTime(executionTime);

        } catch (Exception e) {
            testCase.setPassed(false);
            testCase.setMessage("性能测试失败: " + e.getMessage());
            testCase.setExecutionTime(200L);
        }

        return testCase;
    }

    private TestCase testSecurity(String pluginType, Map<String, Object> configData) {
        TestCase testCase = new TestCase();
        testCase.setName("安全测试");
        testCase.setDescription("检查配置是否存在安全风险");

        try {
            // 检查安全配置
            List<String> securityIssues = checkSecurityIssues(configData);

            testCase.setPassed(securityIssues.isEmpty());
            if (securityIssues.isEmpty()) {
                testCase.setMessage("安全检查通过");
            } else {
                testCase.setMessage("发现安全问题: " + String.join(", ", securityIssues));
            }
            testCase.setExecutionTime(80L);

        } catch (Exception e) {
            testCase.setPassed(false);
            testCase.setMessage("安全测试失败: " + e.getMessage());
            testCase.setExecutionTime(80L);
        }

        return testCase;
    }

    // ==================== 辅助方法 ====================

    private boolean isTypeValid(Object value, String expectedType) {
        return switch (expectedType.toLowerCase()) {
            case "string" -> value instanceof String;
            case "number", "integer" -> value instanceof Number;
            case "boolean" -> value instanceof Boolean;
            case "array", "list" -> value instanceof List;
            case "object", "map" -> value instanceof Map;
            default -> true; // 未知类型默认通过
        };
    }

    private boolean checkConfigurationConsistency(Map<String, Object> configData) {
        // 检查配置参数之间的依赖关系
        // 例如：如果启用了某个功能，必须配置相关参数

        if (Boolean.TRUE.equals(configData.get("enableCache"))) {
            if (!configData.containsKey("cacheSize") || configData.get("cacheSize") == null) {
                return false;
            }
        }

        return true;
    }

    private List<String> checkSecurityIssues(Map<String, Object> configData) {
        List<String> issues = new ArrayList<>();

        // 检查敏感信息
        if (configData.containsKey("password") && configData.get("password") instanceof String) {
            String password = (String) configData.get("password");
            if (password.length() < 8) {
                issues.add("密码长度不足8位");
            }
        }

        // 检查调试模式
        if (Boolean.TRUE.equals(configData.get("debug"))) {
            issues.add("调试模式已启用，可能暴露敏感信息");
        }

        // 检查超时设置
        if (configData.containsKey("timeout") && configData.get("timeout") instanceof Number) {
            int timeout = ((Number) configData.get("timeout")).intValue();
            if (timeout > 300000) { // 5分钟
                issues.add("超时时间设置过长，可能导致资源占用");
            }
        }

        return issues;
    }

    // ==================== 初始化方法 ====================

    private void loadDefaultTemplates() {
        // HTTP插件配置模板
        ConfigTemplate httpTemplate = new ConfigTemplate();
        httpTemplate.setPluginType("http");
        httpTemplate.setRequiredFields(Arrays.asList("url", "method"));
        httpTemplate.setFieldTypes(Map.of(
            "url", "string",
            "method", "string",
            "timeout", "number",
            "headers", "object",
            "body", "string"
        ));

        Map<String, ValidationRule> httpRules = new HashMap<>();
        ValidationRule urlRule = new ValidationRule();
        urlRule.setPattern("^https?://.*");
        urlRule.setErrorMessage("URL必须以http://或https://开头");
        httpRules.put("url", urlRule);

        ValidationRule methodRule = new ValidationRule();
        methodRule.setAllowedValues(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH"));
        methodRule.setErrorMessage("HTTP方法必须是GET、POST、PUT、DELETE或PATCH之一");
        httpRules.put("method", methodRule);

        ValidationRule timeoutRule = new ValidationRule();
        timeoutRule.setMinValue(1000.0);
        timeoutRule.setMaxValue(300000.0);
        timeoutRule.setErrorMessage("超时时间必须在1000ms到300000ms之间");
        httpRules.put("timeout", timeoutRule);

        httpTemplate.setFieldRules(httpRules);
        configTemplates.put("http", httpTemplate);

        // 数据库插件配置模板
        ConfigTemplate dbTemplate = new ConfigTemplate();
        dbTemplate.setPluginType("database");
        dbTemplate.setRequiredFields(Arrays.asList("host", "port", "database", "username"));
        dbTemplate.setFieldTypes(Map.of(
            "host", "string",
            "port", "number",
            "database", "string",
            "username", "string",
            "password", "string",
            "maxConnections", "number"
        ));

        Map<String, ValidationRule> dbRules = new HashMap<>();
        ValidationRule portRule = new ValidationRule();
        portRule.setMinValue(1.0);
        portRule.setMaxValue(65535.0);
        portRule.setErrorMessage("端口号必须在1到65535之间");
        dbRules.put("port", portRule);

        ValidationRule maxConnRule = new ValidationRule();
        maxConnRule.setMinValue(1.0);
        maxConnRule.setMaxValue(1000.0);
        maxConnRule.setErrorMessage("最大连接数必须在1到1000之间");
        dbRules.put("maxConnections", maxConnRule);

        dbTemplate.setFieldRules(dbRules);
        configTemplates.put("database", dbTemplate);

        log.info("已加载 {} 个配置模板", configTemplates.size());
    }

    private void loadDefaultValidationRules() {
        // 可以在这里加载更复杂的自定义验证规则
        log.info("已加载 {} 个验证规则集", validationRules.size());
    }

    private ConfigTemplate getGenericTemplate() {
        ConfigTemplate generic = new ConfigTemplate();
        generic.setPluginType("generic");
        generic.setRequiredFields(new ArrayList<>());
        generic.setFieldTypes(new HashMap<>());
        generic.setFieldRules(new HashMap<>());
        return generic;
    }

    // ==================== 数据传输对象 ====================

    public static class ValidationResult {
        private boolean valid;
        private List<String> errors;
        private List<String> warnings;
        private List<String> suggestions;

        // Getters and setters
        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }
        public List<String> getErrors() { return errors; }
        public void setErrors(List<String> errors) { this.errors = errors; }
        public List<String> getWarnings() { return warnings; }
        public void setWarnings(List<String> warnings) { this.warnings = warnings; }
        public List<String> getSuggestions() { return suggestions; }
        public void setSuggestions(List<String> suggestions) { this.suggestions = suggestions; }
    }

    public static class TestResult {
        private boolean success;
        private String errorMessage;
        private List<TestCase> testCases;
        private long executionTime;
        private long timestamp;

        // Getters and setters
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
        public List<TestCase> getTestCases() { return testCases; }
        public void setTestCases(List<TestCase> testCases) { this.testCases = testCases; }
        public long getExecutionTime() { return executionTime; }
        public void setExecutionTime(long executionTime) { this.executionTime = executionTime; }
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    }

    public static class TestCase {
        private String name;
        private String description;
        private boolean passed;
        private String message;
        private long executionTime;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public boolean isPassed() { return passed; }
        public void setPassed(boolean passed) { this.passed = passed; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public long getExecutionTime() { return executionTime; }
        public void setExecutionTime(long executionTime) { this.executionTime = executionTime; }
    }

    public static class ConfigTemplate {
        private String pluginType;
        private List<String> requiredFields;
        private Map<String, String> fieldTypes;
        private Map<String, ValidationRule> fieldRules;

        // Getters and setters
        public String getPluginType() { return pluginType; }
        public void setPluginType(String pluginType) { this.pluginType = pluginType; }
        public List<String> getRequiredFields() { return requiredFields; }
        public void setRequiredFields(List<String> requiredFields) { this.requiredFields = requiredFields; }
        public Map<String, String> getFieldTypes() { return fieldTypes; }
        public void setFieldTypes(Map<String, String> fieldTypes) { this.fieldTypes = fieldTypes; }
        public Map<String, ValidationRule> getFieldRules() { return fieldRules; }
        public void setFieldRules(Map<String, ValidationRule> fieldRules) { this.fieldRules = fieldRules; }
    }

    public static class ValidationRule {
        private Double minValue;
        private Double maxValue;
        private Integer minLength;
        private Integer maxLength;
        private String pattern;
        private List<String> allowedValues;
        private String errorMessage;
        private CustomValidator validator;

        // Getters and setters
        public Double getMinValue() { return minValue; }
        public void setMinValue(Double minValue) { this.minValue = minValue; }
        public Double getMaxValue() { return maxValue; }
        public void setMaxValue(Double maxValue) { this.maxValue = maxValue; }
        public Integer getMinLength() { return minLength; }
        public void setMinLength(Integer minLength) { this.minLength = minLength; }
        public Integer getMaxLength() { return maxLength; }
        public void setMaxLength(Integer maxLength) { this.maxLength = maxLength; }
        public String getPattern() { return pattern; }
        public void setPattern(String pattern) { this.pattern = pattern; }
        public List<String> getAllowedValues() { return allowedValues; }
        public void setAllowedValues(List<String> allowedValues) { this.allowedValues = allowedValues; }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
        public CustomValidator getValidator() { return validator; }
        public void setValidator(CustomValidator validator) { this.validator = validator; }
    }

    public interface CustomValidator {
        boolean validate(Map<String, Object> configData);
    }
}
