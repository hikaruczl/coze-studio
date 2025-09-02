package com.coze.studio.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coze.studio.entity.Bot;
import org.apache.ibatis.annotations.Mapper;

/**
 * Bot Mapper接口
 */
@Mapper
public interface BotMapper extends BaseMapper<Bot> {
    
    // MyBatis-Plus提供了基础的CRUD操作
    // 自定义查询方法可以在这里添加
    
}