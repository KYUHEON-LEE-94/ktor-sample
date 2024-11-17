import React, { useEffect, useState } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './pages/Home';  // Home 컴포넌트 import

function App() {
  const [socket, setSocket] = useState(null);
  const [stockUpdates, setStockUpdates] = useState([]);

  useEffect(() => {
    // 백엔드 서버 주소로 직접 연결
    const ws = new WebSocket('ws://localhost:8081/stock-updates');

    ws.onopen = () => {
      console.log('WebSocket 연결 성공');
      setSocket(ws);
    };

    ws.onmessage = (event) => {
      try {
        const data = JSON.parse(event.data);
        console.log('받은 데이터:', data);
        setStockUpdates(prev => [...prev, data]);
      } catch (error) {
        console.error('데이터 파싱 에러:', error);
      }
    };

    ws.onerror = (error) => {
      console.error('WebSocket 에러:', error);
    };

    ws.onclose = () => {
      console.log('WebSocket 연결 종료');
    };

    return () => {
      if (ws) {
        ws.close();
      }
    };
  }, []);

  return (
    <Router>
      <div>
        <h1>실시간 주식 업데이트</h1>
        <div>
          {stockUpdates.map((update, index) => (
            <div key={index}>
              {/* 받은 데이터 구조에 맞게 수정 */}
              <p>데이터: {JSON.stringify(update)}</p>
            </div>
          ))}
        </div>

        <Routes>
          {/* 라우트 설정 */}
          <Route path="/" element={<Home />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
