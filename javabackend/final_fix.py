#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import os
import re

def final_fix_repository(file_path):
    """最终修复Repository文件"""
    try:
        # 使用二进制模式读取，避免BOM问题
        with open(file_path, 'rb') as f:
            content_bytes = f.read()
        
        # 移除BOM字符
        if content_bytes.startswith(b'\xef\xbb\xbf'):
            content_bytes = content_bytes[3:]
        
        # 转换为字符串
        content = content_bytes.decode('utf-8')
        
        lines = content.split('\n')
        clean_lines = []
        
        for line in lines:
            # 跳过包含残留@Query的行
            if line.strip().startswith('@Query(') and not line.strip().endswith(')'):
                continue
            
            # 跳过包含残留参数的行
            if ('@Param(' in line and 
                not line.strip().startswith('//') and 
                not 'interface' in line and
                not line.strip().endswith('{')):
                continue
            
            # 跳过单独的Pageable参数行
            if line.strip() == 'Pageable pageable);':
                continue
            
            # 跳过包含复杂SQL字符串但没有完整方法的行
            if (('SELECT ' in line or 'ORDER BY' in line or 'WHERE ' in line) and 
                line.strip().startswith('"') and 
                not line.strip().startswith('//') and
                not '@Query(' in line):
                continue
            
            clean_lines.append(line)
        
        content = '\n'.join(clean_lines)
        
        # 移除多余的空行
        content = re.sub(r'\n\s*\n\s*\n+', '\n\n', content)
        
        # 使用二进制模式写入，避免BOM问题
        with open(file_path, 'wb') as f:
            f.write(content.encode('utf-8'))
        
        print(f"Final fixed: {file_path}")
        return True
    except Exception as e:
        print(f"Error fixing {file_path}: {e}")
        return False

def main():
    """主函数"""
    repository_dir = "src/main/java/com/coze/studio/repository"
    
    # 需要最终修复的文件
    files_to_fix = [
        "DocumentSliceRepository.java",
        "ModelConfigurationRepository.java", 
        "ModelUsageRecordRepository.java",
        "WorkflowExecutionRepository.java",
        "WorkflowTemplateRepository.java",
        "KnowledgeBaseRepository.java",
        "WorkflowRepository.java"
    ]
    
    for file_name in files_to_fix:
        file_path = os.path.join(repository_dir, file_name)
        if os.path.exists(file_path):
            final_fix_repository(file_path)

if __name__ == "__main__":
    main()
