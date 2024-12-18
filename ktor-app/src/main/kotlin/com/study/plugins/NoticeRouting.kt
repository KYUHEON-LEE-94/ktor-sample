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
        delete("/api/notice/{id}") {
            // 1️⃣ id를 경로 파라미터로 가져옵니다.
            val id = call.parameters["id"]?.toIntOrNull()
            println("Deleted ID : $id")

            // 2️⃣ id가 null이면 400 Bad Request 응답을 반환합니다.
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid or missing ID")
                return@delete
            }

            try {
                // 3️⃣ 삭제 작업을 비동기 처리합니다.
                val rowsDeleted = NoticeService.deleteNotice(id)

                if (rowsDeleted > 0) {
                    call.respond(HttpStatusCode.OK, "Notice with ID $id has been deleted successfully")
                } else {
                    call.respond(HttpStatusCode.NotFound, "Notice with ID $id not found")
                }
            } catch (e: Exception) {
                println("Error occurred: ${e.message}")
                call.respond(HttpStatusCode.InternalServerError, "Error occurred while deleting notice")
            }
        }

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
                // Query Parameter에서 "limit" 값을 가져오고, 없으면 기본값 10으로 설정
                val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10
                println("limit : $limit")

                runBlocking {
                    val notices = NoticeService.allNotices(limit)
                    call.respond(HttpStatusCode.OK, notices)
                }
            } catch (e: Exception) {
                println("Error occurred: ${e.message}")
                call.respond(HttpStatusCode.InternalServerError, "Error occurred while retrieving notices")
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

