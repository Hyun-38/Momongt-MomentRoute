# Momongt : Moment-Route
2025 학술제 - 모몽트 : Moment Route

AI 기반 여행 경로 추천 시스템

---

## 📂 프로젝트 구조

```
Momongt-MomentRoute/
├── backend/          # Spring Boot 백엔드
│   ├── src/
│   ├── build.gradle
│   └── README.md
│
├── frontend/         # React 프론트엔드 (예정)
│
└── README.md         # 이 파일
```

---

## 🚀 빠른 시작

### Backend
```bash
cd backend
./gradlew bootRun
```

자세한 내용: [backend/README.md](backend/README.md)

### Frontend
```bash
cd frontend
# Coming soon...
```

---

## ✅ 코드 컨벤션

- **클래스명**: PascalCase (파스칼 케이스)  
- **메서드/변수명**: camelCase (카멜 케이스)
- **패키지명**: 소문자로 작성
- **PR/이슈 작성 시**: PR, 이슈 템플릿 사용

---

## ✅ 커밋 메시지 컨벤션

| 타입 | 설명 |
|------|------|
| `Fix(#이슈번호)` | 🐞 버그 수정 |
| `Chore(#이슈번호)` | 🔧 빌드 작업, 설정 수정 등 |
| `Refactor(#이슈번호)` | ♻️ 코드 리팩토링 (기능 변경 없음) |
| `Docs(#이슈번호)` | 📝 문서 수정 |
| `Feat(#이슈번호)` | ✨ 새로운 기능 추가 |

---

## 🌿 브랜치 전략

- 브랜치는 **이슈 템플릿 번호**를 기준으로 생성
- 브랜치 이름은 **이슈 번호를 함께 표기**  
  예) `feat/#1`

---

## 📦 Backend 패키지 구조
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

## 🔧 개발 환경

### Backend
- Java 17
- Spring Boot 3.5.7
- MySQL
- Gradle

### Frontend (예정)
- React
- TBD

---

## 📚 문서

- [Backend 설정 가이드](backend/SETUP.md)
- [Backend README](backend/README.md)

---

## 👥 팀

2025 학술제 - 모몽트 팀


