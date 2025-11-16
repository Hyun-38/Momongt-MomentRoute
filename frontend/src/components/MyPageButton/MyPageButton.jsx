import React from "react";
import styles from "./MyPageButton.module.css";

import userIcon from "../../assets/user.svg";

export default function MyPageButton({ onClick }) {
  return (
    <button className={styles.myPageBtn} onClick={onClick}>
      <img src={userIcon} className={styles.userIcon} alt="" />
      마이페이지
    </button>
  );
}