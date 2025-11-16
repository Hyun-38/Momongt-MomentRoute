import React, { useState, useEffect } from "react";
import axios from "axios";
import styles from "./RegionRecommend.module.css";
import locationIcon from "../../assets/locationIcon.svg";
import refreshIcon from "../../assets/refresh.svg";
import starIcon from "../../assets/star.svg";

export default function RegionBox({
  viaCities = [],
  destinationCity = "",
  preferredCategories = [],
}) {
  const [tab, setTab] = useState("맛집");
  const [foods, setFoods] = useState([]);
  const [tourist, setTourist] = useState([]); // 관광지(명소+전시회+축제)
  const [loading, setLoading] = useState(true);

  // ⭐ API 요청 함수
  const fetchRecommend = async () => {
    setLoading(true);

    const body = {
      viaCities,
      destinationCity,
      preferredCategories,
    };

    console.log("📤 POST /routes/recommend:", body);

    try {
      const res = await axios.post(
        "http://172.30.1.31:8080/api/routes/recommend",
        body,
        { headers: { "Content-Type": "application/json" } }
      );

      console.log("🔥 추천 응답:", res.data);

      const cityData = res.data.cities?.[0];

      if (cityData) {
        setFoods(cityData.foods || []);

        // ⭐ 관광지 = attractions + festivals + exhibitions 합치기
        const mergedAttractions = [
          ...(cityData.attractions || []),
          ...(cityData.festivals || []),
          ...(cityData.exhibitions || []),
        ];

        setTourist(mergedAttractions);
      }

    } catch (error) {
      console.error("❌ 추천 정보 로딩 실패:", error);
      alert("추천 정보를 불러오는 중 오류가 발생했습니다.");
    }

    setLoading(false);
  };

  // 처음 로딩 + 새로고침 버튼 누르면 실행
  useEffect(() => {
    fetchRecommend();
  }, []);

  const data = tab === "맛집" ? foods : tourist;

  return (
    <div className={styles.cityBox}>

      {/* 상단 헤더 */}
      <div className={styles.boxHeader}>
        <div className={styles.headerLeft}>
          <img src={locationIcon} alt="" className={styles.locIcon} />
          <div>
            <div className={styles.headerTitle}>{destinationCity} 추천 정보</div>
            <div className={styles.headerSub}>
              AI가 추천하는 {destinationCity}의 {tab}
            </div>
          </div>
        </div>

        <button className={styles.refreshBtn} onClick={fetchRecommend}>
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

      {/* 데이터 로딩 중 */}
      {loading ? (
        <p className={styles.loading}>추천 정보를 불러오는 중입니다...</p>
      ) : (
        <div className={styles.cardList}>
          {data.length > 0 ? (
            data.map((item) => (
              <div key={item.placeId} className={styles.card}>
                <img src={item.imageUrl} className={styles.cardImg} alt="" />

                <div className={styles.cardInfo}>
                  <div className={styles.cardTop}>
                    <span className={styles.tag}>{item.category}</span>

                    <div className={styles.rating}>
                      <img src={starIcon} alt="" />
                      {item.rating ?? 4.3}
                    </div>
                  </div>

                  <div className={styles.name}>{item.name}</div>
                  <div className={styles.desc}>{item.description}</div>
                </div>
              </div>
            ))
          ) : (
            <p className={styles.noData}>데이터가 없습니다.</p>
          )}
        </div>
      )}
    </div>
  );
}
