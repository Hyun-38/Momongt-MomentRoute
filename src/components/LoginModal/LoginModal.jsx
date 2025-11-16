import React, { useState } from "react";
import styles from "./LoginModal.module.css";
import closeIcon from "../../assets/close.svg";
import markerIcon from "../../assets/LoginPageIcon.svg";
import axios from "axios";

export default function LoginModal({ onClose, onGoSignup, onLoginSuccess }) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const canLogin = email.trim() !== "" && password.trim() !== "";

  const handleLogin = async () => {
    if (!canLogin) return;

    try {
      const requestBody = {
        email: email,
        password: password
      };

      console.log("📌 로그인 요청:", requestBody);

      const res = await axios.post(
        "http://13.124.41.43/api/member/login",
        requestBody,
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      console.log("🎉 로그인 성공:", res.data);
      alert("로그인 성공!");

      // ⭐ 로그인 성공 처리 (매우 중요)
      localStorage.setItem("isLoggedIn", "true");

      // ⭐ 백엔드에서 token, refreshToken 을 내려준다고 가정
      if (res.data.refreshToken) {
        localStorage.setItem("refreshToken", res.data.refreshToken);
      }
      if (res.data.accessToken) {
        localStorage.setItem("accessToken", res.data.accessToken);
      }

      onLoginSuccess();

      onClose();  // 모달 닫기

    } catch (err) {
      console.error("❌ 로그인 실패:", err);

      if (err.response) {
        alert("로그인 실패: " + JSON.stringify(err.response.data));
      } else {
        alert("로그인 요청 실패!");
      }
    }
  };

  return (
    <div className={styles.overlay}>
      <div className={styles.modal}>

        <div className={styles.header}>
          <img
            src={closeIcon}
            className={styles.close}
            alt="close"
            onClick={onClose}
          />
        </div>

        <div className={styles.iconWrap}>
          <img src={markerIcon} className={styles.markerIcon} alt="" />
        </div>

        <h2 className={styles.title}>경기도 축제 가이드</h2>
        <p className={styles.sub}>경기도의 모든 축제와 이벤트를 한눈에</p>

        <label className={styles.label}>이메일</label>
        <input
          className={styles.input}
          type="email"
          placeholder="example@email.com"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />

        <label className={styles.label}>비밀번호</label>
        <input
          className={styles.input}
          type="password"
          placeholder="••••••••"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />

        <button
          className={`${styles.loginBtn} ${canLogin ? styles.enabled : ""}`}
          disabled={!canLogin}
          onClick={handleLogin}
        >
          로그인
        </button>

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
