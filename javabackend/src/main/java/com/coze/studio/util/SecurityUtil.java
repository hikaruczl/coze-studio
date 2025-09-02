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

package com.coze.studio.util;

import com.coze.studio.security.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * 安全工具类
 * 提供获取当前用户信息的便捷方法
 * 
 * @author coze-dev
 */
public final class SecurityUtil {

    private SecurityUtil() {
        // 防止实例化
    }

    /**
     * 获取当前认证信息
     */
    public static Optional<Authentication> getCurrentAuthentication() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
    }

    /**
     * 获取当前用户主体
     */
    public static Optional<UserPrincipal> getCurrentUserPrincipal() {
        return getCurrentAuthentication()
                .filter(auth -> auth.getPrincipal() instanceof UserPrincipal)
                .map(auth -> (UserPrincipal) auth.getPrincipal());
    }

    /**
     * 获取当前用户ID
     */
    public static Optional<Long> getCurrentUserId() {
        return getCurrentUserPrincipal()
                .map(UserPrincipal::getId);
    }

    /**
     * 获取当前用户名
     */
    public static Optional<String> getCurrentUsername() {
        return getCurrentUserPrincipal()
                .map(UserPrincipal::getUsername);
    }

    /**
     * 获取当前用户邮箱
     */
    public static Optional<String> getCurrentUserEmail() {
        return getCurrentUserPrincipal()
                .map(UserPrincipal::getEmail);
    }

    /**
     * 获取当前用户角色
     */
    public static Optional<String> getCurrentUserRole() {
        return getCurrentUserPrincipal()
                .map(UserPrincipal::getRole);
    }

    /**
     * 检查当前用户是否已认证
     */
    public static boolean isAuthenticated() {
        return getCurrentAuthentication()
                .map(Authentication::isAuthenticated)
                .orElse(false);
    }

    /**
     * 检查当前用户是否具有指定角色
     */
    public static boolean hasRole(String role) {
        return getCurrentAuthentication()
                .map(auth -> auth.getAuthorities().stream()
                        .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + role)))
                .orElse(false);
    }

    /**
     * 检查当前用户是否是管理员
     */
    public static boolean isAdmin() {
        return hasRole("ADMIN") || hasRole("SUPER_ADMIN");
    }

    /**
     * 检查当前用户是否是超级管理员
     */
    public static boolean isSuperAdmin() {
        return hasRole("SUPER_ADMIN");
    }
}
