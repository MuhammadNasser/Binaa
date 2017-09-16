package com.binaa.android.binaa.views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by Muhammad on 07/29/2017
 */
public class TextViewRobotoRegular extends AppCompatTextView {
    public TextViewRobotoRegular(Context context) {
        super(context);
        setTypedFace(context);
    }

    public TextViewRobotoRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypedFace(context);
    }

    public TextViewRobotoRegular(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypedFace(context);
    }

    private void setTypedFace(Context context) {

        String englishFont = "Roboto-Regular.ttf";

        Typeface type = Typeface.createFromAsset(context.getAssets(), englishFont);

        setTypeface(type);
    }
}
