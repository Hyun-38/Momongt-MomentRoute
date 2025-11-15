import React, { useState } from 'react';
import { Routes, Route, useNavigate } from 'react-router-dom';
import Header from './components/Header';
import Home from './pages/Home';
import Mypage from './pages/Mypage';
import MyTrips from './pages/MyTrips';

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(true);
  const navigate = useNavigate();

  // 2. 로그아웃 함수 정의
  const handleLogout = () => {
    setIsLoggedIn(false);
    navigate('/');
  };

  // (로그인 페이지에서 로그인 성공 시 setIsLoggedIn(true)를 호출해야 함)

  return (
    <>
      {/* 3. Header에 상태와 함수를 prop으로 전달 */}
      <div className="main-header">
        <Header isLoggedIn={isLoggedIn} onLogout={handleLogout} />
      </div>
      
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/mypage" element={<Mypage />} />
        <Route path="/mytrips" element={<MyTrips />} />
        {/* <Route path="/login" element={<LoginPage onLoginSuccess={() => setIsLoggedIn(true)} />} /> */}
        {/* <Route path="/my-trips" element={<MyTripsPage />} /> */}
        {/* <Route path="/plan" element={<PlanPage />} /> */}
      </Routes>
    </>
  );
}

export default App;