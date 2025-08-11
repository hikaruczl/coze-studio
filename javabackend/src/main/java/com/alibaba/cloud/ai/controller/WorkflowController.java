package com.alibaba.cloud.ai.controller;

import com.alibaba.cloud.ai.dto.SaveWorkflowRequest;
import com.alibaba.cloud.ai.dto.TestRunWorkflowRequest;
import com.alibaba.cloud.ai.model.Workflow;
import com.alibaba.cloud.ai.service.WorkflowService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/workflow")
public class WorkflowController {

    private final WorkflowService workflowService;

    @Autowired
    public WorkflowController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @PostMapping("/save")
    public Workflow saveWorkflow(@RequestBody SaveWorkflowRequest request) {
        return workflowService.saveWorkflow(request);
    }

    @PostMapping("/test_run")
    public ResponseEntity<?> testRunWorkflow(@RequestBody TestRunWorkflowRequest request) {
        try {
            Map<String, JsonNode> result = workflowService.runWorkflow(request.getWorkflowId(), request.getInputs());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
}
