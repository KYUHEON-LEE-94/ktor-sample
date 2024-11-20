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
import io.ktor.websocket.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull
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

            var request = StockInfoRequest()

            // WebSocket 연결이 열리면 주기적으로 API 데이터를 가져와서 전송
            while (true) {
            // 클라이언트로부터 메시지 수신 - 메시지를 받을 떄까지 대기함
                try {
                    val message = withTimeoutOrNull(2000) { // 2초 동안 메시지를 기다림
                        incoming.receive() // 메시지 수신 대기
                    }

                    if (message != null && message is Frame.Text) {
                        // 수신한 메시지를 JSON으로 파싱
                        val pageRequest = Json.decodeFromString<StockInfoRequest>(message.readText())
                        println("pageRequest $pageRequest")
                        request = request.copy(pageNo = pageRequest.pageNo) // 페이지 번호 업데이트
                    } else {
                        println("No message received, using default request: $request")
                    }
                } catch (e: Exception) {
                    println("Error while receiving message: ${e.message}")
                }

                var url = makeUrl(request)

                val newStockInfo = fetchStockInfo(url)
                println("result $newStockInfo")

                if (lastStockInfoList.isEmpty()) lastStockInfoList.add(newStockInfo)

                for (i in lastStockInfoList.indices) {
                    // 데이터가 변경된 경우에만 클라이언트로 전송
                    if (newStockInfo != lastStockInfoList[i] || lastStockInfoList.size == 1) {
                        lastStockInfoList[i] = newStockInfo
                        sendSerialized(newStockInfo)

                    }else{
                        println("Duplicated Info")
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

            // JSON 응답 파싱
            return Json.decodeFromString<StockInfoResponse>(result)
        }
    }
}

