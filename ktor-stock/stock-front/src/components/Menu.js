import React from 'react';
import { Link } from 'react-router-dom';

function Menu() {
    return (
        <nav className="bg-blue-500 p-4">
            <ul className="flex items-center">
                <li className="mx-4">
                    <Link to="/" className="text-white hover:underline">홈</Link>
                </li>
                <li className="mx-4">
                    <Link to="/stock" className="text-white hover:underline">주식 정보</Link>
                </li>
                <li className="ml-auto">
                    <Link to="/login" className="text-white hover:underline">로그인</Link>
                </li>
            </ul>
        </nav>
    );
}

export default Menu; 