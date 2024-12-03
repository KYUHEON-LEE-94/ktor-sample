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
                    let nx = locationData.nx
                    let ny = locationData.ny
                    const response = await axios.get(`http://localhost:8081/weather?nx=${nx}&ny=${ny}&base_date=${locationData.baseDate}&base_time=${locationData.basetime}`);

                    setWeatherData(response.data); // 응답 데이터를 상태에 저장
                    
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
                    // console.log(response.data)

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
            {locationInfo && locationInfo?.city && locationInfo?.road && ( // locationInfo가 null이 아닐 때만 렌더링
                <div className="text-right text-gray-500 text-sm"> {/* Tailwind CSS 클래스 추가 */}
                    <p>[{locationInfo.country}] {locationInfo.city}, {locationInfo.road}</p> {/* 도시와 도로 출력 */}
                </div>
            )}

        {
        
        weatherData?.response?.body?.items?.item?.map((weather) => {
            console.log(weatherData)
            if (weather.category === "강수형태") {
                return <div key={`강수-${weather.obsrValue}`}>강수형태: {weather.obsrValue}</div>;
            }
            if (weather.category === "기온") {
                return <div key={`기온-${weather.obsrValue}`}>기온: {weather.obsrValue}°C</div>;
            }
            return null; // 조건에 맞지 않는 경우 렌더링하지 않음
        })
        }
        </div>
    );
}

export default Weather;