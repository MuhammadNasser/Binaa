package com.binaa.company.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Muhammad on 8/30/2017
 */

public class PropertyOffer implements Serializable {

    private String text;
    private Property property;

    public PropertyOffer(JSONObject jsonObject) throws JSONException {
        this.text = jsonObject.optString("description");
        property = new Property(jsonObject.optJSONObject("apartment"));
    }

    public PropertyOffer() {
    }

    public String getText() {
        return text;
    }

    public Property getProperty() {
        return property;
    }
}
