#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import os

def create_mapper_interfaces():
    """创建MyBatis-Plus Mapper接口"""
    
    # 创建mapper目录
    mapper_dir = "src/main/java/com/coze/studio/mapper"
    os.makedirs(mapper_dir, exist_ok=True)
    
    # Mapper接口列表
    mappers = [
        ("WorkflowMapper", "Workflow"),
        ("WorkflowNodeMapper", "WorkflowNode"),
        ("WorkflowConnectionMapper", "WorkflowConnection"),
        ("WorkflowExecutionMapper", "WorkflowExecution"),
        ("WorkflowNodeExecutionMapper", "WorkflowNodeExecution"),
        ("WorkflowTemplateMapper", "WorkflowTemplate"),
        ("WorkflowTemplateFavoriteMapper", "WorkflowTemplateFavorite"),
        ("WorkflowTemplateRatingMapper", "WorkflowTemplateRating"),
        ("WorkflowTemplateUsageMapper", "WorkflowTemplateUsage"),
        ("ConversationMapper", "Conversation"),
        ("ConversationVariableMapper", "ConversationVariable"),
        ("WorkflowVariableMapper", "WorkflowVariable"),
        ("KnowledgeQueryMapper", "KnowledgeQuery"),
        ("DocumentSliceMapper", "DocumentSlice"),
        ("DocumentMapper", "Document"),
        ("BotMapper", "Bot"),
        ("UserMapper", "User"),
        ("MessageMapper", "Message")
    ]
    
    for mapper_name, entity_name in mappers:
        mapper_file = os.path.join(mapper_dir, f"{mapper_name}.java")
        
        # 生成Mapper接口内容
        mapper_content = f'''package com.coze.studio.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coze.studio.entity.{entity_name};
import org.apache.ibatis.annotations.Mapper;

/**
 * {entity_name} Mapper接口
 */
@Mapper
public interface {mapper_name} extends BaseMapper<{entity_name}> {{
    
    // MyBatis-Plus提供了基础的CRUD操作
    // 自定义查询方法可以在这里添加
    
}}'''
        
        with open(mapper_file, 'w', encoding='utf-8') as f:
            f.write(mapper_content)
        
        print(f"Created: {mapper_file}")

def main():
    """主函数"""
    print("开始创建MyBatis-Plus Mapper接口...")
    create_mapper_interfaces()
    print("Mapper接口创建完成！")

if __name__ == "__main__":
    main()
