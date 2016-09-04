package com.microsoft.cognitiveservices.luis.clientlibrary;

import org.json.JSONObject;

/**
 * Describes the LUIS Action structure
 */
public class LUISDialog {
    private String prompt;
    private String parameterName;
    private String contextId;
    private String status;
    private boolean finished;

    /**
     * Constructs a LUIS Dialog from a JSON object
     * @param JSONdialog a JSONObject containing the data of a dialog
     */
    public LUISDialog(JSONObject JSONdialog) {
        prompt = JSONdialog.optString("prompt");
        parameterName = JSONdialog.optString("parameterName");
        contextId = JSONdialog.optString("contextId");
        status = JSONdialog.optString("status");
        finished = status.equals("Finished");
    }

    public String getPrompt() {
        return prompt;
    }

    public String getParameterName() {
        return parameterName;
    }

    public String getContextId() {
        return contextId;
    }

    public String getStatus() {
        return status;
    }

    public boolean isFinished() {
        return finished;
    }
}
