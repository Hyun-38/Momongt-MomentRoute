import React, { useState, useEffect } from 'react'; // 1. useEffect 임포트
import { Link } from 'react-router-dom';

import ConfirmModal from '../components/ConfirmModal';
import ToastPopup from '../components/ToastPopup';

import './MyTrips.css'; // MyTrips 전용 CSS

// --- 아이콘 (기존과 동일) ---
const IconCalendar = () => (
  <svg width="12" height="12" viewBox="0 0 12 12" fill="none" xmlns="http://www.w3.org/2000/svg">
    <path d="M9.49992 2.00004H2.49992C2.22378 2.00004 1.99992 2.2239 1.99992 2.50004V9.50004C1.99992 9.77618 2.22378 10.0000 2.49992 10.0000H9.49992C9.77606 10.0000 9.99992 9.77618 9.99992 9.50004V2.50004C9.99992 2.2239 9.77606 2.00004 9.49992 2.00004Z" stroke="#475569" strokeWidth="1" strokeLinecap="round" strokeLinejoin="round"/>
    <path d="M8 1V3" stroke="#475569" strokeWidth="1" strokeLinecap="round" strokeLinejoin="round"/>
    <path d="M4 1V3" stroke="#475569" strokeWidth="1" strokeLinecap="round" strokeLinejoin="round"/>
    <path d="M2 4.5H10" stroke="#475569" strokeWidth="1" strokeLinecap="round" strokeLinejoin="round"/>
  </svg>
);
const IconTrash = () => (
  <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
    <path d="M2.66669 4.00004H13.3334" stroke="#475569" strokeWidth="1.33333" strokeLinecap="round" strokeLinejoin="round"/>
    <path d="M4 4.00004V2.66671C4 2.31309 4.14048 1.97395 4.39052 1.72391C4.64057 1.47386 4.97971 1.33337 5.33335 1.33337H10.6667C11.0203 1.33337 11.3595 1.47386 11.6095 1.72391C11.8595 1.97395 12 2.31309 12 2.66671V4.00004M12.6667 4.00004V12.6667C12.6667 13.0203 12.5262 13.3595 12.2762 13.6095C12.0261 13.8596 11.687 14.0000 11.3334 14.0000H4.66669C4.31306 14.0000 3.97392 13.8596 3.72387 13.6095C3.47383 13.3595 3.33335 13.0203 3.33335 12.6667V4.00004H12.6667Z" stroke="#475569" strokeWidth="1.33333" strokeLinecap="round" strokeLinejoin="round"/>
    <path d="M6.66669 7.33337V10.6667" stroke="#475569" strokeWidth="1.33333" strokeLinecap="round" strokeLinejoin="round"/>
    <path d="M9.33331 7.33337V10.6667" stroke="#475569" strokeWidth="1.33333" strokeLinecap="round" strokeLinejoin="round"/>
  </svg>
);
const IconLocation = () => (
    <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
        <g transform="scale(0.6666) translate(0 0)">
            <path d="M20 10C20 14.993 14.461 20.193 12.601 21.799C12.4277 21.9293 12.2168 21.9998 12 21.9998C11.7832 21.9998 11.5723 21.9293 11.399 21.799C9.539 20.193 4 14.993 4 10C4 7.87827 4.84285 5.84344 6.34315 4.34315C7.84344 2.84285 9.87827 2 12 2C14.1217 2 16.1566 2.84285 17.6569 4.34315C19.1571 5.84344 20 7.87827 20 10Z" stroke="#0EA5E9" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
            <path d="M12 13C13.6569 13 15 11.6569 15 10C15 8.34315 13.6569 7 12 7C10.3431 7 9 8.34315 9 10C9 11.6569 10.3431 13 12 13Z" stroke="#0EA5E9" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
        </g>
    </svg>
);
// --- 아이콘 끝 ---

// --- 🌟 2. 가짜(Dummy) API 함수 ---
/** (가짜) 저장된 여행 목록을 가져오는 API */
const fetchMyTrips = () => {
  console.log("API 요청: 내 여행 목록 로딩 중...");
  return new Promise(resolve => {
    setTimeout(() => {
      // 1초 후 가짜 데이터를 반환합니다.
      // route가 'type' 없이 도시 이름 배열로만 되어 있습니다.
      const apiResponse = [
        {
          id: 1,
          title: '아자아자화이팅',
          date: '2025. 10. 16.',
          route: ['성남', '화성', '시흥', '용인'], // 4개 경로 (시작, 경유, 경유, 끝)
          eventCount: 2,
        },
        {
          id: 2,
          title: '한개만 더 넣어놓을게',
          date: '2025. 10. 16.',
          route: ['고양', '안산'], // 2개 경로 (시작, 끝)
          eventCount: 1,
        },
        {
          id: 3,
          title: '당일치기',
          date: '2025. 10. 17.',
          route: ['수원'], // 1개 경로 (시작=끝)
          eventCount: 1,
        }
      ];
      console.log("API 응답:", apiResponse);
      resolve(apiResponse);
    }, 1000); // 1초 딜레이
  });
};

/** 🌟 3. API 데이터를 UI용 데이터로 변환하는 함수 */
const transformApiData = (apiTrips) => {
  return apiTrips.map(trip => {
    // route 배열(string[])을 객체 배열({ city, type }[])로 변환
    const processedRoute = trip.route.map((city, index) => {
      // 1. 첫 번째 항목이거나 (index === 0)
      // 2. 마지막 항목이거나 (index === trip.route.length - 1)
      // 3. 경로가 1개뿐인 경우 (trip.route.length === 1)
      //    -> 모두 'stop' (출발지/목적지)
      if (index === 0 || index === trip.route.length - 1) {
        return { city: city, type: 'stop' };
      }
      
      // 그 외 중간 항목은 모두 'via' (경유지)
      return { city: city, type: 'via' };
    });

    // 변환된 route를 포함하여 새로운 trip 객체 반환
    return {
      ...trip, // id, title, date, eventCount는 그대로 복사
      route: processedRoute, // route만 변환된 데이터로 덮어쓰기
    };
  });
};


// --- MyTrips 컴포넌트 ---

function MyTrips() {
  const [trips, setTrips] = useState([]); // 4. 초기값을 빈 배열로
  const [toast, setToast] = useState(null);
  const [modal, setModal] = useState({ isOpen: false, tripId: null, tripName: '' });
  const [isLoading, setIsLoading] = useState(true); // 5. 로딩 상태 추가

  // 6. 페이지가 처음 로드될 때 API 데이터를 불러옵니다.
  useEffect(() => {
    const loadTrips = async () => {
      setIsLoading(true);
      try {
        const apiData = await fetchMyTrips();
        const transformedData = transformApiData(apiData); // 7. 데이터 변환!
        setTrips(transformedData); // 변환된 데이터를 state에 저장
      } catch (error) {
        console.error("여행 목록 로딩 실패:", error);
        showToast("여행 목록을 불러오는 데 실패했습니다.", "info");
      } finally {
        setIsLoading(false);
      }
    };

    loadTrips();
  }, []); // 빈 배열[]: 컴포넌트가 처음 마운트될 때 1번만 실행

  // (토스트 및 모달 관련 함수는 기존과 동일)
  const showToast = (message, type = 'info', subtitle = null) => {
    setToast({ message, type, subtitle });
  };
  const handleDeleteClick = (trip) => {
    setModal({ isOpen: true, tripId: trip.id, tripName: trip.title });
  };
  const handleCloseModal = () => {
    setModal({ isOpen: false, tripId: null, tripName: '' });
  };
  const handleConfirmDelete = () => {
    // TODO: 추후에 여기도 API로 삭제 요청
    setTrips(currentTrips =>
      currentTrips.filter(trip => trip.id !== modal.tripId)
    );
    handleCloseModal();
    showToast('여행이 삭제되었습니다.', 'success');
  };

  return (
    <main className="main-content">
      <div className="container mytrips-container">
        
        <header className="mytrips-header">
          <h1>내 여행</h1>
          <p>저장된 여행 계획을 확인하고 관리하세요</p>
        </header>

        <div className="trip-grid">
          {/* 8. 로딩 상태에 따른 UI 분기 처리 */}
          {isLoading ? (
            <p>여행 목록을 불러오는 중입니다...</p>
          ) : trips.length > 0 ? (
            trips.map(trip => (
              <div className="trip-card" key={trip.id}>
                
                <header className="trip-card-header">
                  <div className="trip-card-info">
                    <h3>{trip.title}</h3>
                    <span>
                      <IconCalendar />
                      {trip.date}
                    </span>
                  </div>
                  <button 
                    className="trash-btn" 
                    aria-label="여행 삭제"
                    onClick={() => handleDeleteClick(trip)}
                  >
                    <IconTrash />
                  </button>
                </header>

                <div className="trip-card-body">
                  <div className="trip-card-section">
                    <h4 className="section-title">
                      <IconLocation />
                      여행 경로
                    </h4>
                    {/* 9. 렌더링 로직은 수정할 필요가 없습니다.
                         왜냐하면 transformApiData 함수가
                         JSX가 기대하는 { city, type } 구조로
                         미리 데이터를 만들어줬기 때문입니다.
                    */}
                    <div className="trip-route-tags">
                      {trip.route.map((stop, index) => (
                        <React.Fragment key={index}>
                          <span className={`tag ${stop.type === 'via' ? 'tag-via' : 'tag-stop'}`}>
                            {stop.city}
                          </span>
                          {index < trip.route.length - 1 && (
                            <span className="arrow">→</span>
                          )}
                        </React.Fragment>
                      ))}
                    </div>
                  </div>

                  <div className="trip-card-section">
                    <h4 className="section-title">포함된 이벤트</h4>
                    <p className="event-count">{trip.eventCount}개의 축제/이벤트</p>
                  </div>
                </div>
                
                <footer className="trip-card-footer">
                    <a href="#" 
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
            // 로딩이 끝났는데 trips가 0개일 때
            <p>저장된 여행이 없습니다.</p>
          )}
        </div>
      </div>

      {/* --- 팝업/모달 렌더링 (기존과 동일) --- */}
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
        message={`이 작업은 되돌릴 수 없습니다. "${modal.tripName}" 여행이 영구적으로 삭제됩니다.`}
        isDanger={true}
        onClose={handleCloseModal}
        onConfirm={handleConfirmDelete}
      />
    </main>
  );
}

export default MyTrips;