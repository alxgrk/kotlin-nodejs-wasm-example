import {createInstanceWithImportObject} from './wasmInstance.mjs';

// must use exported (!) memory - if memory was created before and passed on intialization, the buffer wouldn't contain actual data
// const memory = new WebAssembly.Memory({initial:1})
let memory;

let primeFactorization;

let storedCallback;

export default async function (i, callback) {
    storedCallback = callback;

    const onResult = (array) => {
        console.log("Array: " + array)
        // bigint can not be serialized directly, so convert it to string first
        const isBigIntArray = array && array.length && 'bigint' === typeof array[0];
        console.log("Type: " + typeof array[0])
        const copiedArray = isBigIntArray ? Array.from(array).map(it => it.toString()) : Array.from(array);
        storedCallback(copiedArray);
    };

    if (!memory) {
        const {exports} = await createInstanceWithImportObject({
            imports: {
                onResult
            }
        }, false);
        memory = exports.memory;

        primeFactorization = exports.primeFactorizationJs;
    }

    primeFactorization(i);
}
