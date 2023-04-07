import kotlin.test.Test
import kotlin.test.BeforeTest
import kotlin.test.Ignore
import kotlin.test.assertTrue
import kotlin.wasm.unsafe.Pointer

@ExperimentalJsExport
class PrimeFactorizationTests {

    @Test
    fun primeFactorsByte() {
        primeFactorization(44L) { pointer: Int, size, bytesPerElement ->
            val result = load(pointer, size, bytesPerElement)
            val numbers = result.map { it.toString() }
            println("44 Test: $numbers")
            assertTrue("2" ==  numbers[0])
            assertTrue("2" ==  numbers[1])
            assertTrue("11" ==  numbers[2])
        }
    }

    @Test
    fun primeFactorsShort() {
        primeFactorization(12345L) { pointer, size, bytesPerElement ->
            val result = load(pointer, size, bytesPerElement)
            val numbers = result.map { it.toString() }
            println("12345 Test: $numbers")
            assertTrue("3" ==  numbers[0])
            assertTrue("5" ==  numbers[1])
            assertTrue("823" ==  numbers[2])
        }
    }

    @Test
    fun primeFactorsInt() {
        primeFactorization(123456789L) { pointer, size, bytesPerElement ->
            val result = load(pointer, size, bytesPerElement)
            val numbers = result.map { it.toString() }
            println("123456789 Test: $numbers")
            assertTrue("3" ==  numbers[0])
            assertTrue("3" ==  numbers[1])
            assertTrue("3607" ==  numbers[2])
            assertTrue("3803" ==  numbers[3])
        }
    }

    @Test
    @Ignore
    fun primeFactorsLong() {
        primeFactorization(2_234_456_678L) { pointer, size, bytesPerElement ->
            val result = load(pointer, size, bytesPerElement)
            val numbers = result.map { it.toString() }
            println("2,234,456,678 Test: $numbers")
            assertTrue("2" ==  numbers[0])
            assertTrue("2423" ==  numbers[1])
            assertTrue("461093" ==  numbers[2])
        }
    }

    private fun load(pointer: Int, size: Int, bytesPerElement: Byte): Array<*> =
        when(bytesPerElement) {
            1.toByte() -> ByteArray(size) { i -> (Pointer(pointer.toUInt()) + i * bytesPerElement).loadByte() }.toTypedArray()
            2.toByte() -> ShortArray(size) { i -> (Pointer(pointer.toUInt()) + i * bytesPerElement).loadShort() }.toTypedArray()
            4.toByte() -> IntArray(size) { i -> (Pointer(pointer.toUInt()) + i * bytesPerElement).loadInt() }.toTypedArray()
            else -> LongArray(size) { i -> (Pointer(pointer.toUInt()) + i * bytesPerElement).loadLong() }.toTypedArray()
        }
}

@JsFun("""
    (s) => console.log(s)
""")
external fun println(s: String)
