package one.block.eosiojavaabieosserializationprovider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class JsonUtils {

    @Nullable
    public static String jsonString(@NonNull Map<String, Object> map) {
        try {
            JSONObject json = getJsonFromMap(map);
            return json.toString();
        } catch (JSONException ex) {
            Log.e("JsonUtils", "Error converting map to JSON.", ex);
            return null;
        }
    }

    @NonNull
    public static JSONObject getJsonFromMap(@NonNull Map<String, Object> map) throws JSONException {
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
