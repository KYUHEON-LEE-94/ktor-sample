import React, { useEffect, useState } from 'react';
import axios from 'axios';

function Weather() {
    const [weatherData, setWeatherData] = useState(null); // 날씨 데이터를 저장할 상태
    const [locationData, setLocationData] = useState(null); //내 위치 좌표
    const [locationInfo, setLocationInfo] = useState(null); //내위치 국가,도시, 길 정보

    function getCurrentDateFormatted() {
        const today = new Date();
        const year = today.getFullYear();
        const month = String(today.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1
        const day = String(today.getDate()).padStart(2, '0');

        return `${year}${month}${day}`; // YYYYMMDD 형식
    }

    function getCurrentTimeFormatted() {
        const now = new Date();
        const hours = String(now.getHours() - 2).padStart(2, '0'); // 24시간 형식

        return `${hours}00`; // HHmm 형식
    }

    useEffect(() => {
        // Geolocation API를 사용하여 현재 위치 가져오기
        const getLocation = () => {
            if (navigator.geolocation) {
                navigator.geolocation.getCurrentPosition(
                    (position) => {
                        const { latitude, longitude } = position.coords;
                        console.log(latitude, longitude); // 정수값 출력
                        setLocationData({ nx: latitude, ny: longitude, baseDate: getCurrentDateFormatted(), basetime: getCurrentTimeFormatted() }); // 위치 상태 업데이트
                    },
                    (error) => {
                        console.error('위치를 가져오는 데 오류가 발생했습니다:', error);
                    }
                );
            } else {
                console.error('Geolocation이 지원되지 않습니다.');
            }
        };

        getLocation(); // 위치 가져오기 함수 실행
    }, []); // 컴포넌트가 마운트될 때 한 번만 실행

    useEffect(() => {
        // 위치 정보가 있을 때 날씨 API 호출
        const fetchWeather = async () => {
            if (locationData) {
                try {
                    let nx = Math.floor(locationData.nx)
                    let ny = Math.floor(locationData.ny)
                    const response = await axios.get(`http://localhost:8081/weather?nx=${nx}&ny=${ny}&base_date=${locationData.baseDate}&base_time=${locationData.basetime}`);

                    setWeatherData(response.data); // 응답 데이터를 상태에 저장
                    console.log(response.data)
                } catch (error) {
                    console.error('날씨 데이터를 가져오는 데 오류가 발생했습니다:', error);
                }
            }
        };

        fetchWeather(); // 날씨 API 호출 함수 실행
    }, [locationData]); // 위치 정보가 변경될 때마다 실행

    useEffect(() => {
        // 위치 정보가 있을 때 도시와 국가 정보 가져오기
        const fetchLocationInfo = async () => {
            if (locationData) {
                try {
                    const response = await axios.get(`https://nominatim.openstreetmap.org/reverse?lat=${locationData.nx}&lon=${locationData.ny}&format=json`);
                    console.log(response.data)

                    setLocationInfo(response.data.address)
                } catch (error) {
                    console.error('위치 정보를 가져오는 데 오류가 발생했습니다:', error);
                }
            }
        };

        fetchLocationInfo(); // 도시와 국가 정보 가져오기 함수 실행
    }, [locationData]); // 위치 정보가 변경될 때마다 실행

    return (
        <div>
            {/* 날씨 정보를 여기에 추가하세요 */}
            <h3>날씨 정보</h3>
            {locationInfo && locationInfo.city && locationInfo.road && ( // locationInfo가 null이 아닐 때만 렌더링
                <div>
                    <p>현재 위치: {locationInfo.city}, {locationInfo.road}</p> {/* 도시와 도로 출력 */}
                </div>
            )}
            
        </div>
    );
}

export default Weather;