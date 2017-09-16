package com.binaa.android.binaa.views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

/**
 * Created by Muhammad on 07/29/2017
 */

public class ButtonRobotoRegular extends AppCompatButton {

    public ButtonRobotoRegular(Context context) {
        super(context);
        setTypedFace(context);
    }

    public ButtonRobotoRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypedFace(context);
    }

    public ButtonRobotoRegular(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypedFace(context);
    }

    private void setTypedFace(Context context) {

        String englishFont = "Roboto-Light.ttf";

        Typeface type = Typeface.createFromAsset(context.getAssets(), englishFont);

        setTypeface(type);
    }

}
