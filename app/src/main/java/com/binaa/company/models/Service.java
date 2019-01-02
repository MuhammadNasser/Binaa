package com.binaa.company.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Muhammad on 8/19/2017
 */

public class Service implements Serializable {

    private int id;
    private String title;
    private String description;
    private String image;

    public Service(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.optInt("id");
        this.title = jsonObject.optString("title");
        this.description = jsonObject.optString("description");
        this.image = jsonObject.optString("image");
    }


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

}
