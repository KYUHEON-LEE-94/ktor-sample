package com.study

import com.study.weather.model.PrecipitationType
import com.study.weather.model.WeatherRequest
import com.study.weather.service.WeatherService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe


class WeatherTest: StringSpec ({
    "Weather 기본 적인 Get 테스트" {
            val request = WeatherRequest()
            request.nx = 37.5399238
            request.ny = 127.0507572

        val service = WeatherService()

        val todayWeather = service.getTodayWeather(request)

        todayWeather.response.header.resultCode shouldBe "00"
    }
})

