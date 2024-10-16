package sample.study.utils

import io.ktor.util.logging.*
import java.io.PrintWriter
import java.io.StringWriter

/**
 * @Description : AppLogger.java
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
internal val AppLog = KtorSimpleLogger("my-app-logger")

fun Throwable.dumpStackTraceAsString():String =
    StringWriter().use{stringWriter ->
        PrintWriter(stringWriter).use { printWriter ->
            this.printStackTrace()
            stringWriter.toString()
        }

    }

fun Logger.outException(th:Throwable){
    error(th.dumpStackTraceAsString())
}