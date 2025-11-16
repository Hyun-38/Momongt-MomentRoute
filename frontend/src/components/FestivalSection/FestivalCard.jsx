import React from "react";
import styles from "./FestivalSection.module.css";

import calendarIcon from "../../assets/calendar.svg";
import locationIcon from "../../assets/locationIconWhite.svg";

export default function FestivalCard({ data }) {
  if (!data) return null;

  const {
    img = "",
    tags = [],
    title = "",
    location = "",
    start = "",
    end = ""
  } = data;

  return (
    <div
      className={styles.cardBackground}
      style={{ backgroundImage: `url(${img})` }}
    >
      <div className={styles.overlay}>
        <div className={styles.tagWrap}>
          {tags.map((t, i) => (
            <span key={i} className={styles.tag}>
              {t}
            </span>
          ))}
        </div>

        <div className={styles.cardTitle}>{title}</div>

        <div className={styles.cardRow}>
          <img src={locationIcon} className={styles.icon} alt="위치" />
          <span>{location}</span>
        </div>

        <div className={styles.cardRow}>
          <img src={calendarIcon} className={styles.icon} alt="기간" />
          <span>
            {start} ~ {end}
          </span>
        </div>
      </div>
    </div>
  );
}
