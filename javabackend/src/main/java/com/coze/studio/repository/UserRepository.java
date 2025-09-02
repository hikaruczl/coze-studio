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

package com.coze.studio.repository;

import com.coze.studio.entity.User;
import com.coze.studio.entity.enums.UserRole;
import com.coze.studio.entity.enums.UserStatus;

import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * User 数据访问层
 * 
 * @author coze-dev
 */
@Mapper
public interface UserRepository {

    /**
     * 根据用户名查找用户
     */
    Optional<User> findByUsername(String username);

    /**
     * 根据邮箱查找用户
     */
    Optional<User> findByEmail(String email);

    /**
     * 根据手机号查找用户
     */
    Optional<User> findByPhone(String phone);

    /**
     * 根据用户名或邮箱查找用户
     */
        Optional<User> findByUsernameOrEmail(@Param("usernameOrEmail") String usernameOrEmail);

    /**
     * 检查用户名是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 检查邮箱是否存在
     */
    boolean existsByEmail(String email);

    /**
     * 检查手机号是否存在
     */
    boolean existsByPhone(String phone);

    /**
     * 根据角色查找用户列表
     */
    List<User> findByRole(UserRole role);

    /**
     * 根据状态查找用户列表
     */
    // Page<User> findByStatus(UserStatus status, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 根据角色和状态查找用户列表
     */
    List<User> findByRoleAndStatus(UserRole role, UserStatus status);

    /**
     * 查找活跃用户
     */
        // List<User> findActiveUsers(Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 根据昵称模糊查询用户
     */
    // Page<User> findByNicknameContainingIgnoreCase(String nickname, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 查找需要解锁的用户（锁定时间已过）
     */
        List<User> findUsersToUnlock(@Param("now") LocalDateTime now);

    /**
     * 查找未验证邮箱的用户
     */
        List<User> findUnverifiedUsers();

    /**
     * 查找长时间未登录的用户
     */
        List<User> findInactiveUsers(@Param("cutoffTime") LocalDateTime cutoffTime);

    /**
     * 统计各状态用户数量
     */
    List<Object[]> countUsersByStatus();

    /**
     * 统计各角色用户数量
     */
    List<Object[]> countUsersByRole();

    /**
     * 更新用户最后登录信息
     */
            void updateLastLoginInfo(@Param("userId") Long userId, @Param("loginTime") LocalDateTime loginTime, @Param("loginIp") String loginIp);

    /**
     * 增加登录失败次数
     */
            void incrementFailedLoginAttempts(@Param("userId") Long userId);

    /**
     * 锁定用户账户
     */
            void lockUser(@Param("userId") Long userId, @Param("lockedUntil") LocalDateTime lockedUntil);

    /**
     * 解锁用户账户
     */
            void unlockUser(@Param("userId") Long userId);

    /**
     * 验证用户邮箱
     */
            void verifyEmail(@Param("userId") Long userId, @Param("verifiedAt") LocalDateTime verifiedAt);

    /**
     * 验证用户手机
     */
            void verifyPhone(@Param("userId") Long userId, @Param("verifiedAt") LocalDateTime verifiedAt);

    // 基础CRUD方法
    User save(User entity);
    
    Optional<User> findById(Long id);
    
    List<User> findAll();
    
    boolean existsById(Long id);
    
    long count();
    
    void deleteById(Long id);

}