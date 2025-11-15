import React from "react";
import { useNavigate } from "react-router-dom";
import styles from "./Home.module.css";
import Header from "../components/Header/Header.jsx";
import locationIconSquare from "../assets/locationIconSquare.svg";
import CalendarSquare from "../assets/CalendarSquare.svg";
import Pointer from "../assets/Pointer.svg";
import PointerOrange from "../assets/PointerOrange.svg";
import Recommend from "../assets/Recommend.svg";
import suwonImg from "../assets/suwon.svg";
import koreaglobalImg from "../assets/koreaglobal.svg";
import goyangImg from "../assets/goyang.svg";
import seongnamImg from "../assets/seongnam.svg";
import anyangImg from "../assets/anyang.svg";
import gyeonggiunivImg from "../assets/gyeonggiuniv.svg";

export default function Home() {
    const navigate = useNavigate();
    
  return (
    <div className={styles.page}>
      <Header />

      {/* 히어로 영역 */}
      <section className={styles.heroSection}>
        <div className={styles.badge}>경기도 축제 & 이벤트 가이드</div>

        <h1 className={styles.title}>
          경기도 여행, <br />
          <span className={styles.highlight}>AI가 완벽하게 계획해드립니다</span>
        </h1>

        <p className={styles.sub}>
          출발지와 목적지만 선택하면 AI가 경로상의 축제, 맛집, 관광지를 추천해드립니다.
          경기도의 숨은 매력을 발견해보세요.
        </p>

        <button className={styles.startBtn} onClick={() => navigate("/regionselect")}>여행 경로 계획하기</button>
      </section>

      {/* 3개 특징 */}
      <section className={styles.featureSection}>
        <div className={styles.card}>
          <img src={locationIconSquare} alt="icon" className={styles.locationIconSquare} />
          <h3 className={styles.cardTitle}>스마트 경로 계획</h3>
          <p className={styles.cardSub}>
            출발지와 목적지를 선택하면 최적 경로와 경유지를 추천해드립니다.
          </p>
        </div>

        <div className={styles.card}>
          <img src={CalendarSquare} alt="icon" className={styles.CalendarSquare} />
          <h3 className={styles.cardTitle}>실시간 축제 정보</h3>
          <p className={styles.cardSub}>
            여행 경로에 있는 도시의 축제와 이벤트를 실시간 제공받으세요.
          </p>
        </div>

        <div className={styles.card}>
          <img src={Pointer} alt="icon" className={styles.Pointer} />
          <h3 className={styles.cardTitle}>맛집 & 관광지 추천</h3>
          <p className={styles.cardSub}>
            AI가 각 지역별 인기 맛집과 관광지를 자동으로 추천합니다.
          </p>
        </div>
      </section>

      {/* 진행 중인 축제 영역 */}
      <section className={styles.festivalSection}>
        <div className={styles.festivalHeader}>
          <span className={styles.badgeOrange}>
            <img src={PointerOrange} alt="icon" className={styles.PointerOrange} />
            지금 경기도에서 진행 중인 축제
            <img src={Recommend} alt="icon" className={styles.Recommend} />
            </span>
        </div>

        <p className={styles.festivalSub}>
          경기도 전역에서 열리는 다양한 축제와 이벤트를 놓치지 마세요!
        </p>

        <div className={styles.festivalGrid}>
          <div
            className={styles.festivalCard}
            style={{ backgroundImage: `url(${suwonImg})` }}
          >
            <div className={styles.festivalInfo}>
                <span className={styles.tag}>축제</span>
                <h3>수원 화성문화제 2025</h3>
                <p>2025-10-05 ~ 2025-10-12</p>
            </div>
          </div>

          <div
            className={styles.festivalCard}
            style={{ backgroundImage: `url(${koreaglobalImg})` }}
          >
            <div className={styles.festivalInfo}>
              <span className={styles.tag}>대학축제</span>
              <h3>한국외대 글로벌캠퍼스 축제</h3>
              <p>2025-10-08 ~ 2025-10-10</p>
            </div>
          </div>

          <div
            className={styles.festivalCard}
            style={{ backgroundImage: `url(${goyangImg})` }}
          >
            <div className={styles.festivalInfo}>
              <span className={styles.tag}>공연</span>
              <h3>고양 호수예술제</h3>
              <p>2025-10-01 ~ 2025-10-15</p>
            </div>
          </div>

          <div
            className={styles.festivalCard}
            style={{ backgroundImage: `url(${seongnamImg})` }}
          >
            <div className={styles.festivalInfo}>
              <span className={styles.tag}>전시</span>
              <h3>성남 현대미술전</h3>
              <p>2025-09-20 ~ 2025-11-30</p>
            </div>
          </div>

          <div
            className={styles.festivalCard}
            style={{ backgroundImage: `url(${anyangImg})` }}
          >
            <div className={styles.festivalInfo}>
              <span className={styles.tag}>축제</span>
              <h3>안양 예술공원 가을축제</h3>
              <p>2025-10-10 ~ 2025-10-20</p>
            </div>
          </div>

          <div
            className={styles.festivalCard}
            style={{ backgroundImage: `url(${gyeonggiunivImg})` }}
          >
            <div className={styles.festivalInfo}>
              <span className={styles.tag}>전시</span>
              <h3>경기대학교 졸업전시회</h3>
              <p>2025-10-15 ~ 2025-10-22</p>
            </div>

          </div>
        </div>
      </section>
      {/* CTA 영역 */}
      <section className={styles.ctaSection}>
        <h2>지금 바로 여행 계획을 시작하세요</h2>
        <p>
          로그인 없이도 여행 계획을 확인할 수 있습니다. 일정을 저장하려면
          로그인이 필요합니다.
        </p>
        <button className={styles.ctaButton} onClick={() => navigate("/regionselect")}>여행 계획 시작하기</button>
      </section>
    </div>
  );
}

