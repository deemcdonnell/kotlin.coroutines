import kotlinx.coroutines.async
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Test
import java.lang.Exception
import kotlin.system.measureTimeMillis

internal class ComposingSuspendingKtTest {

    @Test
    fun `doFewThings - compose kotlin coroutines`() {
        doFewThingsConcurrently()
    }

    @Test
    fun `doFewThings Lazily - compose kotlin coroutines`() {
        doFewThingsConcurrentlyLazily()
    }

    @Test
    fun `concurrent compute method which has 2 co-routines`() = runBlocking {
        var sum = 0
        val measureTimeMillis = measureTimeMillis {
            try {

                sum = concurrentSum()
            } catch (e: Exception) {
                println("sum failed e = ${e}")
            }
        }
        println("sum = $sum, took  $measureTimeMillis ms")
    }

    @Test
    fun `launch with different dispatch threads`() {
        println("main       : I'm working in thread ${Thread.currentThread().name}")

        launchAndDisplayThreadNames()
    }

    @Test
    fun `run 2 async methods`() = runBlocking<Unit> {
        //sampleStart
        val a = async {
            log("I'm computing a piece of the answer")
            6
        }
        val b = async {
            log("I'm computing another piece of the answer")
            7
        }
        log("The answer is ${a.await() * b.await()}")
//sampleEnd
    }

    @Test
    fun `try newSingleThreadContext`() {
//sampleStart
        newSingleThreadContext("Ctx1").use { ctx1 ->
            newSingleThreadContext("Ctx2").use { ctx2 ->
                runBlocking(ctx1) {
                    log("Started in ctx1")
                    withContext(ctx2) {
                        log("Working in ctx2")
                    }
                    log("Back to ctx1")
                }
            }
        }
//sampleEnd
    }
}