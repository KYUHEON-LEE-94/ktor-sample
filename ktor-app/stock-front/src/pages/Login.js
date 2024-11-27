import React from 'react';
import { Link } from 'react-router-dom';

function Login() {
    return (
        <div className="p-8 bg-gray-100 min-h-screen flex flex-col items-center justify-center">
            <h1 className="text-3xl font-bold mb-6">로그인</h1>
            <input
                type="text"
                placeholder="사용자 이름"
                className="border rounded p-3 mb-3 w-80"
            />
            <input
                type="password"
                placeholder="비밀번호"
                className="border rounded p-3 mb-6 w-80"
            />
            <button className="bg-blue-500 text-white rounded px-6 py-3 hover:bg-blue-600">
                로그인
            </button>
            <p className="mt-6">
                아직 회원이 아니신가요?{' '}
                <Link to="/register" className="text-blue-500 hover:underline">
                    회원가입
                </Link>
            </p>
        </div>
    );
}

export default Login; 