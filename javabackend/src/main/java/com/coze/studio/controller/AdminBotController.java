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

import com.coze.studio.dto.bot.BotQueryRequest;
import com.coze.studio.dto.bot.BotResponse;
import com.coze.studio.dto.bot.BotStatsResponse;
import com.coze.studio.dto.common.ApiResponse;
import com.coze.studio.dto.common.PageResponse;
import com.coze.studio.service.BotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Bot 管理员控制器
 * 提供管理员级别的 Bot 管理功能
 * 
 * @author coze-dev
 */
@Tag(name = "Bot管理员", description = "Bot管理员相关接口")
@RestController
@RequestMapping("/api/admin/bots")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
public class AdminBotController {

    private final BotService botService;

    /**
     * 管理员查看所有 Bot
     */
    @Operation(summary = "查看所有Bot", description = "管理员查看系统中所有Bot")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<BotResponse>>> getAllBots(
            BotQueryRequest queryRequest,
            @PageableDefault(size = 20, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<BotResponse> bots = botService.getBots(queryRequest, pageable);
        return ResponseEntity.ok(ApiResponse.success(bots));
    }

    /**
     * 强制删除 Bot
     */
    @Operation(summary = "强制删除Bot", description = "管理员强制删除任意Bot")
    @DeleteMapping("/{botId}/force")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<String>> forceDeleteBot(@PathVariable Long botId) {
        // TODO: 实现强制删除逻辑
        // botService.forceDeleteBot(botId);
        return ResponseEntity.ok(ApiResponse.success("Bot强制删除成功"));
    }

    /**
     * 批量操作 Bot
     */
    @Operation(summary = "批量操作Bot", description = "批量启用/禁用/删除Bot")
    @PostMapping("/batch")
    public ResponseEntity<ApiResponse<String>> batchOperateBots(
            @RequestParam String operation,
            @RequestBody Long[] botIds) {
        // TODO: 实现批量操作逻辑
        // botService.batchOperateBots(operation, Arrays.asList(botIds));
        return ResponseEntity.ok(ApiResponse.success("批量操作完成"));
    }

    /**
     * 获取 Bot 统计信息
     */
    @Operation(summary = "获取Bot统计", description = "获取指定Bot的详细统计信息")
    @GetMapping("/{botId}/stats")
    public ResponseEntity<ApiResponse<BotStatsResponse>> getBotStats(@PathVariable Long botId) {
        // TODO: 实现统计信息获取逻辑
        BotStatsResponse stats = new BotStatsResponse();
        stats.setBotId(botId);
        stats.setTotalConversations(0L);
        stats.setTotalMessages(0L);
        stats.setActiveUsers(0L);
        
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    /**
     * 获取系统 Bot 概览统计
     */
    @Operation(summary = "获取系统Bot概览", description = "获取系统中所有Bot的概览统计")
    @GetMapping("/overview")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getBotOverview() {
        // TODO: 实现系统概览统计逻辑
        Map<String, Object> overview = new HashMap<>();
        overview.put("totalBots", 0);
        overview.put("publishedBots", 0);
        overview.put("activeBots", 0);
        overview.put("totalUsers", 0);
        overview.put("totalConversations", 0);
        overview.put("totalMessages", 0);
        
        return ResponseEntity.ok(ApiResponse.success(overview));
    }

    /**
     * 审核 Bot 发布申请
     */
    @Operation(summary = "审核Bot发布", description = "审核Bot的发布申请")
    @PostMapping("/{botId}/review")
    public ResponseEntity<ApiResponse<String>> reviewBotPublication(
            @PathVariable Long botId,
            @RequestParam boolean approved,
            @RequestParam(required = false) String reason) {
        // TODO: 实现发布审核逻辑
        // botService.reviewBotPublication(botId, approved, reason);
        return ResponseEntity.ok(ApiResponse.success(approved ? "Bot发布已批准" : "Bot发布已拒绝"));
    }

    /**
     * 设置 Bot 推荐状态
     */
    @Operation(summary = "设置Bot推荐", description = "设置Bot是否为推荐Bot")
    @PostMapping("/{botId}/recommend")
    public ResponseEntity<ApiResponse<String>> recommendBot(
            @PathVariable Long botId,
            @RequestParam boolean recommended) {
        // TODO: 实现推荐设置逻辑
        // botService.setRecommended(botId, recommended);
        return ResponseEntity.ok(ApiResponse.success(recommended ? "Bot已设为推荐" : "Bot推荐已取消"));
    }

    /**
     * 获取 Bot 使用报告
     */
    @Operation(summary = "获取Bot使用报告", description = "获取Bot的详细使用报告")
    @GetMapping("/{botId}/report")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getBotReport(
            @PathVariable Long botId,
            @RequestParam(defaultValue = "7") int days) {
        // TODO: 实现使用报告生成逻辑
        Map<String, Object> report = new HashMap<>();
        report.put("botId", botId);
        report.put("period", days + " days");
        report.put("conversations", 0);
        report.put("messages", 0);
        report.put("users", 0);
        
        return ResponseEntity.ok(ApiResponse.success(report));
    }

    /**
     * 导出 Bot 数据
     */
    @Operation(summary = "导出Bot数据", description = "导出Bot的配置和数据")
    @GetMapping("/{botId}/export")
    public ResponseEntity<ApiResponse<Map<String, Object>>> exportBotData(@PathVariable Long botId) {
        // TODO: 实现数据导出逻辑
        Map<String, Object> exportData = new HashMap<>();
        exportData.put("botId", botId);
        exportData.put("exportTime", System.currentTimeMillis());
        
        return ResponseEntity.ok(ApiResponse.success(exportData));
    }
}
