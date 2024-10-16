package com.study

import com.study.model.Priority
import com.study.model.Task
import com.study.plugins.configureRouting
import com.study.plugins.configureSerialization
import com.study.plugins.configureSockets
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.*
import io.ktor.serialization.kotlinx.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.scan
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            configureRouting()
            configureSerialization()
            configureSockets()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }

            install(WebSockets){
                contentConverter= KotlinxWebsocketSerializationConverter(Json)
            }
        }


        val expectedTasks = listOf(
            Task("cleaning", "Clean the house", Priority.Low),
            Task("gardening", "Mow the lawn", Priority.Medium),
            Task("shopping", "Buy the groceries", Priority.High),
            Task("painting", "Paint the fence", Priority.Medium)
        )

        var actualTasks = emptyList<Task>()

        client.webSocket("/tasks") {
            consumeTasksAsFlow().collect { allTasks ->
                actualTasks = allTasks
            }
        }

        assertEquals(expectedTasks.size, actualTasks.size)
        expectedTasks.forEachIndexed { index, task ->
            assertEquals(task, actualTasks[index])
        }
    }

    private fun DefaultClientWebSocketSession.consumeTasksAsFlow() = incoming //WebSocket으로 들어오는 메시지들을 의미
        .consumeAsFlow() // incoming 채널에서 들어오는 데이터를 Flow로 변환
        .map {
            converter!!.deserialize<Task>(it)
        }
        //누적된 상태를 유지하면서 데이터를 처리하는 연산자
        .scan(emptyList<Task>()){list, task ->
            //새로운 Task 객체가 들어올 때마다 기존의 list에 새 task를 추가
            list + task
        }
}
