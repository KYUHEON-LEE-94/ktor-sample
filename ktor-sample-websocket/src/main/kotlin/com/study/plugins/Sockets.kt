package com.study.plugins

import com.study.model.Priority
import com.study.model.Task
import com.study.repo.TaskRepository
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import java.util.ArrayList
import java.util.Collections
import kotlin.time.Duration

fun Application.configureSockets() {
    install(WebSockets) {
        //WebSocket을 통해 주고받는 메시지를 직렬화 및 역직렬화하기 위해 KotlinxWebsocketSerializationConverter를 사용
        contentConverter = KotlinxWebsocketSerializationConverter(Json)
        pingPeriod = Duration.parse("15s")
        timeout = Duration.parse("15s")
        maxFrameSize = Long.MAX_VALUE
        //Masking은 보안상의 이유로 클라이언트에서 서버로 데이터를 전송할 때 데이터를 난독화하는 방법
        masking = false
    }
    routing {
        // 현재 연결된 모든 클라이언트의 세션을 저장
        val sessions = Collections.synchronizedList<WebSocketServerSession>(ArrayList())

        webSocket("/tasks") {

            sendAllTasks()

            close(CloseReason(CloseReason.Codes.NORMAL, "All done"))
        }

        webSocket("/tasks2") {
            //현재 session을 sessions에 추가
            sessions.add(this)
            sendAllTasks()

            while(true) {
                //클라이언트가 보낸 새 작업을 역직렬화하여 수신
                val newTask = receiveDeserialized<Task>()
                TaskRepository.addTask(newTask)
                for(session in sessions) {
                    session.sendSerialized(newTask)
                }
            }
        }

        webSocket("/delete/{taskName}") {
            val name = call.parameters["taskName"]
            println("taskName: $name")

            if (name == null) {
                outgoing.send(Frame.Text("Invalid task name"))
                close(CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "Invalid task name"))
                return@webSocket
            }

            TaskRepository.removeTask(name)
            //현재 session을 sessions에 추가
            sessions.add(this)
            sendAllTasks()

            while(true) {
                //클라이언트가 보낸 새 작업을 역직렬화하여 수신
                val newTask = receiveDeserialized<Task>()
                TaskRepository.addTask(newTask)
                for(session in sessions) {
                    session.sendSerialized(newTask)
                }
            }
        }
    }


}

private suspend fun DefaultWebSocketServerSession.sendAllTasks() {
    for (task in TaskRepository.allTasks()) {
        sendSerialized(task)
        delay(1000)
    }
}