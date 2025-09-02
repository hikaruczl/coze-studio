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
import com.coze.studio.dto.workflow.WorkflowResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * 工作流模板服务接口
 * 
 * @author coze-dev
 */
public interface WorkflowTemplateService {

    /**
     * 创建工作流模板
     */
    WorkflowTemplateResponse createTemplate(Long userId, CreateTemplateRequest request);

    /**
     * 更新工作流模板
     */
    WorkflowTemplateResponse updateTemplate(Long userId, Long templateId, UpdateTemplateRequest request);

    /**
     * 删除工作流模板
     */
    void deleteTemplate(Long userId, Long templateId);

    /**
     * 获取模板详情
     */
    WorkflowTemplateResponse getTemplateById(Long templateId);

    /**
     * 分页查询模板列表
     */
    PageResponse<WorkflowTemplateResponse> getTemplates(TemplateQueryRequest queryRequest, Pageable pageable);

    /**
     * 获取用户的模板列表
     */
    PageResponse<WorkflowTemplateResponse> getUserTemplates(Long userId, Pageable pageable);

    /**
     * 获取热门模板
     */
    PageResponse<WorkflowTemplateResponse> getPopularTemplates(Pageable pageable);

    /**
     * 搜索模板
     */
    PageResponse<WorkflowTemplateResponse> searchTemplates(String keyword, Pageable pageable);

    /**
     * 从模板创建工作流
     */
    WorkflowResponse createWorkflowFromTemplate(Long userId, Long templateId, String workflowName);

    /**
     * 发布模板到市场
     */
    WorkflowTemplateResponse publishTemplate(Long userId, Long templateId);

    /**
     * 取消发布模板
     */
    WorkflowTemplateResponse unpublishTemplate(Long userId, Long templateId);

    /**
     * 收藏模板
     */
    void favoriteTemplate(Long userId, Long templateId);

    /**
     * 取消收藏模板
     */
    void unfavoriteTemplate(Long userId, Long templateId);

    /**
     * 获取收藏的模板
     */
    PageResponse<WorkflowTemplateResponse> getFavoriteTemplates(Long userId, Pageable pageable);

    /**
     * 评价模板
     */
    void rateTemplate(Long userId, Long templateId, int rating, String comment);

    /**
     * 获取模板评价
     */
    PageResponse<TemplateRatingResponse> getTemplateRatings(Long templateId, Pageable pageable);

    /**
     * 导出模板
     */
    Map<String, Object> exportTemplate(Long templateId);

    /**
     * 导入模板
     */
    WorkflowTemplateResponse importTemplate(Long userId, Map<String, Object> templateData);

    /**
     * 获取模板统计信息
     */
    TemplateStatsResponse getTemplateStats(Long templateId);

    /**
     * 创建模板请求
     */
    class CreateTemplateRequest {
        private Long workflowId;
        private String name;
        private String description;
        private String category;
        private List<String> tags;
        private String iconUrl;
        private boolean isPublic;

        // Getters and setters
        public Long getWorkflowId() { return workflowId; }
        public void setWorkflowId(Long workflowId) { this.workflowId = workflowId; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        
        public List<String> getTags() { return tags; }
        public void setTags(List<String> tags) { this.tags = tags; }
        
        public String getIconUrl() { return iconUrl; }
        public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }
        
        public boolean isPublic() { return isPublic; }
        public void setPublic(boolean isPublic) { this.isPublic = isPublic; }
    }

    /**
     * 更新模板请求
     */
    class UpdateTemplateRequest {
        private String name;
        private String description;
        private String category;
        private List<String> tags;
        private String iconUrl;
        private boolean isPublic;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        
        public List<String> getTags() { return tags; }
        public void setTags(List<String> tags) { this.tags = tags; }
        
        public String getIconUrl() { return iconUrl; }
        public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }
        
        public boolean isPublic() { return isPublic; }
        public void setPublic(boolean isPublic) { this.isPublic = isPublic; }
    }

    /**
     * 模板查询请求
     */
    class TemplateQueryRequest {
        private String category;
        private List<String> tags;
        private String status;
        private Long creatorId;
        private String sortBy;
        private String sortDirection;

        // Getters and setters
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        
        public List<String> getTags() { return tags; }
        public void setTags(List<String> tags) { this.tags = tags; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public Long getCreatorId() { return creatorId; }
        public void setCreatorId(Long creatorId) { this.creatorId = creatorId; }
        
        public String getSortBy() { return sortBy; }
        public void setSortBy(String sortBy) { this.sortBy = sortBy; }
        
        public String getSortDirection() { return sortDirection; }
        public void setSortDirection(String sortDirection) { this.sortDirection = sortDirection; }
    }

    /**
     * 工作流模板响应
     */
    class WorkflowTemplateResponse {
        private Long id;
        private String name;
        private String description;
        private String category;
        private List<String> tags;
        private String iconUrl;
        private boolean isPublic;
        private String status;
        private Long creatorId;
        private String creatorName;
        private Long workflowId;
        private String version;
        private int usageCount;
        private int favoriteCount;
        private double averageRating;
        private int ratingCount;
        private String createdAt;
        private String updatedAt;
        private Map<String, Object> workflowDefinition;

        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        
        public List<String> getTags() { return tags; }
        public void setTags(List<String> tags) { this.tags = tags; }
        
        public String getIconUrl() { return iconUrl; }
        public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }
        
        public boolean isPublic() { return isPublic; }
        public void setPublic(boolean isPublic) { this.isPublic = isPublic; }
        public void setIsPublic(boolean isPublic) { this.isPublic = isPublic; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public Long getCreatorId() { return creatorId; }
        public void setCreatorId(Long creatorId) { this.creatorId = creatorId; }
        
        public String getCreatorName() { return creatorName; }
        public void setCreatorName(String creatorName) { this.creatorName = creatorName; }

        public Long getWorkflowId() { return workflowId; }
        public void setWorkflowId(Long workflowId) { this.workflowId = workflowId; }

        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }

        public int getUsageCount() { return usageCount; }
        public void setUsageCount(int usageCount) { this.usageCount = usageCount; }
        
        public int getFavoriteCount() { return favoriteCount; }
        public void setFavoriteCount(int favoriteCount) { this.favoriteCount = favoriteCount; }
        
        public double getAverageRating() { return averageRating; }
        public void setAverageRating(double averageRating) { this.averageRating = averageRating; }
        
        public int getRatingCount() { return ratingCount; }
        public void setRatingCount(int ratingCount) { this.ratingCount = ratingCount; }
        
        public String getCreatedAt() { return createdAt; }
        public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
        
        public String getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
        
        public Map<String, Object> getWorkflowDefinition() { return workflowDefinition; }
        public void setWorkflowDefinition(Map<String, Object> workflowDefinition) { this.workflowDefinition = workflowDefinition; }
    }

    /**
     * 模板评价响应
     */
    class TemplateRatingResponse {
        private Long id;
        private Long templateId;
        private Long userId;
        private String userName;
        private int rating;
        private String comment;
        private String createdAt;

        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public Long getTemplateId() { return templateId; }
        public void setTemplateId(Long templateId) { this.templateId = templateId; }
        
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        
        public String getUserName() { return userName; }
        public void setUserName(String userName) { this.userName = userName; }
        
        public int getRating() { return rating; }
        public void setRating(int rating) { this.rating = rating; }
        
        public String getComment() { return comment; }
        public void setComment(String comment) { this.comment = comment; }
        
        public String getCreatedAt() { return createdAt; }
        public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    }

    /**
     * 模板统计响应
     */
    class TemplateStatsResponse {
        private Long templateId;
        private int totalUsage;
        private int monthlyUsage;
        private int weeklyUsage;
        private int dailyUsage;
        private int favoriteCount;
        private double averageRating;
        private int ratingCount;
        private Map<String, Integer> usageByCategory;
        private Map<String, Integer> usageTrend;

        // Getters and setters
        public Long getTemplateId() { return templateId; }
        public void setTemplateId(Long templateId) { this.templateId = templateId; }
        
        public int getTotalUsage() { return totalUsage; }
        public void setTotalUsage(int totalUsage) { this.totalUsage = totalUsage; }
        
        public int getMonthlyUsage() { return monthlyUsage; }
        public void setMonthlyUsage(int monthlyUsage) { this.monthlyUsage = monthlyUsage; }
        
        public int getWeeklyUsage() { return weeklyUsage; }
        public void setWeeklyUsage(int weeklyUsage) { this.weeklyUsage = weeklyUsage; }
        
        public int getDailyUsage() { return dailyUsage; }
        public void setDailyUsage(int dailyUsage) { this.dailyUsage = dailyUsage; }
        
        public int getFavoriteCount() { return favoriteCount; }
        public void setFavoriteCount(int favoriteCount) { this.favoriteCount = favoriteCount; }
        
        public double getAverageRating() { return averageRating; }
        public void setAverageRating(double averageRating) { this.averageRating = averageRating; }
        
        public int getRatingCount() { return ratingCount; }
        public void setRatingCount(int ratingCount) { this.ratingCount = ratingCount; }
        
        public Map<String, Integer> getUsageByCategory() { return usageByCategory; }
        public void setUsageByCategory(Map<String, Integer> usageByCategory) { this.usageByCategory = usageByCategory; }
        
        public Map<String, Integer> getUsageTrend() { return usageTrend; }
        public void setUsageTrend(Map<String, Integer> usageTrend) { this.usageTrend = usageTrend; }
    }
}
