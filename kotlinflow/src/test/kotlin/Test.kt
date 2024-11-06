import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.time.measureTime

/**
 * @Description : Test.java
 * @author      : heon
 * @since       : 24. 11. 6.
 *
 * <pre>
 *
 * << 개정이력(Modification Information) >>
 *
 * ===========================================================
 * 수정인          수정자      수정내용
 * ----------    ----------    --------------------
 * 24. 11. 6.       heon         최초 생성
 *
 * <pre>
 */
class Test {
    @Test
    fun testFlow() = runBlocking {
        val flow = flowOf(1, 2, 3, 4, 5)
            .map { it + 1 }

        flow.collect { println(it) }
    }

    @Test
    @DisplayName("flow dispatcher를 바꿔주기 위해 사용")
    fun testFlowOn() = runBlocking {
        val time = measureTime {
            val flow = flow{
                for(i in 1..5){
                    emit(i)
                    println("Emitting $i on thread: ${Thread.currentThread().name}")
                    delay(100) // 비동기적으로 딜레이
                }
            }
                .flowOn(Dispatchers.IO) // 이 부분을 IO 스레드에서 실행
                .map { value ->
                    println("Processing $value on thread: ${Thread.currentThread().name}")
                    value * 2
                }

            flow.collect { value ->
                println("Collected $value on thread: ${Thread.currentThread().name}")
            }

        }
    }
}