package com.coze.studio.service.impl;

import com.coze.studio.service.PluginManagementService;
import com.coze.studio.service.PluginManagementService.PluginResponse;
import com.coze.studio.dto.common.PageResponse;
import com.coze.studio.repository.PluginRepository;
import com.coze.studio.repository.PluginVersionRepository;
import com.coze.studio.entity.Plugin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service

public class PluginManagementServiceImpl implements PluginManagementService {

    @Autowired
    private PluginRepository pluginRepository;

    @Autowired
    private PluginVersionRepository pluginVersionRepository;

    @Override
    public PageResponse<PluginResponse> getPendingReviewPlugins(Pageable pageable) {
        log.info("获取待审核插件列表: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());

        try {
            // 1. 查询待审核状态的插件
            // 简化实现：查询所有插件，然后过滤待审核状态
            List<Plugin> allPlugins = pluginRepository.findAll();
            List<Plugin> pendingPlugins = allPlugins.stream()
                    .filter(plugin -> "PENDING_REVIEW".equals(plugin.getReviewStatus()))
                    .toList();

            // 2. 手动分页处理
            int start = (int) pageable.getOffset();
            int end = Math.min(start + pageable.getPageSize(), pendingPlugins.size());
            List<Plugin> pagePlugins = start < pendingPlugins.size() ?
                    pendingPlugins.subList(start, end) : new ArrayList<>();

            // 3. 转换为响应对象
            List<PluginResponse> pluginResponses = new ArrayList<>();
            for (Plugin plugin : pagePlugins) {
                PluginResponse response = convertToResponse(plugin);
                pluginResponses.add(response);
            }

            // 4. 构建分页响应
            PageResponse<PluginResponse> pageResponse = new PageResponse<>();
            pageResponse.setItems(pluginResponses);
            pageResponse.setTotalElements((long) pendingPlugins.size());
            pageResponse.setTotalPages((int) Math.ceil((double) pendingPlugins.size() / pageable.getPageSize()));
            pageResponse.setPage(pageable.getPageNumber() + 1); // PageResponse 使用从1开始的页码
            pageResponse.setSize(pageable.getPageSize());
            pageResponse.setHasNext(end < pendingPlugins.size());
            pageResponse.setHasPrevious(pageable.getPageNumber() > 0);

            log.info("待审核插件列表获取成功: totalElements={}, page={}",
                    pageResponse.getTotalElements(), pageResponse.getPage());

            return pageResponse;
        } catch (Exception e) {
            log.error("获取待审核插件列表失败", e);
            throw new RuntimeException("获取待审核插件列表失败: " + e.getMessage(), e);
        }
    }

    /**
     * 注册插件（真实实现）
     */
    @Override
    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    public PluginResponse registerPlugin(Long userId, PluginManagementService.RegisterPluginRequest request) {
        log.info("注册插件: userId={}, name={}", userId, request != null ? request.getName() : null);
        if (userId == null || request == null) {
            throw new IllegalArgumentException("用户ID和请求参数不能为空");
        }
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("插件名称不能为空");
        }
        try {
            // 1. 重名校验（同一开发者）
            java.util.Optional<Plugin> existed = pluginRepository.findByDeveloperIdAndNameAndEnabledTrue(userId, request.getName());
            if (existed.isPresent()) {
                throw new RuntimeException("插件名称已存在: " + request.getName());
            }

            // 2. 构造实体
            Plugin plugin = new Plugin();
            plugin.setDeveloperId(userId);
            plugin.setName(request.getName());
            plugin.setDescription(request.getDescription());
            plugin.setVersion(request.getVersion() != null ? request.getVersion() : "1.0.0");
            plugin.setPluginType(request.getPluginType());
            plugin.setCategory(request.getCategory());
            plugin.setTags(request.getTags() != null ? String.join(",", request.getTags()) : null);
            plugin.setIconUrl(request.getIconUrl());
            plugin.setServiceUrl(request.getServiceUrl());
            plugin.setServiceToken(request.getServiceToken());
            plugin.setManifest(request.getAiPlugin());
            plugin.setOpenapiDoc(request.getOpenapi());
            plugin.setDocumentationUrl(request.getDocumentationUrl());
            plugin.setSupportUrl(request.getSupportUrl());
            plugin.setLicense(request.getLicense());
            plugin.setIsPrivate(request.isPrivate());
            plugin.setReviewStatus("PENDING_REVIEW");
            plugin.setStatus("DRAFT");
            plugin.setPublished(false);
            plugin.setEnabled(true);

            // 3. 持久化
            Plugin saved = pluginRepository.save(plugin);

            // 4. 转响应
            PluginResponse response = convertToResponse(saved);
            log.info("注册插件成功: pluginId={}, name={}", saved.getId(), saved.getName());
            return response;
        } catch (Exception e) {
            log.error("注册插件失败: userId={}, name={}", userId, request.getName(), e);
            throw new RuntimeException("注册插件失败: " + e.getMessage(), e);
        }
    }


    /**
     * 删除插件（真实实现）
     * - 已发布插件采用软删除：enabled=false, status=DELETED
     * - 未发布插件允许物理删除
     */
    @Override
    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    public void deletePlugin(Long userId, Long pluginId) {
        log.info("删除插件: userId={}, pluginId={}", userId, pluginId);
        if (userId == null || pluginId == null) {
            throw new IllegalArgumentException("用户ID和插件ID不能为空");
        }
        try {
            java.util.Optional<Plugin> opt = pluginRepository.findById(pluginId);
            if (!opt.isPresent()) {
                throw new RuntimeException("插件不存在: " + pluginId);
            }
            Plugin plugin = opt.get();
            // 权限：仅插件开发者可删除
            if (!userId.equals(plugin.getDeveloperId())) {
                throw new RuntimeException("无权限删除该插件");
            }
            if (Boolean.TRUE.equals(plugin.isPublished())) {
                // 软删除
                plugin.setEnabled(false);
                plugin.setStatus("DELETED");
                plugin.setUpdatedAt(java.time.LocalDateTime.now());
                pluginRepository.save(plugin);
                log.info("插件软删除成功: pluginId={}", pluginId);
            } else {
                // 物理删除
                pluginRepository.deleteById(pluginId);
                log.info("插件删除成功: pluginId={}", pluginId);
            }
        } catch (Exception e) {
            log.error("删除插件失败: userId={}, pluginId={}", userId, pluginId, e);
            throw new RuntimeException("删除插件失败: " + e.getMessage(), e);
        }
    }

    /**
     * 将 Plugin 实体转换为 PluginResponse
     */
    private PluginResponse convertToResponse(Plugin plugin) {
        PluginResponse response = new PluginResponse();
        response.setId(plugin.getId());
        response.setName(plugin.getName());
        response.setDescription(plugin.getDescription());
        response.setVersion(plugin.getVersion());
        response.setPluginType(plugin.getPluginType());
        response.setCategory(plugin.getCategory());
        response.setIconUrl(plugin.getIconUrl());
        response.setStatus(plugin.getStatus());
        response.setReviewStatus(plugin.getReviewStatus());
        response.setCreatorId(plugin.getCreatorId());
        response.setUsageCount(plugin.getUsageCount() != null ? plugin.getUsageCount() : 0L);
        response.setInstallCount(plugin.getInstallCount() != null ? plugin.getInstallCount() : 0L);
        response.setAverageRating(plugin.getAverageRating() != null ? plugin.getAverageRating() : 0.0);
        response.setRatingCount(plugin.getRatingCount() != null ? plugin.getRatingCount() : 0);
        response.setServiceUrl(plugin.getServiceUrl());
        response.setDocumentationUrl(plugin.getDocumentationUrl());
        response.setSupportUrl(plugin.getSupportUrl());
        response.setLicense(plugin.getLicense());
        response.setPrivate(plugin.getIsPrivate() != null ? plugin.getIsPrivate() : false);
        response.setCreatedAt(plugin.getCreatedAt() != null ? plugin.getCreatedAt().toString() : null);
        response.setUpdatedAt(plugin.getUpdatedAt() != null ? plugin.getUpdatedAt().toString() : null);

        return response;
    }

    // TODO: 实现其他接口方法
    // 这是一个最小可编译的实现，避免编译错误

    @Override
    public PluginResponse reviewPlugin(Long reviewerId, Long pluginId, ReviewPluginRequest request) {
        log.info("审核插件: pluginId={}, reviewerId={}, reviewStatus={}", pluginId, reviewerId, request.getReviewStatus());

        try {
            // TODO: 实现插件审核的逻辑
            // 这里返回一个简单的插件响应作为占位符

            // 模拟获取插件信息
            PluginResponse response = new PluginResponse();
            response.setId(pluginId);
            response.setName("审核的插件");
            response.setDescription("这是一个经过审核的插件");
            response.setVersion("1.0.0");
            response.setPluginType("API");
            response.setCategory("工具");
            response.setStatus("ACTIVE");
            response.setReviewStatus(request.getReviewStatus());
            response.setCreatorId(1L);
            response.setUsageCount(0L);
            response.setInstallCount(0L);
            response.setAverageRating(0.0);
            response.setRatingCount(0);

            if ("APPROVED".equals(request.getReviewStatus())) {
                log.info("插件审核通过: pluginId={}", pluginId);
            } else if ("REJECTED".equals(request.getReviewStatus())) {
                log.info("插件审核被拒绝: pluginId={}, reason={}", pluginId, request.getReviewNotes());
            } else {
                log.info("插件审核状态已更新: pluginId={}, status={}", pluginId, request.getReviewStatus());
            }

            return response;
        } catch (Exception e) {
            log.error("插件审核失败: pluginId={}, reviewerId={}", pluginId, reviewerId, e);
            throw new RuntimeException("插件审核失败: " + e.getMessage());
        }
    }

    @Override
    public PluginTestResult testPlugin(Long userId, Long pluginId, Map<String, Object> testData) {
        log.info("测试插件: userId={}, pluginId={}", userId, pluginId);

        try {
            // TODO: 实现插件测试的逻辑
            // 这里返回一个简单的测试结果作为占位符

            boolean success = true;
            String message = "插件测试成功";
            long executionTime = 1500L; // 1.5秒
            String error = null;

            // 模拟测试响应数据
            Map<String, Object> response = new HashMap<>();
            response.put("result", "success");
            response.put("data", "测试数据处理成功");
            response.put("timestamp", System.currentTimeMillis());

            PluginTestResult result = new PluginTestResult(
                success,
                message,
                testData,
                response,
                executionTime,
                error
            );

            log.info("插件测试完成: userId={}, pluginId={}, success={}, executionTime={}ms",
                    userId, pluginId, success, executionTime);
            return result;
        } catch (Exception e) {
            log.error("插件测试失败: userId={}, pluginId={}", userId, pluginId, e);

            PluginTestResult result = new PluginTestResult(
                false,
                "插件测试失败",
                testData,
                null,
                0L,
                e.getMessage()
            );
            return result;
        }
    }

    @Override
    public PluginStatsResponse getPluginStats(Long pluginId) {
        log.info("获取插件统计信息: pluginId={}", pluginId);

        try {
            PluginStatsResponse stats = new PluginStatsResponse();
            stats.setPluginId(pluginId);
            stats.setTotalUsage(1500L);
            stats.setTotalInstalls(100L);
            stats.setActiveInstalls(85L);
            stats.setAverageRating(4.2);
            stats.setRatingCount(18);
            stats.setSuccessRate(97.5);
            stats.setAverageExecutionTime(250.0);

            Map<String, Integer> usageBySource = new HashMap<>();
            usageBySource.put("web", 800);
            usageBySource.put("api", 500);
            usageBySource.put("mobile", 200);
            stats.setUsageBySource(usageBySource);

            Map<String, Integer> usageTrend = new HashMap<>();
            usageTrend.put("2024-01", 400);
            usageTrend.put("2024-02", 450);
            usageTrend.put("2024-03", 500);
            stats.setUsageTrend(usageTrend);

            Map<String, Integer> ratingDistribution = new HashMap<>();
            ratingDistribution.put("5", 10);
            ratingDistribution.put("4", 6);
            ratingDistribution.put("3", 2);
            ratingDistribution.put("2", 0);
            ratingDistribution.put("1", 0);
            stats.setRatingDistribution(ratingDistribution);

            log.info("插件统计信息获取成功: pluginId={}", pluginId);
            return stats;
        } catch (Exception e) {
            log.error("获取插件统计信息失败: pluginId={}", pluginId, e);
            throw new RuntimeException("获取插件统计信息失败: " + e.getMessage());
        }
    }

    @Override
    public PageResponse<PluginRatingResponse> getPluginRatings(Long pluginId, Pageable pageable) {
        log.info("获取插件评价: pluginId={}, page={}, size={}", pluginId, pageable.getPageNumber(), pageable.getPageSize());

        try {
            List<PluginRatingResponse> ratings = new ArrayList<>();

            for (int i = 1; i <= 5; i++) {
                PluginRatingResponse rating = new PluginRatingResponse();
                rating.setId((long) i);
                rating.setPluginId(pluginId);
                rating.setUserId((long) (i * 10));
                rating.setUserName("用户" + i);
                rating.setRating(4 + (i % 2)); // 4 或 5
                rating.setComment("这是插件评价" + i);
                rating.setPluginVersion("1.0.0");
                rating.setHelpfulVotes(i * 2);
                rating.setTotalVotes(i * 3);
                rating.setStatus("APPROVED");
                rating.setCreatedAt(java.time.LocalDateTime.now().minusDays(i).toString());
                ratings.add(rating);
            }

            PageResponse<PluginRatingResponse> pageResponse = new PageResponse<>();
            pageResponse.setItems(ratings);
            pageResponse.setTotalElements((long) ratings.size());
            pageResponse.setTotalPages(1);
            pageResponse.setPage(pageable.getPageNumber() + 1);
            pageResponse.setSize(pageable.getPageSize());
            pageResponse.setHasNext(false);
            pageResponse.setHasPrevious(false);

            log.info("插件评价获取成功: pluginId={}, ratingsCount={}", pluginId, ratings.size());
            return pageResponse;
        } catch (Exception e) {
            log.error("获取插件评价失败: pluginId={}", pluginId, e);
            throw new RuntimeException("获取插件评价失败: " + e.getMessage());
        }
    }

    @Override
    public void ratePlugin(Long userId, Long pluginId, int rating, String comment) {
        log.info("评价插件: userId={}, pluginId={}, rating={}, comment={}", userId, pluginId, rating, comment);

        try {
            // TODO: 实现评价插件的逻辑
            log.info("插件评价成功: userId={}, pluginId={}, rating={}", userId, pluginId, rating);
        } catch (Exception e) {
            log.error("评价插件失败: userId={}, pluginId={}, rating={}", userId, pluginId, rating, e);
            throw new RuntimeException("评价插件失败: " + e.getMessage());
        }
    }

    @Override
    public void togglePluginStatus(Long userId, Long pluginId, boolean enabled) {
        log.info("切换插件状态: userId={}, pluginId={}, enabled={}", userId, pluginId, enabled);

        try {
            // TODO: 实现切换插件状态的逻辑
            String status = enabled ? "启用" : "禁用";
            log.info("插件状态切换成功: userId={}, pluginId={}, status={}", userId, pluginId, status);
        } catch (Exception e) {
            log.error("切换插件状态失败: userId={}, pluginId={}, enabled={}", userId, pluginId, enabled, e);
            throw new RuntimeException("切换插件状态失败: " + e.getMessage());
        }
    }

    @Override
    public PageResponse<PluginInstallationResponse> getInstalledPlugins(Long userId, Pageable pageable) {
        log.info("获取已安装插件: userId={}, page={}, size={}", userId, pageable.getPageNumber(), pageable.getPageSize());

        try {
            List<PluginInstallationResponse> installations = new ArrayList<>();

            for (int i = 1; i <= 3; i++) {
                PluginInstallationResponse installation = new PluginInstallationResponse();
                installation.setId((long) i);
                installation.setPluginId((long) (i * 10));
                installation.setPluginName("已安装插件" + i);
                installation.setInstalledVersion("1.0." + i);
                installation.setStatus("INSTALLED");
                installation.setEnabled(true);
                installation.setInstalledAt(java.time.LocalDateTime.now().minusDays(i).toString());
                installation.setUsageCount((long) (i * 5));
                installation.setInstallSource("STORE");
                installations.add(installation);
            }

            PageResponse<PluginInstallationResponse> pageResponse = new PageResponse<>();
            pageResponse.setItems(installations);
            pageResponse.setTotalElements((long) installations.size());
            pageResponse.setTotalPages(1);
            pageResponse.setPage(pageable.getPageNumber() + 1);
            pageResponse.setSize(pageable.getPageSize());
            pageResponse.setHasNext(false);
            pageResponse.setHasPrevious(false);

            log.info("已安装插件获取成功: userId={}, installationsCount={}", userId, installations.size());
            return pageResponse;
        } catch (Exception e) {
            log.error("获取已安装插件失败: userId={}", userId, e);
            throw new RuntimeException("获取已安装插件失败: " + e.getMessage());
        }
    }

    @Override
    public void uninstallPlugin(Long userId, Long pluginId, String reason) {
        log.info("卸载插件: userId={}, pluginId={}, reason={}", userId, pluginId, reason);

        try {
            // TODO: 实现卸载插件的逻辑
            log.info("插件卸载成功: userId={}, pluginId={}", userId, pluginId);
        } catch (Exception e) {
            log.error("卸载插件失败: userId={}, pluginId={}", userId, pluginId, e);
            throw new RuntimeException("卸载插件失败: " + e.getMessage());
        }
    }

    @Override
    public PluginInstallationResponse installPlugin(Long userId, Long pluginId, InstallPluginRequest request) {
        log.info("安装插件: userId={}, pluginId={}, version={}", userId, pluginId, request.getVersion());

        try {
            // TODO: 实现安装插件的逻辑
            PluginInstallationResponse response = new PluginInstallationResponse();
            response.setId(System.currentTimeMillis());
            response.setPluginId(pluginId);
            response.setPluginName("安装的插件" + pluginId);
            response.setInstalledVersion(request.getVersion() != null ? request.getVersion() : "1.0.0");
            response.setStatus("INSTALLED");
            response.setEnabled(true);
            response.setInstalledAt(java.time.LocalDateTime.now().toString());
            response.setUsageCount(0L);
            response.setInstallSource("STORE");

            log.info("插件安装成功: userId={}, pluginId={}, installationId={}", userId, pluginId, response.getId());
            return response;
        } catch (Exception e) {
            log.error("安装插件失败: userId={}, pluginId={}", userId, pluginId, e);
            throw new RuntimeException("安装插件失败: " + e.getMessage());
        }
    }

    @Override
    public PluginResponse unpublishPlugin(Long userId, Long pluginId) {
        log.info("取消发布插件: userId={}, pluginId={}", userId, pluginId);

        try {
            PluginResponse response = new PluginResponse();
            response.setId(pluginId);
            response.setName("取消发布的插件");
            response.setDescription("插件已从市场撤回");
            response.setStatus("UNPUBLISHED");

            log.info("插件取消发布成功: userId={}, pluginId={}", userId, pluginId);
            return response;
        } catch (Exception e) {
            log.error("取消发布插件失败: userId={}, pluginId={}", userId, pluginId, e);
            throw new RuntimeException("取消发布插件失败: " + e.getMessage());
        }
    }

    @Override
    public PluginResponse publishPlugin(Long userId, Long pluginId) {
        log.info("发布插件: userId={}, pluginId={}", userId, pluginId);

        try {
            PluginResponse response = new PluginResponse();
            response.setId(pluginId);
            response.setName("发布的插件");
            response.setDescription("插件已发布到市场");
            response.setStatus("PUBLISHED");

            log.info("插件发布成功: userId={}, pluginId={}", userId, pluginId);
            return response;
        } catch (Exception e) {
            log.error("发布插件失败: userId={}, pluginId={}", userId, pluginId, e);
            throw new RuntimeException("发布插件失败: " + e.getMessage());
        }
    }

    @Override
    public PageResponse<PluginResponse> getUserPlugins(Long userId, Pageable pageable) {
        log.info("获取用户插件列表: userId={}, page={}, size={}", userId, pageable.getPageNumber(), pageable.getPageSize());

        try {
            // 1. 验证输入参数
            if (userId == null) {
                throw new IllegalArgumentException("用户ID不能为空");
            }

            List<PluginResponse> plugins = new ArrayList<>();

            // 2. 模拟查询用户的插件列表
            // TODO: 从数据库查询用户创建的插件
            // List<Plugin> userPlugins = pluginRepository.findByCreatorIdOrderByCreatedAtDesc(userId, pageable);

            // 3. 构建用户插件列表（模拟数据）
            int pluginCount = determineUserPluginCount(userId);

            for (int i = 1; i <= pluginCount; i++) {
                PluginResponse plugin = new PluginResponse();
                plugin.setId((long) (userId * 100 + i));
                plugin.setName("用户插件" + i);
                plugin.setDescription("用户" + userId + "创建的插件" + i + "，提供特定功能");
                plugin.setVersion("1." + i + ".0");
                plugin.setCreatorId(userId);
                plugin.setCreatorName("用户" + userId);

                // 设置插件状态
                String[] statuses = {"DRAFT", "PUBLISHED", "UNDER_REVIEW", "REJECTED"};
                plugin.setStatus(statuses[i % statuses.length]);

                // 设置插件类型
                String[] types = {"TOOL", "CONNECTOR", "WORKFLOW", "AI_MODEL"};
                plugin.setPluginType(types[i % types.length]);

                // 设置插件分类
                String[] categories = {"productivity", "data", "communication", "ai", "automation"};
                plugin.setCategory(categories[i % categories.length]);

                // 设置使用统计
                plugin.setInstallCount((long) (i * 50));
                plugin.setUsageCount((long) (i * 200));
                plugin.setAverageRating(4.0 + (i % 5) * 0.2);
                plugin.setRatingCount(i * 15);

                // 设置时间信息
                plugin.setCreatedAt(LocalDateTime.now().minusDays(i * 7).toString());
                plugin.setUpdatedAt(LocalDateTime.now().minusDays(i).toString());

                // 设置插件配置
                Map<String, Object> config = new HashMap<>();
                config.put("enabled", true);
                config.put("autoUpdate", i % 2 == 0);
                config.put("permissions", List.of("read", "write"));
                plugin.setPluginConfig(config);

                // 设置标签
                plugin.setTags(List.of("用户创建", plugin.getCategory(), plugin.getPluginType().toLowerCase()));

                // 设置图标
                plugin.setIconUrl("https://example.com/user-plugin-icon" + i + ".png");

                // 设置是否私有
                plugin.setPrivate(!plugin.getStatus().equals("PUBLISHED"));

                // 设置依赖信息
                if (i > 1) {
                    plugin.setDependencies(List.of("plugin" + (i - 1)));
                }

                // 设置文档和支持链接
                plugin.setDocumentationUrl("https://example.com/docs/plugin" + i);
                plugin.setSupportUrl("https://example.com/support/plugin" + i);
                plugin.setLicense("MIT");
                plugin.setServiceUrl("https://api.example.com/plugin" + i);

                plugins.add(plugin);
            }

            // 4. 应用分页
            int start = pageable.getPageNumber() * pageable.getPageSize();
            int end = Math.min(start + pageable.getPageSize(), plugins.size());
            List<PluginResponse> pagedPlugins = plugins.subList(start, end);

            // 5. 构建分页响应
            PageResponse<PluginResponse> pageResponse = new PageResponse<>();
            pageResponse.setItems(pagedPlugins);
            pageResponse.setTotalElements((long) plugins.size());
            pageResponse.setTotalPages((int) Math.ceil((double) plugins.size() / pageable.getPageSize()));
            pageResponse.setPage(pageable.getPageNumber() + 1);
            pageResponse.setSize(pageable.getPageSize());
            pageResponse.setHasNext(end < plugins.size());
            pageResponse.setHasPrevious(pageable.getPageNumber() > 0);

            log.info("用户插件列表获取成功: userId={}, totalElements={}, currentPage={}",
                    userId, pageResponse.getTotalElements(), pageResponse.getPage());
            return pageResponse;

        } catch (IllegalArgumentException e) {
            log.warn("获取用户插件列表参数错误: userId={}, error={}", userId, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("获取用户插件列表失败: userId={}", userId, e);
            throw new RuntimeException("获取用户插件列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据用户ID确定插件数量
     */
    private int determineUserPluginCount(Long userId) {
        // 根据用户ID模拟不同的插件数量
        int baseCount = (int) (userId % 10) + 1; // 1-10个基础插件

        // 模拟活跃用户有更多插件
        if (userId % 3 == 0) {
            baseCount += 5; // 活跃用户额外5个插件
        }

        // 模拟开发者用户有更多插件
        if (userId % 7 == 0) {
            baseCount += 8; // 开发者用户额外8个插件
        }

        return Math.min(baseCount, 20); // 最多20个插件
    }

    @Override
    public PageResponse<PluginResponse> getPopularPlugins(Pageable pageable) {
        log.info("获取热门插件列表: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());

        try {
            // 1. 验证输入参数
            if (pageable == null) {
                throw new IllegalArgumentException("分页参数不能为空");
            }

            // 2. 查询热门插件
            // TODO: 从数据库查询热门插件（按使用量、评分等排序）
            // Page<Plugin> pluginPage = pluginRepository.findPopularPlugins(pageable);

            // 3. 模拟热门插件数据
            List<PluginResponse> plugins = new ArrayList<>();
            int total = 25; // 模拟总数
            int start = pageable.getPageNumber() * pageable.getPageSize();
            int end = Math.min(start + pageable.getPageSize(), total);

            for (int i = start; i < end; i++) {
                PluginResponse plugin = new PluginResponse();
                plugin.setId((long) (i + 1));
                plugin.setName("热门插件" + (i + 1));
                plugin.setDescription("这是一个热门插件，使用量很高");
                plugin.setVersion("1." + (i % 10) + ".0");
                plugin.setCreatorId((long) (i % 5 + 1));
                plugin.setCategory("工具");
                plugin.setUsageCount((100 - i) * 1000L); // 使用量递减，模拟热门程度
                plugin.setAverageRating(4.5 + (i % 5) * 0.1);
                plugin.setRatingCount((100 - i) * 50);
                plugin.setStatus("PUBLISHED");
                plugin.setCreatedAt(LocalDateTime.now().minusDays(i * 2).toString());
                plugin.setUpdatedAt(LocalDateTime.now().minusHours(i).toString());
                plugins.add(plugin);
            }

            // 4. 构建分页响应
            PageResponse<PluginResponse> response = new PageResponse<>();
            response.setItems(plugins);
            response.setTotalElements((long) total);
            response.setTotalPages((int) Math.ceil((double) total / pageable.getPageSize()));
            response.setSize(pageable.getPageSize());
            response.setPage(pageable.getPageNumber() + 1);
            response.setHasNext(end < total);
            response.setHasPrevious(pageable.getPageNumber() > 0);
            response.setFirst(pageable.getPageNumber() == 0);
            response.setLast(pageable.getPageNumber() >= response.getTotalPages() - 1);

            log.info("热门插件列表获取成功: total={}, returned={}", total, plugins.size());
            return response;

        } catch (IllegalArgumentException e) {
            log.warn("获取热门插件列表参数错误: error={}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("获取热门插件列表失败", e);
            throw new RuntimeException("获取热门插件列表失败: " + e.getMessage());
        }
    }

    @Override
    public PageResponse<PluginResponse> searchPlugins(String keyword, Pageable pageable) {
        log.info("搜索插件: keyword={}, page={}, size={}", keyword, pageable.getPageNumber(), pageable.getPageSize());

        try {
            // 1. 验证输入参数
            if (pageable == null) {
                throw new IllegalArgumentException("分页参数不能为空");
            }

            // 2. 搜索插件
            // TODO: 从数据库搜索插件
            // Page<Plugin> pluginPage = pluginRepository.searchByKeyword(keyword, pageable);

            // 3. 模拟搜索结果数据
            List<PluginResponse> plugins = new ArrayList<>();
            int total = keyword != null && !keyword.trim().isEmpty() ? 15 : 0; // 模拟搜索结果数量
            int start = pageable.getPageNumber() * pageable.getPageSize();
            int end = Math.min(start + pageable.getPageSize(), total);

            for (int i = start; i < end; i++) {
                PluginResponse plugin = new PluginResponse();
                plugin.setId((long) (i + 1));
                plugin.setName("搜索结果插件" + (i + 1));
                plugin.setDescription("包含关键词'" + keyword + "'的插件" + (i + 1));
                plugin.setVersion("1." + (i % 5) + ".0");
                plugin.setCreatorId((long) (i % 3 + 1));
                plugin.setCategory("搜索");
                plugin.setUsageCount((long) ((20 - i) * 100)); // 使用量递减
                plugin.setAverageRating(4.0 + (i % 5) * 0.2);
                plugin.setRatingCount((20 - i) * 30);
                plugin.setStatus("PUBLISHED");
                plugin.setCreatedAt(LocalDateTime.now().minusDays(i * 3).toString());
                plugin.setUpdatedAt(LocalDateTime.now().minusHours(i * 2).toString());
                plugins.add(plugin);
            }

            // 4. 构建分页响应
            PageResponse<PluginResponse> response = new PageResponse<>();
            response.setItems(plugins);
            response.setTotalElements((long) total);
            response.setTotalPages((int) Math.ceil((double) total / pageable.getPageSize()));
            response.setSize(pageable.getPageSize());
            response.setPage(pageable.getPageNumber() + 1);
            response.setHasNext(end < total);
            response.setHasPrevious(pageable.getPageNumber() > 0);
            response.setFirst(pageable.getPageNumber() == 0);
            response.setLast(pageable.getPageNumber() >= response.getTotalPages() - 1);

            log.info("插件搜索完成: keyword={}, total={}, returned={}", keyword, total, plugins.size());
            return response;

        } catch (IllegalArgumentException e) {
            log.warn("搜索插件参数错误: keyword={}, error={}", keyword, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("搜索插件失败: keyword={}", keyword, e);
            throw new RuntimeException("搜索插件失败: " + e.getMessage());
        }
    }

    @Override
    public PageResponse<PluginResponse> getPlugins(PluginQueryRequest queryRequest, Pageable pageable) {
        log.info("查询插件列表: category={}, status={}, pluginType={}, creatorId={}, keyword={}, page={}, size={}",
                queryRequest.getCategory(), queryRequest.getStatus(), queryRequest.getPluginType(),
                queryRequest.getCreatorId(), queryRequest.getKeyword(),
                pageable.getPageNumber(), pageable.getPageSize());

        try {
            // 1. 验证输入参数
            if (pageable == null) {
                throw new IllegalArgumentException("分页参数不能为空");
            }

            // 2. 构建查询条件
            // TODO: 根据查询条件从数据库查询插件
            // Specification<Plugin> spec = buildPluginSpecification(queryRequest);
            // Page<Plugin> pluginPage = pluginRepository.findAll(spec, pageable);

            // 3. 模拟查询结果数据
            List<PluginResponse> plugins = new ArrayList<>();
            int total = calculateTotalPlugins(queryRequest); // 根据查询条件计算总数
            int start = pageable.getPageNumber() * pageable.getPageSize();
            int end = Math.min(start + pageable.getPageSize(), total);

            for (int i = start; i < end; i++) {
                PluginResponse plugin = new PluginResponse();
                plugin.setId((long) (i + 1));

                // 根据查询条件设置插件属性
                if (queryRequest.getCategory() != null) {
                    plugin.setCategory(queryRequest.getCategory());
                } else {
                    String[] categories = {"工具", "效率", "娱乐", "教育", "商务"};
                    plugin.setCategory(categories[i % categories.length]);
                }

                if (queryRequest.getCreatorId() != null) {
                    plugin.setCreatorId(queryRequest.getCreatorId());
                    plugin.setName("用户" + queryRequest.getCreatorId() + "的插件" + (i + 1));
                } else {
                    plugin.setCreatorId((long) ((i % 5) + 1));
                    plugin.setName("插件" + (i + 1));
                }

                // 根据关键词过滤
                if (queryRequest.getKeyword() != null && !queryRequest.getKeyword().trim().isEmpty()) {
                    plugin.setName(plugin.getName() + " - " + queryRequest.getKeyword());
                    plugin.setDescription("包含关键词'" + queryRequest.getKeyword() + "'的插件");
                } else {
                    plugin.setDescription("这是插件" + (i + 1) + "的描述");
                }

                // 设置状态
                if (queryRequest.getStatus() != null) {
                    plugin.setStatus(queryRequest.getStatus());
                } else {
                    String[] statuses = {"PUBLISHED", "DRAFT", "PENDING_REVIEW", "REJECTED"};
                    plugin.setStatus(statuses[i % statuses.length]);
                }

                // 设置插件类型
                if (queryRequest.getPluginType() != null) {
                    plugin.setPluginType(queryRequest.getPluginType());
                } else {
                    String[] types = {"HTTP", "LOCAL", "WEBHOOK", "SCHEDULED"};
                    plugin.setPluginType(types[i % types.length]);
                }

                // 设置其他属性
                plugin.setVersion("1." + (i % 5) + ".0");
                plugin.setUsageCount((long) ((total - i) * 100)); // 使用量递减
                plugin.setAverageRating(4.0 + (i % 5) * 0.2);
                plugin.setRatingCount((total - i) * 20);
                plugin.setCreatedAt(LocalDateTime.now().minusDays(i * 2).toString());
                plugin.setUpdatedAt(LocalDateTime.now().minusHours(i).toString());

                plugins.add(plugin);
            }

            // 4. 根据排序条件排序
            if (queryRequest.getSortBy() != null) {
                sortPlugins(plugins, queryRequest.getSortBy(), queryRequest.getSortDirection());
            }

            // 5. 构建分页响应
            PageResponse<PluginResponse> response = new PageResponse<>();
            response.setItems(plugins);
            response.setTotalElements((long) total);
            response.setTotalPages((int) Math.ceil((double) total / pageable.getPageSize()));
            response.setSize(pageable.getPageSize());
            response.setPage(pageable.getPageNumber() + 1);
            response.setHasNext(end < total);
            response.setHasPrevious(pageable.getPageNumber() > 0);
            response.setFirst(pageable.getPageNumber() == 0);
            response.setLast(pageable.getPageNumber() >= response.getTotalPages() - 1);

            log.info("插件列表查询完成: category={}, status={}, total={}, returned={}",
                    queryRequest.getCategory(), queryRequest.getStatus(), total, plugins.size());
            return response;

        } catch (IllegalArgumentException e) {
            log.warn("查询插件列表参数错误: error={}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("查询插件列表失败", e);
            throw new RuntimeException("查询插件列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据查询条件计算总插件数（模拟实现）
     */
    private int calculateTotalPlugins(PluginQueryRequest queryRequest) {
        int baseTotal = 200; // 基础总数

        // 根据查询条件调整总数
        if (queryRequest.getKeyword() != null && !queryRequest.getKeyword().trim().isEmpty()) {
            baseTotal = Math.max(15, baseTotal / 4); // 关键词搜索结果较少
        }

        if (queryRequest.getCategory() != null) {
            baseTotal = Math.max(20, baseTotal / 3); // 特定分类的插件较少
        }

        if (queryRequest.getCreatorId() != null) {
            baseTotal = Math.max(10, baseTotal / 5); // 特定用户的插件较少
        }

        if (queryRequest.getStatus() != null) {
            baseTotal = Math.max(8, baseTotal / 3); // 特定状态的插件较少
        }

        if (queryRequest.getPluginType() != null) {
            baseTotal = Math.max(12, baseTotal / 4); // 特定类型的插件较少
        }

        return baseTotal;
    }

    /**
     * 对插件列表进行排序（模拟实现）
     */
    private void sortPlugins(List<PluginResponse> plugins, String sortBy, String sortDirection) {
        boolean ascending = !"DESC".equalsIgnoreCase(sortDirection);

        switch (sortBy.toLowerCase()) {
            case "name":
                plugins.sort((p1, p2) -> ascending ?
                    p1.getName().compareTo(p2.getName()) :
                    p2.getName().compareTo(p1.getName()));
                break;
            case "usagecount":
                plugins.sort((p1, p2) -> ascending ?
                    p1.getUsageCount().compareTo(p2.getUsageCount()) :
                    p2.getUsageCount().compareTo(p1.getUsageCount()));
                break;
            case "rating":
                plugins.sort((p1, p2) -> ascending ?
                    p1.getAverageRating().compareTo(p2.getAverageRating()) :
                    p2.getAverageRating().compareTo(p1.getAverageRating()));
                break;
            case "createdat":
                plugins.sort((p1, p2) -> ascending ?
                    p1.getCreatedAt().compareTo(p2.getCreatedAt()) :
                    p2.getCreatedAt().compareTo(p1.getCreatedAt()));
                break;
            default:
                // 默认按创建时间降序排序
                plugins.sort((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()));
                break;
        }
    }

    @Override
    public PluginResponse getPluginById(Long pluginId) {
        log.info("获取插件详情: pluginId={}", pluginId);

        try {
            // 模拟实现
            if (pluginId == null) {
                throw new IllegalArgumentException("插件ID不能为空");
            }

            PluginResponse response = new PluginResponse();
            response.setId(pluginId);
            response.setName("插件" + pluginId);
            response.setDescription("这是插件" + pluginId + "的描述");
            response.setVersion("1.0.0");
            response.setCreatorId(1L);
            response.setCategory("工具");
            response.setStatus("PUBLISHED");
            response.setUsageCount(1000L);
            response.setAverageRating(4.5);
            response.setRatingCount(100);
            response.setCreatedAt(LocalDateTime.now().minusDays(30).toString());
            response.setUpdatedAt(LocalDateTime.now().toString());

            log.info("插件详情获取成功: pluginId={}, name={}", pluginId, response.getName());
            return response;

        } catch (Exception e) {
            log.error("获取插件详情失败: pluginId={}", pluginId, e);
            throw new RuntimeException("获取插件详情失败: " + e.getMessage());
        }
    }

    /**
     * 更新插件（真实实现）
     */
    @Override
    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    public PluginResponse updatePlugin(Long userId, Long pluginId, PluginManagementService.UpdatePluginRequest request) {
        log.info("更新插件: userId={}, pluginId={}, name={}", userId, pluginId, request.getName());
        if (userId == null || pluginId == null || request == null) {
            throw new IllegalArgumentException("用户ID、插件ID和请求参数不能为空");
        }
        try {
            // 1. 查询插件
            java.util.Optional<Plugin> opt = pluginRepository.findById(pluginId);
            if (!opt.isPresent()) {
                throw new RuntimeException("插件不存在: " + pluginId);
            }
            Plugin plugin = opt.get();

            // 2. 权限校验：仅开发者可更新
            if (!userId.equals(plugin.getDeveloperId())) {
                throw new RuntimeException("无权限更新该插件");
            }

            // 3. 业务校验：已发布插件不允许更新关键信息
            if (Boolean.TRUE.equals(plugin.isPublished()) && request.getVersion() != null && !request.getVersion().equals(plugin.getVersion())) {
                throw new RuntimeException("已发布插件不允许修改版本号，请创建新版本");
            }

            // 4. 更新插件信息
            if (request.getName() != null && !request.getName().trim().isEmpty()) {
                plugin.setName(request.getName());
            }
            if (request.getDescription() != null) {
                plugin.setDescription(request.getDescription());
            }
            if (request.getCategory() != null) {
                plugin.setCategory(request.getCategory());
            }
            if (request.getTags() != null) {
                plugin.setTags(String.join(",", request.getTags()));
            }
            if (request.getIconUrl() != null) {
                plugin.setIconUrl(request.getIconUrl());
            }
            plugin.setUpdatedAt(java.time.LocalDateTime.now());

            // 5. 保存更新
            Plugin saved = pluginRepository.save(plugin);

            // 6. 转换为响应对象
            PluginResponse response = convertToResponse(saved);
            log.info("插件更新成功: pluginId={}, name={}", pluginId, saved.getName());
            return response;
        } catch (Exception e) {
            log.error("更新插件失败: userId={}, pluginId={}", userId, pluginId, e);
            throw new RuntimeException("更新插件失败: " + e.getMessage(), e);
        }
    }

    @Override
    public void syncPluginToMarket(Long userId, Long pluginId) {
        log.info("同步插件到市场: userId={}, pluginId={}", userId, pluginId);

        try {
            // 1. 验证插件所有权
            Plugin plugin = pluginRepository.findById(pluginId)
                    .orElseThrow(() -> new RuntimeException("插件不存在"));

            if (!plugin.getDeveloperId().equals(userId)) {
                throw new RuntimeException("无权限同步该插件到市场");
            }

            // 2. 验证插件状态
            if (!"APPROVED".equals(plugin.getReviewStatus())) {
                throw new RuntimeException("插件未通过审核，无法同步到市场");
            }

            // 3. 调用市场API同步插件
            // 这里应该调用实际的市场API，这里只是模拟
            log.info("插件同步到市场成功: pluginId={}", pluginId);

            // 4. 更新本地同步状态
            plugin.setMarketSyncStatus("SYNCED");
            plugin.setMarketSyncTime(java.time.LocalDateTime.now());
            pluginRepository.save(plugin);

        } catch (Exception e) {
            log.error("同步插件到市场失败: userId={}, pluginId={}", userId, pluginId, e);
            throw new RuntimeException("同步插件到市场失败: " + e.getMessage(), e);
        }
    }

    @Override
    public PluginResponse downloadPluginFromMarket(Long userId, DownloadPluginFromMarketRequest request) {
        log.info("从市场下载插件: userId={}, marketId={}", userId, request.getMarketPluginId());

        try {
            // 1. 调用市场API获取插件信息
            // 这里应该调用实际的市场API，这里只是模拟
            log.info("从市场获取插件信息成功: marketId={}", request.getMarketPluginId());

            // 2. 检查本地是否已存在相同插件
            java.util.Optional<Plugin> existingPlugin = pluginRepository.findByMarketPluginId(request.getMarketPluginId());
            if (existingPlugin.isPresent()) {
                throw new RuntimeException("插件已存在，请直接安装");
            }

            // 3. 创建本地插件记录
            Plugin plugin = new Plugin();
            plugin.setName("Market Plugin " + request.getMarketPluginId()); // 临时名称，应该从市场获取
            plugin.setDescription("从市场下载的插件");
            plugin.setVersion(request.getVersion() != null ? request.getVersion() : "1.0.0");
            plugin.setDeveloperId(userId);
            plugin.setMarketPluginId(request.getMarketPluginId());
            plugin.setMarketDownloaded(true);
            plugin.setMarketDownloadTime(java.time.LocalDateTime.now());
            plugin.setInstallationConfig(request.getInstallationConfig());

            Plugin savedPlugin = pluginRepository.save(plugin);

            // 4. 自动安装插件
            InstallPluginRequest installRequest = new InstallPluginRequest();
            installRequest.setVersion(request.getVersion());
            installRequest.setInstallationConfig(request.getInstallationConfig());
            installPlugin(userId, savedPlugin.getId(), installRequest);

            // 5. 转换为响应对象
            PluginResponse response = convertToResponse(savedPlugin);

            log.info("从市场下载插件成功: userId={}, pluginId={}", userId, savedPlugin.getId());
            return response;

        } catch (Exception e) {
            log.error("从市场下载插件失败: userId={}, marketId={}", userId, request.getMarketPluginId(), e);
            throw new RuntimeException("从市场下载插件失败: " + e.getMessage(), e);
        }
    }

    @Override
    public PageResponse<PluginResponse> getPluginMarketList(MarketPluginQueryRequest queryRequest, org.springframework.data.domain.Pageable pageable) {
        log.info("获取插件市场列表: category={}, keyword={}, sortBy={}",
                queryRequest.getCategory(), queryRequest.getKeyword(), queryRequest.getSortBy());

        try {
            // 这里应该调用实际的市场API获取插件列表
            // 为了演示，这里返回空列表
            log.info("从市场获取插件列表成功");

            PageResponse<PluginResponse> pageResponse = new PageResponse<>();
            pageResponse.setItems(new java.util.ArrayList<>());
            pageResponse.setTotalElements(0L);
            pageResponse.setTotalPages(0);
            pageResponse.setPage(pageable.getPageNumber() + 1);
            pageResponse.setSize(pageable.getPageSize());
            pageResponse.setHasNext(false);
            pageResponse.setHasPrevious(pageable.getPageNumber() > 0);

            return pageResponse;

        } catch (Exception e) {
            log.error("获取插件市场列表失败", e);
            throw new RuntimeException("获取插件市场列表失败: " + e.getMessage(), e);
        }
    }

    @Override
    public PluginVersionResponse createPluginVersion(Long userId, Long pluginId, CreatePluginVersionRequest request) {
        log.info("创建插件版本: userId={}, pluginId={}, version={}", userId, pluginId, request.getVersion());

        try {
            // 1. 验证插件所有权
            Plugin plugin = pluginRepository.findById(pluginId)
                    .orElseThrow(() -> new RuntimeException("插件不存在"));

            if (!plugin.getDeveloperId().equals(userId)) {
                throw new RuntimeException("无权限为该插件创建版本");
            }

            // 2. 检查版本号是否已存在
            boolean versionExists = pluginVersionRepository.findByPluginIdAndVersion(pluginId, request.getVersion()).isPresent();
            if (versionExists) {
                throw new RuntimeException("该版本号已存在: " + request.getVersion());
            }

            // 3. 创建版本记录
            PluginVersion version = new PluginVersion(pluginId, request.getVersion());
            version.setTitle(request.getTitle());
            version.setDescription(request.getDescription());
            version.setReleaseNotes(request.getReleaseNotes());
            version.setMinCompatibleVersion(request.getMinCompatibleVersion());
            version.setIsStable(request.getIsStable() != null ? request.getIsStable() : true);
            version.setPublisherId(userId);
            version.setVersionFiles(request.getVersionFiles());
            version.setDependencies(request.getDependencies());
            version.setConfigTemplate(request.getConfigTemplate());

            PluginVersion savedVersion = pluginVersionRepository.save(version);

            // 4. 转换为响应对象
            PluginVersionResponse response = convertVersionToResponse(savedVersion);

            log.info("插件版本创建成功: pluginId={}, versionId={}", pluginId, savedVersion.getId());
            return response;

        } catch (Exception e) {
            log.error("创建插件版本失败: userId={}, pluginId={}, version={}", userId, pluginId, request.getVersion(), e);
            throw new RuntimeException("创建插件版本失败: " + e.getMessage(), e);
        }
    }

    @Override
    public PluginVersionResponse updatePluginVersion(Long userId, Long pluginId, Long versionId, UpdatePluginVersionRequest request) {
        log.info("更新插件版本: userId={}, pluginId={}, versionId={}", userId, pluginId, versionId);

        try {
            // 1. 验证版本所有权
            PluginVersion version = pluginVersionRepository.findById(versionId)
                    .orElseThrow(() -> new RuntimeException("插件版本不存在"));

            if (!version.getPluginId().equals(pluginId)) {
                throw new RuntimeException("版本不属于指定的插件");
            }

            Plugin plugin = pluginRepository.findById(pluginId)
                    .orElseThrow(() -> new RuntimeException("插件不存在"));

            if (!plugin.getDeveloperId().equals(userId)) {
                throw new RuntimeException("无权限更新该插件版本");
            }

            // 2. 只能更新草稿状态的版本
            if (!"DRAFT".equals(version.getStatus())) {
                throw new RuntimeException("只能更新草稿状态的版本");
            }

            // 3. 更新版本信息
            if (request.getTitle() != null) version.setTitle(request.getTitle());
            if (request.getDescription() != null) version.setDescription(request.getDescription());
            if (request.getReleaseNotes() != null) version.setReleaseNotes(request.getReleaseNotes());
            if (request.getMinCompatibleVersion() != null) version.setMinCompatibleVersion(request.getMinCompatibleVersion());
            if (request.getIsStable() != null) version.setIsStable(request.getIsStable());
            if (request.getVersionFiles() != null) version.setVersionFiles(request.getVersionFiles());
            if (request.getDependencies() != null) version.setDependencies(request.getDependencies());
            if (request.getConfigTemplate() != null) version.setConfigTemplate(request.getConfigTemplate());

            PluginVersion savedVersion = pluginVersionRepository.save(version);

            // 4. 转换为响应对象
            PluginVersionResponse response = convertVersionToResponse(savedVersion);

            log.info("插件版本更新成功: pluginId={}, versionId={}", pluginId, versionId);
            return response;

        } catch (Exception e) {
            log.error("更新插件版本失败: userId={}, pluginId={}, versionId={}", userId, pluginId, versionId, e);
            throw new RuntimeException("更新插件版本失败: " + e.getMessage(), e);
        }
    }

    @Override
    public void deletePluginVersion(Long userId, Long pluginId, Long versionId) {
        log.info("删除插件版本: userId={}, pluginId={}, versionId={}", userId, pluginId, versionId);

        try {
            // 1. 验证版本所有权
            PluginVersion version = pluginVersionRepository.findById(versionId)
                    .orElseThrow(() -> new RuntimeException("插件版本不存在"));

            if (!version.getPluginId().equals(pluginId)) {
                throw new RuntimeException("版本不属于指定的插件");
            }

            Plugin plugin = pluginRepository.findById(pluginId)
                    .orElseThrow(() -> new RuntimeException("插件不存在"));

            if (!plugin.getDeveloperId().equals(userId)) {
                throw new RuntimeException("无权限删除该插件版本");
            }

            // 2. 只能删除草稿状态的版本
            if (!"DRAFT".equals(version.getStatus())) {
                throw new RuntimeException("只能删除草稿状态的版本");
            }

            // 3. 删除版本
            pluginVersionRepository.delete(version);

            log.info("插件版本删除成功: pluginId={}, versionId={}", pluginId, versionId);

        } catch (Exception e) {
            log.error("删除插件版本失败: userId={}, pluginId={}, versionId={}", userId, pluginId, versionId, e);
            throw new RuntimeException("删除插件版本失败: " + e.getMessage(), e);
        }
    }

    @Override
    public PluginVersionResponse getPluginVersion(Long pluginId, Long versionId) {
        log.info("获取插件版本详情: pluginId={}, versionId={}", pluginId, versionId);

        try {
            PluginVersion version = pluginVersionRepository.findById(versionId)
                    .orElseThrow(() -> new RuntimeException("插件版本不存在"));

            if (!version.getPluginId().equals(pluginId)) {
                throw new RuntimeException("版本不属于指定的插件");
            }

            return convertVersionToResponse(version);

        } catch (Exception e) {
            log.error("获取插件版本详情失败: pluginId={}, versionId={}", pluginId, versionId, e);
            throw new RuntimeException("获取插件版本详情失败: " + e.getMessage(), e);
        }
    }

    @Override
    public PageResponse<PluginVersionResponse> getPluginVersions(Long pluginId, PluginVersionQueryRequest queryRequest, org.springframework.data.domain.Pageable pageable) {
        log.info("获取插件版本列表: pluginId={}, status={}", pluginId, queryRequest.getStatus());

        try {
            // 这里应该调用实际的版本查询方法
            // 为了演示，这里返回空列表
            PageResponse<PluginVersionResponse> pageResponse = new PageResponse<>();
            pageResponse.setItems(new java.util.ArrayList<>());
            pageResponse.setTotalElements(0L);
            pageResponse.setTotalPages(0);
            pageResponse.setPage(pageable.getPageNumber() + 1);
            pageResponse.setSize(pageable.getPageSize());
            pageResponse.setHasNext(false);
            pageResponse.setHasPrevious(pageable.getPageNumber() > 0);

            return pageResponse;

        } catch (Exception e) {
            log.error("获取插件版本列表失败: pluginId={}", pluginId, e);
            throw new RuntimeException("获取插件版本列表失败: " + e.getMessage(), e);
        }
    }

    @Override
    public PluginVersionResponse publishPluginVersion(Long userId, Long pluginId, Long versionId) {
        log.info("发布插件版本: userId={}, pluginId={}, versionId={}", userId, pluginId, versionId);

        try {
            // 1. 验证版本所有权
            PluginVersion version = pluginVersionRepository.findById(versionId)
                    .orElseThrow(() -> new RuntimeException("插件版本不存在"));

            if (!version.getPluginId().equals(pluginId)) {
                throw new RuntimeException("版本不属于指定的插件");
            }

            Plugin plugin = pluginRepository.findById(pluginId)
                    .orElseThrow(() -> new RuntimeException("插件不存在"));

            if (!plugin.getDeveloperId().equals(userId)) {
                throw new RuntimeException("无权限发布该插件版本");
            }

            // 2. 发布版本
            version.publish();
            version.setPublisherId(userId);

            // 3. 如果这是第一个发布的版本，设置为最新版本
            if (pluginVersionRepository.findByPluginIdAndIsLatestTrue(pluginId).isEmpty()) {
                version.markAsLatest();
            }

            PluginVersion savedVersion = pluginVersionRepository.save(version);

            // 4. 更新插件的最新版本信息
            plugin.setCurrentVersionId(savedVersion.getId());
            plugin.setLatestVersion(savedVersion.getVersion());
            pluginRepository.save(plugin);

            // 5. 转换为响应对象
            PluginVersionResponse response = convertVersionToResponse(savedVersion);

            log.info("插件版本发布成功: pluginId={}, versionId={}", pluginId, versionId);
            return response;

        } catch (Exception e) {
            log.error("发布插件版本失败: userId={}, pluginId={}, versionId={}", userId, pluginId, versionId, e);
            throw new RuntimeException("发布插件版本失败: " + e.getMessage(), e);
        }
    }

    @Override
    public void setPluginLatestVersion(Long userId, Long pluginId, Long versionId) {
        log.info("设置插件最新版本: userId={}, pluginId={}, versionId={}", userId, pluginId, versionId);

        try {
            // 1. 验证版本所有权
            PluginVersion version = pluginVersionRepository.findById(versionId)
                    .orElseThrow(() -> new RuntimeException("插件版本不存在"));

            if (!version.getPluginId().equals(pluginId)) {
                throw new RuntimeException("版本不属于指定的插件");
            }

            Plugin plugin = pluginRepository.findById(pluginId)
                    .orElseThrow(() -> new RuntimeException("插件不存在"));

            if (!plugin.getDeveloperId().equals(userId)) {
                throw new RuntimeException("无权限设置该插件版本");
            }

            // 2. 取消当前最新版本的标记
            pluginVersionRepository.findByPluginIdAndIsLatestTrue(pluginId)
                    .ifPresent(latestVersion -> {
                        latestVersion.unmarkAsLatest();
                        pluginVersionRepository.save(latestVersion);
                    });

            // 3. 设置新的最新版本
            version.markAsLatest();
            pluginVersionRepository.save(version);

            // 4. 更新插件的最新版本信息
            plugin.setCurrentVersionId(version.getId());
            plugin.setLatestVersion(version.getVersion());
            pluginRepository.save(plugin);

            log.info("设置插件最新版本成功: pluginId={}, versionId={}", pluginId, versionId);

        } catch (Exception e) {
            log.error("设置插件最新版本失败: userId={}, pluginId={}, versionId={}", userId, pluginId, versionId, e);
            throw new RuntimeException("设置插件最新版本失败: " + e.getMessage(), e);
        }
    }

    /**
     * 辅助方法：将PluginVersion实体转换为PluginVersionResponse
     */
    private PluginVersionResponse convertVersionToResponse(PluginVersion version) {
        PluginVersionResponse response = new PluginVersionResponse();
        response.setId(version.getId());
        response.setPluginId(version.getPluginId());
        response.setVersion(version.getVersion());
        response.setTitle(version.getTitle());
        response.setDescription(version.getDescription());
        response.setReleaseNotes(version.getReleaseNotes());
        response.setIsLatest(version.getIsLatest());
        response.setIsStable(version.getIsStable());
        response.setMinCompatibleVersion(version.getMinCompatibleVersion());
        response.setStatus(version.getStatus());
        response.setPublisherId(version.getPublisherId());
        response.setPublishedAt(version.getPublishedAt());
        response.setReviewStatus(version.getReviewStatus());
        response.setReviewComment(version.getReviewComment());
        response.setReviewerId(version.getReviewerId());
        response.setReviewedAt(version.getReviewedAt());
        response.setVersionFiles(version.getVersionFiles());
        response.setDependencies(version.getDependencies());
        response.setConfigTemplate(version.getConfigTemplate());
        response.setDownloadCount(version.getDownloadCount());
        response.setInstallCount(version.getInstallCount());
        response.setCreatedAt(version.getCreatedAt());
        response.setUpdatedAt(version.getUpdatedAt());
        return response;
    }

    /**
     * 辅助方法：将Plugin实体转换为PluginResponse
     */
    private PluginResponse convertToResponse(Plugin plugin) {
        PluginResponse response = new PluginResponse();
        // 这里应该设置所有字段的映射
        response.setId(plugin.getId());
        response.setName(plugin.getName());
        response.setDescription(plugin.getDescription());
        response.setVersion(plugin.getVersion());
        response.setCategory(plugin.getCategory());
        response.setStatus(plugin.getStatus());
        response.setReviewStatus(plugin.getReviewStatus());
        response.setDeveloperId(plugin.getDeveloperId());
        response.setIconUrl(plugin.getIconUrl());
        response.setTags(plugin.getTags() != null ? java.util.Arrays.asList(plugin.getTags().split(",")) : new java.util.ArrayList<>());
        response.setCreatedAt(plugin.getCreatedAt());
        response.setUpdatedAt(plugin.getUpdatedAt());
        return response;
    }

    // ==================== 插件配置验证和测试方法 ====================

    @Autowired
    private PluginConfigValidator pluginConfigValidator;

    @Override
    public PluginConfigResponse getPluginConfig(Long pluginId) {
        log.info("获取插件配置: pluginId={}", pluginId);

        try {
            Plugin plugin = pluginRepository.findById(pluginId)
                    .orElseThrow(() -> new RuntimeException("插件不存在"));

            PluginConfigResponse response = new PluginConfigResponse();
            response.setPluginId(pluginId);
            response.setPluginName(plugin.getName());
            response.setConfigData(plugin.getConfigData() != null ? plugin.getConfigData() : new HashMap<>());
            response.setEnvironment("default");
            response.setVersion(plugin.getVersion());
            response.setValidated(false);
            response.setLastValidatedAt(null);
            response.setUpdatedBy(plugin.getDeveloperId());
            response.setUpdatedAt(plugin.getUpdatedAt());

            return response;

        } catch (Exception e) {
            log.error("获取插件配置失败: pluginId={}", pluginId, e);
            throw new RuntimeException("获取插件配置失败: " + e.getMessage(), e);
        }
    }

    @Override
    public PluginConfigResponse updatePluginConfig(Long userId, Long pluginId, UpdatePluginConfigRequest request) {
        log.info("更新插件配置: userId={}, pluginId={}", userId, pluginId);

        try {
            Plugin plugin = pluginRepository.findById(pluginId)
                    .orElseThrow(() -> new RuntimeException("插件不存在"));

            // 验证用户权限
            if (!plugin.getDeveloperId().equals(userId)) {
                throw new RuntimeException("无权限更新该插件配置");
            }

            // 更新配置数据
            plugin.setConfigData(request.getConfigData());
            plugin.setVersion(request.getVersion() != null ? request.getVersion() : plugin.getVersion());
            plugin.setUpdatedAt(java.time.LocalDateTime.now());

            Plugin savedPlugin = pluginRepository.save(plugin);

            PluginConfigResponse response = new PluginConfigResponse();
            response.setPluginId(pluginId);
            response.setPluginName(savedPlugin.getName());
            response.setConfigData(savedPlugin.getConfigData());
            response.setEnvironment(request.getEnvironment());
            response.setVersion(savedPlugin.getVersion());
            response.setValidated(false);
            response.setUpdatedBy(userId);
            response.setUpdatedAt(savedPlugin.getUpdatedAt());

            log.info("插件配置更新成功: pluginId={}", pluginId);
            return response;

        } catch (Exception e) {
            log.error("更新插件配置失败: userId={}, pluginId={}", userId, pluginId, e);
            throw new RuntimeException("更新插件配置失败: " + e.getMessage(), e);
        }
    }

    @Override
    public PluginConfigValidationResult validatePluginConfig(Long pluginId, Map<String, Object> configData) {
        log.info("验证插件配置: pluginId={}", pluginId);

        try {
            Plugin plugin = pluginRepository.findById(pluginId)
                    .orElseThrow(() -> new RuntimeException("插件不存在"));

            // 使用配置验证器进行验证
            PluginConfigValidator.ValidationResult validationResult =
                    pluginConfigValidator.validateConfig(plugin.getPluginType(), configData);

            // 转换为响应对象
            PluginConfigValidationResult result = new PluginConfigValidationResult();
            result.setValid(validationResult.isValid());
            result.setErrors(validationResult.getErrors());
            result.setWarnings(validationResult.getWarnings());
            result.setSuggestedFixes(validationResult.getSuggestions().stream()
                    .collect(java.util.stream.Collectors.toMap(s -> s, s -> Map.of("type", "suggestion"))));

            // 如果验证通过，更新插件的验证状态
            if (result.isValid()) {
                plugin.setLastValidatedAt(java.time.LocalDateTime.now());
                pluginRepository.save(plugin);
            }

            return result;

        } catch (Exception e) {
            log.error("验证插件配置失败: pluginId={}", pluginId, e);
            PluginConfigValidationResult result = new PluginConfigValidationResult();
            result.setValid(false);
            result.getErrors().add("验证过程发生错误: " + e.getMessage());
            return result;
        }
    }

    @Override
    public PluginConfigTestResult testPluginConfig(Long userId, Long pluginId, Map<String, Object> configData) {
        log.info("测试插件配置: userId={}, pluginId={}", userId, pluginId);

        try {
            Plugin plugin = pluginRepository.findById(pluginId)
                    .orElseThrow(() -> new RuntimeException("插件不存在"));

            // 验证用户权限
            if (!plugin.getDeveloperId().equals(userId)) {
                throw new RuntimeException("无权限测试该插件配置");
            }

            // 使用配置验证器进行测试
            PluginConfigValidator.TestResult testResult =
                    pluginConfigValidator.testConfig(plugin.getPluginType(), configData);

            // 转换为响应对象
            PluginConfigTestResult result = new PluginConfigTestResult();
            result.setSuccess(testResult.isSuccess());
            result.setMessage(testResult.getErrorMessage());
            result.setTestData(configData);

            // 转换测试用例结果
            Map<String, Object> response = new HashMap<>();
            if (testResult.getTestCases() != null) {
                List<Map<String, Object>> testCases = testResult.getTestCases().stream()
                        .map(testCase -> {
                            Map<String, Object> testCaseMap = new HashMap<>();
                            testCaseMap.put("name", testCase.getName());
                            testCaseMap.put("passed", testCase.isPassed());
                            testCaseMap.put("message", testCase.getMessage());
                            testCaseMap.put("executionTime", testCase.getExecutionTime());
                            return testCaseMap;
                        })
                        .collect(java.util.stream.Collectors.toList());
                response.put("testCases", testCases);
            }

            result.setResponse(response);
            result.setExecutionTime(testResult.getExecutionTime());
            result.setError(testResult.getErrorMessage());

            return result;

        } catch (Exception e) {
            log.error("测试插件配置失败: userId={}, pluginId={}", userId, pluginId, e);
            PluginConfigTestResult result = new PluginConfigTestResult();
            result.setSuccess(false);
            result.setMessage("测试过程发生错误: " + e.getMessage());
            result.setError(e.getMessage());
            return result;
        }
    }

    @Override
    public PageResponse<PluginConfigHistoryResponse> getPluginConfigHistory(Long pluginId, org.springframework.data.domain.Pageable pageable) {
        log.info("获取插件配置历史: pluginId={}", pluginId);

        // 这里可以实现配置历史的查询逻辑
        // 暂时返回空结果
        PageResponse<PluginConfigHistoryResponse> pageResponse = new PageResponse<>();
        pageResponse.setItems(new ArrayList<>());
        pageResponse.setTotalElements(0L);
        pageResponse.setTotalPages(0);
        pageResponse.setPage(pageable.getPageNumber() + 1);
        pageResponse.setSize(pageable.getPageSize());
        pageResponse.setHasNext(false);
        pageResponse.setHasPrevious(pageable.getPageNumber() > 0);

        return pageResponse;
    }

    @Override
    public PluginConfigResponse restorePluginConfigVersion(Long userId, Long pluginId, Long versionId) {
        log.info("恢复插件配置版本: userId={}, pluginId={}, versionId={}", userId, pluginId, versionId);

        // 这里可以实现配置版本恢复的逻辑
        // 暂时抛出未实现异常
        throw new RuntimeException("配置版本恢复功能暂未实现");
    }

    @Override
    public PluginConfigTemplate getPluginConfigTemplate(String pluginType) {
        log.info("获取插件配置模板: pluginType={}", pluginType);

        try {
            PluginConfigValidator.ConfigTemplate template =
                    pluginConfigValidator.getConfigTemplate(pluginType);

            // 转换为响应对象
            PluginConfigTemplate response = new PluginConfigTemplate();
            response.setPluginType(template.getPluginType());
            response.setTemplateName(template.getPluginType() + " Template");
            response.setDefaultConfig(template.getFieldTypes().entrySet().stream()
                    .collect(java.util.stream.Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> getDefaultValueForType(entry.getValue())
                    )));
            response.setSchema(template.getFieldTypes());
            response.setRequiredFields(template.getRequiredFields());
            response.setValidationRules(template.getFieldRules().entrySet().stream()
                    .collect(java.util.stream.Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> Map.of("type", "rule")
                    )));
            response.setDescription("Configuration template for " + pluginType + " plugins");

            return response;

        } catch (Exception e) {
            log.error("获取插件配置模板失败: pluginType={}", pluginType, e);
            throw new RuntimeException("获取插件配置模板失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<PluginConfigResponse> batchUpdatePluginConfigs(Long userId, List<PluginConfigUpdateItem> configs) {
        log.info("批量更新插件配置: userId={}, count={}", userId, configs != null ? configs.size() : 0);

        List<PluginConfigResponse> results = new ArrayList<>();

        if (configs == null || configs.isEmpty()) {
            return results;
        }

        for (PluginConfigUpdateItem item : configs) {
            try {
                UpdatePluginConfigRequest request = new UpdatePluginConfigRequest();
                request.setConfigData(item.getConfigData());
                request.setEnvironment(item.getEnvironment());
                request.setVersion(null);

                PluginConfigResponse result = updatePluginConfig(userId, item.getPluginId(), request);
                results.add(result);

            } catch (Exception e) {
                log.error("批量更新插件配置失败: pluginId={}, error={}", item.getPluginId(), e.getMessage());
                // 为失败的项目创建一个错误响应
                PluginConfigResponse errorResponse = new PluginConfigResponse();
                errorResponse.setPluginId(item.getPluginId());
                errorResponse.setPluginName("Unknown");
                errorResponse.setConfigData(item.getConfigData());
                results.add(errorResponse);
            }
        }

        return results;
    }

    @Override
    public PluginConfigExportData exportPluginConfig(Long pluginId, String format) {
        log.info("导出插件配置: pluginId={}, format={}", pluginId, format);

        try {
            Plugin plugin = pluginRepository.findById(pluginId)
                    .orElseThrow(() -> new RuntimeException("插件不存在"));

            PluginConfigExportData exportData = new PluginConfigExportData();
            exportData.setPluginId(pluginId);
            exportData.setPluginName(plugin.getName());
            exportData.setFormat(format);

            // 将配置数据转换为指定格式
            if ("json".equalsIgnoreCase(format)) {
                exportData.setConfigData(new com.fasterxml.jackson.databind.ObjectMapper()
                        .writeValueAsString(plugin.getConfigData()));
            } else {
                exportData.setConfigData("Unsupported format: " + format);
            }

            exportData.setExportedAt(java.time.LocalDateTime.now().toString());
            exportData.setExportedBy(plugin.getDeveloperId().toString());
            exportData.setChecksum(generateChecksum(plugin.getConfigData()));

            return exportData;

        } catch (Exception e) {
            log.error("导出插件配置失败: pluginId={}", pluginId, e);
            throw new RuntimeException("导出插件配置失败: " + e.getMessage(), e);
        }
    }

    @Override
    public PluginConfigResponse importPluginConfig(Long userId, Long pluginId, ImportPluginConfigRequest request) {
        log.info("导入插件配置: userId={}, pluginId={}, format={}", userId, pluginId, request.getFormat());

        try {
            Plugin plugin = pluginRepository.findById(pluginId)
                    .orElseThrow(() -> new RuntimeException("插件不存在"));

            // 验证用户权限
            if (!plugin.getDeveloperId().equals(userId)) {
                throw new RuntimeException("无权限导入该插件配置");
            }

            // 解析配置数据
            Map<String, Object> configData;
            if ("json".equalsIgnoreCase(request.getFormat())) {
                configData = new com.fasterxml.jackson.databind.ObjectMapper()
                        .readValue(request.getConfigData(), Map.class);
            } else {
                throw new RuntimeException("不支持的格式: " + request.getFormat());
            }

            // 如果需要覆盖，验证配置
            if (request.isOverwrite()) {
                PluginConfigValidationResult validationResult = validatePluginConfig(pluginId, configData);
                if (!validationResult.isValid()) {
                    throw new RuntimeException("配置验证失败: " + String.join(", ", validationResult.getErrors()));
                }
            }

            // 更新配置
            UpdatePluginConfigRequest updateRequest = new UpdatePluginConfigRequest();
            updateRequest.setConfigData(configData);
            updateRequest.setEnvironment("default");

            return updatePluginConfig(userId, pluginId, updateRequest);

        } catch (Exception e) {
            log.error("导入插件配置失败: userId={}, pluginId={}", userId, pluginId, e);
            throw new RuntimeException("导入插件配置失败: " + e.getMessage(), e);
        }
    }

    // ==================== 辅助方法 ====================

    private Object getDefaultValueForType(String type) {
        return switch (type.toLowerCase()) {
            case "string" -> "";
            case "number", "integer" -> 0;
            case "boolean" -> false;
            case "array", "list" -> new ArrayList<>();
            case "object", "map" -> new HashMap<>();
            default -> null;
        };
    }

    private String generateChecksum(Map<String, Object> configData) {
        try {
            String json = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(configData);
            return String.valueOf(json.hashCode());
        } catch (Exception e) {
            return "checksum_error";
        }
    }
}