import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

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
    fun testCoroutine() = runBlocking<Unit> {
        val flow = flowOf(1, 2, 3, 4, 5)
            .map { it + 1 }

        flow.collect { println(it) }
    }
}