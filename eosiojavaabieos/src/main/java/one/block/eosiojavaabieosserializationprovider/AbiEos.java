package one.block.eosiojavaabieosserializationprovider;

import one.block.eosiojava.interfaces.ISerializationProvider;
import one.block.eosiojava.models.AbiEosSerializationObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import android.util.Log;

import java.nio.ByteBuffer;
import java.util.Map;
import one.block.eosiojava.EosioError;
import one.block.eosiojava.EosioErrorCode;

public class AbiEos implements ISerializationProvider {
    static {
        System.loadLibrary("abieos-lib");
    }

    public native String stringFromAbiEos();
    public native ByteBuffer create();
    public native void destroy(ByteBuffer context);
    public native String getError(ByteBuffer context);
    public native int getBinSize(ByteBuffer context);
    public native ByteBuffer getBinData(ByteBuffer context);
    public native String getBinHex(ByteBuffer context);
    public native long stringToName(ByteBuffer context, String str);
    public native String nameToString(ByteBuffer context, long name);
    public native boolean setAbi(ByteBuffer context, long contract, String abi);
    public native boolean jsonToBin(ByteBuffer context, long contract, String type, String json, boolean reorderable);
    public native String hexToJson(ByteBuffer context, long contract, String type, String hex);
    public native String getTypeForAction(ByteBuffer context, long contract, long action);

    private ByteBuffer context;

    private String TAG = "AbiEos";

    public AbiEos() throws EosioError {
        context = create();
        if (null == context) {
            EosioError eosioError = new EosioError(EosioErrorCode.serializationError, "Serialization provider context error.");
            eosioError.originalError = new AbiEosContextError("Could not create abieos context.");
            throw eosioError;
        }
    }

    public void destroyContext() {
        if (null != context) {
            destroy(context);
            context = null;
        }
    }

    public long stringToName64(@Nullable String str) {
        if (null == context) throw new AbiEosContextError("Null context!  Has destroyContext() already been called?");
        return stringToName(context, str);
    }

    @NotNull
    public String name64ToString(long name) {
        if (null == context) throw new AbiEosContextError("Null context!  Has destroyContext() already been called?");
        return nameToString(context, name);
    }

    @Nullable
    public String error() {
        if (null == context) throw new AbiEosContextError("Null context!  Has destroyContext() already been called?");
        return getError(context);
    }

    public void serialize(@NotNull AbiEosSerializationObject serializationObject)
            throws EosioError {

        // refreshContext() will throw an error if it can't create the context so we don't need
        // to check it explicitly before this.
        refreshContext();

        if (serializationObject.json.isEmpty()) {
            throw new EosioError(EosioErrorCode.serializationError, "No content to serialize.");
        }

        long contract64 = stringToName64(serializationObject.contract);

        if (serializationObject.abi.isEmpty()) {
            throw new EosioError(EosioErrorCode.vaultError, String.format("Json to hex -- No ABI provided for %s %s",
                    serializationObject.contract == null ? serializationObject.contract : "",
                    serializationObject.name));
        }

        boolean result = setAbi(context, contract64, serializationObject.abi);
        if (result == false) {
            String err = error();
            String errMsg = String.format("Json to hex == Unable to set ABI. %s", err == null ? "" : err);
            throw new EosioError(EosioErrorCode.serializationError, errMsg);
        }

        String typeStr = serializationObject.type == null ?
                getType(serializationObject.name, contract64) : serializationObject.type;
        if (typeStr == null) {
            String err = error();
            String errMsg = String.format("Unable to find type for action %s. %s",
                    serializationObject.name, err == null ? "" : err);
            throw new EosioError(EosioErrorCode.serializationError, errMsg);
        }

        boolean jsonToBinResult = jsonToBin(context,
                contract64,
                typeStr,
                serializationObject.json,
                true);

        if (jsonToBinResult == false) {
            String err = error();
            String errMsg = String.format("Unable to pack json to bin. %s", err == null ? "" : err);
            throw new EosioError(EosioErrorCode.serializationError, errMsg);
        }

        String hex = getBinHex(context);
        if (hex == null) {
            throw new EosioError(EosioErrorCode.serializationError, "Unable to convert binary to hex.");
        }

        serializationObject.hex = hex;
        return;

    }

    @NotNull
    public String serializeTransaction(String json) throws EosioError {
        String abi = getAbiJsonString("transaction.abi.json");
        AbiEosSerializationObject serializationObject = new AbiEosSerializationObject(null,
                "", "transaction", abi);
        serializationObject.json = json;
        serialize(serializationObject);
        return serializationObject.hex;
    }

    @NotNull
    public String serializeAbi(String json) throws EosioError {
        String abi = getAbiJsonString("abi.abi.json");
        AbiEosSerializationObject serializationObject = new AbiEosSerializationObject(null,
                "", "abi_def", abi);
        serializationObject.json = json;
        serialize(serializationObject);
        return serializationObject.hex;
    }

    public void deserialize(@NotNull AbiEosSerializationObject deserilizationObject) throws EosioError {

        // refreshContext() will throw an error if it can't create the context so we don't need
        // to check it explicitly before this.
        refreshContext();

        if (deserilizationObject.hex.isEmpty()) {
            throw new EosioError(EosioErrorCode.deserializationError, "No content to serialize.");
        }

        long contract64 = stringToName64(deserilizationObject.contract);

        if (deserilizationObject.abi.isEmpty()) {
            throw new EosioError(EosioErrorCode.vaultError, String.format("deserialize -- No ABI provided for %s %s",
                    deserilizationObject.contract == null ? deserilizationObject.contract : "",
                    deserilizationObject.name));
        }

        boolean result = setAbi(context, contract64, deserilizationObject.abi);
        if (result == false) {
            String err = error();
            String errMsg = String.format("Hex to Json == Unable to set ABI. %s", err == null ? "" : err);
            throw new EosioError(EosioErrorCode.deserializationError, errMsg);
        }

        String typeStr = deserilizationObject.type == null ?
                getType(deserilizationObject.name, contract64) : deserilizationObject.type;
        if (typeStr == null) {
            String err = error();
            String errMsg = String.format("Unable to find type for action %s. %s",
                    deserilizationObject.name, err == null ? "" : err);
            throw new EosioError(EosioErrorCode.deserializationError, errMsg);
        }

        String jsonStr = hexToJson(context, contract64, typeStr, deserilizationObject.hex);
        if (jsonStr == null) {
            String err = error();
            String errMsg = String.format("Unable to unpack hex to json. %s", err == null ? "" : err);
            throw new EosioError(EosioErrorCode.deserializationError, errMsg);
        }

        deserilizationObject.json = jsonStr;

        return;

    }

    @NotNull
    public String deserializeTransaction(String hex) throws EosioError {
        String abi = getAbiJsonString("transaction.abi.json");
        AbiEosSerializationObject serializationObject = new AbiEosSerializationObject(null,
                "", "transaction", abi);
        serializationObject.hex = hex;
        deserialize(serializationObject);
        return serializationObject.json;
    }

    @NotNull
    public String deserializeAbi(String hex) throws EosioError {
        String abi = getAbiJsonString("abi.abi.json");
        AbiEosSerializationObject serializationObject = new AbiEosSerializationObject(null,
                "", "abi_def", abi);
        serializationObject.hex = hex;
        deserialize(serializationObject);
        return serializationObject.json;
    }

    private void refreshContext() throws EosioError {
        destroyContext();
        context = create();
        if (null == context) {
            EosioError eosioError = new EosioError(EosioErrorCode.serializationError, "Serialization provider context error.");
            eosioError.originalError = new AbiEosContextError("Could not create abieos context.");
            throw eosioError;
        }
    }

    @NotNull
    private String getAbiJsonString(@NotNull String abi)  throws EosioError {

        String abiString;

        Map<String, String>jsonMap = AbiEosJson.abiEosJsonMap;
        if (jsonMap.containsKey(abi)) {
            abiString = jsonMap.get(abi);
        } else {
            Log.e(TAG, "Error, no json in map for: " + abi);
            abiString = "";
        }

        if (abiString == null || abiString.isEmpty()) {
            throw new EosioError(EosioErrorCode.serializationError, String.format("Serialization Provider -- No ABI found for %s",
                    abi));
        }

        return abiString;

    }

    @Nullable
    private String getType(@NotNull String action, long contract) {
        long action64 = stringToName64(action);
        return getTypeForAction(context, contract, action64);
    }

}
