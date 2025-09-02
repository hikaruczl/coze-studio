package com.coze.studio.service;

import com.coze.studio.dto.common.PageResponse;
import com.coze.studio.dto.knowledgebase.CreateKnowledgeBaseRequest;
import com.coze.studio.dto.knowledgebase.UpdateKnowledgeBaseRequest;
import com.coze.studio.entity.KnowledgeBase;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * 知识库服务接口
 * 
 * @author coze-dev
 */
public interface KnowledgeBaseService {

    /**
     * 创建知识库
     */
    KnowledgeBase createKnowledgeBase(Long userId, CreateKnowledgeBaseRequest request);

    /**
     * 获取知识库列表
     */
    PageResponse<KnowledgeBase> getKnowledgeBases(Long userId, String keyword, String status, Pageable pageable);

    /**
     * 根据ID获取知识库
     */
    KnowledgeBase getKnowledgeBaseById(Long id, Long userId);

    /**
     * 更新知识库
     */
    KnowledgeBase updateKnowledgeBase(Long id, Long userId, UpdateKnowledgeBaseRequest request);

    /**
     * 删除知识库
     */
    void deleteKnowledgeBase(Long id, Long userId);

    /**
     * 获取知识库统计信息
     */
    Map<String, Object> getKnowledgeBaseStats(Long id, Long userId);
}
