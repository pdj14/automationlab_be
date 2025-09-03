@echo off
chcp 65001 >nul
echo ================================================
echo Maven 빠른 설치 스크립트
echo ================================================
echo.

echo Maven이 설치되어 있지 않습니다.
echo.
echo 이 스크립트는 Maven 3.9.6을 자동으로 설치합니다.
echo.
echo 설치 과정:
echo 1. Maven 다운로드 (약 10MB)
echo 2. 압축 해제
echo 3. 환경변수 설정
echo.
echo 자동 설치를 시작합니다...
echo.

set MAVEN_VERSION=3.9.6
set MAVEN_HOME=%USERPROFILE%\apache-maven-%MAVEN_VERSION%
set MAVEN_URL=https://archive.apache.org/dist/maven/maven-3/%MAVEN_VERSION%/binaries/apache-maven-%MAVEN_VERSION%-bin.zip
set TEMP_DIR=%TEMP%\maven-install

echo 1. 임시 디렉토리 생성...
if exist "%TEMP_DIR%" rmdir /s /q "%TEMP_DIR%"
mkdir "%TEMP_DIR%"

echo 2. Maven 다운로드 중... (인터넷 연결 필요)
powershell -Command "try { Invoke-WebRequest -Uri '%MAVEN_URL%' -OutFile '%TEMP_DIR%\maven.zip' -UseBasicParsing; Write-Host '다운로드 완료!' } catch { Write-Host '다운로드 실패!'; exit 1 }"
if %errorlevel% neq 0 (
    echo 다운로드 실패! 인터넷 연결을 확인하세요.
    pause
    exit /b 1
)

echo 3. Maven 압축 해제...
powershell -Command "try { Expand-Archive -Path '%TEMP_DIR%\maven.zip' -DestinationPath '%USERPROFILE%' -Force; Write-Host '압축 해제 완료!' } catch { Write-Host '압축 해제 실패!'; exit 1 }"
if %errorlevel% neq 0 (
    echo 압축 해제 실패!
    pause
    exit /b 1
)

echo 4. 환경변수 설정...
setx MAVEN_HOME "%MAVEN_HOME%" >nul
if %errorlevel% neq 0 (
    echo MAVEN_HOME 설정 실패!
    pause
    exit /b 1
)

echo MAVEN_HOME 설정 완료: %MAVEN_HOME%

rem PATH에 Maven bin 디렉토리 추가 (안전한 방법)
powershell -Command "[Environment]::SetEnvironmentVariable('PATH', [Environment]::GetEnvironmentVariable('PATH', 'User') + ';%MAVEN_HOME%\bin', 'User')"
if %errorlevel% neq 0 (
    echo PATH 환경변수 설정 실패!
    echo 수동으로 PATH에 %MAVEN_HOME%\bin 을 추가하세요.
    pause
    exit /b 1
) else (
    echo PATH 환경변수에 Maven bin 디렉토리 추가 완료
)

echo 5. 임시 파일 정리...
rmdir /s /q "%TEMP_DIR%"

echo.
echo ================================================
echo Maven 설치 완료!
echo ================================================
echo.
echo 설치된 Maven 정보:
echo - 버전: %MAVEN_VERSION%
echo - 설치 경로: %MAVEN_HOME%
echo.
echo 다음 단계:
echo 1. 명령 프롬프트를 새로 열어주세요
echo 2. 'mvn -version' 명령으로 설치 확인
echo 3. 프로젝트에서 'mvn clean install' 실행
echo.
echo 참고: 환경변수 변경사항이 적용되려면 새 명령 프롬프트가 필요합니다.
echo.

pause
