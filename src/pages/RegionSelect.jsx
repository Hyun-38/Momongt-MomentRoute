import React, { useState } from "react";
import styles from "./RegionSelect.module.css";
import Header from "../components/Header/Header.jsx";
import { useNavigate } from "react-router-dom";
import { useTravel } from "../context/TravelContext";

export default function RegionSelect() {
  const navigate = useNavigate();

  const regions = [
    "가평군", "고양시", "과천시",
    "광명시", "광주시", "구리시",
    "군포시", "김포시", "남양주시",
    "동두천시", "부천시", "성남시",
    "수원시", "시흥시", "안산시",
    "안성시", "안양시", "양주시",
    "양평군", "여주시", "연천군",
    "오산시", "용인시", "의왕시",
    "의정부시", "이천시", "파주시",
    "평택시", "포천시", "하남시",
    "화성시"
  ];

  const [selected, setSelected] = useState(null);

  const handleSelect = (region) => {
    setSelected(region);
  };

  const goNext = () => {
    if (!selected) return;
    navigate("/selectdestination", { state: { region: selected } });
  };

  return (
    <div className={styles.page}>
      <Header />

      <div className={styles.container}>
        <div className={styles.topTextSmall}>이번 여행, 어디로 떠나볼까요?</div>
        <div className={styles.topTextLarge}>여행을 떠나고 싶은 지역을 <br></br>선택해 주세요.</div>

        <div className={styles.grid}>
          {regions.map((region) => (
            <button
              key={region}
              className={`${styles.regionBtn} ${selected === region ? styles.active : ""}`}
              onClick={() => handleSelect(region)}
            >
              {region}
            </button>
          ))}
        </div>

        <button
          className={`${styles.nextBtn} ${selected ? styles.enabled : ""}`}
          onClick={goNext}
        >
          다음
        </button>
      </div>
    </div>
  );
}
