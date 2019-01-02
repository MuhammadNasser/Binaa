package com.binaa.company.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Muhammad on 8/19/2017
 */

public class Hotel implements Serializable {

    private int id;
    private String title;
    private String code;
    private String description;
    private String facebook;
    private String twitter;
    private String instagram;
    private String views;
    private String area;
    private String bedrooms;
    private String bathrooms;
    private String price;
    private String coverPic;
    private String priceMonth;
    private String phoneNumber;
    private ArrayList<Image> imagesLinks = new ArrayList<>();

    public Hotel(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.optInt("id");
        this.title = jsonObject.optString("title");
        this.code = jsonObject.optString("code");
        this.description = jsonObject.optString("description");
        this.facebook = jsonObject.optString("facebook");
        this.twitter = jsonObject.optString("twitter");
        this.instagram = jsonObject.optString("instagram");
        this.price = jsonObject.optString("price");
        this.priceMonth = jsonObject.optString("price_per_month");
        this.phoneNumber = jsonObject.optString("phone_number");
        this.views = jsonObject.optString("count_views");
        this.area = jsonObject.optString("area");
        this.bedrooms = jsonObject.optString("number_of_bedrooms");
        this.bathrooms = jsonObject.optString("number_of_bathrooms");
        this.coverPic = jsonObject.optString("cover_pic");

        JSONArray images = jsonObject.getJSONArray("images");
        for (int i = 0; i < images.length(); i++) {
            JSONObject imageItem = images.optJSONObject(i);
            Image image = new Image(imageItem);
            imagesLinks.add(image);
        }
    }


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
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

    public String getPrice() {
        return price;
    }

    public String getPriceMonth() {
        return priceMonth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public ArrayList<Image> getImagesLinks() {
        return imagesLinks;
    }

    public String getViews() {
        return views;
    }

    public String getArea() {
        return area;
    }

    public String getBedrooms() {
        return bedrooms;
    }

    public String getBathrooms() {
        return bathrooms;
    }

    public String getCoverPic() {
        return coverPic;
    }
}
