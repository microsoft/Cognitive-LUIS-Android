package com.microsoft.cognitiveservices.luis.clientlibrary;

import org.json.JSONObject;

import java.util.Map;

/**
 * Describes the Parameter Value structure
 */
public class LUISParameterValue {
    private String name;
    private String type;
    private double score;
    private Map<String, Object> resolution;

    /**
     * Constructs a LUIS Action from a JSON object
     * @param JSONparameterValue a JSONObject containing the parameter value data
     */
    public LUISParameterValue(JSONObject JSONparameterValue) {
        name = JSONparameterValue.optString("entity");
        type = JSONparameterValue.optString("type");
        score = JSONparameterValue.optDouble("score");
        JSONObject JSONresolution = JSONparameterValue.optJSONObject("resolution");
        resolution = JSONresolution != null ? LUISUtility.JSONObjectToMap(JSONresolution) : null;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public double getScore() {
        return score;
    }

    public Map<String, Object> getResolution() {
        return resolution;
    }
}