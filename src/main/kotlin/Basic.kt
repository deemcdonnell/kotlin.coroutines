import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicLong
import kotlin.concurrent.thread

fun main(args: Array<String>) {
    println("1. Hello ${Thread.currentThread()}")
    val job1 = GlobalScope.launch {
        // delay does not block a thread
        delay(1000)
        println("3. Hello ${Thread.currentThread()}")
    }
    val job2 = GlobalScope.launch {
        delay(50)
        println("2. Hello ${Thread.currentThread()}")
    }
    job2.cancel()
    println("isActive: job 1 ${job1.isActive}, job2 ${job2.isActive}")
    Thread.sleep(2000)
    println("4. End ${Thread.currentThread()}")

    val async: Deferred<Unit> = GlobalScope.async {
        delay(1000)
        println("5. Async Hello ${Thread.currentThread()}")
    }

//    runBlocking {
//        async.invokeOnCompletion {
//            println("6. Blocked until async job completed")
//        }
//    }
//
    runBlocking {
        println("async job isComplete = ${async.invokeOnCompletion { }}")
        async.await()
        println("6. Blocked until async job completed")
    }

}

fun createMillionThreads() {

    val c = AtomicLong()

    for (i in 1..1_000_000L)
        thread(start = true) {
            c.addAndGet(i)
        }

    println(c.get())
}


fun createMillionCoroutinesWithoutWaiting() {
    val c = AtomicLong()

    for (i in 1..1_000_000L)
        GlobalScope.launch {
            c.addAndGet(i)
        }

    println(c.get())
}


fun createMillionCoroutines() {
    val c = AtomicLong()
    for (i in 1..1_000_000L) {

        val launch: Deferred<Long> = GlobalScope.async {
            c.addAndGet(i)
        }
    }
    val deferred: List<Deferred<Long>> = (1..1_000_000L).map { n ->
        GlobalScope.async {
            c.addAndGet(n)
        }
    }
    runBlocking {
        val sum = deferred.sumBy { deferred -> deferred.await().toInt() }
        println("Sum: $sum")
    }


    println(c.get())
}

fun basic() {
    println("1. Hello ${Thread.currentThread()}")
    val job1 = GlobalScope.launch {
        // delay does not block a thread
        delay(2000)
        println("2. Hello ${Thread.currentThread()}")
    }

    runBlocking {
        delay(3000)
    }
    println("3 job1.isCompleted = ${job1.isCompleted}")
}


fun basicAlternative() = runBlocking<Unit> {
    println("1. Hello ${Thread.currentThread()}")
    val job1 = GlobalScope.launch {
        // delay does not block a thread
        delay(2000)
        println("2. Hello ${Thread.currentThread()}")
    }

    delay(3000)
    println("3 job1.isCompleted = ${job1.isCompleted}")
}

suspend fun suspendingFunction(){
    GlobalScope.launch {
        delay(2000)
        println("Delayed for 2 seconds")
    }
    delay(3000)
}

// all coroutines must finish
fun blockingFuncInCoroutineScope() = runBlocking { // this: CoroutineScope
    launch { // launch new coroutine in the scope of runBlocking
        doWorld()
    }
    println("Hello,")
}

private suspend fun doWorld() {
    delay(1000L)
    println("World!")
}

fun anotherBlockingFuncInCoroutineScope() = runBlocking { // this: CoroutineScope
    launch {
        delay(200L)
        println("2: Task from runBlocking")
        println(Thread.currentThread())

    }
    println("X: runBLocking itself ")
    println(Thread.currentThread())

    // coRoutineScope does not block the current thread while waiting for all children to complete.
    doSomethingInNewCoroutineScope()

    println("Coroutine scope is over") // This line is not printed until nested launch completes
}

private suspend fun doSomethingInNewCoroutineScope() {
    coroutineScope {
        // Creates a new coroutine scope
        launch {
            delay(500L)
            println("3: Task from nested launch")
            println(Thread.currentThread())
        }

        delay(100L)
        println("1. Task from coroutine scope") // This line will be printed before nested launch
        println(Thread.currentThread())
    }
}

fun largeNumCoroutines(useThreads : Boolean = false) = runBlocking {
    repeat(100_000) { // launch a lot of coroutines
        if (useThreads) {
            thread(start = true) {
                Thread.sleep(1000L)
                print(".")
            }
        }
        else
        {
            launch {
                delay(1000L)
                print(".")
            }
        }
    }
    println()
    repeat(10){ print(it)}
}