import React, { useState, useRef } from "react";
import { useNavigate } from "react-router-dom";
import styles from "./Header.module.css";

import logoImg from "../../assets/logo.svg";
import searchIcon from "../../assets/search.svg";
import menuIcon from "../../assets/menu.svg";

import MyPageButton from "../MyPageButton/MyPageButton.jsx";
import LoginButton from "../LoginButton/LoginButton.jsx";

import LoginModal from "../LoginModal/LoginModal.jsx";
import SignupModal from "../SignupModal/SignupModal.jsx";

import routeIconBlack from "../../assets/routeIconBlack.svg";
import PersonIcon from "../../assets/PersonIcon.svg";
import HeartIcon from "../../assets/HeartIcon.svg";
import OutIcon from "../../assets/OutIcon.svg";
import HomeIcon from "../../assets/HomeIcon.svg";

export default function Header() {
  const navigate = useNavigate();
  const isLoggedIn = false;

  const [showLogin, setShowLogin] = useState(false);
  const [showSignup, setShowSignup] = useState(false);
  const [openMenu, setOpenMenu] = useState(false);

  const menuRef = useRef(null);

  const goHome = () => {
    navigate("/");
  };

  const logout = () => {
    if (isLoggedIn) {
      console.log("로그아웃 실행!");
      // TODO: 실제 로그아웃 로직 추가 (예: localStorage.clear(), api.logout 등)
    }
    setOpenMenu(false);
    navigate("/");
  };

  const openSignupFromLogin = () => {
    setShowLogin(false);
    setShowSignup(true);
  };

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

          {/* 메뉴 아이콘 */}
          <img
            src={menuIcon}
            alt="menu"
            className={styles.icon}
            ref={menuRef}
            onClick={() => setOpenMenu(!openMenu)}
            style={{ cursor: "pointer" }}
          />

          {isLoggedIn ? (
            <MyPageButton />
          ) : (
            <LoginButton onClick={() => setShowLogin(true)} />
          )}
        </div>
      </header>

      {/* ⭐ 드롭다운 메뉴 */}
      {openMenu && (
        <div className={styles.dropdown} ref={menuRef}>
          <div className={styles.item} onClick={() => { navigate("/"); setOpenMenu(false); }}>
            <img src={HomeIcon} alt="home" className={styles.HomeIcon} />
            홈
          </div>
          <div className={styles.item} onClick={() => { navigate("/regionselect"); setOpenMenu(false); }}>
            <img src={routeIconBlack} alt="route" className={styles.routeIconBlack} />
            여행 계획
          </div>
          <div className={styles.item} onClick={() => { navigate("/mytrips"); setOpenMenu(false); }}>
            <img src={HeartIcon} alt="heart" className={styles.HeartIcon} />
            내 여행
          </div>
          <div className={styles.item} onClick={() => { navigate("/mypage"); setOpenMenu(false); }}>
            <img src={PersonIcon} alt="person" className={styles.PersonIcon} />
            마이페이지
          </div>

          <div className={styles.logout} onClick={logout}>
            <img src={OutIcon} alt="out" className={styles.OutIcon} />
            로그아웃
          </div>
        </div>
      )}

      {/* 로그인 모달 */}
      {showLogin && (
        <LoginModal
          onClose={() => setShowLogin(false)}
          onGoSignup={openSignupFromLogin}
        />
      )}

      {/* 회원가입 모달 */}
      {showSignup && (
        <SignupModal
          onClose={() => setShowSignup(false)}
          onGoLogin={openLoginFromSignup}
        />
      )}
    </>
  );
}
