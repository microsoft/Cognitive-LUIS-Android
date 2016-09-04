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
    private List<LUISIntent> intents;
    private List<LUISEntity> entities;
    private List<LUISCompositeEntity> compositeEntities;
    private LUISDialog dialog;

    /**
     * Constructs a LUIS Response from the JSON sent by LUIS
     * @param JSONResponse a String containing the JSON response sent by LUIS
     * @throws NullPointerException
     * @throws IllegalArgumentException
     */
    public LUISResponse(String JSONResponse) throws NullPointerException,IllegalArgumentException {
        if (JSONResponse == null)
            throw new NullPointerException("NULL JSON response");

        if (JSONResponse.isEmpty())
            throw new IllegalArgumentException("Invalid Subscription Key");

        JSONObject JSONresponse;
        //Create an empty JSONObject If there is a problem with the JSON sent by LUIS
        try {
            JSONresponse = new JSONObject(JSONResponse);
        } catch (JSONException e) {
            JSONresponse = new JSONObject();
        }

        query = JSONresponse.optString("query");
        intents = new ArrayList<>();
        entities = new ArrayList<>();
        compositeEntities = new ArrayList<>();

        JSONObject JSONdialog = JSONresponse.optJSONObject("dialog");
        dialog = JSONdialog != null ? new LUISDialog(JSONdialog) : null;


        JSONObject JSONtopIntent = JSONresponse.optJSONObject("topScoringIntent");
        if (JSONtopIntent != null) {
            intents.add(new LUISIntent(JSONtopIntent));
        } else {
            JSONArray JSONintents = JSONresponse.optJSONArray("intents");
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
            if(JSONentity != null) {
                LUISEntity entity = new LUISEntity(JSONentity);
                entities.add(entity);
            }
        }

        JSONArray JSONcompositeEntities = JSONresponse.optJSONArray("compositeEntities");
        for (int i = 0; JSONcompositeEntities != null && i < JSONcompositeEntities.length(); i++) {
            JSONObject JSONcompositeEntity = JSONcompositeEntities.optJSONObject(i);
            if(JSONcompositeEntity != null) {
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