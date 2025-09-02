#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import os
import re

def ultimate_cleanup(file_path):
    """终极清理Repository文件"""
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
        i = 0
        
        while i < len(lines):
            line = lines[i]
            
            # 跳过所有包含SQL关键字但不是完整方法的行
            if (line.strip().startswith('"') and 
                ('SELECT ' in line or 'WHERE ' in line or 'ORDER BY' in line or 
                 'AND ' in line or 'FROM ' in line or 'JOIN ' in line) and
                not line.strip().startswith('//') and
                not '@Query(' in line):
                i += 1
                continue
            
            # 跳过包含@Query但不完整的行
            if '@Query(' in line and not line.strip().endswith(')'):
                i += 1
                continue
            
            # 跳过包含@Param但不是完整方法的行
            if ('@Param(' in line and 
                not line.strip().startswith('//') and 
                not 'interface' in line and
                not line.strip().endswith('{')):
                i += 1
                continue
            
            # 跳过单独的Pageable参数行
            if 'Pageable' in line and ';' in line and not line.strip().startswith('//'):
                i += 1
                continue
            
            # 跳过包含复杂SQL片段的行
            if (line.strip().endswith('" +') or 
                line.strip().endswith('") +') or
                (line.strip().startswith('"') and line.strip().endswith('"'))):
                i += 1
                continue
            
            clean_lines.append(line)
            i += 1
        
        content = '\n'.join(clean_lines)
        
        # 移除多余的空行
        content = re.sub(r'\n\s*\n\s*\n+', '\n\n', content)
        
        # 使用二进制模式写入，避免BOM问题
        with open(file_path, 'wb') as f:
            f.write(content.encode('utf-8'))
        
        print(f"Ultimate cleaned: {file_path}")
        return True
    except Exception as e:
        print(f"Error cleaning {file_path}: {e}")
        return False

def main():
    """主函数"""
    repository_dir = "src/main/java/com/coze/studio/repository"
    
    # 需要终极清理的文件
    files_to_clean = [
        "DocumentSliceRepository.java",
        "ModelConfigurationRepository.java", 
        "ModelUsageRecordRepository.java",
        "WorkflowExecutionRepository.java",
        "WorkflowTemplateRepository.java",
        "KnowledgeBaseRepository.java",
        "WorkflowRepository.java"
    ]
    
    for file_name in files_to_clean:
        file_path = os.path.join(repository_dir, file_name)
        if os.path.exists(file_path):
            ultimate_cleanup(file_path)

if __name__ == "__main__":
    main()
