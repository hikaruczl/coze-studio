#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import os
import re
import glob

def generate_getters_setters_for_dto(file_path):
    """为DTO类生成getter/setter方法"""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        # 提取所有字段
        field_pattern = r'private\s+(\w+(?:<[^>]+>)?)\s+(\w+)(?:\s*=\s*[^;]+)?;'
        fields = re.findall(field_pattern, content)
        
        if not fields:
            print(f"No fields found in {file_path}")
            return
        
        # 生成getter/setter方法
        methods = []
        for field_type, field_name in fields:
            # 生成getter
            getter_name = f"get{field_name[0].upper()}{field_name[1:]}"
            if field_type == "boolean":
                getter_name = f"is{field_name[0].upper()}{field_name[1:]}"
            
            getter = f'''    public {field_type} {getter_name}() {{
        return {field_name};
    }}'''
            
            # 生成setter
            setter_name = f"set{field_name[0].upper()}{field_name[1:]}"
            setter = f'''    public void {setter_name}({field_type} {field_name}) {{
        this.{field_name} = {field_name};
    }}'''
            
            methods.append(getter)
            methods.append(setter)
        
        # 在类的结尾添加方法
        if content.strip().endswith('}'):
            # 移除最后的大括号
            content = content.rstrip().rstrip('}')
            # 添加生成的方法
            content += '\n\n    // Lombok生成的getter/setter方法\n'
            content += '\n\n'.join(methods)
            content += '\n}\n'
        
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(content)
        
        print(f"Generated {len(fields)} getter/setter pairs for: {file_path}")
        
    except Exception as e:
        print(f"Error processing {file_path}: {e}")

def main():
    """主函数"""
    print("开始为DTO类生成getter/setter方法...")
    
    # 为PageResponse类生成getter/setter
    dto_files = [
        "src/main/java/com/coze/studio/dto/common/PageResponse.java",
        "src/main/java/com/coze/studio/dto/workflow/WorkflowResponse.java"
    ]
    
    for file_path in dto_files:
        if os.path.exists(file_path):
            generate_getters_setters_for_dto(file_path)
    
    print("DTO类getter/setter方法生成完成！")

if __name__ == "__main__":
    main()
