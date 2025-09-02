package com.coze.studio.service.impl;

import com.coze.studio.dto.common.PageResponse;
import com.coze.studio.dto.knowledgebase.CreateKnowledgeBaseRequest;
import com.coze.studio.dto.knowledgebase.UpdateKnowledgeBaseRequest;
import com.coze.studio.entity.KnowledgeBase;
import com.coze.studio.entity.enums.KnowledgeStatus;
import com.coze.studio.service.KnowledgeBaseService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * KnowledgeBaseService 最小可运行实现（桩）
 */
@Service
public class KnowledgeBaseServiceImpl implements KnowledgeBaseService {
    @Override
    public KnowledgeBase createKnowledgeBase(Long userId, CreateKnowledgeBaseRequest request) {
        KnowledgeBase kb = new KnowledgeBase();
        kb.setId(System.currentTimeMillis());
        kb.setName(request != null ? request.getName() : "知识库");
        kb.setDescription(request != null ? request.getDescription() : "");
        kb.setCreatorId(userId);
        kb.setStatus(KnowledgeStatus.ENABLE);
        kb.setCreatedAt(LocalDateTime.now());
        kb.setUpdatedAt(LocalDateTime.now());
        return kb;
    }

    @Override
    public PageResponse<KnowledgeBase> getKnowledgeBases(Long userId, String keyword, String status, Pageable pageable) {
        return PageResponse.of(new ArrayList<>());
    }

    @Override
    public KnowledgeBase getKnowledgeBaseById(Long id, Long userId) {
        KnowledgeBase kb = new KnowledgeBase();
        kb.setId(id);
        kb.setName("知识库-" + id);
        kb.setCreatorId(userId);
        kb.setStatus(KnowledgeStatus.ENABLE);
        kb.setCreatedAt(LocalDateTime.now());
        kb.setUpdatedAt(LocalDateTime.now());
        return kb;
    }

    @Override
    public KnowledgeBase updateKnowledgeBase(Long id, Long userId, UpdateKnowledgeBaseRequest request) {
        KnowledgeBase kb = new KnowledgeBase();
        kb.setId(id);
        kb.setName(request != null && request.getName() != null ? request.getName() : ("知识库-" + id));
        kb.setDescription(request != null ? request.getDescription() : null);
        kb.setStatus(KnowledgeStatus.ENABLE);
        kb.setUpdatedAt(LocalDateTime.now());
        return kb;
    }

    @Override
    public void deleteKnowledgeBase(Long id, Long userId) {
        // 最小实现：占位
    }

    @Override
    public Map<String, Object> getKnowledgeBaseStats(Long id, Long userId) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("knowledgeBaseId", id);
        stats.put("documentCount", 0);
        stats.put("totalCharCount", 0);
        return stats;
    }
}

