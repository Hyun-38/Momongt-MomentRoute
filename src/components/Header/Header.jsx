import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import styles from "./Header.module.css";

import logoImg from "../../assets/logo.svg";
import searchIcon from "../../assets/search.svg";
import menuIcon from "../../assets/menu.svg";

import MyPageButton from "../MyPageButton/MyPageButton.jsx";
import LoginButton from "../LoginButton/LoginButton.jsx";

import LoginModal from "../LoginModal/LoginModal.jsx";
import SignupModal from "../SignupModal/SignupModal.jsx";

export default function Header() {
  const navigate = useNavigate();
  const isLoggedIn = false;

  // ⭐ 모달 상태 2개 추가
  const [showLogin, setShowLogin] = useState(false);
  const [showSignup, setShowSignup] = useState(false);

  const goHome = () => {
    navigate("/");
  };

  // ⭐ 로그인 → 회원가입 이동
  const openSignupFromLogin = () => {
    setShowLogin(false);
    setShowSignup(true);
  };

  // ⭐ 회원가입 → 로그인 이동
  const openLoginFromSignup = () => {
    setShowSignup(false);
    setShowLogin(true);
  };

  return (
    <>
      <header className={styles.header}>
        <div className={styles.left} onClick={goHome} style={{ cursor: "pointer" }}>
          <img src={logoImg} alt="logo" className={styles.logo} />
          <span className={styles.title}>모몽트</span>
        </div>

        <div className={styles.right}>
          <img src={searchIcon} alt="search" className={styles.icon} />
          <img src={menuIcon} alt="menu" className={styles.icon} />

          {isLoggedIn ? (
            <MyPageButton />
          ) : (
            <LoginButton onClick={() => setShowLogin(true)} />
          )}
        </div>
      </header>

      {/* ⭐ 로그인 모달 */}
      {showLogin && (
        <LoginModal
          onClose={() => setShowLogin(false)}
          onGoSignup={openSignupFromLogin}   // 🔥 로그인 → 회원가입
        />
      )}

      {/* ⭐ 회원가입 모달 */}
      {showSignup && (
        <SignupModal
          onClose={() => setShowSignup(false)}
          onGoLogin={openLoginFromSignup}   // 🔥 회원가입 → 로그인
        />
      )}
    </>
  );
}
