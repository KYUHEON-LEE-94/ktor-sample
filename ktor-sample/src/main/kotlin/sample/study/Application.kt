package sample.study

import com.example.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import sample.study.createWeb.configureTemplating
import sample.study.domain.people.employeeRoutes
import sample.study.plugins.*
import sample.study.web.handleRequestResponse.renderRoutes
import sample.study.web.restfulAPI.apiRoutes

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    configureMonitoring()
    configureRouting()
    configureSerialization()

    routing{
        employeeRoutes()
        renderRoutes()
        apiRoutes()
        configureTemplating()
    }
}
