import React from "react";
import styles from "./Route.module.css";
import saveIcon from "../../assets/saveIcon.svg";
import routeIcon from "../../assets/routeIcon.svg";
import locateIcon from "../../assets/locateIcon.svg";
import foodIcon from "../../assets/food.svg";
import ticketIcon from "../../assets/ticket.svg";

export default function SelectedRouteCard({
  cities,
  foodPreferences = [],
  categories = [],
  onSave,
}) {

  const orderedCities = cities;

  return (
    <div className={styles.card}>
      <div className={styles.header}>
        <div className={styles.left}>
          <img src={routeIcon} className={styles.routeIcon} alt="" />
          <div>
            <div className={styles.title}>선택하신 여행 경로</div>
            <div className={styles.subTitle}>총 {orderedCities.length}개 도시</div>
          </div>
        </div>

        <button className={styles.saveBtn} onClick={onSave}>
          <img src={saveIcon} alt="" />
          내 여행에 저장
        </button>
      </div>

      {/* 도시 경로 */}
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
            <span className={styles.noData}>선호하는 음식이 선택되지 않았습니다.</span>
          )}
        </div>
      </div>

      {/* 여행 카테고리 */}
      <div className={styles.section}>
        <div className={styles.sectionTitle}>
            <img src={ticketIcon} className={styles.ticketIcon} alt="" />
            여행 카테고리
        </div>
        <div className={styles.pillContainer}>
          {categories.length > 0 ? (
            categories.map((c, idx) => (
              <span key={idx} className={styles.categoryPill}>
                {c}
              </span>
            ))
          ) : (
            <span className={styles.noData}>선호하는 카테고리가 선택되지 않았습니다.</span>
          )}
        </div>
      </div>
    </div>
  );
}
