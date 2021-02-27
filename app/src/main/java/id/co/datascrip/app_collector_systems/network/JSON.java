package id.co.datascrip.app_collector_systems.network;

import android.util.Log;

import org.json.JSONObject;

public class JSON {

    public static String getString(JSONObject jsonObject, String Key) {
        try {
            if (jsonObject.has(Key)) {
                if (!jsonObject.getString(Key).trim().toLowerCase().equals("null"))
                    return jsonObject.getString(Key).trim();
            }
        } catch (Exception e) {
            Log.e("JSON->getString", e.toString());
        }
        return null;
    }

    public static int getInt(JSONObject jsonObject, String Key) {
        try {
            if (jsonObject.has(Key))
                return jsonObject.getInt(Key);
        } catch (Exception e) {
            Log.e("JSON->getString", e.toString());
        }
        return 0;
    }

    public static double getDouble(JSONObject jsonObject, String Key) {
        try {
            if (jsonObject.has(Key))
                return jsonObject.getDouble(Key);
        } catch (Exception e) {
            Log.e("JSON->getString", e.toString());
        }
        return 0;
    }
    public static float getFloat(JSONObject jsonObject, String Key) {
        try {
            if (jsonObject.has(Key))
                return Float.valueOf(String.valueOf(jsonObject.getDouble(Key)));
        } catch (Exception e) {
            Log.e("JSON->getFloat", e.toString());
        }
        return 0;
    }
}
