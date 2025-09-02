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
import com.coze.studio.entity.PromptCategory;
import com.coze.studio.entity.PromptTemplate;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 提示词管理服务接口
 * 提供提示词模板的创建、管理、版本控制和渲染功能
 * 
 * @author coze-dev
 */
public interface PromptManagementService {

    // ==================== 模板管理 ====================

    /**
     * 创建提示词模板
     */
    PromptTemplate createTemplate(CreateTemplateRequest request);

    /**
     * 更新提示词模板
     */
    PromptTemplate updateTemplate(Long id, UpdateTemplateRequest request);

    /**
     * 删除提示词模板
     */
    boolean deleteTemplate(Long id, Long userId);

    /**
     * 获取提示词模板
     */
    Optional<PromptTemplate> getTemplate(Long id, Long userId);

    /**
     * 根据名称获取模板
     */
    Optional<PromptTemplate> getTemplateByName(String name, Long userId);

    /**
     * 根据名称和版本获取模板
     */
    Optional<PromptTemplate> getTemplateByNameAndVersion(String name, String version, Long userId);

    /**
     * 获取模板列表
     */
    PageResponse<PromptTemplate> getTemplates(GetTemplatesRequest request, Pageable pageable);

    /**
     * 搜索模板
     */
    PageResponse<PromptTemplate> searchTemplates(SearchTemplatesRequest request, Pageable pageable);

    /**
     * 获取用户模板
     */
    PageResponse<PromptTemplate> getUserTemplates(Long userId, Pageable pageable);

    /**
     * 获取公开模板
     */
    PageResponse<PromptTemplate> getPublicTemplates(Pageable pageable);

    /**
     * 获取热门模板
     */
    PageResponse<PromptTemplate> getPopularTemplates(Pageable pageable);

    /**
     * 获取最新模板
     */
    PageResponse<PromptTemplate> getLatestTemplates(Pageable pageable);

    /**
     * 获取推荐模板
     */
    PageResponse<PromptTemplate> getRecommendedTemplates(Pageable pageable);

    // ==================== 版本管理 ====================

    /**
     * 创建新版本
     */
    PromptTemplate createNewVersion(Long templateId, CreateVersionRequest request);

    /**
     * 获取模板的所有版本
     */
    List<PromptTemplate> getTemplateVersions(Long rootId);

    /**
     * 获取模板的最新版本
     */
    Optional<PromptTemplate> getLatestVersion(Long rootId);

    /**
     * 比较两个版本
     */
    VersionComparisonResult compareVersions(Long version1Id, Long version2Id);

    // ==================== 模板操作 ====================

    /**
     * 发布模板
     */
    PromptTemplate publishTemplate(Long id, Long userId);

    /**
     * 弃用模板
     */
    PromptTemplate deprecateTemplate(Long id, Long userId);

    /**
     * 归档模板
     */
    PromptTemplate archiveTemplate(Long id, Long userId);

    /**
     * 复制模板
     */
    PromptTemplate cloneTemplate(Long id, CloneTemplateRequest request);

    /**
     * 收藏模板
     */
    boolean favoriteTemplate(Long templateId, Long userId);

    /**
     * 取消收藏模板
     */
    boolean unfavoriteTemplate(Long templateId, Long userId);

    /**
     * 评价模板
     */
    boolean rateTemplate(Long templateId, Long userId, double rating);

    /**
     * 下载模板
     */
    TemplateDownloadResult downloadTemplate(Long templateId, Long userId);

    // ==================== 模板渲染 ====================

    /**
     * 渲染提示词模板
     */
    String renderTemplate(Long templateId, Map<String, Object> variables, Long userId);

    /**
     * 根据名称渲染模板
     */
    String renderTemplateByName(String templateName, Map<String, Object> variables, Long userId);

    /**
     * 根据名称和版本渲染模板
     */
    String renderTemplateByNameAndVersion(String templateName, String version, Map<String, Object> variables, Long userId);

    /**
     * 预览模板渲染结果
     */
    TemplatePreviewResult previewTemplate(PreviewTemplateRequest request);

    /**
     * 验证模板语法
     */
    TemplateValidationResult validateTemplate(String content);

    /**
     * 提取模板变量
     */
    List<String> extractTemplateVariables(String content);

    // ==================== 分类管理 ====================

    /**
     * 创建分类
     */
    PromptCategory createCategory(CreateCategoryRequest request);

    /**
     * 更新分类
     */
    PromptCategory updateCategory(Long id, UpdateCategoryRequest request);

    /**
     * 删除分类
     */
    boolean deleteCategory(Long id);

    /**
     * 获取分类
     */
    Optional<PromptCategory> getCategory(Long id);

    /**
     * 根据代码获取分类
     */
    Optional<PromptCategory> getCategoryByCode(String code);

    /**
     * 获取所有分类
     */
    List<PromptCategory> getAllCategories();

    /**
     * 获取分类树
     */
    List<CategoryTreeNode> getCategoryTree();

    /**
     * 获取根分类
     */
    List<PromptCategory> getRootCategories();

    /**
     * 获取子分类
     */
    List<PromptCategory> getChildCategories(Long parentId);

    // ==================== 统计和分析 ====================

    /**
     * 获取模板统计
     */
    TemplateStatistics getTemplateStatistics(Long userId);

    /**
     * 获取用户模板统计
     */
    UserTemplateStatistics getUserTemplateStatistics(Long userId);

    /**
     * 获取分类统计
     */
    CategoryStatistics getCategoryStatistics();

    /**
     * 获取使用趋势
     */
    List<UsageTrendData> getUsageTrend(Long userId, String period);

    // ==================== 批量操作 ====================

    /**
     * 批量更新模板状态
     */
    BatchOperationResult batchUpdateStatus(List<Long> templateIds, String status, Long userId);

    /**
     * 批量删除模板
     */
    BatchOperationResult batchDeleteTemplates(List<Long> templateIds, Long userId);

    /**
     * 导出模板
     */
    TemplateExportResult exportTemplates(ExportTemplatesRequest request);

    /**
     * 导入模板
     */
    TemplateImportResult importTemplates(ImportTemplatesRequest request);

    // ==================== 请求和响应类 ====================

    /**
     * 创建模板请求
     */
    class CreateTemplateRequest {
        private String name;
        private String displayName;
        private String description;
        private String content;
        private String type;
        private String category;
        private List<String> tags;
        private String language;
        private Map<String, Object> parameters;
        private Map<String, Object> variables;
        private Map<String, Object> config;
        private Map<String, Object> metadata;
        private Boolean isPublic;
        private String author;
        private String license;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDisplayName() { return displayName; }
        public void setDisplayName(String displayName) { this.displayName = displayName; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public List<String> getTags() { return tags; }
        public void setTags(List<String> tags) { this.tags = tags; }
        public String getLanguage() { return language; }
        public void setLanguage(String language) { this.language = language; }
        public Map<String, Object> getParameters() { return parameters; }
        public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
        public Map<String, Object> getVariables() { return variables; }
        public void setVariables(Map<String, Object> variables) { this.variables = variables; }
        public Map<String, Object> getConfig() { return config; }
        public void setConfig(Map<String, Object> config) { this.config = config; }
        public Map<String, Object> getMetadata() { return metadata; }
        public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
        public Boolean getIsPublic() { return isPublic; }
        public void setIsPublic(Boolean isPublic) { this.isPublic = isPublic; }
        public String getAuthor() { return author; }
        public void setAuthor(String author) { this.author = author; }
        public String getLicense() { return license; }
        public void setLicense(String license) { this.license = license; }
    }

    /**
     * 更新模板请求
     */
    class UpdateTemplateRequest {
        private String displayName;
        private String description;
        private String content;
        private String category;
        private List<String> tags;
        private Map<String, Object> parameters;
        private Map<String, Object> variables;
        private Map<String, Object> config;
        private Map<String, Object> metadata;
        private Boolean isPublic;
        private String author;
        private String license;

        // Getters and setters (省略具体实现)
        public String getDisplayName() { return displayName; }
        public void setDisplayName(String displayName) { this.displayName = displayName; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public List<String> getTags() { return tags; }
        public void setTags(List<String> tags) { this.tags = tags; }
        public Map<String, Object> getParameters() { return parameters; }
        public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
        public Map<String, Object> getVariables() { return variables; }
        public void setVariables(Map<String, Object> variables) { this.variables = variables; }
        public Map<String, Object> getConfig() { return config; }
        public void setConfig(Map<String, Object> config) { this.config = config; }
        public Map<String, Object> getMetadata() { return metadata; }
        public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
        public Boolean getIsPublic() { return isPublic; }
        public void setIsPublic(Boolean isPublic) { this.isPublic = isPublic; }
        public String getAuthor() { return author; }
        public void setAuthor(String author) { this.author = author; }
        public String getLicense() { return license; }
        public void setLicense(String license) { this.license = license; }
    }

    /**
     * 获取模板列表请求
     */
    class GetTemplatesRequest {
        private String type;
        private String category;
        private String status;
        private String language;
        private Boolean isPublic;
        private Long userId;

        // Getters and setters (省略具体实现)
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getLanguage() { return language; }
        public void setLanguage(String language) { this.language = language; }
        public Boolean getIsPublic() { return isPublic; }
        public void setIsPublic(Boolean isPublic) { this.isPublic = isPublic; }
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
    }

    /**
     * 搜索模板请求
     */
    class SearchTemplatesRequest {
        private String keyword;
        private String type;
        private String category;
        private String status;
        private String language;
        private Boolean isPublic;
        private List<String> tags;
        private Long userId;

        // Getters and setters (省略具体实现)
        public String getKeyword() { return keyword; }
        public void setKeyword(String keyword) { this.keyword = keyword; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getLanguage() { return language; }
        public void setLanguage(String language) { this.language = language; }
        public Boolean getIsPublic() { return isPublic; }
        public void setIsPublic(Boolean isPublic) { this.isPublic = isPublic; }
        public List<String> getTags() { return tags; }
        public void setTags(List<String> tags) { this.tags = tags; }
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
    }

    // 其他请求和响应类（省略具体实现，但包含必要的字段和方法）
    class CreateVersionRequest {
        private String versionType; // major, minor, patch
        private String content;
        private String description;
        private Map<String, Object> parameters;
        private Map<String, Object> variables;
        private Map<String, Object> config;

        // Getters and setters
        public String getVersionType() { return versionType; }
        public void setVersionType(String versionType) { this.versionType = versionType; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Map<String, Object> getParameters() { return parameters; }
        public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
        public Map<String, Object> getVariables() { return variables; }
        public void setVariables(Map<String, Object> variables) { this.variables = variables; }
        public Map<String, Object> getConfig() { return config; }
        public void setConfig(Map<String, Object> config) { this.config = config; }
    }

    class CloneTemplateRequest {
        private String name;
        private String displayName;
        private String description;
        private Boolean isPublic;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDisplayName() { return displayName; }
        public void setDisplayName(String displayName) { this.displayName = displayName; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Boolean getIsPublic() { return isPublic; }
        public void setIsPublic(Boolean isPublic) { this.isPublic = isPublic; }
    }

    class PreviewTemplateRequest {
        private String content;
        private Map<String, Object> variables;

        // Getters and setters
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public Map<String, Object> getVariables() { return variables; }
        public void setVariables(Map<String, Object> variables) { this.variables = variables; }
    }

    // 响应类
    class VersionComparisonResult {
        private PromptTemplate version1;
        private PromptTemplate version2;
        private List<String> differences;
        private String diffHtml;

        public VersionComparisonResult(PromptTemplate version1, PromptTemplate version2, List<String> differences, String diffHtml) {
            this.version1 = version1;
            this.version2 = version2;
            this.differences = differences;
            this.diffHtml = diffHtml;
        }

        // Getters
        public PromptTemplate getVersion1() { return version1; }
        public PromptTemplate getVersion2() { return version2; }
        public List<String> getDifferences() { return differences; }
        public String getDiffHtml() { return diffHtml; }
    }

    class TemplateDownloadResult {
        private PromptTemplate template;
        private String downloadUrl;
        private String format;

        public TemplateDownloadResult(PromptTemplate template, String downloadUrl, String format) {
            this.template = template;
            this.downloadUrl = downloadUrl;
            this.format = format;
        }

        // Getters
        public PromptTemplate getTemplate() { return template; }
        public String getDownloadUrl() { return downloadUrl; }
        public String getFormat() { return format; }
    }

    class TemplatePreviewResult {
        private String renderedContent;
        private List<String> usedVariables;
        private List<String> missingVariables;
        private boolean hasErrors;
        private List<String> errors;

        public TemplatePreviewResult(String renderedContent, List<String> usedVariables, List<String> missingVariables, boolean hasErrors, List<String> errors) {
            this.renderedContent = renderedContent;
            this.usedVariables = usedVariables;
            this.missingVariables = missingVariables;
            this.hasErrors = hasErrors;
            this.errors = errors;
        }

        // Getters
        public String getRenderedContent() { return renderedContent; }
        public List<String> getUsedVariables() { return usedVariables; }
        public List<String> getMissingVariables() { return missingVariables; }
        public boolean isHasErrors() { return hasErrors; }
        public List<String> getErrors() { return errors; }
    }

    class TemplateValidationResult {
        private boolean isValid;
        private List<String> errors;
        private List<String> warnings;
        private List<String> variables;

        public TemplateValidationResult(boolean isValid, List<String> errors, List<String> warnings, List<String> variables) {
            this.isValid = isValid;
            this.errors = errors;
            this.warnings = warnings;
            this.variables = variables;
        }

        // Getters
        public boolean isValid() { return isValid; }
        public List<String> getErrors() { return errors; }
        public List<String> getWarnings() { return warnings; }
        public List<String> getVariables() { return variables; }
    }

    // 分类相关请求类
    class CreateCategoryRequest {
        private String code;
        private String name;
        private String displayName;
        private String description;
        private Long parentId;
        private String icon;
        private String color;
        private Integer sortOrder;

        // Getters and setters
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDisplayName() { return displayName; }
        public void setDisplayName(String displayName) { this.displayName = displayName; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Long getParentId() { return parentId; }
        public void setParentId(Long parentId) { this.parentId = parentId; }
        public String getIcon() { return icon; }
        public void setIcon(String icon) { this.icon = icon; }
        public String getColor() { return color; }
        public void setColor(String color) { this.color = color; }
        public Integer getSortOrder() { return sortOrder; }
        public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    }

    class UpdateCategoryRequest {
        private String name;
        private String displayName;
        private String description;
        private String icon;
        private String color;
        private Integer sortOrder;
        private Boolean enabled;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDisplayName() { return displayName; }
        public void setDisplayName(String displayName) { this.displayName = displayName; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getIcon() { return icon; }
        public void setIcon(String icon) { this.icon = icon; }
        public String getColor() { return color; }
        public void setColor(String color) { this.color = color; }
        public Integer getSortOrder() { return sortOrder; }
        public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
        public Boolean getEnabled() { return enabled; }
        public void setEnabled(Boolean enabled) { this.enabled = enabled; }
    }

    // 统计和其他响应类
    class CategoryTreeNode {
        private PromptCategory category;
        private List<CategoryTreeNode> children;

        public CategoryTreeNode(PromptCategory category) {
            this.category = category;
            this.children = new java.util.ArrayList<>();
        }

        // Getters and setters
        public PromptCategory getCategory() { return category; }
        public void setCategory(PromptCategory category) { this.category = category; }
        public List<CategoryTreeNode> getChildren() { return children; }
        public void setChildren(List<CategoryTreeNode> children) { this.children = children; }
    }

    class TemplateStatistics {
        private long totalTemplates;
        private long activeTemplates;
        private long draftTemplates;
        private long publicTemplates;
        private Map<String, Long> templatesByType;
        private Map<String, Long> templatesByCategory;
        private double averageRating;
        private long totalUsage;

        // Getters and setters
        public long getTotalTemplates() { return totalTemplates; }
        public void setTotalTemplates(long totalTemplates) { this.totalTemplates = totalTemplates; }
        public long getActiveTemplates() { return activeTemplates; }
        public void setActiveTemplates(long activeTemplates) { this.activeTemplates = activeTemplates; }
        public long getDraftTemplates() { return draftTemplates; }
        public void setDraftTemplates(long draftTemplates) { this.draftTemplates = draftTemplates; }
        public long getPublicTemplates() { return publicTemplates; }
        public void setPublicTemplates(long publicTemplates) { this.publicTemplates = publicTemplates; }
        public Map<String, Long> getTemplatesByType() { return templatesByType; }
        public void setTemplatesByType(Map<String, Long> templatesByType) { this.templatesByType = templatesByType; }
        public Map<String, Long> getTemplatesByCategory() { return templatesByCategory; }
        public void setTemplatesByCategory(Map<String, Long> templatesByCategory) { this.templatesByCategory = templatesByCategory; }
        public double getAverageRating() { return averageRating; }
        public void setAverageRating(double averageRating) { this.averageRating = averageRating; }
        public long getTotalUsage() { return totalUsage; }
        public void setTotalUsage(long totalUsage) { this.totalUsage = totalUsage; }
    }

    class UserTemplateStatistics {
        private long userTemplates;
        private long publicTemplates;
        private long favoriteTemplates;
        private Map<String, Long> templatesByType;
        private Map<String, Long> templatesByStatus;
        private long totalUsage;
        private double averageRating;

        // Getters and setters
        public long getUserTemplates() { return userTemplates; }
        public void setUserTemplates(long userTemplates) { this.userTemplates = userTemplates; }
        public long getPublicTemplates() { return publicTemplates; }
        public void setPublicTemplates(long publicTemplates) { this.publicTemplates = publicTemplates; }
        public long getFavoriteTemplates() { return favoriteTemplates; }
        public void setFavoriteTemplates(long favoriteTemplates) { this.favoriteTemplates = favoriteTemplates; }
        public Map<String, Long> getTemplatesByType() { return templatesByType; }
        public void setTemplatesByType(Map<String, Long> templatesByType) { this.templatesByType = templatesByType; }
        public Map<String, Long> getTemplatesByStatus() { return templatesByStatus; }
        public void setTemplatesByStatus(Map<String, Long> templatesByStatus) { this.templatesByStatus = templatesByStatus; }
        public long getTotalUsage() { return totalUsage; }
        public void setTotalUsage(long totalUsage) { this.totalUsage = totalUsage; }
        public double getAverageRating() { return averageRating; }
        public void setAverageRating(double averageRating) { this.averageRating = averageRating; }
    }

    class CategoryStatistics {
        private long totalCategories;
        private long rootCategories;
        private long leafCategories;
        private Map<Integer, Long> categoriesByLevel;
        private long totalTemplates;

        // Getters and setters
        public long getTotalCategories() { return totalCategories; }
        public void setTotalCategories(long totalCategories) { this.totalCategories = totalCategories; }
        public long getRootCategories() { return rootCategories; }
        public void setRootCategories(long rootCategories) { this.rootCategories = rootCategories; }
        public long getLeafCategories() { return leafCategories; }
        public void setLeafCategories(long leafCategories) { this.leafCategories = leafCategories; }
        public Map<Integer, Long> getCategoriesByLevel() { return categoriesByLevel; }
        public void setCategoriesByLevel(Map<Integer, Long> categoriesByLevel) { this.categoriesByLevel = categoriesByLevel; }
        public long getTotalTemplates() { return totalTemplates; }
        public void setTotalTemplates(long totalTemplates) { this.totalTemplates = totalTemplates; }
    }

    class UsageTrendData {
        private String period;
        private long usageCount;
        private long templateCount;

        public UsageTrendData(String period, long usageCount, long templateCount) {
            this.period = period;
            this.usageCount = usageCount;
            this.templateCount = templateCount;
        }

        // Getters
        public String getPeriod() { return period; }
        public long getUsageCount() { return usageCount; }
        public long getTemplateCount() { return templateCount; }
    }

    class BatchOperationResult {
        private boolean success;
        private int successCount;
        private int failedCount;
        private List<String> errors;

        public BatchOperationResult(boolean success, int successCount, int failedCount, List<String> errors) {
            this.success = success;
            this.successCount = successCount;
            this.failedCount = failedCount;
            this.errors = errors;
        }

        // Getters
        public boolean isSuccess() { return success; }
        public int getSuccessCount() { return successCount; }
        public int getFailedCount() { return failedCount; }
        public List<String> getErrors() { return errors; }
    }

    // 导入导出相关类
    class ExportTemplatesRequest {
        private List<Long> templateIds;
        private String format; // JSON, YAML, CSV
        private boolean includeMetadata;

        // Getters and setters
        public List<Long> getTemplateIds() { return templateIds; }
        public void setTemplateIds(List<Long> templateIds) { this.templateIds = templateIds; }
        public String getFormat() { return format; }
        public void setFormat(String format) { this.format = format; }
        public boolean isIncludeMetadata() { return includeMetadata; }
        public void setIncludeMetadata(boolean includeMetadata) { this.includeMetadata = includeMetadata; }
    }

    class ImportTemplatesRequest {
        private String data;
        private String format;
        private boolean overwriteExisting;
        private boolean validateBeforeImport;

        // Getters and setters
        public String getData() { return data; }
        public void setData(String data) { this.data = data; }
        public String getFormat() { return format; }
        public void setFormat(String format) { this.format = format; }
        public boolean isOverwriteExisting() { return overwriteExisting; }
        public void setOverwriteExisting(boolean overwriteExisting) { this.overwriteExisting = overwriteExisting; }
        public boolean isValidateBeforeImport() { return validateBeforeImport; }
        public void setValidateBeforeImport(boolean validateBeforeImport) { this.validateBeforeImport = validateBeforeImport; }
    }

    class TemplateExportResult {
        private String data;
        private String format;
        private int templateCount;
        private String downloadUrl;

        public TemplateExportResult(String data, String format, int templateCount, String downloadUrl) {
            this.data = data;
            this.format = format;
            this.templateCount = templateCount;
            this.downloadUrl = downloadUrl;
        }

        // Getters
        public String getData() { return data; }
        public String getFormat() { return format; }
        public int getTemplateCount() { return templateCount; }
        public String getDownloadUrl() { return downloadUrl; }
    }

    class TemplateImportResult {
        private boolean success;
        private int importedCount;
        private int skippedCount;
        private int failedCount;
        private List<String> errors;
        private List<String> warnings;

        public TemplateImportResult(boolean success, int importedCount, int skippedCount, int failedCount, List<String> errors, List<String> warnings) {
            this.success = success;
            this.importedCount = importedCount;
            this.skippedCount = skippedCount;
            this.failedCount = failedCount;
            this.errors = errors;
            this.warnings = warnings;
        }

        // Getters
        public boolean isSuccess() { return success; }
        public int getImportedCount() { return importedCount; }
        public int getSkippedCount() { return skippedCount; }
        public int getFailedCount() { return failedCount; }
        public List<String> getErrors() { return errors; }
        public List<String> getWarnings() { return warnings; }
    }
}
