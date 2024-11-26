import React from 'react';

function Register() {
    return (
        <div className="p-4 bg-gray-100 min-h-screen flex flex-col items-center justify-center">
            <h1 className="text-2xl font-bold mb-4">회원가입</h1>
            <input
                type="text"
                placeholder="사용자 이름"
                className="border rounded p-2 mb-2"
            />
            <input
                type="email"
                placeholder="이메일"
                className="border rounded p-2 mb-2"
            />
            <input
                type="password"
                placeholder="비밀번호"
                className="border rounded p-2 mb-4"
            />
            <button className="bg-blue-500 text-white rounded px-4 py-2 hover:bg-blue-600">
                회원가입
            </button>
        </div>
    );
}

export default Register; 