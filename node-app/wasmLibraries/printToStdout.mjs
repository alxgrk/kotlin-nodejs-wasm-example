import wasmInstance from './wasmInstance.mjs';

const printToStdout = wasmInstance.printToStdout;

export default function (i) {
    printToStdout(i.toString())
}
