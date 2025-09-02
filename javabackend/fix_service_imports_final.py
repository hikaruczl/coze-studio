#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import os
import re

def fix_service_impl_imports():
    """修复Service实现类的import问题"""
    
    service_mappings = {
        'WorkflowServiceImpl.java': 'WorkflowService',
        'WorkflowTemplateServiceImpl.java': 'WorkflowTemplateService',
        'RAGServiceImpl.java': 'RAGService',
        'WorkflowOptimizationServiceImpl.java': 'WorkflowOptimizationService',
        'ConversationServiceImpl.java': 'ConversationService',
        'WorkflowExecutionServiceImpl.java': 'WorkflowExecutionService',
        'PluginManagementServiceImpl.java': 'PluginManagementService',
        'PromptManagementServiceImpl.java': 'PromptManagementService',
        'UserServiceImpl.java': 'UserService',
        'DocumentProcessingServiceImpl.java': 'DocumentProcessingService',
        'MessageServiceImpl.java': 'MessageService'
    }
    
    for impl_file, service_interface in service_mappings.items():
        file_path = f"src/main/java/com/coze/studio/service/impl/{impl_file}"
        if os.path.exists(file_path):
            try:
                with open(file_path, 'r', encoding='utf-8') as f:
                    content = f.read()
                
                # 添加缺失的import
                if f'import com.coze.studio.service.{service_interface};' not in content:
                    # 在package声明后添加import
                    content = content.replace(
                        'package com.coze.studio.service.impl;',
                        f'package com.coze.studio.service.impl;\n\nimport com.coze.studio.service.{service_interface};'
                    )
                
                with open(file_path, 'w', encoding='utf-8') as f:
                    f.write(content)
                
                print(f"Fixed import for: {impl_file}")
                
            except Exception as e:
                print(f"Error fixing {impl_file}: {e}")

def main():
    """主函数"""
    print("开始修复Service实现类的import问题...")
    fix_service_impl_imports()
    print("Service实现类import修复完成！")

if __name__ == "__main__":
    main()
