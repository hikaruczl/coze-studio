#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import os
import re

def remove_all_jpa_annotations(file_path):
    """彻底移除所有JPA注解"""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        # 移除所有JPA注解（单行）
        content = re.sub(r'@Column[^)]*\)\s*\n', '', content)
        content = re.sub(r'@Lob\s*\n', '', content)
        content = re.sub(r'@Enumerated[^)]*\)\s*\n', '', content)
        content = re.sub(r'@CreatedDate\s*\n', '', content)
        content = re.sub(r'@LastModifiedDate\s*\n', '', content)
        content = re.sub(r'@PrePersist\s*\n', '', content)
        content = re.sub(r'@PreUpdate\s*\n', '', content)
        content = re.sub(r'@ManyToOne[^)]*\)\s*\n', '', content)
        content = re.sub(r'@OneToMany[^)]*\)\s*\n', '', content)
        content = re.sub(r'@JoinColumn[^)]*\)\s*\n', '', content)
        content = re.sub(r'@Query[^)]*\)\s*\n', '', content)
        content = re.sub(r'@Modifying\s*\n', '', content)
        
        # 移除多行@Query注解
        content = re.sub(r'@Query\([^)]*"[^"]*"\s*\+[^)]*\)\s*\n', '', content)
        
        # 清理多余的空行
        content = re.sub(r'\n\s*\n\s*\n+', '\n\n', content)
        
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(content)
        
        return True
    except Exception as e:
        print(f"Error cleaning {file_path}: {e}")
        return False

def clean_all_entities():
    """清理所有实体类"""
    entity_dir = "src/main/java/com/coze/studio/entity"
    
    for root, dirs, files in os.walk(entity_dir):
        for file in files:
            if file.endswith('.java'):
                file_path = os.path.join(root, file)
                if remove_all_jpa_annotations(file_path):
                    print(f"Cleaned entity: {file}")

def clean_all_repositories():
    """清理所有Repository接口"""
    repository_dir = "src/main/java/com/coze/studio/repository"
    
    for root, dirs, files in os.walk(repository_dir):
        for file in files:
            if file.endswith('.java'):
                file_path = os.path.join(root, file)
                if remove_all_jpa_annotations(file_path):
                    print(f"Cleaned repository: {file}")

def main():
    """主函数"""
    print("开始彻底清理所有JPA注解...")
    
    # 1. 清理所有实体类
    print("\n1. 清理所有实体类...")
    clean_all_entities()
    
    # 2. 清理所有Repository接口
    print("\n2. 清理所有Repository接口...")
    clean_all_repositories()
    
    print("\n彻底清理完成！")

if __name__ == "__main__":
    main()
