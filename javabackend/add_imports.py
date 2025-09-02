#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import os
import re

def add_imports_to_repository(file_path):
    """为Repository接口添加必要的import语句"""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        # 检查是否已经有必要的import
        if 'import java.util.Optional;' in content:
            print(f"Skipping {file_path} - already has imports")
            return True
        
        # 在package声明后添加import语句
        lines = content.split('\n')
        new_lines = []
        package_found = False
        imports_added = False
        
        for line in lines:
            new_lines.append(line)
            
            if line.startswith('package ') and not imports_added:
                package_found = True
            elif package_found and line.strip() == '' and not imports_added:
                # 在package后的第一个空行添加imports
                new_lines.extend([
                    'import java.util.List;',
                    'import java.util.Optional;',
                    ''
                ])
                imports_added = True
        
        content = '\n'.join(new_lines)
        
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(content)
        
        print(f"Added imports to: {file_path}")
        return True
    except Exception as e:
        print(f"Error processing {file_path}: {e}")
        return False

def main():
    """主函数"""
    print("开始为Repository接口添加import语句...")
    
    repository_dir = "src/main/java/com/coze/studio/repository"
    
    for root, dirs, files in os.walk(repository_dir):
        for file in files:
            if file.endswith('.java'):
                file_path = os.path.join(root, file)
                add_imports_to_repository(file_path)
    
    print("Import语句添加完成！")

if __name__ == "__main__":
    main()
