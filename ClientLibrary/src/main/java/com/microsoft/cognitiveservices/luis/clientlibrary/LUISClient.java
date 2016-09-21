package com.microsoft.cognitiveservices.luis.clientlibrary;

import com.loopj.android.http.*;
import cz.msebera.android.httpclient.Header;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.regex.Pattern;

/**
 * This is the interface of the LUIS SDK
 */
public class LUISClient {

    private final String LUISURL = "https://api.projectoxford.ai/luis/v1/application";
    private final String LUISPreviewURL;
    private final String LUISPredictMask = "%s%s?id=%s&subscription-key=%s%s&q=%s";
    private final String LUISReplyMask = "%s%s?id=%s&subscription-key=%s&contextid=%s%s&q=%s";
    private final String LUISVerboseURL;
    private String appId;
    private String appKey;
    private boolean preview;
    private boolean verbose;

    /**
     * Constructs a LUISClient with the corresponding user's App Id and Subscription Key
     *
     * @param appId   a String containing the Application Id
     * @param appKey  a String containing the Subscription Key
     * @param preview a boolean to choose whether to use the published or the in preview version
     * @param verbose a boolean to choose whether or not to use the verbose version
     * @throws IllegalArgumentException
     */
    public LUISClient(String appId, String appKey, boolean preview, boolean verbose) {
        if (appId == null)
            throw new IllegalArgumentException("NULL Application Id");
        if (appId.isEmpty())
            throw new IllegalArgumentException("Empty Application Id");
        if (Pattern.compile("\\s").matcher(appId).find())
            throw new IllegalArgumentException("Invalid Application Id");
        if (appKey == null)
            throw new IllegalArgumentException("NULL Subscription Key");
        if (appKey.isEmpty())
            throw new IllegalArgumentException("Empty Subscription Key");
        if (Pattern.compile("\\s").matcher(appKey).find())
            throw new IllegalArgumentException("Invalid Subscription Key");

        this.appId = appId;
        this.appKey = appKey;
        this.preview = preview;
        this.verbose = verbose;
        LUISPreviewURL = preview ? "/preview" : "";
        LUISVerboseURL = verbose ? "&verbose=true" : "";
    }

    /**
     * Constructs a LUISClient with the corresponding user's App Id and Subscription Keys
     * in non verbose version (if preview version is used
     *
     * @param appId   a String containing the Application Id
     * @param appKey  a String containing the Subscription Key
     * @param preview a boolean to choose whether to use the published or the in preview version
     * @throws IllegalArgumentException
     */
    public LUISClient(String appId, String appKey, boolean preview) {
        this(appId, appKey, preview, false);
    }

    /**
     * Constructs a LUISClient with the corresponding user's App Id and Subscription Keys
     * in non preview version
     * in non verbose version (if preview version is used)
     *
     * @param appId  a String containing the Application Id
     * @param appKey a String containing the Subscription Key
     * @throws IllegalArgumentException
     */
    public LUISClient(String appId, String appKey) {
        this(appId, appKey, false, false);
    }

    /**
     * Starts the prediction procedure for the user's text
     *
     * @param text A String containing the text that needs to be analysed and predicted
     * @return A LUISResponse containing the content of the response sent by LUIS API
     * @throws IllegalArgumentException
     * @throws Exception
     */
    public LUISResponse predict(String text) throws Exception {
        if (text == null)
            throw new IllegalArgumentException("NULL text to predict");
        text = text.trim();
        if (text.isEmpty())
            throw new IllegalArgumentException("Empty text to predict");

        return LUISSyncHttpClient.get(predictURLGen(text));
    }

    /**
     * Starts the replying procedure for the user's dialog, and accepts a LUISResponse object
     * which includes the context Id of the dialog
     *
     * @param text     A String containing the text that needs to be analysed and predicted
     * @param response A LUISResponse containing the context Id of the current Dialog
     * @throws RuntimeException
     * @throws IllegalArgumentException
     * @throws IOException
     * @returns A LUISResponse containing the reply data sent by LUIS
     */
    public LUISResponse reply(String text, LUISResponse response) throws Exception {
        return reply(text, response, "");
    }


    /**
     * Starts the replying procedure for the user's dialog, and accepts a LUISResponse object
     * which includes the context Id of the dialog
     *
     * @param text                  A String containing the text that needs to be analysed and predicted
     * @param response              A LUISResponse containing the context Id of the current Dialog
     * @param forceSetParameterName A string containing the name of a parameter that needs to be
     *                              reset in the dialog
     * @throws RuntimeException
     * @throws IllegalArgumentException
     * @throws Exception
     * @returns A LUISResponse containing the reply data sent by LUIS
     */
    public LUISResponse reply(String text, LUISResponse response, String forceSetParameterName)
            throws Exception {
        //TODO: When the reply can be used in the published version:
        //TODO: This condition has to be removed
        if (!preview)
            throw new RuntimeException("Reply can only be used with the preview version");
        if (text == null)
            throw new IllegalArgumentException("NULL text to predict");
        text = text.trim();
        if (text.isEmpty())
            throw new IllegalArgumentException("Empty text to predict");
        if (response == null)
            throw new IllegalArgumentException("NULL LUIS response");

        return LUISSyncHttpClient.get(replyURLGen(text, response, forceSetParameterName));
    }

    /**
     * Starts the prediction procedure for the user's text
     *
     * @param text            A String containing the text that needs to be analysed and predicted
     * @param responseHandler A responseHandler object af a class that can contains 2 functions
     *                        onSuccess and onFailure to be executed based on the success or the failure of the prediction
     * @throws IllegalArgumentException
     * @throws IOException
     */
    public void predict(String text, final LUISResponseHandler responseHandler) throws IOException {
        if (text == null)
            throw new IllegalArgumentException("NULL text to predict");
        text = text.trim();
        if (text.isEmpty())
            throw new IllegalArgumentException("Empty text to predict");
        if (responseHandler == null)
            throw new IllegalArgumentException("Null response handler");

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(predictURLGen(text), new LUISJsonHttpResponseHandler(responseHandler));
    }


    /**
     * Starts the replying procedure for the user's dialog, and accepts a LUISResponse object
     * which includes the context Id of the dialog
     *
     * @param text            A String containing the text that needs to be analysed and predicted
     * @param response        A LUISResponse containing the context Id of the current Dialog
     * @param responseHandler A responseHandler object af a class that can contains 2 functions
     *                        onSuccess and onFailure to be executed based on the success or the failure of the prediction
     * @throws RuntimeException
     * @throws IllegalArgumentException
     * @throws IOException
     */
    public void reply(String text, LUISResponse response, final LUISResponseHandler responseHandler)
            throws IOException {
        reply(text, response, responseHandler, "");
    }


    /**
     * Starts the replying procedure for the user's dialog, and accepts a LUISResponse object
     * which includes the context Id of the dialog
     *
     * @param text                  A String containing the text that needs to be analysed and predicted
     * @param response              A LUISResponse containing the context Id of the current Dialog
     * @param responseHandler       A responseHandler object af a class that can contains 2 functions
     *                              onSuccess and onFailure to be executed based on the success or
     *                              the failure of the prediction
     * @param forceSetParameterName A string containing the name of a parameter that needs to be
     *                              reset in the dialog
     * @throws RuntimeException
     * @throws IllegalArgumentException
     * @throws IOException
     */
    public void reply(String text, LUISResponse response, final LUISResponseHandler responseHandler,
                      String forceSetParameterName) throws IOException {
        if (!preview)
            throw new RuntimeException("Reply can only be used with the preview version");
        if (text == null)
            throw new IllegalArgumentException("NULL text to predict");
        text = text.trim();
        if (text.isEmpty())
            throw new IllegalArgumentException("Empty text to predict");
        if (response == null)
            throw new IllegalArgumentException("NULL LUIS response");
        if (responseHandler == null)
            throw new IllegalArgumentException("Null response handler");

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(replyURLGen(text, response, forceSetParameterName), new LUISJsonHttpResponseHandler(responseHandler));
    }

    /**
     * Generates the url for the predict web request
     *
     * @param text A String containing the text that needs to be analysed and predicted
     * @return A String containing the url for the predict web request
     * @throws IOException
     */
    public String predictURLGen(String text) throws IOException {
        String encodedQuery;
        encodedQuery = URLEncoder.encode(text, "UTF-8");
        return String.format(LUISPredictMask, LUISURL, LUISPreviewURL, appId, appKey
                , LUISVerboseURL, encodedQuery);
    }

    /**
     * Generates the url for the reply web request
     *
     * @param text                  A String containing the text that needs to be analysed and predicted
     * @param forceSetParameterName A string containing the name of a parameter that needs to be
     *                              reset in the dialog
     * @return A String containing the url for the reply web request
     * @throws IOException
     */
    String replyURLGen(String text, LUISResponse response, String forceSetParameterName)
            throws IOException {
        String encodedQuery;
        encodedQuery = URLEncoder.encode(text, "UTF-8");
        String url = String.format(LUISReplyMask, LUISURL, LUISPreviewURL, appId, appKey,
                response.getDialog().getContextId(), LUISVerboseURL, encodedQuery);
        if (forceSetParameterName != null && !forceSetParameterName.isEmpty()) {
            url += String.format("&forceset=%s", forceSetParameterName);
        }
        return url;
    }
}