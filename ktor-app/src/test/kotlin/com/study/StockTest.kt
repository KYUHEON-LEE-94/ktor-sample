package com.study

import com.study.model.stock.StockInfoRequest
import com.study.model.stock.StockInfoResponse
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils

class StockTest:StringSpec ({

    "기본적인 주식 테스트" {
        val request = StockInfoRequest()
        println("request: $request")
        val url = "https://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService/getStockPriceInfo?serviceKey" +
                "=${request.serviceKey}&numOfRows=${request.numOfRows}&pageNo=${request.pageNo}&resultType=${request.resultType}"

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
                val stockInfoResponse = Json.decodeFromString<StockInfoResponse>(result)
                stockInfoResponse.response.header.resultCode shouldBe "01"
            }
        }
    }

})
