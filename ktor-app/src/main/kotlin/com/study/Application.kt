package com.study

import com.study.board.service.NoticeService
import com.study.plugins.*
import com.study.plugins.configureSerialization
import com.study.weather.service.WeatherService
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8081, host = "0.0.0.0", module = Application::module)
        .start(wait = true)


}

fun Application.module() {

    allowRequest()
    configureSerialization()
    configureDatabases()
    configureSockets()
    weatherRouting(WeatherService())
    noticeRouting()

}
