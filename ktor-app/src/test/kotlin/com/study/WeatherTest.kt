package com.study

import com.study.model.weather.WeatherRequest
import com.study.model.weather.WeatherResponse
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils

class WeatherTest: StringSpec ({
    "Weather 기본 적인 Get 테스트" {
            val request = WeatherRequest()
            request.nx = 55
            request.ny = 127

            println("request: $request")

            val url = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst?" +
                    "serviceKey=${request.serviceKey}" +
                    "&numOfRows=${request.numOfRows}" +
                    "&pageNo=${request.pageNo}" +
                    "&dataType=${request.dataType}"
            "&base_date=${request.baseDate}"
            "&nx=${request.nx}"
            "&ny=${request.ny}"

            println("url: $url")

            // Apache HttpClient 사용
            HttpClients.createDefault().use { httpClient ->
                val httpGet = HttpGet(url)
                httpGet.addHeader("Accept", "application/json")

                httpClient.execute(httpGet).use { response ->
                    val entity = response.entity
                    val result = EntityUtils.toString(entity)
                    println("Raw response: $result")

                    // JSON 응답 파싱
                    val weatherResponse = Json.decodeFromString<WeatherResponse>(result)

                    weatherResponse.response.header.resultCode shouldBe "01"
                }
            }
        }
})

