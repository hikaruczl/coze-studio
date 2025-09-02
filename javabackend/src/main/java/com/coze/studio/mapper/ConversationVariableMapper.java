package com.coze.studio.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coze.studio.entity.ConversationVariable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * ConversationVariable Mapper接口
 */
@Mapper
public interface ConversationVariableMapper extends BaseMapper<ConversationVariable> {

    /**
     * 更新访问统计信息
     * @param id 变量ID
     * @param accessTime 访问时间
     */
    void updateAccessStats(@Param("id") Long id, @Param("accessTime") LocalDateTime accessTime);

    /**
     * 根据作用域和用户ID查找变量
     * @param scope 作用域
     * @param userId 用户ID
     * @return 变量列表
     */
    List<ConversationVariable> findByScopeAndUserId(@Param("scope") String scope, @Param("userId") Long userId);

    /**
     * 根据Bot ID查找变量
     * @param botId Bot ID
     * @return 变量列表
     */
    List<ConversationVariable> findByBotId(@Param("botId") Long botId);

    /**
     * 根据对话ID查找变量
     * @param conversationId 对话ID
     * @return 变量列表
     */
    List<ConversationVariable> findByConversationId(@Param("conversationId") Long conversationId);

    /**
     * 根据会话ID查找变量
     * @param sessionId 会话ID
     * @return 变量列表
     */
    List<ConversationVariable> findBySessionId(@Param("sessionId") String sessionId);

    /**
     * 根据消息ID查找变量
     * @param messageId 消息ID
     * @return 变量列表
     */
    List<ConversationVariable> findByMessageId(@Param("messageId") Long messageId);

    /**
     * 根据作用域删除变量
     * @param scope 作用域
     * @param userId 用户ID
     */
    void deleteByScope(@Param("scope") String scope, @Param("userId") Long userId);

    /**
     * 根据Bot ID删除变量
     * @param botId Bot ID
     */
    void deleteByBotId(@Param("botId") Long botId);

    /**
     * 根据对话ID删除变量
     * @param conversationId 对话ID
     */
    void deleteByConversationId(@Param("conversationId") Long conversationId);

    /**
     * 根据会话ID删除变量
     * @param sessionId 会话ID
     */
    void deleteBySessionId(@Param("sessionId") String sessionId);

    /**
     * 根据用户ID统计变量数量
     * @param userId 用户ID
     * @return 变量数量
     */
    long countByUserId(@Param("userId") Long userId);

    /**
     * 获取变量统计信息
     * @param userId 用户ID
     * @return 统计信息Map
     */
    Map<String, Object> getVariableStatistics(@Param("userId") Long userId);

    /**
     * 删除过期变量
     * @param expireTime 过期时间
     * @return 删除的记录数
     */
    int deleteExpiredVariables(@Param("expireTime") LocalDateTime expireTime);

}