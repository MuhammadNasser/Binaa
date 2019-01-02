package com.binaa.company.views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by Muhammad on 07/29/2017
 */
public class TextViewRobotoLight extends AppCompatTextView {
    public TextViewRobotoLight(Context context) {
        super(context);
        if (!isInEditMode())
            setTypedFace(context);
    }

    public TextViewRobotoLight(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            setTypedFace(context);
    }

    public TextViewRobotoLight(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode())
            setTypedFace(context);
    }

    private void setTypedFace(Context context) {

        String englishFont = "Roboto-Light.ttf";

        Typeface type = Typeface.createFromAsset(context.getAssets(), englishFont);

        setTypeface(type);
    }
}
