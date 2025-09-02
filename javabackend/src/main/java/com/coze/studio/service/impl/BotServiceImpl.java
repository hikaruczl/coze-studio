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

package com.coze.studio.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.coze.studio.constant.CommonConstants;
import com.coze.studio.dto.bot.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import com.coze.studio.dto.common.PageResponse;
import com.coze.studio.entity.Bot;
import com.coze.studio.entity.User;
import com.coze.studio.entity.enums.PublishStatus;
import com.coze.studio.exception.BusinessException;
import com.coze.studio.exception.ErrorCode;
import com.coze.studio.exception.ResourceNotFoundException;
import com.coze.studio.mapper.BotMapper;
import com.coze.studio.repository.BotRepository;
import com.coze.studio.service.BotService;
import com.coze.studio.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Bot 服务实现类
 * 
 * @author coze-dev
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BotServiceImpl implements BotService {

    private final BotRepository botRepository;
    private final BotMapper botMapper;
    private final UserService userService;

    @Override
    @Transactional
    public BotResponse createBot(Long userId, CreateBotRequest request) {
        log.info("创建Bot: userId={}, name={}", userId, request.getName());

        // 验证用户存在
        User user = userService.getUserById(userId);

        // 检查Bot名称是否已存在
        if (!isBotNameAvailable(userId, request.getName())) {
            throw new BusinessException(ErrorCode.BOT_ALREADY_EXISTS, "Bot名称已存在");
        }

        // 创建Bot实体
        Bot bot = new Bot();
        bot.setName(request.getName());
        bot.setDescription(request.getDescription());
        bot.setIconUri(StringUtils.hasText(request.getIconUri()) ? 
                      request.getIconUri() : CommonConstants.DEFAULT_BOT_ICON);
        bot.setOwnerId(userId);
        bot.setSpaceId(request.getSpaceId());
        bot.setConnectorIds(request.getConnectorIds() != null ? 
                           request.getConnectorIds() : new ArrayList<>());
        bot.setScene(request.getScene());
        bot.setVersion("1.0.0");
        bot.setVersionDesc(request.getVersionDesc());
        bot.setPublishStatus(PublishStatus.UNPUBLISHED);
        bot.setEnabled(true);

        botMapper.insert(bot);
        log.info("Bot创建成功: id={}, name={}", bot.getId(), bot.getName());

        return BotResponse.fromBot(bot, user.getUsername());
    }

    @Override
    @Transactional
    public BotResponse updateBot(Long userId, Long botId, UpdateBotRequest request) {
        log.info("更新Bot: userId={}, botId={}", userId, botId);

        Bot bot = getBotEntityById(botId);

        // 检查权限
        if (!isBotOwner(userId, botId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "无权限修改此Bot");
        }

        // 检查Bot名称是否已存在（排除当前Bot）
        if (StringUtils.hasText(request.getName()) && !request.getName().equals(bot.getName())) {
            if (!isBotNameAvailable(userId, request.getName())) {
                throw new BusinessException(ErrorCode.BOT_ALREADY_EXISTS, "Bot名称已存在");
            }
            bot.setName(request.getName());
        }

        // 更新其他字段
        if (StringUtils.hasText(request.getDescription())) {
            bot.setDescription(request.getDescription());
        }
        if (StringUtils.hasText(request.getIconUri())) {
            bot.setIconUri(request.getIconUri());
        }
        if (request.getConnectorIds() != null) {
            bot.setConnectorIds(request.getConnectorIds());
        }
        if (StringUtils.hasText(request.getScene())) {
            bot.setScene(request.getScene());
        }
        if (StringUtils.hasText(request.getVersionDesc())) {
            bot.setVersionDesc(request.getVersionDesc());
        }
        if (request.getEnabled() != null) {
            bot.setEnabled(request.getEnabled());
        }

        botMapper.updateById(bot);
        log.info("Bot更新成功: id={}, name={}", bot.getId(), bot.getName());

        User user = userService.getUserById(userId);
        return BotResponse.fromBot(bot, user.getUsername());
    }

    @Override
    @Transactional(readOnly = true)
    public BotResponse getBotById(Long botId) {
        Bot bot = getBotEntityById(botId);
        User owner = userService.getUserById(bot.getOwnerId());
        return BotResponse.fromBot(bot, owner.getUsername());
    }

    @Override
    @Transactional(readOnly = true)
    public Bot getBotEntityById(Long botId) {
        Bot bot = botMapper.selectById(botId);
        if (bot == null) {
            throw ResourceNotFoundException.of("Bot", botId);
        }
        return bot;
    }

    @Override
    @Transactional
    public void deleteBot(Long userId, Long botId) {
        log.info("删除Bot: userId={}, botId={}", userId, botId);

        Bot bot = getBotEntityById(botId);

        // 检查权限
        if (!isBotOwner(userId, botId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "无权限删除此Bot");
        }

        // 软删除
        bot.setEnabled(false);
        botMapper.updateById(bot);

        log.info("Bot删除成功: id={}, name={}", bot.getId(), bot.getName());
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<BotResponse> getBots(BotQueryRequest request, Pageable pageable) {
        // TODO: 实现复杂查询逻辑
        // 这里应该根据查询条件构建动态查询
        Page<Bot> botPage = botMapper.selectPage(
            new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize()),
            new QueryWrapper<>()
        );

        List<BotResponse> responseList = botPage.getRecords().stream()
            .map(bot -> {
                User owner = userService.getUserById(bot.getOwnerId());
                return BotResponse.fromBot(bot, owner.getUsername());
            })
            .collect(java.util.stream.Collectors.toList());

        return PageResponse.of(responseList);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<BotResponse> getUserBots(Long userId, Pageable pageable) {
        Page<Bot> botPage = botMapper.selectPage(
            new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize()),
            new QueryWrapper<Bot>().eq("owner_id", userId)
        );

        List<BotResponse> responseList = botPage.getRecords().stream()
            .map(bot -> {
                User owner = userService.getUserById(bot.getOwnerId());
                return BotResponse.fromBot(bot, owner.getUsername());
            })
            .collect(Collectors.toList());

        return PageResponse.of(responseList);
    }

    @Override
    @Transactional
    public BotResponse publishBot(Long userId, Long botId, PublishBotRequest request) {
        log.info("发布Bot: userId={}, botId={}, version={}", userId, botId, request.getVersion());

        Bot bot = getBotEntityById(botId);

        // 检查权限
        if (!isBotOwner(userId, botId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "无权限发布此Bot");
        }

        // 检查Bot是否已启用
        if (!bot.getEnabled()) {
            throw new BusinessException(ErrorCode.BOT_PUBLISH_FAILED, "Bot已禁用，无法发布");
        }

        // 更新发布信息
        bot.setVersion(request.getVersion());
        bot.setVersionDesc(request.getVersionDesc());
        bot.setPublishStatus(PublishStatus.PUBLISHED);
        bot.setPublishedAt(LocalDateTime.now());

        botMapper.updateById(bot);
        log.info("Bot发布成功: id={}, version={}", bot.getId(), bot.getVersion());

        User owner = userService.getUserById(userId);
        return BotResponse.fromBot(bot, owner.getUsername());
    }

    @Override
    @Transactional
    public BotResponse unpublishBot(Long userId, Long botId) {
        log.info("下线Bot: userId={}, botId={}", userId, botId);

        Bot bot = getBotEntityById(botId);

        // 检查权限
        if (!isBotOwner(userId, botId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "无权限下线此Bot");
        }

        // 更新发布状态
        bot.setPublishStatus(PublishStatus.OFFLINE);

        botMapper.updateById(bot);
        log.info("Bot下线成功: id={}", bot.getId());

        User owner = userService.getUserById(userId);
        return BotResponse.fromBot(bot, owner.getUsername());
    }

    @Override
    @Transactional
    public BotResponse toggleBotStatus(Long userId, Long botId, boolean enabled) {
        log.info("切换Bot状态: userId={}, botId={}, enabled={}", userId, botId, enabled);

        Bot bot = getBotEntityById(botId);

        // 检查权限
        if (!isBotOwner(userId, botId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "无权限修改此Bot状态");
        }

        bot.setEnabled(enabled);

        // 如果禁用Bot，同时下线
        if (!enabled && bot.getPublishStatus() == PublishStatus.PUBLISHED) {
            bot.setPublishStatus(PublishStatus.OFFLINE);
        }

        botMapper.updateById(bot);
        log.info("Bot状态切换成功: id={}, enabled={}", bot.getId(), enabled);

        User owner = userService.getUserById(userId);
        return BotResponse.fromBot(bot, owner.getUsername());
    }

    @Override
    @Transactional
    public BotResponse cloneBot(Long userId, Long botId, String newName) {
        log.info("复制Bot: userId={}, botId={}, newName={}", userId, botId, newName);

        Bot originalBot = getBotEntityById(botId);

        // 检查访问权限
        if (!hasAccessToBot(userId, botId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "无权限访问此Bot");
        }

        // 检查新名称是否可用
        if (!isBotNameAvailable(userId, newName)) {
            throw new BusinessException(ErrorCode.BOT_ALREADY_EXISTS, "Bot名称已存在");
        }

        // 创建新Bot
        Bot newBot = new Bot();
        BeanUtils.copyProperties(originalBot, newBot, "id", "name", "ownerId", "createdAt", "updatedAt");
        newBot.setName(newName);
        newBot.setOwnerId(userId);
        newBot.setPublishStatus(PublishStatus.UNPUBLISHED);
        newBot.setPublishedAt(null);
        newBot.setPublishRecordId(null);
        newBot.setVersion("1.0.0");

        botMapper.insert(newBot);
        log.info("Bot复制成功: originalId={}, newId={}, newName={}", botId, newBot.getId(), newName);

        User owner = userService.getUserById(userId);
        return BotResponse.fromBot(newBot, owner.getUsername());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isBotNameAvailable(Long userId, String name) {
        return true; // TODO: 实现MyBatis查询方法
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasAccessToBot(Long userId, Long botId) {
        Bot bot = getBotEntityById(botId);

        // 所有者有访问权限
        if (bot.getOwnerId().equals(userId)) {
            return true;
        }

        // 已发布的Bot所有人都可以访问
        if (bot.getPublishStatus() == PublishStatus.PUBLISHED && bot.getEnabled()) {
            return true;
        }

        // TODO: 实现更复杂的权限控制（如团队成员、协作者等）

        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isBotOwner(Long userId, Long botId) {
        Bot bot = getBotEntityById(botId);
        return bot.getOwnerId().equals(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<BotResponse> getPublishedBots(Pageable pageable) {
        Page<Bot> botPage = botMapper.selectPage(
            new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize()),
            new QueryWrapper<Bot>().eq("publish_status", PublishStatus.PUBLISHED)
        );

        List<BotResponse> responseList = botPage.getRecords().stream()
            .map(bot -> {
                User owner = userService.getUserById(bot.getOwnerId());
                return BotResponse.fromBot(bot, owner.getUsername());
            })
            .collect(Collectors.toList());

        return PageResponse.of(responseList);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<BotResponse> getBotsByConnector(Long connectorId, Pageable pageable) {
        Page<Bot> botPage = botMapper.selectPage(
            new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize()),
            new QueryWrapper<Bot>().eq("connector_id", connectorId)
        );

        List<BotResponse> responseList = botPage.getRecords().stream()
            .map(bot -> {
                User owner = userService.getUserById(bot.getOwnerId());
                return BotResponse.fromBot(bot, owner.getUsername());
            })
            .collect(Collectors.toList());

        return PageResponse.of(responseList);
    }

    @Override
    @Transactional(readOnly = true)
    public long countUserBots(Long userId) {
        return 0L; // TODO: 实现MyBatis查询方法
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<BotResponse> getPopularBots(Pageable pageable) {
        // TODO: 实现基于使用量、评分等的热门Bot排序
        // 目前先返回已发布的Bot
        return getPublishedBots(pageable);
    }
}
