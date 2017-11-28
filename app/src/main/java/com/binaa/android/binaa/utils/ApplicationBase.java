package com.binaa.android.binaa.utils;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Locale;

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
        setLocale(false);
        mInstance = this;
    }

    private void setLocale(boolean restart) {
        Configuration config = getBaseContext().getResources().getConfiguration();
        Locale locale = getLocale();
        Locale.setDefault(locale);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
        } else {
            // noinspection deprecation
            config.locale = locale;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getBaseContext().createConfigurationContext(config);
        } else {
            // noinspection deprecation
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }

        if (restart) {
            restartApplication();
        }
    }

    public Locale getLocale() {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String localeISO = defaultSharedPreferences.getString("lang", "ar");
        return new Locale(localeISO);
    }

    public void setLocale(String languageIso) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        defaultSharedPreferences.edit().putString("lang", languageIso).apply();
        setLocale(true);
    }

    private void restartApplication() {
        Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        startActivity(intent);
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
