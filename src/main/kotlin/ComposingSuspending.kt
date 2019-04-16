import kotlinx.coroutines.*
import java.lang.NullPointerException
import kotlin.system.measureTimeMillis

suspend fun doSomethingUsefulOne(): Int {
    delay(4000L) // pretend we are doing something useful here
    return 13
}

suspend fun doSomethingUsefulTwo(): Int {
//    throw NullPointerException()
    delay(4000L) // pretend we are doing something useful here, too
    return 29
}

fun doFewThingsConcurrently(): Unit {
    runBlocking {
        val time = measureTimeMillis {


            val doSomethingUsefulTwo = async { doSomethingUsefulTwo() }
            val doSomethingUsefulOne = async { doSomethingUsefulOne() }

            println("total = ${doSomethingUsefulOne.await() + doSomethingUsefulTwo.await()}")

        }
        println("time(ms) = ${time}")
        //        delay(3000)

        println("coroutineContext = ${coroutineContext}")

    }

}

fun doFewThingsConcurrentlyLazily(): Unit {
    runBlocking {
        val time = measureTimeMillis {

            val one = async(start = CoroutineStart.LAZY) { doSomethingUsefulOne() }
            val two = async(start = CoroutineStart.LAZY) { doSomethingUsefulTwo() }

            one.start()
            two.start()

            println("total = ${one.await() + two.await()}")

        }
        println("time(ms) = ${time}")
        //        delay(3000)

        println("coroutineContext = ${coroutineContext}")

    }
}

// use of coroutineScope
suspend fun concurrentSum(): Int = coroutineScope {
    val one = async { doSomethingUsefulOne() }
    val two = async { doSomethingUsefulTwo() }
    one.await() + two.await()
}

fun launchAndDisplayThreadNames() = runBlocking {
    println("main runBlocking no launch  : I'm working in thread ${Thread.currentThread().name}")
    launch {
        // context of the parent, main runBlocking coroutine

        println("main runBlocking launch    : I'm working in thread ${Thread.currentThread().name}")
    }
    launch(Dispatchers.Unconfined) {
        // not confined -- will work with main thread
        println("Unconfined            : I'm working in thread ${Thread.currentThread().name}")
    }
    launch(Dispatchers.Default) {
        // will get dispatched to DefaultDispatcher
        println("Default               : I'm working in thread ${Thread.currentThread().name}")
    }
    launch(newSingleThreadContext("MyOwnThread")) {
        // will get its own new thread
        println("newSingleThreadContext: I'm working in thread ${Thread.currentThread().name}")
    }
}
