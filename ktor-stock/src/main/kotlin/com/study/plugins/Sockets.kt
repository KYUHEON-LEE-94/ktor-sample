package com.study.plugins

import com.study.model.StockInfoRequest
import com.study.model.StockInfoResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import kotlin.time.Duration

fun Application.configureSockets() {
    install(WebSockets) {
        //WebSocket을 통해 주고받는 메시지를 직렬화 및 역직렬화하기 위해 KotlinxWebsocketSerializationConverter를 사용
        contentConverter = KotlinxWebsocketSerializationConverter(Json)
        pingPeriod = Duration.parse("15s")
        timeout = Duration.parse("15s")
        maxFrameSize = Long.MAX_VALUE
        //Masking은 보안상의 이유로 클라이언트에서 서버로 데이터를 전송할 때 데이터를 난독화하는 방법
        masking = false
    }

    routing {
        // 이전 주가 데이터 저장
        var lastStockInfoList: MutableList<StockInfoResponse> = mutableListOf()
        webSocket("/stock-updates") {
            val client = HttpClient()
            val stockInfoRequest = StockInfoRequest()


            // WebSocket 연결이 열리면 주기적으로 API 데이터를 가져와서 전송
            while (true) {
                val newStockInfo = fetchStockInfo(client, stockInfoRequest)

                for (i in lastStockInfoList.indices) {
                    // 데이터가 변경된 경우에만 클라이언트로 전송
                    if (newStockInfo != lastStockInfoList[i]) {
                        lastStockInfoList[i] = newStockInfo
                        sendSerialized(newStockInfo)
                    }

                    // 2초마다 데이터 확인
                    delay(2000)
                }

            }
        }

    }


}



suspend fun fetchStockInfo(client: HttpClient, request: StockInfoRequest): StockInfoResponse {
    val url = "https://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService/getStockPriceInfo" +
            "?serviceKey=${request.serviceKey}&numOfRows=${request.numOfRows}&pageNo=${request.pageNo}&resultType=${request.resultType}"

    val response = client.get(url){
        headers {
            append(HttpHeaders.Accept, "application/json")
        }
    }

    val stockInfoResponse:StockInfoResponse = response.body() // 응답을 문자열로 변환
    println("stockInfoResponse: $stockInfoResponse")
    return stockInfoResponse
}

