package sample.study.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import sample.study.utils.AppLog

fun Application.configureRouting() {


    routing {
        get("/") {
            call.respond(mapOf("hello" to "world"))
        }
    }

}
