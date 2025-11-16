# Momongt : Moment-Route
2025 학술제 - 모몽트 : Moment Route

---

# ✅ 코드 컨벤션

- **클래스명**: PascalCase (파스칼 케이스)  
- **메서드/변수명**: camelCase (카멜 케이스)
- **패키지명**: 소문자로 작성
- **PR/이슈 작성 시**: PR, 이슈 템플릿 사용

---

# ✅ 커밋 메시지 컨벤션

| 타입 | 설명 |
|------|------|
| `Fix(#이슈번호)` | 🐞 버그 수정 |
| `Chore(#이슈번호)` | 🔧 빌드 작업, 설정 수정 등 |
| `Refactor(#이슈번호)` | ♻️ 코드 리팩토링 (기능 변경 없음) |
| `Docs(#이슈번호)` | 📝 문서 수정 |
| `Feat(#이슈번호)` | ✨ 새로운 기능 추가 |

---

# 🌿 브랜치 전략

- 브랜치는 **이슈 템플릿 번호**를 기준으로 생성
- 브랜치 이름은 **이슈 번호를 함께 표기**  
  예) `feat/#1`

---

# 📦 패키지 구조
```
com.Momongt.Momongt_MomentRoute
│
├── config        : 설정 관련 클래스
├── exception     : 예외 처리 관련 클래스
├── jwt           : 로그인 클래스
├── controller    : 컨트롤러 클래스
├── service       : 서비스 인터페이스 및 구현체
├── repository    : 레포지토리 인터페이스
├── entity        : 엔티티 클래스
└── dto           : Request, Response DTO 클래스
```

---

# ⚙️ 개발 환경 설정

## 1. 데이터베이스 설정

프로젝트는 **환경 변수**를 통해 데이터베이스 연결 정보를 관리합니다.

### 로컬 환경 설정 방법
자세한 설정 방법은 [SETUP.md](SETUP.md) 문서를 참조하세요.

**간단 요약:**
1. 환경 변수 설정 (Windows/Mac/Linux)
2. IntelliJ에서 `local` 프로파일 활성화
3. 애플리케이션 실행

## 2. 애플리케이션 구성 파일

- **`application.properties`**: 공통 설정 (JPA, Hibernate 등)
- **`application-local.properties`**: 로컬 개발용 DB 연결 설정 (환경 변수 사용)

## 3. Swagger UI

애플리케이션 실행 후 다음 주소로 접속:
- `http://localhost:8080/swagger-ui.html`
- API 명세서 확인 및 테스트 가능


