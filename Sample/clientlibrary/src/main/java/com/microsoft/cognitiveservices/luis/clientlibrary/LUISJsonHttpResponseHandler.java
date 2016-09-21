package com.microsoft.cognitiveservices.luis.clientlibrary;

import com.loopj.android.http.JsonHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A wrapper class to encapsulate asynchronous requests handling
 */
public class LUISJsonHttpResponseHandler extends JsonHttpResponseHandler {
    LUISResponseHandler responseHandler;

    public LUISJsonHttpResponseHandler(LUISResponseHandler responseHandler) {
        super();
        if(responseHandler == null){
            throw new IllegalArgumentException("Null response handler");
        }
        this.responseHandler = responseHandler;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        if (response != null) {
            LUISResponse LUISresponse = new LUISResponse(response);
            responseHandler.onSuccess(LUISresponse);
        } else {
            responseHandler.onFailure(new Exception("Null response received"));
        }
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString) {
        responseHandler.onFailure(new Exception("Unable to parse the response"));
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        responseHandler.onFailure(new Exception("Unexpected response received"));
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        onFailureHandler(statusCode);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
        onFailureHandler(statusCode);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        onFailureHandler(statusCode);
    }

    private void onFailureHandler(int statusCode){
        String errorMsg = "";
        if (statusCode == 400) {
            errorMsg = "Invalid Application Id";
        } else if (statusCode == 401) {
            errorMsg = "Invalid Subscription Key";
        }
        responseHandler.onFailure(new IllegalArgumentException(errorMsg));
    }
}
