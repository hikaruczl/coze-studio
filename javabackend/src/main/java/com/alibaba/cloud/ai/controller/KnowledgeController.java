package com.alibaba.cloud.ai.controller;

import com.alibaba.cloud.ai.dto.CreateKnowledgeBaseRequest;
import com.alibaba.cloud.ai.model.KnowledgeBase;
import com.alibaba.cloud.ai.service.KnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/knowledge")
public class KnowledgeController {

    private final KnowledgeService knowledgeService;

    @Autowired
    public KnowledgeController(KnowledgeService knowledgeService) {
        this.knowledgeService = knowledgeService;
    }

    @PostMapping("/create")
    public KnowledgeBase createKnowledgeBase(@RequestBody CreateKnowledgeBaseRequest request) {
        return knowledgeService.createKnowledgeBase(request);
    }

    @PostMapping("/document/create")
    public ResponseEntity<String> createDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("knowledge_base_id") Long knowledgeBaseId) {
        try {
            knowledgeService.addDocument(file, knowledgeBaseId);
            return ResponseEntity.ok("Document added successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to add document: " + e.getMessage());
        }
    }
}
