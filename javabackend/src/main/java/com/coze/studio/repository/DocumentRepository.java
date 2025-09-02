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

import java.util.List;
import java.util.Optional;

import com.coze.studio.entity.Document;
import com.coze.studio.entity.enums.DocumentStatus;
import com.coze.studio.entity.enums.DocumentType;

import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Document 数据访问层
 * 
 * @author coze-dev
 */
@Mapper
public interface DocumentRepository {

    /**
     * 根据知识库ID查找文档列表
     */
    List<Document> findByKnowledgeBaseIdAndEnabledTrue(Long knowledgeBaseId);

    /**
     * 根据知识库ID分页查找文档列表
     */
    // Page<Document> findByKnowledgeBaseIdAndEnabledTrue(Long knowledgeBaseId, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 根据文档类型查找文档列表
     */
    List<Document> findByTypeAndEnabledTrue(DocumentType type);

    /**
     * 根据文档状态查找文档列表
     */
    List<Document> findByStatusAndEnabledTrue(DocumentStatus status);

    /**
     * 根据知识库ID和状态查找文档列表
     */
    List<Document> findByKnowledgeBaseIdAndStatusAndEnabledTrue(Long knowledgeBaseId, DocumentStatus status);

    /**
     * 根据名称模糊查询文档
     */
    // Page<Document> findByNameContainingIgnoreCaseAndEnabledTrue(String name, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 根据文件扩展名查找文档
     */
    List<Document> findByFileExtensionAndEnabledTrue(String fileExtension);

    /**
     * 统计知识库的文档数量
     */
    long countByKnowledgeBaseIdAndEnabledTrue(Long knowledgeBaseId);

    /**
     * 统计知识库中已完成处理的文档数量
     */
    long countByKnowledgeBaseIdAndStatusAndEnabledTrue(Long knowledgeBaseId, DocumentStatus status);

    /**
     * 查找处理完成的文档
     */
        List<Document> findCompletedDocuments();

    /**
     * 根据文档大小范围查找文档
     */
        List<Document> findBySizeBetween(@Param("minSize") Long minSize, @Param("maxSize") Long maxSize);

    /**
     * 查找包含特定关键词的文档
     */
    // Page<Document> findByKeyword(@Param("keyword") String keyword, Pageable pageable); // TODO: 需要在MyBatis XML中实现分页

    /**
     * 根据知识库ID和文档类型查找文档
     */
    List<Document> findByKnowledgeBaseIdAndTypeAndEnabledTrue(Long knowledgeBaseId, DocumentType type);

    // 基础CRUD方法
    Document save(Document entity);
    
    Optional<Document> findById(Long id);
    
    List<Document> findAll();
    
    boolean existsById(Long id);
    
    long count();
    
    void deleteById(Long id);

}