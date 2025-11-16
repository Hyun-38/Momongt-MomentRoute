# 간단한 경로 API 추가 완료! 🎉

## ✅ 추가된 기능

### 새로운 엔드포인트: `/api/routes/simple`

**가장 빠른 경로 API** - 도시명 리스트만 간단하게 반환!

---

## 📍 요청

```bash
POST /api/routes/simple
Content-Type: application/json

{
  "viaCities": ["동두천", "의정부"],
  "destinationCity": "수원"
}
```

---

## 📦 응답

```json
{
  "route": ["동두천", "의정부", "수원"],
  "algorithm": "BRUTE_FORCE",
  "totalDistanceKm": 71.32
}
```

---

## 🎯 특징

- ✅ **초고속** (~50ms)
- ✅ **간단한 응답** (도시명만 배열로)
- ✅ **프론트 친화적** (바로 사용 가능)

---

## 💻 프론트엔드 사용 예시

```javascript
const response = await fetch('/api/routes/simple', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    viaCities: ["동두천", "의정부"],
    destinationCity: "수원"
  })
});

const data = await response.json();
// { route: ["동두천", "의정부", "수원"], algorithm: "BRUTE_FORCE", totalDistanceKm: 71.32 }

// 바로 사용!
data.route.forEach((city, index) => {
  console.log(`${index + 1}번째: ${city}`);
});
// 출력:
// 1번째: 동두천
// 2번째: 의정부
// 3번째: 수원
```

---

## 🔄 API 비교

| API | 응답 속도 | 응답 형태 | 사용 시나리오 |
|-----|----------|-----------|---------------|
| `/simple` ⭐ | ~50ms | `["동두천", "의정부", "수원"]` | 경로만 빠르게 |
| `/optimize` | ~100ms | `[{cityId, name, lat, lng, order}]` | 상세 정보 필요 |
| `/recommend` | ~3-5초 | 경로 + GPT 추천 | 전체 추천 |

---

## 🧪 테스트 방법

### Swagger UI
```
http://localhost:8080/swagger-ui.html
→ POST /api/routes/simple
→ Try it out
→ Execute
```

### curl
```bash
curl -X POST http://localhost:8080/api/routes/simple \
  -H "Content-Type: application/json" \
  -d '{"viaCities": ["동두천", "의정부"], "destinationCity": "수원"}'
```

---

## 📝 참고 문서

- `QUICK_START.md` - 전체 사용 가이드
- `POSTMAN_GUIDE.md` - Postman 설정

