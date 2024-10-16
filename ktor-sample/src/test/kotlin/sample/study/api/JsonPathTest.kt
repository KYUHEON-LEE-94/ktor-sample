package sample.study.api


import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.JsonPath
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import sample.study.module
import sample.study.web.model.Priority
import sample.study.web.model.Task
import kotlin.test.*
/**
 * @Description : JsonPathTest.java
 * @author      : heon
 * @since       : 24. 10. 14.
 *
 * <pre>
 *
 * << 개정이력(Modification Information) >>
 *
 * ===========================================================
 * 수정인          수정자      수정내용
 * ----------    ----------    --------------------
 * 24. 10. 14.       heon         최초 생성
 *
 * <pre>
 */
class JsonPathTest {
    @Test
    fun tasksCanBeFound() = testApplication {
        application {
            module()
        }

        val response = client.get("/tasks/api")
        val jsonResponse = response.bodyAsText()

        val result = JsonPath.read<List<String>>(jsonResponse, "$[*].name")
        assertEquals("cleaning", result[0])
        assertEquals("gardening", result[1])
        assertEquals("shopping", result[2])
    }

    @Test
    fun tasksCanBeFoundByPriority() = testApplication {
        application {
            module()
        }
        val priority = Priority.Medium
//        val response = client.get("/tasks/api/byPriority/$priority")
//        val jsonResponse = response.bodyAsText()
//        val result = JsonPath.read<List<String>>(jsonResponse, "$[?(@.priority == '$priority')].name")

        val jsonDoc = client.getAsJsonPath("/tasks/api/byPriority/$priority")
        val result: List<String> =
            jsonDoc.read("$[?(@.priority == '$priority')].name")

        assertEquals(2, result.size)

        assertEquals("gardening", result[0])
        assertEquals("painting", result[1])
    }

    suspend fun HttpClient.getAsJsonPath(url: String): DocumentContext {
        val response = this.get(url) {
            accept(ContentType.Application.Json)
        }
        return JsonPath.parse(response.bodyAsText())
    }
}