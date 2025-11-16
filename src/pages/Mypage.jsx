import React, { useState, useEffect } from 'react';
import Header from "../components/Header/Header.jsx";
import ToastPopup from '../components/ToastPopup';
import ConfirmModal from '../components/ConfirmModal';
import './Mypage.css';
import api from "../api/axiosInstance";

// --- 실제 API 함수들 ---

/** 1. GET /member/me - 사용자 프로필 불러오기 */
const fetchUserProfile = async () => {
  const res = await api.get("/member/me");
  return res.data;
};

/** 2. PUT /member/me - 사용자 정보 수정 */
const updateUserProfile = async (formData) => {
  const body = {
    email: formData.email,
    name: formData.name,
    phoneNumber: formData.phone
  };

  const res = await api.put("/member/me", body);
  return res.data;
};

/** ⭐ 3. DELETE /member/me - 계정 삭제 */
const deleteMember = async () => {
  const res = await api.delete("/member/me");
  return res.data;
};


// --- MyPage 컴포넌트 ---

function MyPage() {
  const [toast, setToast] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

  // 입력용 폼 상태
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    phone: '',
  });

  // 화면 표시용 정보
  const [userProfile, setUserProfile] = useState({
    name: '',
    email: '',
    joinDate: ''
  });

  const [isLoading, setIsLoading] = useState(true);
  const [isSaving, setIsSaving] = useState(false);


  // 🚀 페이지 로드 시 API에서 사용자 정보 GET
  useEffect(() => {
    const loadData = async () => {
      try {
        const userData = await fetchUserProfile();

        setFormData({
          name: userData.name,
          email: userData.email,
          phone: userData.phoneNumber
        });

        setUserProfile({
          name: userData.name,
          email: userData.email,
          joinDate: userData.createdAt // createdAt 사용
        });

      } catch (error) {
        console.error("프로필 로딩 실패:", error);
        showToast("정보를 불러오는 데 실패했습니다.", "info");
      } finally {
        setIsLoading(false);
      }
    };

    loadData();
  }, []);


  // 토스트 띄우기
  const showToast = (message, type = 'info', subtitle = null) => {
    setToast({ message, type, subtitle });
  };

  // 폼 입력 변경 핸들러
  const handleFormChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };


  // 🚀 PUT /member/me — 프로필 저장
  const handleProfileUpdate = async (e) => {
    e.preventDefault();
    if (isSaving) return;

    setIsSaving(true);

    try {
      await updateUserProfile(formData);

      setUserProfile({
        name: formData.name,
        email: formData.email,
        joinDate: userProfile.joinDate // 기존 가입일 유지
      });

      showToast("프로필이 수정되었습니다!", "success");

    } catch (error) {
      console.error("프로필 수정 실패:", error);
      showToast("프로필 수정 중 오류가 발생했습니다.", "info");
    } finally {
      setIsSaving(false);
    }
  };


  // 계정 삭제 버튼 클릭
  const handleDeleteClick = (e) => {
    e.preventDefault();
    setIsModalOpen(true);
  };

  // 🚀 DELETE /member/me — 계정 삭제
  const handleConfirmDelete = async () => {
    try {
      const res = await deleteMember();
      console.log("계정 삭제 성공:", res);

      // 로그인 정보 완전 제거
      localStorage.removeItem("isLoggedIn");
      localStorage.removeItem("accessToken");
      localStorage.removeItem("refreshToken");

      setIsModalOpen(false);
      showToast("계정이 삭제되었습니다.", "success");

      // 홈으로 이동
      setTimeout(() => {
        window.location.href = "/";
      }, 800);

    } catch (error) {
      console.error("계정 삭제 실패:", error);
      showToast("계정 삭제 중 오류가 발생했습니다.", "info");
    }
  };


  // 로딩 화면
  if (isLoading) {
    return (
      <main className="main-content">
        <div className="container mypage-container">
          <p>프로필 정보를 불러오는 중입니다...</p>
        </div>
      </main>
    );
  }


  // --- 렌더링 ---
  return (
    <>
      <Header />

      <main className="main-content">
        <div className="container mypage-container">

          <header className="mypage-header">
            <h1 className="mypage-header__title">마이페이지</h1>
            <p className="mypage-header__subtitle">
              개인정보를 관리하고 수정할 수 있습니다
            </p>
          </header>

          <div className="mypage-content">

            <section className="mypage-card">
              <header className="mypage-card__header mypage-card__header--with-divider">
                <h2 className="mypage-card__title">프로필 정보</h2>
                <p className="mypage-card__subtitle">프로필 기본 정보를 관리합니다</p>
              </header>

              <div className="mypage-card__body">

                {/* 아바타 표시 */}
                <div className="profile-section">
                  <div className="profile-section__avatar">
                    {userProfile.name ? userProfile.name.charAt(0) : '?' }
                  </div>
                  <div className="profile-section__info">
                    <div className="info__name">{userProfile.name}</div>
                    <div className="info__email">{userProfile.email}</div>
                    <div className="info__date">가입일: {userProfile.joinDate}</div>
                  </div>
                </div>


                {/* 수정 폼 */}
                <form className="profile-form">
                  <div className="form-group">
                    <label htmlFor="name" className="form-label">이름</label>
                    <input 
                      type="text"
                      id="name"
                      name="name"
                      value={formData.name}
                      onChange={handleFormChange}
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
                    <label htmlFor="phone" className="form-label">전화번호</label>
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
                  disabled={isSaving}
                >
                  {isSaving ? "저장 중..." : "프로필 수정"}
                </a>
              </div>
            </section>


            {/* 비밀번호 변경 */}
            <section className="mypage-card">
              <header className="mypage-card__header">
                <h2 className="mypage-card__title">비밀번호 변경</h2>
                <p className="mypage-card__subtitle">계정 비밀번호를 변경합니다</p>
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


            {/* 계정 삭제 */}
            <section className="mypage-card mypage-card--danger-outline">
              <header className="mypage-card__header">
                <h2 className="mypage-card__title mypage-card__title--danger">계정 삭제</h2>
                <p className="mypage-card__subtitle">
                  계정을 삭제하면 모든 데이터가 삭제됩니다.
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


          {/* 팝업 / 모달 */}
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
            message="이 작업은 되돌릴 수 없습니다."
            isDanger={true}
            onClose={() => setIsModalOpen(false)}
            onConfirm={handleConfirmDelete}
          />
        </div>
      </main>
    </>
  );
}

export default MyPage;
