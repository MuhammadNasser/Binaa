package com.binaa.android.binaa.models;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Muhammad on 8/30/2017
 */

public class BaseModel implements Serializable {

    private int id;
    private String name;

    public BaseModel(JSONObject jsonObject) {
        this.id = jsonObject.optInt("id");
        this.name = jsonObject.optString("name");
    }

    public BaseModel() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
