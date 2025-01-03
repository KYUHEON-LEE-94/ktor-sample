import React, { useMemo, useState, useEffect, useRef } from 'react';
import { useLocation } from 'react-router-dom';
import axios from 'axios';
import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css';

import {
    Save,
    Trash2,
    PlusCircle,
    X,
    Search
} from 'lucide-react';



function NoticeBoardPage() {
    const [newContents, setNewContents] = useState('');
    const [newTitle, setNewTitle] = useState('');
    const [editingContents, setEditingContents] = useState('');
    const [editingTitle, setEditingTitle] = useState('');

    const formats = [
        'header',
        'bold', 'italic', 'underline', 'strike',
        'list', 'bullet',
        'code-block', // code-block 포맷 추가
        'link', 'image'
    ];

    const imageHandler = async () => {
        const input = document.createElement('input');
        input.setAttribute('type', 'file');
        input.setAttribute('accept', 'image/*');
        input.click();

        input.onchange = async () => {
            const file = input.files[0];
            const formData = new FormData();
            formData.append('image', file);

            try {
                const result = await axios.post('http://localhost:8081/api/notice/upload', formData, {
                    headers: {
                        'Content-Type': 'multipart/form-data'
                    }
                });

                // 현재 에디터의 인스턴스를 가져옵니다
                const quill = document.querySelector('.ql-editor').getEditor();
                // 현재 커서 위치를 가져옵니다
                const range = quill.getSelection(true);

                // 서버에서 받은 이미지 URL을 에디터에 삽입합니다
                quill.insertEmbed(range.index, 'image', result.data.imageUrl);
                // 커서를 이미지 다음으로 이동합니다
                quill.setSelection(range.index + 1);
            } catch (error) {
                console.error('이미지 업로드 실패:', error);
            }
        };
    };

    const modules = useMemo(() => {
        return {
            toolbar: {
                container: [
                    [{ header: [1, 2, 3, false] }],
                    ['bold', 'italic', 'underline', 'strike'],
                    [{ list: 'ordered' }, { list: 'bullet' }],
                    ['code-block'], // code-block 버튼 추가
                    ['link', 'image'],
                    ['clean']
                ],
                handlers: { image: imageHandler },
            }
        };
    }, []);

    const location = useLocation();
    //DashBoard 페이지에서 noticeId를 받아옴
    const noticeId = location.state?.noticeId || null;

    const [notices, setNotices] = useState([]);

    const [selectedNotice, setSelectedNotice] = useState(null);
    const [isCreating, setIsCreating] = useState(false);
    const [searchTerm, setSearchTerm] = useState('');
    /*로딩 상태 */
    const [isLoading, setIsLoading] = useState(false);

    // URL에서 전달된 noticeId로 공지사항 선택
    useEffect(() => {
        if (noticeId && notices) {
            const notice = notices.find(n => n.id === parseInt(noticeId));
            if (notice) {
                setSelectedNotice(notice);
                setIsCreating(false);
            }
        } else {
            // noticeId가 없는 경우의 처리
            setSelectedNotice(null);
            setIsCreating(false);
        }
    }, [noticeId, notices]);

    // 컴포넌트 마운트 시 데이터 가져오기
    useEffect(() => {
        getNotices();
    }, []); // 빈 배열을 넣어 컴포넌트 마운트 시 한 번만 실행

    // selectedNotice가 변경될 때 편집용 state 업데이트
    useEffect(() => {
        if (selectedNotice) {
            setEditingContents(selectedNotice.contents);
            setEditingTitle(selectedNotice.title);
        }
    }, [selectedNotice]);

    const saveRequest = async () => {
        if (newContents && newTitle) {  // title도 체크
            try {
                const response = await axios.post('http://localhost:8081/api/notice', {
                    title: newTitle,
                    contents: newContents,
                    author: '관리자',
                    date: new Date().toISOString().replace('T', ' ').split('.')[0]
                });

                console.log(response);
                if (response.status === 200 || response.status === 201) {
                    console.log('공지사항이 성공적으로 저장되었습니다.');
                    // 성공 후 처리
                    setNewContents('');
                    setNewTitle('');  // title도 초기화
                    setIsCreating(false);
                    return response
                }
            } catch (error) {
                console.error('공지사항 저장 중 오류가 발생했습니다:', error);
            }
        }
    };

    const getNotices = async () => {
        try {
            const response = await axios.get('http://localhost:8081/api/notice');
            if (response.status === 200 || response.status === 202) {
                console.log('get Notices success');
                console.log(response.data)
                setNotices(response.data); // 받아온 데이터를 notices state에 저장
            }
        } catch (error) {
            console.error('공지사항을 가져오는 중 오류가 발생했습니다:', error);
        } finally {
            setIsLoading(false);
        }
    };

    const deleteNotice = async (id) => {
        try {
            const response = await axios.delete(`http://localhost:8081/api/notice/${id}`);
            if (response.status === 200 || response.status === 202) {
                console.log('delete Notices success');
                return response;
            }
        } catch (error) {
            console.error('공지사항을 가져오는 중 오류가 발생했습니다:', error);
        } finally {
            setIsLoading(false);
        }
    };


    const handleCreate = () => {
        setNewContents('');
        setNewTitle('');// ReactQuill 내용 초기화

        const newNotice = saveRequest();

        if (notices) setNotices([...notices, newNotice]);
        else setNotices([newNotice]);

        setSelectedNotice(newNotice);
        setIsCreating(true);
    };

    const handleSave = async () => {
        if (isCreating) {
            try {
                const response = await saveRequest();  // 저장 요청 대기

                if (response && (response.status === 200 || response.status === 201)) {
                    await getNotices();  // 서버에서 최신 목록 다시 가져오기
                    setIsCreating(false);
                    setSelectedNotice(null);
                }
            } catch (error) {
                console.error('공지사항 저장 중 오류 발생:', error);
            }
        } else {
            const updatedNotices = notices.map(notice =>
                notice.id === selectedNotice.id ? selectedNotice : notice
            );
            setNotices(updatedNotices);
            setSelectedNotice(selectedNotice);
        }
    };


    const handleUpdate = async () => {

    };


    const handleDelete = async (id) => {
        console.log(id)
        const response = await deleteNotice(id)
        if (response && (response.status === 200 || response.status === 201)) {
            await getNotices();  // 서버에서 최신 목록 다시 가져오기
            setIsCreating(false);
            setSelectedNotice(null);
        }

    };

    /* notices 중에서 title을 기준으로 filter*/
    const filteredNotices = useMemo(() => {
        if (!notices || !Array.isArray(notices)) return [];

        return notices.filter(notice =>
            notice && notice.title && notice.title.includes(searchTerm || '')
        );
    }, [notices, searchTerm]);





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
                        {filteredNotices.map((notice, index) => (
                            <div
                                key={notice.id}
                                className={`p-4 hover:bg-gray-100 cursor-pointer ${selectedNotice && selectedNotice.id === notice.id ? 'bg-blue-50' : ''}`}
                                onClick={() => {
                                    setSelectedNotice(notice);
                                    setIsCreating(false);
                                }}
                            >
                                <div className="flex justify-between items-center" title={notice.contents}>
                                    <h3 className="font-semibold text-gray-800 truncate">{notice.title}</h3>
                                    <span className="text-sm text-gray-500">{notice.date}</span>
                                </div>
                            </div>
                        ))}
                    </div>
                </div>

                {/* 공지사항 상세/편집 영역 */}
                <div className="col-span-2 bg-white shadow-md rounded-lg p-6">
                    {selectedNotice ? (
                        <div>
                            {isCreating ? (
                                <>
                                    <div className="flex justify-between items-center mb-6">
                                        <input className="text-2xl font-bold w-full"
                                            value={newTitle}
                                            onChange={(e) => setNewTitle(e.target.value)}
                                            placeholder='제목을 입력해주세요' />
                                    </div>

                                    <ReactQuill
                                        theme="snow"
                                        modules={modules}
                                        formats={formats}
                                        value={newContents}
                                        onChange={setNewContents}
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
                                                setIsCreating(false);
                                                setSelectedNotice(null);
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
                                        <input
                                            className="text-2xl font-bold w-full"
                                            value={editingTitle}
                                            onChange={(e) => setEditingTitle(e.target.value)}
                                        />
                                        <div className="flex space-x-2">
                                            <button
                                                onClick={() => handleUpdate(selectedNotice)}
                                                className="text-blue-500 hover:bg-blue-50 p-2 rounded-full"
                                                id='modifyButton'
                                            >
                                                <Save />
                                            </button>
                                            <button
                                                onClick={() => handleDelete(selectedNotice.id)}
                                                className="text-red-500 hover:bg-red-50 p-2 rounded-full"
                                            >
                                                <Trash2 />
                                            </button>
                                        </div>
                                    </div>

                                    <ReactQuill
                                        theme="snow"
                                        modules={modules}
                                        formats={formats}
                                        value={editingContents}
                                        onChange={setEditingContents}
                                    />


                                    <div className="mt-4 text-sm text-gray-500">
                                        <span>작성자: {selectedNotice.author}</span>
                                        <span className="ml-4">작성일: {selectedNotice.date}</span>
                                    </div>
                                </>
                            )}
                        </div>
                    ) : (
                        <div className="text-center text-gray-500 py-16">
                            {notices && notices.length > 0
                                ? "공지사항을 선택해주세요"
                                : "등록된 공지사항이 없습니다"}
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}


export default NoticeBoardPage;