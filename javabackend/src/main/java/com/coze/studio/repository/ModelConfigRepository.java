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

import com.coze.studio.entity.ModelConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 模型配置数据访问层
 *
 * @author coze-dev
 */
@Repository
public interface ModelConfigRepository extends JpaRepository<ModelConfig, Long> {

    /**
     * 根据名称查找模型配置
     */
    Optional<ModelConfig> findByName(String name);

    /**
     * 根据模型ID查找配置
     */
    List<ModelConfig> findByModelIdOrderByCreatedAtDesc(Long modelId);

    /**
     * 根据创建者ID查找配置
     */
    List<ModelConfig> findByCreatorIdOrderByCreatedAtDesc(Long creatorId);

    /**
     * 查找模板配置
     */
    List<ModelConfig> findByIsTemplateTrueOrderByCreatedAtDesc();

    /**
     * 根据分类查找模板配置
     */
    List<ModelConfig> findByIsTemplateTrueAndTemplateCategoryOrderByCreatedAtDesc(Boolean isTemplate, String templateCategory);

    /**
     * 根据状态查找配置
     */
    List<ModelConfig> findByStatusOrderByCreatedAtDesc(Integer status);

    /**
     * 根据名称模糊搜索
     */
    @Query("SELECT m FROM ModelConfig m WHERE m.name LIKE %:name% ORDER BY m.createdAt DESC")
    List<ModelConfig> findByNameContaining(@Param("name") String name);

    /**
     * 根据模板分类查找模板
     */
    @Query("SELECT m FROM ModelConfig m WHERE m.isTemplate = true AND (m.templateCategory = :category OR :category IS NULL) ORDER BY m.createdAt DESC")
    List<ModelConfig> findTemplatesByCategory(@Param("category") String category);

    /**
     * 统计模板数量
     */
    long countByIsTemplateTrue();

    /**
     * 根据分类统计模板数量
     */
    long countByIsTemplateTrueAndTemplateCategory(String templateCategory);

    /**
     * 根据创建者统计配置数量
     */
    long countByCreatorId(Long creatorId);
}
