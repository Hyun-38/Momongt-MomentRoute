import React from 'react';
// 1. <a> 태그 대신 NavLink를 임포트합니다.
import { NavLink } from 'react-router-dom';

// 2. 부모 컴포넌트(App.jsx)로부터 로그인 상태와 로그아웃 함수를 props로 받습니다.
function Header({ isLoggedIn, onLogout }) {
  // 3. 로그아웃 버튼 클릭 시 실행될 함수
  const handleLogout = (e) => {
    e.preventDefault(); // <a> 태그의 기본 동작 방지
    // App.jsx에서 전달받은 onLogout 함수를 실행
    if (onLogout) {
      onLogout();
    }
  };

  return (
    <nav className="container">
      {/* 로고: 클릭하면 홈('/')으로 이동 */}
      <NavLink to="/" className="logo">
        <div className="logo-icon">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M20 10C20 14.993 14.461 20.193 12.601 21.799C12.4277 21.9293 12.2168 21.9998 12 21.9998C11.7832 21.9998 11.5723 21.9293 11.399 21.799C9.539 20.193 4 14.993 4 10C4 7.87827 4.84285 5.84344 6.34315 4.34315C7.84344 2.84285 9.87827 2 12 2C14.1217 2 16.1566 2.84285 17.6569 4.34315C19.1571 5.84344 20 7.87827 20 10Z" stroke="#0EA5E9" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
            <path d="M12 13C13.6569 13 15 11.6569 15 10C15 8.34315 13.6569 7 12 7C10.3431 7 9 8.34315 9 10C9 11.6569 10.3431 13 12 13Z" stroke="#0EA5E9" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
          </svg>
        </div>
        <span>경기도 축제 가이드</span>
      </NavLink>

      <div className="nav-links">
        {/* NavLink는 현재 경로와 'to'가 일치하면 'active' 클래스를 자동으로 추가합니다.
          'end' prop은 '/' 경로가 다른 경로(예: /mypage)와 중복으로 active되는 것을 방지합니다.
        */}
        <NavLink to="/" className="nav-link" end>
          <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
            {/* stroke 속성을 제거해야 CSS로 색상(흰색/검은색) 제어가 됩니다. */}
            <path d="M10 14V8.66667C10 8.48986 9.92976 8.32029 9.80474 8.19526C9.67971 8.07024 9.51014 8 9.33333 8H6.66667C6.48986 8 6.32029 8.07024 6.19526 8.19526C6.07024 8.32029 6 8.48986 6 8.66667V14" strokeWidth="1.33333" strokeLinecap="round" strokeLinejoin="round" />
            <path d="M2 6.66666C1.99995 6.47271 2.04222 6.28108 2.12386 6.10514C2.20549 5.9292 2.32453 5.77319 2.47267 5.64799L7.13933 1.64799C7.37999 1.4446 7.6849 1.33301 8 1.33301C8.3151 1.33301 8.62001 1.4446 8.86067 1.64799L13.5273 5.64799C13.6755 5.77319 13.7945 5.9292 13.8761 6.10514C13.9578 6.28108 14 6.47271 14 6.66666V12.6667C14 13.0203 13.8595 13.3594 13.6095 13.6095C13.3594 13.8595 13.0203 14 12.6667 14H3.33333C2.97971 14 2.64057 13.8595 2.39052 13.6095C2.14048 13.3594 2 13.0203 2 12.6667V6.66666Z" strokeWidth="1.33333" strokeLinecap="round" strokeLinejoin="round" />
          </svg>
          <span>홈</span>
        </NavLink>
        
        <NavLink to="/plan" className="nav-link">
          <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M4 14.6667C5.10457 14.6667 6 13.7713 6 12.6667C6 11.5622 5.10457 10.6667 4 10.6667C2.89543 10.6667 2 11.5622 2 12.6667C2 13.7713 2.89543 14.6667 4 14.6667Z" strokeWidth="1.33333" strokeLinecap="round" strokeLinejoin="round" />
            <path d="M6 12.6666H11.6667C12.2855 12.6666 12.879 12.4208 13.3166 11.9832C13.7542 11.5456 14 10.9521 14 10.3333C14 9.71441 13.7542 9.12092 13.3166 8.68334C12.879 8.24575 12.2855 7.99992 11.6667 7.99992H4.33333C3.71449 7.99992 3.121 7.75409 2.68342 7.3165C2.24583 6.87892 2 6.28542 2 5.66659C2 5.04775 2.24583 4.45425 2.68342 4.01667C3.121 3.57908 3.71449 3.33325 4.33333 3.33325H10" strokeWidth="1.33333" strokeLinecap="round" strokeLinejoin="round" />
            <path d="M12 5.33325C13.1046 5.33325 14 4.43782 14 3.33325C14 2.22868 13.1046 1.33325 12 1.33325C10.8954 1.33325 10 2.22868 10 3.33325C10 4.43782 10.8954 5.33325 12 5.33325Z" strokeWidth="1.33333" strokeLinecap="round" strokeLinejoin="round" />
          </svg>
          <span>여행 계획</span>
        </NavLink>

        {/* 4. 로그인 상태에 따라 다른 메뉴를 보여줍니다. */}
        {isLoggedIn ? (
          <>
            <NavLink to="/mytrips" className="nav-link">
              {/* "내 여행" 아이콘 (피그마의 아이콘 대신 "여행 계획" 아이콘 재사용) */}
              <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M4 14.6667C5.10457 14.6667 6 13.7713 6 12.6667C6 11.5622 5.10457 10.6667 4 10.6667C2.89543 10.6667 2 11.5622 2 12.6667C2 13.7713 2.89543 14.6667 4 14.6667Z" strokeWidth="1.33333" strokeLinecap="round" strokeLinejoin="round" />
                <path d="M6 12.6666H11.6667C12.2855 12.6666 12.879 12.4208 13.3166 11.9832C13.7542 11.5456 14 10.9521 14 10.3333C14 9.71441 13.7542 9.12092 13.3166 8.68334C12.879 8.24575 12.2855 7.99992 11.6667 7.99992H4.33333C3.71449 7.99992 3.121 7.75409 2.68342 7.3165C2.24583 6.87892 2 6.28542 2 5.66659C2 5.04775 2.24583 4.45425 2.68342 4.01667C3.121 3.57908 3.71449 3.33325 4.33333 3.33325H10" strokeWidth="1.33333" strokeLinecap="round" strokeLinejoin="round" />
                <path d="M12 5.33325C13.1046 5.33325 14 4.43782 14 3.33325C14 2.22868 13.1046 1.33325 12 1.33325C10.8954 1.33325 10 2.22868 10 3.33325C10 4.43782 10.8954 5.33325 12 5.33325Z" strokeWidth="1.33333" strokeLinecap="round" strokeLinejoin="round" />
              </svg>
              <span>내 여행</span>
            </NavLink>

            <NavLink to="/mypage" className="nav-link">
              {/* "마이페이지" 아이콘 (피그마의 아이콘 반영) */}
              <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M8 8C9.65685 8 11 6.65685 11 5C11 3.34315 9.65685 2 8 2C6.34315 2 5 3.34315 5 5C5 6.65685 6.34315 8 8 8Z" strokeWidth="1.33333" strokeLinecap="round" strokeLinejoin="round"/>
                <path d="M8 9C4.68629 9 2 11.6863 2 15V16H14V15C14 11.6863 11.3137 9 8 9Z" strokeWidth="1.33333" strokeLinecap="round" strokeLinejoin="round"/>
              </svg>
              <span>마이페이지</span>
            </NavLink>
            
            {/* 로그아웃은 NavLink가 아닌 onClick 이벤트를 가진 <a> 태그(또는 button)가 적합합니다. */}
            <a href="#" className="nav-link" onClick={handleLogout}>
              {/* "로그아웃" 아이콘 (로그인 아이콘 재사용) */}
              <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M6.6665 11.3334L9.99984 8.00008L6.6665 4.66675" strokeWidth="1.33333" strokeLinecap="round" strokeLinejoin="round" />
                <path d="M10 8H2" strokeWidth="1.33333" strokeLinecap="round" strokeLinejoin="round" />
                <path d="M10 2H12.6667C13.0203 2 13.3594 2.14048 13.6095 2.39052C13.8595 2.64057 14 2.97971 14 3.33333V12.6667C14 13.0203 13.8595 13.3594 13.6095 13.6095C13.3594 13.8595 13.0203 14 12.6667 14H10" strokeWidth="1.D" strokeLinecap="round" strokeLinejoin="round" />
              </svg>
              <span>로그아웃</span>
            </a>
          </>
        ) : (
          // 로그인하지 않았을 때 "로그인" 버튼 표시
          <NavLink to="/login" className="nav-link login-btn">
            <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M6.6665 11.3334L9.99984 8.00008L6.6665 4.66675" strokeWidth="1.33333" strokeLinecap="round" strokeLinejoin="round" />
              <path d="M10 8H2" strokeWidth="1.33333" strokeLinecap="round" strokeLinejoin="round" />
              <path d="M10 2H12.6667C13.0203 2 13.3594 2.14048 13.6095 2.39052C13.8595 2.64057 14 2.97971 14 3.33333V12.6667C14 13.0203 13.8595 13.3594 13.6095 13.6095C13.3594 13.8595 13.0203 14 12.6667 14H10" strokeWidth="1.33333" strokeLinecap="round" strokeLinejoin="round" />
            </svg>
            <span>로그인</span>
          </NavLink>
        )}
      </div>
    </nav>
  );
}

export default Header;