package com.coze.studio.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coze.studio.entity.WorkflowNode;
import org.apache.ibatis.annotations.Mapper;

/**
 * WorkflowNode Mapper接口
 */
@Mapper
public interface WorkflowNodeMapper extends BaseMapper<WorkflowNode> {
    
    // MyBatis-Plus提供了基础的CRUD操作
    // 自定义查询方法可以在这里添加
    
}