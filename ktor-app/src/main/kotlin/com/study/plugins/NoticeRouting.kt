package com.study.plugins

import com.study.board.model.Notice
import com.study.board.service.NoticeService
import java.io.*
import io.ktor.http.*
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.toByteArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun Application.noticeRouting() {

    val NoticeUploadDir = "/notice/uploads" //게시판 이미지 저장 경로

    routing {
        staticResources("/notice/images", "notice/uploads")

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

        get("/api/notice/upload") {
            try {
                val multipart = call.receiveMultipart()
                println("multipart $multipart ")
                var fileName: String? = null

                multipart.forEachPart { part ->
                    when (part) {
                        is PartData.FileItem -> {
                            // 파일 이름 생성 (중복 방지를 위해 UUID 사용)
                            fileName = Uuid.random().toString() + "_" + part.originalFileName
                            println("fileName $fileName ")
                            // 업로드 디렉토리가 없으면 생성
                            val directory = File(NoticeUploadDir)
                            if (!directory.exists()) {
                                directory.mkdirs()
                            }

                            // 파일 저장
                            val file = File(directory, fileName)
                            // ByteReadChannel을 사용하여 파일 저장
                            withContext(Dispatchers.IO) {
                                val channel: ByteReadChannel = part.provider()
                                file.writeBytes(channel.toByteArray())
                            }
                        }
                        else -> Unit // 다른 파트는 처리하지 않음
                    }

                    // 파트를 닫아야 메모리 누수를 방지할 수 있음
                    part.dispose()
                }

                if (fileName != null) {
                    // 이미지 URL 생성
                    val imageUrl = "/notice/images/$fileName" // 클라이언트에서 접근 가능한 URL

                    // 응답 데이터
                    val response = mapOf("imageUrl" to imageUrl)

                    call.respond(HttpStatusCode.OK, response)
                } else {
                    call.respond(HttpStatusCode.BadRequest, "No file uploaded")
                }
            } catch (e: IOException) {
                call.respond(HttpStatusCode.InternalServerError, "Error uploading file: ${e.message}")
            }
        }

    }
}

