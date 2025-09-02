#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import os
import re

def fix_service_imports_and_fields(file_path):
    """修复Service类中的import语句和字段声明"""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        # 添加必要的import语句
        imports_to_add = []
        
        if 'workflowMapper' in content and 'import com.coze.studio.mapper.WorkflowMapper' not in content:
            imports_to_add.extend([
                'import com.coze.studio.mapper.WorkflowMapper;',
                'import com.coze.studio.mapper.WorkflowNodeMapper;',
                'import com.coze.studio.mapper.WorkflowConnectionMapper;'
            ])
        
        if 'workflowTemplateMapper' in content and 'import com.coze.studio.mapper.WorkflowTemplateMapper' not in content:
            imports_to_add.extend([
                'import com.coze.studio.mapper.WorkflowTemplateMapper;',
                'import com.coze.studio.mapper.WorkflowTemplateFavoriteMapper;',
                'import com.coze.studio.mapper.WorkflowTemplateRatingMapper;',
                'import com.coze.studio.mapper.WorkflowTemplateUsageMapper;'
            ])
        
        if 'conversationMapper' in content and 'import com.coze.studio.mapper.ConversationMapper' not in content:
            imports_to_add.append('import com.coze.studio.mapper.ConversationMapper;')
        
        if 'workflowExecutionMapper' in content and 'import com.coze.studio.mapper.WorkflowExecutionMapper' not in content:
            imports_to_add.append('import com.coze.studio.mapper.WorkflowExecutionMapper;')
        
        if 'knowledgeQueryMapper' in content and 'import com.coze.studio.mapper.KnowledgeQueryMapper' not in content:
            imports_to_add.append('import com.coze.studio.mapper.KnowledgeQueryMapper;')
        
        # 添加MyBatis-Plus相关import
        if 'QueryWrapper' in content and 'import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper' not in content:
            imports_to_add.append('import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;')
        
        if 'Page' in content and 'import com.baomidou.mybatisplus.extension.plugins.pagination.Page' not in content:
            imports_to_add.append('import com.baomidou.mybatisplus.extension.plugins.pagination.Page;')
        
        # 在package声明后添加import语句
        if imports_to_add:
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
                    new_lines.extend(imports_to_add)
                    new_lines.append('')
                    imports_added = True
            
            content = '\n'.join(new_lines)
        
        # 添加缺失的字段声明
        class_pattern = r'(public class \w+ServiceImpl.*?\{)'
        
        if 'workflowMapper' in content and 'private WorkflowMapper workflowMapper' not in content:
            content = re.sub(
                class_pattern,
                r'\1\n\n    @Autowired\n    private WorkflowMapper workflowMapper;\n\n    @Autowired\n    private WorkflowNodeMapper workflowNodeMapper;\n\n    @Autowired\n    private WorkflowConnectionMapper workflowConnectionMapper;',
                content
            )
        
        if 'workflowTemplateMapper' in content and 'private WorkflowTemplateMapper workflowTemplateMapper' not in content:
            content = re.sub(
                class_pattern,
                r'\1\n\n    @Autowired\n    private WorkflowTemplateMapper workflowTemplateMapper;\n\n    @Autowired\n    private WorkflowTemplateFavoriteMapper favoriteMapper;\n\n    @Autowired\n    private WorkflowTemplateRatingMapper ratingMapper;\n\n    @Autowired\n    private WorkflowTemplateUsageMapper usageMapper;',
                content
            )
        
        if 'conversationMapper' in content and 'private ConversationMapper conversationMapper' not in content:
            content = re.sub(
                class_pattern,
                r'\1\n\n    @Autowired\n    private ConversationMapper conversationMapper;',
                content
            )
        
        if 'workflowExecutionMapper' in content and 'private WorkflowExecutionMapper workflowExecutionMapper' not in content:
            content = re.sub(
                class_pattern,
                r'\1\n\n    @Autowired\n    private WorkflowExecutionMapper workflowExecutionMapper;',
                content
            )
        
        if 'knowledgeQueryMapper' in content and 'private KnowledgeQueryMapper knowledgeQueryMapper' not in content:
            content = re.sub(
                class_pattern,
                r'\1\n\n    @Autowired\n    private KnowledgeQueryMapper knowledgeQueryMapper;',
                content
            )
        
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(content)
        
        print(f"Fixed imports and fields in: {file_path}")
        return True
    except Exception as e:
        print(f"Error fixing {file_path}: {e}")
        return False

def main():
    """主函数"""
    print("开始修复Service类中的import语句和字段声明...")
    
    service_dir = "src/main/java/com/coze/studio/service/impl"
    for root, dirs, files in os.walk(service_dir):
        for file in files:
            if file.endswith('.java'):
                file_path = os.path.join(root, file)
                fix_service_imports_and_fields(file_path)
    
    print("Import语句和字段声明修复完成！")

if __name__ == "__main__":
    main()
