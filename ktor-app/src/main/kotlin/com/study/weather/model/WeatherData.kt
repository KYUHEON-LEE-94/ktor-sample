package com.study.weather.model

/**
 * @Description : WeatherData.java
 * @author      : heon
 * @since       : 24. 12. 3.
 *
 * <pre>
 *
 * << 개정이력(Modification Information) >>
 *
 * ===========================================================
 * 수정인          수정자      수정내용
 * ----------    ----------    --------------------
 * 24. 12. 3.       heon         최초 생성
 *
 * <pre>
 */
data class WeatherData(
    val temperature: Double, // 기온 (℃)
    val precipitation: Double?, // 1시간 강수량 (mm)
    val eastWestWind: Double, // 동서바람 성분 (m/s)
    val northSouthWind: Double, // 남북바람 성분 (m/s)
    val humidity: Int, // 습도 (%)
    val precipitationType: PrecipitationType, // 강수 형태
    val skyCondition: SkyCondition, // 하늘 상태
    val windDirection: Int, // 풍향 (deg)
    val windSpeed: Double // 풍속 (m/s)
)
