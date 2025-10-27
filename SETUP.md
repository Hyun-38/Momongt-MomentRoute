# 🔐 로컬 개발 환경 설정 가이드

이 프로젝트는 **환경 변수**를 통해 데이터베이스 연결 정보를 관리합니다.

---

## 📋 필요한 환경 변수

| 변수명 | 설명 | 예시 |
|--------|------|------|
| `DB_URL` | 데이터베이스 URL | `jdbc:mysql://localhost:3306/momongt_db?serverTimezone=Asia/Seoul&useSSL=false&allowPublicKeyRetrieval=true` |
| `DB_USERNAME` | 데이터베이스 사용자명 | `root` |
| `DB_PASSWORD` | 데이터베이스 비밀번호 | `your_password` |

---

## 🖥️ 운영체제별 설정 방법

### 1️⃣ Windows (CMD)

```cmd
set DB_URL=jdbc:mysql://localhost:3306/momongt_db?serverTimezone=Asia/Seoul^&useSSL=false^&allowPublicKeyRetrieval=true
set DB_USERNAME=root
set DB_PASSWORD=your_password
```

⚠️ **주의**: `&` 문자 앞에 `^`를 붙여야 합니다.

### 2️⃣ Windows (PowerShell)

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/momongt_db?serverTimezone=Asia/Seoul&useSSL=false&allowPublicKeyRetrieval=true"
$env:DB_USERNAME="root"
$env:DB_PASSWORD="your_password"
```

### 3️⃣ Mac / Linux (Bash/Zsh)

```bash
export DB_URL="jdbc:mysql://localhost:3306/momongt_db?serverTimezone=Asia/Seoul&useSSL=false&allowPublicKeyRetrieval=true"
export DB_USERNAME="root"
export DB_PASSWORD="your_password"
```

---

## ✅ 환경 변수 확인 방법

### Windows (CMD)
```cmd
echo %DB_URL%
echo %DB_USERNAME%
echo %DB_PASSWORD%
```

### Windows (PowerShell)
```powershell
echo $env:DB_URL
echo $env:DB_USERNAME
echo $env:DB_PASSWORD
```

### Mac / Linux
```bash
echo $DB_URL
echo $DB_USERNAME
echo $DB_PASSWORD
```

---

## 🚀 IntelliJ IDEA 설정 (권장)

터미널에서 설정한 환경 변수는 **해당 세션에만 유효**합니다.  
IntelliJ에서 실행하려면 다음 설정이 필요합니다:

### 1. Run Configuration 설정

1. **Run** → **Edit Configurations** 클릭
2. **Active profiles** 필드에 `local` 입력
3. **Environment variables** 옆의 폴더 아이콘 클릭
4. 다음 변수 추가:
   - `DB_URL`: `jdbc:mysql://localhost:3306/momongt_db?serverTimezone=Asia/Seoul&useSSL=false&allowPublicKeyRetrieval=true`
   - `DB_USERNAME`: `root`
   - `DB_PASSWORD`: `your_password`
5. **Apply** → **OK** 클릭

### 2. 프로파일 동작 방식

```
application.properties (공통 설정)
         ↓
application-local.properties (local 프로파일 활성화 시)
         ↓
환경 변수에서 DB 정보 읽기
```

---

## 🔒 보안 주의사항

### ⚠️ 절대 Git에 커밋하면 안 되는 것:
- 실제 DB 비밀번호가 포함된 파일
- `application-local.properties`는 `.gitignore`에 포함되어 자동 제외됨

### ✅ Git에 포함되는 파일:
- `application.properties` (공통 설정만)
- `application-local.properties.example` (참고용 템플릿)

---

## 🧪 설정 테스트

1. IntelliJ에서 애플리케이션 실행
2. 콘솔 로그 확인:
   ```
   Tomcat started on port(s): 8080
   ```
3. Swagger UI 접속: `http://localhost:8080/swagger-ui.html`

---

## 🛠️ 문제 해결

### 문제 1: 환경 변수가 인식되지 않음
- ✅ IntelliJ **Run Configuration**에서 직접 설정했는지 확인
- ✅ `Active profiles`에 `local` 입력했는지 확인

### 문제 2: DB 연결 실패
- ✅ MySQL 서버가 실행 중인지 확인
- ✅ 데이터베이스 `momongt_db`가 생성되어 있는지 확인
- ✅ 사용자명/비밀번호가 올바른지 확인

### 문제 3: Swagger UI 접속 불가
- ✅ 애플리케이션이 정상 실행되었는지 확인
- ✅ 포트 8080이 사용 중인지 확인

---

## 📚 추가 정보

- [Spring Boot Profiles 공식 문서](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.profiles)
- [IntelliJ Run Configuration](https://www.jetbrains.com/help/idea/run-debug-configuration.html)

