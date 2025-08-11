package com.alibaba.cloud.ai.controller;

import com.alibaba.cloud.ai.dto.DebugApiRequest;
import com.alibaba.cloud.ai.dto.DebugApiResponse;
import com.alibaba.cloud.ai.dto.PluginInfoResponse;
import com.alibaba.cloud.ai.dto.RegisterPluginRequest;
import com.alibaba.cloud.ai.model.Plugin;
import com.alibaba.cloud.ai.service.PluginExecutionService;
import com.alibaba.cloud.ai.service.PluginService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Description;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/plugin_api")
public class PluginController {

    private final ApplicationContext applicationContext;
    private final PluginService pluginService;
    private final PluginExecutionService pluginExecutionService;
    private final ObjectMapper objectMapper;

    @Autowired
    public PluginController(ApplicationContext applicationContext, PluginService pluginService, PluginExecutionService pluginExecutionService, ObjectMapper objectMapper) {
        this.applicationContext = applicationContext;
        this.pluginService = pluginService;
        this.pluginExecutionService = pluginExecutionService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/list_beans") // Renamed to avoid conflict
    public List<Map<String, String>> listPluginBeans() {
        String[] functionBeanNames = applicationContext.getBeanNamesForType(Function.class);
        return Arrays.stream(functionBeanNames)
                .map(name -> {
                    Description description = applicationContext.findAnnotationOnBean(name, Description.class);
                    return Map.of(
                            "name", name,
                            "description", description != null ? description.value() : "No description"
                    );
                })
                .collect(Collectors.toList());
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerPlugin(@RequestBody RegisterPluginRequest request) {
        try {
            Plugin plugin = pluginService.registerPlugin(request);
            return ResponseEntity.ok(new PluginInfoResponse(plugin));
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("Invalid ai_plugin JSON format.");
        }
    }

    @PostMapping("/get_plugin_info")
    public ResponseEntity<?> getPluginInfo(@RequestBody PluginIdRequest request) {
        Optional<Plugin> plugin = pluginService.getPluginInfo(request.getPluginId());
        return plugin.map(p -> ResponseEntity.ok(new PluginInfoResponse(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/debug_api")
    public ResponseEntity<DebugApiResponse> debugApi(@RequestBody DebugApiRequest request) {
        try {
            String rawRequest = objectMapper.writeValueAsString(request.getParameters());
            String result = pluginExecutionService.executeTool(
                    request.getPluginId(),
                    request.getOperationId(),
                    request.getParameters()
            );
            return ResponseEntity.ok(DebugApiResponse.builder()
                    .success(true)
                    .resp(result)
                    .rawResp(result)
                    .rawReq(rawRequest)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.ok(DebugApiResponse.builder()
                    .success(false)
                    .reason(e.getMessage())
                    .build());
        }
    }

    @Data
    static class PluginIdRequest {
        @JsonProperty("plugin_id")
        private Long pluginId;
    }
}
