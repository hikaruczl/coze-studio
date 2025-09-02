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

import com.coze.studio.entity.PromptTemplate;

import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 提示词模板数据访问接口
 * 
 * @author coze-dev
 */
@Mapper
public interface PromptTemplateRepository {

    /**
     * 根据名称查找模板
     */
    Optional<PromptTemplate> findByNameAndUserId(String name, Long userId);

    /**
     * 根据名称和版本查找模板
     */
    Optional<PromptTemplate> findByNameAndVersionAndUserId(String name, String version, Long userId);

    /**
     * 根据用户ID查找模板
     */
    // TODO: 分页方法需要在MyBatis XML中实现

    /**
     * 根据类型查找模板
     */
    // TODO: 分页方法需要在MyBatis XML中实现

    /**
     * 根据分类查找模板
     */
    // TODO: 分页方法需要在MyBatis XML中实现

    /**
     * 根据状态查找模板
     */
    // TODO: 分页方法需要在MyBatis XML中实现

    /**
     * 查找公开模板
     */
    // TODO: 分页方法需要在MyBatis XML中实现

    /**
     * 查找系统模板
     */
    List<PromptTemplate> findByIsSystemTrueAndStatusOrderByCreatedAtDesc(String status);

    /**
     * 查找默认模板
     */
    List<PromptTemplate> findByIsDefaultTrueAndStatusOrderByCreatedAtDesc(String status);

    /**
     * 根据父模板ID查找所有版本
     */
    List<PromptTemplate> findByRootIdOrderByMajorVersionDescMinorVersionDescPatchVersionDesc(Long rootId);

    /**
     * 根据父模板ID查找最新版本
     */
        /**
     * 根据名称模糊查找模板
     */
    // TODO: 分页方法需要在MyBatis XML中实现

    /**
     * 根据标签查找模板
     */
        // TODO: 分页方法需要在MyBatis XML中实现

    /**
     * 获取热门模板（按使用次数排序）
     */
        // TODO: 分页方法需要在MyBatis XML中实现

    /**
     * 获取最新模板
     */
        // TODO: 分页方法需要在MyBatis XML中实现

    /**
     * 获取推荐模板（按评分排序）
     */
        // TODO: 分页方法需要在MyBatis XML中实现

    /**
     * 根据用户和类型查找模板
     */
    // TODO: 分页方法需要在MyBatis XML中实现

    /**
     * 根据用户和分类查找模板
     */
    // TODO: 分页方法需要在MyBatis XML中实现

    /**
     * 根据用户和状态查找模板
     */
    // TODO: 分页方法需要在MyBatis XML中实现

    /**
     * 统计用户模板数量
     */
    long countByUserId(Long userId);

    /**
     * 统计分类模板数量
     */
    long countByCategory(String category);

    /**
     * 统计类型模板数量
     */
    long countByType(String type);

    /**
     * 统计状态模板数量
     */
    long countByStatus(String status);

    /**
     * 统计公开模板数量
     */
    long countByIsPublicTrue();

    /**
     * 更新使用统计
     */
            /**
     * 更新下载次数
     */
            /**
     * 更新收藏次数
     */
            /**
     * 更新评分
     */
            /**
     * 批量更新状态
     */
            /**
     * 删除用户的所有模板
     */
            /**
     * 获取模板统计信息
     */
        List<Object[]> getTemplateStatistics();

    /**
     * 获取用户模板统计
     */
        /**
     * 获取分类模板统计
     */
        List<Object[]> getCategoryStatistics();

    /**
     * 查找相似模板（基于内容相似度）
     */
        /**
     * 查找用户收藏的模板
     */
        // TODO: 分页方法需要在MyBatis XML中实现

    /**
     * 查找最近使用的模板
     */
        // TODO: 分页方法需要在MyBatis XML中实现

    /**
     * 查找长时间未使用的模板
     */
        /**
     * 查找指定时间范围内创建的模板
     */
        /**
     * 高级搜索
     */
    // TODO: 分页方法需要在MyBatis XML中实现

    /**
     * 检查模板名称是否存在
     */
    boolean existsByNameAndUserId(String name, Long userId);

    /**
     * 检查模板名称和版本是否存在
     */
    boolean existsByNameAndVersionAndUserId(String name, String version, Long userId);

    // 基础CRUD方法
    PromptTemplate save(PromptTemplate entity);

    Optional<PromptTemplate> findById(Long id);

    List<PromptTemplate> findAll();

    boolean existsById(Long id);

    long count();

    void deleteById(Long id);

    /**
     * 根据名称查找模板（不限用户）
     */
    List<PromptTemplate> findByName(String name);
}
