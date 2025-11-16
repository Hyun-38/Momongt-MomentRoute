import React from "react";
import RegionBox from "./RegionBox.jsx";
import styles from "./RegionRecommend.module.css";

export default function RegionRecommendSection({ cities = [] }) {
  // 기본 값: 목적지 단독이면 1개, 경유지 많으면 여러 개
  // cities = ["광주시", "수원시", "양주시"] 이런 형태라고 가정

  return (
    <div className={styles.section}>
      <h2 className={styles.title}>지역별 맛집 & 관광지</h2>
      <p className={styles.sub}>
        각 경유지에서 꼭 가봐야 할 맛집과 관광명소를 추천해드립니다.
      </p>

      {/* 카드 컨테이너 */}
      <div
        className={
          cities.length === 1
            ? styles.oneColumn   // 도시 1개일 때
            : styles.gridTwo     // 도시 2개 이상일 때
        }
      >
        {cities.map((city, index) => (
          <RegionBox key={index} city={city} />
        ))}
      </div>
    </div>
  );
}
