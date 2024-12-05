import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { MapPin, Sun, CloudRain, Thermometer } from 'lucide-react';

function Weather() {
    const [weatherData, setWeatherData] = useState(null);
    const [locationData, setLocationData] = useState(null);
    const [locationInfo, setLocationInfo] = useState(null);

    const getCurrentDateFormatted = () => {
        const today = new Date();
        const year = today.getFullYear();
        const month = String(today.getMonth() + 1).padStart(2, '0');
        const day = String(today.getDate()).padStart(2, '0');

        return `${year}${month}${day}`;
    }

    const getCurrentTimeFormatted = () => {
        const now = new Date();
        const hours = String(now.getHours() - 1).padStart(2, '0');

        return `${hours}00`;
    }

    const formatBaseTime = (baseTime) => {
        if (!baseTime || baseTime.length < 4) return baseTime;
        const hours = baseTime.slice(0, 2);
        const minutes = baseTime.slice(2, 4);
        return `${hours}:${minutes}`;
    };

    useEffect(() => {
        const getLocation = () => {
            if (navigator.geolocation) {
                navigator.geolocation.getCurrentPosition(
                    (position) => {
                        const { latitude, longitude } = position.coords;
                        setLocationData({ 
                            nx: latitude, 
                            ny: longitude, 
                            baseDate: getCurrentDateFormatted(), 
                            basetime: getCurrentTimeFormatted() 
                        });
                    },
                    (error) => {
                        console.error('위치를 가져오는 데 오류가 발생했습니다:', error);
                    }
                );
            } else {
                console.error('Geolocation이 지원되지 않습니다.');
            }
        };

        getLocation();
    }, []);

    useEffect(() => {
        const fetchWeather = async () => {
            if (locationData) {
                try {
                    let nx = locationData.nx
                    let ny = locationData.ny
                    const response = await axios.get(`http://localhost:8081/weather?nx=${nx}&ny=${ny}&base_date=${locationData.baseDate}&base_time=${locationData.basetime}`);

                    setWeatherData(response.data);
                } catch (error) {
                    console.error('날씨 데이터를 가져오는 데 오류가 발생했습니다:', error);
                }
            }
        };

        fetchWeather();
    }, [locationData]);

    useEffect(() => {
        const fetchLocationInfo = async () => {
            if (locationData) {
                try {
                    const response = await axios.get(`https://nominatim.openstreetmap.org/reverse?lat=${locationData.nx}&lon=${locationData.ny}&format=json`);
                    setLocationInfo(response.data.address)
                } catch (error) {
                    console.error('위치 정보를 가져오는 데 오류가 발생했습니다:', error);
                }
            }
        };

        fetchLocationInfo();
    }, [locationData]);

    const renderWeatherIcon = (category) => {
        switch(category) {
            case '강수형태':
                return <CloudRain className="w-6 h-6 text-blue-500" />;
            case '기온':
                return <Thermometer className="w-6 h-6 text-red-500" />;
            default:
                return <Sun className="w-6 h-6 text-yellow-500" />;
        }
    }

    return (
        <div className="bg-white shadow-lg rounded-lg p-6 max-w-sm mx-auto mt-10 transition-all duration-300 hover:shadow-xl hover:scale-105">
            {locationInfo && (
                <div className="flex items-center text-gray-600 mb-4">
                    <MapPin className="w-5 h-5 mr-2" />
                    <p className="text-sm">
                        [{locationInfo.country}] {locationInfo.city}, {locationInfo.road}
                    </p>
                </div>
            )}

            <div className="text-right text-xs text-gray-500 mb-2">
                조회시간: {formatBaseTime(weatherData?.response?.body?.items?.item[0]?.baseTime)}
            </div>

            <div className="space-y-3">
                {weatherData?.response?.body?.items?.item?.map((weather) => {
                    if (weather.category === "강수형태" || weather.category === "기온") {
                        return (
                            <div 
                                key={`weather-${weather.category}`} 
                                className="flex items-center space-x-3 bg-gray-100 p-3 rounded-md"
                            >
                                {renderWeatherIcon(weather.category)}
                                <span className="font-medium text-gray-700">
                                    {weather.category === "강수형태" ? "강수" : "기온"}: 
                                    {weather.category === "기온" ? `${weather.obsrValue}°C` : weather.obsrValue}
                                </span>
                            </div>
                        );
                    }
                    return null;
                })}
            </div>
        </div>
    );
}

export default Weather;