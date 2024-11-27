package com.study

import com.study.model.StockInfoRequest
import com.study.model.StockInfoResponse
import com.study.plugins.configureRouting
import com.study.plugins.configureSerialization
import com.study.plugins.configureSockets
import io.ktor.server.testing.*
import kotlin.test.Test
import org.apache.http.impl.client.HttpClients
import org.apache.http.client.methods.HttpGet
import org.apache.http.util.EntityUtils
import kotlinx.serialization.json.Json

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            configureRouting()
            configureSerialization()
            configureSockets()
        }

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
                println("stockInfoResponse: $stockInfoResponse")
            }
        }
    }
}
