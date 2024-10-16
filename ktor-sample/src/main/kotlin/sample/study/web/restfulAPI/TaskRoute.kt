package sample.study.web.restfulAPI

import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import sample.study.web.model.Priority
import sample.study.web.handleRequestResponse.repo.TaskRepository
import sample.study.web.model.Task
import sample.study.web.restfulAPI.repo.ApiTaskRepository

/**
 * @Description : WriteWeb.java
 * @author      : heon
 * @since       : 24. 10. 10.
 *
 * <pre>
 *
 * << 개정이력(Modification Information) >>
 *
 * ===========================================================
 * 수정인          수정자      수정내용
 * ----------    ----------    --------------------
 * 24. 10. 10.       heon         최초 생성
 *
 * <pre>
 */
fun Route.apiRoutes(){
    staticResources("static", "static")
    route("/tasks/api") {
        get {
            val tasks = ApiTaskRepository.allTasks()
            call.respond(tasks)
        }

        get("/byName/{taskName}") {
            val name = call.parameters["taskName"]
            if (name == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val task = ApiTaskRepository.taskByName(name)
            if (task == null) {
                call.respond(HttpStatusCode.NotFound)
                return@get
            }
            call.respond(task)
        }

        get("/byPriority/{priority}") {
            val priorityAsText = call.parameters["priority"]
            if (priorityAsText == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            try {
                val priority = Priority.valueOf(priorityAsText)
                val tasks = ApiTaskRepository.tasksByPriority(priority)

                if (tasks.isEmpty()) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }
                call.respond(tasks)
            } catch (ex: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        post {
            try {
                val task = call.receive<Task>()
                ApiTaskRepository.addTask(task)
                call.respond(HttpStatusCode.NoContent)
            } catch (ex: IllegalStateException) {
                call.respond(HttpStatusCode.BadRequest)
            } catch (ex: JsonConvertException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        delete("/{taskName}") {
            val name = call.parameters["taskName"]
            if (name == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }

            if (ApiTaskRepository.removeTask(name)) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }



}