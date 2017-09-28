package com.project.seoulmarket.report.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kh on 2016. 10. 21..
 */
public class JsonUtil {

    /**
     * Get the JSON object from a JSON-encoded string
     * @param jsonString A JSON-encoded string
     * @return
     */
    public static JSONObject getJSONObjectFrom(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Read a string mapped by key from a JSON object if the key exists
     * @param jsonObject The source JSON object
     * @param key The key of jsonObject
     * @return The string mapped by key
     */
    public static String getStringFrom(JSONObject jsonObject, String key) {
        try {
            return jsonObject.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }
}