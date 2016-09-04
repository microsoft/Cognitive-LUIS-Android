package com.microsoft.cognitiveservices.luis.clientlibrary;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Describes the LUIS Action structure
 */
public class LUISAction {

    private boolean triggered;
    private String name;
    private List<LUISParameter> parameters;

    /**
     * Constructs a LUIS Action from a JSON object
     * @param JSONaction a JSONObject containing the data of an action
     */
    public LUISAction(JSONObject JSONaction) {
        triggered = JSONaction.optBoolean("triggered");
        name = JSONaction.optString("name");
        parameters = new ArrayList<>();

        JSONArray JSONparameters = JSONaction.optJSONArray("parameters");

        for (int i = 0; JSONparameters != null && i < JSONparameters.length(); i++) {
            JSONObject JSONparameter = JSONparameters.optJSONObject(i);
            if (JSONparameter != null) {
                LUISParameter parameter = new LUISParameter(JSONparameter);
                parameters.add(parameter);
            }
        }
    }

    public boolean getTrigerred() {
        return triggered;
    }

    public String getName() {
        return name;
    }

    public List<LUISParameter> getParams() {
        return parameters;
    }
}