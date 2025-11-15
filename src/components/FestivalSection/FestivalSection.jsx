import React from "react";
import styles from "./FestivalSection.module.css";
import FestivalCard from "./FestivalCard.jsx";
import routeIcon from "../../assets/locationIcon.svg";

export default function FestivalSection({ cities = [] }) {

  const cityPath = cities.join(" → ");

  const dummyFestivals = [
    {
      id: 1,
      img: "/images/festival1.jpg",
      tags: ["축제", "전통", "문화"],
      title: "수원 화성문화제 2025",
      location: "수원시 팔달구",
      start: "2025-10-05",
      end: "2025-10-12",
    },
    {
      id: 2,
      img: "/images/festival2.jpg",
      tags: ["전시", "전시회", "졸업작품"],
      title: "경기대학교 졸업전시회",
      location: "수원시 영통구",
      start: "2025-10-15",
      end: "2025-10-22",
    },
    {
      id: 3,
      img: "/images/festival3.jpg",
      tags: ["축제", "책", "문화"],
      title: "파주 북소리 축제",
      location: "파주시 리움",
      start: "2025-10-12",
      end: "2025-10-14",
    },
    {
      id: 4,
      img: "/images/festival4.jpg",
      tags: ["체험", "축제"],
      title: "부천 만화축제",
      location: "부천시",
      start: "2025-09-01",
      end: "2025-09-05",
    },
    {
      id: 5,
      img: "/images/festival5.jpg",
      tags: ["환경", "문화"],
      title: "하남 환경문화제",
      location: "하남시",
      start: "2025-10-20",
      end: "2025-10-23",
    },
    
  ];

  return (
    <div className={styles.section}>
      
      <h2 className={styles.title}>경로 기반 축제 및 이벤트</h2>
      <p className={styles.sub}>
        여행 경로에 있는 도시에서 진행 중인 축제와 이벤트를 확인하세요
      </p>

      <div className={styles.recommendBox}>
        <div className={styles.recommendHeader}>
          <img src={routeIcon} className={styles.recoIcon} alt="" />
          <div className={styles.recoTitle}>AI 추천 축제 및 이벤트</div>
        </div>

        <div className={styles.recoText}>
          {cities.length > 0
            ? `${cityPath} 경로에서 참여 가능한 축제와 이벤트입니다.`
            : "여행 경로를 설정해주세요."}
        </div>

        <div className={styles.cityPills}>
          {cities.map((city) => (
            <div key={city} className={styles.cityPill}>
              {city}
            </div>
          ))}
        </div>
      </div>

      {/* 카드 리스트 (최대 6개 표시) */}
      <div className={styles.cardList}>
        {dummyFestivals.slice(0, 6).map((item) => (
          <FestivalCard key={item.id} data={item} />
        ))}
      </div>
    </div>
  );
}
