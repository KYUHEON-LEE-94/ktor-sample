package com.study.plugins

import com.study.stock.model.StockInfoRequest
import com.study.stock.model.StockInfoResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.serialization.json.Json
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import kotlin.time.Duration

fun Application.allowRequest() {
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


}






