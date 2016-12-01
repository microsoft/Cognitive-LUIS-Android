package com.microsoft.cognitiveservices.luis.clientlibrary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Describes the response structure, and is the main point
 * to access the response sent by LUIS after prediction
 */
public class LUISResponse {
    private String query;
    private LUISIntent topScoringIntent;
    private List<LUISIntent> intents;
    private List<LUISEntity> entities;
    private List<LUISCompositeEntity> compositeEntities;
    private LUISDialog dialog;

    public LUISResponse(JSONObject JSONresponse) {
        if (JSONresponse == null)
            throw new NullPointerException("NULL JSON response");

        query = JSONresponse.optString("query");
        intents = new ArrayList<>();
        entities = new ArrayList<>();
        compositeEntities = new ArrayList<>();

        JSONObject JSONdialog = JSONresponse.optJSONObject("dialog");
        dialog = JSONdialog != null ? new LUISDialog(JSONdialog) : null;


        JSONObject JSONtopIntent = JSONresponse.optJSONObject("topScoringIntent");
        topScoringIntent = JSONtopIntent != null ? new LUISIntent(JSONtopIntent) : null;

        JSONArray JSONintents = JSONresponse.optJSONArray("intents");
        if (JSONintents == null) {
            intents.add(topScoringIntent);
        } else {
            for (int i = 0; JSONintents != null && i < JSONintents.length(); i++) {
                JSONObject JSONintent = JSONintents.optJSONObject(i);
                if (JSONintent != null) {
                    LUISIntent intent = new LUISIntent(JSONintent);
                    intents.add(intent);
                }
            }
        }

        JSONArray JSONentities = JSONresponse.optJSONArray("entities");
        for (int i = 0; JSONentities != null && i < JSONentities.length(); i++) {
            JSONObject JSONentity = JSONentities.optJSONObject(i);
            if (JSONentity != null) {
                LUISEntity entity = new LUISEntity(JSONentity);
                entities.add(entity);
            }
        }

        JSONArray JSONcompositeEntities = JSONresponse.optJSONArray("compositeEntities");
        for (int i = 0; JSONcompositeEntities != null && i < JSONcompositeEntities.length(); i++) {
            JSONObject JSONcompositeEntity = JSONcompositeEntities.optJSONObject(i);
            if (JSONcompositeEntity != null) {
                LUISCompositeEntity compositeEntity = new LUISCompositeEntity(JSONcompositeEntity);
                compositeEntities.add(compositeEntity);
            }
        }
    }

    public String getQuery() {
        return query;
    }

    public LUISIntent getTopIntent() {
        return intents.get(0);
    }

    public List<LUISIntent> getIntents() {
        return intents;
    }

    public List<LUISEntity> getEntities() {
        return entities;
    }

    public List<LUISCompositeEntity> getCompositeEntities() {
        return compositeEntities;
    }

    public LUISDialog getDialog() {
        return dialog;
    }
}