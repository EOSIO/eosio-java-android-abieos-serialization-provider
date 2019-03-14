package one.block.eosiojavaabieosserializationprovider;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import android.util.Log;

import java.nio.ByteBuffer;
import java.util.Map;
import one.block.eosiojava.EosioError;
import one.block.eosiojava.EosioErrorCode;

public class AbiEos {
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

    public AbiEos() {
        context = create();
        if (null == context) {
            throw new AbiEosContextError("Could not create abieos context.");
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

    @NotNull
    public String jsonToHex(@Nullable String contract,
                            @NotNull String json,
                            @Nullable Map<String, Object> abi,
                            boolean isReorderable) throws EosioError {
        return jsonToHex(contract, "", null, json, abi, isReorderable);
    }

    @NotNull
    public String jsonToHex(@Nullable String contract,
                            @NotNull String name,
                            @Nullable String type,
                            @NotNull String json,
                            @Nullable Map<String, Object> abi,
                            boolean isReorderable) throws EosioError {
        String abiString = JsonUtils.jsonString(abi);
        return jsonToHex(contract, name, type, json, abi, isReorderable);
    }

    @NotNull
    public String jsonToHex(@Nullable String contract,
                            @NotNull String json,
                            @Nullable String abi,
                            boolean isReorderable) throws EosioError {
        return jsonToHex(contract, "", null, json, abi, isReorderable);
    }

    @NotNull
    public String jsonToHex(@Nullable String contract,
                            @NotNull String name,
                            @Nullable String type,
                            @NotNull String json,
                            @Nullable String abi,
                            boolean isReorderable) throws EosioError {

        // refreshContext() will throw an error if it can't create the context so we don't need
        // to check it explicitly before this.
        refreshContext();

        long contract64 = stringToName64(contract);

        String abiJsonString = getAbiJsonString(contract, name, abi);

        boolean result = setAbi(context, contract64, abiJsonString);
        if (result == false) {
            String err = error();
            String errMsg = String.format("Json to hex == Unable to set ABI. %s", err == null ? "" : err);
            throw new EosioError(EosioErrorCode.vaultError, errMsg);
        }

        String typeStr = type == null ? getType(name, contract64) : type;
        if (typeStr == null) {
            String err = error();
            String errMsg = String.format("Unable to find type for action %s. %s", name, err == null ? "" : err);
            throw new EosioError(EosioErrorCode.vaultError, errMsg);
        }

        boolean jsonToBinResult = jsonToBin(context, contract64, typeStr, json, isReorderable);

        if (jsonToBinResult == false) {
            String err = error();
            String errMsg = String.format("Unable to pack json to bin. %s", err == null ? "" : err);
            throw new EosioError(EosioErrorCode.vaultError, errMsg);
        }

        String hex = getBinHex(context);
        if (hex == null) {
            throw new EosioError(EosioErrorCode.vaultError, "Unable to convert binary to hex.");
        }

        return hex;

    }

    @NotNull
    public String hexToJson(@Nullable String contract,
                            @NotNull String hex,
                            @Nullable Map<String, Object> abi) throws EosioError {
        return hexToJson(contract, "", null, hex, abi);
    }

    @NotNull
    public String hexToJson(@Nullable String contract,
                            @NotNull String name,
                            @Nullable String type,
                            @NotNull String hex,
                            @Nullable Map<String, Object> abi) throws EosioError {
        String abiStr = JsonUtils.jsonString(abi);
        return hexToJson(contract, name, type, hex, abiStr);
    }

    @NotNull
    public String hexToJson(@Nullable String contract,
                            @NotNull String hex,
                            @Nullable String abi) throws EosioError {
        return hexToJson(contract, "", null, hex, abi);
    }

    @NotNull
    public String hexToJson(@Nullable String contract,
                            @NotNull String name,
                            @Nullable String type,
                            @NotNull String hex,
                            @Nullable String abi) throws EosioError {

        // refreshContext() will throw an error if it can't create the context so we don't need
        // to check it explicitly before this.
        refreshContext();

        long contract64 = stringToName64(contract);
        String abiJsonString = getAbiJsonString(contract, name, abi);

        boolean result = setAbi(context, contract64, abiJsonString);
        if (result == false) {
            String err = error();
            String errMsg = String.format("Hex to Json == Unable to set ABI. %s", err == null ? "" : err);
            throw new EosioError(EosioErrorCode.vaultError, errMsg);
        }

        String typeStr = type == null ? getType(name, contract64) : type;
        if (typeStr == null) {
            String err = error();
            String errMsg = String.format("Unable to find type for action %s. %s", name, err == null ? "" : err);
            throw new EosioError(EosioErrorCode.vaultError, errMsg);
        }

        String jsonStr = hexToJson(context, contract64, typeStr, hex);
        if (jsonStr == null) {
            String err = error();
            String errMsg = String.format("Unable to unpack hex to json. %s", err == null ? "" : err);
            throw new EosioError(EosioErrorCode.vaultError, errMsg);
        }

        return jsonStr;

    }

    private void refreshContext() {
        destroyContext();
        context = create();
        if (null == context) {
            throw new AbiEosContextError("Could not create abieos context.");
        }
    }

    @NotNull
    private String getAbiJsonString(@Nullable String contract, @NotNull String name, @Nullable String abi)  throws EosioError {

        String abiString = (abi != null && !abi.trim().isEmpty()) ? abi : "";

        if (abiString.endsWith("abi.json")) {
            Map<String, String>jsonMap = AbiEosJson.abiEosJsonMap;
            if (jsonMap.containsKey(abiString)) {
                abiString = jsonMap.get(abiString);
            } else {
                Log.e(TAG, "Error, no json in map for: " + abiString);
                abiString = "";
            }
        }

        if (abiString.isEmpty()) {
            throw new EosioError(EosioErrorCode.vaultError, String.format("Json to hex -- No ABI provided for %s %s",
                    contract == null ? contract : "",
                    name));
        }

        return abiString;

    }

    @NotNull
    private String getAbiJsonString(@Nullable String contract,
                                    @NotNull String name,
                                    @Nullable Map<String, Object> abi) throws EosioError {
        String abiString = JsonUtils.jsonString(abi);
        if (abiString == null) {
            throw new EosioError(EosioErrorCode.vaultError, String.format("Json to hex -- No ABI provided for %s %s",
                    contract == null ? contract : "",
                    name));
        }
        return abiString;
    }

    @Nullable
    private String getType(@NotNull String action, long contract) {
        long action64 = stringToName64(action);
        return getTypeForAction(context, contract, action64);
    }

}
