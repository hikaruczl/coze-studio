package com.coze.studio.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coze.studio.entity.WorkflowVariable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * WorkflowVariable Mapper接口
 */
@Mapper
public interface WorkflowVariableMapper extends BaseMapper<WorkflowVariable> {

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
    List<WorkflowVariable> findByScopeAndUserId(@Param("scope") String scope, @Param("userId") Long userId);

    /**
     * 根据工作流ID查找变量
     * @param workflowId 工作流ID
     * @return 变量列表
     */
    List<WorkflowVariable> findByWorkflowId(@Param("workflowId") Long workflowId);

    /**
     * 根据执行ID查找变量
     * @param executionId 执行ID
     * @return 变量列表
     */
    List<WorkflowVariable> findByExecutionId(@Param("executionId") String executionId);

    /**
     * 根据会话ID查找变量
     * @param sessionId 会话ID
     * @return 变量列表
     */
    List<WorkflowVariable> findBySessionId(@Param("sessionId") String sessionId);

    /**
     * 根据节点ID查找变量
     * @param nodeId 节点ID
     * @return 变量列表
     */
    List<WorkflowVariable> findByNodeId(@Param("nodeId") String nodeId);

    /**
     * 根据作用域删除变量
     * @param scope 作用域
     * @param userId 用户ID
     */
    void deleteByScope(@Param("scope") String scope, @Param("userId") Long userId);

    /**
     * 根据工作流ID删除变量
     * @param workflowId 工作流ID
     */
    void deleteByWorkflowId(@Param("workflowId") Long workflowId);

    /**
     * 根据执行ID删除变量
     * @param executionId 执行ID
     */
    void deleteByExecutionId(@Param("executionId") String executionId);

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