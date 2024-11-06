import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
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
    fun testFlowOf() = runBlocking {
        val flow = flowOf(1, 2, 3, 4, 5)
            .map { it + 1 }

        flow.collect { println(it) }
    }

    @Test
    fun testAsFlow(){
        val list = listOf(1, 2, 3, 4, 5)
        val flow = list.asFlow()
        runBlocking {
            flow.collect { println(it) }
        }
    }

    @Test
    fun testFlow(){
        val flow = flow {
            for (i in 1..5) {
                emit(i)  // 값 방출
                delay(100)  // 비동기적 처리
            }
        }

        runBlocking {
            flow.collect { value ->
                println(value)
            }
        }
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


    /**
     * Flow 안에서 채널을 사용하여 데이터를 비동기적으로 방출
     * **/
    @Test
    fun testChannelFlow()  {
        var channelFlow = channelFlow {
            for(i in 1..5){
                delay(500)
                send(i)
            }
        }

        runBlocking {
            channelFlow.collect{value ->
                println(value)
            }
        }
    }

    /** Hot stream
     * 상태를 표현하는 데 사용됩니다.
     * 값이 변경될 때마다 최신 상태를 구독자에게 방출합니다.
     * StateFlow는 항상 가장 최근의 값을 가지고 있습니다.
     * **/
    @Test
    fun testMutableStateFlow(){
        runBlocking {
            val stateFlow = MutableStateFlow(0) //초기값

            launch{
                repeat(5){
                    delay(1000)
                    stateFlow.value = it + 1 //값 업데이트
                }
            }

            // collect는 Flow가 방출하는 값을 전부 처리할때까지 기다린다.
            //stateFlow는 상태를 변경할때마다 방출되기 때문에 이 collect는 값이 계속 변경될때마다 실행된다.
/*            stateFlow.collect{value ->
                println(value)
            }*/

            // collect 시, 타임아웃을 걸어서 6초 후에 종료하도록 설정
            withTimeout(6000) {
                stateFlow.collect { value ->
                    println(value)
                }
            }
        }
    }

    /** Hot Stream
     *여러 구독자가 동시에 데이터를 수신할 수 있습니다.
     * 최근의 값을 저장하지 않으며, 값이 방출된 후 구독자가 받지 않으면 그 값은 손실됩니다.
     * 이벤트 기반 데이터 스트림(예: 버튼 클릭, 상태 변경 등)을 처리할 때 유용합니다.
     * **/
    @Test
    fun testMutableSharedFlow(){
        runBlocking {
            val sharedFlow = MutableSharedFlow<Int>()  // 여러 구독자에게 데이터를 보낼 수 있음

            launch{
                for (i in 1..5) {
                    delay(1000)
                    sharedFlow.emit(i)  // 값을 방출
                }
            }

// 구독자 1
            launch {
                sharedFlow.collect { value ->
                    println("Subscriber 1 received: $value")
                }
            }

            // 구독자 2
            launch {
                sharedFlow.collect { value ->
                    println("Subscriber 2 received: $value")
                }
            }


        }
    }
}