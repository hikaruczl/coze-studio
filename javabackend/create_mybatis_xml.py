#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import os

def create_mybatis_xml_files():
    """创建MyBatis XML映射文件"""
    
    # 创建mapper目录
    mapper_dir = "src/main/resources/mapper"
    os.makedirs(mapper_dir, exist_ok=True)
    
    # Repository接口列表
    repositories = [
        "WorkflowRepository",
        "WorkflowNodeRepository", 
        "WorkflowConnectionRepository",
        "WorkflowExecutionRepository",
        "WorkflowNodeExecutionRepository",
        "WorkflowVariableRepository",
        "ConversationVariableRepository",
        "ConversationRepository",
        "WorkflowTemplateRepository",
        "WorkflowTemplateFavoriteRepository",
        "WorkflowTemplateRatingRepository",
        "WorkflowTemplateUsageRepository",
        "KnowledgeQueryRepository",
        "DocumentSliceRepository",
        "DocumentRepository",
        "BotRepository",
        "UserRepository",
        "MessageRepository"
    ]
    
    for repo_name in repositories:
        xml_file = os.path.join(mapper_dir, f"{repo_name}.xml")
        
        # 生成XML内容
        xml_content = f'''<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" 
    "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.coze.studio.repository.{repo_name}">

    <!-- 基础CRUD操作 -->
    
    <!-- 保存实体 -->
    <insert id="save" parameterType="Object" useGeneratedKeys="true" keyProperty="id">
        <!-- TODO: 实现保存逻辑 -->
        INSERT INTO table_name (column1, column2) VALUES (#{{field1}}, #{{field2}})
    </insert>
    
    <!-- 根据ID查找 -->
    <select id="findById" parameterType="Long" resultType="Object">
        <!-- TODO: 实现查找逻辑 -->
        SELECT * FROM table_name WHERE id = #{{id}}
    </select>
    
    <!-- 查找所有 -->
    <select id="findAll" resultType="Object">
        <!-- TODO: 实现查找所有逻辑 -->
        SELECT * FROM table_name
    </select>
    
    <!-- 检查是否存在 -->
    <select id="existsById" parameterType="Long" resultType="boolean">
        <!-- TODO: 实现存在检查逻辑 -->
        SELECT COUNT(*) > 0 FROM table_name WHERE id = #{{id}}
    </select>
    
    <!-- 计数 -->
    <select id="count" resultType="long">
        <!-- TODO: 实现计数逻辑 -->
        SELECT COUNT(*) FROM table_name
    </select>
    
    <!-- 删除 -->
    <delete id="deleteById" parameterType="Long">
        <!-- TODO: 实现删除逻辑 -->
        DELETE FROM table_name WHERE id = #{{id}}
    </delete>
    
    <!-- 自定义查询方法将在这里添加 -->
    <!-- TODO: 根据具体Repository接口添加相应的查询方法 -->
    
</mapper>'''
        
        with open(xml_file, 'w', encoding='utf-8') as f:
            f.write(xml_content)
        
        print(f"Created: {xml_file}")

def main():
    """主函数"""
    print("开始创建MyBatis XML映射文件...")
    create_mybatis_xml_files()
    print("MyBatis XML映射文件创建完成！")

if __name__ == "__main__":
    main()
