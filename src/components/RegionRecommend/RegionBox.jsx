import React, { useState } from "react";
import styles from "./RegionRecommend.module.css";
import locationIcon from "../../assets/locationIcon.svg";
import refreshIcon from "../../assets/refresh.svg";
import starIcon from "../../assets/star.svg";

/* ⭐ 더미데이터 (나중에 API에서 받아오기) */
const dummyRestaurants = [
  {
    id: 1,
    img: "/images/food1.jpg",
    tag: "카페",
    title: "분당 정자동 카페거리",
    desc: "세련된 카페와 레스토랑",
    rating: 4.5,
  },
  {
    id: 2,
    img: "/images/food2.jpg",
    tag: "다양",
    title: "야탑 맛집거리",
    desc: "다양한 음식 문화",
    rating: 4.3,
  },
  {
    id: 3,
    img: "/images/food3.jpg",
    tag: "다양",
    title: "판교 테크노밸리 맛집",
    desc: "IT밸리 주변 트렌디한 맛집",
    rating: 4.4,
  },
];

const dummyTourist = [
  {
    id: 11,
    img: "/images/place1.jpg",
    tag: "명소",
    title: "남한산성",
    desc: "세계문화유산 명소",
    rating: 4.6,
  },
  {
    id: 12,
    img: "/images/place2.jpg",
    tag: "자연",
    title: "율동공원",
    desc: "여유로운 호수 산책",
    rating: 4.3,
  },
];

export default function RegionBox({ city }) {
  const [tab, setTab] = useState("맛집"); // 탭 상태

  /* ⭐ API 전환 시:
     fetch(`/api/recommend?city=${city}&type=${tab}`)
     → 결과로 리스트 변경
  */

  const data = tab === "맛집" ? dummyRestaurants : dummyTourist;

  return (
    <div className={styles.cityBox}>
      
      {/* 상단 헤더 */}
      <div className={styles.boxHeader}>
        <div className={styles.headerLeft}>
          <img src={locationIcon} alt="" className={styles.locIcon} />
          <div>
            <div className={styles.headerTitle}>{city} 추천 정보</div>
            <div className={styles.headerSub}>
              AI가 추천하는 {city}의 {tab}과 관광지
            </div>
          </div>
        </div>

        <button className={styles.refreshBtn}>
          <img src={refreshIcon} alt="" />
          새로고침
        </button>
      </div>

      {/* 탭 */}
      <div className={styles.tabWrap}>
        <button
          className={`${styles.tab} ${tab === "맛집" ? styles.activeTab : ""}`}
          onClick={() => setTab("맛집")}
        >
          🍜 맛집
        </button>

        <button
          className={`${styles.tab} ${tab === "관광지" ? styles.activeTab : ""}`}
          onClick={() => setTab("관광지")}
        >
          🏛 관광지
        </button>
      </div>

      {/* 카드 리스트 */}
      <div className={styles.cardList}>
        {data.map((item) => (
          <div key={item.id} className={styles.card}>
            <img src={item.img} className={styles.cardImg} alt="" />

            <div className={styles.cardInfo}>
              <div className={styles.cardTop}>
                <span className={styles.tag}>{item.tag}</span>

                <div className={styles.rating}>
                  <img src={starIcon} alt="" />
                  {item.rating}
                </div>
              </div>

              <div className={styles.name}>{item.title}</div>
              <div className={styles.desc}>{item.desc}</div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
