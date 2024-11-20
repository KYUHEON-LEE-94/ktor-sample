import React, { useEffect, useState, useRef } from 'react';


const stockInfoRequest = {
  numOfRows: 10, // 한 페이지 결과 수
  pageNo: 1, // 페이지 번호
  resultType: "json",
  basDt: "", // 현재 날짜
  beginBasDt: "", // 1년 전 날짜
  endBasDt: "", // 현재 날짜
  likeBasDt: "", // 현재 날짜
  likeSrtnCd: "",
  isinCd: "",
  likeIsinCd: "",
  itmsNm: "", // 검색값과 종목명이 일치하는 데이터
  likeItmsNm: "", // 종목명이 검색값을 포함하는 데이터
  mrktCls: "", // 검색값과 시장구분이 일치하는 데이터
  beginVs: "", // 대비가 검색값보다 크거나 같은 데이터
  endVs: "", // 대비가 검색값보다 작은 데이터
  beginFltRt: "", // 등락률이 검색값보다 크거나 같은 데이터
  endFltRt: "", // 등락률이 검색값보다 작은 데이터
  beginTrqu: 0, // 거래량이 검색값보다 크거나 같은 데이터
  endTrqu: 0, // 거래량이 검색값보다 작은 데이터
  beginTrPrc: 0, // 거래대금이 검색값보다 크거나 같은 데이터
  endTrPrc: 0, // 거래대금이 검색값보다 작은 데이터
  beginLstgStCnt: 0, // 상장주식수가 검색값보다 크거나 같은 데이터
  endLstgStCnt: 0, // 상장주식수가 검색값보다 작은 데이터
  beginMrktTotAmt: 0, // 시가총액이 검색값보다 크거나 같은 데이터
  endMrktTotAmt: 0 // 시가총액이 검색값보다 작은 데이터
};


function Stock() {
  const [stockUpdates, setStockUpdates] = useState([]);
  const [StockTotalCount, setStockTotlCount] = useState(0);
  const [stockRequestVo, setStockRequestVo] = useState(stockInfoRequest);
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 10;
  const inputRef = useRef(null);
  const ws = useRef(null); // WebSocket을 위한 ref

  const searchByName = () => {
    const newStockRequestVo = {
      ...stockRequestVo, // 기존 상태를 복사
      itmsNm: inputRef.current.value // 입력된 기업명으로 업데이트
    };
    console.log(newStockRequestVo)
    setStockRequestVo(newStockRequestVo); 
    ws.current.send(JSON.stringify(newStockRequestVo));
  }

  useEffect(() => {
    ws.current = new WebSocket('ws://localhost:8081/stock-updates');

    ws.current.onopen = () => {
      console.log('WebSocket 연결 성공');
    };

    ws.current.onmessage = (event) => {
      ws.current.send(JSON.stringify(stockRequestVo));
      try {
        const data = JSON.parse(event.data);
        console.log('data:', data);
        setStockUpdates([...data.response.body.items.item]);
        setStockTotlCount(data.response.body.totalCount)
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
      if (ws.current && ws.current.readyState === WebSocket.OPEN) {
        ws.current.close();
      }
    };
  }, []);

  useEffect(() => {
    console.log('업데이트된 stockUpdates:', stockUpdates);
  }, [stockUpdates]);

  const handlePageChange = (pageNumber) => {
    console.log(pageNumber)
    const newStockRequestVo = {
      ...stockRequestVo, // 기존 상태를 복사
      pageNo: pageNumber // 입력된 기업명으로 업데이트
    };
    setStockRequestVo(newStockRequestVo); 
    ws.current.send(JSON.stringify(newStockRequestVo)); // 페이지 번호를 JSON 문자열로 변환하여 전송
  };

  // 페이지 버튼 생성 로직
  const totalPages = Math.ceil(StockTotalCount / itemsPerPage); // 총 페이지 수 계산
  const maxPagesToShow = 10; // 최대 페이지 버튼 수
  const startPage = Math.max(1, currentPage - Math.floor(maxPagesToShow / 2)); // 시작 페이지
  const endPage = Math.min(totalPages, startPage + maxPagesToShow - 1); // 끝 페이지

  return (
      <div className="p-4 bg-gray-100 min-h-screen">
        <h1 className="text-2xl font-bold text-center mb-4">실시간 주식 업데이트</h1>
        
        {/* 기업명 입력창 추가 */}
        <div className="mb-4">
          <input
            name="itmsNm"
            type="text"
            placeholder="기업명을 입력하세요"
            ref={inputRef}
            className="border rounded p-2 w-30"
          />
          <button onClick={searchByName} className="mt-2 bg-blue-500 text-white rounded p-2">검색</button>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {stockUpdates.slice((currentPage - 1) * itemsPerPage, currentPage * itemsPerPage).map((stock, index) => (
            <div key={index} className="bg-white shadow-md rounded-lg p-4">
              <h2 className="text-xl font-semibold">기업명: {stock.itmsNm} ({stock.mrktCtg})</h2>
              <p className="text-gray-700">시가: {stock.mkp}</p>
              <p className="text-gray-700">최고가: {stock.hipr}</p>
              <p className="text-gray-700">저가: {stock.lopr}</p>
            </div>
          ))}
        </div>
        <div className="flex justify-center mt-4">
        {Array.from({ length: Math.min(maxPagesToShow, totalPages) }, (_, index) => {
          const page = startPage + index;
          if (page > totalPages) return null; // 총 페이지 수를 초과하는 경우 null 반환
          return (
            <button
              key={page}
              onClick={() => handlePageChange(page)} // 페이지 번호 클릭 시 핸들러 호출
              className={`mx-1 px-4 py-2 rounded ${currentPage === page ? 'bg-blue-500 text-white' : 'bg-gray-300 text-black'}`}
            >
              {page}
            </button>
          );
        })}
      </div>
      </div>
  );
}

export default Stock;
