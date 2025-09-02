package com.coze.studio.service;

import com.coze.studio.dto.common.PageResponse;
import com.coze.studio.dto.document.UpdateDocumentRequest;
import com.coze.studio.entity.Document;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 文档服务接口
 * 
 * @author coze-dev
 */
public interface DocumentService {

    /**
     * 上传文档
     */
    Document uploadDocument(MultipartFile file, Long knowledgeBaseId, Long userId, String title, String description);

    /**
     * 批量上传文档
     */
    List<Document> batchUploadDocuments(MultipartFile[] files, Long knowledgeBaseId, Long userId);

    /**
     * 获取文档列表
     */
    PageResponse<Document> getDocuments(Long knowledgeBaseId, Long userId, String keyword, String status, String fileType, Pageable pageable);

    /**
     * 根据ID获取文档
     */
    Document getDocumentById(Long id, Long userId);

    /**
     * 更新文档
     */
    Document updateDocument(Long id, Long userId, UpdateDocumentRequest request);

    /**
     * 删除文档
     */
    void deleteDocument(Long id, Long userId);

    /**
     * 获取文档切片
     */
    PageResponse<Map<String, Object>> getDocumentSlices(Long id, Long userId, Pageable pageable);

    /**
     * 获取文档统计信息
     */
    Map<String, Object> getDocumentStats(Long id, Long userId);
}
