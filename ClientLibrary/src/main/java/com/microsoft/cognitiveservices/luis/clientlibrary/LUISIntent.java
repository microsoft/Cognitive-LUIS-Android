package com.microsoft.cognitiveservices.luis.clientlibrary;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Describes the LUIS Intent structure
 */
public class LUISIntent {

    private String name;
    private double score;
    private List<LUISAction> actions;

    /**
     * Constructs a LUIS Intention from a JSON object
     * @param JSONintent a JSONObject containing the intent data
     */
    public LUISIntent(JSONObject JSONintent) {
        name = JSONintent.optString("intent");
        score = JSONintent.optDouble("score");
        actions = new ArrayList<>();

        JSONArray JSONactions = JSONintent.optJSONArray("actions");

        for (int i = 0; JSONactions != null && i < JSONactions.length(); i++) {
            JSONObject JSONaction = JSONactions.optJSONObject(i);
            if(JSONaction != null) {
                LUISAction action = new LUISAction(JSONaction);
                actions.add(action);
            }
        }
    }

    public String getName() {
        return name;
    }

    public double getScore() {
        return score;
    }

    public List<LUISAction> getActions() {
        return actions;
    }
}
