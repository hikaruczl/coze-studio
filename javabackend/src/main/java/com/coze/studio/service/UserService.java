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

import com.coze.studio.dto.user.*;
import com.coze.studio.dto.common.PageResponse;
import com.coze.studio.entity.User;
import org.springframework.data.domain.Pageable;

/**
 * 用户服务接口
 * 
 * @author coze-dev
 */
public interface UserService {

    /**
     * 用户注册
     */
    UserResponse register(RegisterRequest request);

    /**
     * 用户登录
     */
    LoginResponse login(LoginRequest request);

    /**
     * 刷新令牌
     */
    LoginResponse refreshToken(RefreshTokenRequest request);

    /**
     * 用户登出
     */
    void logout(String token);

    /**
     * 获取当前用户信息
     */
    UserResponse getCurrentUser(Long userId);

    /**
     * 更新用户信息
     */
    UserResponse updateUser(Long userId, UpdateUserRequest request);

    /**
     * 修改密码
     */
    void changePassword(Long userId, ChangePasswordRequest request);

    /**
     * 重置密码
     */
    void resetPassword(ResetPasswordRequest request);

    /**
     * 验证邮箱
     */
    void verifyEmail(String token);

    /**
     * 发送邮箱验证码
     */
    void sendEmailVerification(String email);

    /**
     * 根据ID获取用户
     */
    User getUserById(Long userId);

    /**
     * 根据用户名获取用户
     */
    User getUserByUsername(String username);

    /**
     * 分页查询用户列表
     */
    PageResponse<UserResponse> getUsers(UserQueryRequest request, Pageable pageable);

    /**
     * 启用/禁用用户
     */
    void toggleUserStatus(Long userId, boolean enabled);

    /**
     * 删除用户
     */
    void deleteUser(Long userId);

    /**
     * 检查用户名是否可用
     */
    boolean isUsernameAvailable(String username);

    /**
     * 检查邮箱是否可用
     */
    boolean isEmailAvailable(String email);

    /**
     * 更新用户最后登录信息
     */
    void updateLastLoginInfo(Long userId, String loginIp);

    /**
     * 处理登录失败
     */
    void handleLoginFailure(String usernameOrEmail);

    /**
     * 解锁用户账户
     */
    void unlockUser(Long userId);
}
