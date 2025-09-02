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

import com.coze.studio.entity.User;
import com.coze.studio.entity.enums.UserRole;
import com.coze.studio.entity.enums.UserStatus;
import com.coze.studio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 数据库初始化服务
 * 在应用启动时创建默认数据
 * 
 * @author coze-dev
 */
@Slf4j
@Service
@ConditionalOnProperty(name = "app.init-db", havingValue = "true", matchIfMissing = false)
@RequiredArgsConstructor
public class DatabaseInitService implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        initDefaultUsers();
    }

    /**
     * 初始化默认用户
     */
    private void initDefaultUsers() {
        // 检查是否已存在管理员用户
        if (userRepository.findByRole(UserRole.SUPER_ADMIN).isEmpty()) {
            createDefaultSuperAdmin();
        }

        if (userRepository.findByRole(UserRole.ADMIN).isEmpty()) {
            createDefaultAdmin();
        }

        log.info("数据库初始化完成");
    }

    /**
     * 创建默认超级管理员
     */
    private void createDefaultSuperAdmin() {
        User superAdmin = new User();
        superAdmin.setUsername("superadmin");
        superAdmin.setEmail("superadmin@coze.studio");
        superAdmin.setPassword(passwordEncoder.encode("Admin123!"));
        superAdmin.setNickname("超级管理员");
        superAdmin.setRole(UserRole.SUPER_ADMIN);
        superAdmin.setStatus(UserStatus.ACTIVE);
        superAdmin.setEmailVerifiedAt(LocalDateTime.now());
        superAdmin.setTermsAccepted(true);
        superAdmin.setTermsAcceptedAt(LocalDateTime.now());

        userRepository.save(superAdmin);
        log.info("默认超级管理员创建成功: username=superadmin, password=Admin123!");
    }

    /**
     * 创建默认管理员
     */
    private void createDefaultAdmin() {
        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@coze.studio");
        admin.setPassword(passwordEncoder.encode("Admin123!"));
        admin.setNickname("管理员");
        admin.setRole(UserRole.ADMIN);
        admin.setStatus(UserStatus.ACTIVE);
        admin.setEmailVerifiedAt(LocalDateTime.now());
        admin.setTermsAccepted(true);
        admin.setTermsAcceptedAt(LocalDateTime.now());

        userRepository.save(admin);
        log.info("默认管理员创建成功: username=admin, password=Admin123!");
    }
}
