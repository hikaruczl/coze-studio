/*
 * Copyright 2025 coze-dev Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.coze.studio.controller;

import com.coze.studio.dto.bot.*;
import com.coze.studio.dto.common.ApiResponse;
import com.coze.studio.dto.common.PageResponse;
import com.coze.studio.security.UserPrincipal;
import com.coze.studio.service.BotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Bot 管理控制器
 * 处理 Bot 相关的 CRUD 操作
 * 
 * @author coze-dev
 */
@Tag(name = "Bot管理", description = "Bot应用管理相关接口")
@RestController
@RequestMapping("/api/bots")
@RequiredArgsConstructor
public class BotController {

    private final BotService botService;

    /**
     * 创建 Bot
     */
    @Operation(summary = "创建Bot", description = "创建一个新的Bot应用")
    @PostMapping
    public ResponseEntity<ApiResponse<BotResponse>> createBot(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody CreateBotRequest request) {
        BotResponse bot = botService.createBot(userPrincipal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Bot创建成功", bot));
    }

    /**
     * 更新 Bot
     */
    @Operation(summary = "更新Bot", description = "更新Bot的基本信息")
    @PutMapping("/{botId}")
    public ResponseEntity<ApiResponse<BotResponse>> updateBot(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long botId,
            @Valid @RequestBody UpdateBotRequest request) {
        BotResponse bot = botService.updateBot(userPrincipal.getId(), botId, request);
        return ResponseEntity.ok(ApiResponse.success("Bot更新成功", bot));
    }

    /**
     * 获取 Bot 详情
     */
    @Operation(summary = "获取Bot详情", description = "根据ID获取Bot的详细信息")
    @GetMapping("/{botId}")
    public ResponseEntity<ApiResponse<BotResponse>> getBotById(@PathVariable Long botId) {
        BotResponse bot = botService.getBotById(botId);
        return ResponseEntity.ok(ApiResponse.success(bot));
    }

    /**
     * 删除 Bot
     */
    @Operation(summary = "删除Bot", description = "删除指定的Bot（软删除）")
    @DeleteMapping("/{botId}")
    public ResponseEntity<ApiResponse<String>> deleteBot(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long botId) {
        botService.deleteBot(userPrincipal.getId(), botId);
        return ResponseEntity.ok(ApiResponse.success("Bot删除成功"));
    }

    /**
     * 分页查询 Bot 列表
     */
    @Operation(summary = "查询Bot列表", description = "分页查询Bot列表")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<BotResponse>>> getBots(
            BotQueryRequest queryRequest,
            @PageableDefault(size = 20, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<BotResponse> bots = botService.getBots(queryRequest, pageable);
        return ResponseEntity.ok(ApiResponse.success(bots));
    }

    /**
     * 获取当前用户的 Bot 列表
     */
    @Operation(summary = "获取我的Bot列表", description = "获取当前用户创建的Bot列表")
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<PageResponse<BotResponse>>> getMyBots(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PageableDefault(size = 20, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<BotResponse> bots = botService.getUserBots(userPrincipal.getId(), pageable);
        return ResponseEntity.ok(ApiResponse.success(bots));
    }

    /**
     * 发布 Bot
     */
    @Operation(summary = "发布Bot", description = "发布Bot到公开市场")
    @PostMapping("/{botId}/publish")
    public ResponseEntity<ApiResponse<BotResponse>> publishBot(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long botId,
            @Valid @RequestBody PublishBotRequest request) {
        BotResponse bot = botService.publishBot(userPrincipal.getId(), botId, request);
        return ResponseEntity.ok(ApiResponse.success("Bot发布成功", bot));
    }

    /**
     * 下线 Bot
     */
    @Operation(summary = "下线Bot", description = "将Bot从公开市场下线")
    @PostMapping("/{botId}/unpublish")
    public ResponseEntity<ApiResponse<BotResponse>> unpublishBot(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long botId) {
        BotResponse bot = botService.unpublishBot(userPrincipal.getId(), botId);
        return ResponseEntity.ok(ApiResponse.success("Bot下线成功", bot));
    }

    /**
     * 启用/禁用 Bot
     */
    @Operation(summary = "启用/禁用Bot", description = "启用或禁用指定的Bot")
    @PostMapping("/{botId}/toggle-status")
    public ResponseEntity<ApiResponse<BotResponse>> toggleBotStatus(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long botId,
            @RequestParam boolean enabled) {
        BotResponse bot = botService.toggleBotStatus(userPrincipal.getId(), botId, enabled);
        return ResponseEntity.ok(ApiResponse.success(enabled ? "Bot已启用" : "Bot已禁用", bot));
    }

    /**
     * 复制 Bot
     */
    @Operation(summary = "复制Bot", description = "复制一个现有的Bot")
    @PostMapping("/{botId}/clone")
    public ResponseEntity<ApiResponse<BotResponse>> cloneBot(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long botId,
            @RequestParam String newName) {
        BotResponse bot = botService.cloneBot(userPrincipal.getId(), botId, newName);
        return ResponseEntity.ok(ApiResponse.success("Bot复制成功", bot));
    }

    /**
     * 检查 Bot 名称可用性
     */
    @Operation(summary = "检查Bot名称可用性", description = "检查Bot名称是否可用")
    @GetMapping("/check-name")
    public ResponseEntity<ApiResponse<Boolean>> checkBotName(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam String name) {
        boolean available = botService.isBotNameAvailable(userPrincipal.getId(), name);
        return ResponseEntity.ok(ApiResponse.success(available));
    }

    /**
     * 获取已发布的 Bot 列表
     */
    @Operation(summary = "获取已发布Bot列表", description = "获取所有已发布的Bot列表")
    @GetMapping("/published")
    public ResponseEntity<ApiResponse<PageResponse<BotResponse>>> getPublishedBots(
            @PageableDefault(size = 20, sort = "publishedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<BotResponse> bots = botService.getPublishedBots(pageable);
        return ResponseEntity.ok(ApiResponse.success(bots));
    }

    /**
     * 获取热门 Bot 列表
     */
    @Operation(summary = "获取热门Bot列表", description = "获取热门Bot列表")
    @GetMapping("/popular")
    public ResponseEntity<ApiResponse<PageResponse<BotResponse>>> getPopularBots(
            @PageableDefault(size = 20) Pageable pageable) {
        PageResponse<BotResponse> bots = botService.getPopularBots(pageable);
        return ResponseEntity.ok(ApiResponse.success(bots));
    }

    /**
     * 统计用户的 Bot 数量
     */
    @Operation(summary = "统计Bot数量", description = "统计当前用户的Bot数量")
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> countMyBots(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        long count = botService.countUserBots(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success(count));
    }

    /**
     * 根据连接器获取 Bot 列表（管理员接口）
     */
    @Operation(summary = "根据连接器获取Bot列表", description = "根据连接器ID获取相关的Bot列表")
    @GetMapping("/by-connector/{connectorId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<PageResponse<BotResponse>>> getBotsByConnector(
            @PathVariable Long connectorId,
            @PageableDefault(size = 20, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<BotResponse> bots = botService.getBotsByConnector(connectorId, pageable);
        return ResponseEntity.ok(ApiResponse.success(bots));
    }
}
