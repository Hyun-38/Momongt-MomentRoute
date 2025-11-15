import React from 'react';
import './ConfirmModal.css';

// 경고 아이콘
const IconWarning = () => (
  <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
    <path d="M10 18.3333C14.6024 18.3333 18.3334 14.6023 18.3334 9.99996C18.3334 5.39759 14.6024 1.66663 10 1.66663C5.39766 1.66663 1.66669 5.39759 1.66669 9.99996C1.66669 14.6023 5.39766 18.3333 10 18.3333Z" stroke="#EF4444" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
    <path d="M10 13.3334V10" stroke="#EF4444" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
    <path d="M10 6.66663H10.0083" stroke="#EF4444" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
  </svg>
);


/**
 * 확인 모달 컴포넌트
 * @param {object} props
 * @param {boolean} props.isOpen - 모달 표시 여부
 * @param {string} props.title - 제목
 * @param {string} props.message - 설명 메시지
 * @param {function} props.onClose - 취소/닫기 버튼 클릭 시 호출될 함수
 * @param {function} props.onConfirm - 확인/삭제 버튼 클릭 시 호출될 함수
 * @param {boolean} [props.isDanger] - (선택) 위험 작업을 위한 스타일 (기본값 false)
 */
function ConfirmModal({ isOpen, title, message, onClose, onConfirm, isDanger = false }) {
  
  // isOpen이 false면 아무것도 렌더링하지 않음
  if (!isOpen) {
    return null;
  }

  // 모달 뒷 배경 클릭 시 닫기
  const handleOverlayClick = (e) => {
    // 모달 콘텐츠 영역(e.currentTarget)이 아닌
    // 배경(e.target)을 클릭했을 때만 닫히도록
    if (e.target === e.currentTarget) {
      onClose();
    }
  };

  return (
    // 1. 모달 배경 (어둡게 처리)
    <div className="modal-overlay" onClick={handleOverlayClick}>
      
      {/* 2. 모달 본체 */}
      <div className="modal-content">
        
        {/* 3. 모달 헤더 (아이콘 + 제목) */}
        <div className="modal-header">
          {/* isDanger가 true일 때만 경고 아이콘 표시 */}
          {isDanger && (
            <div className="modal-icon">
              <IconWarning />
            </div>
          )}
          <h3 className="modal-title">{title}</h3>
        </div>
        
        {/* 4. 모달 메시지 */}
        <p className="modal-message">{message}</p>
        
        {/* 5. 모달 버튼 영역 */}
        <div className="modal-actions">
          <button 
            type="button" 
            className="btn modal-btn btn-cancel" 
            onClick={onClose}
          >
            취소
          </button>
          <button 
            type="button" 
            // isDanger 여부에 따라 버튼 스타일 변경
            className={`btn modal-btn ${isDanger ? 'btn-danger' : 'btn-confirm'}`}
            onClick={onConfirm}
          >
            {isDanger ? '삭제' : '확인'}
          </button>
        </div>
      </div>
    </div>
  );
}

export default ConfirmModal;