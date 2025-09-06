# Automation Lab Backend Service

MongoDB를 사용하는 Java Spring Boot 기반의 3D 객체 관리 백엔드 서비스입니다.

## 기술 스택

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data MongoDB**
- **MongoDB**
- **Maven**
- **Lombok**

## 프로젝트 구조

```
src/
├── main/
│   ├── java/com/automationlab/be/
│   │   ├── AutomationLabBeApplication.java    # 메인 애플리케이션
│   │   ├── controller/
│   │   │   └── Object3DController.java         # REST API 컨트롤러
│   │   ├── service/
│   │   │   └── Object3DService.java            # 비즈니스 로직 서비스
│   │   ├── repository/
│   │   │   └── Object3DRepository.java         # MongoDB 데이터 접근
│   │   ├── model/
│   │   │   └── Object3D.java                   # 3D 객체 엔티티
│   │   ├── dto/
│   │   │   └── Object3DDto.java                # 데이터 전송 객체
│   │   └── exception/
│   │       ├── GlobalExceptionHandler.java      # 전역 예외 처리
│   │       └── ErrorResponse.java               # 에러 응답 모델
│   └── resources/
│       ├── application.yml                      # 기본 설정
│       ├── application-dev.yml                  # 개발 환경 설정
│       └── application-prod.yml                 # 운영 환경 설정
└── test/
    └── java/com/automationlab/be/
        ├── AutomationLabBeApplicationTests.java # 기본 테스트
        └── service/
            └── Object3DServiceTest.java          # 서비스 테스트
```

## 주요 기능

### 3D 객체 관리
- 3D 객체 생성, 조회, 수정, 삭제 (CRUD)
- 카테고리별 객체 조회 (로봇, 장비, 가전제품, AV)
- 이름 기반 검색
- 치수 범위별 객체 조회
- 인스턴싱 활성화 여부별 조회

### 데이터 모델
- **기본 식별 정보**: 이름, 카테고리, 설명
- **3D 모델 정보**: GLB 파일, 썸네일, LOD 파일
- **물리적 속성**: 가로, 세로, 높이 (미터 단위)
- **2D 방향 정보**: 회전 각도 (0-360도)
- **시각적 속성**: HEX 색상 코드
- **성능 최적화**: 인스턴싱 활성화 여부

## API 엔드포인트

### 기본 CRUD
- `GET /api/v1/objects` - 모든 객체 조회
- `GET /api/v1/objects/{id}` - ID로 객체 조회
- `POST /api/v1/objects` - 새 객체 생성
- `PUT /api/v1/objects/{id}` - 객체 수정
- `DELETE /api/v1/objects/{id}` - 객체 삭제

### 검색 및 필터링
- `GET /api/v1/objects/template/{templateName}` - 템플릿별 객체 조회

## 실행 방법

### 사전 요구사항
- Java 17 이상
- Maven 3.6 이상
- MongoDB 4.4 이상

### 1. MongoDB 실행
```bash
# MongoDB 실행 (기본 포트: 27017)
mongod
```

### 2. 애플리케이션 실행
```bash
# 프로젝트 빌드
mvn clean install

# 개발 환경으로 실행
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# 또는 JAR 파일로 실행
java -jar target/automationlab-be-1.0.0.jar
```

### 3. 환경별 실행
```bash
# 개발 환경
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# 운영 환경
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

## 환경 설정

### 개발 환경 (application-dev.yml)
- MongoDB: localhost:27017
- 데이터베이스: automationlab
- 로깅 레벨: DEBUG

### 운영 환경 (application-prod.yml)
- 환경 변수 기반 설정
- MongoDB 인증 지원
- 로깅 레벨: INFO

## 테스트

```bash
# 전체 테스트 실행
mvn test

# 특정 테스트 클래스 실행
mvn test -Dtest=Object3DServiceTest
```

## API 사용 예시

### 객체 생성
```bash
curl -X POST http://localhost:8080/api/v1/objects \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Industrial Robot Instance",
    "description": "6-axis industrial robot instance",
    "degrees": 0.0,
    "x": 10.0,
    "y": 5.0,
    "templateName": "industrial-robot-template"
  }'
```

### 객체 조회
```bash
# 모든 객체 조회
curl http://localhost:8080/api/v1/objects

# ID로 객체 조회
curl http://localhost:8080/api/v1/objects/{id}

# 템플릿별 객체 조회
curl http://localhost:8080/api/v1/objects/template/industrial-robot-template
```

## 데이터베이스 스키마

### Object3D 컬렉션: `objects`
```json
{
  "_id": "ObjectId",
  "name": "String (Required)",
  "description": "String (Optional)",
  "degrees": "Double (Required, 0-360)",
  "x": "Double (Required)",
  "y": "Double (Required)",
  "templateName": "String (Required, References Object3DTemplate)"
}
```

### Object3DTemplate 컬렉션: `object3d_templates`
```json
{
  "_id": "ObjectId",
  "name": "String (Required, Unique)",
  "category": "Enum (ROBOT|EQUIPMENT|APPLIANCES|AV|RACK)",
  "description": "String (Optional)",
  "glbFile": "String (Optional)",
  "thumbnailFile": "String (Optional)",
  "lodFile": "String (Optional)",
  "width": "Double (Required, Positive)",
  "depth": "Double (Required, Positive)",
  "height": "Double (Required, Positive)",
  "color": "String (Optional, HEX)",
  "instancingEnabled": "Boolean (Default: true)"
}
```

## 라이센스

이 프로젝트는 MIT 라이센스 하에 배포됩니다.
