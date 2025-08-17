# automationlab_be - Vue.js App Backend

MongoDB를 사용하는 Node.js + Express.js 백엔드 API 서버입니다.

## 🚀 기술 스택

- **Runtime**: Node.js
- **Framework**: Express.js
- **Database**: MongoDB (Mongoose ODM)
- **Security**: Helmet, CORS, Rate Limiting
- **Logging**: Morgan
- **Environment**: dotenv

## 📁 프로젝트 구조

```
automationlab_be/
├── models/          # MongoDB 스키마 모델
│   ├── User.js     # 사용자 모델
│   └── Post.js     # 게시물 모델
├── routes/          # API 라우트
│   ├── users.js    # 사용자 관련 API
│   └── posts.js    # 게시물 관련 API
├── server.js        # 메인 서버 파일
├── package.json     # 프로젝트 의존성
├── env.example      # 환경 변수 예시
└── README.md        # 프로젝트 문서
```

## 🛠️ 설치 및 실행

### 1. 의존성 설치
```bash
npm install
```

### 2. 환경 변수 설정
`env.example` 파일을 복사하여 `.env` 파일을 생성하고 필요한 값들을 설정하세요:

```bash
cp env.example .env
```

`.env` 파일에서 다음 값들을 설정하세요:
- `MONGODB_URI`: MongoDB 연결 문자열
- `PORT`: 서버 포트 (기본값: 3000)
- `JWT_SECRET`: JWT 시크릿 키 (인증 기능 사용 시)

### 3. MongoDB 실행
로컬 MongoDB를 실행하거나 MongoDB Atlas를 사용하세요.

### 4. 서버 실행
```bash
# 개발 모드 (nodemon 사용)
npm run dev

# 프로덕션 모드
npm start
```

## 📡 API 엔드포인트

### 사용자 API (`/api/users`)
- `GET /` - 모든 사용자 조회
- `GET /:id` - 특정 사용자 조회
- `POST /` - 새 사용자 생성
- `PUT /:id` - 사용자 정보 수정
- `DELETE /:id` - 사용자 삭제

### 게시물 API (`/api/posts`)
- `GET /` - 모든 게시물 조회 (페이지네이션, 검색, 필터링)
- `GET /:id` - 특정 게시물 조회
- `POST /` - 새 게시물 생성
- `PUT /:id` - 게시물 수정
- `DELETE /:id` - 게시물 삭제
- `POST /:id/like` - 게시물 좋아요/좋아요 취소

## 🔧 주요 기능

- **RESTful API**: 표준 HTTP 메서드를 사용한 REST API
- **데이터 검증**: Mongoose 스키마를 통한 데이터 유효성 검사
- **에러 핸들링**: 전역 에러 핸들러와 적절한 HTTP 상태 코드
- **보안**: Helmet을 통한 보안 헤더, CORS 설정, Rate Limiting
- **로깅**: Morgan을 통한 HTTP 요청 로깅
- **페이지네이션**: 게시물 목록에 페이지네이션 지원
- **검색**: 제목과 내용 기반 텍스트 검색
- **필터링**: 카테고리별 게시물 필터링

## 🚧 향후 개선 사항

- [ ] JWT 기반 인증/인가 시스템
- [ ] 비밀번호 해시화 (bcrypt)
- [ ] 파일 업로드 기능
- [ ] 이메일 인증
- [ ] 소셜 로그인
- [ ] API 문서화 (Swagger)
- [ ] 테스트 코드 작성
- [ ] Docker 컨테이너화

## 📝 라이선스

ISC License

## 🤝 기여

이슈나 풀 리퀘스트를 통해 기여해주세요!
