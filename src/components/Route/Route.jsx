import React from "react";
import axios from "axios";
import styles from "./Route.module.css";

import saveIcon from "../../assets/saveIcon.svg";
import routeIcon from "../../assets/routeIcon.svg";
import locateIcon from "../../assets/locateIcon.svg";
import foodIcon from "../../assets/food.svg";

export default function SelectedRouteCard({
  cities,
  foodPreferences = [],
  onSave,
}) {

  const orderedCities = cities;

  // 🚀 추천 경로 API 호출 함수 (axiosInstance 사용 X)
  const handleRecommend = async () => {
    try {
      const body = {
        viaCities: orderedCities.slice(0, -1),
        destinationCity: orderedCities[orderedCities.length - 1],
        preferredCategories: foodPreferences
      };

      console.log("📤 POST /routes/recommend 요청:", body);

      const res = await axios.post(
        "http://172.30.1.31:8080/api/routes/recommend",
        body,
        {
          headers: {
            "Content-Type": "application/json"
          }
        }
      );

      console.log("🔥 추천 경로 응답:", res.data);
      alert("추천 경로를 받아왔습니다!");

      if (onSave) {
        onSave(res.data);
      }

    } catch (error) {
      console.error("❌ 추천 경로 요청 실패:", error);
      alert("추천 경로 요청 중 오류가 발생했습니다.");
    }
  };

  return (
    <div className={styles.card}>
      <div className={styles.header}>
        <div className={styles.left}>
          <img src={routeIcon} className={styles.routeIcon} alt="" />
          <div>
            <div className={styles.title}>선택하신 여행 경로</div>
            <div className={styles.subTitle}>
              총 {orderedCities.length}개 도시
            </div>
          </div>
        </div>

        <button className={styles.saveBtn} onClick={handleRecommend}>
          <img src={saveIcon} alt="" />
          추천 경로 받기
        </button>
      </div>

      {/* 도시 경로 표시 */}
      <div className={styles.cityRow}>
        {orderedCities.map((city, index) => (
          <React.Fragment key={index}>
            <div className={styles.cityPill}>
              <img src={locateIcon} className={styles.locateIcon} alt="" />
              {city}
            </div>
            {index < orderedCities.length - 1 && (
              <span className={styles.arrow}>→</span>
            )}
          </React.Fragment>
        ))}
      </div>

      {/* 음식 선호 */}
      <div className={styles.section}>
        <div className={styles.sectionTitle}>
          <img src={foodIcon} className={styles.foodIcon} alt="" />
          음식 선호
        </div>
        <div className={styles.pillContainer}>
          {foodPreferences.length > 0 ? (
            foodPreferences.map((f, idx) => (
              <span key={idx} className={styles.foodPill}>
                {f}
              </span>
            ))
          ) : (
            <span className={styles.noData}>
              선호하는 음식이 선택되지 않았습니다.
            </span>
          )}
        </div>
      </div>
    </div>
  );
}
