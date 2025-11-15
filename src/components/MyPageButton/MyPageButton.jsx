import React from "react";
import styles from "./MyPageButton.module.css";

import userIcon from "../../assets/user.svg";

export default function MyPageButton() {
  return (
    <button className={styles.myPageBtn}>
      <img src={userIcon} className={styles.userIcon} alt="" />
      마이페이지
    </button>
  );
}