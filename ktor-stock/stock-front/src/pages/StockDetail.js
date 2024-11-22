import React from 'react';
import { useLocation } from 'react-router-dom';

function StockDetail() {
    const location = useLocation();
    const stockRequestVo = location.state || {}; // stockRequestVo가 없을 경우 빈 객체로 초기화
    console.log("테스트 : ", stockRequestVo)
    return (
      <div>
        <h1>주식 상세 정보</h1>
        {Object.keys(stockRequestVo).length > 0 ? ( // stockRequestVo가 비어있지 않을 때만 렌더링
          <pre>{JSON.stringify(stockRequestVo, null, 2)}</pre> // stockRequestVo를 표시
        ) : (
          <p>주식 정보가 없습니다.</p> // 주식 정보가 없을 경우 메시지 표시
        )}
        {/* 추가적인 상세 정보를 여기에 표시할 수 있습니다 */}
      </div>
    );
}

export default StockDetail; 