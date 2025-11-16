# Momongt Backend

Spring Boot 기반 여행 추천 시스템 백엔드

## 🚀 시작하기

### 개발 환경 설정

자세한 설정 방법은 [SETUP.md](SETUP.md) 참조

### 빌드 및 실행

```bash
# 루트가 아닌 backend 폴더에서 실행
cd backend

# 빌드
./gradlew build

# 실행
./gradlew bootRun
```

## 📚 API 문서

애플리케이션 실행 후:
- Swagger UI: `http://localhost:8080/swagger-ui.html`

## 🎯 주요 API

### 경로 최적화 API
- `POST /api/routes/simple` - 간단한 경로 순서 (도시명 리스트)
- `POST /api/routes/optimize` - 상세 경로 정보 (위도/경도 포함)
- `POST /api/routes/recommend` - AI 추천 포함

### 회원 관리 API
- `POST /api/member/signup` - 회원가입
- `POST /api/member/login` - 로그인
- `GET /api/member/me` - 내 정보 조회
- `PUT /api/member/me` - 내 정보 수정
- `PUT /api/member/password` - 비밀번호 변경

## 📦 기술 스택

- Java 17
- Spring Boot 3.5.7
- Spring Data JPA
- MySQL
- OpenAI API
- Swagger/OpenAPI 3.0

## 🔧 환경 변수

필수 환경 변수:
- `DB_URL` - MySQL 연결 URL
- `DB_USERNAME` - DB 사용자명
- `DB_PASSWORD` - DB 비밀번호
- `OPENAI_API_KEY` - OpenAI API 키

