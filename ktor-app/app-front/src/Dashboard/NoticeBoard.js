import React, { useState, useEffect } from 'react';
import { useLocation , useNavigate } from 'react-router-dom';

import { 
    Edit2, 
    Trash2, 
    PlusCircle, 
    Save, 
    X, 
    Search 
} from 'lucide-react';

function NoticeBoardPage() {
    const location = useLocation();
    const noticeId = location.state.noticeId;
    console.log(noticeId)

    const navigate = useNavigate();

    const [notices, setNotices] = useState([
        { 
            id: 1, 
            title: '시스템 점검 안내', 
            content: '오는 토요일 새벽 2시부터 4시까지 시스템 점검이 예정되어 있습니다.', 
            author: '관리자', 
            date: '2024-02-15' 
        },
        { 
            id: 2, 
            title: '새로운 기능 업데이트', 
            content: '대시보드에 새로운 분석 기능이 추가되었습니다.', 
            author: '관리자', 
            date: '2024-02-10' 
        },
    ]);

    const [selectedNotice, setSelectedNotice] = useState(null);
    const [isEditing, setIsEditing] = useState(false);
    const [isCreating, setIsCreating] = useState(false);
    const [searchTerm, setSearchTerm] = useState('');

    // URL에서 전달된 noticeId로 공지사항 선택
    useEffect(() => {
        if (noticeId) {
            const notice = notices.find(n => n.id === parseInt(noticeId));
            if (notice) {
                setSelectedNotice(notice);
                setIsEditing(false);
                setIsCreating(false);
            } 
        }
    }, [noticeId, notices, navigate]);

    const handleCreate = () => {
        const newNotice = {
            id: notices.length + 1,
            title: '',
            content: '',
            author: '관리자',
            date: new Date().toISOString().split('T')[0]
        };
        setSelectedNotice(newNotice);
        setIsCreating(true);
    };

    const handleSave = () => {
        if (isCreating) {
            const newNotice = {...selectedNotice, id: notices.length + 1};
            setNotices([...notices, newNotice]);
            // 새로 생성된 공지사항으로 리다이렉트
            navigate(`/notices/${newNotice.id}`);
            setIsCreating(false);
            setSelectedNotice(newNotice);
        } else {
            const updatedNotices = notices.map(notice => 
                notice.id === selectedNotice.id ? selectedNotice : notice
            );
            setNotices(updatedNotices);
            setIsEditing(false);
            setSelectedNotice(selectedNotice);
        }
    };

    const handleDelete = (id) => {
        const updatedNotices = notices.filter(notice => notice.id !== id);
        setNotices(updatedNotices);
        // 삭제 후 목록 페이지로 리다이렉트
        navigate('/notices');
        setSelectedNotice(null);
        setIsEditing(false);
    };

    const handleEdit = (notice) => {
        setSelectedNotice({...notice});
        setIsEditing(true);
    };

    const filteredNotices = notices.filter(notice => 
        notice.title.toLowerCase().includes(searchTerm.toLowerCase())
    );

    return (
        <div className="container mx-auto px-4 py-8 bg-gray-50 min-h-screen">
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-3xl font-bold text-gray-800">공지사항</h1>
                <div className="flex items-center space-x-4">
                    <div className="relative">
                        <input 
                            type="text" 
                            placeholder="공지사항 검색" 
                            className="pl-10 pr-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                        />
                        <Search className="absolute left-3 top-3 text-gray-400" />
                    </div>
                    <button 
                        onClick={handleCreate}
                        className="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 flex items-center"
                    >
                        <PlusCircle className="mr-2" /> 새 공지
                    </button>
                </div>
            </div>

            <div className="grid grid-cols-3 gap-6">
                {/* 공지사항 목록 */}
                <div className="col-span-1 bg-white shadow-md rounded-lg overflow-hidden">
                    <div className="divide-y divide-gray-200">
                        {filteredNotices.map(notice => (
                            <div 
                                key={notice.id} 
                                className={`p-4 hover:bg-gray-100 cursor-pointer ${selectedNotice?.id === notice.id ? 'bg-blue-50' : ''}`}
                                onClick={() => {
                                    navigate(`/notices/${notice.id}`);
                                }}
                            >
                                <div className="flex justify-between items-center">
                                    <h3 className="font-semibold text-gray-800 truncate">{notice.title}</h3>
                                    <span className="text-sm text-gray-500">{notice.date}</span>
                                </div>
                                <p className="text-sm text-gray-600 truncate">{notice.content}</p>
                            </div>
                        ))}
                    </div>
                </div>

                {/* 공지사항 상세/편집 영역 */}
                <div className="col-span-2 bg-white shadow-md rounded-lg p-6">
                    {selectedNotice ? (
                        <div>
                            {isEditing || isCreating ? (
                                <>
                                    <input 
                                        type="text" 
                                        value={selectedNotice.title}
                                        onChange={(e) => setSelectedNotice({...selectedNotice, title: e.target.value})}
                                        placeholder="제목을 입력하세요"
                                        className="w-full border-b mb-4 text-xl font-bold focus:outline-none"
                                    />
                                    <textarea 
                                        value={selectedNotice.content}
                                        onChange={(e) => setSelectedNotice({...selectedNotice, content: e.target.value})}
                                        placeholder="내용을 입력하세요"
                                        className="w-full border rounded-lg p-3 h-64 focus:outline-none focus:ring-2 focus:ring-blue-500"
                                    />
                                    <div className="flex justify-end space-x-2 mt-4">
                                        <button 
                                            onClick={handleSave}
                                            className="bg-green-500 text-white px-4 py-2 rounded-lg hover:bg-green-600 flex items-center"
                                        >
                                            <Save className="mr-2" /> 저장
                                        </button>
                                        <button 
                                            onClick={() => {
                                                setIsEditing(false);
                                                setIsCreating(false);
                                                if (noticeId) {
                                                    navigate(`/notices/${noticeId}`);
                                                } else {
                                                    setSelectedNotice(null);
                                                }
                                            }}
                                            className="bg-red-500 text-white px-4 py-2 rounded-lg hover:bg-red-600 flex items-center"
                                        >
                                            <X className="mr-2" /> 취소
                                        </button>
                                    </div>
                                </>
                            ) : (
                                <>
                                    <div className="flex justify-between items-center mb-6">
                                        <h2 className="text-2xl font-bold">{selectedNotice.title}</h2>
                                        <div className="flex space-x-2">
                                            <button 
                                                onClick={() => handleEdit(selectedNotice)}
                                                className="text-blue-500 hover:bg-blue-50 p-2 rounded-full"
                                            >
                                                <Edit2 />
                                            </button>
                                            <button 
                                                onClick={() => handleDelete(selectedNotice.id)}
                                                className="text-red-500 hover:bg-red-50 p-2 rounded-full"
                                            >
                                                <Trash2 />
                                            </button>
                                        </div>
                                    </div>
                                    <div className="text-gray-700">
                                        <p className="whitespace-pre-line">{selectedNotice.content}</p>
                                    </div>
                                    <div className="mt-4 text-sm text-gray-500">
                                        <span>작성자: {selectedNotice.author}</span>
                                        <span className="ml-4">작성일: {selectedNotice.date}</span>
                                    </div>
                                </>
                            )}
                        </div>
                    ) : (
                        <div className="text-center text-gray-500 py-16">
                            공지사항을 선택해주세요
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}


export default NoticeBoardPage;