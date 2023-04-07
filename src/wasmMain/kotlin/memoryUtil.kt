import kotlin.wasm.unsafe.MemoryAllocator
import kotlin.wasm.unsafe.Pointer
import kotlin.wasm.unsafe.withScopedMemoryAllocator

internal fun transferArrayTo(array: Array<Int>, onResult: (address: Int, size: Int, bytesPerElement: Byte) -> Unit) {
    withScopedMemoryAllocator {
        val (pointer, bytesPerElement) = when {
            array.last() <= Byte.MAX_VALUE ->
                it.writeByteArray(array.map { it.toByte() }.toTypedArray()) to 1
            array.last() <= Short.MAX_VALUE ->
                it.writeShortArray(array.map { it.toShort() }.toTypedArray()) to 2
            array.last() <= Int.MAX_VALUE ->
                it.writeIntArray(array.map { it }.toTypedArray()) to 4
            else -> it.writeLongArray(array.map { it.toLong() }.toTypedArray()) to 8
        }
        onResult(pointer.address.toInt(), array.size, bytesPerElement.toByte())
    }
}

internal fun MemoryAllocator.writeByteArray(array: Array<Byte>): Pointer {
    return writeToLinearMemory(array) {
        storeByte(it)
        1
    }
}

internal fun MemoryAllocator.writeShortArray(array: Array<Short>): Pointer {
    return writeToLinearMemory(array) {
        storeShort(it)
        2
    }
}

internal fun MemoryAllocator.writeIntArray(array: Array<Int>): Pointer {
    return writeToLinearMemory(array) {
        storeInt(it)
        4
    }
}

internal fun MemoryAllocator.writeLongArray(array: Array<Long>): Pointer {
    return writeToLinearMemory(array) {
        storeLong(it)
        8
    }
}

private fun <T> MemoryAllocator.writeToLinearMemory(array: Array<T>, store: Pointer.(T) -> Int): Pointer {
    val ptr = allocate(array.size)
    var currentPtr = ptr
    for (el in array) {
        currentPtr += currentPtr.store(el)
    }
    return ptr
}

@JsFun("""
    (address, size, bytesPerElement) => {
        // unsigned values not yet supported
        let constructor;
        switch (bytesPerElement) {
            case 1: constructor = Int8Array; break;
            case 2: constructor = Int16Array; break;
            case 4: constructor = Int32Array; break;
            case 8: constructor = BigInt64Array; break;
            default: throw Error(`Invalid value for bytesPerElement: bytesPerElement`);
        }
        const array = new constructor(wasmExports.memory.buffer, address, size);
        imports.imports?.onResult?.call(null, array);
    }
""")
external fun readJsArrayFromLinearMemory(address: Int, size: Int, bytesPerElement: Byte)
