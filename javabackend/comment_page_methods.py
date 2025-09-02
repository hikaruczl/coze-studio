#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import os
import re

def comment_page_methods(file_path):
    """注释掉Repository文件中的Page和Pageable方法"""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        lines = content.split('\n')
        new_lines = []
        i = 0
        
        while i < len(lines):
            line = lines[i].strip()
            
            # 检查是否是包含Page或Pageable的方法声明行
            if ('Page<' in line or 'Pageable' in line) and ('(' in line):
                # 如果这行还没有被注释
                if not line.startswith('//'):
                    # 找到方法的开始（可能包括注释）
                    method_start = i
                    
                    # 向上查找方法的注释块
                    j = i - 1
                    while j >= 0:
                        prev_line = lines[j].strip()
                        if prev_line.startswith('/**') or prev_line.startswith('*') or prev_line.startswith('*/') or prev_line == '':
                            j -= 1
                        else:
                            break
                    
                    if j >= 0 and lines[j].strip().startswith('/**'):
                        method_start = j
                    elif j + 1 < i and lines[j + 1].strip().startswith('/**'):
                        method_start = j + 1
                    
                    # 注释掉从方法开始到当前行的所有内容
                    for k in range(method_start, i + 1):
                        original_line = lines[k]
                        if not original_line.strip().startswith('//') and original_line.strip():
                            # 保持原有的缩进，在前面加上 //
                            indent = len(original_line) - len(original_line.lstrip())
                            lines[k] = ' ' * indent + '// ' + original_line.strip()
                            if k == i:  # 在方法声明行后面加上TODO注释
                                lines[k] += ' // TODO: 需要在MyBatis XML中实现分页'
            
            new_lines.append(lines[i])
            i += 1
        
        content = '\n'.join(new_lines)
        
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(content)
        
        print(f"Commented Page methods in: {file_path}")
        return True
    except Exception as e:
        print(f"Error processing {file_path}: {e}")
        return False

def main():
    """主函数"""
    repository_dir = "src/main/java/com/coze/studio/repository"
    
    # 所有Repository文件
    repository_files = [
        "PromptTemplateRepository.java",
        "WorkflowTemplateRatingRepository.java",
        "ConversationVariableRepository.java",
        "DocumentSliceRepository.java",
        "WorkflowNodeRepository.java",
        "WorkflowConnectionRepository.java",
        "WorkflowTemplateRepository.java",
        "WorkflowTemplateFavoriteRepository.java",
        "WorkflowTemplateUsageRepository.java",
        "KnowledgeQueryRepository.java",
        "ModelConfigurationRepository.java",
        "DocumentProcessingTaskRepository.java",
        "PluginRatingRepository.java",
        "ModelProviderRepository.java",
        "VectorStoreRepository.java",
        "PromptCategoryRepository.java",
        "ModelUsageRecordRepository.java",
        "WorkflowVariableRepository.java",
        "WorkflowExecutionRepository.java",
        "WorkflowNodeExecutionRepository.java", 
        "PluginUsageRepository.java",
        "WorkflowRepository.java",
        "UserRepository.java",
        "MessageRepository.java",
        "BotRepository.java",
        "DocumentRepository.java",
        "KnowledgeBaseRepository.java"
    ]
    
    fixed_count = 0
    for file_name in repository_files:
        file_path = os.path.join(repository_dir, file_name)
        if os.path.exists(file_path):
            if comment_page_methods(file_path):
                fixed_count += 1
        else:
            print(f"File not found: {file_path}")
    
    print(f"\nProcessed {fixed_count} repository files")

if __name__ == "__main__":
    main()
