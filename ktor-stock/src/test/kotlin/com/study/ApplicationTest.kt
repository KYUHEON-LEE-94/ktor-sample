package com.study

import com.study.model.StockInfoRequest
import com.study.model.StockInfoResponse
import com.study.plugins.configureRouting
import com.study.plugins.configureSerialization
import com.study.plugins.configureSockets
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import java.net.URLEncoder
import java.net.http.HttpResponse
import kotlin.test.Test

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            configureRouting()
            configureSerialization()
            configureSockets()
        }

        val request = StockInfoRequest()
        val url = "https://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService/getStockPriceInfo?serviceKey" +
                "=${request.serviceKey}&numOfRows=${request.numOfRows}&pageNo=${request.pageNo}&resultType=${request.resultType}"

        val client = HttpClient{
            install(ContentNegotiation){
                json()
            }
        }

        runBlocking {
            // 요청 보내고 응답 받기
            val response = client.get(url){
                // JSON 응답을 받기 위해 헤더 설정
                headers {
                    append(HttpHeaders.Accept, "application/json")
                }
            }
            val stockInfoResponse:StockInfoResponse = response.body() // 응답을 문자열로 변환
            println("stockInfoResponse: $stockInfoResponse")


        }
    }


}
