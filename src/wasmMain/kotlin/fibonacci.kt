val ZERO: ULong = 0u
val ONE: ULong = 1u

@ExperimentalJsExport
@JsExport
fun fibonacciJs(n: Int): String = fibonacci(n.toULong(), ZERO, ONE).toString()

tailrec fun fibonacci(n: ULong, a: ULong = ZERO, b: ULong = ONE): ULong =
    when (n) {
        ZERO -> a
        ONE -> b
        else -> fibonacci(n - ONE, b, a + b)
    }
