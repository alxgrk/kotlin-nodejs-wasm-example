import { instantiate } from '../../build/js/packages/kotlin-nodejs-wasm-example-wasm/kotlin/kotlin-nodejs-wasm-example-wasm.uninstantiated.mjs';

let instance;

/**
 *
 * @param {{imports: any, env: any}} importObject
 * @return {Promise<*>}
 */
export async function createInstanceWithImportObject(importObject) {
    return await instantiate(importObject);
}

if (!instance) {
    instance = await createInstanceWithImportObject();
}
export default instance.exports;
