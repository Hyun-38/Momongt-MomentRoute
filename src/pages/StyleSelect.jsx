import React, { useState } from "react";
import styles from "./StyleSelect.module.css";
import Header from "../components/Header/Header.jsx";
import { useNavigate, useLocation } from "react-router-dom";
import foodIcon from "../assets/food.svg";
import ticketIcon from "../assets/ticket.svg";

export default function FoodStyleSelect() {
  const navigate = useNavigate();
  const location = useLocation();

  // ⭐ RegionSelectMulti에서 전달된 값들
  const region = location.state?.region;         // 목적지
  const viaRegions = location.state?.viaRegions; // 경유지 → TravelPlanner에서 stops로 사용함

  const foodTypes = ["한식", "양식", "일식", "중식"];
  const categories = ["전시", "대학교 축제", "관광", "문화행사", "자연경관", "쇼핑"];

  const [selectedFood, setSelectedFood] = useState([]);
  const [selectedCategories, setSelectedCategories] = useState([]);

  const toggleFood = (food) => {
    setSelectedFood((prev) =>
      prev.includes(food) ? prev.filter((f) => f !== food) : [...prev, food]
    );
  };

  const toggleCategory = (category) => {
    setSelectedCategories((prev) =>
      prev.includes(category)
        ? prev.filter((c) => c !== category)
        : [...prev, category]
    );
  };

  const goNext = () => {
    navigate("/main", {
      state: {
        destination: region,     // ⭐ 목적지 이름 일치!
        stops: viaRegions,       // ⭐ 경유지 이름 통일 완료!
        food: selectedFood,
        categories: selectedCategories,
      },
    });
  };

  const canNext = selectedFood.length > 0 || selectedCategories.length > 0;

  return (
    <div className={styles.page}>
      <Header />

      <div className={styles.container}>
        <div className={styles.topTextSmall}>이번 여행을 더욱 특별하게</div>

        <div className={styles.topTextLarge}>
          선호하는 음식과 여행 스타일을<br />
          선택해 주세요. (복수 선택 가능)
        </div>

        <div className={styles.sectionTitle}>
          <img src={foodIcon} className={styles.icon} alt="" /> 음식 종류
        </div>

        <div className={styles.grid}>
          {foodTypes.map((food) => (
            <button
              key={food}
              className={`${styles.optionBtn} ${
                selectedFood.includes(food) ? styles.active : ""
              }`}
              onClick={() => toggleFood(food)}
            >
              {food}
            </button>
          ))}
        </div>

        <div className={styles.sectionTitle}>
          <img src={ticketIcon} className={styles.icon} alt="" /> 여행 카테고리
        </div>

        <div className={styles.grid}>
          {categories.map((cat) => (
            <button
              key={cat}
              className={`${styles.optionBtn} ${
                selectedCategories.includes(cat) ? styles.active : ""
              }`}
              onClick={() => toggleCategory(cat)}
            >
              {cat}
            </button>
          ))}
        </div>

        <div className={styles.btnRow}>
          <button className={styles.prevBtn} onClick={() => navigate(-1)}>
            이전
          </button>

          <button
            className={`${styles.nextBtn} ${canNext ? styles.enabled : ""}`}
            onClick={goNext}
            disabled={!canNext}
          >
            다음
          </button>
        </div>
      </div>
    </div>
  );
}
