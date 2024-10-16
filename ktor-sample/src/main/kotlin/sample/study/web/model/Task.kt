package sample.study.web.model

/**
 * @Description : Task.java
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

data class Task(
    val name: String,
    val description: String,
    val priority: Priority
)

fun Task.taskAsRow() = """
        <tr>
            <td>$name</td><td>$description</td><td>$priority</td>
        </tr>
""".trimIndent()

fun List<Task>.tasksAsTable() = this.joinToString(
    prefix = """
        <table rules="all">
            <thead>
                <tr>
                    <th>Name</th><th>Description</th><th>Priority</th>
                </tr>
            </thead>
            <tbody>
    """.trimIndent(),

    postfix = """
            </tbody>
        </table>
    """.trimIndent(),

    separator = "\n",

    transform = Task::taskAsRow
)