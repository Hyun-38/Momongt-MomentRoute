import React, { useState } from "react";
import styles from "./SignupModal.module.css";
import closeIcon from "../../assets/close.svg";
import locationBig from "../../assets/LoginPageIcon.svg";

export default function SignupModal({ onClose, onGoLogin }) {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [pw, setPw] = useState("");
  const [pwCheck, setPwCheck] = useState("");

  const canSubmit = name && email && pw && pwCheck && pw === pwCheck;

  return (
    <div className={styles.overlay}>
      <div className={styles.modal}>

        {/* 헤더 */}
        <div className={styles.header}>
          <img src={locationBig} alt="icon" className={styles.icon} />
          <img src={closeIcon} className={styles.close} alt="close" onClick={onClose}/>
        </div>

        <h2 className={styles.title}>회원가입</h2>
        <p className={styles.desc}>경기도 축제를 함께 즐겨보세요</p>

        {/* 이름 */}
        <label className={styles.label}>이름</label>
        <input
          className={styles.input}
          placeholder="홍길동"
          value={name}
          onChange={(e) => setName(e.target.value)}
        />

        {/* 이메일 */}
        <label className={styles.label}>이메일</label>
        <input
          className={styles.input}
          placeholder="example@email.com"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />

        {/* 비밀번호 */}
        <label className={styles.label}>비밀번호</label>
        <input
          type="password"
          className={styles.input}
          placeholder="••••••••"
          value={pw}
          onChange={(e) => setPw(e.target.value)}
        />

        {/* 비밀번호 확인 */}
        <label className={styles.label}>비밀번호 확인</label>
        <input
          type="password"
          className={styles.input}
          placeholder="••••••••"
          value={pwCheck}
          onChange={(e) => setPwCheck(e.target.value)}
        />

        {/* 가입하기 버튼 */}
        <button
          className={`${styles.signupBtn} ${canSubmit ? styles.active : ""}`}
          disabled={!canSubmit}
        >
          가입하기
        </button>

        <div className={styles.bottom}>
          이미 계정이 있으신가요?{" "}
          <span className={styles.loginLink} onClick={onGoLogin}>
            로그인
          </span>
        </div>
      </div>
    </div>
  );
}
