package com.study.plugins

import com.study.model.StockInfoRequest
import com.study.model.StockInfoResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import kotlin.time.Duration

fun Application.configureSockets() {
    install(CORS) {
        allowHost("localhost:3000")
        allowHost("127.0.0.1:3000")

        // 필요한 HTTP 메서드 허용
        allowMethod(HttpMethod.Get)

        // 필요한 헤더 허용
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)

        // WebSocket 관련 설정
        allowNonSimpleContentTypes = true
    }

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

            val request = StockInfoRequest()

            var url = makeUrl(request)

            // WebSocket 연결이 열리면 주기적으로 API 데이터를 가져와서 전송
            while (true) {
                val newStockInfo = fetchStockInfo(url)

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


fun makeUrl(request:StockInfoRequest): String{
    return "https://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService/getStockPriceInfo" +
            "?serviceKey=${request.serviceKey}&numOfRows=${request.numOfRows}&pageNo=${request.pageNo}&resultType=${request.resultType}"
}

fun fetchStockInfo(url:String): StockInfoResponse {


    HttpClients.createDefault().use { httpClient ->
        val httpGet = HttpGet(url)
        httpGet.addHeader("Accept", "application/json")

        httpClient.execute(httpGet).use { response ->
            val entity = response.entity
            val result = EntityUtils.toString(entity)
            println("result $result")
            // JSON 응답 파싱
            return Json.decodeFromString<StockInfoResponse>(result)
        }
    }
}

