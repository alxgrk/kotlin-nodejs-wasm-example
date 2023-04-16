# kotlin-nodejs-wasm-example
Playground for playing around with K/Wasm on Node.js

Inspired by https://github.com/linux-china/kotlin-wasm-node-demo & https://github.com/kowasm/kowasm.

## Contents

 - Node.js App instantiating WasmInstances to run Kotlin/Wasm code
 - there are endpoints for both, JS & Wasm implementation of certain CPU intensive tasks, which are:
   - **Counting to a specific number:** `/countToJs?i={number}`, `/countToWasm?i={number}` & `/countToWasmOnWorker?i={number}` (uses piscina `worker_thread` pool)
   - **Fibonacci:** `/fibonacciJs?i={number}` & `/fibonacciWasm?i={number}`
   - **PrimeFactorization:** `/primeFactorizationJs?i={number}` & `/primeFactorizationWasm?i={number}`
     - includes custom logic for passing number arrays from Wasm to JS utilizing `ScopedMemoryAllocator`, see also `memoryUtils.kt`
   - **WASI println:** `/printToStdout?i={any}`
       - utilizes hacky WASI fd_write support to print the incoming value to stdout 
