import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class CancellationKtTest {

    @Test
    fun `can cancel a coroutine`() {
        cancellableCoroutine()
    }

    @Test
    fun `can cancel a coroutine if exceeds timeout`() {
        cancellableWithTimeout()
    }
}