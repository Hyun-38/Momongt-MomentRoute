import React, { useState } from "react";
import styles from "./SelectDestination.module.css";
import Header from "../components/Header/Header.jsx";
import { useNavigate, useLocation } from "react-router-dom";

export default function RegionSelectMulti() {
  const navigate = useNavigate();
  const location = useLocation();

  const region = location.state?.region;

  const regions = [
    "가평군","고양시","과천시","광명시","광주시","구리시",
    "군포시","김포시","남양주시","동두천시","부천시","성남시",
    "수원시","시흥시","안산시","안성시","안양시","양주시",
    "양평군","여주시","연천군","오산시","용인시","의왕시",
    "의정부시","이천시","파주시","평택시","포천시","하남시",
    "화성시"
  ];

  const [selected, setSelected] = useState([]);

  const handleSelect = (region) => {
    if (selected.includes(region)) {
      setSelected(selected.filter((r) => r !== region));
    } else {
      if (selected.length >= 4) return;
      setSelected([...selected, region]);
    }
  };

  const goNext = () => {
    if (selected.length === 0) return;

    navigate("/styleselect", {
      state: {
        region: region,
        viaRegions: selected   // ⭐ 경유지는 여기까지만 viaRegions
      }
    });
  };

  return (
    <div className={styles.page}>
      <Header />

      <div className={styles.container}>
        <div className={styles.topTextSmall}>이번 여행, 어디로 떠나볼까요?</div>

        <div className={styles.topTextLarge}>
          목적지로 갈 때, 들르고 싶은 경유지를 <br />
          선택해 주세요. <span className={styles.limit}>( 최대 4개 )</span>
        </div>

        <div className={styles.grid}>
          {regions.map((r) => (
            <button
              key={r}
              className={`${styles.regionBtn} ${
                selected.includes(r) ? styles.active : ""
              }`}
              onClick={() => handleSelect(r)}
            >
              {r}
            </button>
          ))}
        </div>

        <div className={styles.bottomButtons}>
          <button className={styles.prevBtn} onClick={() => navigate(-1)}>
            이전
          </button>

          <button
            className={`${styles.nextBtn} ${
              selected.length > 0 ? styles.enabled : ""
            }`}
            onClick={goNext}
          >
            다음
          </button>
        </div>
      </div>
    </div>
  );
}
