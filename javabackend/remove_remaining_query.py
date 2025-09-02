#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import os
import re

def remove_remaining_annotations(file_path):
    """移除剩余的所有JPA注解"""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        # 移除所有@Query注解（包括多行的）
        content = re.sub(r'@Query\([^)]*\)\s*\n', '', content)
        content = re.sub(r'@Query\s*\n', '', content)
        
        # 移除其他JPA注解
        content = re.sub(r'@ElementCollection[^)]*\)\s*\n', '', content)
        content = re.sub(r'@CollectionTable[^)]*\)\s*\n', '', content)
        content = re.sub(r'@JoinColumn[^)]*\)\s*\n', '', content)
        
        # 清理多余的空行
        content = re.sub(r'\n\s*\n\s*\n+', '\n\n', content)
        
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(content)
        
        return True
    except Exception as e:
        print(f"Error cleaning {file_path}: {e}")
        return False

def main():
    """主函数"""
    print("开始移除剩余的JPA注解...")
    
    # 处理所有Java文件
    for root, dirs, files in os.walk("src/main/java"):
        for file in files:
            if file.endswith('.java'):
                file_path = os.path.join(root, file)
                if remove_remaining_annotations(file_path):
                    print(f"Processed: {file}")
    
    print("\n清理完成！")

if __name__ == "__main__":
    main()
