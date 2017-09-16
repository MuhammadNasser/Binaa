package com.binaa.android.binaa.utils;

import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by Muhammad on 07/29/2017
 */
public class ApplicationBase extends MultiDexApplication {


    private static ApplicationBase mInstance;

    public static synchronized ApplicationBase getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public String getRegistrationToken() {
        FirebaseInstanceId firebaseInstanceId = null;
        try {
            firebaseInstanceId = FirebaseInstanceId.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String token = null;
        if (firebaseInstanceId != null) {
            token = firebaseInstanceId.getToken();
            Log.e("FireBaseRegistration", "getRegistrationToken: " + token);
        } else {
            Log.e("FireBaseRegistration", "getRegistrationToken: firebaseInstanceId is null");
        }
        return token;
    }


}
