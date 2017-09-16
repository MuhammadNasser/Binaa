package com.binaa.android.binaa.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Muhammad on 8/19/2017
 */

public class Property implements Parcelable {

    public static final Creator<Property> CREATOR = new Creator<Property>() {
        @Override
        public Property createFromParcel(Parcel in) {
            return new Property(in);
        }

        @Override
        public Property[] newArray(int size) {
            return new Property[size];
        }
    };
    private String id;
    private String title;
    private String code;
    private String description;
    private String facebook;
    private String twitter;
    private String instagram;
    private String price;
    private String phoneNumber;
    private ArrayList<Image> imagesLinks = new ArrayList<>();

    public Property(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.optString("id");
        this.title = jsonObject.optString("title");
        this.code = jsonObject.optString("code");
        this.description = jsonObject.optString("description");
        this.facebook = jsonObject.optString("facebook");
        this.twitter = jsonObject.optString("twitter");
        this.instagram = jsonObject.optString("instagram");
        this.price = jsonObject.optString("price");
        this.phoneNumber = jsonObject.optString("phone_number");

        JSONArray images = jsonObject.getJSONArray("images");
        for (int i = 0; i < images.length(); i++) {
            JSONObject imageItem = images.optJSONObject(i);
            Image image = new Image(imageItem);
            imagesLinks.add(image);
        }
    }

    public Property() {
    }

    protected Property(Parcel in) {
        id = in.readString();
        title = in.readString();
        code = in.readString();
        description = in.readString();
        facebook = in.readString();
        twitter = in.readString();
        instagram = in.readString();
        price = in.readString();
        phoneNumber = in.readString();
        imagesLinks = in.createTypedArrayList(Image.CREATOR);
    }

    public String getId() {
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public ArrayList<Image> getImagesLinks() {
        return imagesLinks;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(code);
        parcel.writeString(description);
        parcel.writeString(facebook);
        parcel.writeString(twitter);
        parcel.writeString(instagram);
        parcel.writeString(price);
        parcel.writeString(phoneNumber);
        parcel.writeTypedList(imagesLinks);
    }
}
