package com.coze.studio.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coze.studio.entity.Conversation;
import org.apache.ibatis.annotations.Mapper;

/**
 * Conversation Mapper接口
 */
@Mapper
public interface ConversationMapper extends BaseMapper<Conversation> {
    
    // MyBatis-Plus提供了基础的CRUD操作
    // 自定义查询方法可以在这里添加
    
}