package one.block.eosiojavaabieosserializationprovider;

import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class JsonUtils {

    @Nullable
    public static String jsonString(@NotNull Map<String, Object> map) {
        try {
            JSONObject json = getJsonFromMap(map);
            return json.toString();
        } catch (JSONException ex) {
            Log.e("JsonUtils", "Error converting map to JSON.", ex);
            return null;
        }
    }

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
