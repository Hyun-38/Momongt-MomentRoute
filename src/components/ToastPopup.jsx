import React, { useEffect } from 'react';
import './ToastPopup.css';

// SVG 아이콘 컴포넌트들
const IconSuccess = () => (
  <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
    <path d="M10 18.3333C14.6024 18.3333 18.3334 14.6023 18.3334 9.99996C18.3334 5.39759 14.6024 1.66663 10 1.66663C5.39766 1.66663 1.66669 5.39759 1.66669 9.99996C1.66669 14.6023 5.39766 18.3333 10 18.3333Z" stroke="#0F172A" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
    <path d="M7.5 10L9.16667 11.6667L12.5 8.33337" stroke="#0F172A" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
  </svg>
);

const IconInfo = () => (
  <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
    <path d="M10 18.3333C14.6024 18.3333 18.3334 14.6023 18.3334 9.99996C18.3334 5.39759 14.6024 1.66663 10 1.66663C5.39766 1.66663 1.66669 5.39759 1.66669 9.99996C1.66669 14.6023 5.39766 18.3333 10 18.3333Z" stroke="#0F172A" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
    <path d="M10 13.3334V10" stroke="#0F172A" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
    <path d="M10 6.66663H10.0083" stroke="#0F172A" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
  </svg>
);

/**
 * 토스트 팝업 컴포넌트
 * @param {object} props
 * @param {string} props.message - 메인 메시지
 * @param {string} [props.subtitle] - (선택) 서브 메시지
 * @param {'success' | 'info'} [props.type] - (선택) 아이콘 타입 (기본값 'info')
 * @param {function} props.onClose - 닫기 함수 (자동 닫기를 위해)
 */
function ToastPopup({ message, subtitle, type = 'info', onClose }) {
  // 3초 후에 자동으로 닫히도록 설정
  useEffect(() => {
    const timer = setTimeout(() => {
      onClose();
    }, 3000);

    // 컴포넌트가 사라질 때 타이머 정리
    return () => {
      clearTimeout(timer);
    };
  }, [onClose]);

  return (
    <div className="toast-popup">
      <div className="toast-icon">
        {type === 'success' ? <IconSuccess /> : <IconInfo />}
      </div>
      <div className="toast-content">
        <span className="toast-message">{message}</span>
        {/* subtitle이 있을 때만 렌더링 */}
        {subtitle && (
          <span className="toast-subtitle">{subtitle}</span>
        )}
      </div>
    </div>
  );
}

export default ToastPopup;