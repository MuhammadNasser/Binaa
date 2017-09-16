package com.binaa.android.binaa.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Created by Muhammad on 8/30/2017
 */

public class Image implements Parcelable {

    String id;
    String imageUrl;

    public Image(JSONObject jsonObject) {
        this.id = jsonObject.optString("id");
        this.imageUrl = jsonObject.optString("image_path");
    }

    public Image(Cursor cursor) {
    }

    public Image() {
    }

    protected Image(Parcel in) {
        id = in.readString();
        imageUrl = in.readString();
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(imageUrl);
    }
}
