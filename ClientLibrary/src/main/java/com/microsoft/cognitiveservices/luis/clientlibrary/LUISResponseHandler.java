package com.microsoft.cognitiveservices.luis.clientlibrary;

/**
 * An interface to be inherited when predicting text
 * in order to encapsulate the developer's logic that
 * depeneds on the response sent by LUIS
 */
public interface LUISResponseHandler {
    /**
     * This functions needs to be implemented by the user in order to
     * encapsulate his logic that should be executed if the prediction
     * or replying actions succeed
     * @param response a LUISResponse object that contains the response data
     */
    void onSuccess(LUISResponse response);

    /**
     * This functions needs to be implemented by the user in order to
     * encapsulate his logic that should be executed if the prediction
     * or replying actions fail
     * @param e an Exception that is thrown while predicting or replying
     */
    void onFailure(Exception e);

}
