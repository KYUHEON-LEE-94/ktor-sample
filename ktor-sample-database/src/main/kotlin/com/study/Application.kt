package com.study

import com.study.plugins.*
import com.study.repo.FakeTaskRepository
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val repository = FakeTaskRepository()

    configureSerialization(repository)
    configureDatabases()
    configureRouting()
}
