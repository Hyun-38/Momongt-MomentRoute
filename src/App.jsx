
import React from 'react';
import { Routes, Route, useNavigate } from 'react-router-dom';
import MainPage from "./pages/MainPage.jsx";
import Home from "./pages/Home.jsx";
import RegionSelect from "./pages/RegionSelect.jsx";
import SelectDestination from "./pages/SelectDestination.jsx"
import StyleSelect from "./pages/StyleSelect.jsx"
import Mypage from './pages/Mypage';
import MyTrips from './pages/MyTrips';

export default function App() {
  return (
    <>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/main" element={<MainPage />} />
        <Route path="/regionselect" element={<RegionSelect />} />
        <Route path="/selectdestination" element={<SelectDestination />} />
        <Route path="/styleselect" element={<StyleSelect />} />
        <Route path="/mypage" element={<Mypage />} />
        <Route path="/mytrips" element={<MyTrips />} />
      </Routes>
    </>
  );
}

