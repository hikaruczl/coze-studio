#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import os

def remove_orphan_lines(file_path):
    """移除孤立的SQL字符串行"""
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
            # 移除孤立的SQL字符串行（以引号开头但不是完整语句的行）
            if (line.strip().startswith('"') and 
                line.strip().endswith('")') and
                not line.strip().startswith('//') and
                not '@Query(' in line):
                continue
            
            # 移除其他孤立的SQL片段
            if (line.strip() in [
                '"GROUP BY mc.type")',
                '"ORDER BY mc.performanceScore DESC")',
                '"AND e.status = :status ORDER BY e.startTime DESC")',
                '"AND kb.enabled = true")',
                '"ORDER BY mur.createdAt DESC")',
                '"ORDER BY w.executionCount DESC")'
            ]):
                continue
            
            clean_lines.append(line)
        
        content = '\n'.join(clean_lines)
        
        # 使用二进制模式写入，避免BOM问题
        with open(file_path, 'wb') as f:
            f.write(content.encode('utf-8'))
        
        print(f"Removed orphan lines from: {file_path}")
        return True
    except Exception as e:
        print(f"Error processing {file_path}: {e}")
        return False

def main():
    """主函数"""
    repository_dir = "src/main/java/com/coze/studio/repository"
    
    # 需要清理的文件
    files_to_clean = [
        "ModelConfigurationRepository.java",
        "WorkflowExecutionRepository.java",
        "KnowledgeBaseRepository.java",
        "ModelUsageRecordRepository.java",
        "WorkflowRepository.java"
    ]
    
    for file_name in files_to_clean:
        file_path = os.path.join(repository_dir, file_name)
        if os.path.exists(file_path):
            remove_orphan_lines(file_path)

if __name__ == "__main__":
    main()
