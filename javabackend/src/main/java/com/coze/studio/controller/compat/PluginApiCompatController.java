/*
 * Copyright 2025 coze-dev Authors
 */
package com.coze.studio.controller.compat;

import com.coze.studio.dto.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * {{ AURA-X: Add - 兼容 /api/plugin_api/* 的最小一组骨架（convert_to_openapi、create_api、debug_api、register_plugin_meta）。Confirmed via 寸止 }}
 */
@Slf4j
@RestController
@RequestMapping("/api/plugin_api")
@Validated
@RequiredArgsConstructor
@Tag(name = "兼容层", description = "插件开发者工作流兼容接口")
public class PluginApiCompatController {

    @Operation(summary = "创建插件API")
    @PostMapping("/create_api")
    public ResponseEntity<ApiResponse<Map<String, Object>>> createApi(@RequestBody Map<String, Object> body) {
        log.info("[Compat] create_api: {}", body.keySet());
        Map<String, Object> resp = new HashMap<>();
        resp.put("status", "created");
        resp.put("api_id", System.currentTimeMillis());
        return ResponseEntity.ok(ApiResponse.success(resp));
    }

    @Operation(summary = "OpenAPI转换")
    @PostMapping("/convert_to_openapi")
    public ResponseEntity<ApiResponse<Map<String, Object>>> convertToOpenapi(@RequestBody Map<String, Object> body) {
        log.info("[Compat] convert_to_openapi");
        Map<String, Object> resp = new HashMap<>();
        resp.put("openapi", "3.0.0");
        resp.put("paths", body.getOrDefault("paths", new HashMap<>()));
        return ResponseEntity.ok(ApiResponse.success(resp));
    }

    @Operation(summary = "调试插件API")
    @PostMapping("/debug_api")
    public ResponseEntity<ApiResponse<Map<String, Object>>> debugApi(@RequestBody Map<String, Object> body) {
        log.info("[Compat] debug_api");
        Map<String, Object> resp = new HashMap<>();
        resp.put("result", "ok");
        resp.put("echo", body);
        return ResponseEntity.ok(ApiResponse.success(resp));
    }

    @Operation(summary = "注册插件元信息")
    @PostMapping("/register_plugin_meta")
    public ResponseEntity<ApiResponse<Map<String, Object>>> registerPluginMeta(@RequestBody Map<String, Object> body) {
        log.info("[Compat] register_plugin_meta");
        Map<String, Object> resp = new HashMap<>();
        resp.put("registered", true);
        resp.put("plugin_id", System.currentTimeMillis());
        return ResponseEntity.ok(ApiResponse.success(resp));
    }
}

