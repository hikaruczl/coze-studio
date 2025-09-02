#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import os
import re

def remove_sql_strings(file_path):
    """移除残留的SQL字符串"""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            lines = f.readlines()
        
        new_lines = []
        i = 0
        
        while i < len(lines):
            line = lines[i].strip()
            
            # 跳过包含SQL字符串的行（以引号开始，包含SQL关键字）
            if (line.startswith('"') and 
                ('SELECT' in line or 'UPDATE' in line or 'DELETE' in line or 
                 'FROM' in line or 'WHERE' in line or 'ORDER BY' in line or
                 'GROUP BY' in line or 'LIKE' in line or 'AND' in line)):
                i += 1
                continue
            
            # 跳过以引号开始的多行SQL字符串
            if line.startswith('"') and line.endswith('" +'):
                # 跳过整个多行SQL字符串
                while i < len(lines):
                    current_line = lines[i].strip()
                    i += 1
                    if current_line.endswith('")') or current_line.endswith('"'):
                        break
                continue
            
            # 跳过中间的SQL字符串行
            if (line.startswith('"') and line.endswith('" +')) or line.endswith('")'):
                i += 1
                continue
            
            new_lines.append(lines[i])
            i += 1
        
        with open(file_path, 'w', encoding='utf-8') as f:
            f.writelines(new_lines)
        
        return True
    except Exception as e:
        print(f"Error cleaning {file_path}: {e}")
        return False

def main():
    """主函数"""
    print("开始移除残留的SQL字符串...")
    
    # 处理所有Repository文件
    repository_dir = "src/main/java/com/coze/studio/repository"
    for root, dirs, files in os.walk(repository_dir):
        for file in files:
            if file.endswith('.java'):
                file_path = os.path.join(root, file)
                if remove_sql_strings(file_path):
                    print(f"Processed: {file}")
    
    print("\nSQL字符串清理完成！")

if __name__ == "__main__":
    main()
