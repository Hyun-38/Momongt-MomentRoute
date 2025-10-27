# 🔐 로컬 개발 환경 설정 가이드

## 데이터베이스 연결 설정

### 방법 1: 환경 변수 사용 (권장)

#### Windows (cmd)
```cmd
set DB_URL=jdbc:mysql://localhost:3306/momongt_db?serverTimezone=Asia/Seoul&useSSL=false&allowPublicKeyRetrieval=true
set DB_USERNAME=root
set DB_PASSWORD=your_password
```

#### Windows (PowerShell)
```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/momongt_db?serverTimezone=Asia/Seoul&useSSL=false&allowPublicKeyRetrieval=true"
$env:DB_USERNAME="root"
$env:DB_PASSWORD="your_password"
```

#### Mac/Linux (bash)
```bash
export DB_URL=jdbc:mysql://localhost:3306/momongt_db?serverTimezone=Asia/Seoul&useSSL=false&allowPublicKeyRetrieval=true
export DB_USERNAME=root
export DB_PASSWORD=your_password
```

### 방법 2: IntelliJ IDEA 환경 변수 설정

1. **Run** → **Edit Configurations** 선택
2. **Environment variables** 항목에 다음 추가:
   ```
   DB_URL=jdbc:mysql://localhost:3306/momongt_db?serverTimezone=Asia/Seoul&useSSL=false&allowPublicKeyRetrieval=true;DB_USERNAME=root;DB_PASSWORD=your_password
   ```

### 방법 3: application-local.properties 파일 생성

1. `src/main/resources/application-local.properties.example` 파일 복사
2. `application-local.properties`로 이름 변경
3. 파일 내용은 그대로 두고, 실행 시 환경 변수로 값 전달

## ⚠️ 보안 주의사항

- `application-local.properties` 파일은 **절대 Git에 커밋하지 마세요**
- `.gitignore`에 이미 추가되어 있어 자동으로 제외됩니다
- 민감한 정보(비밀번호, API 키 등)는 항상 환경 변수로 관리하세요

## 실행 방법

```bash
# 빌드
./gradlew build

# 실행 (환경 변수 설정 후)
./gradlew bootRun --args='--spring.profiles.active=local'
```

