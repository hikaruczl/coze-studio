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
 * 插件安装记录实体
 * 
 * @author coze-dev
 */
@Data
@EqualsAndHashCode(callSuper = true)
// 表名: plugin_installations
// 索引和约束信息已移至数据库DDL脚本
public class PluginInstallation extends BaseEntity {

    /**
     * 用户ID
     */
        private Long userId;

    /**
     * 插件ID
     */
        private Long pluginId;

    /**
     * 安装的插件版本
     */
        private String installedVersion;

    /**
     * 安装状态：INSTALLING-安装中, INSTALLED-已安装, FAILED-安装失败, UNINSTALLED-已卸载
     */
        private String status = "INSTALLING";

    /**
     * 是否启用
     */
        private Boolean enabled = true;

    /**
     * 安装配置（JSON格式）
     */
        private String installationConfig;

    /**
     * 安装时间
     */
        private java.time.LocalDateTime installedAt;

    /**
     * 最后使用时间
     */
        private java.time.LocalDateTime lastUsedAt;

    /**
     * 使用次数
     */
        private Long usageCount = 0L;

    /**
     * 安装来源：STORE-应用商店, MANUAL-手动安装, IMPORT-导入
     */
        private String installSource = "STORE";

    /**
     * 错误信息（安装失败时）
     */
        private String errorMessage;

    /**
     * 卸载时间
     */
        private java.time.LocalDateTime uninstalledAt;

    /**
     * 卸载原因
     */
        private String uninstallReason;


    // Lombok生成的getter/setter方法
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPluginId() {
        return pluginId;
    }

    public void setPluginId(Long pluginId) {
        this.pluginId = pluginId;
    }

    public String getInstalledVersion() {
        return installedVersion;
    }

    public void setInstalledVersion(String installedVersion) {
        this.installedVersion = installedVersion;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getInstallationConfig() {
        return installationConfig;
    }

    public void setInstallationConfig(String installationConfig) {
        this.installationConfig = installationConfig;
    }

    public Long getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(Long usageCount) {
        this.usageCount = usageCount;
    }

    public String getInstallSource() {
        return installSource;
    }

    public void setInstallSource(String installSource) {
        this.installSource = installSource;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getUninstallReason() {
        return uninstallReason;
    }

    public void setUninstallReason(String uninstallReason) {
        this.uninstallReason = uninstallReason;
    }
}
