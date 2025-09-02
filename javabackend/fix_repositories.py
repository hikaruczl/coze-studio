#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import os
import re

def fix_repository_file(file_path):
    """修复Repository文件，将JPA改为MyBatis并注释掉Page方法"""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()

        # 替换import语句
        content = re.sub(r'import org\.springframework\.data\.domain\.Page;', '', content)
        content = re.sub(r'import org\.springframework\.data\.domain\.Pageable;', '', content)
        content = re.sub(r'import org\.springframework\.data\.jpa\.repository\.JpaRepository;', 'import org.apache.ibatis.annotations.Mapper;', content)
        content = re.sub(r'import org\.springframework\.data\.jpa\.repository\.Modifying;', '', content)
        content = re.sub(r'import org\.springframework\.data\.jpa\.repository\.Query;', '', content)
        content = re.sub(r'import org\.springframework\.data\.repository\.query\.Param;', 'import org.apache.ibatis.annotations.Param;', content)
        content = re.sub(r'import org\.springframework\.stereotype\.Repository;', '', content)

        # 替换类声明
        content = re.sub(r'@Repository\s*\n', '', content)
        content = re.sub(r'public interface (\w+) extends JpaRepository<\w+, \w+> \{', r'@Mapper\npublic interface \1 {', content)

        # 注释掉包含Page或Pageable的方法
        lines = content.split('\n')
        new_lines = []
        i = 0
        while i < len(lines):
            line = lines[i]

            # 检查是否是包含Page或Pageable的方法声明
            if ('Page<' in line or 'Pageable' in line) and ('(' in line and ';' in line):
                # 找到方法的开始注释
                comment_start = i
                while comment_start > 0 and not lines[comment_start - 1].strip().startswith('/**'):
                    comment_start -= 1
                if comment_start > 0 and lines[comment_start - 1].strip().startswith('/**'):
                    comment_start -= 1

                # 注释掉从注释开始到方法结束的所有行
                for j in range(comment_start, i + 1):
                    if not lines[j].strip().startswith('//'):
                        lines[j] = '    // ' + lines[j].strip() + ' // TODO: 需要在MyBatis XML中实现分页'

            new_lines.append(lines[i])
            i += 1

        content = '\n'.join(new_lines)

        # 清理多余的空行
        content = re.sub(r'\n\s*\n\s*\n', '\n\n', content)

        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(content)

        print(f"Fixed: {file_path}")
        return True
    except Exception as e:
        print(f"Error fixing {file_path}: {e}")
        return False

def comment_out_remaining_jpa_repos():
    """注释掉剩余的JPA Repository接口"""
    repository_dir = "src/main/java/com/coze/studio/repository"

    # 剩余需要修复的Repository文件
    remaining_files = [
        "WorkflowExecutionRepository.java",
        "WorkflowNodeExecutionRepository.java",
        "PluginUsageRepository.java",
        "WorkflowRepository.java",
        "UserRepository.java",
        "MessageRepository.java"
    ]

    for file_name in remaining_files:
        file_path = os.path.join(repository_dir, file_name)
        if os.path.exists(file_path):
            fix_repository_file(file_path)

def main():
    """主函数"""
    repository_dir = "src/main/java/com/coze/studio/repository"

    # 需要修复的Repository文件列表
    repository_files = [
        "PromptTemplateRepository.java",
        "WorkflowTemplateRatingRepository.java",
        "ConversationVariableRepository.java",
        "DocumentSliceRepository.java",
        "WorkflowNodeRepository.java",
        "WorkflowConnectionRepository.java",
        "WorkflowTemplateRepository.java",
        "WorkflowTemplateFavoriteRepository.java",
        "WorkflowTemplateUsageRepository.java",
        "KnowledgeQueryRepository.java",
        "ModelConfigurationRepository.java",
        "DocumentProcessingTaskRepository.java",
        "PluginRatingRepository.java",
        "ModelProviderRepository.java",
        "VectorStoreRepository.java",
        "PromptCategoryRepository.java",
        "ModelUsageRecordRepository.java",
        "WorkflowVariableRepository.java"
    ]

    fixed_count = 0
    for file_name in repository_files:
        file_path = os.path.join(repository_dir, file_name)
        if os.path.exists(file_path):
            if fix_repository_file(file_path):
                fixed_count += 1
        else:
            print(f"File not found: {file_path}")

    # 修复剩余的Repository文件
    comment_out_remaining_jpa_repos()

    print(f"\nFixed {fixed_count} repository files")

if __name__ == "__main__":
    main()
