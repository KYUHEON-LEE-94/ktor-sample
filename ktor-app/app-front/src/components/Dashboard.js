import React from 'react';
import Weather from '../Dashboard/Weather';

function Dashboard() {
    return (
      <div className="dashboard-content w-4/5 mx-auto p-4">
        <h2 className="text-2xl font-bold mb-4">대시보드 내용</h2>
        {/* 여기에 대시보드 관련 내용을 추가하세요 */}
        <div className="flex flex-row gap-6 mb-6">
          {/* 작은 카드 4개 */}
          <div className="card small-card bg-white p-6 shadow rounded w-1/4">
            <Weather />
          </div>
          
          <div className="card small-card bg-white p-6 shadow rounded w-1/4">작은 카드 2</div>
          <div className="card small-card bg-white p-6 shadow rounded w-1/4">작은 카드 3</div>
          <div className="card small-card bg-white p-6 shadow rounded w-1/4">작은 카드 4</div>
        </div>
        <div className="grid grid-cols-1 gap-6">
          {/* 큰 카드 2개 */}
          <div className="card large-card bg-white p-8 shadow-lg rounded">큰 카드 1</div>
          <div className="card large-card bg-white p-8 shadow-lg rounded">큰 카드 2</div>
        </div>
      </div>
    );
  }

export default Dashboard;