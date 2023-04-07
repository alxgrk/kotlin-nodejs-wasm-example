import kotlin.test.Test
import kotlin.test.assertTrue

@kotlin.js.ExperimentalJsExport
class FibonacciTests {
    @Test
    fun fibonacci1() {
        assertTrue(ONE == fibonacci(1u))
    }

    @Test
    fun fibonacci2() {
        assertTrue(ONE == fibonacci(2u))
    }

    @Test
    fun fibonacci3() {
        val two: ULong = 2u
        assertTrue(two == fibonacci(3u))
    }

    @Test
    fun fibonacci40() {
        val result = fibonacci(40u)
        println(result)
        val highNumber: ULong = 102334155u
        assertTrue(highNumber == result)
    }
}
