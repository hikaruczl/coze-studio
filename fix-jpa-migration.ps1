# 批量修复JPA到MyBatis迁移的脚本

# 修复实体类 - 移除JPA注解
$entityPath = "javabackend/src/main/java/com/coze/studio/entity"
Get-ChildItem -Path $entityPath -Filter "*.java" | ForEach-Object {
    $filePath = $_.FullName
    $content = Get-Content $filePath -Raw
    
    # 移除JPA导入
    $content = $content -replace "import jakarta\.persistence\.\*;", ""
    $content = $content -replace "import jakarta\.persistence\..*;", ""
    
    # 移除JPA注解
    $content = $content -replace "@Entity\s*\n", ""
    $content = $content -replace "@Table\([^)]*\)\s*\n", ""
    $content = $content -replace "@Column\([^)]*\)\s*\n", ""
    $content = $content -replace "@Id\s*\n", ""
    $content = $content -replace "@GeneratedValue\([^)]*\)\s*\n", ""
    $content = $content -replace "@Enumerated\([^)]*\)\s*\n", ""
    $content = $content -replace "@JoinColumn\([^)]*\)\s*\n", ""
    $content = $content -replace "@OneToMany\([^)]*\)\s*\n", ""
    $content = $content -replace "@ManyToOne\([^)]*\)\s*\n", ""
    $content = $content -replace "@OneToOne\([^)]*\)\s*\n", ""
    $content = $content -replace "@ManyToMany\([^)]*\)\s*\n", ""
    $content = $content -replace "@Transient\s*\n", ""
    
    # 清理多余的空行
    $content = $content -replace "\n\n\n+", "`n`n"
    
    Set-Content $filePath $content -NoNewline
    Write-Host "Fixed entity: $($_.Name)"
}

# 修复Repository接口 - 改为MyBatis Mapper
$repoPath = "javabackend/src/main/java/com/coze/studio/repository"
Get-ChildItem -Path $repoPath -Filter "*.java" | ForEach-Object {
    $filePath = $_.FullName
    $content = Get-Content $filePath -Raw
    
    # 移除Spring Data JPA导入
    $content = $content -replace "import org\.springframework\.data\.jpa\.repository\.\*;", ""
    $content = $content -replace "import org\.springframework\.data\.jpa\.repository\..*;", ""
    $content = $content -replace "import org\.springframework\.data\.domain\.\*;", ""
    $content = $content -replace "import org\.springframework\.data\.domain\..*;", ""
    $content = $content -replace "import org\.springframework\.data\.repository\.query\.Param;", "import org.apache.ibatis.annotations.Param;"
    
    # 添加MyBatis导入
    if ($content -notmatch "import org\.apache\.ibatis\.annotations\.Mapper;") {
        $content = $content -replace "(package [^;]+;)", "`$1`n`nimport org.apache.ibatis.annotations.Mapper;"
    }
    
    # 替换Repository注解和继承
    $content = $content -replace "@Repository", "@Mapper"
    $content = $content -replace "extends JpaRepository<[^>]+>", ""
    $content = $content -replace "extends CrudRepository<[^>]+>", ""
    
    # 移除@Query注解（需要手动转换为MyBatis SQL）
    $content = $content -replace "@Query\([^)]*\)\s*\n", "// TODO: Convert to MyBatis SQL`n"
    
    Set-Content $filePath $content -NoNewline
    Write-Host "Fixed repository: $($_.Name)"
}

Write-Host "JPA to MyBatis migration completed!"
