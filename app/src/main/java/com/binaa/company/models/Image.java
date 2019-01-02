package com.binaa.company.models;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Muhammad on 8/30/2017
 */

public class Image implements Serializable {

    private int id;
    private String imageUrl;

    public Image(JSONObject jsonObject) {
        this.id = jsonObject.optInt("id");
        this.imageUrl = jsonObject.optString("image_path");
    }

    public Image() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
