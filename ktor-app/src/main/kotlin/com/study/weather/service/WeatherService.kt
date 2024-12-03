package com.study.weather.service

import com.study.util.GpsTransfer
import com.study.weather.model.PrecipitationType
import com.study.weather.model.WeatherRequest
import com.study.weather.model.WeatherResponse
import kotlinx.serialization.json.Json
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils

/**
 * @Description : WeatherService.java
 * @author      : heon
 * @since       : 24. 11. 29.
 *
 * <pre>
 *
 * << 개정이력(Modification Information) >>
 *
 * ===========================================================
 * 수정인          수정자      수정내용
 * ----------    ----------    --------------------
 * 24. 11. 29.       heon         최초 생성
 *
 * <pre>
 */

class WeatherService {
    fun getTodayWeather(request:WeatherRequest): WeatherResponse {
        val gps = GpsTransfer(request.nx, request.ny) // 위도, 경도
        gps.transfer(gps, 0) // 위경도 -> x, y 좌표
        request.nx = gps.xLat
        request.ny = gps.yLon

        val url = getUrl(request)
        println("WeatherRequest : $url")
        return getResponse(url)

    }

    private fun getUrl(request:WeatherRequest):String{
        return "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst?" +
                "serviceKey=${request.serviceKey}" +
                "&pageNo=${request.pageNo}" +
                "&numOfRows=${request.numOfRows}" +
                "&dataType=${request.dataType}" +
                "&base_date=${request.baseDate}" +
                "&base_time=${request.basetime}" +
                "&nx=${request.nx.toInt()}" +
                "&ny=${request.ny.toInt()}"
    }

    private fun getResponse(url:String):WeatherResponse {
        HttpClients.createDefault().use { httpClient ->
            val httpGet = HttpGet(url)
            httpGet.addHeader("Accept", "application/json")

            httpClient.execute(httpGet).use { response ->
                val entity = response.entity
                val result = EntityUtils.toString(entity)
                val todayWeather = Json.decodeFromString<WeatherResponse>(result)

                val items = todayWeather.response.body.items
                items.item.map {weather ->
                    if(weather.category == "T1H"){
                        weather.category = "기온"
                    }
                    if(weather.category == "PTY"){
                        weather.category = "강수형태"
                        weather.obsrValue = PrecipitationType.fromCode(weather.obsrValue.toInt()).description
                    }

                }
                println(todayWeather)

                return todayWeather
            }
        }
    }
}