import React, { useState } from "react";
import styles from "./LoginModal.module.css";
import closeIcon from "../../assets/close.svg";
import markerIcon from "../../assets/LoginPageIcon.svg"; // 타이틀 아이콘(원하는 걸로 바꿔도 됨)

export default function LoginModal({ onClose, onGoSignup }) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const canLogin = email.trim() !== "" && password.trim() !== "";

  return (
    <div className={styles.overlay}>
      <div className={styles.modal}>
        
        {/* 헤더 */}
        <div className={styles.header}>
          <img
            src={closeIcon}
            className={styles.close}
            alt="close"
            onClick={onClose}
          />
        </div>

        {/* 아이콘 */}
        <div className={styles.iconWrap}>
          <img src={markerIcon} className={styles.markerIcon} alt="" />
        </div>

        {/* 타이틀 */}
        <h2 className={styles.title}>경기도 축제 가이드</h2>
        <p className={styles.sub}>
          경기도의 모든 축제와 이벤트를 한눈에
        </p>

        {/* 이메일 */}
        <label className={styles.label}>이메일</label>
        <input
          className={styles.input}
          type="email"
          placeholder="example@email.com"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />

        {/* 비밀번호 */}
        <label className={styles.label}>비밀번호</label>
        <input
          className={styles.input}
          type="password"
          placeholder="••••••••"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />

        {/* 로그인 버튼 */}
        <button
          className={`${styles.loginBtn} ${canLogin ? styles.enabled : ""}`}
          disabled={!canLogin}
        >
          로그인
        </button>

        {/* 회원가입 링크 */}
        <div className={styles.signupBox}>
          계정이 없으신가요?{" "}
          <span className={styles.signupLink} onClick={onGoSignup}>
            회원가입
          </span>
        </div>
      </div>
    </div>
  );
}
