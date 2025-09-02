@echo off
chcp 65001 > nul
REM ============================
REM  Java 服务启动脚本（Windows）
REM ============================

:: 设置 JDK 路径（请改成你自己的）
set "JAVA_HOME=D:\Programs\java\jdk-17"
set "PATH=%JAVA_HOME%\bin;%PATH%"

:: 2. 杀掉端口 8888 的进程（可根据需要改成别的端口）
echo [INFO] 正在清理旧进程 ...
for /f "tokens=5" %%p in ('netstat -aon ^| findstr ":8888" ^| findstr LISTENING') do (
    echo [INFO] 发现残留进程 PID %%p，正在结束 ...
    taskkill /f /pid %%p >nul 2>&1
)

:: 3. 可选：按项目名杀进程（更保险）
:: for /f "tokens=2" %%i in ('tasklist ^| findstr /i "java.exe"') do (
::     taskkill /f /pid %%i >nul 2>&1
:: )

echo.
echo [INFO] 当前使用的 JDK 版本：
java -version
echo.

echo [INFO] 正在启动 Spring Boot 服务（DEBUG，控制台+落盘: run.log, UTF-8）...
set "MAVEN_OPTS=-Dfile.encoding=UTF-8"
:: 使用 PowerShell Tee-Object 同时输出到控制台和文件，并强制 UTF-8
powershell -NoProfile -ExecutionPolicy Bypass -Command "$ProgressPreference='SilentlyContinue'; $OutputEncoding=[Console]::OutputEncoding=[Text.UTF8Encoding]::UTF8; & mvn -DskipTests -Dspring-boot.run.profiles=dev -Dspring-boot.run.arguments='--debug' spring-boot:run 2>&1 | Tee-Object -FilePath run.log -Encoding UTF8"
echo [INFO] 日志文件路径: %cd%\run.log
