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
 * {{ AURA-X: Add - 兼容 knowledge 子域的 review/photo 骨架。Confirmed via 寸止 }}
 */
@Slf4j
@RestController
@RequestMapping("/api/knowledge")
@Validated
@RequiredArgsConstructor
@Tag(name = "兼容层", description = "知识库兼容接口：review/photo 骨架")
public class KnowledgeCompatController {

    @Operation(summary = "文档切片质检请求")
    @PostMapping("/review/request")
    public ResponseEntity<ApiResponse<Map<String, Object>>> reviewRequest(@RequestBody Map<String, Object> body) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("review_id", System.currentTimeMillis());
        resp.put("status", "queued");
        return ResponseEntity.ok(ApiResponse.success(resp));
    }

    @Operation(summary = "图片解析上传")
    @PostMapping("/photo/upload")
    public ResponseEntity<ApiResponse<Map<String, Object>>> photoUpload(@RequestBody Map<String, Object> body) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("image_id", System.currentTimeMillis());
        resp.put("status", "uploaded");
        return ResponseEntity.ok(ApiResponse.success(resp));
    }
}

