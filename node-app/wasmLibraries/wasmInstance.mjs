import { instantiate } from '../../build/js/packages/kotlin-nodejs-wasm-example-wasm/kotlin/kotlin-nodejs-wasm-example-wasm.uninstantiated.mjs';

let instance;

/**
 *
 * @param {{imports: any, env: any}} importObject
 * @param {boolean} needsWasi
 * @return {Promise<*>}
 */
export async function createInstanceWithImportObject(importObject, needsWasi = true) {
    if (needsWasi) {
        const { WASI } = await import("wasi");
        const wasi = new WASI();
        const instance = await instantiate({ wasi_snapshot_preview1: wasi.wasiImport, ...importObject});
        wasi.initialize(instance);
        return instance;
    } else {
        return await instantiate({ wasi_snapshot_preview1: { fd_write: () => {}}, ...importObject});
    }
}

if (!instance) {
    instance = await createInstanceWithImportObject();
}
export default instance.exports;
