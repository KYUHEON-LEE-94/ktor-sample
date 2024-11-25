import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Stock from './pages/Stock';
import StockDetail from './pages/StockDetail';

function App() {
  return (
    <Router>
        <Routes>
          {/* 라우트 설정 */}
          <Route path="/" element={<Stock />} />
          <Route path="/stock-detail" element={<StockDetail />} />
        </Routes>
    </Router>
  );
}

export default App;
