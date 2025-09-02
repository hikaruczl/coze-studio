package com.coze.studio.service.impl;

import com.coze.studio.dto.common.PageResponse;
import com.coze.studio.dto.document.UpdateDocumentRequest;
import com.coze.studio.entity.Document;
import com.coze.studio.entity.enums.DocumentStatus;
import com.coze.studio.service.DocumentService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DocumentService 最小可运行实现（桩）
 * 目的：提供可被注入的 Spring Bean 以解除启动依赖
 */
@Service
public class DocumentServiceImpl implements DocumentService {

    @Override
    public Document uploadDocument(MultipartFile file, Long knowledgeBaseId, Long userId, String title, String description) {
        // 最小实现：构造一个文档对象并返回
        Document doc = new Document();
        doc.setId(System.currentTimeMillis());
        doc.setName(title != null ? title : (file != null ? file.getOriginalFilename() : "document"));
        doc.setStatus(DocumentStatus.PROCESSING);
        doc.setSize(file != null ? file.getSize() : 0L);
        doc.setCreatedAt(LocalDateTime.now());
        doc.setUpdatedAt(LocalDateTime.now());
        return doc;
    }

    @Override
    public List<Document> batchUploadDocuments(MultipartFile[] files, Long knowledgeBaseId, Long userId) {
        List<Document> list = new ArrayList<>();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                MultipartFile f = files[i];
                Document d = new Document();
                d.setId(System.currentTimeMillis() + i);
                d.setName(f != null ? f.getOriginalFilename() : ("document_" + i));
                d.setStatus(DocumentStatus.PROCESSING);
                d.setSize(f != null ? f.getSize() : 0L);
                d.setCreatedAt(LocalDateTime.now());
                d.setUpdatedAt(LocalDateTime.now());
                list.add(d);
            }
        }
        return list;
    }

    @Override
    public PageResponse<Document> getDocuments(Long knowledgeBaseId, Long userId, String keyword, String status, String fileType, Pageable pageable) {
        // 最小实现：返回空列表的分页响应
        return PageResponse.of(new ArrayList<>());
    }

    @Override
    public Document getDocumentById(Long id, Long userId) {
        // 最小实现：返回一个占位文档
        Document d = new Document();
        d.setId(id);
        d.setName("document-" + id);
        d.setStatus(DocumentStatus.COMPLETED);
        d.setCreatedAt(LocalDateTime.now());
        d.setUpdatedAt(LocalDateTime.now());
        return d;
    }

    @Override
    public Document updateDocument(Long id, Long userId, UpdateDocumentRequest request) {
        // 最小实现：根据请求更新部分字段并返回
        Document d = new Document();
        d.setId(id);
        if (request != null && request.getTitle() != null) {
            d.setName(request.getTitle());
        } else {
            d.setName("document-" + id);
        }
        d.setStatus(DocumentStatus.COMPLETED);
        d.setUpdatedAt(LocalDateTime.now());
        return d;
    }

    @Override
    public void deleteDocument(Long id, Long userId) {
        // 最小实现：不做持久化删除，仅占位
    }

    @Override
    public PageResponse<Map<String, Object>> getDocumentSlices(Long id, Long userId, Pageable pageable) {
        // 最小实现：返回空切片
        return PageResponse.of(new ArrayList<>());
    }

    @Override
    public Map<String, Object> getDocumentStats(Long id, Long userId) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("documentId", id);
        stats.put("sliceCount", 0);
        stats.put("charCount", 0);
        stats.put("status", "COMPLETED");
        return stats;
    }
}

