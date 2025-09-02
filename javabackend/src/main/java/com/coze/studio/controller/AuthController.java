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

package com.coze.studio.controller;

import com.coze.studio.dto.common.ApiResponse;
import com.coze.studio.dto.user.*;
import com.coze.studio.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 * 处理用户注册、登录、登出等认证相关操作
 * 
 * @author coze-dev
 */
@Tag(name = "认证管理", description = "用户认证相关接口")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    /**
     * 用户注册
     */
    @Operation(summary = "用户注册", description = "创建新用户账户")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse user = userService.register(request);
        return ResponseEntity.ok(ApiResponse.success("注册成功", user));
    }

    /**
     * 用户登录
     */
    @Operation(summary = "用户登录", description = "用户登录获取访问令牌")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        return ResponseEntity.ok(ApiResponse.success("登录成功", response));
    }

    /**
     * 刷新令牌
     */
    @Operation(summary = "刷新令牌", description = "使用刷新令牌获取新的访问令牌")
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        LoginResponse response = userService.refreshToken(request);
        return ResponseEntity.ok(ApiResponse.success("令牌刷新成功", response));
    }

    /**
     * 用户登出
     */
    @Operation(summary = "用户登出", description = "用户登出并使令牌失效")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token != null) {
            userService.logout(token);
        }
        return ResponseEntity.ok(ApiResponse.success("登出成功"));
    }

    /**
     * 重置密码
     */
    @Operation(summary = "重置密码", description = "通过邮箱重置密码")
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        userService.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.success("密码重置成功"));
    }

    /**
     * 验证邮箱
     */
    @Operation(summary = "验证邮箱", description = "通过验证令牌验证邮箱")
    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse<String>> verifyEmail(@RequestParam String token) {
        userService.verifyEmail(token);
        return ResponseEntity.ok(ApiResponse.success("邮箱验证成功"));
    }

    /**
     * 发送邮箱验证码
     */
    @Operation(summary = "发送邮箱验证码", description = "向指定邮箱发送验证码")
    @PostMapping("/send-verification")
    public ResponseEntity<ApiResponse<String>> sendEmailVerification(@RequestParam String email) {
        userService.sendEmailVerification(email);
        return ResponseEntity.ok(ApiResponse.success("验证码已发送"));
    }

    /**
     * 检查用户名可用性
     */
    @Operation(summary = "检查用户名可用性", description = "检查用户名是否可用")
    @GetMapping("/check-username")
    public ResponseEntity<ApiResponse<Boolean>> checkUsername(@RequestParam String username) {
        boolean available = userService.isUsernameAvailable(username);
        return ResponseEntity.ok(ApiResponse.success(available));
    }

    /**
     * 检查邮箱可用性
     */
    @Operation(summary = "检查邮箱可用性", description = "检查邮箱是否可用")
    @GetMapping("/check-email")
    public ResponseEntity<ApiResponse<Boolean>> checkEmail(@RequestParam String email) {
        boolean available = userService.isEmailAvailable(email);
        return ResponseEntity.ok(ApiResponse.success(available));
    }

    /**
     * 从请求头中提取令牌
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
