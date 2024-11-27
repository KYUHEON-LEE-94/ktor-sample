import React from 'react';
import { useLocation } from 'react-router-dom';

function StockDetail() {
    const location = useLocation();
    const stockRequestVo = location.state || {}; // stockRequestVo가 없을 경우 빈 객체로 초기화

    return (
        <div className="p-4 bg-gray-100 min-h-screen">
            <h1 className="text-2xl font-bold text-center mb-4">주식 상세 정보</h1>
            {Object.keys(stockRequestVo).length > 0 ? (
                <div className="bg-white shadow-md rounded-lg p-6">
                    <h2 className="text-xl font-semibold mb-2">기업명: {stockRequestVo.itmsNm}</h2>
                    <p className="text-gray-700"><strong>시장 구분:</strong> {stockRequestVo.mrktCls}</p>
                    <p className="text-gray-700"><strong>ISIN 코드:</strong> {stockRequestVo.isinCd}</p>
                    <p className="text-gray-700"><strong>시가:</strong> {stockRequestVo.clpr}</p>
                    <p className="text-gray-700"><strong>최고가:</strong> {stockRequestVo.hipr}</p>
                    <p className="text-gray-700"><strong>저가:</strong> {stockRequestVo.lopr}</p>
                    <p className="text-gray-700"><strong>거래량:</strong> {stockRequestVo.trqu}</p>
                    <p className="text-gray-700"><strong>거래대금:</strong> {stockRequestVo.trPrc}</p>
                    <p className="text-gray-700"><strong>상장일:</strong> {stockRequestVo.lstgDt}</p>
                    {/* 추가적인 정보가 있다면 여기에 추가 */}
                </div>
            ) : (
                <p className="text-center text-red-500">주식 정보가 없습니다.</p>
            )}
        </div>
    );
}

export default StockDetail; 