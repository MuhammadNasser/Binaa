package com.binaa.android.binaa.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Muhammad on 8/19/2017
 */

public class Contact implements Serializable {

    private String id;
    private String phone;
    private String email;
    private String address;
    private String facebook;
    private String twitter;
    private String instagram;


    public Contact(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.optString("id");
        this.phone = jsonObject.optString("phone");
        this.email = jsonObject.optString("email");
        this.address = jsonObject.optString("address");
        this.facebook = jsonObject.optString("facebook");
        this.twitter = jsonObject.optString("twitter");
        this.instagram = jsonObject.optString("instagram");
    }


    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getInstagram() {
        return instagram;
    }
}
