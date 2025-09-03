@echo off
chcp 65001 >nul
echo ================================================
echo MongoDB 중지 스크립트
echo ================================================
echo.

echo 1. MongoDB 서비스 중지...
sc query MongoDB >nul 2>&1
if %errorlevel% equ 0 (
    echo MongoDB 서비스를 중지합니다...
    net stop MongoDB
    if %errorlevel% equ 0 (
        echo MongoDB 서비스가 중지되었습니다.
    ) else (
        echo MongoDB 서비스 중지 실패!
    )
) else (
    echo MongoDB 서비스가 설치되어 있지 않습니다.
)
echo.

echo 2. MongoDB Docker 컨테이너 중지...
docker ps --filter "name=mongodb" --format "{{.Names}}" | findstr mongodb >nul
if %errorlevel% equ 0 (
    echo MongoDB Docker 컨테이너를 중지합니다...
    docker stop mongodb
    if %errorlevel% equ 0 (
        echo MongoDB Docker 컨테이너가 중지되었습니다.
    ) else (
        echo MongoDB Docker 컨테이너 중지 실패!
    )
) else (
    echo 실행 중인 MongoDB Docker 컨테이너가 없습니다.
)
echo.

echo 3. MongoDB 프로세스 확인...
tasklist | findstr mongod >nul
if %errorlevel% equ 0 (
    echo 실행 중인 MongoDB 프로세스가 있습니다:
    tasklist | findstr mongod
    echo.
    echo 수동으로 실행된 MongoDB 프로세스를 종료하려면:
    echo taskkill /f /im mongod.exe
    echo.
    set /p choice="MongoDB 프로세스를 강제 종료하시겠습니까? (y/n): "
    if /i "%choice%"=="y" (
        taskkill /f /im mongod.exe
        echo MongoDB 프로세스가 종료되었습니다.
    )
) else (
    echo 실행 중인 MongoDB 프로세스가 없습니다.
)
echo.

echo ================================================
echo MongoDB 중지 완료!
echo ================================================
echo.
echo 모든 MongoDB 인스턴스가 중지되었습니다.
echo.
echo MongoDB 다시 시작:
echo - 서비스: net start MongoDB
echo - Docker: docker start mongodb
echo - 수동: start-mongodb.bat 실행
echo.

pause
