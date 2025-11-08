# Flyway 마이그레이션 가이드

## 🦅 Flyway란?

데이터베이스 스키마를 **버전 관리**하는 도구입니다.
- ✅ 팀원 모두 동일한 DB 스키마 유지
- ✅ 변경 이력 추적 가능
- ✅ Git으로 스키마 변경 관리

---

## 📁 파일 구조

```
src/main/resources/db/migration/
├── V1__init_schema.sql           # 초기 스키마 (event 테이블)
└── V2__insert_sample_data.sql    # 샘플 데이터
```

### 파일 명명 규칙

```
V{버전}__{설명}.sql

예시:
V1__init_schema.sql
V2__add_user_table.sql
V3__add_event_location_column.sql
```

**중요:**
- `V` (대문자 V) + 버전 번호
- `__` (언더스코어 2개)
- 설명은 영문 또는 영문+숫자
- `.sql` 확장자

---

## 🔄 동작 방식

### 1. 앱 시작 시 자동 실행

```
1. Flyway가 DB 연결
2. flyway_schema_history 테이블 확인
3. 실행 안 된 마이그레이션 파일만 순서대로 실행
4. 실행 완료 후 버전 기록
```

### 2. flyway_schema_history 테이블

Flyway가 자동으로 생성하는 관리 테이블:

```sql
SELECT * FROM flyway_schema_history;

+-----------------+---------+------------------+------------+---------+
| installed_rank  | version | description      | type       | success |
+-----------------+---------+------------------+------------+---------+
| 1               | 1       | init schema      | SQL        | 1       |
| 2               | 2       | insert sample    | SQL        | 1       |
+-----------------+---------+------------------+------------+---------+
```

---

## ✏️ 새로운 스키마 변경 추가하기

### 예시 1: 새 테이블 추가

**파일명**: `V3__create_user_table.sql`

```sql
CREATE TABLE IF NOT EXISTS user (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_user_email ON user(email);
```

### 예시 2: 컬럼 추가

**파일명**: `V4__add_event_location.sql`

```sql
ALTER TABLE event 
ADD COLUMN location VARCHAR(255) COMMENT '이벤트 장소';

CREATE INDEX idx_event_location ON event(location);
```

### 예시 3: 데이터 수정

**파일명**: `V5__update_event_dates.sql`

```sql
UPDATE event 
SET date = '2025-04-01 ~ 2025-04-15' 
WHERE title = '경기도 벚꽃 축제';
```

---

## 🚀 실행 방법

### 로컬에서 테스트

```bash
# Gradle 빌드
./gradlew clean bootJar

# Docker Compose 재시작
docker compose down -v  # 볼륨 삭제 (깨끗한 상태)
docker compose up -d --build

# 로그 확인
docker compose logs app -f
```

### Flyway 실행 로그 확인

```
app-1 | Flyway Community Edition 10.x.x
app-1 | Database: jdbc:mysql://mysql:3306/appdb
app-1 | Successfully validated 2 migrations
app-1 | Creating Schema History table `appdb`.`flyway_schema_history`
app-1 | Current version of schema `appdb`: << Empty Schema >>
app-1 | Migrating schema `appdb` to version "1 - init schema"
app-1 | Migrating schema `appdb` to version "2 - insert sample data"
app-1 | Successfully applied 2 migrations to schema `appdb`
```

---

## 🔍 확인하기

### 마이그레이션 이력 확인

```bash
# MySQL 컨테이너 접속
docker compose exec mysql mysql -u appuser -p appdb

# 비밀번호 입력 후
mysql> SELECT * FROM flyway_schema_history;
mysql> SHOW TABLES;
mysql> SELECT * FROM event;
```

---

## ⚠️ 주의사항

### ❌ 하면 안 되는 것

1. **이미 실행된 파일 수정 금지**
   ```
   ❌ V1__init_schema.sql 수정 (이미 실행됨)
   ✅ V3__modify_event_table.sql 새로 생성
   ```

2. **버전 번호 건너뛰기 금지**
   ```
   ❌ V1, V2, V5 (V3, V4 누락)
   ✅ V1, V2, V3, V4, V5
   ```

3. **파일명 규칙 위반**
   ```
   ❌ v1_init.sql (소문자 v)
   ❌ V1_init.sql (언더스코어 1개)
   ✅ V1__init.sql
   ```

### ✅ 권장사항

- 각 마이그레이션은 작은 단위로
- 설명은 명확하게 (영문)
- 롤백 계획 포함 (주석)
- Git 커밋과 함께 관리

---

## 🛠️ 문제 해결

### 마이그레이션 실패 시

```bash
# 에러 확인
docker compose logs app

# 실패한 마이그레이션 확인
mysql> SELECT * FROM flyway_schema_history WHERE success = 0;

# 문제 해결 후 재실행
docker compose down -v
docker compose up -d --build
```

### 개발 중 스키마 초기화

```bash
# 데이터베이스 전체 삭제 후 재생성
docker compose down -v
docker compose up -d --build
```

---

## 📋 환경별 설정

### Local (IntelliJ)
```yaml
# application-local.yml
flyway:
  enabled: false  # H2 사용, Flyway 비활성화
```

### Dev (Docker 개발)
```yaml
# application-dev.yml
flyway:
  enabled: true
  baseline-on-migrate: true
```

### Prod (운영)
```yaml
# application-prod.yml
flyway:
  enabled: true
  validate-on-migrate: true
  clean-disabled: true  # 데이터 손실 방지
```

---

## 🎯 팀 협업 워크플로우

### 개발자 A가 스키마 변경

1. 새 마이그레이션 파일 생성
   ```sql
   -- V3__add_user_table.sql
   CREATE TABLE user (...);
   ```

2. Git 커밋 & 푸시
   ```bash
   git add src/main/resources/db/migration/V3__add_user_table.sql
   git commit -m "feat: Add user table migration"
   git push
   ```

### 개발자 B가 받기

1. Git Pull
   ```bash
   git pull
   ```

2. Docker 재시작
   ```bash
   docker compose down
   docker compose up -d --build
   ```

3. **자동으로 V3 마이그레이션 실행!**
   - Flyway가 V3만 자동 실행
   - 모든 팀원이 동일한 스키마 유지

---

## 📚 더 알아보기

- [Flyway 공식 문서](https://flywaydb.org/documentation/)
- [Migration Scripts](https://flywaydb.org/documentation/concepts/migrations)
- [Best Practices](https://flywaydb.org/documentation/concepts/migrations#best-practices)

---

## 🚀 Quick Start

```bash
# 1. 새 마이그레이션 파일 생성
echo "ALTER TABLE event ADD COLUMN location VARCHAR(255);" > \
  src/main/resources/db/migration/V3__add_event_location.sql

# 2. 빌드 & 재시작
./gradlew bootJar && docker compose up -d --build

# 3. 로그 확인
docker compose logs app -f

# 4. Git 커밋
git add src/main/resources/db/migration/
git commit -m "feat: Add event location column"
git push
```

**이제 모든 팀원이 동일한 DB 스키마를 자동으로 유지합니다!** 🎉

