import React from 'react';
import { Link } from 'react-router-dom';
import { Home, TrendingUp, LogIn } from 'lucide-react';

function Menu() {
    return (
        <nav className="bg-gradient-to-r from-indigo-600 to-blue-500 shadow-md">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div className="flex items-center justify-between h-16">
                    <div className="flex items-center">
                        <div className="flex-shrink-0">
                            {/* 로고나 앱 이름을 추가할 수 있는 공간 */}
                            <span className="text-white font-bold text-xl">MyApp</span>
                        </div>
                        <div className="ml-10 flex items-baseline space-x-4">
                            <Link 
                                to="/" 
                                className="text-white hover:bg-indigo-700 hover:text-white px-3 py-2 rounded-md flex items-center space-x-2 transition-all duration-300"
                            >
                                <Home className="w-5 h-5" />
                                <span>홈</span>
                            </Link>
                            <Link 
                                to="/stock" 
                                className="text-white hover:bg-indigo-700 hover:text-white px-3 py-2 rounded-md flex items-center space-x-2 transition-all duration-300"
                            >
                                <TrendingUp className="w-5 h-5" />
                                <span>주식 정보</span>
                            </Link>
                        </div>
                    </div>
                    <div>
                        <Link 
                            to="/login" 
                            className="text-white hover:bg-indigo-700 hover:text-white px-3 py-2 rounded-md flex items-center space-x-2 transition-all duration-300"
                        >
                            <LogIn className="w-5 h-5" />
                            <span>로그인</span>
                        </Link>
                    </div>
                </div>
            </div>
        </nav>
    );
}

export default Menu;