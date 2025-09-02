#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import os
import re

def fix_syntax_errors(file_path):
    """修复Repository文件中的语法错误"""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        lines = content.split('\n')
        new_lines = []
        i = 0
        
        while i < len(lines):
            line = lines[i]
            
            # 检查是否是有语法错误的注释行（包含方法参数但被错误注释）
            if line.strip().startswith('// ') and ('(' in line and ')' in line and ';' in line):
                # 这是一个被注释的方法，但可能有语法错误
                # 完全注释掉这一行，不保留方法签名
                indent = len(line) - len(line.lstrip())
                new_lines.append(' ' * indent + '// TODO: 分页方法需要在MyBatis XML中实现')
            elif '错误: 非法的类型开始' in line or '错误: 需要=' in line:
                # 跳过有语法错误的行
                continue
            else:
                new_lines.append(line)
            
            i += 1
        
        content = '\n'.join(new_lines)
        
        # 移除多余的空行
        content = re.sub(r'\n\s*\n\s*\n', '\n\n', content)
        
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(content)
        
        print(f"Fixed syntax errors in: {file_path}")
        return True
    except Exception as e:
        print(f"Error fixing {file_path}: {e}")
        return False

def fix_specific_files():
    """修复特定有语法错误的文件"""
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
            # 读取文件内容
            with open(file_path, 'r', encoding='utf-8') as f:
                content = f.read()
            
            # 移除所有包含语法错误的行
            lines = content.split('\n')
            clean_lines = []
            
            for line in lines:
                # 跳过包含复杂泛型或有语法问题的注释行
                if ('// ' in line and 
                    ('Page<' in line or 'Pageable' in line) and 
                    ('(' in line and ')' in line)):
                    # 替换为简单的TODO注释
                    indent = len(line) - len(line.lstrip())
                    clean_lines.append(' ' * indent + '// TODO: 分页方法需要在MyBatis XML中实现')
                else:
                    clean_lines.append(line)
            
            # 写回文件
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write('\n'.join(clean_lines))
            
            print(f"Cleaned syntax errors in: {file_path}")

def main():
    """主函数"""
    fix_specific_files()
    print("Fixed syntax errors in repository files")

if __name__ == "__main__":
    main()
