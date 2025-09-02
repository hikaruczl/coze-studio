#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import os
import re

def fix_repository_completely(file_path):
    """完全修复Repository文件"""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        # 替换import语句
        content = re.sub(r'import org\.springframework\.data\.domain\.Page;', '', content)
        content = re.sub(r'import org\.springframework\.data\.domain\.Pageable;', '', content)
        content = re.sub(r'import org\.springframework\.data\.jpa\.repository\.JpaRepository;', 'import org.apache.ibatis.annotations.Mapper;', content)
        content = re.sub(r'import org\.springframework\.data\.jpa\.repository\.Modifying;', '', content)
        content = re.sub(r'import org\.springframework\.data\.jpa\.repository\.Query;', '', content)
        content = re.sub(r'import org\.springframework\.data\.repository\.query\.Param;', 'import org.apache.ibatis.annotations.Param;', content)
        content = re.sub(r'import org\.springframework\.stereotype\.Repository;', '', content)
        
        # 替换类声明
        content = re.sub(r'@Repository\s*\n', '', content)
        content = re.sub(r'public interface (\w+) extends JpaRepository<\w+, \w+> \{', r'@Mapper\npublic interface \1 {', content)
        
        # 移除所有@Query和@Modifying注解
        content = re.sub(r'@Query\([^)]*\)\s*\n', '', content)
        content = re.sub(r'@Modifying\s*\n', '', content)
        
        lines = content.split('\n')
        clean_lines = []
        i = 0
        
        while i < len(lines):
            line = lines[i]
            
            # 跳过包含Page或Pageable的方法声明
            if ('Page<' in line or 'Pageable' in line) and ('(' in line):
                # 跳过这个方法的所有行直到找到分号或大括号
                while i < len(lines) and not (';' in lines[i] or '{' in lines[i]):
                    i += 1
                if i < len(lines):
                    i += 1  # 跳过包含分号或大括号的行
                continue
            
            # 跳过不完整的方法声明（只有注释没有方法体）
            if (line.strip().startswith('/**') and 
                i + 1 < len(lines) and 
                lines[i + 1].strip().startswith('*') and
                i + 2 < len(lines) and
                lines[i + 2].strip() == '*/'):
                # 检查后面是否有完整的方法声明
                j = i + 3
                while j < len(lines) and lines[j].strip() == '':
                    j += 1
                if j >= len(lines) or not ('(' in lines[j] and ';' in lines[j]):
                    # 这是一个不完整的方法，跳过整个注释块
                    i = j
                    continue
            
            # 跳过残留的@Param行
            if '@Param(' in line and not line.strip().endswith('{'):
                i += 1
                continue
            
            # 跳过孤立的参数行
            if (line.strip().endswith(',') and 
                ('@Param(' in line or 'Pageable' in line)):
                i += 1
                continue
            
            clean_lines.append(line)
            i += 1
        
        content = '\n'.join(clean_lines)
        
        # 清理多余的空行
        content = re.sub(r'\n\s*\n\s*\n+', '\n\n', content)
        
        # 确保文件以正确的方式结束
        if not content.endswith('\n'):
            content += '\n'
        
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(content)
        
        print(f"Completely fixed: {file_path}")
        return True
    except Exception as e:
        print(f"Error fixing {file_path}: {e}")
        return False

def main():
    """主函数"""
    repository_dir = "src/main/java/com/coze/studio/repository"
    
    # 需要修复的文件
    files_to_fix = [
        "WorkflowExecutionRepository.java",
        "WorkflowTemplateRepository.java", 
        "KnowledgeBaseRepository.java",
        "ModelUsageRecordRepository.java",
        "WorkflowRepository.java"
    ]
    
    for file_name in files_to_fix:
        file_path = os.path.join(repository_dir, file_name)
        if os.path.exists(file_path):
            fix_repository_completely(file_path)

if __name__ == "__main__":
    main()
