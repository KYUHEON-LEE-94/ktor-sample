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
        const data = JSON.parse(event.data);
        console.log('data:', data);
        setStockUpdates([...data.response.body.items.item]);
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

  useEffect(() => {
    console.log('업데이트된 stockUpdates:', stockUpdates);
  }, [stockUpdates]); // stockUpdates가 변경될 때 실행

  return (
      <div className="p-4 bg-gray-100 min-h-screen">
        <h1 className="text-2xl font-bold text-center mb-4">실시간 주식 업데이트</h1>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {stockUpdates.map((stock, index) => (
            <div key={index} className="bg-white shadow-md rounded-lg p-4">
              <h2 className="text-xl font-semibold">기업명: {stock.itmsNm}</h2>
              <p className="text-gray-700">시가: {stock.mkp}</p>
              <p className="text-gray-700">최고가: {stock.hipr}</p>
              <p className="text-gray-700">저가: {stock.lopr}</p>
            </div>
          ))}
        </div>
      </div>
  );
}

export default Stock;
