/*
 * Copyright 2025 coze-dev Authors
 */
package com.coze.studio.controller.compat;

import com.coze.studio.dto.common.ApiResponse;
import com.coze.studio.entity.Document;
import com.coze.studio.controller.DocumentController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * {{ AURA-X: Add - 兼容 /v1/files/upload 映射到现有 DocumentController.upload. Confirmed via 寸止 }}
 */
@Slf4j
@RestController
@RequestMapping("/v1/files")
@Validated
@RequiredArgsConstructor
@Tag(name = "兼容层", description = "提供与 Go /v1/files 接口兼容的端点")
public class FilesOpenApiCompatController {

    private final DocumentController documentController;

    @Operation(summary = "上传文件(兼容)")
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<Document>> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("knowledgeBaseId") Long kbId,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("[Compat] /v1/files/upload -> delegate DocumentController.upload");
        return documentController.uploadDocument(file, kbId, null, null, true, userDetails);
    }
}

