import kotlin.wasm.WasmImport
import kotlin.wasm.unsafe.Pointer
import kotlin.wasm.unsafe.withScopedMemoryAllocator

@ExperimentalJsExport
@JsExport
fun primeFactorizationJs(numbers: Long) = primeFactorization(numbers, ::readJsArrayFromLinearMemory)

fun primeFactorization(numbers: Long, onResult: (address: Int, size: Int, bytesPerElement: Byte) -> Unit) {
    var n = numbers
    val factors = mutableListOf<Int>()
    for (i in 2L..n) {
        while (n % i == 0L) {
            factors.add(i.toInt())
            n /= i
        }
    }
    if (n > 1L) {
        factors.add(n.toInt())
    }

    transferArrayTo(factors.toTypedArray(), onResult)
}

// Alternative to 'readJsArrayFromLinearMemory' in memoryUtil.kt:
// only one of the following block can be enabled at the same time
// the first one, when compiling for external use
// the second one, when running tests - which is due to the lack of possibility to specify the importObject when running tests

//@WasmImport(module = "imports")
//external fun handlePrimeFactorizationResult(address: UInt, size: Int, bytesPerElement: Byte)

// or

//fun handlePrimeFactorizationResult(address: UInt, size: Int, bytesPerElement: Byte) {
//    // do nothing
//}
