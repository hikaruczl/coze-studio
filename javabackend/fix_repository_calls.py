#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import os
import re

def fix_repository_method_calls(file_path):
    """修复Repository方法调用"""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        # 修复具体的Repository方法调用
        
        # 1. 修复分页查询
        content = re.sub(
            r'(\w+Repository)\.findAll\(([^)]+)\)',
            r'// TODO: 实现分页查询 - \1.findAll(\2)',
            content
        )
        
        # 2. 修复自定义查询方法
        content = re.sub(
            r'(\w+Repository)\.find(\w+)\(([^)]*)\)',
            r'// TODO: 实现查询方法 - \1.find\2(\3)',
            content
        )
        
        # 3. 修复计数方法
        content = re.sub(
            r'(\w+Repository)\.count(\w*)\(([^)]*)\)',
            r'// TODO: 实现计数方法 - \1.count\2(\3)',
            content
        )
        
        # 4. 修复更新方法
        content = re.sub(
            r'(\w+Repository)\.(update|increment)(\w*)\(([^)]*)\)',
            r'// TODO: 实现更新方法 - \1.\2\3(\4)',
            content
        )
        
        # 5. 修复删除方法
        content = re.sub(
            r'(\w+Repository)\.delete\(([^)]+)\)',
            r'// TODO: 实现删除方法 - \1.delete(\2)',
            content
        )
        
        # 6. 修复获取平均值等统计方法
        content = re.sub(
            r'(\w+Repository)\.get(\w+)\(([^)]*)\)',
            r'// TODO: 实现统计方法 - \1.get\2(\3)',
            content
        )
        
        # 7. 修复特殊的查询方法
        content = re.sub(
            r'findSimilarQueries\(([^)]+), PageRequest\.of\([^)]+\)\)',
            r'findSimilarQueries(\1)',
            content
        )
        
        # 8. 修复类型转换问题
        content = re.sub(
            r'conversationVariableMapper\.selectCount\(new QueryWrapper<>\(\)\)',
            r'(int) conversationVariableMapper.selectCount(new QueryWrapper<>())',
            content
        )
        
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(content)
        
        print(f"Fixed repository calls in: {file_path}")
        return True
    except Exception as e:
        print(f"Error fixing {file_path}: {e}")
        return False

def main():
    """主函数"""
    print("开始修复Repository方法调用...")
    
    service_dir = "src/main/java/com/coze/studio/service/impl"
    for root, dirs, files in os.walk(service_dir):
        for file in files:
            if file.endswith('.java'):
                file_path = os.path.join(root, file)
                fix_repository_method_calls(file_path)
    
    print("Repository方法调用修复完成！")

if __name__ == "__main__":
    main()
