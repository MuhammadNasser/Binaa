package com.binaa.company.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.binaa.company.R;


/**
 * Created by Muhammad on 1/1/17
 * <p>
 * class to handle basic initialization, show and dismiss functionality for dialogs
 */
public class BaseDialogHolder {

    private Dialog dialog;

    @SuppressWarnings("ConstantConditions")
    public BaseDialogHolder(Activity activity, int layoutId, float widthPercentage) {

        dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);

        @SuppressLint("InflateParams")
        View view = activity.getLayoutInflater().inflate(layoutId, null, false);

        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setDimAmount(0.0f);

        FrameLayout frameLayout = new FrameLayout(activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            frameLayout.setBackgroundColor(activity.getResources().getColor(R.color.black60, null));
        } else {
            //noinspection deprecation
            frameLayout.setBackgroundColor(activity.getResources().getColor(R.color.black60));
        }
        frameLayout.addView(view);

        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float windowX = metrics.widthPixels;

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) view.getLayoutParams();

        lp.width = (int) (windowX * widthPercentage);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        lp.gravity = Gravity.CENTER;

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        view.setLayoutParams(lp);

        dialog.setContentView(frameLayout);
    }

    public Dialog getDialog() {
        return dialog;
    }

    public void showDialog() {
        dialog.show();
    }

    public void dismissDialog() {
        dialog.dismiss();
    }

    public <T extends View> T findViewById(int viewId) {
        return dialog.findViewById(viewId);
    }
}
