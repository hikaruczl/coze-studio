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

import com.coze.studio.entity.DocumentSlice;

import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 文档切片数据访问接口
 * 
 * @author coze-dev
 */
@Mapper
public interface DocumentSliceRepository {

    /**
     * 根据文档ID查询切片
     */
    List<DocumentSlice> findByDocumentIdAndEnabledTrueOrderByPosition(Long documentId);

    /**
     * 根据知识库ID查询切片
     */

    /**
     * 根据处理状态查询切片
     */
    List<DocumentSlice> findByProcessingStatusAndEnabledTrueOrderByCreatedAtAsc(String processingStatus);

    /**
     * 统计文档的切片数量
     */
    long countByDocumentIdAndEnabledTrue(Long documentId);

    /**
     * 统计知识库的切片数量
     */
    long countByKnowledgeBaseIdAndEnabledTrue(Long knowledgeBaseId);

    /**
     * 根据向量ID查询切片
     */
    Optional<DocumentSlice> findByVectorIdAndEnabledTrue(String vectorId);

    /**
     * 查询待向量化的切片
     */
        /**
     * 查询已向量化的切片
     */
        /**
     * 根据嵌入模型查询切片
     */

    /**
     * 查询指定时间范围内的切片
     */

    /**
     * 查询大于指定字符数的切片
     */
        /**
     * 查询包含关键词的切片
     */
        /**
     * 更新切片的向量信息
     */
        /**
     * 更新切片处理状态
     */
            /**
     * 批量更新切片状态
     */
            /**
     * 删除文档的所有切片
     */
    void deleteByDocumentId(Long documentId);

    /**
     * 删除知识库的所有切片
     */
    void deleteByKnowledgeBaseId(Long knowledgeBaseId);

    /**
     * 物理删除已禁用的切片
     */
    void deleteByEnabledFalse();

    /**
     * 查询切片统计信息
     */

    /**
     * 查询文档的切片统计
     */

    /**
     * 查询需要重新向量化的切片（模型变更时）
     */

    /**
     * 查询向量化失败的切片
     */

    // 基础CRUD方法
    DocumentSlice save(DocumentSlice entity);
    
    Optional<DocumentSlice> findById(Long id);
    
    List<DocumentSlice> findAll();
    
    boolean existsById(Long id);
    
    long count();
    
    void deleteById(Long id);

}