#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import os
import re

def fix_syntax_errors(file_path):
    """修复语法错误"""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            lines = f.readlines()
        
        new_lines = []
        i = 0
        
        while i < len(lines):
            line = lines[i]
            
            # 检查是否是有语法错误的行（包含方法参数但没有完整的方法声明）
            if (line.strip().startswith('List<') or 
                line.strip().startswith('Optional<') or
                line.strip().startswith('boolean ') or
                line.strip().startswith('int ') or
                line.strip().startswith('long ') or
                line.strip().startswith('String ') or
                line.strip().startswith('Double ') or
                line.strip().startswith('void ')) and not line.strip().endswith(';'):
                
                # 这可能是一个不完整的方法声明，跳过它
                continue
            
            # 检查是否是孤立的参数行
            if (('@Param(' in line or 'Pageable' in line) and 
                not line.strip().startswith('//') and
                not 'interface' in line and
                not 'class' in line):
                continue
            
            new_lines.append(line)
            i += 1
        
        with open(file_path, 'w', encoding='utf-8') as f:
            f.writelines(new_lines)
        
        return True
    except Exception as e:
        print(f"Error fixing {file_path}: {e}")
        return False

def main():
    """主函数"""
    print("开始修复语法错误...")
    
    # 有语法错误的文件列表
    error_files = [
        "src/main/java/com/coze/studio/repository/PromptTemplateRepository.java",
        "src/main/java/com/coze/studio/repository/PluginUsageRepository.java",
        "src/main/java/com/coze/studio/repository/WorkflowTemplateRatingRepository.java",
        "src/main/java/com/coze/studio/repository/PluginInstallationRepository.java",
        "src/main/java/com/coze/studio/repository/KnowledgeQueryRepository.java",
        "src/main/java/com/coze/studio/repository/DocumentProcessingTaskRepository.java",
        "src/main/java/com/coze/studio/repository/WorkflowConnectionRepository.java",
        "src/main/java/com/coze/studio/repository/ModelProviderRepository.java",
        "src/main/java/com/coze/studio/repository/VectorStoreRepository.java",
        "src/main/java/com/coze/studio/repository/PromptCategoryRepository.java",
        "src/main/java/com/coze/studio/repository/PluginRatingRepository.java",
        "src/main/java/com/coze/studio/repository/WorkflowTemplateUsageRepository.java"
    ]
    
    for file_path in error_files:
        if os.path.exists(file_path):
            if fix_syntax_errors(file_path):
                print(f"Fixed: {file_path}")
    
    print("\n语法错误修复完成！")

if __name__ == "__main__":
    main()
