@echo off
echo Aubotmation Lab Backend Service (Docker) 실행 스크립트
echo ======================================================

echo.
echo 1. Docker 서비스 확인 중...
docker --version >nul 2>&1
if %errorlevel% neq 0 (
    echo Docker가 설치되어 있지 않습니다. Docker Desktop을 설치하세요.
    pause
    exit /b 1
)

echo.
echo 2. 기존 컨테이너 정리 중...
docker-compose down

echo.
echo 3. Docker 이미지 빌드 및 서비스 시작 중...
echo MongoDB와 Spring Boot 애플리케이션이 함께 실행됩니다.
echo.
echo 서비스 URL:
echo - MongoDB: localhost:27017
echo - Spring Boot App: http://localhost:8080
echo - API: http://localhost:8080/api/v1/objects
echo.
echo 종료하려면 Ctrl+C를 누르고 'docker-compose down'을 실행하세요.
echo.

docker-compose up --build

pause
