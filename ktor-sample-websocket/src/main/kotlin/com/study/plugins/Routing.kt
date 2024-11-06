package com.study.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

fun Application.configureRouting() {
    routing {
        // Static plugin. Try to access `/static/index.html`
        staticResources("/static", "static")

        get("/") {
            call.respondText("Hello World!")
        }

        get("/stream") {
            val numberFlow = flow {
                for (i in 1..5) {
                    emit(i)
                    delay(1000)
                }
            }
        }
    }
}

