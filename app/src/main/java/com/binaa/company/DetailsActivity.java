package com.binaa.company;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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

import com.binaa.company.models.Car;
import com.binaa.company.models.Hotel;
import com.binaa.company.models.Image;
import com.binaa.company.models.Property;
import com.binaa.company.models.Service;
import com.binaa.company.server.ContentVolley;
import com.binaa.company.utils.ApplicationBase;
import com.binaa.company.utils.BaseDialogHolder;
import com.binaa.company.utils.MyContextWrapper;
import com.binaa.company.utils.PhonesDialogHolder;
import com.binaa.company.views.PagerSlidingTabStrip;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import static com.binaa.company.ReservationActivity.ID;
import static com.binaa.company.ReservationActivity.TYPE;

/**
 * Created by Muhammad on 8/6/2017
 */

public class DetailsActivity extends AppCompatActivity {

    public static String ITEM_TYPE = "item_type";
    public static String ID_KEY = "id";


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
    private View view;
    private Button buttonCall;
    private Button buttonReserve;
    private ViewPager viewPager;
    private RelativeLayout relativeLayoutLoading;
    private RelativeLayout relativeLayoutRelated;
    private PagerSlidingTabStrip pagerSlidingTabStrip;
    private LinearLayout LinearLayoutSocial;
    private RelativeLayout relativeLayoutDetails;
    private RecyclerView recycler;
    private DetailsType detailsType;
    private int id;

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
        buttonReserve = findViewById(R.id.buttonReserve);

        view = findViewById(R.id.view);

        viewPager = findViewById(R.id.viewPager);
        pagerSlidingTabStrip = findViewById(R.id.pagerSlidingTabStrip);
        recycler = findViewById(R.id.recycler);

        relativeLayoutLoading = findViewById(R.id.relativeLayoutLoading);
        relativeLayoutRelated = findViewById(R.id.relativeLayoutRelated);
        LinearLayoutSocial = findViewById(R.id.LinearLayoutSocial);
        relativeLayoutDetails = findViewById(R.id.relativeLayoutDetails);

        detailsType = (DetailsType) getIntent().getSerializableExtra(ITEM_TYPE);
        id = getIntent().getIntExtra(ID_KEY, 0);

        initializeData();

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        Locale languageType = ApplicationBase.getInstance().getLocale();
        super.attachBaseContext(MyContextWrapper.wrap(newBase, languageType));
    }

    private void initializeData() {
        Content content = new Content();
        switch (detailsType) {
            case Properties:
                content.getPropertyDetails(id);
                break;
            case Hotels:
                content.getHotelDetails(id);
                break;
            case Cars:
                content.getCarDetails(id);
                break;
            case Services:
                content.getServiceDetails(id);
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
        Properties, Hotels, Cars, Services
    }

    private class Listeners implements View.OnClickListener {

        String link;

        Listeners(String link) {
            this.link = link;
        }

        Listeners() {
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String facebook = "http://www.facebook.com";
            String twitter = "http://www.twitter.com";
            String instagram = "http://www.instagram.com";
            if (v == buttonCall) {
                if (link.contains("/")) {
                    String[] strings = link.split("/");
                    BaseDialogHolder dialogHolder = new PhonesDialogHolder(DetailsActivity.this, strings);
                    dialogHolder.showDialog();
                } else {
                    Intent intentCall = new Intent(Intent.ACTION_DIAL);
                    intentCall.setData(Uri.parse("tel:" + link));
                    startActivity(intentCall);
                }
            } else if (v == imageViewFacebook) {
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
            } else if (v == buttonReserve) {
                Intent intent1 = new Intent(DetailsActivity.this, ReservationActivity.class);
                intent1.putExtra(TYPE, detailsType);
                intent1.putExtra(ID, id);
                startActivity(intent1);
            }
        }
    }

    public class RelatedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private ArrayList<Property> properties;


        private LayoutInflater inflater;

        private RelatedAdapter(ArrayList<Property> properties) {

            inflater = getLayoutInflater();
            this.properties = properties;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            View view;
            RecyclerView.ViewHolder holder;

            //inflater your layout and pass it to view holder
            view = inflater.inflate(R.layout.related_item, viewGroup, false);
            holder = new ItemHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

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

            private ItemHolder(View itemView) {
                super(itemView);

                imageViewCover = itemView.findViewById(R.id.imageViewCover);
                textViewPrice = itemView.findViewById(R.id.textViewPrice);
                textViewCode = itemView.findViewById(R.id.textViewCode);
                textViewDescription = itemView.findViewById(R.id.textViewDescription);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(DetailsActivity.this, DetailsActivity.class);
                        intent.putExtra(ID_KEY, property.getId());
                        intent.putExtra(ITEM_TYPE, DetailsActivity.DetailsType.Properties);
                        startActivity(intent);
                        finish();
                    }
                });
            }

            public void setDetails(Property property) {
                this.property = property;

                if (!property.getCoverPic().isEmpty()) {
                    Picasso.with(getApplicationContext()).load(property.getCoverPic()).
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

    public class RelatedHotelsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private ArrayList<Hotel> hotels;


        private LayoutInflater inflater;

        private RelatedHotelsAdapter(ArrayList<Hotel> hotels) {

            inflater = getLayoutInflater();
            this.hotels = hotels;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            View view;
            RecyclerView.ViewHolder holder;

            //inflater your layout and pass it to view holder
            view = inflater.inflate(R.layout.related_item, viewGroup, false);
            holder = new ItemHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams();
            layoutParams.setFullSpan(true);

            layoutParams.width = (int) (recycler.getMeasuredWidth() / 1.5);
            layoutParams.height = StaggeredGridLayoutManager.LayoutParams.MATCH_PARENT;

            ItemHolder itemHolder = (ItemHolder) viewHolder;
            itemHolder.setDetails(hotels.get(position));
        }

        @Override
        public int getItemCount() {
            return hotels.size();
        }

        public class ItemHolder extends RecyclerView.ViewHolder {

            ImageView imageViewCover;
            TextView textViewPrice;
            TextView textViewCode;
            TextView textViewDescription;

            Hotel hotel;

            private ItemHolder(View itemView) {
                super(itemView);

                imageViewCover = itemView.findViewById(R.id.imageViewCover);
                textViewPrice = itemView.findViewById(R.id.textViewPrice);
                textViewCode = itemView.findViewById(R.id.textViewCode);
                textViewDescription = itemView.findViewById(R.id.textViewDescription);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(DetailsActivity.this, DetailsActivity.class);
                        intent.putExtra(ID_KEY, hotel.getId());
                        intent.putExtra(ITEM_TYPE, DetailsType.Hotels);
                        startActivity(intent);
                        finish();
                    }
                });
            }

            public void setDetails(Hotel hotel) {
                this.hotel = hotel;

                if (!hotel.getCoverPic().isEmpty()) {
                    Picasso.with(getApplicationContext()).load(hotel.getCoverPic()).
                            placeholder(R.drawable.placeholder).fit().centerCrop().fit().centerCrop().
                            error(R.drawable.ic_warning).
                            into(imageViewCover);
                } else {
                    imageViewCover.setImageResource(R.drawable.ic_warning);
                }

                textViewPrice.setText(String.format("%s - %s %s %s", hotel.getPrice(), hotel.getPriceMonth(), getResources().getString(R.string.dolar), getResources().getString(R.string.including)));
                textViewCode.setText(String.format("%s", hotel.getTitle()));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    textViewDescription.setText(Html.fromHtml(hotel.getDescription(), Html.FROM_HTML_OPTION_USE_CSS_COLORS));
                } else {
                    //noinspection deprecation
                    textViewDescription.setText(Html.fromHtml(hotel.getDescription()));
                }
            }

        }
    }


    private class PageAdapter extends PagerAdapter implements PagerSlidingTabStrip.IconTabProvider {

        private LayoutInflater inflater;
        private ArrayList<Image> images;

        private PageAdapter(ArrayList<Image> images) {
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
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {

            View itemView = inflater.inflate(R.layout.header_item, container, false);

            ImageView imageViewCover = itemView.findViewById(R.id.imageViewCover);

            Picasso.with(DetailsActivity.this).load(images.get(position).getImageUrl()).
                    placeholder(R.drawable.placeholder).fit().centerCrop().
                    error(R.drawable.ic_warning).
                    into(imageViewCover);

            container.addView(itemView);

            imageViewCover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DetailsActivity.this, FullScreenViewActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("images", images);
                    startActivity(intent);
                }
            });

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

        @Override
        protected void onPostExecuteGetHotels(ActionType actionType, boolean success, String message, ArrayList<Hotel> hotels) {
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
                recycler.setAdapter(new RelatedHotelsAdapter(hotels));

            } else {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPostExecuteGetPropertyDetails(ActionType actionType, boolean success, String message, Property property) {
            isLoading(false);
            if (success) {
                getRelatedApartments(property.getId());

                textViewPrice.setText(String.format("%s - %s %s", property.getPrice(), property.getPriceMonth(), getResources().getString(R.string.egp)));
                textViewCode.setText(String.format("%s %s", getResources().getString(R.string.property_code), property.getCode()));
                textViewArea.setText(String.format("%s%s%s", getString(R.string.area), property.getArea(), getString(R.string.m_)));
                textViewBedrooms.setText(String.format("%s %s", getString(R.string.bedrooms), property.getBedrooms()));
                textViewBathrooms.setText(String.format("%s %s", getString(R.string.bathrooms), property.getBathrooms()));
                relativeLayoutDetails.setVisibility(View.VISIBLE);

                imageViewInstagram.setOnClickListener(new Listeners(property.getInstagram()));
                imageViewFacebook.setOnClickListener(new Listeners(property.getFacebook()));
                imageViewTwitter.setOnClickListener(new Listeners(property.getTwitter()));


                buttonCall.setOnClickListener(new Listeners(property.getPhoneNumber()));
                buttonReserve.setOnClickListener(new Listeners());


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
            } else {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPostExecuteGetHotelDetails(ActionType actionType, boolean success, String message, Hotel hotel) {
            isLoading(false);
            if (success) {
                getRelatedHotels(hotel.getId());

                textViewPrice.setText(String.format("%s - %s %s %s", hotel.getPrice(), hotel.getPriceMonth(), getResources().getString(R.string.dolar), getResources().getString(R.string.including)));
                textViewCode.setText(String.format("%s", hotel.getTitle()));

                imageViewInstagram.setOnClickListener(new Listeners(hotel.getInstagram()));
                imageViewFacebook.setOnClickListener(new Listeners(hotel.getFacebook()));
                imageViewTwitter.setOnClickListener(new Listeners(hotel.getTwitter()));


                buttonCall.setOnClickListener(new Listeners(hotel.getPhoneNumber()));
                buttonReserve.setOnClickListener(new Listeners());


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    textViewDescription.setText(Html.fromHtml(hotel.getDescription(), Html.FROM_HTML_OPTION_USE_CSS_COLORS));
                } else {
                    //noinspection deprecation
                    textViewDescription.setText(Html.fromHtml(hotel.getDescription()));
                }

                viewPager.setAdapter(new PageAdapter(hotel.getImagesLinks()));

                if (Build.VERSION.SDK_INT > 22) {
                    pagerSlidingTabStrip.setIndicatorColor(getResources().getColor(android.R.color.transparent, null));
                } else {
                    // noinspection deprecation
                    getResources().getColor(android.R.color.transparent);
                }
                pagerSlidingTabStrip.setSelectedTabSrc(R.drawable.circle_gray_dark);
                pagerSlidingTabStrip.setViewPager(viewPager);
            } else {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPostExecuteGetCarDetails(ActionType actionType, boolean success, String message, Car car) {
            isLoading(false);
            if (success) {
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
                buttonCall.setOnClickListener(new Listeners("01027567799"));
                buttonReserve.setOnClickListener(new Listeners());

                viewPager.setAdapter(new PageAdapter(car.getImages()));

                if (Build.VERSION.SDK_INT > 22) {
                    pagerSlidingTabStrip.setIndicatorColor(getResources().getColor(android.R.color.transparent, null));
                } else {
                    // noinspection deprecation
                    getResources().getColor(android.R.color.transparent);
                }
                pagerSlidingTabStrip.setSelectedTabSrc(R.drawable.circle_gray_dark);
                pagerSlidingTabStrip.setViewPager(viewPager);
            } else {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPostExecuteGetServiceDetails(ActionType actionType, boolean success, String message, Service service) {
            isLoading(false);
            if (success) {
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

                buttonCall.setOnClickListener(new Listeners("01027567799"));
                buttonReserve.setVisibility(View.GONE);
                view.setVisibility(View.GONE);

                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) buttonCall.getLayoutParams();
                layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
                buttonCall.setLayoutParams(layoutParams);

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
            } else {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        }
    }

}
