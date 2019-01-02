package com.binaa.company.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Muhammad on 8/19/2017
 */

public class Car implements Serializable {

    private int id;
    private String description;
    private String price;
    private String priceMonth;
    private String model;
    private ArrayList<Image> images = new ArrayList<>();


    public Car(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.optInt("id");
        this.description = jsonObject.optString("description");
        this.model = jsonObject.optString("model");
        this.price = jsonObject.optString("price");
        this.priceMonth = jsonObject.optString("price_per_month");

        JSONArray images = jsonObject.getJSONArray("images");
        for (int i = 0; i < images.length(); i++) {
            JSONObject imageItem = images.optJSONObject(i);
            Image image = new Image(imageItem);
            this.images.add(image);
        }
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getPriceMonth() {
        return priceMonth;
    }

    public String getModel() {
        return model;
    }

    public ArrayList<Image> getImages() {
        return images;
    }
}
