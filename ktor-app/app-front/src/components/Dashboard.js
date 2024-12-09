import React from 'react';
import { Link } from 'react-router-dom'; // Link 임포트
import { Waves, CheckCheck, Clock, PieChart } from 'lucide-react';
import Weather from '../Dashboard/Weather';

function Dashboard() {

    // Axios로 가져와야함
    const notices = [
        { id: 1, title: '시스템 점검 안내', content: '오는 토요일 새벽 2시부터 4시까지 시스템 점검이 예정되어 있습니다.' },
        { id: 2, title: '새로운 기능 업데이트', content: '대시보드에 새로운 분석 기능이 추가되었습니다.' },
        { id: 3, title: '서비스 점검', content: '서비스 점검이 있습니다. 자세한 내용은 공지사항을 확인하세요.' },
    ];



    const smallCardStyle = "bg-white p-6 shadow-md rounded-lg transition-all duration-300 hover:shadow-xl hover:scale-105 flex flex-col";
    const largeCardStyle = "bg-white p-8 shadow-lg rounded-lg space-y-4";

    return (
        <div className="bg-gray-50 min-h-screen py-10">
            <div className="dashboard-content w-11/12 max-w-7xl mx-auto">
                <div className="flex items-center mb-8">
                    <h2 className="text-3xl font-bold text-gray-800 mr-4">대시보드</h2>
                    <span className="text-sm text-gray-500">오늘의 요약</span>
                </div>

                <div className="grid grid-cols-4 gap-6 mb-8">
                    <div className={`${smallCardStyle}`}>
                        <div className="flex justify-between items-center mb-4">
                            <Waves className="w-8 h-8 text-blue-500" />
                            <span className="text-gray-500 text-sm">날씨</span>
                        </div>
                        <Weather />
                    </div>
                    
                    <div className={`${smallCardStyle}`}>
                        <div className="flex justify-between items-center mb-4">
                            <CheckCheck className="w-8 h-8 text-green-500" />
                            <span className="text-gray-500 text-sm">공지</span>
                        </div>
                        {notices.slice(0, 3).map(notice => ( // 최대 3개의 공지사항 표시
                        <Link to="/notice-board" key={notice.id} className="block p-4 bg-white shadow rounded-lg hover:bg-gray-100">
                            <h4 className="font-semibold text-gray-800">{notice.title}</h4>
                            <p className="text-gray-600 overflow-hidden h-8 whitespace-nowrap overflow-ellipsis">{notice.content}</p> {/* 텍스트 넘침 처리 */}
                        </Link>
                    ))}
                    </div>
                    
                    <div className={`${smallCardStyle}`}>
                        <div className="flex justify-between items-center mb-4">
                            <Clock className="w-8 h-8 text-purple-500" />
                            <span className="text-gray-500 text-sm">시간</span>
                        </div>
                        <div className="text-center">
                            <p className="text-xl font-bold text-gray-700">현재 시간</p>
                            <p className="text-sm text-gray-500">{new Date().toLocaleTimeString()}</p>
                        </div>
                    </div>
                    
                    <div className={`${smallCardStyle}`}>
                        <div className="flex justify-between items-center mb-4">
                            <PieChart className="w-8 h-8 text-red-500" />
                            <span className="text-gray-500 text-sm">요약</span>
                        </div>
                        <div className="text-center">
                            <p className="text-xl font-bold text-gray-700">총계</p>
                            <p className="text-sm text-gray-500">준비 중</p>
                        </div>
                    </div>
                </div>

                <div className="grid grid-cols-2 gap-8">
                    <div className={`${largeCardStyle}`}>
                        <div className="flex items-center space-x-4 border-b pb-4">
                            <CheckCheck className="w-8 h-8 text-indigo-600" />
                            <h3 className="text-xl font-semibold text-gray-800">상세 분석</h3>
                        </div>
                        <p className="text-gray-600">데이터 분석 내용이 여기에 표시됩니다.</p>
                    </div>
                    
                    <div className={`${largeCardStyle}`}>
                        <div className="flex items-center space-x-4 border-b pb-4">
                            <PieChart className="w-8 h-8 text-teal-600" />
                            <h3 className="text-xl font-semibold text-gray-800">종합 보고서</h3>
                        </div>
                        <p className="text-gray-600">종합 보고서 내용이 여기에 표시됩니다.</p>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Dashboard;