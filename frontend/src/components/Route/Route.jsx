import React, { useState } from "react";
import axios from "axios";
import styles from "./Route.module.css";

import saveIcon from "../../assets/saveIcon.svg";
import routeIcon from "../../assets/routeIcon.svg";
import locateIcon from "../../assets/locateIcon.svg";
import foodIcon from "../../assets/food.svg";
import SaveTripModal from "../SaveModal/SaveTripModal";

export default function SelectedRouteCard({
  cities,
  foodPreferences = [],
}) {

  const [orderedCities, setOrderedCities] = useState(cities);
  const [showSaveModal, setShowSaveModal] = useState(false);

  // ⭐ /routes/simple 요청 함수
  const fetchSimpleRoute = async () => {
    try {
      const body = {
        viaCities: cities.slice(0, -1),               // 마지막 도시 제외
        destinationCity: cities[cities.length - 1],   // 마지막 도시
      };

      console.log("📤 POST /routes/simple 요청:", body);

      const res = await axios.post(
        "http://172.30.1.31:8080/api/routes/simple",
        body,
        {
          headers: { "Content-Type": "application/json" },
        }
      );

      console.log("🔥 응답:", res.data);

      if (res.data.route) {
        setOrderedCities(res.data.route); // ⭐ 응답 루트로 변경
      }

    } catch (err) {
      console.error("❌ /routes/simple 실패:", err);
      alert("추천 경로를 불러오는 중 오류가 발생했습니다.");
    }
  };

  // 컴포넌트가 처음 렌더링될 때 추천 경로 자동 요청
  React.useEffect(() => {
    fetchSimpleRoute();
  }, []);

  return (
    <>
      <div className={styles.card}>
        <div className={styles.header}>
          <div className={styles.left}>
            <img src={routeIcon} className={styles.routeIcon} alt="" />
            <div>
              <div className={styles.title}>선택하신 여행 경로</div>
              <div className={styles.subTitle}>총 {orderedCities.length}개 도시</div>
            </div>
          </div>

          <button
            className={styles.saveBtn}
            onClick={() => setShowSaveModal(true)}
          >
            <img src={saveIcon} alt="" />
            내 여행에 저장
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

      {/* 저장 모달 */}
      {showSaveModal && (
        <SaveTripModal
          onClose={() => setShowSaveModal(false)}
          routeData={{
            cities: orderedCities,
            foodPreferences: foodPreferences,
          }}
        />
      )}
    </>
  );
}
