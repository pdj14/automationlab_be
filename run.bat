@echo off
chcp 65001 >nul
echo Aubotmation Lab Backend Service 실행 스크립트
echo ================================================

echo 1. 프로젝트 빌드 중...
call mvn clean install -DskipTests
if %errorlevel% neq 0 (
    echo 빌드 실패! Java 17이 설치되어 있는지 확인하세요.
    pause
    exit /b 1
)

echo.
echo 2. MongoDB 연결 확인 중...
echo MongoDB가 실행 중인지 확인하세요 (기본 포트: 27017)

echo.
echo 3. Spring Boot 애플리케이션 시작 중...
echo 개발 환경으로 실행됩니다.
echo 브라우저에서 http://localhost:8080/api/v1/objects 를 확인하세요.
echo.
echo 종료하려면 Ctrl+C를 누르세요.
echo.

call mvn spring-boot:run -Dspring-boot.run.profiles=dev

pause
