package com.microsoft.cognitiveservices.luis.clientlibrary;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Describes the Parameter structure
 */
public class LUISParameter {

    private String name;
    private boolean required;
    private List<LUISParameterValue> parameterValues;

    /**
     * Constructs a LUIS Parameter from a JSON object
     * @param JSONparameter a JSONObject containing the parameter data
     */
    public LUISParameter(JSONObject JSONparameter) {
        name = JSONparameter.optString("name");
        required = JSONparameter.optBoolean("required");
        parameterValues = new ArrayList<>();

        JSONArray JSONparameterValues = JSONparameter.optJSONArray("value");

        for (int i = 0; JSONparameterValues != null && i < JSONparameterValues.length(); i++) {
            JSONObject JSONparameterValue = JSONparameterValues.optJSONObject(i);
            if (JSONparameterValue != null) {
                LUISParameterValue parameterValue = new LUISParameterValue(JSONparameterValue);
                parameterValues.add(parameterValue);
            }
        }
    }

    public String getName() {
        return name;
    }

    public boolean getRequired() {
        return required;
    }

    public List<LUISParameterValue> getParameterValues() {
        return parameterValues;
    }
}
