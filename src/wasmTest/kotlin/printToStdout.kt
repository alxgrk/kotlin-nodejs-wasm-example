import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.fail

@kotlin.js.ExperimentalJsExport
class PrintTests {
    @Test
    fun println() {
        try {
            // first print should initialize WASI
            printToStdout("first")
            // second print should simply run
            printToStdout("first")
        } catch (e: Error) {
            fail("Unexpected error")
        }
    }

}
