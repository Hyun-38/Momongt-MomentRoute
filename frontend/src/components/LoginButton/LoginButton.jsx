import React from "react";
import styles from "./LoginButton.module.css";
import loginIcon from "../../assets/login.svg";

export default function LoginButton({ onClick }) {
  return (
    <button className={styles.LoginBtn} onClick={onClick}>
      <img src={loginIcon} className={styles.loginIcon} alt="" />
      로그인
    </button>
  );
}