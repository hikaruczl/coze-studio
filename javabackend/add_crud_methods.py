#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import os
import re

def add_crud_methods_to_repository(file_path, entity_name):
    """为Repository接口添加基础CRUD方法"""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        # 检查是否已经有基础方法
        if 'save(' in content:
            print(f"Skipping {file_path} - already has CRUD methods")
            return True
        
        # 在接口结束前添加基础CRUD方法
        crud_methods = f'''
    // 基础CRUD方法
    {entity_name} save({entity_name} entity);
    
    Optional<{entity_name}> findById(Long id);
    
    List<{entity_name}> findAll();
    
    boolean existsById(Long id);
    
    long count();
    
    void deleteById(Long id);
'''
        
        # 在最后一个大括号前插入方法
        content = content.rstrip()
        if content.endswith('}'):
            content = content[:-1] + crud_methods + '\n}'
        else:
            content += crud_methods + '\n}'
        
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(content)
        
        print(f"Added CRUD methods to: {file_path}")
        return True
    except Exception as e:
        print(f"Error processing {file_path}: {e}")
        return False

def main():
    """主函数"""
    print("开始为Repository接口添加CRUD方法...")
    
    # Repository和对应的实体类映射
    repo_entity_mapping = {
        "WorkflowRepository.java": "Workflow",
        "WorkflowNodeRepository.java": "WorkflowNode", 
        "WorkflowConnectionRepository.java": "WorkflowConnection",
        "WorkflowExecutionRepository.java": "WorkflowExecution",
        "WorkflowNodeExecutionRepository.java": "WorkflowNodeExecution",
        "WorkflowVariableRepository.java": "WorkflowVariable",
        "ConversationVariableRepository.java": "ConversationVariable",
        "ConversationRepository.java": "Conversation",
        "WorkflowTemplateRepository.java": "WorkflowTemplate",
        "WorkflowTemplateFavoriteRepository.java": "WorkflowTemplateFavorite",
        "WorkflowTemplateRatingRepository.java": "WorkflowTemplateRating",
        "WorkflowTemplateUsageRepository.java": "WorkflowTemplateUsage",
        "KnowledgeQueryRepository.java": "KnowledgeQuery",
        "DocumentSliceRepository.java": "DocumentSlice",
        "DocumentRepository.java": "Document",
        "BotRepository.java": "Bot",
        "UserRepository.java": "User",
        "MessageRepository.java": "Message"
    }
    
    repository_dir = "src/main/java/com/coze/studio/repository"
    
    for repo_file, entity_name in repo_entity_mapping.items():
        file_path = os.path.join(repository_dir, repo_file)
        if os.path.exists(file_path):
            add_crud_methods_to_repository(file_path, entity_name)
    
    print("CRUD方法添加完成！")

if __name__ == "__main__":
    main()
