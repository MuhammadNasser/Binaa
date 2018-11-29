package com.binaa.android.binaa.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Muhammad on 8/30/2017
 */

public class Offer implements Serializable {

    private String text;
    private Hotel hotel;

    public Offer(JSONObject jsonObject) throws JSONException {
        this.text = jsonObject.optString("description");
        hotel = new Hotel(jsonObject.optJSONObject("hotel"));
    }

    public Offer() {
    }

    public String getText() {
        return text;
    }

    public Hotel getHotel() {
        return hotel;
    }
}
