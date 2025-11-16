import React, { useState, useEffect } from 'react'; // 1. useEffect 임포트
import ToastPopup from '../components/ToastPopup';
import ConfirmModal from '../components/ConfirmModal';
import './Mypage.css';

// --- 가짜(Dummy) API 함수 ---

/** 1. (가짜) 프로필 정보를 가져오는 API */
const fetchUserProfile = () => {
  console.log("API 요청: 사용자 프로필 로딩 중...");
  return new Promise(resolve => {
    setTimeout(() => {
      // 1초 후 가짜 사용자 데이터를 반환합니다.
      const userData = {
        name: '홍길동',
        email: 'hong@example.com',
        phone: '010-1234-5678',
        joinDate: '2025. 10. 16.'
      };
      console.log("API 응답:", userData);
      resolve(userData);
    }, 1000); // 1초 딜레이
  });
};

/** 2. (가짜) 프로필 정보를 수정하는 API */
const updateUserProfile = (formData) => {
  console.log("API 요청: 프로필 업데이트...", formData);
  return new Promise(resolve => {
    setTimeout(() => {
      // 1초 후 성공했다고 응답합니다.
      console.log("API 응답: 성공");
      resolve({ success: true, data: formData });
    }, 1000); // 1초 딜레이
  });
};

// --- MyPage 컴포넌트 ---

function MyPage() {
  const [toast, setToast] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

  // 1. 폼 입력을 위한 state (사용자가 수정 중인 내용)
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    phone: '',
  });

  // 2. 🌟 (신규) 화면 표시에 사용할 state (마지막으로 저장된 내용)
  const [userProfile, setUserProfile] = useState({
    name: '',
    email: '',
    joinDate: ''
  });

  const [isLoading, setIsLoading] = useState(true);
  const [isSaving, setIsSaving] = useState(false);

  // 5. 페이지가 처음 로드될 때 사용자 데이터를 불러옵니다. (useEffect 사용)
  useEffect(() => {
    const loadData = async () => {
      try {
        const userData = await fetchUserProfile();
        setFormData(userData); // 받아온 데이터로 폼 state 설정
      } catch (error) {
        console.error("프로필 로딩 실패:", error);
        showToast("정보를 불러오는 데 실패했습니다.", "info");
      } finally {
        setIsLoading(false); // 로딩 완료
      }
    };

    loadData();
  }, []); // 빈 배열[]: 컴포넌트가 처음 마운트될 때 1번만 실행

  // 토스트 띄우는 함수
  const showToast = (message, type = 'info', subtitle = null) => {
    setToast({ message, type, subtitle });
  };

  // 6. 폼 입력값이 변경될 때마다 formData state를 업데이트하는 함수
  const handleFormChange = (e) => {
    const { name, value } = e.target;
    setFormData(prevData => ({
      ...prevData,
      [name]: value,
    }));
  };
  
  // 7. 프로필 "수정" 버튼 클릭 시 실행할 함수 (API 호출)
  const handleProfileUpdate = async (e) => {
    e.preventDefault();
    if (isSaving) return; // 이미 저장 중이면 중복 실행 방지

    setIsSaving(true); // 저장 시작
    try {
      // 폼 데이터를 가짜 API로 전송
      await updateUserProfile(formData);
      setUserProfile(prevProfile => ({
        ...prevProfile, // joinDate 등 기존 값 유지
        name: formData.name,   // 폼에서 수정된 이름으로 덮어쓰기
        email: formData.email  // 폼에서 수정된 이메일로 덮어쓰기
      }));
      
      // 성공 시 토스트 팝업 띄우기
      showToast("프로필이 수정되었습니다!", "success");
    } catch (error) {
      console.error("프로필 수정 실패:", error);
      showToast("프로필 수정 중 오류가 발생했습니다.", "info");
    } finally {
      setIsSaving(false); // 저장 완료
    }
  };

  // --- (계정 삭제 관련 함수는 기존과 동일) ---
  const handleDeleteClick = (e) => {
    e.preventDefault();
    setIsModalOpen(true);
  };
  
  const handleConfirmDelete = async () => {
    console.log("계정 삭제 로직 실행...");
    // TODO: 여기에 실제 계정 삭제 API 호출
    setIsModalOpen(false);
    showToast("계정이 삭제되었습니다.", "success");
  };
  
  // 8. 로딩 중일 때 표시할 화면
  if (isLoading) {
    return (
      <main className="main-content">
        <div className="container mypage-container">
          <p>프로필 정보를 불러오는 중입니다...</p>
        </div>
      </main>
    );
  }

  // 9. 로딩 완료 후 실제 페이지 렌더링
  return (
    <main className="main-content">
      <div className="container mypage-container">
        
        <header className="mypage-header">
          <h1 className="mypage-header__title">
            마이페이지
          </h1>
          <p className="mypage-header__subtitle">
            개인정보를 관리하고 수정할 수 있습니다
          </p>
        </header>

        <div className="mypage-content">
          
          <section className="mypage-card">
            <header className="mypage-card__header mypage-card__header--with-divider">
              <h2 className="mypage-card__title">
                프로필 정보
              </h2>
              <p className="mypage-card__subtitle">
                프로필 기본 정보를 관리합니다
              </p>
            </header>

            <div className="mypage-card__body">
              {/* 아바타 섹션 (데이터 연동 불필요) */}
                <div className="profile-section">
                    <div className="profile-section__avatar">
                        {userProfile.name ? userProfile.name.charAt(0) : '?'}
                    </div>
                    <div className="profile-section__info">
                        <div className="info__name">{userProfile.name}</div>
                        <div className="info__email">{userProfile.email}</div>
                        <div className="info__date">가입일: {userProfile.joinDate}</div>
                    </div>
                </div>

              {/* --- 10. 수정된 폼 --- */}
              <form className="profile-form">
                <div className="form-group">
                  <label htmlFor="name" className="form-label">이름</label>
                  <input 
                    type="text" 
                    id="name" 
                    name="name" // input의 name 속성 (state 키와 일치)
                    value={formData.name} // state와 연결
                    onChange={handleFormChange} // onChange 핸들러 연결
                    className="form-input" 
                  />
                </div>
                <div className="form-group">
                  <label htmlFor="email" className="form-label">이메일</label>
                  <input 
                    type="email" 
                    id="email" 
                    name="email"
                    value={formData.email}
                    onChange={handleFormChange}
                    className="form-input" 
                  />
                </div>
                <div className="form-group">
                  <label htmlFor="phone" className="form-label">전화번호 &#40; "-" 하이픈 포함 &#41;</label>
                  <input 
                    type="tel" 
                    id="phone" 
                    name="phone"
                    value={formData.phone}
                    onChange={handleFormChange}
                    className="form-input" 
                  />
                </div>
              </form>
              
              <a 
                href="#" 
                className="btn mypage-card__button mypage-card__button--primary"
                onClick={handleProfileUpdate}
                disabled={isSaving} // 11. 저장 중일 때 버튼 비활성화
              >
                {isSaving ? '저장 중...' : '프로필 수정'}
              </a>
            </div>
          </section>

          {/* === (카드 2, 3은 기존과 동일) === */}
          <section className="mypage-card">
            <header className="mypage-card__header">
              <h2 className="mypage-card__title">
                비밀번호 변경
              </h2>
              <p className="mypage-card__subtitle">
                계정의 비밀번호를 변경합니다
              </p>
            </header>
            
            <a 
              href="#" 
              className="btn mypage-card__button mypage-card__button--secondary"
              onClick={(e) => {
                e.preventDefault();
                showToast("비밀번호 변경 기능은 준비 중입니다.", "info");
              }}
            >
              비밀번호 변경하기
            </a>
          </section>

          <section className="mypage-card mypage-card--danger-outline">
            <header className="mypage-card__header">
              <h2 className="mypage-card__title mypage-card__title--danger">
                계정 삭제
              </h2>
              <p className="mypage-card__subtitle">
                계정을 삭제하면 모든 데이터가 영구적으로 삭제됩니다
              </p>
            </header>
            
            <a 
              href="#" 
              className="btn mypage-card__button mypage-card__button--danger"
              onClick={handleDeleteClick}
            >
              계정 삭제
            </a>
          </section>
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
            isOpen={isModalOpen}
            title="정말 계정을 삭제하시겠습니까?"
            message="이 작업은 되돌릴 수 없습니다. 계정과 모든 여행 계획이 영구적으로 삭제됩니다."
            isDanger={true}
            onClose={() => setIsModalOpen(false)}
            onConfirm={handleConfirmDelete}
        />
    </div>
    </main>
  );
}

export default MyPage;