import React, { useState } from "react";
import styles from "./SignupModal.module.css";
import closeIcon from "../../assets/close.svg";
import locationBig from "../../assets/LoginPageIcon.svg";
import axios from "axios";   // ⭐ axios 추가

export default function SignupModal({ onClose, onGoLogin }) {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [pw, setPw] = useState("");
  const [pwCheck, setPwCheck] = useState("");

  const canSubmit = name && email && pw && pwCheck && pw === pwCheck;

  // ⭐ 회원가입 요청 함수
  const handleSignup = async () => {
    if (!canSubmit) return;

    try {
      const requestBody = {
        email: email,
        password: pw,
        name: name,
        phoneNumber: ""   // phoneNumber 입력창 없으므로 빈값
      };

      console.log("📌 보낼 데이터:", requestBody);

      const res = await axios.post(
        "http://momonteroute.store:8082/api/member/signup",   // ⭐ 실제 엔드포인트로 수정 필요
        requestBody,
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      console.log("🎉 회원가입 성공:", res.data);
      alert("회원가입 완료!");

      onClose();      // 모달 닫기
      onGoLogin();    // 로그인 모달 열기
    } catch (err) {
      console.error("❌ 회원가입 실패:", err);
      alert("회원가입 실패했습니다!");
    }
  };

  return (
    <div className={styles.overlay}>
      <div className={styles.modal}>

        <div className={styles.header}>
          <img src={locationBig} alt="icon" className={styles.icon} />
          <img src={closeIcon} className={styles.close} alt="close" onClick={onClose}/>
        </div>

        <h2 className={styles.title}>회원가입</h2>
        <p className={styles.desc}>경기도 축제를 함께 즐겨보세요</p>

        <label className={styles.label}>이름</label>
        <input
          className={styles.input}
          placeholder="홍길동"
          value={name}
          onChange={(e) => setName(e.target.value)}
        />

        <label className={styles.label}>이메일</label>
        <input
          className={styles.input}
          placeholder="example@email.com"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />

        <label className={styles.label}>비밀번호</label>
        <input
          type="password"
          className={styles.input}
          placeholder="••••••••"
          value={pw}
          onChange={(e) => setPw(e.target.value)}
        />

        <label className={styles.label}>비밀번호 확인</label>
        <input
          type="password"
          className={styles.input}
          placeholder="••••••••"
          value={pwCheck}
          onChange={(e) => setPwCheck(e.target.value)}
        />

        <button
          className={`${styles.signupBtn} ${canSubmit ? styles.active : ""}`}
          disabled={!canSubmit}
          onClick={handleSignup}   // ⭐ 버튼 클릭 시 호출
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
