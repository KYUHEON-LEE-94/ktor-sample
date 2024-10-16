package sample.study.web.handleRequestResponse

import io.ktor.http.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import sample.study.web.model.Priority
import sample.study.web.model.Task
import sample.study.web.model.tasksAsTable
import sample.study.web.handleRequestResponse.repo.TaskRepository

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
fun Route.renderRoutes(){
    staticResources("/task-ui", "task-ui")

    route("/tasks"){
        get("/table") {
            val tasks = TaskRepository.allTasks()

            call.respondText(
                contentType = ContentType.parse("text/html"),
                text = tasks.tasksAsTable()
            )
        }

        get("/by-priority/{priority}") {
            val priorityAsText = call.parameters["priority"]
            if(priorityAsText == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            try {
                val priority = Priority.valueOf(priorityAsText)
                val tasks = TaskRepository.tasksByPriority(priority)

                if(tasks.isEmpty()){
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }

                call.respondText(
                    contentType = ContentType.parse("text/html"),
                    text = tasks.tasksAsTable()
                )
            }catch (ex:IllegalArgumentException){
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        post{
            //receive form data
            val formContent = call.receiveParameters()

            val params = Triple(
                formContent["name"] ?: "",
                formContent["description"] ?: "",
                formContent["priority"] ?: ""
            )

            //to user "any" method. toList()
            if(params.toList().any{it.isEmpty()}){
                call.respond(HttpStatusCode.BadRequest)
                //현재 post 블록 종료
                return@post
            }

            try {
                val priority = Priority.valueOf(params.third)
                TaskRepository.addTask(
                    Task(
                        params.first,
                        params.second,
                        priority
                    )
                )

                call.respond(HttpStatusCode.NoContent)
            } catch (ex: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest)
            } catch (ex: IllegalStateException) {
                call.respond(HttpStatusCode.BadRequest)
            }

        }
    }

}