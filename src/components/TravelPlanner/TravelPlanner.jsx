import React from "react";
import { useNavigate } from "react-router-dom";
import styles from "./TravelPlanner.module.css";
import locationIcon from "../../assets/locationIcon.svg";
import locationIconBlack from "../../assets/locationIconBlack.svg";
import addIcon from "../../assets/addIcon.svg";
import starWhite from "../../assets/starWhite.svg";

export default function TravelPlanner({ destination, stops, removeStop }) {
  const navigate = useNavigate();

  return (
    <div className={styles.wrapper}>
      <div className={styles.container}>
        <h1 className={styles.title}>경기도 여행 플래너</h1>

        <p className={styles.subtitle}>
          여행 경로를 설정하면 AI가 축제, 맛집, 관광지를 추천해드립니다.
        </p>

        <div className={styles.formBox}>

          {/* 목적지 */}
          <label className={styles.label}>
            <img src={locationIcon} className={styles.labelIcon} alt="" />
            목적지
          </label>

          <div className={styles.inputWrapper}>
            <input className={styles.input} value={destination || ""} readOnly />
            <img src={locationIconBlack} className={styles.inputIcon} alt="" />
          </div>

          {/* 경유지 */}
          <div className={styles.sectionTitle}>
            <img src={addIcon} alt="" />
            경유지 (선택사항)
          </div>

          <div className={styles.tagList}>
            {stops && stops.length > 0 ? (
              stops.map((stop) => (
                <div key={stop} className={styles.tag}>
                  {stop}
                  <span
                    className={styles.tagClose}
                    onClick={() => removeStop(stop)}
                  >
                    ×
                  </span>
                </div>
              ))
            ) : (
              <div className={styles.noTag}>선택된 경유지가 없습니다.</div>
            )}
          </div>

          {/* 경유지 추가 버튼 */}
          <div
            className={styles.addRow}
            onClick={() => navigate("/selectdestination")}
          >
            <img src={addIcon} alt="" />
            경유지 추가
          </div>

          <button className={styles.submitBtn}>
            <img src={starWhite} className={styles.starWhite} alt="" />
            AI 여행 추천 받기
          </button>
        </div>
      </div>
    </div>
  );
}
