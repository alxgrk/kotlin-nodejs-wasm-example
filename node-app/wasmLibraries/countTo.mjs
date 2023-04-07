import wasmInstance from './wasmInstance.mjs';

const countTo = wasmInstance.countTo;

export default function (i) {
    return {result: countTo(i).toString()};
}
