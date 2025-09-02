# 修复文件编码问题的脚本

# 设置PowerShell默认编码为UTF-8
$PSDefaultParameterValues['*:Encoding'] = 'utf8'

# 修复实体类编码
$entityPath = "src/main/java/com/coze/studio/entity"
Get-ChildItem -Path $entityPath -Filter "*.java" | ForEach-Object {
    $filePath = $_.FullName
    try {
        # 使用UTF-8编码读取和写入文件
        $content = Get-Content $filePath -Encoding UTF8 -Raw
        Set-Content $filePath $content -Encoding UTF8 -NoNewline
        Write-Host "Fixed encoding for entity: $($_.Name)"
    } catch {
        Write-Warning "Failed to fix encoding for entity: $($_.Name) - $($_.Exception.Message)"
    }
}

# 修复Repository编码
$repoPath = "src/main/java/com/coze/studio/repository"
Get-ChildItem -Path $repoPath -Filter "*.java" | ForEach-Object {
    $filePath = $_.FullName
    try {
        # 使用UTF-8编码读取和写入文件
        $content = Get-Content $filePath -Encoding UTF8 -Raw
        Set-Content $filePath $content -Encoding UTF8 -NoNewline
        Write-Host "Fixed encoding for repository: $($_.Name)"
    } catch {
        Write-Warning "Failed to fix encoding for repository: $($_.Name) - $($_.Exception.Message)"
    }
}

Write-Host "Encoding fix completed!"
