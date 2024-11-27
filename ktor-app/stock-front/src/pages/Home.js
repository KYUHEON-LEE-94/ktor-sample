import React from 'react';
import { Link } from 'react-router-dom';
import Dashboard from '../components/Dashboard'

function Home() {
    return (
        <div className="p-4 bg-gray-100 min-h-screen flex flex-col items-center justify-center">
            <Dashboard />
        </div>
    );
}

export default Home; 