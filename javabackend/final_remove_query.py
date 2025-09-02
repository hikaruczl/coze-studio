#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import os
import re

def remove_all_query_lines(file_path):
    """移除所有包含@Query的行"""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            lines = f.readlines()
        
        new_lines = []
        for line in lines:
            # 跳过包含@Query的行
            if '@Query' in line:
                continue
            # 跳过包含@CollectionTable或@JoinColumn的行
            if '@CollectionTable' in line or '@JoinColumn' in line:
                continue
            new_lines.append(line)
        
        with open(file_path, 'w', encoding='utf-8') as f:
            f.writelines(new_lines)
        
        return True
    except Exception as e:
        print(f"Error cleaning {file_path}: {e}")
        return False

def main():
    """主函数"""
    print("开始移除所有@Query行...")
    
    # 处理所有Repository文件
    repository_dir = "src/main/java/com/coze/studio/repository"
    for root, dirs, files in os.walk(repository_dir):
        for file in files:
            if file.endswith('.java'):
                file_path = os.path.join(root, file)
                if remove_all_query_lines(file_path):
                    print(f"Processed: {file}")
    
    # 处理Bot实体文件
    bot_file = "src/main/java/com/coze/studio/entity/Bot.java"
    if os.path.exists(bot_file):
        remove_all_query_lines(bot_file)
        print(f"Processed: Bot.java")
    
    print("\n清理完成！")

if __name__ == "__main__":
    main()
