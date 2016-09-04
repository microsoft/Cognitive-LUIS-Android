package com.microsoft.cognitiveservices.luis.clientlibrary;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LUISCompositeEntity {
    private String parentType;
    private String value;
    private List<LUISCompositeEntityChild> children;

    /**
     * Constructs a LUIS Composite Entity from a JSON object
     *
     * @param JSONcompositeEntity a JSONObject containing the composite entity data
     */
    public LUISCompositeEntity(JSONObject JSONcompositeEntity) {
        parentType = JSONcompositeEntity.optString("parentType");
        value = JSONcompositeEntity.optString("value");
        children = new ArrayList<>();

        JSONArray JSONcompositeEntityChildren = JSONcompositeEntity.optJSONArray("children");
        for (int i = 0; JSONcompositeEntityChildren != null
                && i < JSONcompositeEntityChildren.length(); i++) {
            JSONObject JSONcompositeEntityChild = JSONcompositeEntityChildren.optJSONObject(i);
            if (JSONcompositeEntityChild != null) {
                LUISCompositeEntityChild compositeEntityChild =
                        new LUISCompositeEntityChild(JSONcompositeEntityChild);
                children.add(compositeEntityChild);
            }
        }
    }

    public String getParentType() {
        return parentType;
    }

    public String getValue() {
        return value;
    }

    public List<LUISCompositeEntityChild> getChildren() {
        return children;
    }
}