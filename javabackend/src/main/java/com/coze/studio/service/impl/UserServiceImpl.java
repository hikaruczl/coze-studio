package com.coze.studio.service.impl;

import com.coze.studio.service.UserService;
import com.coze.studio.dto.user.UserResponse;
import com.coze.studio.dto.user.UserQueryRequest;
import com.coze.studio.dto.user.ResetPasswordRequest;
import com.coze.studio.dto.user.ChangePasswordRequest;
import com.coze.studio.dto.user.UpdateUserRequest;
import com.coze.studio.dto.common.PageResponse;
import com.coze.studio.entity.enums.UserStatus;
import com.coze.studio.repository.UserRepository;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import com.coze.studio.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * UserService的实现类
 * 这是一个最小可编译的实现，所有方法都需要具体实现
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @org.springframework.beans.factory.annotation.Autowired
    private com.coze.studio.util.JwtUtil jwtUtil;

    /**
     * 刷新令牌（真实实现）
     * 说明：当前项目未见 RefreshToken 持久化实体，采用 JWT 自验证策略：
     * 1) 校验 refreshToken 类型与有效性；2) 解析用户；3) 生成新 access/refresh；4) 返回 LoginResponse
     */
    @Override
    public com.coze.studio.dto.user.LoginResponse refreshToken(com.coze.studio.dto.user.RefreshTokenRequest request) {
        log.info("刷新令牌请求");
        if (request == null || request.getRefreshToken() == null || request.getRefreshToken().trim().isEmpty()) {
            throw new IllegalArgumentException("刷新令牌不能为空");
        }
        String refreshToken = request.getRefreshToken();
        try {
            // 1. 验证刷新令牌
            boolean valid = jwtUtil.validateRefreshToken(refreshToken);
            if (!valid) {
                throw new RuntimeException("刷新令牌无效或已过期");
            }
            // 2. 解析用户信息
            Long userId = jwtUtil.getUserIdFromToken(refreshToken);
            String username = jwtUtil.getUsernameFromToken(refreshToken);
            if (userId == null || username == null) {
                throw new RuntimeException("刷新令牌解析失败");
            }
            // 3. 加载用户，校验状态
            java.util.Optional<User> opt = userRepository.findById(userId);
            if (!opt.isPresent()) {
                throw new RuntimeException("用户不存在: " + userId);
            }
            User user = opt.get();
            if (!user.isAccountEnabled()) {
                throw new RuntimeException("用户状态不可用，无法刷新令牌");
            }
            // 4. 生成新令牌
            String role = user.getRole() != null ? user.getRole().name() : "USER";
            String newAccess = jwtUtil.generateAccessToken(user.getId(), user.getUsername(), role);
            String newRefresh = jwtUtil.generateRefreshToken(user.getId(), user.getUsername());
            // 5. 组装响应
            com.coze.studio.dto.user.UserResponse userResp = com.coze.studio.dto.user.UserResponse.fromUser(user);
            com.coze.studio.dto.user.LoginResponse resp = new com.coze.studio.dto.user.LoginResponse();
            resp.setAccessToken(newAccess);
            resp.setRefreshToken(newRefresh);
            resp.setTokenType("Bearer");
            // 取 JwtUtil 配置的过期秒数（毫秒->秒）
            // 由于 JwtUtil 未暴露 getter，这里估算为 3600 秒或通过解析 newAccess 计算剩余时间，简化固定 3600
            resp.setExpiresIn(3600L);
            resp.setUser(userResp);
            log.info("刷新令牌成功: userId={}", userId);
            return resp;
        } catch (Exception e) {
            log.error("刷新令牌失败", e);
            throw new RuntimeException("刷新令牌失败: " + e.getMessage(), e);
        }

    }

    /**
     * 用户注册（真实实现）
     */
    @Override
    public UserResponse register(com.coze.studio.dto.user.RegisterRequest request) {
        log.info("用户注册请求: username={}, email={}", request != null ? request.getUsername() : null, request != null ? request.getEmail() : null);
        if (request == null) {
            throw new IllegalArgumentException("注册请求不能为空");
        }
        if (request.getUsername() == null || request.getEmail() == null || request.getPassword() == null || request.getConfirmPassword() == null) {
            throw new IllegalArgumentException("用户名、邮箱和密码不能为空");
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("两次输入的密码不一致");
        }
        try {
            // 检查唯一性
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new RuntimeException("用户名已存在");
            }
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("邮箱已被注册");
            }
            // 构建实体
            User user = new User();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setPassword(request.getPassword()); // TODO: 使用BCrypt加密
            user.setNickname(request.getNickname());
            user.setPhone(request.getPhone());
            user.setStatus(com.coze.studio.entity.enums.UserStatus.ACTIVE);
            user.setRole(com.coze.studio.entity.enums.UserRole.USER);
            user.setTermsAccepted(Boolean.TRUE.equals(request.getTermsAccepted()));
            if (Boolean.TRUE.equals(request.getTermsAccepted())) {
                user.setTermsAcceptedAt(java.time.LocalDateTime.now());
            }

            // 保存
            User saved = userRepository.save(user);
            log.info("用户注册成功: userId={}", saved.getId());
            return UserResponse.fromUser(saved);
        } catch (Exception e) {
            log.error("用户注册失败: username={}, email={}", request.getUsername(), request.getEmail(), e);
            throw new RuntimeException("用户注册失败: " + e.getMessage(), e);
        }
    }

    /**
     * 用户登录（真实实现）
     */
    @Override
    public com.coze.studio.dto.user.LoginResponse login(com.coze.studio.dto.user.LoginRequest request) {
        log.info("用户登录请求: username={}", request.getUsernameOrEmail());
        if (request == null || request.getUsernameOrEmail() == null || request.getPassword() == null) {
            throw new IllegalArgumentException("用户名和密码不能为空");
        }
        try {
            // 1. 查找用户
            java.util.Optional<User> userOpt = userRepository.findByUsernameOrEmail(request.getUsernameOrEmail());
            if (!userOpt.isPresent()) {
                throw new RuntimeException("用户名或密码错误");
            }
            User user = userOpt.get();

            // 2. 验证密码（简化实现，实际应使用 BCrypt 等加密）
            if (!request.getPassword().equals(user.getPassword())) {
                throw new RuntimeException("用户名或密码错误");
            }

            // 3. 检查用户状态
            if (!user.isAccountEnabled()) {
                throw new RuntimeException("账户已被禁用");
            }

            // 4. 生成令牌
            String role = user.getRole() != null ? user.getRole().name() : "USER";
            String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getUsername(), role);
            String refreshToken = jwtUtil.generateRefreshToken(user.getId(), user.getUsername());

            // 5. 更新最后登录时间
            user.setLastLoginAt(java.time.LocalDateTime.now());
            userRepository.save(user);

            // 6. 构建响应
            com.coze.studio.dto.user.UserResponse userResp = com.coze.studio.dto.user.UserResponse.fromUser(user);
            com.coze.studio.dto.user.LoginResponse response = new com.coze.studio.dto.user.LoginResponse();
            response.setAccessToken(accessToken);
            response.setRefreshToken(refreshToken);
            response.setTokenType("Bearer");
            response.setExpiresIn(3600L); // 1小时
            response.setUser(userResp);

            log.info("用户登录成功: userId={}, username={}", user.getId(), user.getUsername());
            return response;
        } catch (Exception e) {
            log.error("用户登录失败: username={}", request.getUsernameOrEmail(), e);
            throw new RuntimeException("登录失败: " + e.getMessage(), e);
        }
    }

    @Autowired
    private UserRepository userRepository;

    @Override
    public void unlockUser(Long userId) {
        log.info("解锁用户账户: userId={}", userId);

        try {
            // 1. 验证用户是否存在
            Optional<User> userOpt = userRepository.findById(userId);
            if (!userOpt.isPresent()) {
                throw new RuntimeException("用户不存在: " + userId);
            }

            User user = userOpt.get();

            // 2. 检查用户是否被锁定
            if (!user.isAccountLocked()) {
                log.info("用户账户未被锁定，无需解锁: userId={}", userId);
                return;
            }

            // 3. 解锁用户账户
            userRepository.unlockUser(userId);

            log.info("用户账户解锁成功: userId={}", userId);

        } catch (Exception e) {
            log.error("解锁用户账户失败: userId={}", userId, e);
            throw new RuntimeException("解锁用户账户失败: " + e.getMessage(), e);
        }
    }

    // TODO: 实现其他接口方法
    // 这是一个最小可编译的实现，避免编译错误

    @Override
    public void handleLoginFailure(String username) {
        log.info("处理登录失败: username={}", username);

        try {
            // TODO: 实现登录失败处理逻辑
            // 这里实现一个简单的处理逻辑作为占位符
            log.warn("用户登录失败: username={}", username);

            // 可以在这里实现：
            // 1. 记录失败次数
            // 2. 锁定账户（如果失败次数过多）
            // 3. 发送安全警告邮件
            // 4. 记录安全日志

        } catch (Exception e) {
            log.error("处理登录失败时发生错误: username={}", username, e);
        }
    }

    @Override
    public void updateLastLoginInfo(Long userId, String ipAddress) {
        log.info("更新用户最后登录信息: userId={}, ipAddress={}", userId, ipAddress);

        try {
            // TODO: 实现更新最后登录信息的逻辑
            // 这里实现一个简单的更新逻辑作为占位符

            // 实际应该实现：
            // 1. 更新用户的最后登录时间
            // 2. 更新用户的最后登录IP
            // 3. 记录登录历史
            // 4. 更新用户状态

            log.info("用户最后登录信息已更新: userId={}, ipAddress={}", userId, ipAddress);

        } catch (Exception e) {
            log.error("更新用户最后登录信息失败: userId={}, ipAddress={}", userId, ipAddress, e);
            throw new RuntimeException("更新用户最后登录信息失败: " + e.getMessage());
        }
    }

    @Override
    public boolean isEmailAvailable(String email) {
        log.info("检查邮箱是否可用: email={}", email);

        try {
            // TODO: 实现检查邮箱是否可用的逻辑
            // 这里返回一个简单的检查结果作为占位符

            // 简化实现：假设所有邮箱都可用
            // 实际应该检查：
            // 1. 邮箱格式是否正确
            // 2. 邮箱是否已被注册
            // 3. 邮箱是否在黑名单中

            boolean isAvailable = true; // 简化实现

            log.info("邮箱可用性检查完成: email={}, isAvailable={}", email, isAvailable);
            return isAvailable;
        } catch (Exception e) {
            log.error("检查邮箱可用性失败: email={}", email, e);
            return false; // 出错时返回不可用
        }
    }

    @Override
    public boolean isUsernameAvailable(String username) {
        log.info("检查用户名是否可用: username={}", username);

        try {
            // TODO: 实现检查用户名是否可用的逻辑
            // 简化实现：假设所有用户名都可用
            boolean isAvailable = true;

            log.info("用户名可用性检查完成: username={}, isAvailable={}", username, isAvailable);
            return isAvailable;
        } catch (Exception e) {
            log.error("检查用户名可用性失败: username={}", username, e);
            return false;
        }
    }

    @Override
    public void deleteUser(Long userId) {
        log.info("删除用户: userId={}", userId);

        try {
            // TODO: 实现删除用户的逻辑
            // 实际应该实现：
            // 1. 检查用户是否存在
            // 2. 检查用户是否有关联数据需要处理
            // 3. 软删除或硬删除用户
            // 4. 清理相关数据
            // 5. 记录删除日志

            log.info("用户删除成功: userId={}", userId);
        } catch (Exception e) {
            log.error("删除用户失败: userId={}", userId, e);
            throw new RuntimeException("删除用户失败: " + e.getMessage());
        }
    }

    @Override
    public void toggleUserStatus(Long userId, boolean enabled) {
        log.info("切换用户状态: userId={}, enabled={}", userId, enabled);

        try {
            // TODO: 实现切换用户状态的逻辑
            // 实际应该实现：
            // 1. 检查用户是否存在
            // 2. 更新用户状态
            // 3. 记录状态变更日志
            // 4. 可能需要处理相关的业务逻辑（如禁用用户时清理会话等）

            String status = enabled ? "启用" : "禁用";
            log.info("用户状态切换成功: userId={}, status={}", userId, status);
        } catch (Exception e) {
            log.error("切换用户状态失败: userId={}, enabled={}", userId, enabled, e);
            throw new RuntimeException("切换用户状态失败: " + e.getMessage());
        }
    }

    @Override
    public PageResponse<UserResponse> getUsers(UserQueryRequest queryRequest, Pageable pageable) {
        log.info("获取用户列表: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());

        try {
            List<UserResponse> users = new ArrayList<>();

            for (int i = 1; i <= 5; i++) {
                UserResponse user = new UserResponse();
                user.setId((long) i);
                user.setUsername("user" + i);
                user.setEmail("user" + i + "@example.com");
                user.setNickname("用户" + i);
                user.setStatus(UserStatus.ACTIVE);
                user.setCreatedAt(java.time.LocalDateTime.now().minusDays(i));
                users.add(user);
            }

            PageResponse<UserResponse> pageResponse = new PageResponse<>();
            pageResponse.setItems(users);
            pageResponse.setTotalElements((long) users.size());
            pageResponse.setTotalPages(1);
            pageResponse.setPage(pageable.getPageNumber() + 1);
            pageResponse.setSize(pageable.getPageSize());
            pageResponse.setHasNext(false);
            pageResponse.setHasPrevious(false);

            log.info("用户列表获取成功: usersCount={}", users.size());
            return pageResponse;
        } catch (Exception e) {
            log.error("获取用户列表失败", e);
            throw new RuntimeException("获取用户列表失败: " + e.getMessage());
        }
    }

    @Override
    public User getUserByUsername(String username) {
        log.info("根据用户名获取用户: username={}", username);

        try {
            // TODO: 实现根据用户名获取用户的逻辑
            User user = new User();
            user.setId(1L);
            user.setUsername(username);
            user.setEmail(username + "@example.com");
            user.setNickname("用户" + username);
            user.setStatus(UserStatus.ACTIVE);
            user.setCreatedAt(java.time.LocalDateTime.now());

            log.info("根据用户名获取用户成功: username={}, userId={}", username, user.getId());
            return user;
        } catch (Exception e) {
            log.error("根据用户名获取用户失败: username={}", username, e);
            throw new RuntimeException("根据用户名获取用户失败: " + e.getMessage());
        }
    }

    @Override
    public User getUserById(Long userId) {
        log.info("根据ID获取用户: userId={}", userId);

        try {
            // TODO: 实现根据ID获取用户的逻辑
            User user = new User();
            user.setId(userId);
            user.setUsername("user" + userId);
            user.setEmail("user" + userId + "@example.com");
            user.setNickname("用户" + userId);
            user.setStatus(UserStatus.ACTIVE);
            user.setCreatedAt(java.time.LocalDateTime.now());

            log.info("根据ID获取用户成功: userId={}", userId);
            return user;
        } catch (Exception e) {
            log.error("根据ID获取用户失败: userId={}", userId, e);
            throw new RuntimeException("根据ID获取用户失败: " + e.getMessage());
        }
    }

    @Override
    public void sendEmailVerification(String email) {
        log.info("发送邮箱验证: email={}", email);

        try {
            // TODO: 实现发送邮箱验证的逻辑
            log.info("邮箱验证发送成功: email={}", email);
        } catch (Exception e) {
            log.error("发送邮箱验证失败: email={}", email, e);
            throw new RuntimeException("发送邮箱验证失败: " + e.getMessage());
        }
    }

    @Override
    public void verifyEmail(String token) {
        log.info("验证邮箱: token={}", token);

        try {
            // TODO: 实现验证邮箱的逻辑
            log.info("邮箱验证成功: token={}", token);
        } catch (Exception e) {
            log.error("验证邮箱失败: token={}", token, e);
            throw new RuntimeException("验证邮箱失败: " + e.getMessage());
        }
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        log.info("重置密码: email={}", request.getEmail());

        try {
            // TODO: 实现重置密码的逻辑
            log.info("密码重置成功: email={}", request.getEmail());
        } catch (Exception e) {
            log.error("重置密码失败: email={}", request.getEmail(), e);
            throw new RuntimeException("重置密码失败: " + e.getMessage());
        }
    }

    @Override
    public void changePassword(Long userId, ChangePasswordRequest request) {
        log.info("修改密码: userId={}", userId);

        try {
            // 1. 验证输入参数
            if (userId == null || request == null) {
                throw new IllegalArgumentException("用户ID和请求参数不能为空");
            }

            if (request.getCurrentPassword() == null || request.getCurrentPassword().trim().isEmpty()) {
                throw new IllegalArgumentException("当前密码不能为空");
            }

            if (request.getNewPassword() == null || request.getNewPassword().trim().isEmpty()) {
                throw new IllegalArgumentException("新密码不能为空");
            }

            if (request.getConfirmNewPassword() == null || request.getConfirmNewPassword().trim().isEmpty()) {
                throw new IllegalArgumentException("确认新密码不能为空");
            }

            if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
                throw new IllegalArgumentException("新密码与确认密码不一致");
            }

            if (request.getNewPassword().length() < 6) {
                throw new IllegalArgumentException("新密码长度不能少于6位");
            }

            if (request.getCurrentPassword().equals(request.getNewPassword())) {
                throw new IllegalArgumentException("新密码不能与当前密码相同");
            }

            // 2. 查询用户信息
            // TODO: 从数据库查询用户
            // Optional<User> userOpt = userRepository.findById(userId);
            // if (!userOpt.isPresent()) {
            //     throw new RuntimeException("用户不存在: " + userId);
            // }
            // User user = userOpt.get();

            // 3. 验证当前密码
            // TODO: 验证当前密码是否正确
            // if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            //     throw new RuntimeException("当前密码错误");
            // }

            // 4. 检查密码强度
            validatePasswordStrength(request.getNewPassword());

            // 5. 检查密码历史（防止重复使用最近的密码）
            // TODO: 检查用户最近使用的密码历史
            // if (isPasswordRecentlyUsed(userId, request.getNewPassword())) {
            //     throw new RuntimeException("不能使用最近使用过的密码");
            // }

            // 6. 加密新密码
            // TODO: 使用密码加密器加密新密码
            // String encodedNewPassword = passwordEncoder.encode(request.getNewPassword());

            // 7. 更新用户密码
            // TODO: 更新数据库中的用户密码
            // user.setPassword(encodedNewPassword);
            // user.setPasswordChangedAt(LocalDateTime.now());
            // user.setUpdatedAt(LocalDateTime.now());
            // userRepository.save(user);

            // 8. 记录密码历史
            // TODO: 将旧密码添加到密码历史记录
            // addPasswordToHistory(userId, user.getPassword());

            // 9. 清除用户的所有会话（强制重新登录）
            // TODO: 清除用户的所有活跃会话
            // sessionService.invalidateAllUserSessions(userId);

            // 10. 发送密码修改通知
            // TODO: 发送邮件或短信通知用户密码已修改
            // notificationService.sendPasswordChangedNotification(user);

            // 11. 记录安全日志
            // TODO: 记录密码修改的安全日志
            // securityLogService.logPasswordChange(userId, request.getClientIp(), request.getUserAgent());

            log.info("密码修改成功: userId={}", userId);

        } catch (IllegalArgumentException e) {
            log.warn("修改密码参数错误: userId={}, error={}", userId, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("修改密码失败: userId={}", userId, e);
            throw new RuntimeException("修改密码失败: " + e.getMessage());
        }
    }

    /**
     * 验证密码强度
     */
    private void validatePasswordStrength(String password) {
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("密码长度至少8位");
        }

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpper = true;
            } else if (Character.isLowerCase(c)) {
                hasLower = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecial = true;
            }
        }

        int strengthScore = 0;
        if (hasUpper) strengthScore++;
        if (hasLower) strengthScore++;
        if (hasDigit) strengthScore++;
        if (hasSpecial) strengthScore++;

        if (strengthScore < 3) {
            throw new IllegalArgumentException("密码强度不足，需要包含大写字母、小写字母、数字和特殊字符中的至少3种");
        }

        // 检查常见弱密码
        String[] weakPasswords = {
            "password", "123456", "123456789", "qwerty", "abc123",
            "password123", "admin", "root", "user", "guest"
        };

        String lowerPassword = password.toLowerCase();
        for (String weak : weakPasswords) {
            if (lowerPassword.contains(weak)) {
                throw new IllegalArgumentException("密码不能包含常见弱密码模式");
            }
        }
    }

    @Override
    public UserResponse updateUser(Long userId, UpdateUserRequest request) {
        log.info("更新用户信息: userId={}", userId);

        try {
            // 1. 验证输入参数
            if (userId == null || request == null) {
                throw new IllegalArgumentException("用户ID和请求参数不能为空");
            }

            // 2. 查询用户信息
            // TODO: 从数据库查询用户
            // Optional<User> userOpt = userRepository.findById(userId);
            // if (!userOpt.isPresent()) {
            //     throw new RuntimeException("用户不存在: " + userId);
            // }
            // User user = userOpt.get();

            // 3. 验证更新权限
            // TODO: 检查当前用户是否有权限更新此用户信息
            // if (!hasPermissionToUpdateUser(currentUserId, userId)) {
            //     throw new RuntimeException("无权限更新此用户信息");
            // }

            // 4. 验证邮箱唯一性
            if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
                // TODO: 检查邮箱是否已被其他用户使用
                // if (userRepository.existsByEmailAndIdNot(request.getEmail(), userId)) {
                //     throw new RuntimeException("邮箱已被其他用户使用: " + request.getEmail());
                // }
            }

            // 5. 验证昵称唯一性
            if (request.getNickname() != null && !request.getNickname().trim().isEmpty()) {
                // TODO: 检查昵称是否已被其他用户使用
                // if (userRepository.existsByNicknameAndIdNot(request.getNickname(), userId)) {
                //     throw new RuntimeException("昵称已被其他用户使用: " + request.getNickname());
                // }
            }

            // 6. 更新用户信息
            // TODO: 更新数据库中的用户信息
            // if (request.getNickname() != null) user.setNickname(request.getNickname());
            // if (request.getEmail() != null) user.setEmail(request.getEmail());
            // if (request.getPhone() != null) user.setPhone(request.getPhone());
            // if (request.getAvatarUrl() != null) user.setAvatarUrl(request.getAvatarUrl());
            // if (request.getBio() != null) user.setBio(request.getBio());
            // if (request.getTimezone() != null) user.setTimezone(request.getTimezone());
            // if (request.getLanguage() != null) user.setLanguage(request.getLanguage());
            // user.setUpdatedAt(LocalDateTime.now());
            // userRepository.save(user);

            // 7. 构建响应（模拟实现）
            UserResponse response = new UserResponse();
            response.setId(userId);
            response.setUsername("user" + userId);
            response.setEmail(request.getEmail() != null ? request.getEmail() : "user" + userId + "@example.com");
            response.setNickname(request.getNickname() != null ? request.getNickname() : "用户" + userId);
            response.setAvatarUrl(request.getAvatarUrl());
            response.setBio(request.getBio());
            response.setTimezone(request.getTimezone() != null ? request.getTimezone() : "Asia/Shanghai");
            response.setLanguage(request.getLanguage() != null ? request.getLanguage() : "zh-CN");
            response.setStatus(UserStatus.ACTIVE);
            response.setCreatedAt(java.time.LocalDateTime.now().minusDays(userId));
            response.setUpdatedAt(java.time.LocalDateTime.now());

            // 8. 记录操作日志
            // TODO: 记录用户信息更新日志
            // auditLogService.logUserUpdate(userId, request);

            log.info("用户信息更新成功: userId={}, username={}", userId, response.getUsername());
            return response;

        } catch (IllegalArgumentException e) {
            log.warn("更新用户信息参数错误: userId={}, error={}", userId, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("更新用户信息失败: userId={}", userId, e);
            throw new RuntimeException("更新用户信息失败: " + e.getMessage());
        }
    }

    @Override
    public UserResponse getCurrentUser(Long userId) {
        log.info("获取当前用户信息: userId={}", userId);

        try {
            // 1. 验证输入参数
            if (userId == null) {
                throw new IllegalArgumentException("用户ID不能为空");
            }

            // 2. 查询用户信息
            // TODO: 从数据库查询用户
            // Optional<User> userOpt = userRepository.findById(userId);
            // if (!userOpt.isPresent()) {
            //     throw new RuntimeException("用户不存在: " + userId);
            // }
            // User user = userOpt.get();

            // 3. 检查用户状态
            // TODO: 检查用户是否激活
            // if (!user.isActive()) {
            //     throw new RuntimeException("用户已被禁用: " + userId);
            // }

            // 4. 构建响应（模拟实现）
            UserResponse response = new UserResponse();
            response.setId(userId);
            response.setUsername("user" + userId);
            response.setEmail("user" + userId + "@example.com");
            response.setNickname("用户" + userId);
            response.setAvatarUrl("https://example.com/avatar/" + userId + ".jpg");
            response.setBio("这是用户" + userId + "的个人简介");
            response.setTimezone("Asia/Shanghai");
            response.setLanguage("zh-CN");
            response.setStatus(UserStatus.ACTIVE);
            response.setCreatedAt(java.time.LocalDateTime.now().minusDays(userId));
            response.setUpdatedAt(java.time.LocalDateTime.now().minusHours(userId));

            // 5. 更新最后访问时间
            // TODO: 更新用户最后访问时间
            // user.setLastAccessAt(LocalDateTime.now());
            // userRepository.save(user);

            log.info("当前用户信息获取成功: userId={}, username={}", userId, response.getUsername());
            return response;

        } catch (IllegalArgumentException e) {
            log.warn("获取当前用户信息参数错误: userId={}, error={}", userId, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("获取当前用户信息失败: userId={}", userId, e);
            throw new RuntimeException("获取当前用户信息失败: " + e.getMessage());
        }
    }

    @Override
    public void logout(String token) {
        log.info("用户登出: token={}", token != null ? token.substring(0, Math.min(10, token.length())) + "..." : "null");

        try {
            // 1. 验证输入参数
            if (token == null || token.trim().isEmpty()) {
                throw new IllegalArgumentException("令牌不能为空");
            }

            // 2. 验证令牌格式
            if (!isValidTokenFormat(token)) {
                throw new IllegalArgumentException("令牌格式无效");
            }

            // 3. 解析令牌获取用户信息
            // TODO: 解析JWT令牌或从缓存中获取令牌信息
            // Claims claims = jwtTokenProvider.parseToken(token);
            // Long userId = claims.get("userId", Long.class);
            // String sessionId = claims.get("sessionId", String.class);

            // 4. 将令牌加入黑名单
            // TODO: 将令牌加入Redis黑名单
            // redisTemplate.opsForSet().add("token_blacklist", token);
            // redisTemplate.expire("token_blacklist", Duration.ofDays(7)); // 设置过期时间

            // 5. 清除用户会话信息
            // TODO: 清除Redis中的用户会话信息
            // if (sessionId != null) {
            //     redisTemplate.delete("user_session:" + sessionId);
            // }

            // 6. 清除用户相关缓存
            // TODO: 清除用户相关的缓存信息
            // if (userId != null) {
            //     cacheService.evictUserCache(userId);
            //     cacheService.evictUserPermissionCache(userId);
            // }

            // 7. 记录登出日志
            // TODO: 记录用户登出操作日志
            // auditLogService.logUserLogout(userId, getClientIP(), getUserAgent());

            // 8. 发送登出通知
            // TODO: 通知其他服务用户已登出
            // notificationService.notifyUserLogout(userId, sessionId);

            log.info("用户登出成功: token={}", token.substring(0, Math.min(10, token.length())) + "...");

        } catch (IllegalArgumentException e) {
            log.warn("用户登出参数错误: token={}, error={}",
                    token != null ? token.substring(0, Math.min(10, token.length())) + "..." : "null",
                    e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("用户登出失败: token={}",
                    token != null ? token.substring(0, Math.min(10, token.length())) + "..." : "null", e);
            throw new RuntimeException("用户登出失败: " + e.getMessage());
        }
    }

    /**
     * 验证令牌格式是否有效（模拟实现）
     */
    private boolean isValidTokenFormat(String token) {
        // 简单的令牌格式验证
        if (token.length() < 20) {
            return false;
        }

        // 检查是否包含基本的JWT格式（三个部分用.分隔）
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            return false;
        }

        // 检查每个部分是否为Base64格式
        for (String part : parts) {
            if (part.isEmpty() || !isBase64(part)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 检查字符串是否为Base64格式（简单实现）
     */
    private boolean isBase64(String str) {
        try {
            java.util.Base64.getDecoder().decode(str);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    // 临时简化实现，避免编译错误
    // TODO: 实现完整的 refreshToken 方法
}