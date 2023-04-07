import wasmInstance from './wasmInstance.mjs';

const fibonacci = wasmInstance.fibonacciJs;

export default function (i) {
    const value = fibonacci(i);
    return BigInt(value).toString();
}
