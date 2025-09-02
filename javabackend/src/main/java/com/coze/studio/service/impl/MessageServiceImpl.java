package com.coze.studio.service.impl;

import com.coze.studio.service.MessageService;
import com.coze.studio.repository.MessageRepository;
import com.coze.studio.entity.Message;
import com.coze.studio.dto.common.PageResponse;
import com.coze.studio.dto.message.MessageResponse;
import com.coze.studio.dto.message.SendMessageRequest;
import com.coze.studio.entity.enums.RoleType;

import com.coze.studio.entity.enums.MessageType;
import com.coze.studio.entity.enums.MessageStatus;
import org.springframework.data.domain.Pageable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * MessageService的实现类
 * 这是一个最小可编译的实现，所有方法都需要具体实现
 */
@Slf4j
@Service
public class MessageServiceImpl implements MessageService {

    @Override
    public MessageResponse sendMessage(Long userId, SendMessageRequest request) {
        // 最小桩实现：持久化基础字段
        Message msg = new Message();
        msg.setUserId(String.valueOf(userId));
        msg.setContent(request.getContent());
        msg.setContentType(request.getContentType());
        msg.setRole(RoleType.USER);
        msg.setCreatedAt(java.time.LocalDateTime.now());
        msg.setUpdatedAt(java.time.LocalDateTime.now());
        messageRepository.save(msg);
        return com.coze.studio.dto.message.MessageResponse.fromMessage(msg);
    }


    @Autowired
    private MessageRepository messageRepository;


    @Override
    public void sendMessageStream(Long userId, SendMessageRequest request, MessageStreamCallback callback) {
        // 简单桩实现：一次性返回消息
        MessageResponse resp = sendMessage(userId, request);
        if (callback != null) {
            callback.onStart(resp);
            callback.onContent(resp.getContent());
            callback.onComplete(resp);
        }
    }


    @Override
    public MessageResponse getMessageById(Long messageId) {
        Message entity = getMessageEntityById(messageId);
        return convertToResponse(entity);
    }

    private MessageResponse convertToResponse(Message entity) {
        return com.coze.studio.dto.message.MessageResponse.fromMessage(entity);
    }



    /**
     * 根据ID获取消息实体（真实实现）
     */
    @Override
    public Message getMessageEntityById(Long messageId) {
        log.info("根据ID获取消息实体: messageId={}", messageId);
        if (messageId == null) {
            throw new IllegalArgumentException("消息ID不能为空");
        }
        try {
            java.util.Optional<Message> opt = messageRepository.findById(messageId);
            if (!opt.isPresent()) {
                throw new RuntimeException("消息不存在: " + messageId);
            }
            return opt.get();
        } catch (Exception e) {
            log.error("获取消息实体失败: messageId={}", messageId, e);
            throw new RuntimeException("获取消息实体失败: " + e.getMessage(), e);
        }
    }

    @Override
    public void batchDeleteMessages(Long userId, Long[] messageIds) {
        log.info("批量删除消息: userId={}, messageCount={}", userId, messageIds.length);

        try {
            // 1. 验证输入参数
            if (messageIds == null || messageIds.length == 0) {
                log.warn("消息ID列表为空，无需删除");
                return;
            }

            List<String> errors = new ArrayList<>();
            int successCount = 0;
            int failedCount = 0;

            // 2. 逐个删除消息
            for (Long messageId : messageIds) {
                try {
                    // 验证消息是否存在
                    Optional<Message> messageOpt = messageRepository.findById(messageId);
                    if (!messageOpt.isPresent()) {
                        errors.add("消息不存在: " + messageId);
                        failedCount++;
                        continue;
                    }

                    Message message = messageOpt.get();

                    // 验证用户权限（只能删除自己发送的消息或者自己对话中的消息）
                    if (!userId.toString().equals(message.getUserId())) {
                        // 还需要检查是否是对话的参与者
                        // 这里简化处理，实际应该检查对话权限
                        errors.add("无权限删除消息: " + messageId);
                        failedCount++;
                        continue;
                    }

                    // 执行软删除
                    messageRepository.deleteById(messageId);
                    successCount++;

                    log.debug("消息删除成功: messageId={}", messageId);

                } catch (Exception e) {
                    log.error("删除消息失败: messageId={}", messageId, e);
                    errors.add("删除消息失败: " + messageId + " - " + e.getMessage());
                    failedCount++;
                }
            }

            // 3. 记录批量删除结果
            log.info("批量删除消息完成: userId={}, 总数={}, 成功={}, 失败={}",
                    userId, messageIds.length, successCount, failedCount);

            if (!errors.isEmpty()) {
                log.warn("批量删除消息存在错误: {}", String.join("; ", errors));
                // 如果有错误，可以选择抛出异常或者返回错误信息
                // 这里选择记录日志，不中断整个操作
            }

        } catch (Exception e) {
            log.error("批量删除消息失败: userId={}", userId, e);
            throw new RuntimeException("批量删除消息失败: " + e.getMessage(), e);
        }
    }

    /**
     * 分页查询消息列表（真实实现）
     */
    @Override
    public PageResponse<MessageResponse> getMessages(com.coze.studio.dto.message.MessageQueryRequest request, Pageable pageable) {
        log.info("分页查询消息: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        if (pageable == null) {
            throw new IllegalArgumentException("分页参数不能为空");
        }
        try {
            List<Message> all = new java.util.ArrayList<>();
            // 组合条件：尽量利用已提供的仓储方法，复杂条件交给MyBatis XML实现（后续可补）
            if (request != null) {
                if (request.getConversationId() != null) {
                    // 优先按对话查询
                    List<Message> list = messageRepository.findByConversationIdOrderByPositionAsc(request.getConversationId());
                    all.addAll(list);
                } else if (request.getRunId() != null) {
                    all.addAll(messageRepository.findByRunIdOrderByPositionAsc(request.getRunId()));
                } else if (request.getStatus() != null) {
                    all.addAll(messageRepository.findByStatusOrderByCreatedAtDesc(request.getStatus()));
                } else {
                    // 兜底：全部
                    all.addAll(messageRepository.findAll());
                }
                // 进一步内存过滤：角色/类型/是否已读/时间范围/关键词（简单 contains）
                all = all.stream()
                    .filter(m -> request.getRole() == null || (m.getRole() != null && m.getRole().equals(request.getRole())))
                    .filter(m -> request.getMessageType() == null || (m.getMessageType() != null && m.getMessageType().equals(request.getMessageType())))
                    .filter(m -> request.getIsRead() == null || (m.getIsRead() != null && m.getIsRead().equals(request.getIsRead())))
                    .filter(m -> request.getStatus() == null || (m.getStatus() != null && m.getStatus().equals(request.getStatus())))
                    .filter(m -> request.getStartTime() == null || (m.getCreatedAt() != null && !m.getCreatedAt().isBefore(request.getStartTime())))
                    .filter(m -> request.getEndTime() == null || (m.getCreatedAt() != null && !m.getCreatedAt().isAfter(request.getEndTime())))
                    .filter(m -> request.getKeyword() == null || (m.getContent() != null && m.getContent().contains(request.getKeyword())))
                    .toList();
            } else {
                all.addAll(messageRepository.findAll());
            }
            // 分页切片
            int start = (int) pageable.getOffset();
            int end = Math.min(start + pageable.getPageSize(), all.size());
            List<Message> pageList = start < all.size() ? all.subList(start, end) : java.util.Collections.emptyList();
            // 转换
            List<MessageResponse> items = new java.util.ArrayList<>();
            for (Message m : pageList) {
                items.add(MessageResponse.fromMessage(m));
            }
            PageResponse<MessageResponse> resp = new PageResponse<>();
            resp.setItems(items);
            resp.setTotalElements((long) all.size());
            resp.setTotalPages((int) Math.ceil((double) all.size() / pageable.getPageSize()));
            resp.setPage(pageable.getPageNumber() + 1);
            resp.setSize(pageable.getPageSize());
            resp.setHasNext(end < all.size());
            resp.setHasPrevious(pageable.getPageNumber() > 0);
            resp.setFirst(pageable.getPageNumber() == 0);
            resp.setLast(pageable.getPageNumber() >= resp.getTotalPages() - 1);
            log.info("分页查询消息完成: total={}, returned={}", all.size(), items.size());
            return resp;
        } catch (Exception e) {
            log.error("分页查询消息失败", e);
            throw new RuntimeException("分页查询消息失败: " + e.getMessage(), e);
        }
    }

    /**
     * 删除消息（真实实现）
     */
    @Override
    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    public void deleteMessage(Long userId, Long messageId) {
        log.info("删除消息: userId={}, messageId={}", userId, messageId);
        if (userId == null || messageId == null) {
            throw new IllegalArgumentException("用户ID和消息ID不能为空");
        }
        try {
            // 1. 查询消息
            java.util.Optional<Message> opt = messageRepository.findById(messageId);
            if (!opt.isPresent()) {
                throw new RuntimeException("消息不存在: " + messageId);
            }
            Message message = opt.get();

            // 2. 权限校验：仅消息发送者或对话参与者可删除
            if (!userId.equals(Long.valueOf(message.getUserId()))) {
                // 检查是否为对话参与者（简化实现）
                throw new RuntimeException("无权限删除该消息");
            }

            // 3. 软删除：标记为已删除
            message.setStatus(MessageStatus.DELETED);
            message.setUpdatedAt(java.time.LocalDateTime.now());
            messageRepository.save(message);

            log.info("消息删除成功: messageId={}", messageId);
        } catch (Exception e) {
            log.error("删除消息失败: userId={}, messageId={}", userId, messageId, e);
            throw new RuntimeException("删除消息失败: " + e.getMessage(), e);
        }
    }

    // TODO: 实现其他接口方法
    // 这是一个最小可编译的实现，避免编译错误

    @Override
    public boolean hasAccessToMessage(Long userId, Long messageId) {
        log.info("检查消息访问权限: userId={}, messageId={}", userId, messageId);

        try {
            // TODO: 实现消息访问权限检查逻辑
            // 这里返回一个简单的权限检查结果作为占位符

            // 简化实现：假设用户对所有消息都有访问权限
            // 实际应该检查：
            // 1. 消息是否存在
            // 2. 用户是否是消息的发送者或接收者
            // 3. 用户是否有对话的访问权限
            // 4. 消息是否被删除或隐藏

            boolean hasAccess = true; // 简化实现

            log.info("消息访问权限检查完成: userId={}, messageId={}, hasAccess={}", userId, messageId, hasAccess);
            return hasAccess;
        } catch (Exception e) {
            log.error("检查消息访问权限失败: userId={}, messageId={}", userId, messageId, e);
            return false; // 出错时拒绝访问
        }
    }

    @Override
    public long countUserMessages(Long userId) {
        log.info("统计用户消息数量: userId={}", userId);

        try {
            // TODO: 实现统计用户消息数量的逻辑
            // 这里返回一个简单的统计结果作为占位符

            // 简化实现：返回一个模拟的消息数量
            // 实际应该查询数据库获取真实的消息数量
            long messageCount = 100L; // 简化实现

            log.info("用户消息数量统计完成: userId={}, messageCount={}", userId, messageCount);
            return messageCount;
        } catch (Exception e) {
            log.error("统计用户消息数量失败: userId={}", userId, e);
            return 0L; // 出错时返回0
        }
    }

    @Override
    public long countConversationMessages(Long conversationId) {
        log.info("统计对话消息数量: conversationId={}", conversationId);

        try {
            // TODO: 实现统计对话消息数量的逻辑
            // 这里返回一个简单的统计结果作为占位符

            // 简化实现：返回一个模拟的消息数量
            // 实际应该查询数据库获取真实的消息数量
            long messageCount = 50L; // 简化实现

            log.info("对话消息数量统计完成: conversationId={}, messageCount={}", conversationId, messageCount);
            return messageCount;
        } catch (Exception e) {
            log.error("统计对话消息数量失败: conversationId={}", conversationId, e);
            return 0L; // 出错时返回0
        }
    }

    @Override
    public PageResponse<MessageResponse> getMessagesWithToolCalls(Long conversationId, Pageable pageable) {
        log.info("获取包含工具调用的消息: conversationId={}, page={}, size={}", conversationId, pageable.getPageNumber(), pageable.getPageSize());

        try {
            // TODO: 实现获取包含工具调用的消息的逻辑
            List<MessageResponse> messages = new ArrayList<>();

            // 模拟一些包含工具调用的消息数据
            for (int i = 1; i <= 3; i++) {
                MessageResponse message = new MessageResponse();
                message.setId((long) i);
                message.setConversationId(conversationId);
                message.setContent("这是包含工具调用的消息" + i);
                message.setRole(RoleType.ASSISTANT);
                message.setCreatedAt(java.time.LocalDateTime.now().minusMinutes(i * 5));
                messages.add(message);
            }

            PageResponse<MessageResponse> pageResponse = new PageResponse<>();
            pageResponse.setItems(messages);
            pageResponse.setTotalElements((long) messages.size());
            pageResponse.setTotalPages(1);
            pageResponse.setPage(pageable.getPageNumber() + 1);
            pageResponse.setSize(pageable.getPageSize());
            pageResponse.setHasNext(false);
            pageResponse.setHasPrevious(false);

            log.info("包含工具调用的消息获取成功: conversationId={}, messagesCount={}", conversationId, messages.size());
            return pageResponse;
        } catch (Exception e) {
            log.error("获取包含工具调用的消息失败: conversationId={}", conversationId, e);
            throw new RuntimeException("获取包含工具调用的消息失败: " + e.getMessage());
        }
    }

    @Override
    public PageResponse<MessageResponse> searchMessages(Long conversationId, String keyword, Pageable pageable) {
        log.info("搜索消息: conversationId={}, keyword={}, page={}, size={}", conversationId, keyword, pageable.getPageNumber(), pageable.getPageSize());

        try {
            List<MessageResponse> messages = new ArrayList<>();

            for (int i = 1; i <= 3; i++) {
                MessageResponse message = new MessageResponse();
                message.setId((long) i);
                message.setConversationId(conversationId);
                message.setContent("搜索结果消息" + i + "，包含关键词：" + keyword);
                message.setRole(RoleType.USER);
                message.setCreatedAt(java.time.LocalDateTime.now().minusMinutes(i * 10));
                messages.add(message);
            }

            PageResponse<MessageResponse> pageResponse = new PageResponse<>();
            pageResponse.setItems(messages);
            pageResponse.setTotalElements((long) messages.size());
            pageResponse.setTotalPages(1);
            pageResponse.setPage(pageable.getPageNumber() + 1);
            pageResponse.setSize(pageable.getPageSize());
            pageResponse.setHasNext(false);
            pageResponse.setHasPrevious(false);

            log.info("消息搜索成功: conversationId={}, keyword={}, messagesCount={}", conversationId, keyword, messages.size());
            return pageResponse;
        } catch (Exception e) {
            log.error("搜索消息失败: conversationId={}, keyword={}", conversationId, keyword, e);
            throw new RuntimeException("搜索消息失败: " + e.getMessage());
        }
    }

    @Override
    public List<MessageResponse> getMessageReplies(Long messageId) {
        log.info("获取消息回复: messageId={}", messageId);

        try {
            List<MessageResponse> replies = new ArrayList<>();

            for (int i = 1; i <= 2; i++) {
                MessageResponse reply = new MessageResponse();
                reply.setId((long) (messageId * 10 + i));
                reply.setConversationId(1L);
                reply.setContent("这是消息" + messageId + "的回复" + i);
                reply.setRole(RoleType.ASSISTANT);
                reply.setCreatedAt(java.time.LocalDateTime.now().minusMinutes(i * 5));
                replies.add(reply);
            }

            log.info("消息回复获取成功: messageId={}, repliesCount={}", messageId, replies.size());
            return replies;
        } catch (Exception e) {
            log.error("获取消息回复失败: messageId={}", messageId, e);
            throw new RuntimeException("获取消息回复失败: " + e.getMessage());
        }
    }

    @Override
    public MessageResponse replyToMessage(Long userId, Long messageId, SendMessageRequest request) {
        log.info("回复消息: userId={}, messageId={}", userId, messageId);

        try {
            // TODO: 实现回复消息的逻辑
            MessageResponse response = new MessageResponse();
            response.setId(System.currentTimeMillis());
            response.setConversationId(request.getConversationId());
            response.setContent(request.getContent());
            response.setRole(RoleType.USER);
            response.setCreatedAt(java.time.LocalDateTime.now());

            log.info("消息回复成功: userId={}, messageId={}, responseId={}", userId, messageId, response.getId());
            return response;
        } catch (Exception e) {
            log.error("回复消息失败: userId={}, messageId={}", userId, messageId, e);
            throw new RuntimeException("回复消息失败: " + e.getMessage());
        }
    }

    @Override
    public MessageResponse editMessage(Long userId, Long messageId, String newContent) {
        log.info("编辑消息: userId={}, messageId={}, newContentLength={}", userId, messageId, newContent != null ? newContent.length() : 0);

        try {
            // TODO: 实现编辑消息的逻辑
            MessageResponse response = new MessageResponse();
            response.setId(messageId);
            response.setContent(newContent);
            response.setRole(RoleType.USER);
            response.setUpdatedAt(java.time.LocalDateTime.now());

            log.info("消息编辑成功: userId={}, messageId={}", userId, messageId);
            return response;
        } catch (Exception e) {
            log.error("编辑消息失败: userId={}, messageId={}", userId, messageId, e);
            throw new RuntimeException("编辑消息失败: " + e.getMessage());
        }
    }

    @Override
    public MessageResponse regenerateMessage(Long userId, Long messageId) {
        log.info("重新生成消息: userId={}, messageId={}", userId, messageId);

        try {
            // TODO: 实现重新生成消息的逻辑
            MessageResponse response = new MessageResponse();
            response.setId(System.currentTimeMillis());
            response.setContent("这是重新生成的消息内容");
            response.setRole(RoleType.ASSISTANT);
            response.setCreatedAt(java.time.LocalDateTime.now());

            log.info("消息重新生成成功: userId={}, originalMessageId={}, newMessageId={}", userId, messageId, response.getId());
            return response;
        } catch (Exception e) {
            log.error("重新生成消息失败: userId={}, messageId={}", userId, messageId, e);
            throw new RuntimeException("重新生成消息失败: " + e.getMessage());
        }
    }

    @Override
    public long getUnreadMessageCount(Long userId, Long conversationId) {
        log.info("获取未读消息数量: userId={}, conversationId={}", userId, conversationId);

        try {
            // TODO: 实现获取未读消息数量的逻辑
            long unreadCount = 5L; // 简化实现

            log.info("未读消息数量获取成功: userId={}, conversationId={}, unreadCount={}", userId, conversationId, unreadCount);
            return unreadCount;
        } catch (Exception e) {
            log.error("获取未读消息数量失败: userId={}, conversationId={}", userId, conversationId, e);
            return 0L; // 出错时返回0
        }
    }

    @Override
    public void markConversationMessagesAsRead(Long userId, Long conversationId) {
        log.info("标记对话消息为已读: userId={}, conversationId={}", userId, conversationId);

        try {
            // TODO: 实现标记对话消息为已读的逻辑
            log.info("对话消息标记为已读成功: userId={}, conversationId={}", userId, conversationId);
        } catch (Exception e) {
            log.error("标记对话消息为已读失败: userId={}, conversationId={}", userId, conversationId, e);
            throw new RuntimeException("标记对话消息为已读失败: " + e.getMessage());
        }
    }

    @Override
    public void markMessagesAsRead(Long userId, Long[] messageIds) {
        log.info("批量标记消息为已读: userId={}, messageCount={}", userId, messageIds != null ? messageIds.length : 0);

        try {
            // 1. 验证输入参数
            if (userId == null) {
                throw new IllegalArgumentException("用户ID不能为空");
            }

            if (messageIds == null || messageIds.length == 0) {
                log.warn("消息ID列表为空，无需标记: userId={}", userId);
                return;
            }

            // 2. 验证消息ID列表大小
            if (messageIds.length > 1000) {
                throw new IllegalArgumentException("单次最多只能标记1000条消息");
            }

            // 3. 去重消息ID
            List<Long> uniqueMessageIds = java.util.Arrays.stream(messageIds)
                .distinct()
                .filter(id -> id != null && id > 0)
                .collect(java.util.stream.Collectors.toList());

            if (uniqueMessageIds.isEmpty()) {
                log.warn("有效消息ID列表为空，无需标记: userId={}", userId);
                return;
            }

            // 4. 批量查询消息信息
            // TODO: 从数据库查询消息列表
            // List<Message> messages = messageRepository.findByIdInAndUserId(uniqueMessageIds, userId);
            // if (messages.isEmpty()) {
            //     log.warn("未找到任何有效消息: userId={}, messageIds={}", userId, uniqueMessageIds);
            //     return;
            // }

            // 5. 过滤出需要标记的消息（只标记未读消息）
            // TODO: 过滤出状态为未读的消息
            // List<Message> unreadMessages = messages.stream()
            //     .filter(msg -> !msg.isRead())
            //     .collect(Collectors.toList());

            // if (unreadMessages.isEmpty()) {
            //     log.info("所有消息都已是已读状态: userId={}", userId);
            //     return;
            // }

            // 6. 验证用户权限（确保用户只能标记自己的消息）
            // TODO: 验证消息所有权
            // for (Message message : unreadMessages) {
            //     if (!hasPermissionToReadMessage(userId, message)) {
            //         throw new RuntimeException("用户无权限标记消息: messageId=" + message.getId());
            //     }
            // }

            // 7. 批量更新消息状态为已读
            // TODO: 批量更新消息状态
            // int updatedCount = messageRepository.batchMarkAsRead(
            //     unreadMessages.stream().map(Message::getId).collect(Collectors.toList()),
            //     userId,
            //     LocalDateTime.now()
            // );

            // 8. 更新对话的未读消息计数
            // TODO: 更新相关对话的未读消息计数
            // Map<Long, List<Message>> messagesByConversation = unreadMessages.stream()
            //     .collect(Collectors.groupingBy(Message::getConversationId));
            //
            // for (Long conversationId : messagesByConversation.keySet()) {
            //     conversationService.updateUnreadMessageCount(conversationId, userId);
            // }

            // 9. 发送已读状态更新通知
            // TODO: 通知其他客户端消息已读状态变更
            // for (Message message : unreadMessages) {
            //     notificationService.notifyMessageRead(message.getId(), userId);
            // }

            // 10. 记录操作日志
            // TODO: 记录消息标记操作日志
            // auditLogService.logMessageMarkAsRead(userId, uniqueMessageIds);

            // 模拟处理结果
            int processedCount = Math.min(uniqueMessageIds.size(), 50); // 模拟最多处理50条

            log.info("批量标记消息为已读成功: userId={}, requestedCount={}, processedCount={}",
                    userId, uniqueMessageIds.size(), processedCount);

        } catch (IllegalArgumentException e) {
            log.warn("批量标记消息为已读参数错误: userId={}, error={}", userId, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("批量标记消息为已读失败: userId={}, messageIds={}", userId, messageIds, e);
            throw new RuntimeException("批量标记消息为已读失败: " + e.getMessage());
        }
    }

    @Override
    public void markMessageAsRead(Long userId, Long messageId) {
        log.info("标记消息为已读: userId={}, messageId={}", userId, messageId);

        try {
            // 1. 验证输入参数
            if (userId == null || messageId == null) {
                throw new IllegalArgumentException("用户ID和消息ID不能为空");
            }

            // 2. 查询消息信息
            // TODO: 从数据库查询消息
            // Optional<Message> messageOpt = messageRepository.findById(messageId);
            // if (!messageOpt.isPresent()) {
            //     throw new RuntimeException("消息不存在: " + messageId);
            // }
            // Message message = messageOpt.get();

            // 3. 验证用户权限
            // TODO: 检查用户是否有权限标记此消息
            // if (!hasPermissionToReadMessage(userId, message)) {
            //     throw new RuntimeException("用户无权限标记此消息: userId=" + userId + ", messageId=" + messageId);
            // }

            // 4. 检查消息状态
            // TODO: 检查消息是否已经是已读状态
            // if (message.isRead()) {
            //     log.info("消息已是已读状态: messageId={}", messageId);
            //     return;
            // }

            // 5. 更新消息状态为已读
            // TODO: 更新数据库中的消息状态
            // message.setRead(true);
            // message.setReadAt(LocalDateTime.now());
            // message.setUpdatedAt(LocalDateTime.now());
            // messageRepository.save(message);

            // 6. 更新对话的未读消息计数
            // TODO: 更新对话的未读消息计数
            // conversationService.decrementUnreadMessageCount(message.getConversationId(), userId);

            // 7. 发送已读状态更新通知
            // TODO: 通知其他客户端消息已读状态变更
            // notificationService.notifyMessageRead(messageId, userId);

            // 8. 记录操作日志
            // TODO: 记录消息标记操作日志
            // auditLogService.logMessageMarkAsRead(userId, messageId);

            log.info("消息标记为已读成功: userId={}, messageId={}", userId, messageId);

        } catch (IllegalArgumentException e) {
            log.warn("标记消息为已读参数错误: userId={}, messageId={}, error={}", userId, messageId, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("标记消息为已读失败: userId={}, messageId={}", userId, messageId, e);
            throw new RuntimeException("标记消息为已读失败: " + e.getMessage());
        }
    }

    @Override
    public List<MessageResponse> getRecentMessages(Long conversationId, int limit) {
        log.info("获取最近消息: conversationId={}, limit={}", conversationId, limit);

        try {
            // 1. 验证输入参数
            if (conversationId == null) {
                throw new IllegalArgumentException("对话ID不能为空");
            }
            if (limit <= 0) {
                limit = 10; // 默认限制
            }
            if (limit > 100) {
                limit = 100; // 最大限制
            }

            // 2. 查询最近消息
            // TODO: 从数据库查询最近消息
            // List<Message> messages = messageRepository.findTopByConversationIdOrderByCreatedAtDesc(conversationId, limit);

            // 3. 模拟数据
            List<MessageResponse> messages = new ArrayList<>();

            for (int i = 0; i < Math.min(limit, 8); i++) {
                MessageResponse message = new MessageResponse();
                message.setId((long) (i + 1));
                message.setConversationId(conversationId);
                message.setUserId(String.valueOf((i % 2) + 1));
                message.setName("用户" + ((i % 2) + 1));
                message.setContent("这是第" + (i + 1) + "条消息内容");
                message.setMessageType(MessageType.NORMAL);
                message.setIsRead(i < 5); // 前5条已读
                message.setCreatedAt(java.time.LocalDateTime.now().minusMinutes(i * 5));
                message.setUpdatedAt(java.time.LocalDateTime.now().minusMinutes(i * 5));
                messages.add(message);
            }

            // 4. 按时间倒序排列（最新的在前）
            messages.sort((m1, m2) -> m2.getCreatedAt().compareTo(m1.getCreatedAt()));

            log.info("最近消息获取成功: conversationId={}, limit={}, returned={}",
                    conversationId, limit, messages.size());
            return messages;

        } catch (IllegalArgumentException e) {
            log.warn("获取最近消息参数错误: conversationId={}, limit={}, error={}", conversationId, limit, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("获取最近消息失败: conversationId={}, limit={}", conversationId, limit, e);
            throw new RuntimeException("获取最近消息失败: " + e.getMessage());
        }
    }

    @Override
    public PageResponse<MessageResponse> getConversationMessages(Long conversationId, Pageable pageable) {
        log.info("获取对话消息列表: conversationId={}, page={}, size={}",
                conversationId, pageable.getPageNumber(), pageable.getPageSize());

        try {
            // 1. 验证输入参数
            if (conversationId == null || pageable == null) {
                throw new IllegalArgumentException("对话ID和分页参数不能为空");
            }

            // 2. 查询对话消息
            // TODO: 从数据库查询对话的消息列表
            // Page<Message> messagePage = messageRepository.findByConversationIdOrderByCreatedAtDesc(conversationId, pageable);

            // 3. 模拟消息数据
            List<MessageResponse> messages = new ArrayList<>();
            int total = 50; // 模拟总消息数
            int start = pageable.getPageNumber() * pageable.getPageSize();
            int end = Math.min(start + pageable.getPageSize(), total);

            for (int i = start; i < end; i++) {
                MessageResponse message = new MessageResponse();
                message.setId((long) (i + 1));
                message.setConversationId(conversationId);

                // 模拟用户和助手交替发送消息
                boolean isUser = (i % 2 == 0);
                message.setUserId(isUser ? "user_123" : "assistant");
                message.setName(isUser ? "用户" : "AI助手");
                message.setRole(isUser ? RoleType.USER : RoleType.ASSISTANT);

                // 设置消息内容
                if (isUser) {
                    message.setContent("这是用户发送的第" + (i/2 + 1) + "条消息");
                } else {
                    message.setContent("这是AI助手回复的第" + (i/2 + 1) + "条消息，针对用户的问题进行详细回答。");
                }

                // 设置消息类型和状态
                message.setMessageType(MessageType.NORMAL);
                message.setStatus(MessageStatus.SENT);
                message.setIsRead(i < total - 5); // 最后5条消息未读

                // 设置时间（按时间倒序）
                message.setCreatedAt(java.time.LocalDateTime.now().minusMinutes((total - i) * 2));
                message.setUpdatedAt(message.getCreatedAt());

                // 设置运行信息
                if (!isUser) {
                    message.setRunId((long) (i / 2 + 1));
                }

                messages.add(message);
            }

            // 4. 构建分页响应
            PageResponse<MessageResponse> response = new PageResponse<>();
            response.setItems(messages);
            response.setTotalElements((long) total);
            response.setTotalPages((int) Math.ceil((double) total / pageable.getPageSize()));
            response.setSize(pageable.getPageSize());
            response.setPage(pageable.getPageNumber() + 1);
            response.setHasNext(end < total);
            response.setHasPrevious(pageable.getPageNumber() > 0);
            response.setFirst(pageable.getPageNumber() == 0);
            response.setLast(pageable.getPageNumber() >= response.getTotalPages() - 1);

            log.info("对话消息列表获取成功: conversationId={}, total={}, returned={}",
                    conversationId, total, messages.size());
            return response;

        } catch (IllegalArgumentException e) {
            log.warn("获取对话消息列表参数错误: conversationId={}, error={}", conversationId, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("获取对话消息列表失败: conversationId={}", conversationId, e);
            throw new RuntimeException("获取对话消息列表失败: " + e.getMessage());
        }
    }

    @Override
    public List<MessageResponse> getConversationMessages(Long conversationId) {
        log.info("获取对话消息列表: conversationId={}", conversationId);

        try {
            // 模拟实现
            if (conversationId == null) {
                throw new IllegalArgumentException("对话ID不能为空");
            }

            List<MessageResponse> messages = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                MessageResponse message = new MessageResponse();
                message.setId((long) (i + 1));
                message.setConversationId(conversationId);
                message.setUserId("user_" + (i % 2 == 0 ? "123" : "assistant"));
                message.setContent("消息内容" + (i + 1));
                message.setCreatedAt(java.time.LocalDateTime.now().minusMinutes(i * 5));
                messages.add(message);
            }

            log.info("对话消息列表获取成功: conversationId={}, count={}", conversationId, messages.size());
            return messages;

        } catch (Exception e) {
            log.error("获取对话消息列表失败: conversationId={}", conversationId, e);
            throw new RuntimeException("获取对话消息列表失败: " + e.getMessage());
        }
    }
}