package com.microsoft.cognitiveservices.luis.clientlibrary;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import cz.msebera.android.httpclient.Header;

/**
 * A wrapper class to encapsulate synchronous requests handling
 */
public class LUISSyncHttpClient {
    public static LUISResponse get(String url) throws Exception {
        final JSONObject[] JSONresponse = new JSONObject[1];
        final Exception[] exception = new Exception[1];
        JSONresponse[0]=null;
        exception[0]=null;

        SyncHttpClient client = new SyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONresponse[0] = response;
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                exception[0] = new Exception("Unable to parse the response");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                exception[0] = new Exception("Unexpected response received");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                exception[0] = onFailureHandler(statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                exception[0] = onFailureHandler(statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                exception[0] = onFailureHandler(statusCode);
            }
        });
        if(exception[0] != null){
            throw exception[0];
        }
        if(JSONresponse[0] == null){
            throw new Exception("Request Failure due to null response");
        }
        return new LUISResponse(JSONresponse[0]);
    }

    private static Exception onFailureHandler(int statusCode){
        String errorMsg = "";
        if (statusCode == 400) {
            errorMsg = "Invalid Application Id";
        } else if (statusCode == 401) {
            errorMsg = "Invalid Subscription Key";
        }
        return new IllegalArgumentException(errorMsg);
    }
}