# Simple JPA to MyBatis migration script

Write-Host "Starting migration..."

# Process entity files
$entityFiles = Get-ChildItem -Path "src/main/java/com/coze/studio/entity" -Filter "*.java"

foreach ($file in $entityFiles) {
    $fileName = $file.Name
    
    # Skip already processed files
    if ($fileName -eq "BaseEntity.java" -or $fileName -eq "User.java" -or $fileName -eq "PluginInstallation.java") {
        Write-Host "Skipping: $fileName"
        continue
    }
    
    Write-Host "Processing entity: $fileName"
    
    $filePath = $file.FullName
    $content = Get-Content $filePath -Raw
    
    # Replace JPA imports
    $content = $content -replace 'import jakarta\.persistence\.\*;', '// JPA annotations removed, using MyBatis'
    
    # Remove JPA annotations
    $content = $content -replace '@Entity\s*\r?\n', ''
    $content = $content -replace '@Table\([^)]*\)\s*\r?\n', ''
    
    Set-Content $filePath $content -NoNewline
}

# Process repository files
$repoFiles = Get-ChildItem -Path "src/main/java/com/coze/studio/repository" -Filter "*.java"

foreach ($file in $repoFiles) {
    $fileName = $file.Name
    
    # Skip already processed files
    if ($fileName -eq "PluginInstallationRepository.java" -or $fileName -eq "UserRepository.java") {
        Write-Host "Skipping: $fileName"
        continue
    }
    
    Write-Host "Processing repository: $fileName"
    
    $filePath = $file.FullName
    $content = Get-Content $filePath -Raw
    
    # Replace imports
    $content = $content -replace 'import org\.springframework\.data\.jpa\.repository\.\*;', ''
    $content = $content -replace 'import org\.springframework\.data\.domain\.\*;', ''
    $content = $content -replace 'import org\.springframework\.data\.repository\.query\.Param;', 'import org.apache.ibatis.annotations.Param;'
    
    # Add MyBatis import if not present
    if ($content -notmatch 'import org\.apache\.ibatis\.annotations\.Mapper;') {
        $content = $content -replace '(package [^;]+;)', '$1' + "`n`nimport org.apache.ibatis.annotations.Mapper;"
    }
    
    # Replace annotations
    $content = $content -replace '@Repository\s*\r?\n', '@Mapper' + "`n"
    
    # Remove JpaRepository inheritance
    $content = $content -replace 'extends JpaRepository<[^>]+>', ''
    
    Set-Content $filePath $content -NoNewline
}

Write-Host "Migration completed!"
