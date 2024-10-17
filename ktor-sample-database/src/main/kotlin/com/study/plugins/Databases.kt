package com.study.plugins


import io.ktor.server.application.*
import org.jetbrains.exposed.sql.*

fun Application.configureDatabases() {
    //docker run --name my-postgres -e POSTGRES_USER=myuser -e POSTGRES_PASSWORD=mypassword -e POSTGRES_DB=mydatabase -p 5432:5432 -d postgres
    // psql -U myuser -d mydatabase
    Database.connect(
        "jdbc:postgresql://localhost:5432/mydatabase",
        user = "myuser",
        password = "mypassword"
    )
}

