# 简单的JPA到MyBatis迁移脚本

Write-Host "Starting JPA to MyBatis migration..."

# 处理实体类 - 移除JPA注解
Write-Host "Processing entity classes..."
$entityFiles = Get-ChildItem -Path "src/main/java/com/coze/studio/entity" -Filter "*.java"

foreach ($file in $entityFiles) {
    $fileName = $file.Name
    
    # 跳过已经处理过的文件
    if ($fileName -eq "PluginInstallation.java") {
        Write-Host "Skipping already processed: $fileName"
        continue
    }
    
    Write-Host "Processing entity: $fileName"
    
    # 使用sed命令移除JPA注解（更安全）
    $filePath = $file.FullName
    
    # 移除JPA导入
    (Get-Content $filePath) -replace "^import jakarta\.persistence\.\*;", "// JPA注解已移除，改用MyBatis" | Set-Content $filePath
    
    # 移除常见的JPA注解
    (Get-Content $filePath) -replace "^\s*@Entity\s*$", "" | Set-Content $filePath
    (Get-Content $filePath) -replace "^\s*@Table\([^)]*\)\s*$", "" | Set-Content $filePath
    (Get-Content $filePath) -replace "^\s*@Column\([^)]*\)\s*$", "" | Set-Content $filePath
    (Get-Content $filePath) -replace "^\s*@Id\s*$", "" | Set-Content $filePath
    (Get-Content $filePath) -replace "^\s*@GeneratedValue\([^)]*\)\s*$", "" | Set-Content $filePath
    (Get-Content $filePath) -replace "^\s*@Enumerated\([^)]*\)\s*$", "" | Set-Content $filePath
}

# 处理Repository接口
Write-Host "Processing repository interfaces..."
$repoFiles = Get-ChildItem -Path "src/main/java/com/coze/studio/repository" -Filter "*.java"

foreach ($file in $repoFiles) {
    $fileName = $file.Name
    
    # 跳过已经处理过的文件
    if ($fileName -eq "PluginInstallationRepository.java") {
        Write-Host "Skipping already processed: $fileName"
        continue
    }
    
    Write-Host "Processing repository: $fileName"
    
    $filePath = $file.FullName
    
    # 移除Spring Data JPA导入
    (Get-Content $filePath) -replace "^import org\.springframework\.data\.jpa\.repository\.\*;", "" | Set-Content $filePath
    (Get-Content $filePath) -replace "^import org\.springframework\.data\.domain\.\*;", "" | Set-Content $filePath
    (Get-Content $filePath) -replace "^import org\.springframework\.data\.repository\.query\.Param;", "import org.apache.ibatis.annotations.Param;" | Set-Content $filePath
    
    # 添加MyBatis导入
    (Get-Content $filePath) -replace "^(package [^;]+;)", "`$1`n`nimport org.apache.ibatis.annotations.Mapper;" | Set-Content $filePath
    
    # 替换注解和继承
    (Get-Content $filePath) -replace "^\s*@Repository\s*$", "@Mapper" | Set-Content $filePath
    (Get-Content $filePath) -replace "extends JpaRepository<[^>]+>", "" | Set-Content $filePath
}

Write-Host "Migration completed!"
