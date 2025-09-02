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

import com.coze.studio.dto.common.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * 插件管理服务接口
 * 
 * @author coze-dev
 */
public interface PluginManagementService {

    /**
     * 注册插件
     */
    PluginResponse registerPlugin(Long userId, RegisterPluginRequest request);

    /**
     * 更新插件
     */
    PluginResponse updatePlugin(Long userId, Long pluginId, UpdatePluginRequest request);

    /**
     * 删除插件
     */
    void deletePlugin(Long userId, Long pluginId);

    /**
     * 获取插件详情
     */
    PluginResponse getPluginById(Long pluginId);

    /**
     * 查询插件列表
     */
    PageResponse<PluginResponse> getPlugins(PluginQueryRequest queryRequest, Pageable pageable);

    /**
     * 搜索插件
     */
    PageResponse<PluginResponse> searchPlugins(String keyword, Pageable pageable);

    /**
     * 获取热门插件
     */
    PageResponse<PluginResponse> getPopularPlugins(Pageable pageable);

    /**
     * 获取用户的插件列表
     */
    PageResponse<PluginResponse> getUserPlugins(Long userId, Pageable pageable);

    /**
     * 发布插件
     */
    PluginResponse publishPlugin(Long userId, Long pluginId);

    /**
     * 取消发布插件
     */
    PluginResponse unpublishPlugin(Long userId, Long pluginId);

    /**
     * 安装插件
     */
    PluginInstallationResponse installPlugin(Long userId, Long pluginId, InstallPluginRequest request);

    /**
     * 卸载插件
     */
    void uninstallPlugin(Long userId, Long pluginId, String reason);

    /**
     * 获取用户已安装的插件
     */
    PageResponse<PluginInstallationResponse> getInstalledPlugins(Long userId, Pageable pageable);

    /**
     * 启用/禁用插件
     */
    void togglePluginStatus(Long userId, Long pluginId, boolean enabled);

    /**
     * 评价插件
     */
    void ratePlugin(Long userId, Long pluginId, int rating, String comment);

    /**
     * 获取插件评价
     */
    PageResponse<PluginRatingResponse> getPluginRatings(Long pluginId, Pageable pageable);

    /**
     * 获取插件统计信息
     */
    PluginStatsResponse getPluginStats(Long pluginId);

    /**
     * 测试插件
     */
    PluginTestResult testPlugin(Long userId, Long pluginId, Map<String, Object> testData);

    /**
     * 同步插件到市场
     */
    void syncPluginToMarket(Long userId, Long pluginId);

    /**
     * 从市场下载插件
     */
    PluginResponse downloadPluginFromMarket(Long userId, DownloadPluginFromMarketRequest request);

    /**
     * 获取插件市场列表
     */
    PageResponse<PluginResponse> getPluginMarketList(MarketPluginQueryRequest queryRequest, Pageable pageable);

    /**
     * 创建插件版本
     */
    PluginVersionResponse createPluginVersion(Long userId, Long pluginId, CreatePluginVersionRequest request);

    /**
     * 更新插件版本
     */
    PluginVersionResponse updatePluginVersion(Long userId, Long pluginId, Long versionId, UpdatePluginVersionRequest request);

    /**
     * 删除插件版本
     */
    void deletePluginVersion(Long userId, Long pluginId, Long versionId);

    /**
     * 获取插件版本详情
     */
    PluginVersionResponse getPluginVersion(Long pluginId, Long versionId);

    /**
     * 获取插件版本列表
     */
    PageResponse<PluginVersionResponse> getPluginVersions(Long pluginId, PluginVersionQueryRequest queryRequest, Pageable pageable);

    /**
     * 发布插件版本
     */
    PluginVersionResponse publishPluginVersion(Long userId, Long pluginId, Long versionId);

    /**
     * 设置最新版本
     */
    void setPluginLatestVersion(Long userId, Long pluginId, Long versionId);

    // ==================== 插件配置管理 ====================

    /**
     * 获取插件配置
     */
    PluginConfigResponse getPluginConfig(Long pluginId);

    /**
     * 更新插件配置
     */
    PluginConfigResponse updatePluginConfig(Long userId, Long pluginId, UpdatePluginConfigRequest request);

    /**
     * 验证插件配置
     */
    PluginConfigValidationResult validatePluginConfig(Long pluginId, Map<String, Object> configData);

    /**
     * 测试插件配置
     */
    PluginConfigTestResult testPluginConfig(Long userId, Long pluginId, Map<String, Object> configData);

    /**
     * 获取配置历史
     */
    PageResponse<PluginConfigHistoryResponse> getPluginConfigHistory(Long pluginId, org.springframework.data.domain.Pageable pageable);

    /**
     * 恢复配置版本
     */
    PluginConfigResponse restorePluginConfigVersion(Long userId, Long pluginId, Long versionId);

    /**
     * 获取配置模板
     */
    PluginConfigTemplate getPluginConfigTemplate(String pluginType);

    /**
     * 批量更新配置
     */
    List<PluginConfigResponse> batchUpdatePluginConfigs(Long userId, List<PluginConfigUpdateItem> configs);

    /**
     * 导出配置
     */
    PluginConfigExportData exportPluginConfig(Long pluginId, String format);

    /**
     * 导入配置
     */
    PluginConfigResponse importPluginConfig(Long userId, Long pluginId, ImportPluginConfigRequest request);

    // ==================== 插件配置版本控制 ====================

    /**
     * 创建配置版本
     */
    PluginConfigVersionResponse createPluginConfigVersion(Long userId, Long pluginId, CreatePluginConfigVersionRequest request);

    /**
     * 更新配置版本
     */
    PluginConfigVersionResponse updatePluginConfigVersion(Long userId, Long pluginId, Long versionId, UpdatePluginConfigVersionRequest request);

    /**
     * 删除配置版本
     */
    void deletePluginConfigVersion(Long userId, Long pluginId, Long versionId);

    /**
     * 获取配置版本详情
     */
    PluginConfigVersionResponse getPluginConfigVersion(Long pluginId, Long versionId);

    /**
     * 获取配置版本列表
     */
    PageResponse<PluginConfigVersionResponse> getPluginConfigVersions(Long pluginId, org.springframework.data.domain.Pageable pageable);

    /**
     * 发布配置版本
     */
    PluginConfigVersionResponse publishPluginConfigVersion(Long userId, Long pluginId, Long versionId);

    /**
     * 回滚到指定配置版本
     */
    PluginConfigResponse rollbackPluginConfigVersion(Long userId, Long pluginId, Long versionId, String reason);

    /**
     * 比较两个配置版本
     */
    PluginConfigVersionComparison comparePluginConfigVersions(Long pluginId, Long versionId1, Long versionId2);

    /**
     * 设置默认配置版本
     */
    void setDefaultPluginConfigVersion(Long userId, Long pluginId, Long versionId);

    /**
     * 获取默认配置版本
     */
    PluginConfigVersionResponse getDefaultPluginConfigVersion(Long pluginId);

    // ==================== 插件配置版本控制相关DTO类 ====================

    /**
     * 创建配置版本请求
     */
    class CreatePluginConfigVersionRequest {
        private String version;
        private String versionTag;
        private Map<String, Object> configData;
        private String changeLog;
        private String changeType;
        private String changeReason;
        private Boolean isStable;
        private Map<String, Object> metadata;

        // Getters and setters
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        public String getVersionTag() { return versionTag; }
        public void setVersionTag(String versionTag) { this.versionTag = versionTag; }
        public Map<String, Object> getConfigData() { return configData; }
        public void setConfigData(Map<String, Object> configData) { this.configData = configData; }
        public String getChangeLog() { return changeLog; }
        public void setChangeLog(String changeLog) { this.changeLog = changeLog; }
        public String getChangeType() { return changeType; }
        public void setChangeType(String changeType) { this.changeType = changeType; }
        public String getChangeReason() { return changeReason; }
        public void setChangeReason(String changeReason) { this.changeReason = changeReason; }
        public Boolean getIsStable() { return isStable; }
        public void setIsStable(Boolean isStable) { this.isStable = isStable; }
        public Map<String, Object> getMetadata() { return metadata; }
        public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    }

    /**
     * 更新配置版本请求
     */
    class UpdatePluginConfigVersionRequest {
        private String versionTag;
        private Map<String, Object> configData;
        private String changeLog;
        private String changeType;
        private String changeReason;
        private Boolean isStable;
        private Map<String, Object> metadata;

        // Getters and setters
        public String getVersionTag() { return versionTag; }
        public void setVersionTag(String versionTag) { this.versionTag = versionTag; }
        public Map<String, Object> getConfigData() { return configData; }
        public void setConfigData(Map<String, Object> configData) { this.configData = configData; }
        public String getChangeLog() { return changeLog; }
        public void setChangeLog(String changeLog) { this.changeLog = changeLog; }
        public String getChangeType() { return changeType; }
        public void setChangeType(String changeType) { this.changeType = changeType; }
        public String getChangeReason() { return changeReason; }
        public void setChangeReason(String changeReason) { this.changeReason = changeReason; }
        public Boolean getIsStable() { return isStable; }
        public void setIsStable(Boolean isStable) { this.isStable = isStable; }
        public Map<String, Object> getMetadata() { return metadata; }
        public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    }

    /**
     * 配置版本响应
     */
    class PluginConfigVersionResponse {
        private Long id;
        private Long pluginId;
        private String version;
        private String versionTag;
        private Map<String, Object> configData;
        private String changeLog;
        private String changeType;
        private String changeReason;
        private String status;
        private Boolean isStable;
        private Boolean isDefault;
        private Long createdBy;
        private Long reviewedBy;
        private java.time.LocalDateTime reviewedAt;
        private java.time.LocalDateTime publishedAt;
        private java.time.LocalDateTime createdAt;
        private java.time.LocalDateTime updatedAt;
        private String comparisonResult;
        private Integer rollbackCount;
        private Map<String, Object> metadata;

        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getPluginId() { return pluginId; }
        public void setPluginId(Long pluginId) { this.pluginId = pluginId; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        public String getVersionTag() { return versionTag; }
        public void setVersionTag(String versionTag) { this.versionTag = versionTag; }
        public Map<String, Object> getConfigData() { return configData; }
        public void setConfigData(Map<String, Object> configData) { this.configData = configData; }
        public String getChangeLog() { return changeLog; }
        public void setChangeLog(String changeLog) { this.changeLog = changeLog; }
        public String getChangeType() { return changeType; }
        public void setChangeType(String changeType) { this.changeType = changeType; }
        public String getChangeReason() { return changeReason; }
        public void setChangeReason(String changeReason) { this.changeReason = changeReason; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public Boolean getIsStable() { return isStable; }
        public void setIsStable(Boolean isStable) { this.isStable = isStable; }
        public Boolean getIsDefault() { return isDefault; }
        public void setIsDefault(Boolean isDefault) { this.isDefault = isDefault; }
        public Long getCreatedBy() { return createdBy; }
        public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
        public Long getReviewedBy() { return reviewedBy; }
        public void setReviewedBy(Long reviewedBy) { this.reviewedBy = reviewedBy; }
        public java.time.LocalDateTime getReviewedAt() { return reviewedAt; }
        public void setReviewedAt(java.time.LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }
        public java.time.LocalDateTime getPublishedAt() { return publishedAt; }
        public void setPublishedAt(java.time.LocalDateTime publishedAt) { this.publishedAt = publishedAt; }
        public java.time.LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(java.time.LocalDateTime createdAt) { this.createdAt = createdAt; }
        public java.time.LocalDateTime getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(java.time.LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
        public String getComparisonResult() { return comparisonResult; }
        public void setComparisonResult(String comparisonResult) { this.comparisonResult = comparisonResult; }
        public Integer getRollbackCount() { return rollbackCount; }
        public void setRollbackCount(Integer rollbackCount) { this.rollbackCount = rollbackCount; }
        public Map<String, Object> getMetadata() { return metadata; }
        public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    }

    /**
     * 配置版本比较结果
     */
    class PluginConfigVersionComparison {
        private PluginConfigVersionResponse version1;
        private PluginConfigVersionResponse version2;
        private List<ConfigDifference> differences;
        private String summary;
        private boolean hasBreakingChanges;

        public PluginConfigVersionComparison() {
            this.differences = new java.util.ArrayList<>();
        }

        // Getters and setters
        public PluginConfigVersionResponse getVersion1() { return version1; }
        public void setVersion1(PluginConfigVersionResponse version1) { this.version1 = version1; }
        public PluginConfigVersionResponse getVersion2() { return version2; }
        public void setVersion2(PluginConfigVersionResponse version2) { this.version2 = version2; }
        public List<ConfigDifference> getDifferences() { return differences; }
        public void setDifferences(List<ConfigDifference> differences) { this.differences = differences; }
        public String getSummary() { return summary; }
        public void setSummary(String summary) { this.summary = summary; }
        public boolean isHasBreakingChanges() { return hasBreakingChanges; }
        public void setHasBreakingChanges(boolean hasBreakingChanges) { this.hasBreakingChanges = hasBreakingChanges; }
    }

    /**
     * 配置差异
     */
    class ConfigDifference {
        private String field;
        private Object oldValue;
        private Object newValue;
        private String changeType; // ADDED, REMOVED, MODIFIED
        private boolean isBreakingChange;
        private String description;

        // Getters and setters
        public String getField() { return field; }
        public void setField(String field) { this.field = field; }
        public Object getOldValue() { return oldValue; }
        public void setOldValue(Object oldValue) { this.oldValue = oldValue; }
        public Object getNewValue() { return newValue; }
        public void setNewValue(Object newValue) { this.newValue = newValue; }
        public String getChangeType() { return changeType; }
        public void setChangeType(String changeType) { this.changeType = changeType; }
        public boolean isBreakingChange() { return isBreakingChange; }
        public void setBreakingChange(boolean isBreakingChange) { this.isBreakingChange = isBreakingChange; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    /**
     * 审核插件
     */
    PluginResponse reviewPlugin(Long reviewerId, Long pluginId, ReviewPluginRequest request);

    // ==================== 插件配置相关DTO类 ====================

    /**
     * 更新插件配置请求
     */
    class UpdatePluginConfigRequest {
        private Map<String, Object> configData;
        private String environment;
        private String version;

        // Getters and setters
        public Map<String, Object> getConfigData() { return configData; }
        public void setConfigData(Map<String, Object> configData) { this.configData = configData; }
        public String getEnvironment() { return environment; }
        public void setEnvironment(String environment) { this.environment = environment; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
    }

    /**
     * 插件配置更新项
     */
    class PluginConfigUpdateItem {
        private Long pluginId;
        private Map<String, Object> configData;
        private String environment;

        // Getters and setters
        public Long getPluginId() { return pluginId; }
        public void setPluginId(Long pluginId) { this.pluginId = pluginId; }
        public Map<String, Object> getConfigData() { return configData; }
        public void setConfigData(Map<String, Object> configData) { this.configData = configData; }
        public String getEnvironment() { return environment; }
        public void setEnvironment(String environment) { this.environment = environment; }
    }

    /**
     * 导入插件配置请求
     */
    class ImportPluginConfigRequest {
        private String configData;
        private String format;
        private boolean overwrite;

        // Getters and setters
        public String getConfigData() { return configData; }
        public void setConfigData(String configData) { this.configData = configData; }
        public String getFormat() { return format; }
        public void setFormat(String format) { this.format = format; }
        public boolean isOverwrite() { return overwrite; }
        public void setOverwrite(boolean overwrite) { this.overwrite = overwrite; }
    }

    /**
     * 插件配置响应
     */
    class PluginConfigResponse {
        private Long pluginId;
        private String pluginName;
        private Map<String, Object> configData;
        private String environment;
        private String version;
        private boolean validated;
        private String lastValidatedAt;
        private Long updatedBy;
        private java.time.LocalDateTime updatedAt;

        // Getters and setters
        public Long getPluginId() { return pluginId; }
        public void setPluginId(Long pluginId) { this.pluginId = pluginId; }
        public String getPluginName() { return pluginName; }
        public void setPluginName(String pluginName) { this.pluginName = pluginName; }
        public Map<String, Object> getConfigData() { return configData; }
        public void setConfigData(Map<String, Object> configData) { this.configData = configData; }
        public String getEnvironment() { return environment; }
        public void setEnvironment(String environment) { this.environment = environment; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        public boolean isValidated() { return validated; }
        public void setValidated(boolean validated) { this.validated = validated; }
        public String getLastValidatedAt() { return lastValidatedAt; }
        public void setLastValidatedAt(String lastValidatedAt) { this.lastValidatedAt = lastValidatedAt; }
        public Long getUpdatedBy() { return updatedBy; }
        public void setUpdatedBy(Long updatedBy) { this.updatedBy = updatedBy; }
        public java.time.LocalDateTime getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(java.time.LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    }

    /**
     * 插件配置验证结果
     */
    class PluginConfigValidationResult {
        private boolean valid;
        private List<String> errors;
        private List<String> warnings;
        private Map<String, Object> suggestedFixes;

        public PluginConfigValidationResult() {
            this.errors = new java.util.ArrayList<>();
            this.warnings = new java.util.ArrayList<>();
            this.suggestedFixes = new java.util.HashMap<>();
        }

        // Getters and setters
        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }
        public List<String> getErrors() { return errors; }
        public void setErrors(List<String> errors) { this.errors = errors; }
        public List<String> getWarnings() { return warnings; }
        public void setWarnings(List<String> warnings) { this.warnings = warnings; }
        public Map<String, Object> getSuggestedFixes() { return suggestedFixes; }
        public void setSuggestedFixes(Map<String, Object> suggestedFixes) { this.suggestedFixes = suggestedFixes; }
    }

    /**
     * 插件配置测试结果
     */
    class PluginConfigTestResult {
        private boolean success;
        private String message;
        private Map<String, Object> testData;
        private Map<String, Object> response;
        private long executionTime;
        private String error;

        // Getters and setters
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public Map<String, Object> getTestData() { return testData; }
        public void setTestData(Map<String, Object> testData) { this.testData = testData; }
        public Map<String, Object> getResponse() { return response; }
        public void setResponse(Map<String, Object> response) { this.response = response; }
        public long getExecutionTime() { return executionTime; }
        public void setExecutionTime(long executionTime) { this.executionTime = executionTime; }
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
    }

    /**
     * 插件配置历史响应
     */
    class PluginConfigHistoryResponse {
        private Long id;
        private Long pluginId;
        private String pluginName;
        private Map<String, Object> configData;
        private String environment;
        private String version;
        private Long updatedBy;
        private String updatedByName;
        private java.time.LocalDateTime updatedAt;
        private String changeReason;

        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getPluginId() { return pluginId; }
        public void setPluginId(Long pluginId) { this.pluginId = pluginId; }
        public String getPluginName() { return pluginName; }
        public void setPluginName(String pluginName) { this.pluginName = pluginName; }
        public Map<String, Object> getConfigData() { return configData; }
        public void setConfigData(Map<String, Object> configData) { this.configData = configData; }
        public String getEnvironment() { return environment; }
        public void setEnvironment(String environment) { this.environment = environment; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        public Long getUpdatedBy() { return updatedBy; }
        public void setUpdatedBy(Long updatedBy) { this.updatedBy = updatedBy; }
        public String getUpdatedByName() { return updatedByName; }
        public void setUpdatedByName(String updatedByName) { this.updatedByName = updatedByName; }
        public java.time.LocalDateTime getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(java.time.LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
        public String getChangeReason() { return changeReason; }
        public void setChangeReason(String changeReason) { this.changeReason = changeReason; }
    }

    /**
     * 插件配置模板
     */
    class PluginConfigTemplate {
        private String pluginType;
        private String templateName;
        private Map<String, Object> defaultConfig;
        private Map<String, Object> schema;
        private List<String> requiredFields;
        private Map<String, Object> validationRules;
        private String description;

        // Getters and setters
        public String getPluginType() { return pluginType; }
        public void setPluginType(String pluginType) { this.pluginType = pluginType; }
        public String getTemplateName() { return templateName; }
        public void setTemplateName(String templateName) { this.templateName = templateName; }
        public Map<String, Object> getDefaultConfig() { return defaultConfig; }
        public void setDefaultConfig(Map<String, Object> defaultConfig) { this.defaultConfig = defaultConfig; }
        public Map<String, Object> getSchema() { return schema; }
        public void setSchema(Map<String, Object> schema) { this.schema = schema; }
        public List<String> getRequiredFields() { return requiredFields; }
        public void setRequiredFields(List<String> requiredFields) { this.requiredFields = requiredFields; }
        public Map<String, Object> getValidationRules() { return validationRules; }
        public void setValidationRules(Map<String, Object> validationRules) { this.validationRules = validationRules; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    /**
     * 插件配置导出数据
     */
    class PluginConfigExportData {
        private Long pluginId;
        private String pluginName;
        private String format;
        private String configData;
        private String exportedAt;
        private String exportedBy;
        private String checksum;

        // Getters and setters
        public Long getPluginId() { return pluginId; }
        public void setPluginId(Long pluginId) { this.pluginId = pluginId; }
        public String getPluginName() { return pluginName; }
        public void setPluginName(String pluginName) { this.pluginName = pluginName; }
        public String getFormat() { return format; }
        public void setFormat(String format) { this.format = format; }
        public String getConfigData() { return configData; }
        public void setConfigData(String configData) { this.configData = configData; }
        public String getExportedAt() { return exportedAt; }
        public void setExportedAt(String exportedAt) { this.exportedAt = exportedAt; }
        public String getExportedBy() { return exportedBy; }
        public void setExportedBy(String exportedBy) { this.exportedBy = exportedBy; }
        public String getChecksum() { return checksum; }
        public void setChecksum(String checksum) { this.checksum = checksum; }
    }
}

    /**
     * 获取待审核插件列表
     */
    PageResponse<PluginResponse> getPendingReviewPlugins(Pageable pageable);

    /**
     * 注册插件请求
     */
    class RegisterPluginRequest {
        private String name;
        private String description;
        private String version;
        private String pluginType;
        private String category;
        private List<String> tags;
        private String iconUrl;
        private String openapi;
        private String aiPlugin;
        private String serviceUrl;
        private String serviceToken;
        private Map<String, Object> pluginConfig;
        private List<String> dependencies;
        private List<String> permissions;
        private String documentationUrl;
        private String supportUrl;
        private String license;
        private boolean isPrivate;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        
        public String getPluginType() { return pluginType; }
        public void setPluginType(String pluginType) { this.pluginType = pluginType; }
        
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        
        public List<String> getTags() { return tags; }
        public void setTags(List<String> tags) { this.tags = tags; }
        
        public String getIconUrl() { return iconUrl; }
        public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }
        
        public String getOpenapi() { return openapi; }
        public void setOpenapi(String openapi) { this.openapi = openapi; }
        
        public String getAiPlugin() { return aiPlugin; }
        public void setAiPlugin(String aiPlugin) { this.aiPlugin = aiPlugin; }
        
        public String getServiceUrl() { return serviceUrl; }
        public void setServiceUrl(String serviceUrl) { this.serviceUrl = serviceUrl; }
        
        public String getServiceToken() { return serviceToken; }
        public void setServiceToken(String serviceToken) { this.serviceToken = serviceToken; }
        
        public Map<String, Object> getPluginConfig() { return pluginConfig; }
        public void setPluginConfig(Map<String, Object> pluginConfig) { this.pluginConfig = pluginConfig; }
        
        public List<String> getDependencies() { return dependencies; }
        public void setDependencies(List<String> dependencies) { this.dependencies = dependencies; }
        
        public List<String> getPermissions() { return permissions; }
        public void setPermissions(List<String> permissions) { this.permissions = permissions; }
        
        public String getDocumentationUrl() { return documentationUrl; }
        public void setDocumentationUrl(String documentationUrl) { this.documentationUrl = documentationUrl; }
        
        public String getSupportUrl() { return supportUrl; }
        public void setSupportUrl(String supportUrl) { this.supportUrl = supportUrl; }
        
        public String getLicense() { return license; }
        public void setLicense(String license) { this.license = license; }
        
        public boolean isPrivate() { return isPrivate; }
        public void setPrivate(boolean isPrivate) { this.isPrivate = isPrivate; }
    }

    /**
     * 更新插件请求
     */
    class UpdatePluginRequest {
        private String name;
        private String description;
        private String version;
        private String category;
        private List<String> tags;
        private String iconUrl;
        private String openapi;
        private String serviceUrl;
        private String serviceToken;
        private Map<String, Object> pluginConfig;
        private List<String> dependencies;
        private List<String> permissions;
        private String documentationUrl;
        private String supportUrl;
        private String license;
        private boolean isPrivate;

        // Getters and setters (similar to RegisterPluginRequest)
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        
        public List<String> getTags() { return tags; }
        public void setTags(List<String> tags) { this.tags = tags; }
        
        public String getIconUrl() { return iconUrl; }
        public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }
        
        public String getOpenapi() { return openapi; }
        public void setOpenapi(String openapi) { this.openapi = openapi; }
        
        public String getServiceUrl() { return serviceUrl; }
        public void setServiceUrl(String serviceUrl) { this.serviceUrl = serviceUrl; }
        
        public String getServiceToken() { return serviceToken; }
        public void setServiceToken(String serviceToken) { this.serviceToken = serviceToken; }
        
        public Map<String, Object> getPluginConfig() { return pluginConfig; }
        public void setPluginConfig(Map<String, Object> pluginConfig) { this.pluginConfig = pluginConfig; }
        
        public List<String> getDependencies() { return dependencies; }
        public void setDependencies(List<String> dependencies) { this.dependencies = dependencies; }
        
        public List<String> getPermissions() { return permissions; }
        public void setPermissions(List<String> permissions) { this.permissions = permissions; }
        
        public String getDocumentationUrl() { return documentationUrl; }
        public void setDocumentationUrl(String documentationUrl) { this.documentationUrl = documentationUrl; }
        
        public String getSupportUrl() { return supportUrl; }
        public void setSupportUrl(String supportUrl) { this.supportUrl = supportUrl; }
        
        public String getLicense() { return license; }
        public void setLicense(String license) { this.license = license; }
        
        public boolean isPrivate() { return isPrivate; }
        public void setPrivate(boolean isPrivate) { this.isPrivate = isPrivate; }
    }

    /**
     * 插件查询请求
     */
    class PluginQueryRequest {
        private String category;
        private List<String> tags;
        private String status;
        private String pluginType;
        private Long creatorId;
        private String sortBy;
        private String sortDirection;
        private String keyword;

        // Getters and setters
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        
        public List<String> getTags() { return tags; }
        public void setTags(List<String> tags) { this.tags = tags; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public String getPluginType() { return pluginType; }
        public void setPluginType(String pluginType) { this.pluginType = pluginType; }
        
        public Long getCreatorId() { return creatorId; }
        public void setCreatorId(Long creatorId) { this.creatorId = creatorId; }
        
        public String getSortBy() { return sortBy; }
        public void setSortBy(String sortBy) { this.sortBy = sortBy; }
        
        public String getSortDirection() { return sortDirection; }
        public void setSortDirection(String sortDirection) { this.sortDirection = sortDirection; }

        public String getKeyword() { return keyword; }
        public void setKeyword(String keyword) { this.keyword = keyword; }
    }

    /**
     * 安装插件请求
     */
    class InstallPluginRequest {
        private String version;
        private Map<String, Object> installationConfig;

        // Getters and setters
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        
        public Map<String, Object> getInstallationConfig() { return installationConfig; }
        public void setInstallationConfig(Map<String, Object> installationConfig) { this.installationConfig = installationConfig; }
    }

    /**
     * 审核插件请求
     */
    class ReviewPluginRequest {
        private String reviewStatus;
        private String reviewNotes;

        // Getters and setters
        public String getReviewStatus() { return reviewStatus; }
        public void setReviewStatus(String reviewStatus) { this.reviewStatus = reviewStatus; }
        
        public String getReviewNotes() { return reviewNotes; }
        public void setReviewNotes(String reviewNotes) { this.reviewNotes = reviewNotes; }
    }

    /**
     * 插件响应
     */
    class PluginResponse {
        private Long id;
        private String name;
        private String description;
        private String version;
        private String pluginType;
        private String category;
        private List<String> tags;
        private String iconUrl;
        private String status;
        private String reviewStatus;
        private Long creatorId;
        private String creatorName;
        private Long usageCount;
        private Long installCount;
        private Double averageRating;
        private Integer ratingCount;
        private String serviceUrl;
        private Map<String, Object> pluginConfig;
        private List<String> dependencies;
        private List<String> permissions;
        private String documentationUrl;
        private String supportUrl;
        private String license;
        private boolean isPrivate;
        private String createdAt;
        private String updatedAt;
        private String publishedAt;

        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }

        public String getPluginType() { return pluginType; }
        public void setPluginType(String pluginType) { this.pluginType = pluginType; }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }

        public List<String> getTags() { return tags; }
        public void setTags(List<String> tags) { this.tags = tags; }

        public String getIconUrl() { return iconUrl; }
        public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public String getReviewStatus() { return reviewStatus; }
        public void setReviewStatus(String reviewStatus) { this.reviewStatus = reviewStatus; }

        public Long getCreatorId() { return creatorId; }
        public void setCreatorId(Long creatorId) { this.creatorId = creatorId; }

        public String getCreatorName() { return creatorName; }
        public void setCreatorName(String creatorName) { this.creatorName = creatorName; }

        public Long getUsageCount() { return usageCount; }
        public void setUsageCount(Long usageCount) { this.usageCount = usageCount; }

        public Long getInstallCount() { return installCount; }
        public void setInstallCount(Long installCount) { this.installCount = installCount; }

        public Double getAverageRating() { return averageRating; }
        public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }

        public Integer getRatingCount() { return ratingCount; }
        public void setRatingCount(Integer ratingCount) { this.ratingCount = ratingCount; }

        public String getServiceUrl() { return serviceUrl; }
        public void setServiceUrl(String serviceUrl) { this.serviceUrl = serviceUrl; }

        public Map<String, Object> getPluginConfig() { return pluginConfig; }
        public void setPluginConfig(Map<String, Object> pluginConfig) { this.pluginConfig = pluginConfig; }

        public List<String> getDependencies() { return dependencies; }
        public void setDependencies(List<String> dependencies) { this.dependencies = dependencies; }

        public List<String> getPermissions() { return permissions; }
        public void setPermissions(List<String> permissions) { this.permissions = permissions; }

        public String getDocumentationUrl() { return documentationUrl; }
        public void setDocumentationUrl(String documentationUrl) { this.documentationUrl = documentationUrl; }

        public String getSupportUrl() { return supportUrl; }
        public void setSupportUrl(String supportUrl) { this.supportUrl = supportUrl; }

        public String getLicense() { return license; }
        public void setLicense(String license) { this.license = license; }

        public boolean isPrivate() { return isPrivate; }
        public void setPrivate(boolean isPrivate) { this.isPrivate = isPrivate; }

        public String getCreatedAt() { return createdAt; }
        public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

        public String getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

        public String getPublishedAt() { return publishedAt; }
        public void setPublishedAt(String publishedAt) { this.publishedAt = publishedAt; }
    }

    /**
     * 插件安装响应
     */
    class PluginInstallationResponse {
        private Long id;
        private Long pluginId;
        private String pluginName;
        private String installedVersion;
        private String status;
        private boolean enabled;
        private Map<String, Object> installationConfig;
        private String installedAt;
        private String lastUsedAt;
        private Long usageCount;
        private String installSource;

        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public Long getPluginId() { return pluginId; }
        public void setPluginId(Long pluginId) { this.pluginId = pluginId; }

        public String getPluginName() { return pluginName; }
        public void setPluginName(String pluginName) { this.pluginName = pluginName; }

        public String getInstalledVersion() { return installedVersion; }
        public void setInstalledVersion(String installedVersion) { this.installedVersion = installedVersion; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }

        public Map<String, Object> getInstallationConfig() { return installationConfig; }
        public void setInstallationConfig(Map<String, Object> installationConfig) { this.installationConfig = installationConfig; }

        public String getInstalledAt() { return installedAt; }
        public void setInstalledAt(String installedAt) { this.installedAt = installedAt; }

        public String getLastUsedAt() { return lastUsedAt; }
        public void setLastUsedAt(String lastUsedAt) { this.lastUsedAt = lastUsedAt; }

        public Long getUsageCount() { return usageCount; }
        public void setUsageCount(Long usageCount) { this.usageCount = usageCount; }

        public String getInstallSource() { return installSource; }
        public void setInstallSource(String installSource) { this.installSource = installSource; }
    }

    /**
     * 插件评价响应
     */
    class PluginRatingResponse {
        private Long id;
        private Long pluginId;
        private Long userId;
        private String userName;
        private Integer rating;
        private String comment;
        private String pluginVersion;
        private Integer helpfulVotes;
        private Integer totalVotes;
        private String status;
        private String adminReply;
        private String createdAt;
        private String adminReplyAt;

        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public Long getPluginId() { return pluginId; }
        public void setPluginId(Long pluginId) { this.pluginId = pluginId; }

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }

        public String getUserName() { return userName; }
        public void setUserName(String userName) { this.userName = userName; }

        public Integer getRating() { return rating; }
        public void setRating(Integer rating) { this.rating = rating; }

        public String getComment() { return comment; }
        public void setComment(String comment) { this.comment = comment; }

        public String getPluginVersion() { return pluginVersion; }
        public void setPluginVersion(String pluginVersion) { this.pluginVersion = pluginVersion; }

        public Integer getHelpfulVotes() { return helpfulVotes; }
        public void setHelpfulVotes(Integer helpfulVotes) { this.helpfulVotes = helpfulVotes; }

        public Integer getTotalVotes() { return totalVotes; }
        public void setTotalVotes(Integer totalVotes) { this.totalVotes = totalVotes; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public String getAdminReply() { return adminReply; }
        public void setAdminReply(String adminReply) { this.adminReply = adminReply; }

        public String getCreatedAt() { return createdAt; }
        public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

        public String getAdminReplyAt() { return adminReplyAt; }
        public void setAdminReplyAt(String adminReplyAt) { this.adminReplyAt = adminReplyAt; }
    }

    /**
     * 插件统计响应
     */
    class PluginStatsResponse {
        private Long pluginId;
        private Long totalUsage;
        private Long totalInstalls;
        private Long activeInstalls;
        private Double averageRating;
        private Integer ratingCount;
        private Map<String, Integer> usageBySource;
        private Map<String, Integer> usageTrend;
        private Map<String, Integer> ratingDistribution;
        private Double successRate;
        private Double averageExecutionTime;

        // Getters and setters
        public Long getPluginId() { return pluginId; }
        public void setPluginId(Long pluginId) { this.pluginId = pluginId; }

        public Long getTotalUsage() { return totalUsage; }
        public void setTotalUsage(Long totalUsage) { this.totalUsage = totalUsage; }

        public Long getTotalInstalls() { return totalInstalls; }
        public void setTotalInstalls(Long totalInstalls) { this.totalInstalls = totalInstalls; }

        public Long getActiveInstalls() { return activeInstalls; }
        public void setActiveInstalls(Long activeInstalls) { this.activeInstalls = activeInstalls; }

        public Double getAverageRating() { return averageRating; }
        public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }

        public Integer getRatingCount() { return ratingCount; }
        public void setRatingCount(Integer ratingCount) { this.ratingCount = ratingCount; }

        public Map<String, Integer> getUsageBySource() { return usageBySource; }
        public void setUsageBySource(Map<String, Integer> usageBySource) { this.usageBySource = usageBySource; }

        public Map<String, Integer> getUsageTrend() { return usageTrend; }
        public void setUsageTrend(Map<String, Integer> usageTrend) { this.usageTrend = usageTrend; }

        public Map<String, Integer> getRatingDistribution() { return ratingDistribution; }
        public void setRatingDistribution(Map<String, Integer> ratingDistribution) { this.ratingDistribution = ratingDistribution; }

        public Double getSuccessRate() { return successRate; }
        public void setSuccessRate(Double successRate) { this.successRate = successRate; }

        public Double getAverageExecutionTime() { return averageExecutionTime; }
        public void setAverageExecutionTime(Double averageExecutionTime) { this.averageExecutionTime = averageExecutionTime; }
    }

    /**
     * 插件测试结果
     */
    class PluginTestResult {
        private boolean success;
        private String message;
        private Map<String, Object> testData;
        private Map<String, Object> response;
        private long executionTime;
        private String error;

        public PluginTestResult(boolean success, String message, Map<String, Object> testData,
                              Map<String, Object> response, long executionTime, String error) {
            this.success = success;
            this.message = message;
            this.testData = testData;
            this.response = response;
            this.executionTime = executionTime;
            this.error = error;
        }

        // Getters
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public Map<String, Object> getTestData() { return testData; }
        public Map<String, Object> getResponse() { return response; }
        public long getExecutionTime() { return executionTime; }
        public String getError() { return error; }
    }

    /**
     * 从市场下载插件请求
     */
    class DownloadPluginFromMarketRequest {
        private String marketPluginId;
        private String version;
        private Map<String, Object> installationConfig;

        // Getters and setters
        public String getMarketPluginId() { return marketPluginId; }
        public void setMarketPluginId(String marketPluginId) { this.marketPluginId = marketPluginId; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        public Map<String, Object> getInstallationConfig() { return installationConfig; }
        public void setInstallationConfig(Map<String, Object> installationConfig) { this.installationConfig = installationConfig; }
    }

    /**
     * 市场插件查询请求
     */
    class MarketPluginQueryRequest {
        private String category;
        private String keyword;
        private String sortBy; // popularity, rating, newest, downloads

        // Getters and setters
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public String getKeyword() { return keyword; }
        public void setKeyword(String keyword) { this.keyword = keyword; }
        public String getSortBy() { return sortBy; }
        public void setSortBy(String sortBy) { this.sortBy = sortBy; }
    }

    /**
     * 创建插件版本请求
     */
    class CreatePluginVersionRequest {
        private String version;
        private String title;
        private String description;
        private String releaseNotes;
        private String minCompatibleVersion;
        private Boolean isStable;
        private String versionFiles;
        private String dependencies;
        private String configTemplate;

        // Getters and setters
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getReleaseNotes() { return releaseNotes; }
        public void setReleaseNotes(String releaseNotes) { this.releaseNotes = releaseNotes; }
        public String getMinCompatibleVersion() { return minCompatibleVersion; }
        public void setMinCompatibleVersion(String minCompatibleVersion) { this.minCompatibleVersion = minCompatibleVersion; }
        public Boolean getIsStable() { return isStable; }
        public void setIsStable(Boolean isStable) { this.isStable = isStable; }
        public String getVersionFiles() { return versionFiles; }
        public void setVersionFiles(String versionFiles) { this.versionFiles = versionFiles; }
        public String getDependencies() { return dependencies; }
        public void setDependencies(String dependencies) { this.dependencies = dependencies; }
        public String getConfigTemplate() { return configTemplate; }
        public void setConfigTemplate(String configTemplate) { this.configTemplate = configTemplate; }
    }

    /**
     * 更新插件版本请求
     */
    class UpdatePluginVersionRequest {
        private String title;
        private String description;
        private String releaseNotes;
        private String minCompatibleVersion;
        private Boolean isStable;
        private String versionFiles;
        private String dependencies;
        private String configTemplate;

        // Getters and setters
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getReleaseNotes() { return releaseNotes; }
        public void setReleaseNotes(String releaseNotes) { this.releaseNotes = releaseNotes; }
        public String getMinCompatibleVersion() { return minCompatibleVersion; }
        public void setMinCompatibleVersion(String minCompatibleVersion) { this.minCompatibleVersion = minCompatibleVersion; }
        public Boolean getIsStable() { return isStable; }
        public void setIsStable(Boolean isStable) { this.isStable = isStable; }
        public String getVersionFiles() { return versionFiles; }
        public void setVersionFiles(String versionFiles) { this.versionFiles = versionFiles; }
        public String getDependencies() { return dependencies; }
        public void setDependencies(String dependencies) { this.dependencies = dependencies; }
        public String getConfigTemplate() { return configTemplate; }
        public void setConfigTemplate(String configTemplate) { this.configTemplate = configTemplate; }
    }

    /**
     * 插件版本查询请求
     */
    class PluginVersionQueryRequest {
        private String status;

        // Getters and setters
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    /**
     * 插件版本响应
     */
    class PluginVersionResponse {
        private Long id;
        private Long pluginId;
        private String version;
        private String title;
        private String description;
        private String releaseNotes;
        private Boolean isLatest;
        private Boolean isStable;
        private String minCompatibleVersion;
        private String status;
        private Long publisherId;
        private java.time.LocalDateTime publishedAt;
        private String reviewStatus;
        private String reviewComment;
        private Long reviewerId;
        private java.time.LocalDateTime reviewedAt;
        private String versionFiles;
        private String dependencies;
        private String configTemplate;
        private Long downloadCount;
        private Long installCount;
        private java.time.LocalDateTime createdAt;
        private java.time.LocalDateTime updatedAt;

        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getPluginId() { return pluginId; }
        public void setPluginId(Long pluginId) { this.pluginId = pluginId; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getReleaseNotes() { return releaseNotes; }
        public void setReleaseNotes(String releaseNotes) { this.releaseNotes = releaseNotes; }
        public Boolean getIsLatest() { return isLatest; }
        public void setIsLatest(Boolean isLatest) { this.isLatest = isLatest; }
        public Boolean getIsStable() { return isStable; }
        public void setIsStable(Boolean isStable) { this.isStable = isStable; }
        public String getMinCompatibleVersion() { return minCompatibleVersion; }
        public void setMinCompatibleVersion(String minCompatibleVersion) { this.minCompatibleVersion = minCompatibleVersion; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public Long getPublisherId() { return publisherId; }
        public void setPublisherId(Long publisherId) { this.publisherId = publisherId; }
        public java.time.LocalDateTime getPublishedAt() { return publishedAt; }
        public void setPublishedAt(java.time.LocalDateTime publishedAt) { this.publishedAt = publishedAt; }
        public String getReviewStatus() { return reviewStatus; }
        public void setReviewStatus(String reviewStatus) { this.reviewStatus = reviewStatus; }
        public String getReviewComment() { return reviewComment; }
        public void setReviewComment(String reviewComment) { this.reviewComment = reviewComment; }
        public Long getReviewerId() { return reviewerId; }
        public void setReviewerId(Long reviewerId) { this.reviewerId = reviewerId; }
        public java.time.LocalDateTime getReviewedAt() { return reviewedAt; }
        public void setReviewedAt(java.time.LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }
        public String getVersionFiles() { return versionFiles; }
        public void setVersionFiles(String versionFiles) { this.versionFiles = versionFiles; }
        public String getDependencies() { return dependencies; }
        public void setDependencies(String dependencies) { this.dependencies = dependencies; }
        public String getConfigTemplate() { return configTemplate; }
        public void setConfigTemplate(String configTemplate) { this.configTemplate = configTemplate; }
        public Long getDownloadCount() { return downloadCount; }
        public void setDownloadCount(Long downloadCount) { this.downloadCount = downloadCount; }
        public Long getInstallCount() { return installCount; }
        public void setInstallCount(Long installCount) { this.installCount = installCount; }
        public java.time.LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(java.time.LocalDateTime createdAt) { this.createdAt = createdAt; }
        public java.time.LocalDateTime getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(java.time.LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    }
}
