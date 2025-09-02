#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import os
import re

def fix_repository_references(file_path):
    """修复Service文件中的Repository引用"""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        # 替换Repository方法调用为Mapper方法调用
        content = re.sub(r'conversationVariableRepository\.save\(([^)]+)\)', r'conversationVariableMapper.insert(\1)', content)
        content = re.sub(r'workflowVariableRepository\.save\(([^)]+)\)', r'workflowVariableMapper.insert(\1)', content)
        content = re.sub(r'conversationVariableRepository\.findById\(([^)]+)\)', r'conversationVariableMapper.selectById(\1)', content)
        content = re.sub(r'workflowVariableRepository\.findById\(([^)]+)\)', r'workflowVariableMapper.selectById(\1)', content)
        content = re.sub(r'conversationVariableRepository\.deleteById\(([^)]+)\)', r'conversationVariableMapper.deleteById(\1)', content)
        content = re.sub(r'workflowVariableRepository\.deleteById\(([^)]+)\)', r'workflowVariableMapper.deleteById(\1)', content)
        content = re.sub(r'conversationVariableRepository\.count\(\)', r'conversationVariableMapper.selectCount(null)', content)
        content = re.sub(r'workflowVariableRepository\.count\(\)', r'workflowVariableMapper.selectCount(null)', content)
        
        # 替换其他Repository方法调用
        content = re.sub(r'(\w+)Repository\.save\(([^)]+)\)', r'\1Mapper.insert(\2)', content)
        content = re.sub(r'(\w+)Repository\.findById\(([^)]+)\)', r'\1Mapper.selectById(\2)', content)
        content = re.sub(r'(\w+)Repository\.deleteById\(([^)]+)\)', r'\1Mapper.deleteById(\2)', content)
        content = re.sub(r'(\w+)Repository\.existsById\(([^)]+)\)', r'\1Mapper.selectById(\2) != null', content)
        content = re.sub(r'(\w+)Repository\.count\(\)', r'\1Mapper.selectCount(null)', content)
        content = re.sub(r'(\w+)Repository\.findAll\(\)', r'\1Mapper.selectList(null)', content)
        
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(content)
        
        print(f"Fixed repository references in: {file_path}")
        return True
    except Exception as e:
        print(f"Error fixing {file_path}: {e}")
        return False

def main():
    """主函数"""
    print("开始修复Service文件中的Repository引用...")
    
    service_dir = "src/main/java/com/coze/studio/service/impl"
    
    for root, dirs, files in os.walk(service_dir):
        for file in files:
            if file.endswith('.java'):
                file_path = os.path.join(root, file)
                fix_repository_references(file_path)
    
    print("Repository引用修复完成！")

if __name__ == "__main__":
    main()
