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

import com.coze.studio.entity.enums.UserRole;
import com.coze.studio.entity.enums.UserStatus;
// JPA注解已移除，改用MyBatis
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 用户实体类
 * 表示系统中的用户
 * 
 * @author coze-dev
 */
@Data
@EqualsAndHashCode(callSuper = true)
// 表名: users
// 索引信息已移至数据库DDL脚本
public class User extends BaseEntity {

    /**
     * 用户名（唯一）
     */
    private String username;

    /**
     * 邮箱（唯一）
     */
    private String email;

    /**
     * 手机号（唯一）
     */
    private String phone;

    /**
     * 密码（加密存储）
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像URL
     */
    private String avatarUrl;

    /**
     * 用户角色
     */
    private UserRole role = UserRole.USER;

    /**
     * 用户状态
     */
    private UserStatus status = UserStatus.INACTIVE;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginAt;

    /**
     * 最后登录IP
     */
    private String lastLoginIp;

    /**
     * 登录失败次数
     */
    private Integer failedLoginAttempts = 0;

    /**
     * 账户锁定时间
     */
    private LocalDateTime lockedUntil;

    /**
     * 邮箱验证时间
     */
    private LocalDateTime emailVerifiedAt;

    /**
     * 手机验证时间
     */
    private LocalDateTime phoneVerifiedAt;

    /**
     * 个人简介
     */
    private String bio;

    /**
     * 时区
     */
    private String timezone = "GMT+8";

    /**
     * 语言偏好
     */
    private String language = "zh-CN";

    /**
     * 是否启用两步验证
     */
    private Boolean twoFactorEnabled = false;

    /**
     * 两步验证密钥
     */
    private String twoFactorSecret;

    /**
     * 是否同意服务条款
     */
    private Boolean termsAccepted = false;

    /**
     * 服务条款同意时间
     */
    private LocalDateTime termsAcceptedAt;

    /**
     * 是否启用邮件通知
     */
    private Boolean emailNotificationsEnabled = true;

    /**
     * 检查用户是否已验证邮箱
     */
    public boolean isEmailVerified() {
        return emailVerifiedAt != null;
    }

    /**
     * 检查用户是否已验证手机
     */
    public boolean isPhoneVerified() {
        return phoneVerifiedAt != null;
    }

    /**
     * 检查账户是否被锁定
     */
    public boolean isAccountLocked() {
        return status == UserStatus.LOCKED && 
               lockedUntil != null && 
               lockedUntil.isAfter(LocalDateTime.now());
    }

    /**
     * 检查账户是否可用
     */
    public boolean isAccountEnabled() {
        return status == UserStatus.ACTIVE && !isAccountLocked();
    }


    // Lombok生成的getter/setter方法
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public Integer getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public void setFailedLoginAttempts(Integer failedLoginAttempts) {
        this.failedLoginAttempts = failedLoginAttempts;
    }

    public LocalDateTime getLockedUntil() {
        return lockedUntil;
    }

    public void setLockedUntil(LocalDateTime lockedUntil) {
        this.lockedUntil = lockedUntil;
    }

    public LocalDateTime getEmailVerifiedAt() {
        return emailVerifiedAt;
    }

    public void setEmailVerifiedAt(LocalDateTime emailVerifiedAt) {
        this.emailVerifiedAt = emailVerifiedAt;
    }

    public LocalDateTime getPhoneVerifiedAt() {
        return phoneVerifiedAt;
    }

    public void setPhoneVerifiedAt(LocalDateTime phoneVerifiedAt) {
        this.phoneVerifiedAt = phoneVerifiedAt;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Boolean isTwoFactorEnabled() {
        return twoFactorEnabled;
    }

    public void setTwoFactorEnabled(Boolean twoFactorEnabled) {
        this.twoFactorEnabled = twoFactorEnabled;
    }

    public String getTwoFactorSecret() {
        return twoFactorSecret;
    }

    public void setTwoFactorSecret(String twoFactorSecret) {
        this.twoFactorSecret = twoFactorSecret;
    }

    public Boolean isTermsAccepted() {
        return termsAccepted;
    }

    public void setTermsAccepted(Boolean termsAccepted) {
        this.termsAccepted = termsAccepted;
    }

    public LocalDateTime getTermsAcceptedAt() {
        return termsAcceptedAt;
    }

    public void setTermsAcceptedAt(LocalDateTime termsAcceptedAt) {
        this.termsAcceptedAt = termsAcceptedAt;
    }

    public Boolean isEmailNotificationsEnabled() {
        return emailNotificationsEnabled;
    }

    public void setEmailNotificationsEnabled(Boolean emailNotificationsEnabled) {
        this.emailNotificationsEnabled = emailNotificationsEnabled;
    }
}
