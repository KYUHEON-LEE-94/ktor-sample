import React, { useEffect, useState } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './pages/Home';  // Home 컴포넌트 import

function App() {
  const [socket, setSocket] = useState(null);
  const [stockUpdates, setStockUpdates] = useState([]);

  useEffect(() => {
    // WebSocket 연결
    const ws = new WebSocket('ws://localhost:8081/stock-updates');

    // WebSocket 이벤트 핸들러
    ws.onopen = () => {
      console.log('WebSocket 연결 성공');
      setSocket(ws);
    };

    ws.onmessage = (event) => {
      const data = JSON.parse(event.data);
      console.log('받은 데이터:', data);
      setStockUpdates(prev => [...prev, data]);
    };

    ws.onerror = (error) => {
      console.error('WebSocket 에러:', error);
    };

    ws.onclose = () => {
      console.log('WebSocket 연결 종료');
    };

    // 컴포넌트 언마운트 시 WebSocket 연결 종료
    return () => {
      if (ws) {
        ws.close();
      }
    };
  }, []);

  // 메시지 전송 함수 예시
  const sendMessage = (message) => {
    if (socket && socket.readyState === WebSocket.OPEN) {
      socket.send(JSON.stringify(message));
    }
  };

  return (
    <Router>
      <div>
        <h1>실시간 주식 업데이트</h1>
        {/* 실시간 업데이트 표시 */}
        <div>
          {stockUpdates.map((update, index) => (
            <div key={index}>
              {/* 받은 데이터 구조에 따라 표시 방식 수정 */}
              <p>종목: {update.symbol}</p>
              <p>가격: {update.price}</p>
            </div>
          ))}
        </div>

        <Routes>
          {/* 기본 경로 "/" 에 대한 라우트 추가 */}
          <Route path="/" element={<Home />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
