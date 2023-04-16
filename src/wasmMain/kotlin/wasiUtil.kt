import kotlin.wasm.WasmImport
import kotlin.wasm.unsafe.MemoryAllocator
import kotlin.wasm.unsafe.Pointer
import kotlin.wasm.unsafe.withScopedMemoryAllocator

private const val STDOUT = 1

var wasiInitialized = false

@ExperimentalJsExport
@JsExport
fun printToStdout(line: String) {
    if (!wasiInitialized) {
        wasiInitialized = true
    }
    println(line)
}

fun println(x: Any?) {
    fd_write(STDOUT, listOf(x.toString().encodeToByteArray(), "\n".encodeToByteArray()))
}

fun fd_write(fd: Int, ivos: List<ByteArray>): Int {
    withScopedMemoryAllocator { allocator ->
        val iovs = ivos.map { __unsafe__Ciovec(allocator.writeToLinearMemory(it), it.size) }
        val res = __unsafe__fd_write(allocator, fd, iovs)
        return res
    }
}

internal fun MemoryAllocator.writeToLinearMemory(array: ByteArray): Pointer {
    val ptr = allocate(array.size)
    var currentPtr = ptr
    for (el in array) {
        currentPtr.storeByte(el)
        currentPtr += 1
    }
    return ptr
}

internal fun MemoryAllocator.writeToLinearMemory(array: List<__unsafe__Ciovec>): Pointer {
    val ptr = allocate(array.size * 8)
    var currentPtr = ptr
    for (el in array) {
        __store___unsafe__Ciovec(el, currentPtr)
        currentPtr += 8
    }
    return ptr
}

/** Write to a file descriptor. Note: This is similar to `writev` in POSIX. */
///
/// ## Parameters
///
/// * `iovs` - List of scatter/gather vectors from which to retrieve data.
internal fun __unsafe__fd_write(
    allocator: MemoryAllocator,
    fd: Int,
    iovs: List<__unsafe__Ciovec>,
): Int {
    val rp0 = allocator.allocate(4)
    val ret =
        _raw_fd_write(
            fd,
            allocator.writeToLinearMemory(iovs).address.toInt(),
            iovs.size,
            rp0.address.toInt())
    return if (ret == 0) {
        (Pointer(rp0.address.toInt().toUInt())).loadInt()
    } else {
        throw Throwable("WASI Error - return code $ret")
    }
}

@WasmImport("wasi_snapshot_preview1", "fd_write")
external fun _raw_fd_write(a: Int, b: Int, c: Int, d: Int): Int

internal fun __store___unsafe__Ciovec(x: __unsafe__Ciovec, ptr: Pointer) {
    (ptr + 0).storeInt(x.buf.address.toInt())
    (ptr + 4).storeInt(x.buf_len)
}

internal data class __unsafe__Ciovec(
    /** The address of the buffer to be written. */
    var buf: Pointer,
    /** The length of the buffer to be written. */
    var buf_len: Int,
)
