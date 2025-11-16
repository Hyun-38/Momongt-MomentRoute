import React, { useState } from "react";
import styles from "./SaveTripModal.module.css";
import closeIcon from "../../assets/close.svg";
import saveIconBlue from "../../assets/saveIconBlue.svg"

export default function SaveTripModal({ onClose }) {
  const [tripName, setTripName] = useState("");
  const canSave = tripName.trim().length > 0;

  return (
    <div className={styles.overlay}>
      <div className={styles.modal}>
        <div className={styles.header}>
            <div className={styles.title}>
                <img 
                src={saveIconBlue} 
                className={styles.saveIconBlue} 
                onClick={onClose}
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
          >
            저장
          </button>
        </div>
      </div>
    </div>
  );
}
