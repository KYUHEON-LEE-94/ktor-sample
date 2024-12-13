package com.study

import com.study.board.model.Notice
import com.study.board.model.NoticeMapper
import com.study.board.service.NoticeService
import com.study.util.getCurrentDateFormatted
import com.study.weather.model.PrecipitationType
import com.study.weather.model.WeatherRequest
import com.study.weather.service.WeatherService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction


class NoticeTest: StringSpec ({
    "Notice 저장 테스트" {

        // 데이터베이스 연결 (PostgreSQL)
        Database.connect(
            "jdbc:postgresql://localhost:5432/myapp",
            driver = "org.postgresql.Driver",
            user = "user",
            password = "user12#\$"
        )

        // 트랜잭션 내에서 테이블 생성
        transaction {
            SchemaUtils.create(NoticeMapper)

            val request = Notice()
            request.id
            request.title = "공지사항 제목일까 Test"
            request.contents = "공지사항 내용입니다."
            request.author = "관리자"
            request.date = getCurrentDateFormatted()

            val service = NoticeService()

            // Insert 기능 테스트
            val newNotice = service.saveNotice(
                request
            )

            rollback()
        }





    }
})

