package com.example.javabackend.controller;

import com.example.javabackend.controller.dto.PluginInfoResponse;
import com.example.javabackend.controller.dto.RegisterPluginRequest;
import com.example.javabackend.model.Plugin;
import com.example.javabackend.service.PluginService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/plugin")
public class PluginController {

    @Autowired
    private PluginService pluginService;

    @PostMapping("/register")
    public ResponseEntity<?> registerPlugin(@RequestBody RegisterPluginRequest request) {
        try {
            Plugin plugin = pluginService.registerPlugin(request);
            return ResponseEntity.ok(new PluginInfoResponse(plugin));
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("Invalid ai_plugin JSON format.");
        }
    }

    @PostMapping("/info")
    public ResponseEntity<?> getPluginInfo(@RequestBody PluginIdRequest request) {
        Optional<Plugin> plugin = pluginService.getPluginInfo(request.getPluginId());
        return plugin.map(p -> ResponseEntity.ok(new PluginInfoResponse(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Helper DTO for get_plugin_info
    static class PluginIdRequest {
        private Long pluginId;

        public Long getPluginId() {
            return pluginId;
        }

        public void setPluginId(Long pluginId) {
            this.pluginId = pluginId;
        }
    }
}
