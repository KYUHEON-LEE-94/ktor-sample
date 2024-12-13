package com.study.plugins

import com.study.board.model.Notice
import com.study.util.GpsTransfer
import com.study.util.getCurrentDateFormatted
import com.study.util.getCurrentTimeFormatted
import com.study.weather.model.WeatherRequest
import com.study.weather.service.WeatherService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlin.math.round

fun Application.weatherRouting(weatherService: WeatherService) {
    routing {
        staticResources("/static", "static")

        get("/api/weather") {
            val nx = call.request.queryParameters["nx"]?.toDouble() ?: throw IllegalArgumentException("Parameter 'nx' is required and must be an integer.")
            val ny = call.request.queryParameters["ny"]?.toDouble() ?: throw IllegalArgumentException("Parameter 'ny' is required and must be an integer.")
            val baseTime = call.request.queryParameters["base_time"] ?: getCurrentDateFormatted()
            val baseDate = call.request.queryParameters["base_date"] ?: getCurrentTimeFormatted()

            val request = WeatherRequest(
                nx = nx,
                ny = ny,
                basetime = baseTime,
                baseDate = baseDate
            )
            println("request : $request")
            val response = weatherService.getTodayWeather(request)

            // Send the response back to the client
            call.respond(response)
        }

        post("/api/notice") {
            val postData = call.receive<Notice>()
            println("request : $postData")

            // Send the response back to the client
            call.respond(HttpStatusCode.Created, postData)
        }
    }
}

