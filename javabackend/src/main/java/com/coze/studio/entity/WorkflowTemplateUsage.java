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

package com.coze.studio.entity;

// JPA注解已移除，改用MyBatis
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 工作流模板使用记录实体
 * 
 * @author coze-dev
 */
@Data
@EqualsAndHashCode(callSuper = true)
// 表名: workflow_template_usage
// 索引信息已移至数据库DDL脚本
public class WorkflowTemplateUsage extends BaseEntity {

    /**
     * 用户ID
     */
        private Long userId;

    /**
     * 模板ID
     */
        private Long templateId;

    /**
     * 创建的工作流ID
     */
        private Long createdWorkflowId;

    /**
     * 使用类型：CREATE-创建工作流, PREVIEW-预览, DOWNLOAD-下载
     */
        private String usageType;

    /**
     * 使用来源：WEB-网页, API-接口, MOBILE-移动端
     */
        private String usageSource;

    /**
     * 用户代理信息
     */
        private String userAgent;

    /**
     * IP地址
     */
        private String ipAddress;

    /**
     * 额外信息（JSON格式）
     */
        private String extraInfo;


    // Lombok生成的getter/setter方法
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Long getCreatedWorkflowId() {
        return createdWorkflowId;
    }

    public void setCreatedWorkflowId(Long createdWorkflowId) {
        this.createdWorkflowId = createdWorkflowId;
    }

    public String getUsageType() {
        return usageType;
    }

    public void setUsageType(String usageType) {
        this.usageType = usageType;
    }

    public String getUsageSource() {
        return usageSource;
    }

    public void setUsageSource(String usageSource) {
        this.usageSource = usageSource;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }
}
