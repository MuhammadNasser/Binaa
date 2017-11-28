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
    private String views;
    private String area;
    private String bedrooms;
    private String bathrooms;
    private String price;
    private String priceMonth;
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
        this.priceMonth = jsonObject.optString("price_per_month");
        this.phoneNumber = jsonObject.optString("phone_number");
        this.views = jsonObject.optString("count_views");
        this.area = jsonObject.optString("area");
        this.bedrooms = jsonObject.optString("number_of_bedrooms");
        this.bathrooms = jsonObject.optString("number_of_bathrooms");

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
        views = in.readString();
        area = in.readString();
        bedrooms = in.readString();
        bathrooms = in.readString();
        price = in.readString();
        priceMonth = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(code);
        dest.writeString(description);
        dest.writeString(facebook);
        dest.writeString(twitter);
        dest.writeString(instagram);
        dest.writeString(views);
        dest.writeString(area);
        dest.writeString(bedrooms);
        dest.writeString(bathrooms);
        dest.writeString(price);
        dest.writeString(priceMonth);
        dest.writeString(phoneNumber);
        dest.writeTypedList(imagesLinks);
    }
}
