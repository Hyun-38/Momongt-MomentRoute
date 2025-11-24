import React, { useState, useEffect } from 'react';
import axios from "axios";
import { Link } from 'react-router-dom';
import Header from "../components/Header/Header.jsx";

import ConfirmModal from '../components/ConfirmModal';
import ToastPopup from '../components/ToastPopup';

import './MyTrips.css';

// --- 아이콘 ---
const IconCalendar = () => (
  <svg width="12" height="12" viewBox="0 0 12 12" fill="none">
    <path d="M9.49992 2H2.49992C2.22378 2 1.99992 2.22386 1.99992 2.5V9.5C1.99992 9.77614 2.22378 10 2.49992 10H9.49992C9.77606 10 9.99992 9.77614 9.99992 9.5V2.5C9.99992 2.22386 9.77606 2 9.49992 2Z" stroke="#475569" strokeWidth="1" />
    <path d="M8 1V3" stroke="#475569" strokeWidth="1" />
    <path d="M4 1V3" stroke="#475569" strokeWidth="1" />
    <path d="M2 4.5H10" stroke="#475569" strokeWidth="1" />
  </svg>
);
const IconTrash = () => (
  <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
    <path d="M2.66669 4H13.3334" stroke="#475569" strokeWidth="1.3" />
    <path d="M4 4V2.66667C4 2.31304 4.14048 1.9739 4.39052 1.72386C4.64057 1.47381 4.97971 1.33333 5.33335 1.33333H10.6667C11.0203 1.33333 11.3595 1.47381 11.6095 1.72386C11.8595 1.9739 12 2.31304 12 2.66667V4" stroke="#475569" strokeWidth="1.3" />
    <path d="M12.6667 4V12.6667C12.6667 13.0203 12.5262 13.3594 12.2762 13.6095C12.0261 13.8595 11.687 14 11.3334 14H4.66669C4.31306 14 3.97392 13.8595 3.72387 13.6095C3.47383 13.3595 3.33335 13.0203 3.33335 12.6667V4" stroke="#475569" strokeWidth="1.3" />
    <path d="M6.66669 7.33333V10.6667" stroke="#475569" strokeWidth="1.3" />
    <path d="M9.33331 7.33333V10.6667" stroke="#475569" strokeWidth="1.3" />
  </svg>
);
const IconLocation = () => (
  <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
    <g transform="scale(0.6666)">
      <path d="M20 10C20 14.993 14.461 20.193 12.601 21.799C12.4277 21.9293 12.2168 21.9998 12 21.9998C11.7832 21.9998 11.5723 21.9293 11.399 21.799C9.539 20.193 4 14.993 4 10C4 7.87827 4.84285 5.84344 6.34315 4.34315C7.84344 2.84285 9.87827 2 12 2C14.1217 2 16.1566 2.84285 17.6569 4.34315C19.1571 5.84344 20 7.87827 20 10Z" stroke="#0EA5E9" strokeWidth="2" />
      <circle cx="12" cy="10" r="3" stroke="#0EA5E9" strokeWidth="2" />
    </g>
  </svg>
);

// ⭐ 날짜 포맷
const formatDate = (iso) => {
  if (!iso) return "";
  return iso.split("T")[0].replace(/-/g, ". ") + ".";
};

// ⭐ route 가공 함수
const transformApiData = (apiTrips) => {
  return apiTrips.map((trip) => {
    const processedRoute = trip.cities.map((city, index) => {
      if (trip.cities.length === 1) {
        return { city, type: "stop" };
      }
      if (index === 0 || index === trip.cities.length - 1) {
        return { city, type: "stop" };
      }
      return { city, type: "via" };
    });

    return {
      id: trip.id,
      title: trip.tripName,
      date: formatDate(trip.createdAt),
      route: processedRoute,
      eventCount: trip.eventCount ?? 0,
    };
  });
};

// --- MyTrips 컴포넌트 ---
function MyTrips() {
  const [trips, setTrips] = useState([]);
  const [toast, setToast] = useState(null);
  const [modal, setModal] = useState({ isOpen: false, tripId: null, tripName: "" });
  const [isLoading, setIsLoading] = useState(true);

  // ⭐ 실제 API 호출하여 여행 목록 가져오기
  useEffect(() => {
    const loadTrips = async () => {
      try {
        const res = await axios.get("http://192.168.0.68:8082/api/travel/list");
        console.log("🔥 GET /travel/list 응답:", res.data);

        const transformed = transformApiData(res.data);
        setTrips(transformed);
      } catch (err) {
        console.error("여행 목록 불러오기 실패:", err);
        showToast("여행 목록을 불러오지 못했습니다.", "info");
      } finally {
        setIsLoading(false);
      }
    };

    loadTrips();
  }, []);

  const showToast = (message, type = "info", subtitle = null) => {
    setToast({ message, type, subtitle });
  };

  const handleDeleteClick = (trip) => {
    setModal({ isOpen: true, tripId: trip.id, tripName: trip.title });
  };

  const handleCloseModal = () => {
    setModal({ isOpen: false, tripId: null, tripName: "" });
  };

  const handleConfirmDelete = () => {
    setTrips((current) => current.filter((t) => t.id !== modal.tripId));
    handleCloseModal();
    showToast("여행이 삭제되었습니다.", "success");
  };

  return (
    <>
      <Header />

      <main className="main-content">
        <div className="container mytrips-container">
          <header className="mytrips-header">
            <h1>내 여행</h1>
            <p>저장된 여행 계획을 확인하고 관리하세요</p>
          </header>

          <div className="trip-grid">
            {isLoading ? (
              <p>여행 목록을 불러오는 중입니다...</p>
            ) : trips.length > 0 ? (
              trips.map((trip) => (
                <div className="trip-card" key={trip.id}>
                  <header className="trip-card-header">
                    <div className="trip-card-info">
                      <h3>{trip.title}</h3>
                      <span>
                        <IconCalendar /> {trip.date}
                      </span>
                    </div>
                    <button
                      className="trash-btn"
                      onClick={() => handleDeleteClick(trip)}
                    >
                      <IconTrash />
                    </button>
                  </header>

                  <div className="trip-card-body">
                    <h4 className="section-title">
                      <IconLocation /> 여행 경로
                    </h4>
                    <div className="trip-route-tags">
                      {trip.route.map((stop, idx) => (
                        <React.Fragment key={idx}>
                          <span
                            className={`tag ${
                              stop.type === "via" ? "tag-via" : "tag-stop"
                            }`}
                          >
                            {stop.city}
                          </span>
                          {idx < trip.route.length - 1 && (
                            <span className="arrow">→</span>
                          )}
                        </React.Fragment>
                      ))}
                    </div>

                    <div className="trip-card-section">
                      <h4 className="section-title">포함된 이벤트</h4>
                      <p className="event-count">{trip.eventCount}개의 축제/이벤트</p>
                    </div>
                  </div>

                  <footer className="trip-card-footer">
                    <a
                      href="#"
                      className="btn trip-card-button"
                      onClick={(e) => {
                        e.preventDefault();
                        showToast("상세보기 기능은 준비 중입니다.", "info");
                      }}
                    >
                      상세보기
                    </a>
                  </footer>
                </div>
              ))
            ) : (
              <p>저장된 여행이 없습니다.</p>
            )}
          </div>
        </div>

        {toast && (
          <ToastPopup
            message={toast.message}
            subtitle={toast.subtitle}
            type={toast.type}
            onClose={() => setToast(null)}
          />
        )}
        <ConfirmModal
          isOpen={modal.isOpen}
          title="여행을 삭제하시겠습니까?"
          message={`"${modal.tripName}" 여행이 삭제됩니다.`}
          isDanger={true}
          onClose={handleCloseModal}
          onConfirm={handleConfirmDelete}
        />
      </main>
    </>
  );
}

export default MyTrips;
