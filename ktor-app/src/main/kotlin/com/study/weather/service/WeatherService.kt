package com.study.weather.service

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
      var url = getUrl(request)
      //url = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst?serviceKey=uTFBpP%2FqAk5lRHyQWvQ%2Fvpo9rC2vZS9OxT7SNCf78IoN%2BWb3w6yCi5AXwmtWx8%2F5IgKRpKVAX5L8VnOPjuFVjw%3D%3D&pageNo=1&numOfRows=1000&dataType=json&base_date=20241130&base_time=0600&nx=37&ny=127"

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
                "&nx=${request.nx}" +
                "&ny=${request.ny}"
    }

    private fun getResponse(url:String):WeatherResponse {
        HttpClients.createDefault().use { httpClient ->
            val httpGet = HttpGet(url)
            httpGet.addHeader("Accept", "application/json")

            httpClient.execute(httpGet).use { response ->
                val entity = response.entity
                val result = EntityUtils.toString(entity)
                val decodeFromString = Json.decodeFromString<WeatherResponse>(result)

                return decodeFromString
            }
        }
    }
}