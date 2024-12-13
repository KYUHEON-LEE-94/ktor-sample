package com.study.plugins


import com.study.board.model.NoticeMapper
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases() {
    //docker run --name my-postgres -e POSTGRES_USER=myuser -e POSTGRES_PASSWORD=mypassword -e POSTGRES_DB=mydatabase -p 5432:5432 -d postgres
    // psql -U myuser -d mydatabase
    Database.connect(
        "jdbc:postgresql://localhost:5432/myapp",
        user = "user",
        password = "user12#\$"
    )

    transaction {
        // NoticeMapper에 정의된 스키마를 사용하여 테이블을 생성
        SchemaUtils.create(NoticeMapper)
        println("Notice 테이블이 생성되었습니다.")
    }
}

