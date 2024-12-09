import React from 'react';
import { BrowserRouter as Router, Routes, Route, Switch } from 'react-router-dom';
import Stock from './pages/Stock';
import StockDetail from './pages/StockDetail';
import Menu from './components/Menu';
import Login from './pages/Login';
import Dashboard from './components/Dashboard';
import NoticeBoardPage from './Dashboard/NoticeBoard';

function App() {
  return (
    <Router>
      <Menu />

      <Routes>
        <Route path="/" element={<Dashboard />} />
        <Route path="/stock" element={<Stock />} />
        <Route path="/stock-detail" element={<StockDetail />} />
        <Route path="/login" element={<Login />} />

        <Route path="/notice-board" element={<NoticeBoardPage />} />
      </Routes>
    </Router>
  );
}

export default App;
