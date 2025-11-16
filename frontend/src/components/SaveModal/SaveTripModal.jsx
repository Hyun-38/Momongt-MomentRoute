import React, { useState } from "react";
import axios from "axios";
import styles from "./SaveTripModal.module.css";
import closeIcon from "../../assets/close.svg";
import saveIconBlue from "../../assets/saveIconBlue.svg";
import { useNavigate } from "react-router-dom";

export default function SaveTripModal({ onClose, routeData }) {
  const navigate = useNavigate();
  const [tripName, setTripName] = useState("");

  const canSave = tripName.trim().length > 0;

  // 🚀 여행 저장 API 호출
  const handleSave = async () => {
    if (!canSave) return;

    const body = {
      tripName: tripName,
      cities: routeData.cities,
      foodPreferences: routeData.foodPreferences,
    };

    console.log("📤 POST /travel/save 요청:", body);

    try {
      const res = await axios.post(
        "http://13.124.41.43/api/travel/save",
        body,
        { headers: { "Content-Type": "application/json" } }
      );

      console.log("🔥 저장 성공:", res.data);
      alert("여행이 저장되었습니다!");

      onClose(); // 모달 닫기
      navigate("/mytrips"); // MyTrips 페이지로 이동

    } catch (error) {
      console.error("❌ 저장 실패:", error);
      alert("여행 저장 중 오류가 발생했습니다.");
    }
  };

  return (
    <div className={styles.overlay}>
      <div className={styles.modal}>
        <div className={styles.header}>
          <div className={styles.title}>
            <img
              src={saveIconBlue}
              className={styles.saveIconBlue}
              alt="saveIconBlue"
            />
            여행 저장하기
          </div>
          <img
            src={closeIcon}
            className={styles.close}
            onClick={onClose}
            alt="close"
          />
        </div>

        <p className={styles.desc}>
          이 여행 계획을 저장하면 나중에 다시 확인할 수 있습니다.
        </p>

        <label className={styles.label}>여행 이름</label>
        <input
          className={styles.input}
          placeholder="예: 경기도 가을 축제 여행"
          value={tripName}
          onChange={(e) => setTripName(e.target.value)}
        />

        <div className={styles.buttonRow}>
          <button className={styles.cancelBtn} onClick={onClose}>
            취소
          </button>

          <button
            className={`${styles.saveBtn} ${!canSave ? styles.disabled : ""}`}
            disabled={!canSave}
            onClick={handleSave}
          >
            저장
          </button>
        </div>
      </div>
    </div>
  );
}
