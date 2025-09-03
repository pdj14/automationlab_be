@echo off
chcp 65001 >nul
echo ================================================
echo MongoDB 실행 스크립트
echo ================================================
echo.

echo 1. MongoDB 설치 확인...
mongod --version >nul 2>&1
if %errorlevel% neq 0 (
    echo MongoDB가 설치되어 있지 않습니다.
    echo.
    echo MongoDB 설치 방법:
    echo 1. https://www.mongodb.com/try/download/community 방문
    echo 2. Windows용 MongoDB Community Server 다운로드
    echo 3. 설치 후 이 스크립트를 다시 실행하세요.
    echo.
    echo 또는 Docker를 사용하여 MongoDB를 실행할 수 있습니다:
    echo docker run -d -p 27017:27017 --name mongodb mongo:latest
    echo.
    pause
    exit /b 1
) else (
    echo MongoDB가 설치되어 있습니다.
    mongod --version
)
echo.

echo 2. MongoDB 데이터 디렉토리 확인...
if not exist "C:\data\db" (
    echo MongoDB 데이터 디렉토리를 생성합니다...
    mkdir "C:\data\db" 2>nul
    if %errorlevel% neq 0 (
        echo 데이터 디렉토리 생성 실패! 관리자 권한으로 실행하세요.
        pause
        exit /b 1
    ) else (
        echo 데이터 디렉토리 생성 완료: C:\data\db
    )
) else (
    echo MongoDB 데이터 디렉토리가 존재합니다: C:\data\db
)
echo.

echo 3. MongoDB 서비스 상태 확인...
sc query MongoDB >nul 2>&1
if %errorlevel% equ 0 (
    echo MongoDB 서비스가 설치되어 있습니다.
    sc query MongoDB | findstr "RUNNING" >nul
    if %errorlevel% equ 0 (
        echo MongoDB 서비스가 이미 실행 중입니다.
        echo 포트 27017에서 MongoDB에 접근할 수 있습니다.
        echo.
        echo MongoDB 중지하려면: net stop MongoDB
        echo.
        pause
        exit /b 0
    ) else (
        echo MongoDB 서비스를 시작합니다...
        net start MongoDB
        if %errorlevel% neq 0 (
            echo 서비스 시작 실패! 관리자 권한으로 실행하세요.
            pause
            exit /b 1
        ) else (
            echo MongoDB 서비스가 시작되었습니다.
        )
    )
) else (
    echo MongoDB 서비스가 설치되어 있지 않습니다.
    echo.
    echo MongoDB를 수동으로 실행합니다...
    echo 데이터 디렉토리: C:\data\db
    echo 포트: 27017
    echo.
    echo MongoDB를 중지하려면 Ctrl+C를 누르세요.
    echo.
    mongod --dbpath "C:\data\db" --port 27017
)
echo.

echo ================================================
echo MongoDB 실행 완료!
echo ================================================
echo.
echo 연결 정보:
echo - 호스트: localhost
echo - 포트: 27017
echo - 데이터베이스: test (기본값)
echo.
echo MongoDB 클라이언트로 연결:
echo mongo --host localhost:27017
echo 또는
echo mongosh --host localhost:27017
echo.
echo MongoDB 중지:
echo - 서비스인 경우: net stop MongoDB
echo - 수동 실행인 경우: Ctrl+C
echo.

pause
