package com.binaa.android.binaa;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.binaa.android.binaa.models.Car;
import com.binaa.android.binaa.models.Image;
import com.binaa.android.binaa.models.Property;
import com.binaa.android.binaa.models.Service;
import com.binaa.android.binaa.views.PagerSlidingTabStrip;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Muhammad on 8/6/2017
 */

public class DetailsActivity extends AppCompatActivity {

    public static String ITEM_TYPE = "item_type";
    public static String IS_CAR = "isCar";
    public static String IS_PROPERTY = "isProperty";
    public static String IS_SERVICE = "isService";

    @BindView(R.id.textViewDescription)
    TextView textViewDescription;
    @BindView(R.id.textViewPrice)
    TextView textViewPrice;
    @BindView(R.id.textViewCode)
    TextView textViewCode;
    @BindView(R.id.textViewTitle)
    TextView textViewTitle;
    @BindView(R.id.imageViewFacebook)
    ImageView imageViewFacebook;
    @BindView(R.id.imageViewTwitter)
    ImageView imageViewTwitter;
    @BindView(R.id.imageViewInstagram)
    ImageView imageViewInstagram;
    @BindView(R.id.buttonCall)
    Button buttonCall;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.relativeLayoutLoading)
    RelativeLayout relativeLayoutLoading;
    @BindView(R.id.pagerSlidingTabStrip)
    PagerSlidingTabStrip pagerSlidingTabStrip;
    @BindView(R.id.LinearLayoutSocial)
    LinearLayout LinearLayoutSocial;
    @BindView(R.id.textView)
    TextView textView;

    private TextView textViewActivityTitle;
    private DetailsType detailsType;
    private Car car;
    private Property property;
    private Service service;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setToolBar();
        ButterKnife.bind(this);

        detailsType = (DetailsType) getIntent().getSerializableExtra(ITEM_TYPE);
        car = getIntent().getParcelableExtra(IS_CAR);
        property = getIntent().getParcelableExtra(IS_PROPERTY);
        service = getIntent().getParcelableExtra(IS_SERVICE);

        initializeData();

        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        firebaseAnalytics.setAnalyticsCollectionEnabled(true);
    }


    private void initializeData() {
        switch (detailsType) {
            case Properties:
                textViewPrice.setText(property.getPrice() + " " + getResources().getString(R.string.egp));
                textViewCode.setText(getResources().getString(R.string.property_code) + " " + property.getCode());
                textViewTitle.setText(property.getTitle());

                imageViewInstagram.setOnClickListener(new Listeners(property.getInstagram()));
                imageViewFacebook.setOnClickListener(new Listeners(property.getFacebook()));
                imageViewTwitter.setOnClickListener(new Listeners(property.getTwitter()));

                buttonCall.setOnClickListener(new Listeners(property.getPhoneNumber()));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    textViewDescription.setText(Html.fromHtml(property.getDescription(), Html.FROM_HTML_OPTION_USE_CSS_COLORS));
                } else {
                    //noinspection deprecation
                    textViewDescription.setText(Html.fromHtml(property.getDescription()));
                }

                viewPager.setAdapter(new PageAdapter(property.getImagesLinks()));

                if (Build.VERSION.SDK_INT > 22) {
                    pagerSlidingTabStrip.setIndicatorColor(getResources().getColor(android.R.color.transparent, null));
                } else {
                    // noinspection deprecation
                    getResources().getColor(android.R.color.transparent);
                }
                pagerSlidingTabStrip.setSelectedTabSrc(R.drawable.circle_gray_dark);
                pagerSlidingTabStrip.setViewPager(viewPager);
                break;
            case Cars:
                textViewPrice.setVisibility(View.INVISIBLE);
                textViewCode.setVisibility(View.INVISIBLE);
                textViewTitle.setVisibility(View.INVISIBLE);
                LinearLayoutSocial.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    textViewDescription.setText(Html.fromHtml(car.getDescription(), Html.FROM_HTML_OPTION_USE_CSS_COLORS));
                } else {
                    //noinspection deprecation
                    textViewDescription.setText(Html.fromHtml(car.getDescription()));
                }

                viewPager.setAdapter(new PageAdapter(car.getImages()));

                if (Build.VERSION.SDK_INT > 22) {
                    pagerSlidingTabStrip.setIndicatorColor(getResources().getColor(android.R.color.transparent, null));
                } else {
                    // noinspection deprecation
                    getResources().getColor(android.R.color.transparent);
                }
                pagerSlidingTabStrip.setSelectedTabSrc(R.drawable.circle_gray_dark);
                pagerSlidingTabStrip.setViewPager(viewPager);
                break;
            case Services:
                textViewPrice.setVisibility(View.INVISIBLE);
                textViewCode.setVisibility(View.INVISIBLE);
                LinearLayoutSocial.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                textViewTitle.setText(service.getTitle());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    textViewDescription.setText(Html.fromHtml(service.getDescription(), Html.FROM_HTML_OPTION_USE_CSS_COLORS));
                } else {
                    //noinspection deprecation
                    textViewDescription.setText(Html.fromHtml(service.getDescription()));
                }

                ArrayList<Image> images = new ArrayList<>();
                Image image = new Image();
                image.setImageUrl(service.getImage());
                images.add(image);
                viewPager.setAdapter(new PageAdapter(images));

                if (Build.VERSION.SDK_INT > 22) {
                    pagerSlidingTabStrip.setIndicatorColor(getResources().getColor(android.R.color.transparent, null));
                } else {
                    // noinspection deprecation
                    getResources().getColor(android.R.color.transparent);
                }
                pagerSlidingTabStrip.setSelectedTabSrc(R.drawable.circle_gray_dark);
                pagerSlidingTabStrip.setViewPager(viewPager);
                break;
        }
    }

    private void setToolBar() {

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        View actionBarView = getLayoutInflater().inflate(R.layout.toolbar_customview, toolBar, false);

        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("");
        textViewActivityTitle = (TextView) actionBarView.findViewById(R.id.textViewActivityTitle);


        // Set up the drawer.
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(actionBarView);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setActivityTitle("التفاصيل");

    }

    public void setActivityTitle(String title) {
        if (textViewActivityTitle != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                textViewActivityTitle.setText(Html.fromHtml("<font color='#ffffff'>" + title + "</font>", Html.FROM_HTML_OPTION_USE_CSS_COLORS));
            } else {
                //noinspection deprecation
                textViewActivityTitle.setText(Html.fromHtml("<font color='#ffffff'>" + title + "</font>"));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public enum DetailsType {
        Properties, Cars, Services
    }

    private class Listeners implements View.OnClickListener {

        String link;

        public Listeners(String link) {
            this.link = link;
        }

        public Listeners() {
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String facebook = "http://www.facebook.com";
            String twitter = "http://www.twitter.com";
            String instagram = "http://www.instagram.com";
            if (v == imageViewFacebook) {
                if (!link.isEmpty() || link != null) {
                    intent.setData(Uri.parse(link));
                    startActivity(intent);
                } else {
                    intent.setData(Uri.parse(facebook));
                    startActivity(intent);
                }
            } else if (v == imageViewTwitter) {
                if (!link.isEmpty() || link != null) {
                    intent.setData(Uri.parse(link));
                    startActivity(intent);
                } else {
                    intent.setData(Uri.parse(twitter));
                    startActivity(intent);
                }
            } else if (v == imageViewInstagram) {
                if (!link.isEmpty() || link != null) {
                    intent.setData(Uri.parse(link));
                    startActivity(intent);
                } else {
                    intent.setData(Uri.parse(instagram));
                    startActivity(intent);
                }
            } else {
                Intent intentCall = new Intent(Intent.ACTION_DIAL);
                intentCall.setData(Uri.parse(link));
                startActivity(intentCall);
            }
        }
    }

    private class PageAdapter extends PagerAdapter implements PagerSlidingTabStrip.IconTabProvider {

        private LayoutInflater inflater;
        private ArrayList<Image> images;

        public PageAdapter(ArrayList<Image> images) {
            this.inflater = getLayoutInflater();
            this.images = images;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public float getPageWidth(int position) {
            return 1f;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View itemView = inflater.inflate(R.layout.header_item, container, false);

            ImageView imageViewCover = (ImageView) itemView.findViewById(R.id.imageViewCover);

            Picasso.with(DetailsActivity.this).load(images.get(position).getImageUrl()).
                    placeholder(R.drawable.placeholder).fit().centerCrop().
                    error(R.drawable.ic_warning).
                    into(imageViewCover);

            container.addView(itemView);

            return itemView;
        }

        @Override
        public int getPageIconResId(int position) {
            return R.drawable.circle_gray_dark_stroke;
        }
    }

}
