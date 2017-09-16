package com.binaa.android.binaa.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Muhammad on 8/19/2017
 */

public class Service implements Parcelable {

    private String id;
    private String title;
    private String description;
    private String image;

    public Service(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.optString("id");
        this.title = jsonObject.optString("title");
        this.description = jsonObject.optString("description");
        this.image = jsonObject.optString("image");
    }

    protected Service(Parcel in) {
        id = in.readString();
        title = in.readString();
        description = in.readString();
        image = in.readString();
    }

    public static final Creator<Service> CREATOR = new Creator<Service>() {
        @Override
        public Service createFromParcel(Parcel in) {
            return new Service(in);
        }

        @Override
        public Service[] newArray(int size) {
            return new Service[size];
        }
    };

    public String getId() {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(image);
    }
}
