package com.study.plugins

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*

fun Application.configureRouting() {



    routing {
        // Static plugin. Try to access `/static/index.html`
        staticResources("/static", "static")


    }
}

