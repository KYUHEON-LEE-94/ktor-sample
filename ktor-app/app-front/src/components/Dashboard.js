import React, { useMemo, useState, useEffect } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom'; // Link 임포트
import { Waves, CheckCheck, Clock, PieChart } from 'lucide-react';
import Weather from '../Dashboard/Weather';

/**
 *  무언가 작성하거나, 수정되면 Slack으로 보내기
 * 
 * **/
function Dashboard() {

    const [notices, setNotices] = useState([]);

    const getNotices = async () => {
        try {
            const response = await axios.get('http://localhost:8081/api/notice?limit=3');
            if (response.status === 200 || response.status === 202) {
                console.log(response.data)
                setNotices(response.data); // 받아온 데이터를 notices state에 저장
            }
        } catch (error) {
            console.error('공지사항을 가져오는 중 오류가 발생했습니다:', error);
        } finally {
        }
    };

    // 컴포넌트 마운트 시 데이터 가져오기
    useEffect(() => {
        getNotices();
    }, []);

    const smallCardStyle = "bg-white p-4 sm:p-6 shadow-md rounded-lg transition-all duration-300 hover:shadow-xl hover:scale-105 flex flex-col";
    const largeCardStyle = "bg-white p-6 sm:p-8 shadow-lg rounded-lg space-y-4";

    return (
        <div className="bg-gray-50 min-h-screen py-6 sm:py-10">
            <div className="dashboard-content w-11/12 max-w-7xl mx-auto">
                <div className="flex flex-col sm:flex-row items-start sm:items-center mb-6 sm:mb-8">
                    <h2 className="text-2xl sm:text-4xl font-bold text-gray-800 mr-0 sm:mr-4 mb-2 sm:mb-0">DashBoard</h2>
                </div>

                <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-4 sm:gap-6 mb-6 sm:mb-8">
                    <div className={`${smallCardStyle}`}>
                        <div className="flex justify-between items-center mb-4">
                            <Waves className="w-6 sm:w-8 h-6 sm:h-8 text-blue-500" />
                            <span className="text-gray-500">날씨</span>
                        </div>
                        <Weather />
                    </div>

                    <div className={`${smallCardStyle}`}>
                        <div className="flex justify-between items-center mb-4">
                            <CheckCheck className="w-6 sm:w-8 h-6 sm:h-8 text-green-500" />
                            <span className="text-gray-500 ">공지</span>
                        </div>
                        {notices.map(notice => (
                            <Link
                                to="/notice-board"
                                state={{ noticeId: notice.id }}
                                key={notice.id}
                                className="block p-3 sm:p-4 bg-white shadow rounded-lg hover:bg-gray-100 mb-2 last:mb-0"
                            >
                                <h4 className="font-semibold text-gray-800 text-sm sm:text-base">{notice.title}</h4>
                                <p className="text-gray-600 text-xs sm:text-sm overflow-hidden h-6 whitespace-nowrap overflow-ellipsis">
                                    {notice.content}
                                </p>
                            </Link>
                        ))}
                    </div>

                    <div className={`${smallCardStyle}`}>
                        <div className="flex justify-between items-center mb-4">
                            <Clock className="w-6 sm:w-8 h-6 sm:h-8 text-purple-500" />
                            <span className="text-gray-500">시간</span>
                        </div>
                        <div className="text-center">
                            <p className="text-base sm:text-xl font-bold text-gray-700">현재 시간</p>
                            <p className="text-xs sm:text-sm text-gray-500">{new Date().toLocaleTimeString()}</p>
                        </div>
                    </div>

                    <div className={`${smallCardStyle}`}>
                        <div className="flex justify-between items-center mb-4">
                            <PieChart className="w-6 sm:w-8 h-6 sm:h-8 text-red-500" />
                            <span className="text-gray-500">요약</span>
                        </div>
                        <div className="text-center">
                            <p className="text-base sm:text-xl font-bold text-gray-700">총계</p>
                            <p className="text-xs sm:text-sm text-gray-500">준비 중</p>
                        </div>
                    </div>
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-4 sm:gap-8">
                    <div className={`${largeCardStyle}`}>
                        <div className="flex items-center space-x-4 border-b pb-4">
                            <CheckCheck className="w-6 sm:w-8 h-6 sm:h-8 text-indigo-600" />
                            <h3 className="text-lg sm:text-xl font-semibold text-gray-800">주요 뉴스</h3>
                        </div>
                        <p className="text-sm sm:text-base text-gray-600">웹크롤링해서 신문사 4개정도 (썸네일, 제목, 본문, 날짜)</p>
                    </div>

                    <div className={`${largeCardStyle}`}>
                        <div className="flex items-center space-x-4 border-b pb-4">
                            <PieChart className="w-6 sm:w-8 h-6 sm:h-8 text-teal-600" />
                            <h3 className="text-lg sm:text-xl font-semibold text-gray-800">종합 보고서</h3>
                        </div>
                        <p className="text-sm sm:text-base text-gray-600">종합 보고서 내용이 여기에 표시됩니다.</p>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Dashboard;