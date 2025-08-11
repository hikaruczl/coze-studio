package com.alibaba.cloud.ai.controller;

import com.alibaba.cloud.ai.service.PluginExecutionService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/plugin/execute")
public class PluginExecutionController {

    @Autowired
    private PluginExecutionService pluginExecutionService;

    @PostMapping
    public ResponseEntity<?> executePluginTool(@RequestBody ExecutionRequest request) {
        try {
            String result = pluginExecutionService.executeTool(
                request.getPluginId(),
                request.getOperationId(),
                request.getParameters()
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Data
    static class ExecutionRequest {
        private Long pluginId;
        private String operationId;
        private Map<String, Object> parameters;
    }
}
