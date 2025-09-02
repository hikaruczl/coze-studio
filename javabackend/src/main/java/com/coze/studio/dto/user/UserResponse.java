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

package com.coze.studio.dto.user;

import com.coze.studio.entity.User;
import com.coze.studio.entity.enums.UserRole;
import com.coze.studio.entity.enums.UserStatus;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户响应 DTO
 * 
 * @author coze-dev
 */
@Data
public class UserResponse {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

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
    private UserRole role;

    /**
     * 用户状态
     */
    private UserStatus status;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginAt;

    /**
     * 邮箱是否已验证
     */
    private Boolean emailVerified;

    /**
     * 手机是否已验证
     */
    private Boolean phoneVerified;

    /**
     * 个人简介
     */
    private String bio;

    /**
     * 时区
     */
    private String timezone;

    /**
     * 语言偏好
     */
    private String language;

    /**
     * 是否启用两步验证
     */
    private Boolean twoFactorEnabled;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 从 User 实体转换为 UserResponse
     */
    public static UserResponse fromUser(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setNickname(user.getNickname());
        response.setAvatarUrl(user.getAvatarUrl());
        response.setRole(user.getRole());
        response.setStatus(user.getStatus());
        response.setLastLoginAt(user.getLastLoginAt());
        response.setEmailVerified(user.isEmailVerified());
        response.setPhoneVerified(user.isPhoneVerified());
        response.setBio(user.getBio());
        response.setTimezone(user.getTimezone());
        response.setLanguage(user.getLanguage());
        response.setTwoFactorEnabled(user.getTwoFactorEnabled());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
}
