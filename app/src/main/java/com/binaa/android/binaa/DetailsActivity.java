package com.binaa.android.binaa;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.binaa.android.binaa.models.Car;
import com.binaa.android.binaa.models.Image;
import com.binaa.android.binaa.models.Property;
import com.binaa.android.binaa.models.Service;
import com.binaa.android.binaa.server.ContentVolley;
import com.binaa.android.binaa.utils.ApplicationBase;
import com.binaa.android.binaa.utils.MyContextWrapper;
import com.binaa.android.binaa.views.PagerSlidingTabStrip;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Muhammad on 8/6/2017
 */

public class DetailsActivity extends AppCompatActivity {

    public static String ITEM_TYPE = "item_type";
    public static String CAR = "car";
    public static String PROPERTY = "property";
    public static String IS_HOTEL = "isHotel";
    public static String SERVICE = "service";


    private final String TAG = this.toString();

    private TextView textViewDescription;
    private TextView textViewPrice;
    private TextView textViewCode;
    private TextView textView;
    private TextView textViewArea;
    private TextView textViewBedrooms;
    private TextView textViewBathrooms;

    private ImageView imageViewFacebook;
    private ImageView imageViewTwitter;
    private ImageView imageViewInstagram;

    private Button buttonCall;
    private ViewPager viewPager;
    private RelativeLayout relativeLayoutLoading;
    private RelativeLayout relativeLayoutRelated;
    private PagerSlidingTabStrip pagerSlidingTabStrip;
    private LinearLayout LinearLayoutSocial;
    private RelativeLayout relativeLayoutDetails;
    private RecyclerView recycler;

    private DetailsType detailsType;
    private Car car;
    private Property property;
    private Service service;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setToolBar();

        textViewDescription = findViewById(R.id.textViewDescription);
        textViewPrice = findViewById(R.id.textViewPrice);
        textViewCode = findViewById(R.id.textViewCode);
        textView = findViewById(R.id.textView);
        textViewBathrooms = findViewById(R.id.textViewBathrooms);
        textViewBedrooms = findViewById(R.id.textViewBedrooms);
        textViewArea = findViewById(R.id.textViewArea);

        imageViewFacebook = findViewById(R.id.imageViewFacebook);
        imageViewTwitter = findViewById(R.id.imageViewTwitter);
        imageViewInstagram = findViewById(R.id.imageViewInstagram);

        buttonCall = findViewById(R.id.buttonCall);
        viewPager = findViewById(R.id.viewPager);
        pagerSlidingTabStrip = findViewById(R.id.pagerSlidingTabStrip);
        recycler = findViewById(R.id.recycler);

        relativeLayoutLoading = findViewById(R.id.relativeLayoutLoading);
        relativeLayoutRelated = findViewById(R.id.relativeLayoutRelated);
        LinearLayoutSocial = findViewById(R.id.LinearLayoutSocial);
        relativeLayoutDetails = findViewById(R.id.relativeLayoutDetails);

        detailsType = (DetailsType) getIntent().getSerializableExtra(ITEM_TYPE);
        car = getIntent().getParcelableExtra(CAR);
        property = getIntent().getParcelableExtra(PROPERTY);
        service = getIntent().getParcelableExtra(SERVICE);
        boolean isHotel = getIntent().getBooleanExtra(IS_HOTEL, false);

        initializeData();

        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        firebaseAnalytics.setAnalyticsCollectionEnabled(true);

        if (detailsType == DetailsType.Properties) {
            Content content = new Content();
            if (isHotel) {
                content.getRelatedHotels(property.getId());
            } else {
                content.getRelatedApartments(property.getId());
            }
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        Locale languageType = ApplicationBase.getInstance().getLocale();
        super.attachBaseContext(MyContextWrapper.wrap(newBase, languageType));
    }


    private void initializeData() {
        switch (detailsType) {
            case Properties:
                textViewPrice.setText(String.format("%s - %s %s", property.getPrice(), property.getPriceMonth(), getResources().getString(R.string.egp)));
                textViewCode.setText(String.format("%s %s", getResources().getString(R.string.property_code), property.getCode()));
                textViewArea.setText(String.format("%s%s%s", getString(R.string.area), property.getArea(), getString(R.string.m_)));
                textViewBedrooms.setText(String.format("%s %s", getString(R.string.bedrooms), property.getBedrooms()));
                textViewBathrooms.setText(String.format("%s %s", getString(R.string.bathrooms), property.getBathrooms()));
                relativeLayoutDetails.setVisibility(View.VISIBLE);

                imageViewInstagram.setOnClickListener(new Listeners(property.getInstagram()));
                imageViewFacebook.setOnClickListener(new Listeners(property.getFacebook()));
                imageViewTwitter.setOnClickListener(new Listeners(property.getTwitter()));

                buttonCall.setOnClickListener(new Listeners(Integer.valueOf(property.getPhoneNumber())));

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

        Toolbar toolBar = findViewById(R.id.toolbar);
        View actionBarView = getLayoutInflater().inflate(R.layout.toolbar_customview, toolBar, false);

        setSupportActionBar(toolBar);
        //noinspection ConstantConditions
        getSupportActionBar().setTitle("");
        TextView textViewActivityTitle = actionBarView.findViewById(R.id.textViewActivityTitle);


        // Set up the drawer.
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(actionBarView);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        textViewActivityTitle.setText(getString(R.string.details));

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

    public void isLoading(boolean isLoading) {
        relativeLayoutLoading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    public enum DetailsType {
        Properties, Cars, Services
    }

    private class Listeners implements View.OnClickListener {

        String link;
        int callNumber;

        public Listeners(String link) {
            this.link = link;
        }

        public Listeners(int callNumber) {
            this.callNumber = callNumber;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String facebook = "http://www.facebook.com";
            String twitter = "http://www.twitter.com";
            String instagram = "http://www.instagram.com";
            if (v == imageViewFacebook) {
                if (!link.isEmpty() || link != null) {
                    intent.setData(Uri.parse("http://" + link));
                    startActivity(intent);
                } else {
                    intent.setData(Uri.parse(facebook));
                    startActivity(intent);
                }
            } else if (v == imageViewTwitter) {
                if (!link.isEmpty() || link != null) {
                    intent.setData(Uri.parse("http://" + link));
                    startActivity(intent);
                } else {
                    intent.setData(Uri.parse(twitter));
                    startActivity(intent);
                }
            } else if (v == imageViewInstagram) {
                if (!link.isEmpty() || link != null) {
                    intent.setData(Uri.parse("http://" + link));
                    startActivity(intent);
                } else {
                    intent.setData(Uri.parse(instagram));
                    startActivity(intent);
                }
            } else {
                Intent intentCall = new Intent(Intent.ACTION_DIAL);
                intentCall.setData(Uri.parse("tel:0" + String.valueOf(callNumber)));
                startActivity(intentCall);
            }
        }
    }

    public class RelatedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private ArrayList<Property> properties;


        private LayoutInflater inflater;

        public RelatedAdapter(ArrayList<Property> properties) {

            inflater = getLayoutInflater();
            this.properties = properties;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view;
            RecyclerView.ViewHolder holder;

            //inflater your layout and pass it to view holder
            view = inflater.inflate(R.layout.related_item, viewGroup, false);
            holder = new ItemHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams();
            layoutParams.setFullSpan(true);

            layoutParams.width = (int) (recycler.getMeasuredWidth() / 1.5);
            layoutParams.height = StaggeredGridLayoutManager.LayoutParams.MATCH_PARENT;

            ItemHolder itemHolder = (ItemHolder) viewHolder;
            itemHolder.setDetails(properties.get(position));
        }

        @Override
        public int getItemCount() {
            return properties.size();
        }

        public class ItemHolder extends RecyclerView.ViewHolder {

            ImageView imageViewCover;
            TextView textViewPrice;
            TextView textViewCode;
            TextView textViewDescription;

            Property property;

            public ItemHolder(View itemView) {
                super(itemView);

                imageViewCover = itemView.findViewById(R.id.imageViewCover);
                textViewPrice = itemView.findViewById(R.id.textViewPrice);
                textViewCode = itemView.findViewById(R.id.textViewCode);
                textViewDescription = itemView.findViewById(R.id.textViewDescription);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(DetailsActivity.this, DetailsActivity.class);
                        intent.putExtra(PROPERTY, property);
                        intent.putExtra(ITEM_TYPE, DetailsActivity.DetailsType.Properties);
                        startActivity(intent);
                        finish();
                    }
                });
            }

            public void setDetails(Property property) {
                this.property = property;

                if (!property.getImagesLinks().isEmpty()) {
                    Picasso.with(getApplicationContext()).load(property.getImagesLinks().get(0).getImageUrl()).
                            placeholder(R.drawable.placeholder).fit().centerCrop().fit().centerCrop().
                            error(R.drawable.ic_warning).
                            into(imageViewCover);
                } else {
                    imageViewCover.setImageResource(R.drawable.ic_warning);
                }

                textViewPrice.setText(String.format("%s - %s %s", property.getPrice(), property.getPriceMonth(), getResources().getString(R.string.egp)));
                textViewCode.setText(String.format("%s %s", getResources().getString(R.string.property_code), property.getCode()));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    textViewDescription.setText(Html.fromHtml(property.getDescription(), Html.FROM_HTML_OPTION_USE_CSS_COLORS));
                } else {
                    //noinspection deprecation
                    textViewDescription.setText(Html.fromHtml(property.getDescription()));
                }
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

            ImageView imageViewCover = itemView.findViewById(R.id.imageViewCover);

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

    private class Content extends ContentVolley {

        public Content() {
            super(TAG, getApplicationContext());
        }

        @Override
        protected void onPreExecute(ActionType actionType) {
            isLoading(true);
        }

        @Override
        protected void onPostExecuteGetProperties(ActionType actionType, boolean success, String message, ArrayList<Property> properties) {
            isLoading(false);
            if (success) {
                relativeLayoutRelated.setVisibility(View.VISIBLE);

                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                float windowY = metrics.heightPixels;

                recycler.getLayoutParams().height = (int) (windowY / 2.5);

                StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
                layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
                recycler.setLayoutManager(layoutManager);
                recycler.setHasFixedSize(false);
                recycler.setItemAnimator(new DefaultItemAnimator());
                recycler.setAdapter(new RelatedAdapter(properties));

            } else {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        }
    }

}
