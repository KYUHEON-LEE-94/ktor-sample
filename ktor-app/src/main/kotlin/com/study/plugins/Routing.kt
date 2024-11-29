package com.study.plugins

import com.study.weather.model.WeatherRequest
import com.study.weather.service.WeatherService
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.weatherRouting(weatherService: WeatherService) {
    routing {
        staticResources("/static", "static")

        get("/weather") {

            val nx = call.request.queryParameters["nx"]?.toIntOrNull() ?: throw IllegalArgumentException("Parameter 'nx' is required and must be an integer.")
            val ny = call.request.queryParameters["ny"]?.toIntOrNull() ?: throw IllegalArgumentException("Parameter 'ny' is required and must be an integer.")
            val numOfRows = call.request.queryParameters["numOfRows"]?.toLongOrNull() ?: 1000L
            val pageNo = call.request.queryParameters["pageNo"]?.toLongOrNull() ?: 1L

            val request = WeatherRequest(
                nx = nx,
                ny = ny,
                numOfRows = numOfRows,
                pageNo = pageNo
            )
            println("request : $request")
            val response = weatherService.getTodayWeather(request)

            // Send the response back to the client
            call.respond(response)
        }
    }
}

