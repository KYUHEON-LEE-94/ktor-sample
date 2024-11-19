import React, { useEffect, useState } from 'react';

function Stock() {
  const [stockUpdates, setStockUpdates] = useState([]);

  useEffect(() => {
    const ws = new WebSocket('ws://localhost:8081/stock-updates');

    ws.onopen = () => {
      console.log('WebSocket 연결 성공');
    };

    ws.onmessage = (event) => {
      try {
        console.log('받은 데이터:', event);
        const data = JSON.parse(event.data);
        setStockUpdates((prev) => {
          const updates = [...prev, data];
          return updates.slice(-10); // 최신 10개만 유지
        });
      } catch (error) {
        console.error('데이터 파싱 에러:', error);
      }
    };

    ws.onerror = (error) => {
      console.error('WebSocket 에러:', error);
    };

    ws.onclose = () => {
      console.log('WebSocket 연결 종료');
      setTimeout(() => {
        console.log('WebSocket 재연결 시도');
      }, 3000); // 필요 시 재연결
    };

    return () => {
      if (ws && ws.readyState === WebSocket.OPEN) {
        ws.close();
      }
    };
  }, []);

  return (
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
      </div>
  );
}

export default Stock;
