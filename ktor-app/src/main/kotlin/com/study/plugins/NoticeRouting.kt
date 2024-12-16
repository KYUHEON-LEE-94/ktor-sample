package com.study.plugins

import com.study.board.model.Notice
import com.study.board.model.NoticeMapper
import com.study.board.service.NoticeService
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.math.round

fun Application.noticeRouting() {
    routing {

        post("/api/notice") {
            val postData = call.receive<Notice>()
            println("request : $postData")

            try {
                runBlocking {
                    println("runBlocking Start")
                    val savedNotice = NoticeService.saveNotice(postData)
                    println("저장 : $savedNotice")

                    call.respond(HttpStatusCode.Created, savedNotice)
                }
            } catch (e: Exception) {
                println("Error occurred: ${e.message}")
                call.respond(HttpStatusCode.InternalServerError, "Error occurred while saving notice")
            }
        }


        get("/api/notice") {
            try {
                runBlocking {
                    println("runBlocking Start")
                    val notices = NoticeService.allNotices()
                    println("savedNotice : $notices")

                    call.respond(HttpStatusCode.OK, notices)
                }
            } catch (e: Exception) {
                println("Error occurred: ${e.message}")
                call.respond(HttpStatusCode.InternalServerError, "Error occurred while saving notice")
            }
        }
    }
}

