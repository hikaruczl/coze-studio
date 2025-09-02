# Final JPA to MyBatis migration script - Safe version

Write-Host "Starting final JPA to MyBatis migration..."

# 获取所有需要处理的实体类文件
$entityFiles = Get-ChildItem -Path "src/main/java/com/coze/studio/entity" -Filter "*.java" | Where-Object {
    $_.Name -notin @("BaseEntity.java", "User.java", "PluginInstallation.java")
}

Write-Host "Processing $($entityFiles.Count) entity files..."

foreach ($file in $entityFiles) {
    Write-Host "Processing entity: $($file.Name)"
    
    # 使用sed命令安全地替换内容
    $filePath = $file.FullName
    
    # 替换JPA导入为注释
    (Get-Content $filePath -Raw) -replace 'import jakarta\.persistence\.\*;', '// JPA注解已移除，改用MyBatis' | Set-Content $filePath -NoNewline
    
    # 移除@Entity注解
    (Get-Content $filePath -Raw) -replace '@Entity\s*\r?\n', '' | Set-Content $filePath -NoNewline
    
    # 移除@Table注解（包括多行）
    (Get-Content $filePath -Raw) -replace '@Table\([^)]*\)\s*\r?\n', '' | Set-Content $filePath -NoNewline
}

# 获取所有需要处理的Repository文件
$repoFiles = Get-ChildItem -Path "src/main/java/com/coze/studio/repository" -Filter "*.java" | Where-Object { 
    $_.Name -notin @("PluginInstallationRepository.java", "UserRepository.java") 
}

Write-Host "Processing $($repoFiles.Count) Repository files..."

foreach ($file in $repoFiles) {
    Write-Host "Processing Repository: $($file.Name)"
    
    $filePath = $file.FullName
    
    # 替换Spring Data JPA导入
    (Get-Content $filePath -Raw) -replace 'import org\.springframework\.data\.jpa\.repository\.\*;', '' | Set-Content $filePath -NoNewline
    (Get-Content $filePath -Raw) -replace 'import org\.springframework\.data\.domain\.\*;', '' | Set-Content $filePath -NoNewline
    (Get-Content $filePath -Raw) -replace 'import org\.springframework\.data\.repository\.query\.Param;', 'import org.apache.ibatis.annotations.Param;' | Set-Content $filePath -NoNewline
    
    # 添加MyBatis导入
    $content = Get-Content $filePath -Raw
    if ($content -notmatch 'import org\.apache\.ibatis\.annotations\.Mapper;') {
        $content = $content -replace '(package [^;]+;)', '$1' + "`n`nimport org.apache.ibatis.annotations.Mapper;"
        Set-Content $filePath $content -NoNewline
    }
    
    # 替换@Repository为@Mapper
    (Get-Content $filePath -Raw) -replace '@Repository\s*\r?\n', '@Mapper' + "`n" | Set-Content $filePath -NoNewline
    
    # 移除JpaRepository继承
    (Get-Content $filePath -Raw) -replace 'extends JpaRepository<[^>]+>', '' | Set-Content $filePath -NoNewline
}

Write-Host "Migration completed!"
Write-Host "Note: Complex @Query methods need to be manually converted to MyBatis XML"
