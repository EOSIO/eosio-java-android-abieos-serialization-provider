package one.block.eosiojavaabieosserializationprovider;

import one.block.eosiojava.interfaces.ISerializationProvider;
import one.block.eosiojava.models.AbiEosSerializationObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import android.util.Log;

import java.nio.ByteBuffer;
import java.util.Map;
import one.block.eosiojava.error.serializationProvider.*;

/**
 * Implementation of ISerializationProvider based on a native C++ transformation process
 * compiled via the NDK for Android.
 */
public class AbiEosSerializationProviderImpl implements ISerializationProvider {
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

    private String TAG = "AbiEosSerializationProviderImpl";
    private static final String NULL_CONTEXT_ERR_MSG = "Null context!  Has destroyContext() already been called?";
    private static final String CANNOT_CREATE_CONTEXT_ERR_MSG = "Could not create abieos context.";

    /**
     * Create a new AbiEosSerializationProviderImpl serialization provider instance, initilizing a new context for the C++
     * library to work on automatically.
     *
     * @throws SerializationProviderError - An error is thrown if the context cannot be created.
     */
    public AbiEosSerializationProviderImpl() throws SerializationProviderError {
        context = create();
        if (null == context) {
            throw new AbieosContextNullError(CANNOT_CREATE_CONTEXT_ERR_MSG);
        }
    }

    /**
     * Destroy the underlying C++ context, freeing the heap memory that was allocated for it.
     * This method should always be called before letting the AbiEosSerializationProviderImpl instance go out of scope
     * to avoid leaking memory.
     */
    public void destroyContext() {
        if (null != context) {
            destroy(context);
            context = null;
        }
    }

    /**
     * Takes the given string and converts it to a 64 bit value.  This value should always be treated
     * as unsigned.  Java does not provide a native unsigned 64 bit integer so a long
     * is used instead.
     * @param str - String to convert to 64 bit value.
     * @return - 64 bit value.  Returned as long but should be treated as an unsigned value.
     * @throws SerializationProviderError - An error is thrown if the context cannot be created.
     */
    public long stringToName64(@Nullable String str) throws SerializationProviderError {
        if (null == context) throw new AbieosContextNullError(NULL_CONTEXT_ERR_MSG);
        return stringToName(context, str);
    }

    /**
     * Take the given 64 bit value and converts it to a String.
     * @param name - 64 bit value to convert.  This value should always be treated as unsigned.
     * @return - String equivalent to the 64 bit input value.
     * @throws SerializationProviderError - - An error is thrown if the context cannot be created.
     */
    @NotNull
    public String name64ToString(long name) throws SerializationProviderError {
        if (null == context) throw new AbieosContextNullError(NULL_CONTEXT_ERR_MSG);
        return nameToString(context, name);
    }

    /**
     * Returns the underlying context error from the C++ conversion code, if one exists.
     *
     * @return - Current error string from the C++ context, if any.
     * @throws SerializationProviderError - An error is thrown if the context cannot be created.
     */
    @Nullable
    public String error() throws SerializationProviderError {
        if (null == context) throw new AbieosContextNullError(NULL_CONTEXT_ERR_MSG);
        return getError(context);
    }

    /**
     * Perform a serialization process to convert a JSON string to a HEX string given the parameters
     * provided in the input serializationObject.  The result will be placed in the hex field of
     * the serializationObject and can be accessed with getHex().
     *
     * @param serializationObject - Input object passing the JSON string to be converted as well
     * as other parameters to control the serialization process.
     * @throws SerializeError - A serialization error is thrown if there are any exceptions during the
     * conversion process.
     */
    public void serialize(@NotNull AbiEosSerializationObject serializationObject)
            throws SerializeError {

        try {
            // refreshContext() will throw an error if it can't create the context so we don't need
            // to check it explicitly before this.
            refreshContext();

            if (serializationObject.getJson().isEmpty()) {
                throw new SerializeError("No content to serialize.");
            }

            long contract64 = stringToName64(serializationObject.getContract());

            if (serializationObject.getAbi().isEmpty()) {
                throw new SerializeError(String.format("serialize -- No ABI provided for %s %s",
                        serializationObject.getContract() == null ? serializationObject
                                .getContract() : "",
                        serializationObject.getName()));
            }

            boolean result = setAbi(context, contract64, serializationObject.getAbi());
            if (!result) {
                String err = error();
                String errMsg = String
                        .format("Json to hex == Unable to set ABI. %s", err == null ? "" : err);
                throw new SerializeError(errMsg);
            }

            String typeStr = serializationObject.getType() == null ?
                    getType(serializationObject.getName(), contract64)
                    : serializationObject.getType();
            if (typeStr == null) {
                String err = error();
                String errMsg = String.format("Unable to find type for action %s. %s",
                        serializationObject.getName(), err == null ? "" : err);
                throw new SerializeError(errMsg);
            }

            boolean jsonToBinResult = jsonToBin(context,
                    contract64,
                    typeStr,
                    serializationObject.getJson(),
                    true);

            if (!jsonToBinResult) {
                String err = error();
                String errMsg = String
                        .format("Unable to pack json to bin. %s", err == null ? "" : err);
                throw new SerializeError(errMsg);
            }

            String hex = getBinHex(context);
            if (hex == null) {
                throw new SerializeError("Unable to convert binary to hex.");
            }

            serializationObject.setHex(hex);

        } catch (SerializationProviderError serializationProviderError) {
            throw new SerializeError(serializationProviderError);
        }

    }

    /**
     * Convenience method to transform a transaction JSON string to a hex string.
     *
     * @param json - JSON string representing the transaction to serialize.
     * @return - Serialized hex string representing the transaction JSON.
     * @throws SerializeTransactionError - A serialization error is thrown if there are any exceptions during the
     *      * conversion process.
     */
    @NotNull
    public String serializeTransaction(String json) throws SerializeTransactionError {
        try {
            String abi = getAbiJsonString("transaction.abi.json");
            AbiEosSerializationObject serializationObject = new AbiEosSerializationObject(null,
                    "", "transaction", abi);
            serializationObject.setJson(json);
            serialize(serializationObject);
            return serializationObject.getHex();
        } catch (SerializationProviderError serializationProviderError) {
            throw new SerializeTransactionError(serializationProviderError);
        }
    }

    /**
     * Convenience method to transform an ABI JSON string to a hex string.
     *
     * @param json - JSON string representing the ABI to serialize.
     * @return - Serialized hex string representing the ABI JSON.
     * @throws SerializeAbiError - A serialization error is thrown if there are any exceptions during the
     * conversion process.
     */
    @NotNull
    public String serializeAbi(String json) throws SerializeAbiError {
        try {
            String abi = getAbiJsonString("abi.abi.json");
            AbiEosSerializationObject serializationObject = new AbiEosSerializationObject(null,
                    "", "abi_def", abi);
            serializationObject.setJson(json);
            serialize(serializationObject);
            return serializationObject.getHex();
        } catch (SerializationProviderError serializationProviderError) {
            throw new SerializeAbiError(serializationProviderError);
        }
    }

    /**
     * Perform a deserialization process to convert a hex string to a JSON string given the parameters
     * provided in the input deserilizationObject.  The result will be placed in the json field of
     * the deserilizationObject and can be accessed with getJson().
     *
     * @param deserilizationObject - Input object passing the hex string to be converted as well
     * as other parameters to control the deserialization process.
     * @throws DeserializeError - A deserialization error is thrown if there are any exceptions during the
     * conversion process.
     */
    public void deserialize(@NotNull AbiEosSerializationObject deserilizationObject) throws DeserializeError {

        try {
            // refreshContext() will throw an error if it can't create the context so we don't need
            // to check it explicitly before this.
            refreshContext();

            if (deserilizationObject.getHex().isEmpty()) {
                throw new DeserializeError("No content to serialize.");
            }

            long contract64 = stringToName64(deserilizationObject.getContract());

            if (deserilizationObject.getAbi().isEmpty()) {
                throw new DeserializeError(String.format("deserialize -- No ABI provided for %s %s",
                        deserilizationObject.getContract() == null ? deserilizationObject
                                .getContract() : "",
                        deserilizationObject.getName()));
            }

            boolean result = setAbi(context, contract64, deserilizationObject.getAbi());
            if (!result) {
                String err = error();
                String errMsg = String
                        .format("deserialize == Unable to set ABI. %s", err == null ? "" : err);
                throw new DeserializeError(errMsg);
            }

            String typeStr = deserilizationObject.getType() == null ?
                    getType(deserilizationObject.getName(), contract64)
                    : deserilizationObject.getType();
            if (typeStr == null) {
                String err = error();
                String errMsg = String.format("Unable to find type for action %s. %s",
                        deserilizationObject.getName(), err == null ? "" : err);
                throw new DeserializeError(errMsg);
            }

            String jsonStr = hexToJson(context, contract64, typeStr, deserilizationObject.getHex());
            if (jsonStr == null) {
                String err = error();
                String errMsg = String
                        .format("Unable to unpack hex to json. %s", err == null ? "" : err);
                throw new DeserializeError(errMsg);
            }

            deserilizationObject.setJson(jsonStr);
        } catch (SerializationProviderError serializationProviderError) {
            throw new DeserializeError(serializationProviderError);
        }
    }

    /**
     * Convenience method to transform a transaction hex string to a JSON string.
     *
     * @param hex - Hex string representing the transaction to deserialize.
     * @return - Deserialized JSON string representing the transaction hex.
     * @throws DeserializeTransactionError - A deserialization error is thrown if there are any exceptions during the
     *      * conversion process.
     */
    @NotNull
    public String deserializeTransaction(String hex) throws DeserializeTransactionError {
        try {
            String abi = getAbiJsonString("transaction.abi.json");
            AbiEosSerializationObject serializationObject = new AbiEosSerializationObject(null,
                    "", "transaction", abi);
            serializationObject.setHex(hex);
            deserialize(serializationObject);
            return serializationObject.getJson();
        } catch (SerializationProviderError serializationProviderError) {
            throw new DeserializeTransactionError(serializationProviderError);
        }
    }

    /**
     * Convenience method to transform an ABI hex string to a JSON string.
     *
     * @param hex - Hex string representing the ABI to deserialize.
     * @return - Deserialized JSON string representing the ABI hex.
     * @throws DeserializeAbiError - A deserialization error is thrown if there are any exceptions during the
     * conversion process.
     */
    @NotNull
    public String deserializeAbi(String hex) throws DeserializeAbiError {
        try {
            String abi = getAbiJsonString("abi.abi.json");
            AbiEosSerializationObject serializationObject = new AbiEosSerializationObject(null,
                    "", "abi_def", abi);
            serializationObject.setHex(hex);
            deserialize(serializationObject);
            return serializationObject.getJson();
        } catch (SerializationProviderError serializationProviderError) {
            throw new DeserializeAbiError(serializationProviderError);
        }
    }

    /**
     * Reset the underlying C++ context by destroying and recreating it.  This allows multiple
     * conversions to be done using the same AbiEosSerializationProviderImpl instance.
     * @throws SerializationProviderError - if the context cannot be re-created.
     */
    private void refreshContext() throws SerializationProviderError {
        destroyContext();
        context = create();
        if (null == context) {
            throw new AbieosContextNullError("Could not create abieos context.");
        }
    }

    /**
     * Return bundled ABI JSON for transaction or ABI serialization/deserialization conversions.
     *
     * @param abi - Name of the ABI JSON template to return.
     * @return - JSON template string for the specified ABI.
     * @throws SerializationProviderError - if the specified ABI JSON template cannot be found and returned.
     */
    @NotNull
    private String getAbiJsonString(@NotNull String abi)  throws SerializationProviderError {

        String abiString;

        Map<String, String>jsonMap = AbiEosJson.abiEosJsonMap;
        if (jsonMap.containsKey(abi)) {
            abiString = jsonMap.get(abi);
        } else {
            Log.e(TAG, "Error, no json in map for: " + abi);
            abiString = "";
        }

        if (abiString == null || abiString.isEmpty()) {
            throw new SerializationProviderError(String.format("Serialization Provider -- No ABI found for %s",
                    abi));
        }

        return abiString;

    }

    /**
     * Return the type string for a specified action and contract.  Used as part of the internal
     * conversion process to the underlying C++ library.
     *
     * @param action - String specifying the action desired for type, i.e. "transfer"
     * @param contract - 64 bit value of the contract name desired for type.  i.e. originating name
     * might be "eosio.token"
     * @return - String specifying the type for the given action and contract.
     * @throws SerializationProviderError
     */
    @Nullable
    private String getType(@NotNull String action, long contract) throws SerializationProviderError {
        long action64 = stringToName64(action);
        return getTypeForAction(context, contract, action64);
    }

}
