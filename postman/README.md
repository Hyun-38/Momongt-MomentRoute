# API 테스트 가이드

## 🔍 API 테스트 방법 3가지

### 1️⃣ Swagger UI (브라우저에서 바로 테스트) ⭐ 권장

**URL**: http://localhost:8080/swagger-ui.html

브라우저에서 위 URL을 열면 인터랙티브한 API 문서를 확인하고 바로 테스트할 수 있습니다.

**기능**:
- 모든 API 엔드포인트 목록 확인
- 요청/응답 스키마 확인
- "Try it out" 버튼으로 직접 API 호출
- 응답 결과 즉시 확인

---

### 2️⃣ Postman Desktop App

#### 설치
[Postman 다운로드](https://www.postman.com/downloads/)

#### 사용 방법

**옵션 A: 컬렉션 파일 Import**
1. Postman 실행
2. **Import** 버튼 클릭
3. `postman/Momongt-MomentRoute-API.postman_collection.json` 선택
4. Environment도 import: `postman/local-environment.postman_environment.json`
5. 우측 상단에서 **Local Docker Environment** 선택
6. 컬렉션에서 요청 선택 → **Send** 클릭

**옵션 B: 직접 요청 생성**
1. **New** → **HTTP Request**
2. Method: `GET`
3. URL: `http://localhost:8080/api/main`
4. **Send** 클릭

#### 예상 응답
```json
{
  "eventList": [],
  "welcomeMessage": "경기도 여행, AI가 완벽하게 계획해드립니다"
}
```

---

### 3️⃣ cURL (터미널)

```bash
# 메인 페이지 조회
curl http://localhost:8080/api/main

# Pretty print JSON
curl http://localhost:8080/api/main | jq

# 상세 정보 포함
curl -v http://localhost:8080/api/main
```

---

## 📚 OpenAPI 문서

### JSON 형식
```bash
curl http://localhost:8080/v3/api-docs
```

### YAML 형식
```bash
curl http://localhost:8080/v3/api-docs.yaml
```

---

## 🐳 Docker 환경에서 테스트

현재 Docker Compose로 실행 중인 경우:

```bash
# 컨테이너 상태 확인
docker compose ps

# 애플리케이션 로그 확인
docker compose logs app -f

# API 테스트
curl http://localhost:8080/api/main
```

---

## 🔧 Troubleshooting

### API 응답이 없을 때
```bash
# 애플리케이션 재시작
docker compose restart app

# 전체 재시작
docker compose down && docker compose up -d --build
```

### 포트 충돌 확인
```bash
# 8080 포트 사용 프로세스 확인
lsof -i :8080
```

---

## 📝 API 엔드포인트 목록

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/main` | 메인 페이지 정보 조회 |

더 많은 엔드포인트는 Swagger UI에서 확인하세요: http://localhost:8080/swagger-ui.html

