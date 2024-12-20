import React, { useEffect, useState, useRef } from 'react';
import { useNavigate } from 'react-router-dom';

function getCurrentDateFormatted() {
  const today = new Date();
  const dayOfWeek = today.getDay(); // 0: 일요일, 1: 월요일, ..., 6: 토요일

  // 오늘이 월요일이라면 지난 금요일을 계산
  let daysToLastFriday = dayOfWeek === 1 ? -3 : -((dayOfWeek + 2) % 7);

  today.setDate(today.getDate() + daysToLastFriday); // 계산한 날짜로 설정

  const year = today.getFullYear();
  const month = String(today.getMonth() + 1).padStart(2, '0');
  const day = String(today.getDate()).padStart(2, '0');

  return `${year}${month}${day}`; // YYYYMMDD 형식으로 반환
}

const stockInfoRequest = {
  numOfRows: 20, // 한 페이지 결과 수
  pageNo: 1, // 페이지 번호
  resultType: "json",
  basDt: getCurrentDateFormatted(), // 현재 날짜
  beginBasDt: "", // 1년 전 날짜
  endBasDt: "", // 현재 날짜
  likeBasDt: "", // 현재 날짜
  likeSrtnCd: "",
  isinCd: "",
  likeIsinCd: "",
  itmsNm: "",
  likeItmsNm: "",
  mrktCls: "",
  beginVs: "",
  endVs: "",
  beginFltRt: "",
  endFltRt: "",
  beginTrqu: 0,
  endTrqu: 0,
  beginTrPrc: 0,
  endTrPrc: 0,
  beginLstgStCnt: 0,
  endLstgStCnt: 0,
  beginMrktTotAmt: 0,
  endMrktTotAmt: 0
};

function Stock() {
  const navigate = useNavigate();

  const [stockUpdates, setStockUpdates] = useState([]);
  const [StockTotalCount, setStockTotlCount] = useState(0);
  const [stockRequestVo, setStockRequestVo] = useState(stockInfoRequest);
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 10;
  const inputRef = useRef(null);
  const ws = useRef(null); // WebSocket을 위한 ref
  const [marketType, setMarketType] = useState("");

  const handleCardClick = (stock) => {
      
       const newStockRequestVo = {
        ...stockRequestVo,
       ...stock
    };

    console.log(newStockRequestVo); // 새로운 객체를 로그에 출력
    navigate('/stock-detail', { state: newStockRequestVo }); // 새로운 객체를 navigate에 전달
  };

  const searchByName = () => {
    const newStockRequestVo = {
      ...stockRequestVo, // 기존 상태를 복사
      itmsNm: inputRef.current.value, // 입력된 기업명으로 업데이트
      mrktCls: marketType // 선택된 시장 구분 추가
    };
    console.log(newStockRequestVo);
    setStockRequestVo(newStockRequestVo); 

    if (ws.current && ws.current.readyState === WebSocket.OPEN) {
      ws.current.send(JSON.stringify(newStockRequestVo)); // WebSocket이 열려 있을 때만 전송
    }
  }

  const connectWebSocket = () => {
    if (ws.current) {
      console.log('WebSocket이 이미 연결되어 있습니다.');
      return; // 이미 연결된 경우 함수 종료
    }

    ws.current = new WebSocket('ws://localhost:8081/stock-updates');

    ws.current.onopen = () => {
      console.log('WebSocket 연결 성공');

      const newStockRequestVo = {
        ...stockRequestVo, // 기존 상태를 복사
      };
      console.log("send when open : ", newStockRequestVo);
      ws.current.send(JSON.stringify(newStockRequestVo));
    };

    ws.current.onmessage = (event) => {
      try {
        const data = JSON.parse(event.data);
        console.log('data:', data);
        setStockUpdates([...data.response.body.items.item]);
        setStockTotlCount(data.response.body.totalCount);
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
        connectWebSocket(); // 재연결 시도
      }, 5000); // 3초 후 재연결
    };
  };

  useEffect(() => {
    connectWebSocket(); // WebSocket 연결 시도

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
    const newStockRequestVo = {
      ...stockRequestVo, // 기존 상태를 복사
      pageNo: pageNumber // 입력된 기업명으로 업데이트
    };
    setStockRequestVo(newStockRequestVo); 
    setCurrentPage(pageNumber)
    
    if (ws.current && ws.current.readyState === WebSocket.OPEN) {
      console.log("send when handlePageChange : ", newStockRequestVo)
      ws.current.send(JSON.stringify(newStockRequestVo));
    }
  };

  const handlemarketChange = (market_type) => {
    console.log(market_type)
    const newStockRequestVo = {
      ...stockRequestVo, // 기존 상태를 복사
      mrktCls: market_type // 입력된 기업명으로 업데이트
    };
    setStockRequestVo(newStockRequestVo); 
    setMarketType(market_type)
    
    if (ws.current && ws.current.readyState === WebSocket.OPEN) {
      console.log("send when handlePageChange : ", newStockRequestVo)
      ws.current.send(JSON.stringify(newStockRequestVo));
    }
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
        <div className="mb-4 flex items-center">
          <input
            name="itmsNm"
            type="text"
            placeholder="기업명을 입력하세요"
            ref={inputRef}
            className="border rounded p-2 w-30"
          />
          <select
            value={marketType}
            onChange={(e) => handlemarketChange(e.target.value)} // 선택된 값 업데이트
            className="border rounded p-2 mx-2"
          >
            <option value="">전체</option>
            <option value="KOSPI">KOSPI</option>
            <option value="KOSDAQ">KOSDAQ</option>
          </select>
          <button onClick={searchByName} className="mt-2 bg-blue-500 text-white rounded p-2">검색</button>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4" id="cardBox">
          {stockUpdates.map((stock, index) => (
            <div key={index} className="bg-white shadow-md rounded-lg p-4 cursor-pointer"  onClick={() => handleCardClick(stock)}>
              <h2 className="text-xl font-semibold">기업명: {stock.itmsNm} ({stock.mrktCtg})</h2>
              <p className="text-gray-700">시가: {stock.clpr}</p>
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
