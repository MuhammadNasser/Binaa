package com.binaa.company.views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by Muhammad on 07/29/2017
 */
public class TextViewRobotoBold extends AppCompatTextView {
    public TextViewRobotoBold(Context context) {
        super(context);
        if (!isInEditMode())
            setTypedFace(context);
    }

    public TextViewRobotoBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            setTypedFace(context);
    }

    public TextViewRobotoBold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode())
            setTypedFace(context);
    }

    private void setTypedFace(Context context) {

        String englishFont = "Roboto-Bold.ttf";

        Typeface type = Typeface.createFromAsset(context.getAssets(), englishFont);

        setTypeface(type);
    }
}
