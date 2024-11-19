// import React, { useEffect, useState } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './pages/Home';  // Home 컴포넌트 import
import Stock from './pages/Stock';

function App() {
  return (
    <Router>
        <Routes>
          {/* 라우트 설정 */}
          <Route path="/" element={<Home />} />
          <Route path="/stock" element={<Stock />} />
        </Routes>
    </Router>
  );
}

export default App;
