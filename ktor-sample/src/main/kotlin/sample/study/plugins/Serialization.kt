package com.example.plugins

import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*

fun Application.configureSerialization() {

    //application.yaml에 정의
    val prettyFlag = environment.config.propertyOrNull("application.response.serialization.pretty")?.getString().toBoolean()

    install(ContentNegotiation) {
        gson {
            if(prettyFlag){
                this.setPrettyPrinting()
            }
        }
    }
}