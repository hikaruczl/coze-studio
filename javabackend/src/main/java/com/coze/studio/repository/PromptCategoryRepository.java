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

import com.coze.studio.entity.PromptCategory;
import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * 提示词分类数据访问接口
 * 
 * @author coze-dev
 */
@Mapper
public interface PromptCategoryRepository {

    /**
     * 根据代码查找分类
     */
    Optional<PromptCategory> findByCode(String code);

    /**
     * 根据名称查找分类
     */
    Optional<PromptCategory> findByName(String name);

    /**
     * 查找所有启用的分类
     */
    List<PromptCategory> findByEnabledTrueOrderBySortOrderAscCreatedAtAsc();

    /**
     * 查找根分类
     */
    List<PromptCategory> findByParentIdIsNullAndEnabledTrueOrderBySortOrderAscCreatedAtAsc();

    /**
     * 根据父分类ID查找子分类
     */
    List<PromptCategory> findByParentIdAndEnabledTrueOrderBySortOrderAscCreatedAtAsc(Long parentId);

    /**
     * 根据层级查找分类
     */
    List<PromptCategory> findByLevelAndEnabledTrueOrderBySortOrderAscCreatedAtAsc(Integer level);

    /**
     * 查找系统分类
     */
    List<PromptCategory> findByIsSystemTrueAndEnabledTrueOrderBySortOrderAscCreatedAtAsc();

    /**
     * 根据路径查找分类
     */
    Optional<PromptCategory> findByPath(String path);

    /**
     * 根据路径前缀查找分类
     */
    List<PromptCategory> findByPathStartingWithAndEnabledTrueOrderBySortOrderAscCreatedAtAsc(String pathPrefix);

    /**
     * 统计分类数量
     */
    long countByEnabledTrue();

    /**
     * 统计子分类数量
     */
    long countByParentIdAndEnabledTrue(Long parentId);

    /**
     * 统计层级分类数量
     */
    long countByLevelAndEnabledTrue(Integer level);

    /**
     * 更新模板数量
     */
            void updateTemplateCount(@Param("id") Long id, @Param("increment") int increment);

    /**
     * 批量更新模板数量
     */
            void batchUpdateTemplateCount(@Param("codes") List<String> codes, @Param("increment") int increment);

    /**
     * 重新计算模板数量
     */
            void recalculateTemplateCount(@Param("id") Long id);

    /**
     * 批量重新计算模板数量
     */
    void recalculateAllTemplateCounts();

    /**
     * 检查代码是否存在
     */
    boolean existsByCode(String code);

    /**
     * 检查名称是否存在
     */
    boolean existsByName(String name);

    /**
     * 检查是否有子分类
     */
    boolean existsByParentIdAndEnabledTrue(Long parentId);

    /**
     * 获取分类树结构
     */
        List<PromptCategory> findCategoryTree();

    /**
     * 获取分类路径上的所有分类
     */
    List<PromptCategory> findCategoriesInPath(@Param("path") String path);

    /**
     * 查找叶子分类（有模板的分类）
     */
        List<PromptCategory> findLeafCategories();

    /**
     * 查找空分类（没有模板的分类）
     */
        List<PromptCategory> findEmptyCategories();

    /**
     * 根据名称模糊查找分类
     */
        List<PromptCategory> searchByKeyword(@Param("keyword") String keyword);

    /**
     * 获取热门分类（按模板数量排序）
     */
        List<PromptCategory> findPopularCategories();

    /**
     * 获取分类统计信息
     */
        List<Object[]> getCategoryStatistics();

    /**
     * 获取最大排序号
     */
    Integer getMaxSortOrder(@Param("parentId") Long parentId);

    /**
     * 获取最大排序号（根分类）
     */
    Integer getMaxRootSortOrder();
}
