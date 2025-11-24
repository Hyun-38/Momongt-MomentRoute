import React, { useState, useEffect } from 'react';
import Header from "../components/Header/Header.jsx";
import ToastPopup from '../components/ToastPopup';
import ConfirmModal from '../components/ConfirmModal';
import './Mypage.css';
import axios from "axios";

// ---------------------------------------
// 1. API ENDPOINT
// ---------------------------------------
const BASE_URL = "http://192.168.0.68:8082/api";

// ---------------------------------------
// 2. 실제 API 함수들
// ---------------------------------------

/** GET /member/me */
const fetchUserProfile = async () => {
  const token = localStorage.getItem("accessToken");
  const res = await axios.get(`${BASE_URL}/member/me`, {
    headers: { Authorization: `Bearer ${token}` }
  });
  return res.data;
};

/** PUT /member/me */
const updateUserProfile = async (formData) => {
  const token = localStorage.getItem("accessToken");

  const body = {
    email: formData.email,
    name: formData.name,
    phoneNumber: formData.phone,
  };

  const res = await axios.put(`${BASE_URL}/member/me`, body, {
    headers: { Authorization: `Bearer ${token}` }
  });

  return res.data;
};

/** DELETE /member/me */
const deleteMember = async () => {
  const token = localStorage.getItem("accessToken");

  const res = await axios.delete(`${BASE_URL}/member/me`, {
    headers: { Authorization: `Bearer ${token}` }
  });

  return res.data;
};


// ====================================================================
// MyPage Component
// ====================================================================

function MyPage() {
  const [toast, setToast] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const [formData, setFormData] = useState({
    name: '',
    email: '',
    phone: '',
  });

  const [userProfile, setUserProfile] = useState({
    name: '',
    email: '',
    joinDate: '',
  });

  const [isLoading, setIsLoading] = useState(true);
  const [isSaving, setIsSaving] = useState(false);


  // 날짜 포맷 (YYYY-MM-DD)
  const formatDate = (isoString) => {
    if (!isoString) return "";
    return isoString.split("T")[0];
  };


  // -------------------------------------------------
  // 🚀 최초 로딩 - GET /member/me 호출
  // -------------------------------------------------
  useEffect(() => {
    const loadData = async () => {
      try {
        const userData = await fetchUserProfile();
        console.log("🔥 GET /member/me 응답:", userData);

        setFormData({
          name: userData.name,
          email: userData.email || "",   // email 없을 시 빈 값 처리
          phone: userData.phoneNumber,
        });

        setUserProfile({
          name: userData.name,
          email: userData.email || "",
          joinDate: formatDate(userData.createdAt),
        });

      } catch (error) {
        console.error("프로필 로딩 실패:", error);
        setToast({ message: "정보를 불러오는 데 실패했습니다.", type: "info" });
      } finally {
        setIsLoading(false);
      }
    };

    loadData();
  }, []);


  // 입력 핸들러
  const handleFormChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };


  // -------------------------------------------------
  // 🚀 PUT /member/me — 프로필 저장
  // -------------------------------------------------
  const handleProfileUpdate = async (e) => {
    e.preventDefault();
    if (isSaving) return;

    setIsSaving(true);

    try {
      await updateUserProfile(formData);

      setUserProfile(prev => ({
        ...prev,
        name: formData.name,
        email: formData.email,
      }));

      setToast({ message: "프로필이 수정되었습니다!", type: "success" });

    } catch (error) {
      console.error("프로필 수정 실패:", error);
      setToast({ message: "프로필 수정 중 오류가 발생했습니다.", type: "info" });
    } finally {
      setIsSaving(false);
    }
  };


  // -------------------------------------------------
  // 🚀 DELETE /member/me — 계정 삭제
  // -------------------------------------------------
  const handleConfirmDelete = async () => {
    try {
      const res = await deleteMember();
      console.log("계정 삭제 성공:", res);

      localStorage.removeItem("isLoggedIn");
      localStorage.removeItem("accessToken");
      localStorage.removeItem("refreshToken");

      setToast({ message: "계정이 삭제되었습니다.", type: "success" });

      setTimeout(() => {
        window.location.href = "/";
      }, 700);

    } catch (error) {
      console.error("계정 삭제 실패:", error);
      setToast({ message: "계정 삭제 중 오류가 발생했습니다.", type: "info" });
    }
  };


  if (isLoading) {
    return (
      <main className="main-content">
        <div className="container mypage-container">
          <p>프로필 정보를 불러오는 중입니다...</p>
        </div>
      </main>
    );
  }


  // ====================================================================
  // 렌더링
  // ====================================================================
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

            {/* ----------------------------------- */}
            {/* 프로필 카드 */}
            {/* ----------------------------------- */}
            <section className="mypage-card">
              <header className="mypage-card__header mypage-card__header--with-divider">
                <h2 className="mypage-card__title">프로필 정보</h2>
                <p className="mypage-card__subtitle">프로필 기본 정보를 관리합니다</p>
              </header>

              <div className="mypage-card__body">

                {/* 아바타 */}
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

                {/* 폼 */}
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


            {/* ----------------------------------- */}
            {/* 비밀번호 변경 */}
            {/* ----------------------------------- */}
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
                  setToast({ message: "비밀번호 변경 기능은 준비 중입니다.", type: "info" });
                }}
              >
                비밀번호 변경하기
              </a>
            </section>


            {/* ----------------------------------- */}
            {/* 계정 삭제 */}
            {/* ----------------------------------- */}
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
                onClick={(e) => {
                  e.preventDefault();
                  setIsModalOpen(true);
                }}
              >
                계정 삭제
              </a>
            </section>
          </div>

          {/* 토스트 */}
          {toast && (
            <ToastPopup
              message={toast.message}
              subtitle={toast.subtitle}
              type={toast.type}
              onClose={() => setToast(null)}
            />
          )}

          {/* 모달 */}
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
