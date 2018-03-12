package com.binaa.android.binaa;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.binaa.android.binaa.server.ContentVolley;
import com.binaa.android.binaa.utils.ApplicationBase;
import com.binaa.android.binaa.utils.MyContextWrapper;

import java.util.Locale;

/**
 * Created by Muhammad on 1/5/2017
 */

public class SplashActivity extends AppCompatActivity {

    private RelativeLayout relativeLayoutLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        relativeLayoutLoading = findViewById(R.id.relativeLayoutLoading);

        Content content = new Content();
        content.addRegistration(ApplicationBase.getInstance().getRegistrationToken());
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        Locale languageType = ApplicationBase.getInstance().getLocale();
        super.attachBaseContext(MyContextWrapper.wrap(newBase, languageType));
    }

    public void isLoading(boolean isLoading) {
        relativeLayoutLoading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    private class Content extends ContentVolley {

        public Content() {
            super("", getApplicationContext());
        }

        @Override
        protected void onPreExecute(ActionType actionType) {
            isLoading(true);
        }

        @Override
        protected void onPostExecute(ActionType actionType, final boolean success, final String message) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    isLoading(false);
                }
            }, 1000);
        }
    }
}
