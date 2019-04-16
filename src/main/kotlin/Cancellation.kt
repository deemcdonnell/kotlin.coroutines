import kotlinx.coroutines.*

fun cancellableCoroutine(): Unit {

    runBlocking {
        val job = launch {
            delay(2000L)
            println("World")
        }
        val job2 = launch {
            delay(2000L)
            println("or Dia Duit")
        }
        // job does not check for cancellation
        // also not interrupted by job2
        val startTime = System.currentTimeMillis()
        val longComputationInProgressJob = launch(Dispatchers.Default) {
            try {
                var nextPrintTime = startTime
                var i = 0
                while (i < 10 && isActive) { // computation loop, just wastes CPU
                    // print a message twice a second
                    if (System.currentTimeMillis() >= nextPrintTime) {
                        println("I'm sleeping ${i++} ...")
                        //                   delay(100)
                        nextPrintTime += 500L
                    }
                }
            } finally {
                println("in finally block")
            }
        }

        println("hello")
        job.cancel()
        delay(1300L)
        longComputationInProgressJob.cancelAndJoin()
        println("Now I can quit")

    }
}

fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")

fun cancellableWithTimeout(): Unit = runBlocking {
    withTimeout(1300L) {
        repeat(1000) { i ->
            log("I'm sleeping $i ...")
            delay(500L)
        }
    }
}