import React from 'react';
import Header from './components/Header';
import Home from './pages/Home';

function App() {
  return (
    <>
      <div className="main-header">
        <Header />
      </div>
      
      {/* base.html의 <div class="container">와 <main> 태그는
        Home.jsx 안으로 이동시켜서 페이지별로 관리하는 것이 더 유연합니다.
        (현재 Home.jsx는 <main> 대신 <> 프래그먼트를 사용했지만,
        <main>으로 감싸도 좋습니다.)
      */}
      
      {/* React Router를 사용하게 되면 이 부분이 <Outlet /> 등으로 변경됩니다.
        지금은 Home 페이지만 있으므로 직접 렌더링합니다.
      */}
      <Home />

      {/* Footer는 base.html에서 주석 처리되어 있어 제외했습니다. */}
    </>
  );
}

export default App;