/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.binaa.company.views;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.binaa.company.R;

import java.util.Locale;

public class PagerSlidingTabStrip extends HorizontalScrollView {

    private Context context;

    public interface IconTabProvider {
        int getPageIconResId(int position);
    }

    // @formatter:off
    private static final int[] ATTRS = new int[]{android.R.attr.textSize,
            android.R.attr.textColor};

    public static final int[] PagerSlidingTabStrip = {0x7f01006e, 0x7f01006f,
            0x7f010070, 0x7f010071, 0x7f010072, 0x7f010073, 0x7f010074,
            0x7f010075, 0x7f010076, 0x7f010077, 0x7f010078};

    // @formatter:on

    private final PageListener pageListener = new PageListener();
    public OnPageChangeListener delegatePageListener;

    private LinearLayout tabsContainer;
    private ViewPager pager;

    private int tabCount;

    private int currentPosition = 0;
    private float currentPositionOffset = 0f;

    private Paint rectPaint;
    private Paint dividerPaint;

    private int scrollOffset = 52;
    private int indicatorHeight = 4;
    private int underlineHeight = 0;
    private int dividerPadding = 12;
    private int tabPadding = 32;

    private int tabTextSize = 12;
    private int tabTextColor = 0xFFFFFFFF;
    int indicatorColor = 0xff444c63;
    private Typeface tabTypeface = null;

    int selectedTabBackground;
    boolean selectWithBackground = false, setTabWithCustomView = false, selectWithSrc = false;
    View lastEdited = null;
    String[] tabCustomViewItems = null;

    String[] textTabNames = {};
    int[] textTabNumbers = {};

    private int lastScrollX = 0;

    private Locale locale;

    public PagerSlidingTabStrip(Context context) {
        this(context, null);
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;

        if (isInEditMode())
            return;

        setFillViewport(true);
        setWillNotDraw(false);


        // TODO revise
        // detect screen width to set the padding for the tabs

        String tag = getTag() != null ? getTag().toString() : "sw320dp";
        if (tag.equalsIgnoreCase("sw320dp")) {
            tabPadding = 22;
        } else if (tag.equalsIgnoreCase("sw480dp")) {
            tabPadding = 33;
        } else if (tag.equalsIgnoreCase("sw600dp")) {
            tabPadding = 42;
        }

		/*WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		// Point outSize = new Point();
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		int displayWidth = metrics.widthPixels;
		int displayWidthDP = displayWidth
				/ context.getResources().getDisplayMetrics().densityDpi;
		tabPadding = (displayWidth - dividerPadding - dividerWidth) / 30;*/
        int dividerWidth = 1;

        tabsContainer = new LinearLayout(context);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        tabsContainer.setGravity(Gravity.CENTER);
        tabsContainer.setWeightSum(tabCount);
        addView(tabsContainer);

        DisplayMetrics dm = getResources().getDisplayMetrics();

        scrollOffset = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, scrollOffset, dm);
        indicatorHeight = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, indicatorHeight, dm);
        underlineHeight = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, underlineHeight, dm);
        dividerPadding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dividerPadding, dm);
        tabPadding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, tabPadding, dm);
        dividerWidth = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dividerWidth, dm);
        tabTextSize = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, tabTextSize, dm);

        // get system attrs (android:textSize and android:textColor)

        TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);

        tabTextSize = a.getDimensionPixelSize(0, tabTextSize);
        //noinspection ResourceType
        tabTextColor = a.getColor(1, tabTextColor);

        a.recycle();

        // get custom attrs

        a = context.obtainStyledAttributes(attrs, PagerSlidingTabStrip);

		/*
         * indicatorColor =
		 * a.getColor(R.styleable.PagerSlidingTabStrip_pstsIndicatorColor,
		 * indicatorColor); underlineColor =
		 * a.getColor(R.styleable.PagerSlidingTabStrip_pstsUnderlineColor,
		 * underlineColor); dividerColor =
		 * a.getColor(R.styleable.PagerSlidingTabStrip_pstsDividerColor,
		 * dividerColor); indicatorHeight = a.getDimensionPixelSize(R.styleable.
		 * PagerSlidingTabStrip_pstsIndicatorHeight, indicatorHeight);
		 * underlineHeight = a.getDimensionPixelSize(R.styleable.
		 * PagerSlidingTabStrip_pstsUnderlineHeight, underlineHeight);
		 * dividerPadding = a.getDimensionPixelSize(R.styleable.
		 * PagerSlidingTabStrip_pstsDividerPadding, dividerPadding); tabPadding
		 * = a.getDimensionPixelSize(R.styleable.
		 * PagerSlidingTabStrip_pstsTabPaddingLeftRight, tabPadding);
		 * tabBackgroundResId =
		 * a.getResourceId(R.styleable.PagerSlidingTabStrip_pstsTabBackground,
		 * tabBackgroundResId); shouldExpand =
		 * a.getBoolean(R.styleable.PagerSlidingTabStrip_pstsShouldExpand,
		 * shouldExpand); scrollOffset =
		 * a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsScrollOffset
		 * , scrollOffset); textAllCaps =
		 * a.getBoolean(R.styleable.PagerSlidingTabStrip_pstsTextAllCaps,
		 * textAllCaps);
		 */

        a.recycle();

        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Style.FILL);

        dividerPaint = new Paint();
        dividerPaint.setAntiAlias(true);
        dividerPaint.setStrokeWidth(dividerWidth);

        if (locale == null) {
            locale = getResources().getConfiguration().locale;
        }
    }

    public void setViewPager(ViewPager pager) {
        this.pager = pager;

        if (pager.getAdapter() == null) {
            throw new IllegalStateException(
                    "ViewPager does not have adapter instance.");
        }

        pager.addOnPageChangeListener(pageListener);

        notifyDataSetChanged();
    }

    public void setIndicatorColor(int color) {
        indicatorColor = color;
    }

    public void setSelectedTabBackground(int resId) {
        selectedTabBackground = resId;
        selectWithBackground = true;
    }

    public void setSelectedTabSrc(int resId) {
        selectedTabBackground = resId;
        selectWithBackground = false;
        selectWithSrc = true;
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.delegatePageListener = listener;
    }

    public void notifyDataSetChanged() {

        tabsContainer.removeAllViews();

        tabCount = pager.getAdapter().getCount();

        for (int i = 0; i < tabCount; i++) {

            if (pager.getAdapter() instanceof IconTabProvider) {

                addIconTab(i,
                        ((IconTabProvider) pager.getAdapter())
                                .getPageIconResId(i));
            } else if (setTabWithCustomView) {
                addCustomTab(i);
            } else {
//                addTextTab(i);
            }

        }

        updateTabStyles();

        getViewTreeObserver().addOnGlobalLayoutListener(
                new OnGlobalLayoutListener() {

                    @SuppressWarnings("deprecation")
                    @SuppressLint("NewApi")
                    @Override
                    public void onGlobalLayout() {

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            getViewTreeObserver().removeGlobalOnLayoutListener(
                                    this);
                        } else {
                            getViewTreeObserver().removeOnGlobalLayoutListener(
                                    this);
                        }

                        currentPosition = pager.getCurrentItem();
                        scrollToChild(currentPosition, 0);
                    }
                });

    }

//    private void addTextTab(int position) {
//        getContext();
//        // TODO set Tab as TextView
//        LayoutInflater inflater = (LayoutInflater) getContext()
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        RelativeLayout tab = (RelativeLayout) inflater.inflate(
//                R.layout.profile_tab_item, ((ViewGroup) getParent()), false);
//
//        TextView textViewTabName = (TextView) tab.findViewById(R.id.textViewTabName);
//        TextView textViewTabNumber = (TextView) tab.findViewById(R.id.textViewNumber);
//
//        if (textTabNames.length > 0 && textTabNumbers.length > 0) {
//            textViewTabName.setText((position < textTabNames.length) ? textTabNames[position] : "");
//            textViewTabNumber.setText((position < textTabNumbers.length) ? textTabNumbers[position] + "" : "");
//        } else {
//            switch (position) {
//                case 0:
//                    textViewTabName.setText(getResources().getString(R.string.app_name));
//                    break;
//                case 1:
//                    textViewTabName.setText(getResources().getString(R.string.app_name));
//                    break;
//                case 2:
//                    textViewTabName.setText(getResources().getString(R.string.app_name));
//                    break;
//            }
//        }
//
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        lp.setMargins(tabPadding, 0, tabPadding, 0);
//
//        addTab(position, tab, lp);
//    }

    public void addTabCustomView(String[] items) {
        tabCustomViewItems = items;
        setTabWithCustomView = true;
    }

    public void setTextTabItems(String[] textTabNames, int[] textTabNumbers) {
        this.textTabNames = textTabNames;
        this.textTabNumbers = textTabNumbers;
        notifyDataSetChanged();
    }

    private void addCustomTab(int position) {

        TextViewRobotoRegular textViewTab = new TextViewRobotoRegular(getContext());

        textViewTab.setText(tabCustomViewItems[position]);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textViewTab.setTextColor(getResources().getColor(R.color.whiteHint, null));
        } else {
            //noinspection deprecation
            textViewTab.setTextColor(getResources().getColor(R.color.whiteHint));
        }
        textViewTab.setGravity(Gravity.CENTER);
        textViewTab.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.textSizeNormal));

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                .getMetrics(metrics);
        float windowX = metrics.widthPixels;

        lp.width = (int) (windowX / tabCustomViewItems.length);
        lp.setMargins(0, 0, 0, 0);

        addTab(position, textViewTab, lp);
    }

    private void addIconTab(final int position, int resId) {

        ImageButton tab = new ImageButton(getContext());
        tab.setImageResource(resId);
        // TODO set tab custom image

        tab.setScaleType(ImageButton.ScaleType.CENTER);

        int dimension = (int) (getContext().getResources().getDimension(R.dimen.pager_circle_radius) + 4);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dimension, dimension);
        lp.weight = 1;
        int margin;
        if (selectWithSrc) {
            margin = dimension / 4;
            lp.setMargins(margin, margin, margin, margin);
        } else {
            margin = dimension;
            lp.setMargins(margin, margin, margin, margin);
        }
        addTab(position, tab, lp);

    }

    private void addTab(final int position, View tab, LinearLayout.LayoutParams lp) {
        tab.setFocusable(true);
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(position);
            }
        });

        tabsContainer.addView(tab, position, lp);
    }

    private void updateTabStyles() {

        for (int i = 0; i < tabCount; i++) {

            View v = tabsContainer.getChildAt(i);

            int tabBackgroundResId = android.R.color.transparent;
            v.setBackgroundResource(tabBackgroundResId);

            if (v instanceof TextView) {

                TextView tab = (TextView) v;
                tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
                int tabTypefaceStyle = Typeface.BOLD;
                tab.setTypeface(tabTypeface, tabTypefaceStyle);
                tab.setTextColor(tabTextColor);

                // setAllCaps() is only available from API 14, so the upper case
                // is made manually if we are on a
                // pre-ICS-build
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    tab.setAllCaps(true);
                } else {
                    tab.setText(tab.getText().toString()
                            .toUpperCase(locale));
                }

            }
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    private void scrollToChild(int position, int offset) {

        if (tabCount == 0) {
            return;
        }

        int newScrollX = tabsContainer.getChildAt(position).getLeft() + offset;

        if (selectWithBackground) {
            if (lastEdited != null)
                //noinspection deprecation
                lastEdited.setBackgroundColor(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? getResources().getColor(android.R.color.transparent, null)
                        : getResources().getColor(android.R.color.transparent));
            View child = tabsContainer.getChildAt(position);
            child.setBackgroundResource(selectedTabBackground);

            lastEdited = child;
        } else if (selectWithSrc) {
            if (lastEdited != null)
                ((ImageButton) lastEdited).setImageResource(((IconTabProvider) pager.getAdapter())
                        .getPageIconResId(position));
            ImageButton child = (ImageButton) tabsContainer.getChildAt(position);
            child.setImageResource(selectedTabBackground);

            lastEdited = child;
        }


        if (position > 0 || offset > 0) {
            newScrollX -= scrollOffset;
        }

        if (newScrollX != lastScrollX) {
            lastScrollX = newScrollX;
            scrollTo(newScrollX, 0);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isInEditMode() || tabCount == 0) {
            return;
        }

        final int height = getHeight();

        // draw indicator line
        rectPaint.setColor(indicatorColor);

        // default: line below current tab
        View currentTab = tabsContainer.getChildAt(currentPosition);
        float lineLeft = currentTab.getLeft();
        float lineRight = currentTab.getRight();

        // if there is an offset, start interpolating left and right coordinates
        // between current and next tab
        if (currentPositionOffset > 0f && currentPosition < tabCount - 1) {

            View nextTab = tabsContainer.getChildAt(currentPosition + 1);
            final float nextTabLeft = nextTab.getLeft();
            final float nextTabRight = nextTab.getRight();

            lineLeft = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset)
                    * lineLeft);
            lineRight = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset)
                    * lineRight);
        }

        canvas.drawRect(lineLeft, height - indicatorHeight, lineRight, height,
                rectPaint);

        // draw underline

        int underlineColor = 0x00FFFFFF;
        rectPaint.setColor(underlineColor);
        canvas.drawRect(0, height - underlineHeight, tabsContainer.getWidth(),
                height, rectPaint);

        // draw divider

        int dividerColor = 0x00FFFFFF;
        dividerPaint.setColor(dividerColor);
        for (int i = 0; i < tabCount - 1; i++) {
            View tab = tabsContainer.getChildAt(i);
            canvas.drawLine(tab.getRight(), dividerPadding, tab.getRight(),
                    height - dividerPadding, dividerPaint);
        }
    }

    private class PageListener implements OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {

            currentPosition = position;
            currentPositionOffset = positionOffset;

            scrollToChild(position, (int) (positionOffset * tabsContainer
                    .getChildAt(position).getWidth()));

            invalidate();

            if (delegatePageListener != null) {
                delegatePageListener.onPageScrolled(position, positionOffset,
                        positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                scrollToChild(pager.getCurrentItem(), 0);
            }

            if (delegatePageListener != null) {
                delegatePageListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (delegatePageListener != null) {
                delegatePageListener.onPageSelected(position);
            }
        }

    }

    public void setTextSize(int textSizePx) {
        this.tabTextSize = textSizePx;
        updateTabStyles();
    }

    public int getTextSize() {
        return tabTextSize;
    }

    public void setTextColor(int textColor) {
        this.tabTextColor = textColor;
        updateTabStyles();
    }

    public int getTextColor() {
        return tabTextColor;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        currentPosition = savedState.currentPosition;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPosition = currentPosition;
        return savedState;
    }

    static class SavedState extends BaseSavedState {
        int currentPosition;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPosition = in.readInt();
        }

        @Override
        public void writeToParcel(@NonNull Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPosition);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

}
