package com.alibaba.cloud.ai.service;

import com.alibaba.cloud.ai.dto.SaveWorkflowRequest;
import com.alibaba.cloud.ai.model.Workflow;
import com.alibaba.cloud.ai.repository.WorkflowRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class WorkflowService {

    private final WorkflowRepository workflowRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public WorkflowService(WorkflowRepository workflowRepository, ObjectMapper objectMapper) {
        this.workflowRepository = workflowRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public Workflow saveWorkflow(SaveWorkflowRequest request) {
        Workflow workflow;
        if (request.getId() != null) {
            workflow = workflowRepository.findById(request.getId())
                    .orElseThrow(() -> new RuntimeException("Workflow not found with id: " + request.getId()));
        } else {
            workflow = new Workflow();
            workflow.setCreatedAt(LocalDateTime.now());
        }

        workflow.setName(request.getName());
        workflow.setDescription(request.getDescription());
        workflow.setUpdatedAt(LocalDateTime.now());

        try {
            workflow.setCanvas(objectMapper.writeValueAsString(request.getCanvas()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize canvas JSON", e);
        }

        return workflowRepository.save(workflow);
    }
}
