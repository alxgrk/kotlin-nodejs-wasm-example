@ExperimentalJsExport
@JsExport
fun countTo(n: Long): Long {
    var counter = 0L
    for (i in 0 until n) {
        counter++
    }
    return counter
}
