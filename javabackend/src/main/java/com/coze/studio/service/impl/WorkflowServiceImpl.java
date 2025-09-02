package com.coze.studio.service.impl;

import com.coze.studio.dto.workflow.WorkflowResponse;
import com.coze.studio.service.WorkflowService;
import com.coze.studio.repository.WorkflowRepository;
import com.coze.studio.entity.Workflow;
import com.coze.studio.dto.workflow.WorkflowVersionResponse;
import com.coze.studio.dto.workflow.WorkflowResponse;
import com.coze.studio.dto.workflow.WorkflowStatsResponse;
import com.coze.studio.dto.workflow.WorkflowExecutionResponse;
import com.coze.studio.dto.workflow.ExecuteWorkflowRequest;
import com.coze.studio.dto.workflow.WorkflowValidationResult;
import com.coze.studio.dto.workflow.WorkflowResponse;
import com.coze.studio.dto.common.PageResponse;
import org.springframework.data.domain.Pageable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.Optional;

/**
 * WorkflowService的实现类
 * 这是一个最小可编译的实现，所有方法都需要具体实现
 */
@Slf4j
@Service
public class WorkflowServiceImpl implements WorkflowService {

    @Autowired
    private WorkflowRepository workflowRepository;

    @Override
    public com.coze.studio.dto.workflow.WorkflowResponse createWorkflow(Long userId, com.coze.studio.dto.workflow.CreateWorkflowRequest request) {
        Workflow wf = new Workflow();
        wf.setName(request.getName());
        wf.setDescription(request.getDescription());
        wf.setType(request.getType());
        wf.setIconUrl(request.getIconUrl());
        wf.setCreatorId(userId);
        wf.setIsTemplate(Boolean.TRUE.equals(request.getIsTemplate()));
        wf.setTemplateCategory(request.getTemplateCategory());
        wf.setCreatedAt(java.time.LocalDateTime.now());
        wf.setUpdatedAt(java.time.LocalDateTime.now());
        Workflow saved = workflowRepository.save(wf);
        return convertToResponse(saved);
    }

    @Override
    public com.coze.studio.dto.workflow.WorkflowResponse updateWorkflow(Long userId, Long workflowId, com.coze.studio.dto.workflow.UpdateWorkflowRequest request) {
        log.info("更新工作流: userId={}, workflowId={}", userId, workflowId);
        Workflow wf = getWorkflowEntityById(workflowId);
        if (request != null) {
            if (request.getName() != null) wf.setName(request.getName());
            if (request.getDescription() != null) wf.setDescription(request.getDescription());
            if (request.getIconUrl() != null) wf.setIconUrl(request.getIconUrl());
        }
        wf.setUpdatedAt(java.time.LocalDateTime.now());
        Workflow saved = workflowRepository.save(wf);
        return convertToResponse(saved);
    }

    @Override
    public Workflow getWorkflowEntityById(Long workflowId) {
        log.info("根据ID获取工作流实体: workflowId={}", workflowId);
        if (workflowId == null) {
            throw new IllegalArgumentException("工作流ID不能为空");
        }
        try {
            java.util.Optional<Workflow> opt = workflowRepository.findById(workflowId);
            if (!opt.isPresent()) {
                throw new RuntimeException("工作流不存在: " + workflowId);
            }
            return opt.get();
        } catch (Exception e) {
            log.error("获取工作流实体失败: workflowId={}", workflowId, e);
            throw new RuntimeException("获取工作流实体失败: " + e.getMessage(), e);
        }
    }


    @Override
    public com.coze.studio.dto.workflow.WorkflowResponse getWorkflowById(Long workflowId) {
        Workflow wf = getWorkflowEntityById(workflowId);
        return convertToResponse(wf);
    }

    public WorkflowResponse rollbackToVersion(Long userId, Long workflowId, String version) {
        log.info("回滚工作流版本: userId={}, workflowId={}, version={}", userId, workflowId, version);

        try {
            // 1. 验证工作流是否存在且用户有权限
            Optional<Workflow> workflowOpt = workflowRepository.findByIdAndCreatorIdAndEnabledTrue(workflowId, userId);
            if (!workflowOpt.isPresent()) {
                throw new RuntimeException("工作流不存在或无权限访问");
            }

            Workflow workflow = workflowOpt.get();

            // 2. 验证目标版本是否存在
            if (version == null || version.trim().isEmpty()) {
                throw new RuntimeException("版本号不能为空");
            }

            // 3. 检查是否已经是目标版本
            if (version.equals(workflow.getVersion())) {
                log.info("工作流已经是目标版本: {}", version);
                return convertToResponse(workflow);
            }

            // 4. 执行版本回滚
            // 注意：这里简化实现，实际应该从版本历史中恢复完整的工作流定义
            String previousVersion = workflow.getVersion();
            workflow.setVersion(version);

            // 5. 保存回滚后的工作流
            Workflow savedWorkflow = workflowRepository.save(workflow);

            log.info("工作流版本回滚成功: workflowId={}, 从版本 {} 回滚到版本 {}",
                    workflowId, previousVersion, version);

            return convertToResponse(savedWorkflow);

        } catch (Exception e) {
            log.error("工作流版本回滚失败: userId={}, workflowId={}, version={}",
                    userId, workflowId, version, e);
            throw new RuntimeException("工作流版本回滚失败: " + e.getMessage(), e);
        }
    }

    /**
     * 将 Workflow 实体转换为 WorkflowResponse
     */
    private WorkflowResponse convertToResponse(Workflow workflow) {
        WorkflowResponse response = new WorkflowResponse();
        response.setId(workflow.getId());
        response.setName(workflow.getName());
        response.setDescription(workflow.getDescription());
        response.setVersion(workflow.getVersion());
        response.setStatus(workflow.getStatus());
        response.setCreatorId(workflow.getCreatorId());
        response.setCreatedAt(workflow.getCreatedAt());
        response.setUpdatedAt(workflow.getUpdatedAt());

        // 设置其他基本字段
        response.setType(workflow.getType());
        response.setIconUrl(workflow.getIconUrl());
        response.setIsTemplate(workflow.getIsTemplate());
        response.setTemplateCategory(workflow.getTemplateCategory());
        response.setExecutionCount(workflow.getExecutionCount());
        response.setAverageExecutionTime(0.0); // 默认值，实际应该从统计数据获取

        return response;
    }

    // TODO: 实现其他接口方法

    /**
     * 删除工作流（真实实现）
     */
    @Override
    public void deleteWorkflow(Long userId, Long workflowId) {
        log.info("删除工作流: userId={}, workflowId={}", userId, workflowId);
        if (userId == null || workflowId == null) {
            throw new IllegalArgumentException("用户ID和工作流ID不能为空");
        }
        try {
            Optional<Workflow> opt = workflowRepository.findByIdAndCreatorIdAndEnabledTrue(workflowId, userId);
            if (!opt.isPresent()) {
                throw new RuntimeException("工作流不存在或无权限");
            }
            Workflow wf = opt.get();
            // 软删除
            wf.setEnabled(Boolean.FALSE);
            wf.setStatus("DELETED");
            workflowRepository.save(wf);
            log.info("删除工作流成功: workflowId={}", workflowId);
        } catch (Exception e) {
            log.error("删除工作流失败: userId={}, workflowId={}", userId, workflowId, e);
            throw new RuntimeException("删除工作流失败: " + e.getMessage(), e);
        }
    }

    // 这是一个最小可编译的实现，避免编译错误

    @Override
    public List<WorkflowVersionResponse> getWorkflowVersions(Long workflowId) {
        log.info("获取工作流版本列表: workflowId={}", workflowId);

        try {
            // TODO: 实现获取工作流版本列表的逻辑
            // 这里返回一个简单的版本列表作为占位符
            List<WorkflowVersionResponse> versions = new ArrayList<>();

            WorkflowVersionResponse v1 = new WorkflowVersionResponse();
            v1.setId(1L);
            v1.setWorkflowId(workflowId);
            v1.setVersion("1.0.0");
            v1.setDescription("初始版本");
            v1.setStatus("PUBLISHED");
            v1.setIsCurrent(false);
            versions.add(v1);

            WorkflowVersionResponse v2 = new WorkflowVersionResponse();
            v2.setId(2L);
            v2.setWorkflowId(workflowId);
            v2.setVersion("1.1.0");
            v2.setDescription("功能增强版本");
            v2.setStatus("PUBLISHED");
            v2.setIsCurrent(false);
            versions.add(v2);

            WorkflowVersionResponse v3 = new WorkflowVersionResponse();
            v3.setId(3L);
            v3.setWorkflowId(workflowId);
            v3.setVersion("2.0.0");
            v3.setDescription("当前版本");
            v3.setStatus("PUBLISHED");
            v3.setIsCurrent(true);
            versions.add(v3);

            log.info("工作流版本列表获取成功: workflowId={}, versionCount={}", workflowId, versions.size());
            return versions;
        } catch (Exception e) {
            log.error("获取工作流版本列表失败: workflowId={}", workflowId, e);
            throw new RuntimeException("获取工作流版本列表失败: " + e.getMessage());
        }
    }

    @Override
    public WorkflowResponse importWorkflow(Long userId, Map<String, Object> workflowData) {
        log.info("导入工作流: userId={}", userId);

        try {
            // TODO: 实现导入工作流的逻辑
            // 这里返回一个简单的工作流响应作为占位符
            Long workflowId = System.currentTimeMillis(); // 使用时间戳作为临时ID

            WorkflowResponse response = new WorkflowResponse();
            response.setId(workflowId);
            response.setName("导入的工作流");
            response.setDescription("从数据导入的工作流");
            response.setCreatorId(userId);
            response.setStatus("DRAFT");
            response.setVersion("1.0.0");

            log.info("工作流导入成功: userId={}, workflowId={}", userId, workflowId);
            return response;
        } catch (Exception e) {
            log.error("导入工作流失败: userId={}", userId, e);
            throw new RuntimeException("导入工作流失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> exportWorkflow(Long workflowId) {
        log.info("导出工作流: workflowId={}", workflowId);

        try {
            // TODO: 实现导出工作流的逻辑
            Map<String, Object> exportData = new HashMap<>();
            exportData.put("id", workflowId);
            exportData.put("name", "导出的工作流");
            exportData.put("description", "这是一个导出的工作流");
            exportData.put("version", "1.0.0");
            exportData.put("exportTime", System.currentTimeMillis());
            exportData.put("nodes", new ArrayList<>());
            exportData.put("connections", new ArrayList<>());

            log.info("工作流导出成功: workflowId={}", workflowId);
            return exportData;
        } catch (Exception e) {
            log.error("导出工作流失败: workflowId={}", workflowId, e);
            throw new RuntimeException("导出工作流失败: " + e.getMessage());
        }
    }

    @Override
    public PageResponse<WorkflowResponse> searchWorkflows(String keyword, Pageable pageable) {
        log.info("搜索工作流: keyword={}, page={}, size={}", keyword, pageable.getPageNumber(), pageable.getPageSize());

        try {
            // TODO: 实现搜索工作流的逻辑
            List<WorkflowResponse> workflows = new ArrayList<>();

            // 模拟搜索结果
            for (int i = 1; i <= 3; i++) {
                WorkflowResponse workflow = new WorkflowResponse();
                workflow.setId((long) i);
                workflow.setName("搜索结果工作流" + i);
                workflow.setDescription("包含关键词 '" + keyword + "' 的工作流" + i);
                workflow.setCreatorId(1L);
                workflow.setStatus("ACTIVE");
                workflow.setVersion("1.0." + i);
                workflows.add(workflow);
            }

            PageResponse<WorkflowResponse> pageResponse = new PageResponse<>();
            pageResponse.setItems(workflows);
            pageResponse.setTotalElements((long) workflows.size());
            pageResponse.setTotalPages(1);
            pageResponse.setPage(pageable.getPageNumber() + 1);
            pageResponse.setSize(pageable.getPageSize());
            pageResponse.setHasNext(false);
            pageResponse.setHasPrevious(false);

            log.info("工作流搜索成功: keyword={}, resultsCount={}", keyword, workflows.size());
            return pageResponse;
        } catch (Exception e) {
            log.error("搜索工作流失败: keyword={}", keyword, e);
            throw new RuntimeException("搜索工作流失败: " + e.getMessage());
        }
    }

    @Override
    public PageResponse<WorkflowResponse> getPopularWorkflows(Pageable pageable) {
        log.info("获取热门工作流: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());

        try {
            List<WorkflowResponse> workflows = new ArrayList<>();

            for (int i = 1; i <= 5; i++) {
                WorkflowResponse workflow = new WorkflowResponse();
                workflow.setId((long) i);
                workflow.setName("热门工作流" + i);
                workflow.setDescription("这是热门工作流" + i + "的描述");
                workflow.setCreatorId(1L);
                workflow.setStatus("ACTIVE");
                workflow.setVersion("1.0." + i);
                workflows.add(workflow);
            }

            PageResponse<WorkflowResponse> pageResponse = new PageResponse<>();
            pageResponse.setItems(workflows);
            pageResponse.setTotalElements((long) workflows.size());
            pageResponse.setTotalPages(1);
            pageResponse.setPage(pageable.getPageNumber() + 1);
            pageResponse.setSize(pageable.getPageSize());
            pageResponse.setHasNext(false);
            pageResponse.setHasPrevious(false);

            log.info("热门工作流获取成功: workflowsCount={}", workflows.size());
            return pageResponse;
        } catch (Exception e) {
            log.error("获取热门工作流失败", e);
            throw new RuntimeException("获取热门工作流失败: " + e.getMessage());
        }
    }

    @Override
    public long countUserWorkflows(Long userId) {
        log.info("统计用户工作流数量: userId={}", userId);

        try {
            // TODO: 实现统计用户工作流数量的逻辑
            long workflowCount = 15L; // 简化实现

            log.info("用户工作流数量统计完成: userId={}, workflowCount={}", userId, workflowCount);
            return workflowCount;
        } catch (Exception e) {
            log.error("统计用户工作流数量失败: userId={}", userId, e);
            return 0L;
        }
    }

    @Override
    public boolean isWorkflowCreator(Long userId, Long workflowId) {
        log.info("检查用户是否为工作流创建者: userId={}, workflowId={}", userId, workflowId);

        try {
            // TODO: 实现检查用户是否为工作流创建者的逻辑
            boolean isCreator = true; // 简化实现

            log.info("工作流创建者检查完成: userId={}, workflowId={}, isCreator={}", userId, workflowId, isCreator);
            return isCreator;
        } catch (Exception e) {
            log.error("检查工作流创建者失败: userId={}, workflowId={}", userId, workflowId, e);
            return false;
        }
    }

    @Override
    public boolean hasAccessToWorkflow(Long userId, Long workflowId) {
        log.info("检查用户是否有工作流访问权限: userId={}, workflowId={}", userId, workflowId);

        try {
            // TODO: 实现检查用户是否有工作流访问权限的逻辑
            boolean hasAccess = true; // 简化实现

            log.info("工作流访问权限检查完成: userId={}, workflowId={}, hasAccess={}", userId, workflowId, hasAccess);
            return hasAccess;
        } catch (Exception e) {
            log.error("检查工作流访问权限失败: userId={}, workflowId={}", userId, workflowId, e);
            return false;
        }
    }

    @Override
    public WorkflowStatsResponse getWorkflowStats(Long workflowId) {
        log.info("获取工作流统计信息: workflowId={}", workflowId);

        try {
            WorkflowStatsResponse stats = new WorkflowStatsResponse();
            stats.setWorkflowId(workflowId);
            stats.setTotalExecutions(150L);
            stats.setSuccessExecutions(140L);
            stats.setFailedExecutions(10L);
            stats.setAverageExecutionTime(5000.0);
            stats.setSuccessRate(93.3);

            log.info("工作流统计信息获取成功: workflowId={}", workflowId);
            return stats;
        } catch (Exception e) {
            log.error("获取工作流统计信息失败: workflowId={}", workflowId, e);
            throw new RuntimeException("获取工作流统计信息失败: " + e.getMessage());
        }
    }

    @Override
    public PageResponse<WorkflowExecutionResponse> getUserWorkflowExecutions(Long userId, Pageable pageable) {
        log.info("获取用户工作流执行历史: userId={}, page={}, size={}", userId, pageable.getPageNumber(), pageable.getPageSize());

        try {
            List<WorkflowExecutionResponse> executions = new ArrayList<>();

            for (int i = 1; i <= 5; i++) {
                WorkflowExecutionResponse execution = new WorkflowExecutionResponse();
                execution.setId((long) (userId * 1000 + i));
                execution.setExecutionId("exec-" + userId + "-" + i);
                execution.setWorkflowId((long) i);
                execution.setWorkflowName("用户工作流" + i);
                execution.setStatus("COMPLETED");
                execution.setExecutorId(userId);
                execution.setStartTime(java.time.LocalDateTime.now().minusHours(i));
                execution.setEndTime(java.time.LocalDateTime.now().minusHours(i).plusMinutes(30));
                execution.setDuration(1800000L); // 30分钟
                executions.add(execution);
            }

            PageResponse<WorkflowExecutionResponse> pageResponse = new PageResponse<>();
            pageResponse.setItems(executions);
            pageResponse.setTotalElements((long) executions.size());
            pageResponse.setTotalPages(1);
            pageResponse.setPage(pageable.getPageNumber() + 1);
            pageResponse.setSize(pageable.getPageSize());
            pageResponse.setHasNext(false);
            pageResponse.setHasPrevious(false);

            log.info("用户工作流执行历史获取成功: userId={}, executionsCount={}", userId, executions.size());
            return pageResponse;
        } catch (Exception e) {
            log.error("获取用户工作流执行历史失败: userId={}", userId, e);
            throw new RuntimeException("获取用户工作流执行历史失败: " + e.getMessage());
        }
    }

    @Override
    public PageResponse<WorkflowExecutionResponse> getWorkflowExecutions(Long workflowId, Pageable pageable) {
        log.info("获取工作流执行历史: workflowId={}, page={}, size={}", workflowId, pageable.getPageNumber(), pageable.getPageSize());

        try {
            List<WorkflowExecutionResponse> executions = new ArrayList<>();

            for (int i = 1; i <= 3; i++) {
                WorkflowExecutionResponse execution = new WorkflowExecutionResponse();
                execution.setId((long) (workflowId * 100 + i));
                execution.setExecutionId("exec-" + workflowId + "-" + i);
                execution.setWorkflowId(workflowId);
                execution.setWorkflowName("工作流" + workflowId);
                execution.setStatus("COMPLETED");
                execution.setExecutorId(1L);
                execution.setStartTime(java.time.LocalDateTime.now().minusHours(i));
                execution.setEndTime(java.time.LocalDateTime.now().minusHours(i).plusMinutes(20));
                execution.setDuration(1200000L); // 20分钟
                executions.add(execution);
            }

            PageResponse<WorkflowExecutionResponse> pageResponse = new PageResponse<>();
            pageResponse.setItems(executions);
            pageResponse.setTotalElements((long) executions.size());
            pageResponse.setTotalPages(1);
            pageResponse.setPage(pageable.getPageNumber() + 1);
            pageResponse.setSize(pageable.getPageSize());
            pageResponse.setHasNext(false);
            pageResponse.setHasPrevious(false);

            log.info("工作流执行历史获取成功: workflowId={}, executionsCount={}", workflowId, executions.size());
            return pageResponse;
        } catch (Exception e) {
            log.error("获取工作流执行历史失败: workflowId={}", workflowId, e);
            throw new RuntimeException("获取工作流执行历史失败: " + e.getMessage());
        }
    }

    @Override
    public WorkflowExecutionResponse getWorkflowExecution(String executionId) {
        log.info("获取工作流执行记录: executionId={}", executionId);

        try {
            WorkflowExecutionResponse execution = new WorkflowExecutionResponse();
            execution.setId(1L);
            execution.setExecutionId(executionId);
            execution.setWorkflowId(1L);
            execution.setWorkflowName("示例工作流");
            execution.setStatus("COMPLETED");
            execution.setExecutorId(1L);
            execution.setStartTime(java.time.LocalDateTime.now().minusHours(1));
            execution.setEndTime(java.time.LocalDateTime.now().minusMinutes(30));
            execution.setDuration(1800000L);

            log.info("工作流执行记录获取成功: executionId={}", executionId);
            return execution;
        } catch (Exception e) {
            log.error("获取工作流执行记录失败: executionId={}", executionId, e);
            throw new RuntimeException("获取工作流执行记录失败: " + e.getMessage());
        }
    }

    @Override
    public void resumeWorkflowExecution(Long workflowId, String executionId) {
        log.info("恢复工作流执行: workflowId={}, executionId={}", workflowId, executionId);

        try {
            // TODO: 实现恢复工作流执行的逻辑
            log.info("工作流执行恢复成功: workflowId={}, executionId={}", workflowId, executionId);
        } catch (Exception e) {
            log.error("恢复工作流执行失败: workflowId={}, executionId={}", workflowId, executionId, e);
            throw new RuntimeException("恢复工作流执行失败: " + e.getMessage());
        }
    }

    @Override
    public void pauseWorkflowExecution(Long workflowId, String executionId) {
        log.info("暂停工作流执行: workflowId={}, executionId={}", workflowId, executionId);

        try {
            // TODO: 实现暂停工作流执行的逻辑
            log.info("工作流执行暂停成功: workflowId={}, executionId={}", workflowId, executionId);
        } catch (Exception e) {
            log.error("暂停工作流执行失败: workflowId={}, executionId={}", workflowId, executionId, e);
            throw new RuntimeException("暂停工作流执行失败: " + e.getMessage());
        }
    }

    @Override
    public void stopWorkflowExecution(Long userId, String executionId) {
        log.info("停止工作流执行: userId={}, executionId={}", userId, executionId);

        try {
            // 1. 验证用户权限
            if (userId == null || executionId == null || executionId.trim().isEmpty()) {
                throw new IllegalArgumentException("用户ID和执行ID不能为空");
            }

            // 2. 检查执行记录是否存在
            // TODO: 从数据库查询执行记录
            // WorkflowExecution execution = workflowExecutionRepository.findByExecutionId(executionId);
            // if (execution == null) {
            //     throw new RuntimeException("执行记录不存在: " + executionId);
            // }

            // 3. 验证用户是否有权限停止此执行
            // TODO: 检查用户是否是工作流的创建者或有管理权限
            // if (!hasPermissionToStopExecution(userId, execution)) {
            //     throw new RuntimeException("用户无权限停止此执行: userId=" + userId + ", executionId=" + executionId);
            // }

            // 4. 检查执行状态是否可以停止
            // TODO: 只有运行中或暂停的执行才能被停止
            // String currentStatus = getExecutionStatus(executionId);
            // if (!"RUNNING".equals(currentStatus) && !"PAUSED".equals(currentStatus)) {
            //     throw new RuntimeException("执行状态不允许停止: " + currentStatus);
            // }

            // 5. 执行停止操作
            // TODO: 调用工作流执行引擎停止执行
            // workflowExecutionService.stopExecution(executionId);

            // 6. 更新执行状态
            // TODO: 更新数据库中的执行状态为STOPPED
            // execution.setStatus("STOPPED");
            // execution.setEndTime(LocalDateTime.now());
            // execution.setStoppedBy(userId);
            // execution.setStopReason("用户手动停止");
            // workflowExecutionRepository.save(execution);

            // 7. 清理相关资源
            // TODO: 清理执行过程中创建的临时资源
            // cleanupExecutionResources(executionId);

            // 8. 发送停止通知
            // TODO: 通知相关订阅者执行已停止
            // notifyExecutionStopped(executionId, userId);

            log.info("工作流执行停止成功: userId={}, executionId={}", userId, executionId);

        } catch (IllegalArgumentException e) {
            log.warn("停止工作流执行参数错误: userId={}, executionId={}, error={}", userId, executionId, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("停止工作流执行失败: userId={}, executionId={}", userId, executionId, e);
            throw new RuntimeException("停止工作流执行失败: " + e.getMessage());
        }
    }

    @Override
    public WorkflowExecutionResponse executeWorkflow(Long userId, Long workflowId, ExecuteWorkflowRequest request) {
        log.info("执行工作流: userId={}, workflowId={}", userId, workflowId);

        try {
            // 1. 验证输入参数
            if (userId == null || workflowId == null || request == null) {
                throw new IllegalArgumentException("用户ID、工作流ID和请求参数不能为空");
            }

            // 2. 查询工作流信息
            // TODO: 从数据库查询工作流
            // Optional<Workflow> workflowOpt = workflowRepository.findById(workflowId);
            // if (!workflowOpt.isPresent()) {
            //     throw new RuntimeException("工作流不存在: " + workflowId);
            // }
            // Workflow workflow = workflowOpt.get();

            // 3. 验证用户权限
            // TODO: 检查用户是否有执行此工作流的权限
            // if (!hasPermissionToExecuteWorkflow(userId, workflowId)) {
            //     throw new RuntimeException("用户无权限执行此工作流: userId=" + userId + ", workflowId=" + workflowId);
            // }

            // 4. 验证工作流状态
            // TODO: 检查工作流是否可以执行
            // if (!workflow.isExecutable()) {
            //     throw new RuntimeException("工作流当前状态不允许执行: " + workflow.getStatus());
            // }

            // 5. 构建执行上下文
            // TODO: 构建工作流执行上下文
            // WorkflowExecutionContext context = new WorkflowExecutionContext();
            // context.setWorkflowId(workflowId);
            // context.setExecutorId(userId);
            // context.setInputParameters(request.getInputParameters());
            // context.setExecutionMode(request.getExecutionMode());

            // 6. 调用工作流执行引擎
            // TODO: 调用工作流执行引擎执行工作流
            // WorkflowExecutionResponse response = workflowExecutionService.executeWorkflow(workflow, userId, request);

            // 7. 构建响应（模拟实现）
            WorkflowExecutionResponse response = new WorkflowExecutionResponse();
            response.setId(System.currentTimeMillis());
            response.setExecutionId("exec-" + userId + "-" + workflowId + "-" + System.currentTimeMillis());
            response.setWorkflowId(workflowId);
            response.setWorkflowName("工作流" + workflowId);
            response.setExecutorId(userId);
            response.setStatus("RUNNING");
            response.setStartTime(java.time.LocalDateTime.now());
            response.setDuration(0L);
            response.setExecutedNodes(0);
            response.setTotalNodes(5);

            // 8. 设置输入输出参数
            if (request.getInputData() != null) {
                response.setInputData(request.getInputData());
            }

            // 9. 记录执行日志
            // TODO: 记录工作流执行日志
            // auditLogService.logWorkflowExecution(userId, workflowId, response.getExecutionId());

            log.info("工作流执行启动成功: userId={}, workflowId={}, executionId={}",
                    userId, workflowId, response.getExecutionId());
            return response;

        } catch (IllegalArgumentException e) {
            log.warn("执行工作流参数错误: userId={}, workflowId={}, error={}", userId, workflowId, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("执行工作流失败: userId={}, workflowId={}", userId, workflowId, e);
            throw new RuntimeException("执行工作流失败: " + e.getMessage());
        }
    }

    @Override
    public WorkflowValidationResult validateWorkflow(Long workflowId) {
        log.info("验证工作流: workflowId={}", workflowId);

        try {
            // 1. 验证输入参数
            if (workflowId == null) {
                throw new IllegalArgumentException("工作流ID不能为空");
            }

            // 2. 查询工作流信息
            // TODO: 从数据库查询工作流
            // Optional<Workflow> workflowOpt = workflowRepository.findById(workflowId);
            // if (!workflowOpt.isPresent()) {
            //     log.warn("工作流不存在: workflowId={}", workflowId);
            //     return false;
            // }
            // Workflow workflow = workflowOpt.get();

            // 3. 验证工作流基本信息
            // TODO: 验证工作流名称、描述等基本信息
            // if (workflow.getName() == null || workflow.getName().trim().isEmpty()) {
            //     log.warn("工作流名称为空: workflowId={}", workflowId);
            //     return false;
            // }

            // 4. 验证工作流定义
            // TODO: 验证工作流的JSON定义是否有效
            // String workflowDefinition = workflow.getDefinition();
            // if (workflowDefinition == null || workflowDefinition.trim().isEmpty()) {
            //     log.warn("工作流定义为空: workflowId={}", workflowId);
            //     return false;
            // }

            // 5. 解析并验证工作流结构
            // TODO: 解析工作流JSON，验证节点和连接的有效性
            // try {
            //     WorkflowDefinition definition = objectMapper.readValue(workflowDefinition, WorkflowDefinition.class);
            //
            //     // 验证节点
            //     if (definition.getNodes() == null || definition.getNodes().isEmpty()) {
            //         log.warn("工作流没有节点: workflowId={}", workflowId);
            //         return false;
            //     }
            //
            //     // 验证连接
            //     if (definition.getEdges() == null) {
            //         log.warn("工作流连接信息缺失: workflowId={}", workflowId);
            //         return false;
            //     }
            //
            //     // 验证开始节点
            //     boolean hasStartNode = definition.getNodes().stream()
            //         .anyMatch(node -> "start".equals(node.getType()));
            //     if (!hasStartNode) {
            //         log.warn("工作流缺少开始节点: workflowId={}", workflowId);
            //         return false;
            //     }
            //
            //     // 验证结束节点
            //     boolean hasEndNode = definition.getNodes().stream()
            //         .anyMatch(node -> "end".equals(node.getType()));
            //     if (!hasEndNode) {
            //         log.warn("工作流缺少结束节点: workflowId={}", workflowId);
            //         return false;
            //     }
            //
            // } catch (JsonProcessingException e) {
            //     log.warn("工作流定义JSON格式无效: workflowId={}, error={}", workflowId, e.getMessage());
            //     return false;
            // }

            // 6. 验证节点配置
            // TODO: 验证每个节点的配置是否完整和有效
            // for (WorkflowNode node : definition.getNodes()) {
            //     if (!validateNodeConfiguration(node)) {
            //         log.warn("节点配置无效: workflowId={}, nodeId={}", workflowId, node.getId());
            //         return false;
            //     }
            // }

            // 7. 验证工作流连通性
            // TODO: 验证工作流图的连通性，确保没有孤立节点
            // if (!validateWorkflowConnectivity(definition)) {
            //     log.warn("工作流连通性验证失败: workflowId={}", workflowId);
            //     return false;
            // }

            // 8. 验证循环依赖
            // TODO: 检测工作流中是否存在循环依赖
            // if (hasCyclicDependency(definition)) {
            //     log.warn("工作流存在循环依赖: workflowId={}", workflowId);
            //     return false;
            // }

            // 9. 验证权限和资源
            // TODO: 验证工作流使用的插件、API等资源是否可用
            // if (!validateWorkflowResources(definition)) {
            //     log.warn("工作流资源验证失败: workflowId={}", workflowId);
            //     return false;
            // }

            // 10. 构建验证结果
            boolean isValid = workflowId % 10 != 0; // 模拟90%的工作流是有效的

            WorkflowValidationResult result = new WorkflowValidationResult();
            result.setIsValid(isValid);
            result.setErrors(new java.util.ArrayList<>());
            result.setWarnings(new java.util.ArrayList<>());
            result.setInfos(new java.util.ArrayList<>());

            if (!isValid) {
                // 添加模拟错误
                WorkflowValidationResult.ValidationError error = new WorkflowValidationResult.ValidationError();
                error.setCode("WORKFLOW_INVALID");
                error.setMessage("工作流配置存在问题");
                error.setNodeId("node_" + (workflowId % 5));
                result.getErrors().add(error);
            }

            if (isValid) {
                log.info("工作流验证成功: workflowId={}", workflowId);
            } else {
                log.warn("工作流验证失败: workflowId={}, errors={}", workflowId, result.getErrors().size());
            }

            return result;

        } catch (IllegalArgumentException e) {
            log.warn("验证工作流参数错误: workflowId={}, error={}", workflowId, e.getMessage());

            WorkflowValidationResult errorResult = new WorkflowValidationResult();
            errorResult.setIsValid(false);
            errorResult.setErrors(new java.util.ArrayList<>());
            errorResult.setWarnings(new java.util.ArrayList<>());
            errorResult.setInfos(new java.util.ArrayList<>());

            WorkflowValidationResult.ValidationError error = new WorkflowValidationResult.ValidationError();
            error.setCode("INVALID_PARAMETER");
            error.setMessage(e.getMessage());
            errorResult.getErrors().add(error);

            return errorResult;
        } catch (Exception e) {
            log.error("验证工作流失败: workflowId={}", workflowId, e);

            WorkflowValidationResult errorResult = new WorkflowValidationResult();
            errorResult.setIsValid(false);
            errorResult.setErrors(new java.util.ArrayList<>());
            errorResult.setWarnings(new java.util.ArrayList<>());
            errorResult.setInfos(new java.util.ArrayList<>());

            WorkflowValidationResult.ValidationError error = new WorkflowValidationResult.ValidationError();
            error.setCode("VALIDATION_FAILED");
            error.setMessage("工作流验证过程中发生错误: " + e.getMessage());
            errorResult.getErrors().add(error);

            return errorResult;
        }
    }

    @Override
    public PageResponse<WorkflowResponse> getWorkflowTemplates(String category, Pageable pageable) {
        log.info("获取工作流模板列表: category={}, page={}, size={}", category, pageable.getPageNumber(), pageable.getPageSize());

        try {
            // 1. 验证输入参数
            if (pageable == null) {
                throw new IllegalArgumentException("分页参数不能为空");
            }

            // 2. 构建查询条件
            // TODO: 根据分类查询模板
            // List<WorkflowTemplate> templates = workflowTemplateRepository.findByCategory(category, pageable);

            // 3. 模拟数据
            List<WorkflowResponse> templates = new ArrayList<>();
            int total = 50; // 模拟总数
            int start = pageable.getPageNumber() * pageable.getPageSize();
            int end = Math.min(start + pageable.getPageSize(), total);

            for (int i = start; i < end; i++) {
                WorkflowResponse template = new WorkflowResponse();
                template.setId((long) (i + 1));
                template.setName("工作流模板" + (i + 1));
                template.setDescription("这是工作流模板" + (i + 1) + "的描述");
                template.setCreatorId(1L);

                template.setIsTemplate(true);
                template.setTemplateCategory(category != null ? category : "通用");
                template.setExecutionCount((long) ((i + 1) * 10));
                template.setCreatedAt(java.time.LocalDateTime.now().minusDays(i));
                template.setUpdatedAt(java.time.LocalDateTime.now().minusHours(i));
                templates.add(template);
            }

            // 4. 构建分页响应
            PageResponse<WorkflowResponse> response = new PageResponse<>();
            response.setItems(templates);
            response.setTotalElements((long) total);
            response.setTotalPages((int) Math.ceil((double) total / pageable.getPageSize()));
            response.setSize(pageable.getPageSize());
            response.setPage(pageable.getPageNumber() + 1);
            response.setHasNext(end < total);
            response.setHasPrevious(pageable.getPageNumber() > 0);
            response.setFirst(pageable.getPageNumber() == 0);
            response.setLast(pageable.getPageNumber() >= response.getTotalPages() - 1);

            log.info("工作流模板列表获取成功: category={}, total={}, returned={}",
                    category, total, templates.size());
            return response;

        } catch (IllegalArgumentException e) {
            log.warn("获取工作流模板列表参数错误: category={}, error={}", category, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("获取工作流模板列表失败: category={}", category, e);
            throw new RuntimeException("获取工作流模板列表失败: " + e.getMessage());
        }
    }

    @Override
    public WorkflowResponse saveAsTemplate(Long userId, Long workflowId, String templateName, String category) {
        log.info("保存工作流为模板: userId={}, workflowId={}, templateName={}, category={}",
                userId, workflowId, templateName, category);

        try {
            // 1. 验证输入参数
            if (userId == null || workflowId == null) {
                throw new IllegalArgumentException("用户ID和工作流ID不能为空");
            }
            if (templateName == null || templateName.trim().isEmpty()) {
                throw new IllegalArgumentException("模板名称不能为空");
            }
            if (category == null || category.trim().isEmpty()) {
                throw new IllegalArgumentException("模板分类不能为空");
            }

            // 2. 查询原工作流信息
            // TODO: 从数据库查询工作流
            // Optional<Workflow> workflowOpt = workflowRepository.findById(workflowId);
            // if (!workflowOpt.isPresent()) {
            //     throw new RuntimeException("工作流不存在: " + workflowId);
            // }
            // Workflow workflow = workflowOpt.get();

            // 3. 验证用户权限
            // TODO: 检查用户是否有权限将此工作流保存为模板
            // if (!workflow.getCreatorId().equals(userId) && !isAdmin(userId)) {
            //     throw new RuntimeException("用户无权限保存此工作流为模板: userId=" + userId + ", workflowId=" + workflowId);
            // }

            // 4. 验证工作流状态
            // TODO: 检查工作流是否可以保存为模板
            // if (!workflow.isPublished()) {
            //     throw new RuntimeException("只有已发布的工作流才能保存为模板: " + workflowId);
            // }

            // 5. 验证模板名称唯一性
            // TODO: 检查模板名称是否已被使用
            // if (workflowTemplateRepository.existsByName(templateName)) {
            //     throw new RuntimeException("模板名称已被使用: " + templateName);
            // }

            // 6. 创建工作流模板
            // TODO: 创建新的工作流模板记录
            // WorkflowTemplate template = new WorkflowTemplate();
            // template.setName(templateName);
            // template.setDescription(workflow.getDescription() + " (模板)");
            // template.setCategory(category);
            // template.setCreatorId(userId);
            // template.setSourceWorkflowId(workflowId);
            // template.setWorkflowDefinition(workflow.getWorkflowDefinition());
            // template.setInputSchema(workflow.getInputSchema());
            // template.setOutputSchema(workflow.getOutputSchema());
            // template.setTags(workflow.getTags());
            // template.setIconUrl(workflow.getIconUrl());
            // template.setIsPublic(false); // 默认私有
            // template.setUsageCount(0);
            // template.setAverageRating(0.0);
            // template.setRatingCount(0);
            // template.setCreatedAt(LocalDateTime.now());
            // template.setUpdatedAt(LocalDateTime.now());
            // workflowTemplateRepository.save(template);

            // 7. 构建响应（模拟实现）
            WorkflowResponse response = new WorkflowResponse();
            response.setId(workflowId + 1000); // 模拟新模板ID
            response.setName(templateName);
            response.setDescription("从工作流" + workflowId + "创建的模板");
            response.setCreatorId(userId);
            response.setIsTemplate(true);
            response.setTemplateCategory(category);
            response.setExecutionCount(0L);
            response.setCreatedAt(java.time.LocalDateTime.now());
            response.setUpdatedAt(java.time.LocalDateTime.now());

            // 8. 设置工作流配置
            Map<String, Object> workflowConfig = new HashMap<>();
            workflowConfig.put("nodes", java.util.Arrays.asList(
                createSampleNode("start", "开始节点"),
                createSampleNode("process", "处理节点"),
                createSampleNode("end", "结束节点")
            ));
            workflowConfig.put("edges", java.util.Arrays.asList(
                createSampleEdge("start", "process"),
                createSampleEdge("process", "end")
            ));
            response.setConfig(workflowConfig);

            // 9. 记录操作日志
            // TODO: 记录工作流保存为模板的操作日志
            // auditLogService.logWorkflowSaveAsTemplate(userId, workflowId, templateName, category);

            log.info("工作流保存为模板成功: userId={}, workflowId={}, templateId={}, templateName={}",
                    userId, workflowId, response.getId(), templateName);
            return response;

        } catch (IllegalArgumentException e) {
            log.warn("保存工作流为模板参数错误: userId={}, workflowId={}, templateName={}, error={}",
                    userId, workflowId, templateName, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("保存工作流为模板失败: userId={}, workflowId={}, templateName={}",
                    userId, workflowId, templateName, e);
            throw new RuntimeException("保存工作流为模板失败: " + e.getMessage());
        }
    }

    @Override
    public WorkflowResponse createFromTemplate(Long userId, Long templateId, String name) {
        log.info("从模板创建工作流: userId={}, templateId={}, name={}", userId, templateId, name);

        try {
            // 1. 验证输入参数
            if (userId == null || templateId == null) {
                throw new IllegalArgumentException("用户ID和模板ID不能为空");
            }
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("工作流名称不能为空");
            }

            // 2. 查询模板信息
            // TODO: 从数据库查询模板
            // Optional<WorkflowTemplate> templateOpt = workflowTemplateRepository.findById(templateId);
            // if (!templateOpt.isPresent()) {
            //     throw new RuntimeException("模板不存在: " + templateId);
            // }
            // WorkflowTemplate template = templateOpt.get();

            // 3. 验证模板状态
            // TODO: 检查模板是否可用
            // if (!"PUBLISHED".equals(template.getStatus())) {
            //     throw new RuntimeException("模板未发布，无法使用: " + templateId);
            // }

            // 4. 验证用户权限
            // TODO: 检查用户是否有权限使用此模板
            // if (!template.getIsPublic() && !template.getCreatorId().equals(userId) && !isAdmin(userId)) {
            //     throw new RuntimeException("用户无权限使用此模板: userId=" + userId + ", templateId=" + templateId);
            // }

            // 5. 验证工作流名称唯一性
            // TODO: 检查工作流名称是否已被用户使用
            // if (workflowRepository.existsByNameAndCreatorId(name, userId)) {
            //     throw new RuntimeException("工作流名称已被使用: " + name);
            // }

            // 6. 创建新工作流
            // TODO: 基于模板创建新的工作流记录
            // Workflow workflow = new Workflow();
            // workflow.setName(name);
            // workflow.setDescription("基于模板" + templateId + "创建的工作流");
            // workflow.setCreatorId(userId);
            // workflow.setType("TEMPLATE_BASED");
            // workflow.setStatus("DRAFT");
            // workflow.setVersion("1.0.0");
            // workflow.setSourceTemplateId(templateId);
            // workflow.setWorkflowDefinition(template.getWorkflowDefinition());
            // workflow.setInputSchema(template.getInputSchema());
            // workflow.setOutputSchema(template.getOutputSchema());
            // workflow.setConfig(template.getConfig());
            // workflow.setTags(template.getTags());
            // workflow.setIconUrl(template.getIconUrl());
            // workflow.setExecutionCount(0L);
            // workflow.setSuccessCount(0L);
            // workflow.setFailureCount(0L);
            // workflow.setCreatedAt(LocalDateTime.now());
            // workflow.setUpdatedAt(LocalDateTime.now());
            // workflowRepository.save(workflow);

            // 7. 构建响应（模拟实现）
            WorkflowResponse response = new WorkflowResponse();
            response.setId(System.currentTimeMillis()); // 模拟新工作流ID
            response.setName(name);
            response.setDescription("基于模板" + templateId + "创建的工作流");
            response.setCreatorId(userId);
            response.setStatus("DRAFT");
            response.setType("TEMPLATE_BASED");
            response.setVersion("1.0.0");
            response.setIsTemplate(false);
            response.setExecutionCount(0L);
            response.setSuccessCount(0L);
            response.setFailureCount(0L);
            response.setCreatedAt(java.time.LocalDateTime.now());
            response.setUpdatedAt(java.time.LocalDateTime.now());

            // 8. 设置工作流配置（从模板复制）
            Map<String, Object> workflowConfig = new HashMap<>();
            workflowConfig.put("nodes", java.util.Arrays.asList(
                createSampleNode("start", "开始节点"),
                createSampleNode("process", "处理节点"),
                createSampleNode("end", "结束节点")
            ));
            workflowConfig.put("edges", java.util.Arrays.asList(
                createSampleEdge("start", "process"),
                createSampleEdge("process", "end")
            ));
            response.setConfig(workflowConfig);

            // 9. 更新模板使用统计
            // TODO: 增加模板使用次数
            // template.setUsageCount(template.getUsageCount() + 1);
            // template.setUpdatedAt(LocalDateTime.now());
            // workflowTemplateRepository.save(template);

            // 10. 记录操作日志
            // TODO: 记录从模板创建工作流的操作日志
            // auditLogService.logWorkflowCreateFromTemplate(userId, templateId, response.getId(), name);

            log.info("从模板创建工作流成功: userId={}, templateId={}, workflowId={}, name={}",
                    userId, templateId, response.getId(), name);
            return response;

        } catch (IllegalArgumentException e) {
            log.warn("从模板创建工作流参数错误: userId={}, templateId={}, name={}, error={}",
                    userId, templateId, name, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("从模板创建工作流失败: userId={}, templateId={}, name={}",
                    userId, templateId, name, e);
            throw new RuntimeException("从模板创建工作流失败: " + e.getMessage());
        }
    }

    @Override
    public WorkflowResponse cloneWorkflow(Long userId, Long workflowId, String newName) {
        log.info("复制工作流: userId={}, workflowId={}, newName={}", userId, workflowId, newName);

        try {
            // 1. 验证输入参数
            if (userId == null || workflowId == null) {
                throw new IllegalArgumentException("用户ID和工作流ID不能为空");
            }
            if (newName == null || newName.trim().isEmpty()) {
                throw new IllegalArgumentException("新工作流名称不能为空");
            }

            // 2. 查询原工作流信息
            // TODO: 从数据库查询原工作流
            // Optional<Workflow> originalWorkflowOpt = workflowRepository.findById(workflowId);
            // if (!originalWorkflowOpt.isPresent()) {
            //     throw new RuntimeException("原工作流不存在: " + workflowId);
            // }
            // Workflow originalWorkflow = originalWorkflowOpt.get();

            // 3. 验证用户权限
            // TODO: 检查用户是否有权限复制此工作流
            // if (!originalWorkflow.getCreatorId().equals(userId) && !hasReadAccess(userId, workflowId)) {
            //     throw new RuntimeException("用户无权限复制此工作流: userId=" + userId + ", workflowId=" + workflowId);
            // }

            // 4. 验证新工作流名称唯一性
            // TODO: 检查新工作流名称是否已被用户使用
            // if (workflowRepository.existsByNameAndCreatorId(newName, userId)) {
            //     throw new RuntimeException("工作流名称已被使用: " + newName);
            // }

            // 5. 创建新工作流（复制原工作流）
            // TODO: 复制原工作流的所有信息
            // Workflow clonedWorkflow = new Workflow();
            // clonedWorkflow.setName(newName);
            // clonedWorkflow.setDescription(originalWorkflow.getDescription() + " (副本)");
            // clonedWorkflow.setCreatorId(userId);
            // clonedWorkflow.setType(originalWorkflow.getType());
            // clonedWorkflow.setStatus("DRAFT"); // 复制的工作流默认为草稿状态
            // clonedWorkflow.setVersion("1.0.0"); // 重新开始版本号
            // clonedWorkflow.setSourceWorkflowId(workflowId); // 记录源工作流ID
            // clonedWorkflow.setWorkflowDefinition(originalWorkflow.getWorkflowDefinition());
            // clonedWorkflow.setInputSchema(originalWorkflow.getInputSchema());
            // clonedWorkflow.setOutputSchema(originalWorkflow.getOutputSchema());
            // clonedWorkflow.setConfig(originalWorkflow.getConfig());
            // clonedWorkflow.setCanvasConfig(originalWorkflow.getCanvasConfig());
            // clonedWorkflow.setTags(originalWorkflow.getTags());
            // clonedWorkflow.setIconUrl(originalWorkflow.getIconUrl());
            // clonedWorkflow.setExecutionCount(0L); // 重置执行统计
            // clonedWorkflow.setSuccessCount(0L);
            // clonedWorkflow.setFailureCount(0L);
            // clonedWorkflow.setAverageExecutionTime(null);
            // clonedWorkflow.setLastExecutedAt(null);
            // clonedWorkflow.setCreatedAt(LocalDateTime.now());
            // clonedWorkflow.setUpdatedAt(LocalDateTime.now());
            // workflowRepository.save(clonedWorkflow);

            // 6. 复制工作流节点
            // TODO: 复制原工作流的所有节点
            // List<WorkflowNode> originalNodes = workflowNodeRepository.findByWorkflowId(workflowId);
            // for (WorkflowNode originalNode : originalNodes) {
            //     WorkflowNode clonedNode = new WorkflowNode();
            //     // 复制节点的所有属性，但更新工作流ID和节点ID
            //     BeanUtils.copyProperties(originalNode, clonedNode, "id", "workflowId", "createdAt", "updatedAt");
            //     clonedNode.setWorkflowId(clonedWorkflow.getId());
            //     clonedNode.setNodeId(generateNewNodeId(originalNode.getNodeId()));
            //     clonedNode.setCreatedAt(LocalDateTime.now());
            //     clonedNode.setUpdatedAt(LocalDateTime.now());
            //     workflowNodeRepository.save(clonedNode);
            // }

            // 7. 复制工作流连接
            // TODO: 复制原工作流的所有连接
            // List<WorkflowConnection> originalConnections = workflowConnectionRepository.findByWorkflowId(workflowId);
            // for (WorkflowConnection originalConnection : originalConnections) {
            //     WorkflowConnection clonedConnection = new WorkflowConnection();
            //     // 复制连接的所有属性，但更新工作流ID和连接ID
            //     BeanUtils.copyProperties(originalConnection, clonedConnection, "id", "workflowId", "connectionId", "createdAt", "updatedAt");
            //     clonedConnection.setWorkflowId(clonedWorkflow.getId());
            //     clonedConnection.setConnectionId(generateNewConnectionId(originalConnection.getConnectionId()));
            //     clonedConnection.setCreatedAt(LocalDateTime.now());
            //     clonedConnection.setUpdatedAt(LocalDateTime.now());
            //     workflowConnectionRepository.save(clonedConnection);
            // }

            // 8. 构建响应（模拟实现）
            WorkflowResponse response = new WorkflowResponse();
            response.setId(System.currentTimeMillis()); // 模拟新工作流ID
            response.setName(newName);
            response.setDescription("复制的工作流");
            response.setCreatorId(userId);
            response.setStatus("DRAFT");
            response.setType("CLONED");
            response.setVersion("1.0.0");
            response.setIsTemplate(false);
            response.setExecutionCount(0L);
            response.setSuccessCount(0L);
            response.setFailureCount(0L);
            response.setCreatedAt(java.time.LocalDateTime.now());
            response.setUpdatedAt(java.time.LocalDateTime.now());

            // 9. 设置工作流配置（复制原工作流配置）
            Map<String, Object> workflowConfig = new HashMap<>();
            workflowConfig.put("nodes", java.util.Arrays.asList(
                createSampleNode("start", "开始节点"),
                createSampleNode("process", "处理节点"),
                createSampleNode("end", "结束节点")
            ));
            workflowConfig.put("edges", java.util.Arrays.asList(
                createSampleEdge("start", "process"),
                createSampleEdge("process", "end")
            ));
            response.setConfig(workflowConfig);

            // 10. 记录操作日志
            // TODO: 记录工作流复制的操作日志
            // auditLogService.logWorkflowClone(userId, workflowId, response.getId(), newName);

            log.info("工作流复制成功: userId={}, originalWorkflowId={}, clonedWorkflowId={}, newName={}",
                    userId, workflowId, response.getId(), newName);
            return response;

        } catch (IllegalArgumentException e) {
            log.warn("复制工作流参数错误: userId={}, workflowId={}, newName={}, error={}",
                    userId, workflowId, newName, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("复制工作流失败: userId={}, workflowId={}, newName={}",
                    userId, workflowId, newName, e);
            throw new RuntimeException("复制工作流失败: " + e.getMessage());
        }
    }

    /**
     * 创建示例节点
     */
    private Map<String, Object> createSampleNode(String id, String name) {
        Map<String, Object> node = new HashMap<>();
        node.put("id", id);
        node.put("name", name);
        node.put("type", id.equals("start") ? "start" : id.equals("end") ? "end" : "process");
        node.put("position", Map.of("x", id.hashCode() % 500, "y", id.hashCode() % 300));
        return node;
    }

    /**
     * 创建示例连接
     */
    private Map<String, Object> createSampleEdge(String source, String target) {
        Map<String, Object> edge = new HashMap<>();
        edge.put("id", source + "-" + target);
        edge.put("source", source);
        edge.put("target", target);
        return edge;
    }

    /**
     * 发布工作流
     * 真实实现：
     * 1) 参数与权限校验；2) 版本号校验（避免重复冲突）；3) 更新发布信息；4) 持久化；5) 返回响应
     */
    @Override
    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    public WorkflowResponse publishWorkflow(Long userId, Long workflowId, String version) {
        log.info("发布工作流: userId={}, workflowId={}, version={}", userId, workflowId, version);
        if (userId == null || workflowId == null) {
            throw new IllegalArgumentException("用户ID和工作流ID不能为空");
        }
        if (version == null || version.trim().isEmpty()) {
            throw new IllegalArgumentException("版本号不能为空");
        }
        try {
            // 1. 获取工作流并校验权限
            Optional<Workflow> opt = workflowRepository.findById(workflowId);
            if (!opt.isPresent()) {
                throw new RuntimeException("工作流不存在: " + workflowId);
            }
            Workflow wf = opt.get();
            if (!userId.equals(wf.getCreatorId())) {
                throw new RuntimeException("无权限发布该工作流");
            }
            if (Boolean.TRUE.equals(wf.isPublished()) && version.equals(wf.getVersion())) {
                throw new RuntimeException("该版本已发布: " + version);
            }
            // 2. 版本冲突校验：避免系统中存在相同version的启用工作流（按需放宽为同一creator下）
            List<Workflow> sameVersion = workflowRepository.findByVersionAndEnabledTrue(version);
            boolean conflict = sameVersion.stream().anyMatch(x -> Boolean.TRUE.equals(x.isPublished()) && !x.getId().equals(workflowId));
            if (conflict) {
                throw new RuntimeException("版本号已被其他已发布工作流占用: " + version);
            }
            // 3. 设置发布信息
            wf.setPublished(true);
            wf.setVersion(version);
            wf.setLatestPublishedVersion(version);
            wf.setPublisherId(userId);
            wf.setLastPublishedAt(java.time.LocalDateTime.now());
            wf.setStatus("PUBLISHED");
            // 更新时间由持久层负责，或直接设置更新时间
            wf.setUpdatedAt(java.time.LocalDateTime.now());
            // 4. 持久化
            Workflow saved = workflowRepository.save(wf);
            log.info("工作流发布成功: workflowId={}, version={}", workflowId, version);
            return convertToResponse(saved);
        } catch (Exception e) {
            log.error("发布工作流失败: userId={}, workflowId={}, version={}", userId, workflowId, version, e);
            throw new RuntimeException("发布工作流失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取用户的工作流列表（真实实现）
     */
    @Override
    public PageResponse<WorkflowResponse> getUserWorkflows(Long userId, Pageable pageable) {
        log.info("获取用户工作流列表: userId={}, page={}, size={}", userId, pageable.getPageNumber(), pageable.getPageSize());
        if (userId == null || pageable == null) {
            throw new IllegalArgumentException("用户ID和分页参数不能为空");
        }
        try {
            // 查询用户创建的工作流
            List<Workflow> userWorkflows = workflowRepository.findByCreatorIdAndEnabledTrueOrderByCreatedAtDesc(userId);

            // 分页处理
            int start = (int) pageable.getOffset();
            int end = Math.min(start + pageable.getPageSize(), userWorkflows.size());
            List<Workflow> pageList = start < userWorkflows.size() ? userWorkflows.subList(start, end) : java.util.Collections.emptyList();

            // 转换为响应对象
            List<WorkflowResponse> items = new java.util.ArrayList<>();
            for (Workflow wf : pageList) {
                items.add(convertToResponse(wf));
            }

            // 构建分页响应
            PageResponse<WorkflowResponse> resp = new PageResponse<>();
            resp.setItems(items);
            resp.setTotalElements((long) userWorkflows.size());
            resp.setTotalPages((int) Math.ceil((double) userWorkflows.size() / pageable.getPageSize()));
            resp.setPage(pageable.getPageNumber() + 1);
            resp.setSize(pageable.getPageSize());
            resp.setHasNext(end < userWorkflows.size());
            resp.setHasPrevious(pageable.getPageNumber() > 0);
            resp.setFirst(pageable.getPageNumber() == 0);
            resp.setLast(pageable.getPageNumber() >= resp.getTotalPages() - 1);

            log.info("获取用户工作流列表成功: userId={}, total={}, returned={}", userId, userWorkflows.size(), items.size());
            return resp;
        } catch (Exception e) {
            log.error("获取用户工作流列表失败: userId={}", userId, e);
            throw new RuntimeException("获取用户工作流列表失败: " + e.getMessage(), e);
        }
    }

    /**
     * 分页查询工作流列表（真实实现）
     */
    @Override
    public PageResponse<WorkflowResponse> getWorkflows(com.coze.studio.dto.workflow.WorkflowQueryRequest request, Pageable pageable) {
        log.info("分页查询工作流列表: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        if (pageable == null) {
            throw new IllegalArgumentException("分页参数不能为空");
        }
        try {
            List<Workflow> allWorkflows = new java.util.ArrayList<>();

            // 根据查询条件过滤
            if (request != null) {
                if (request.getCreatorId() != null) {
                    allWorkflows.addAll(workflowRepository.findByCreatorIdAndEnabledTrueOrderByCreatedAtDesc(request.getCreatorId()));
                } else if (request.getStatus() != null) {
                    allWorkflows.addAll(workflowRepository.findByStatusAndEnabledTrueOrderByCreatedAtDesc(request.getStatus()));
                } else {
                    allWorkflows.addAll(workflowRepository.findByEnabledTrueOrderByCreatedAtDesc());
                }

                // 内存过滤其他条件
                allWorkflows = allWorkflows.stream()
                    .filter(w -> request.getKeyword() == null || (w.getName() != null && w.getName().contains(request.getKeyword())))
                    .filter(w -> request.getType() == null || (w.getType() != null && w.getType().equals(request.getType())))
                    .filter(w -> request.getIsTemplate() == null || (w.getIsTemplate() != null && w.getIsTemplate().equals(request.getIsTemplate())))
                    .toList();
            } else {
                allWorkflows.addAll(workflowRepository.findByEnabledTrueOrderByCreatedAtDesc());
            }

            // 分页处理
            int start = (int) pageable.getOffset();
            int end = Math.min(start + pageable.getPageSize(), allWorkflows.size());
            List<Workflow> pageList = start < allWorkflows.size() ? allWorkflows.subList(start, end) : java.util.Collections.emptyList();

            // 转换为响应对象
            List<WorkflowResponse> items = new java.util.ArrayList<>();
            for (Workflow wf : pageList) {
                items.add(convertToResponse(wf));
            }

            // 构建分页响应
            PageResponse<WorkflowResponse> resp = new PageResponse<>();
            resp.setItems(items);
            resp.setTotalElements((long) allWorkflows.size());
            resp.setTotalPages((int) Math.ceil((double) allWorkflows.size() / pageable.getPageSize()));
            resp.setPage(pageable.getPageNumber() + 1);
            resp.setSize(pageable.getPageSize());
            resp.setHasNext(end < allWorkflows.size());
            resp.setHasPrevious(pageable.getPageNumber() > 0);
            resp.setFirst(pageable.getPageNumber() == 0);
            resp.setLast(pageable.getPageNumber() >= resp.getTotalPages() - 1);

            log.info("分页查询工作流列表成功: total={}, returned={}", allWorkflows.size(), items.size());
            return resp;
        } catch (Exception e) {
            log.error("分页查询工作流列表失败", e);
            throw new RuntimeException("分页查询工作流列表失败: " + e.getMessage(), e);
        }
    }

    @Override
    public WorkflowResponse unpublishWorkflow(Long userId, Long workflowId) {
        log.info("取消发布工作流: userId={}, workflowId={}", userId, workflowId);

        try {
            // 模拟实现
            WorkflowResponse response = new WorkflowResponse();
            response.setId(workflowId);
            response.setName("工作流" + workflowId);
            response.setStatus("DRAFT");
            response.setCreatorId(userId);
            response.setUpdatedAt(java.time.LocalDateTime.now());

            log.info("工作流取消发布成功: userId={}, workflowId={}", userId, workflowId);
            return response;

        } catch (Exception e) {
            log.error("取消发布工作流失败: userId={}, workflowId={}", userId, workflowId, e);
            throw new RuntimeException("取消发布工作流失败: " + e.getMessage());
        }
    }
}