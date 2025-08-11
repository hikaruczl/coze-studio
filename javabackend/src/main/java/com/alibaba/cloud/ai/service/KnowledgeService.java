package com.alibaba.cloud.ai.service;

import com.alibaba.cloud.ai.dto.CreateKnowledgeBaseRequest;
import com.alibaba.cloud.ai.model.KnowledgeBase;
import com.alibaba.cloud.ai.model.KnowledgeStatus;
import com.alibaba.cloud.ai.repository.KnowledgeBaseRepository;
import org.springframework.ai.alibaba.dashscope.DashscopeAiEmbeddingModel;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.document.FileSystemDocumentReader;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class KnowledgeService {

    private final KnowledgeBaseRepository knowledgeBaseRepository;
    private final VectorStore vectorStore;
    private final DashscopeAiEmbeddingModel embeddingModel;

    @Autowired
    public KnowledgeService(KnowledgeBaseRepository knowledgeBaseRepository, VectorStore vectorStore, DashscopeAiEmbeddingModel embeddingModel) {
        this.knowledgeBaseRepository = knowledgeBaseRepository;
        this.vectorStore = vectorStore;
        this.embeddingModel = embeddingModel;
    }

    public KnowledgeBase createKnowledgeBase(CreateKnowledgeBaseRequest request) {
        KnowledgeBase knowledgeBase = new KnowledgeBase();
        knowledgeBase.setName(request.getName());
        knowledgeBase.setDescription(request.getDescription());
        knowledgeBase.setStatus(KnowledgeStatus.ENABLE); // Default status
        knowledgeBase.setCreatedAt(System.currentTimeMillis());
        knowledgeBase.setUpdatedAt(System.currentTimeMillis());

        return knowledgeBaseRepository.save(knowledgeBase);
    }

    public void addDocument(MultipartFile file, Long knowledgeBaseId) {
        try {
            // 1. Save the uploaded file to a temporary location
            Path tempDir = Files.createTempDirectory("coze-uploads-");
            File tempFile = tempDir.resolve(file.getOriginalFilename()).toFile();
            file.transferTo(tempFile);

            // 2. Use a DocumentReader to parse the file
            DocumentReader documentReader = new FileSystemDocumentReader(tempFile.getParentFile().toPath());
            List<org.springframework.ai.document.Document> documents = documentReader.get();

            // 3. Add documents to the Vector Store
            // TODO: This requires a running OpenSearch instance and proper configuration.
            // vectorStore.add(documents);

            // 4. Clean up the temporary file
            tempFile.delete();
            tempDir.toFile().delete();

        } catch (IOException e) {
            // Handle IO exception
            throw new RuntimeException("Failed to process uploaded file", e);
        }
    }
}
