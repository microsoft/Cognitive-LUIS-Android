package com.microsoft.cognitiveservices.luis.clientlibrary;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A utility class that contains functions needed by LUIS SDK
 */
public class LUISUtility {

    /**
     * Consumes LUIS API and returns an object containing the parsed LUIS reponse
     * @param url A string containing the URL of the LUIS API to be consumed
     * @return LUISResponse containing the content of the response sent by LUIS API
     * @throws IOException
     */
    public static LUISResponse LUISHTTP(String url) throws IOException {
        HttpURLConnection urlConnection;
        LUISResponse response = null;
        URL _url = new URL(url);
        urlConnection = (HttpURLConnection) _url.openConnection();
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            response = responseToLUISResponse(in);
        } finally {
            urlConnection.disconnect();
        }
        return response;
    }

    /**
     * Converts the JSON response into a Java Object
     * @param in an InputStream for the JSON response data
     * @return LUISResponse containing the content of the response sent by LUIS API
     * @throws IOException
     */
    public static LUISResponse responseToLUISResponse(InputStream in) throws IOException {
        LUISResponse response = null;
        String line;
        String result = "";
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            while ((line = bufferedReader.readLine()) != null)
                result += line;
        }finally {
            in.close();
        }
        response = new LUISResponse(result);
        return response;
    }

    /**
     * Converts a JSONObject to a Map<String, Object>
     *
     * @param JSONobject A JSONObject that needs to be converted to a Map<String, Object>
     * @return A Map<String, Object> that contains the data of the JSONObject
     */
    public static Map<String, Object> JSONObjectToMap(JSONObject JSONobject) {
        Map<String, Object> map = new HashMap<>();

        Iterator<String> keysItr = JSONobject.keys();
        while (keysItr.hasNext()) {
            try {
                String key = keysItr.next();
                Object value = JSONobject.get(key);
                if (value instanceof JSONObject) {
                    value = JSONObjectToMap((JSONObject) value);
                } else if (value instanceof JSONArray) {
                    value = JSONArrayToList((JSONArray) value);
                }
                map.put(key, value);
            } catch (JSONException e) {
                Log.e("LUIS Exception", e.getMessage());
                break;
            }
        }

        return map;
    }

    /**
     * Converts a JSONArray to a List<Object>
     *
     * @param JSONarray A JSONArray that needs to be converted to a List<Object>
     * @return A List<Object> that contains the data of the JSONArray
     */
    public static List<Object> JSONArrayToList(JSONArray JSONarray) {
        List<Object> list = new ArrayList<>();

        for (int i = 0; i < JSONarray.length(); i++) {
            try {
                Object value = JSONarray.get(i);
                if (value instanceof JSONObject) {
                    value = JSONObjectToMap((JSONObject) value);
                } else if (value instanceof JSONArray) {
                    value = JSONArrayToList((JSONArray) value);
                }
                list.add(value);
            } catch (JSONException e) {
                Log.e("LUIS Exception", e.getMessage());
                break;
            }
        }

        return list;
    }
}
