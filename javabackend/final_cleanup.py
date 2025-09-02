#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import os
import re

def clean_repository_file(file_path):
    """彻底清理Repository文件中的语法错误"""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        lines = content.split('\n')
        clean_lines = []
        i = 0
        
        while i < len(lines):
            line = lines[i]
            
            # 跳过包含@Param但没有方法声明开始的行（这些是残留的参数行）
            if '@Param(' in line and not line.strip().endswith('{') and not 'interface' in line:
                i += 1
                continue
            
            # 跳过包含Pageable但没有完整方法声明的行
            if 'Pageable' in line and not line.strip().startswith('//') and ';' in line:
                i += 1
                continue
            
            # 跳过明显的语法错误行
            if ('错误:' in line or 
                line.strip().startswith('@Param(') or
                (line.strip().endswith(',') and '@Param(' in line) or
                (line.strip() == 'Pageable pageable);')):
                i += 1
                continue
            
            clean_lines.append(line)
            i += 1
        
        content = '\n'.join(clean_lines)
        
        # 移除多余的空行
        content = re.sub(r'\n\s*\n\s*\n+', '\n\n', content)
        
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(content)
        
        print(f"Cleaned: {file_path}")
        return True
    except Exception as e:
        print(f"Error cleaning {file_path}: {e}")
        return False

def main():
    """主函数"""
    repository_dir = "src/main/java/com/coze/studio/repository"
    
    # 有语法错误的文件
    error_files = [
        "PromptTemplateRepository.java",
        "DocumentSliceRepository.java", 
        "ModelConfigurationRepository.java",
        "ModelProviderRepository.java",
        "WorkflowExecutionRepository.java",
        "WorkflowTemplateRepository.java",
        "KnowledgeBaseRepository.java",
        "ModelUsageRecordRepository.java",
        "WorkflowRepository.java"
    ]
    
    for file_name in error_files:
        file_path = os.path.join(repository_dir, file_name)
        if os.path.exists(file_path):
            clean_repository_file(file_path)

if __name__ == "__main__":
    main()
