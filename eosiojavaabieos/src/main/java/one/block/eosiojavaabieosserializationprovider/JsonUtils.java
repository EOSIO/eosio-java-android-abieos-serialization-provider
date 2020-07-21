package one.block.eosiojavaabieosserializationprovider;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Provides static utility classes for converting JSON to different formats.
 */
public class JsonUtils {

    /**
     * Convert a Map of String, Object to JSON string format.
     * @param map - Map, keyed by String, to convert to a JSON string
     * @return - String containing the JSON representation of the given Map
     */
    @Nullable
    public static String jsonString(@NotNull Map<String, Object> map) {
        try {
            JSONObject json = getJsonFromMap(map);
            return json.toString();
        } catch (JSONException ex) {
            //Log.e("JsonUtils", "Error converting map to JSON.", ex);
            return null;
        }
    }

    /**
     * Convert a Map of String, Object to a JSONObject for further manipulation or conversions.
     * Embedded Maps are followed and converted.
     * @param map - Map, keyed by String, to convert to JSONObject
     * @return - JSONObject representation of the given Map
     * @throws JSONException - thrown if there is an error encountered during the JSON conversion process.
     */
    @NotNull
    public static JSONObject getJsonFromMap(@NotNull Map<String, Object> map) throws JSONException {
        JSONObject jsonData = new JSONObject();
        for (String key : map.keySet()) {
            Object value = map.get(key);
            if (value instanceof Map<?, ?>) {
                value = getJsonFromMap((Map<String, Object>) value);
            }
            jsonData.put(key, value);
        }
        return jsonData;
    }
}
