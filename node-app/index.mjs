import express from "express";
import fibonacci from "./wasmLibraries/fibonacci.mjs";
import countTo from "./wasmLibraries/countTo.mjs";
import primeFactorization from "./wasmLibraries/primeFactorization.mjs";
import { Piscina } from 'piscina';

const piscina = new Piscina({
    filename: new URL('./workerWrapper.mjs', import.meta.url).href
});
const app = express()
const port = 3000

app.get('/fibonacciWasm', (req, res) => {
    const i = req.query.i ?? 0;
    try {
        console.time("fibonacciWasm")
        const result = fibonacci(i);
        console.timeEnd("fibonacciWasm")
        res.json(result)
    } catch (error) {
        res.status(500).json({error: error?.message ?? error})
    }
})

app.get('/fibonacciJs', (req, res) => {
    const i = req.query.i ?? 0;
    const fibonacciJs = (n, a = 0, b = 1) => {
        if (n === 0) {
            return a;
        } else if (n === 1) {
            return b;
        } else {
            return fibonacciJs(n - 1, b, a + b);
        }
    }
    console.time("fibonacciJs")
    const result = fibonacciJs(i);
    console.timeEnd("fibonacciJs")
    res.json(result)
})

app.get('/countToWasmOnWorker', async (req, res) => {
    const i = req.query.i ?? 0;
    console.time("countToWasmOnWorker")
    const result = await piscina.run(i, { name: 'countToWorker' })
    console.timeEnd("countToWasmOnWorker")
    res.json(result)
})

app.get('/countToWasm', (req, res) => {
    const i = req.query.i ?? 0;
    console.time("countToWasm")
    const result = countTo(i)
    console.timeEnd("countToWasm")
    res.json(result)
})

app.get('/countToJs', (req, res) => {
    const i = req.query.i ?? 0;
    console.time("countToJs")
    let counter = 0
    for (let j = 0; j < i; j++) {
        counter++
    }
    const result = counter.toString();
    console.timeEnd("countToJs")
    res.json({result})
})

app.get('/primeFactorizationWasm', async (req, res) => {
    const i = req.query.i ?? 0;
    console.time("primeFactorizationWasm")
    const onPrimeFactorizationResult = (result) => {
        console.timeEnd("primeFactorizationWasm")
        res.json({result})
    };
    await primeFactorization(i, onPrimeFactorizationResult);
})

app.get('/primeFactorizationJs', (req, res) => {
    const i = req.query.i ?? 0;
    console.time("primeFactorizationJs")
    let n = i
    const factors = []
    for (let j = 2; j <=n; j++) {
        while (n % j == 0) {
            factors.push(j)
            n /= j
        }
    }
    if (n > 1) {
        factors.push(n)
    }
    console.timeEnd("primeFactorizationJs")
    res.json({result: factors})
})
app.listen(port, () => {
    console.log(`Listening on port ${port}`)
})
