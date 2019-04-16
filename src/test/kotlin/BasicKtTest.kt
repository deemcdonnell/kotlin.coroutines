import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit
import kotlin.system.measureTimeMillis

internal class BasicKtTest {

    @org.junit.jupiter.api.Test
    fun `should create 1 million threads`() {
// 1m 24 sec - using threads
        val duration = measureTimeMillis { createMillionThreads() }
        println("duration = ${duration}ms" + TimeUnit.MILLISECONDS.toSeconds(duration))
    }

    @Test
    fun `should create 1 million coroutines faster`() {

        val duration = measureTimeMillis { createMillionCoroutines() }
        println(message = "duration = ${duration}ms")
    }

    @Test
    fun `basic `() {
        basicAlternative()
    }

    @Test
    fun `testMySuspendingFunction  `() = runBlocking{
        suspendingFunction()
    }

    @Test
    fun `outer coroutine does not complete until all the coroutines launched in its scope`() {
        blockingFuncInCoroutineScope()
    }

    @Test
    fun `ex 2 router coroutine does not complete until all the coroutines launched in its scope`() {
        anotherBlockingFuncInCoroutineScope()
    }

    @Test
    // Thread duration = 43923, co-routine duration = 1742
    fun `launch large num of lightweight coroutines - a lot faster than threads`() {
        var duration = measureTimeMillis { largeNumCoroutines() }
        println()
        println("co-routine duration = ${duration}")
        duration = measureTimeMillis { largeNumCoroutines(true) }
        println()
        println("Thread duration = ${duration}")
    }
}