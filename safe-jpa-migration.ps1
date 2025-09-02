# 安全的JPA到MyBatis迁移脚本 - 只处理导入和注解，不修改编码

# 处理实体类
Write-Host "Processing entity classes..."
$entityFiles = Get-ChildItem -Path "src/main/java/com/coze/studio/entity" -Filter "*.java"

foreach ($file in $entityFiles) {
    $filePath = $file.FullName
    $fileName = $file.Name
    
    # 跳过已经处理过的文件
    if ($fileName -eq "PluginInstallation.java") {
        Write-Host "Skipping already processed: $fileName"
        continue
    }
    
    Write-Host "Processing entity: $fileName"
    
    # 读取文件内容（保持原始编码）
    $lines = Get-Content $filePath
    $newLines = @()
    
    foreach ($line in $lines) {
        # 移除JPA导入
        if ($line -match "^import jakarta\.persistence\.") {
            $newLines += "// JPA注解已移除，改用MyBatis"
            continue
        }
        
        # 移除JPA注解行
        if ($line -match "^\s*@(Entity|Table|Column|Id|GeneratedValue|Enumerated|JoinColumn|OneToMany|ManyToOne|OneToOne|ManyToMany|Transient)\b") {
            continue
        }
        
        $newLines += $line
    }
    
    # 写回文件（保持原始编码）
    $newLines | Set-Content $filePath
}

# 处理Repository接口
Write-Host "Processing repository interfaces..."
$repoFiles = Get-ChildItem -Path "src/main/java/com/coze/studio/repository" -Filter "*.java"

foreach ($file in $repoFiles) {
    $filePath = $file.FullName
    $fileName = $file.Name
    
    # 跳过已经处理过的文件
    if ($fileName -eq "PluginInstallationRepository.java") {
        Write-Host "Skipping already processed: $fileName"
        continue
    }
    
    Write-Host "Processing repository: $fileName"
    
    # 读取文件内容
    $lines = Get-Content $filePath
    $newLines = @()
    $needsMapperImport = $false
    
    foreach ($line in $lines) {
        # 移除Spring Data JPA导入
        if ($line -match "^import org\.springframework\.data\.(jpa\.repository|domain|repository\.query)\.") {
            continue
        }
        
        # 替换@Repository为@Mapper
        if ($line -match "^\s*@Repository\s*$") {
            $newLines += $line -replace "@Repository", "@Mapper"
            $needsMapperImport = $true
            continue
        }
        
        # 移除JpaRepository继承
        if ($line -match "extends\s+JpaRepository<[^>]+>") {
            $newLines += $line -replace "extends\s+JpaRepository<[^>]+>", ""
            continue
        }
        
        # 注释掉@Query方法
        if ($line -match "^\s*@Query\(") {
            $newLines += "    // TODO: Convert to MyBatis SQL"
            $newLines += "    // " + $line
            continue
        }
        
        # 注释掉@Modifying方法
        if ($line -match "^\s*@Modifying\s*$") {
            $newLines += "    // TODO: Convert to MyBatis SQL"
            $newLines += "    // " + $line
            continue
        }
        
        # 注释掉包含Page或Pageable的方法
        if ($line -match "(Page<|Pageable\s)") {
            $newLines += "    // TODO: 分页查询需要在MyBatis XML中实现"
            $newLines += "    // " + $line
            continue
        }
        
        $newLines += $line
    }
    
    # 添加MyBatis导入
    if ($needsMapperImport) {
        for ($i = 0; $i -lt $newLines.Count; $i++) {
            if ($newLines[$i] -match "^package ") {
                $newLines = $newLines[0..$i] + "" + "import org.apache.ibatis.annotations.Mapper;" + $newLines[($i+1)..($newLines.Count-1)]
                break
            }
        }
    }
    
    # 写回文件
    $newLines | Set-Content $filePath
}

Write-Host "Safe JPA to MyBatis migration completed!"
