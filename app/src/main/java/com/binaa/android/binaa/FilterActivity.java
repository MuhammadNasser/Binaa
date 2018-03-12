package com.binaa.android.binaa;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.binaa.android.binaa.models.Car;
import com.binaa.android.binaa.models.Property;
import com.binaa.android.binaa.server.ContentVolley;
import com.binaa.android.binaa.utils.ApplicationBase;
import com.binaa.android.binaa.utils.MyContextWrapper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import static com.binaa.android.binaa.DetailsActivity.ID_KEY;
import static com.binaa.android.binaa.DetailsActivity.IS_HOTEL;
import static com.binaa.android.binaa.DetailsActivity.ITEM_TYPE;


/**
 * Created by Muhammad on 8/6/2017
 */

public class FilterActivity extends AppCompatActivity {

    public static String TYPE = "type";
    public static String apartments = "apartments";
    public static String hotels = "hotels";
    public static String cars = "cars";

    private final String TAG = this.toString();

    private EditText editTextBedrooms, editTextMinPrice, editTextMaxPrice;
    private TextView textView;
    private RelativeLayout relativeLayoutLoading;
    private LinearLayout LinearLayoutFields;
    private RecyclerView recyclerView;
    private String type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        setToolBar();

        type = getIntent().getStringExtra(TYPE);

        editTextBedrooms = findViewById(R.id.editTextBedrooms);
        editTextMinPrice = findViewById(R.id.editTextMinPrice);
        editTextMaxPrice = findViewById(R.id.editTextMaxPrice);
        textView = findViewById(R.id.textView);
        LinearLayoutFields = findViewById(R.id.LinearLayoutFields);

        Button buttonSearch = findViewById(R.id.buttonSearch);
        recyclerView = findViewById(R.id.recyclerView);

        if (type.contains(cars)) {
            editTextBedrooms.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
        }

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String number_of_bedrooms = editTextBedrooms.getText().toString();
                String minPrice = editTextMinPrice.getText().toString();
                String maxPrice = editTextMaxPrice.getText().toString();

                Content content = new Content();
                if (type.contains(apartments)) {
                    content.search(apartments, minPrice, maxPrice, number_of_bedrooms);
                } else if (type.contains(hotels)) {
                    content.search(hotels, minPrice, maxPrice, number_of_bedrooms);
                } else {
                    content.searchCars(minPrice, maxPrice);
                }
            }
        });

        relativeLayoutLoading = findViewById(R.id.relativeLayoutLoading);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        Locale languageType = ApplicationBase.getInstance().getLocale();
        super.attachBaseContext(MyContextWrapper.wrap(newBase, languageType));
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
        textViewActivityTitle.setText(getString(R.string.search));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (recyclerView.getVisibility() == View.VISIBLE) {
            LinearLayoutFields.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    public void isLoading(boolean isLoading) {
        relativeLayoutLoading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    public class PropertiesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private ArrayList<Property> properties;

        private LayoutInflater inflater;

        public PropertiesAdapter(ArrayList<Property> properties) {
            inflater = getLayoutInflater();
            this.properties = properties;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view;
            RecyclerView.ViewHolder holder;

            //inflater your layout and pass it to view holder
            view = inflater.inflate(R.layout.home_list_item, viewGroup, false);
            holder = new ItemHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams();
            layoutParams.setFullSpan(true);
            layoutParams.height = (int) (recyclerView.getMeasuredHeight() * 0.75);

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
                        Intent intent = new Intent(FilterActivity.this, DetailsActivity.class);
                        intent.putExtra(ID_KEY, property.getId());
                        intent.putExtra(IS_HOTEL, false);
                        intent.putExtra(ITEM_TYPE, DetailsActivity.DetailsType.Properties);
                        startActivity(intent);
                    }
                });
            }

            @SuppressLint("SetTextI18n")
            public void setDetails(Property property) {
                this.property = property;

                if (!property.getCoverPic().isEmpty()) {
                    Picasso.with(FilterActivity.this).load(property.getCoverPic()).
                            placeholder(R.drawable.placeholder).fit().centerCrop().
                            error(R.drawable.ic_warning).
                            into(imageViewCover);
                } else {
                    imageViewCover.setImageResource(R.drawable.ic_warning);
                }
                textViewPrice.setText(String.format("%s - %s %s", property.getPrice(), property.getPriceMonth(), getResources().getString(R.string.egp)));
                textViewCode.setText(getResources().getString(R.string.property_code) + " " + property.getCode());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    textViewDescription.setText(Html.fromHtml(property.getDescription(), Html.FROM_HTML_OPTION_USE_CSS_COLORS));
                } else {
                    //noinspection deprecation
                    textViewDescription.setText(Html.fromHtml(property.getDescription()));
                }
            }

        }
    }

    public class CarsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private ArrayList<Car> cars;


        private LayoutInflater inflater;

        public CarsAdapter(ArrayList<Car> cars) {
            inflater = getLayoutInflater();
            this.cars = cars;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view;
            RecyclerView.ViewHolder holder;

            //inflater your layout and pass it to view holder
            view = inflater.inflate(R.layout.home_list_item, viewGroup, false);
            holder = new ItemHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams();
            layoutParams.setFullSpan(false);
            layoutParams.height = (int) (recyclerView.getMeasuredWidth() * 0.75);

            ItemHolder itemHolder = (ItemHolder) viewHolder;
            itemHolder.setDetails(cars.get(position));
        }

        @Override
        public int getItemCount() {
            return cars.size();
        }


        public class ItemHolder extends RecyclerView.ViewHolder {

            ImageView imageViewCover;
            TextView textViewDescription;
            TextView textViewPrice;
            TextView textViewCode;

            Car car;

            public ItemHolder(View itemView) {
                super(itemView);
                imageViewCover = itemView.findViewById(R.id.imageViewCover);
                textViewDescription = itemView.findViewById(R.id.textViewDescription);
                textViewCode = itemView.findViewById(R.id.textViewCode);
                textViewPrice = itemView.findViewById(R.id.textViewPrice);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(FilterActivity.this, DetailsActivity.class);
                        intent.putExtra(ID_KEY, car.getId());
                        intent.putExtra(ITEM_TYPE, DetailsActivity.DetailsType.Cars);
                        startActivity(intent);
                    }
                });
            }

            private void setDetails(Car car) {
                this.car = car;

                if (!car.getImages().isEmpty()) {
                    Picasso.with(FilterActivity.this).load(car.getImages().get(0).getImageUrl()).
                            placeholder(R.drawable.placeholder).fit().centerCrop().
                            error(R.drawable.ic_warning).
                            into(imageViewCover);
                } else {
                    imageViewCover.setImageResource(R.drawable.ic_warning);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    textViewDescription.setText(Html.fromHtml(car.getDescription(), Html.FROM_HTML_OPTION_USE_CSS_COLORS));
                } else {
                    //noinspection deprecation
                    textViewDescription.setText(Html.fromHtml(car.getDescription()));
                }

                textViewCode.setText(car.getModel());
                textViewPrice.setText(String.format("%s - %s %s", car.getPrice(), car.getPriceMonth(), getResources().getString(R.string.egp)));
            }

        }
    }


    private class Content extends ContentVolley {

        public Content() {
            super(TAG, FilterActivity.this);
        }

        @Override
        protected void onPreExecute(ActionType actionType) {
            isLoading(true);
        }

        @Override
        protected void onPostExecuteGetProperties(ActionType actionType, boolean success, String message, ArrayList<Property> properties) {
            isLoading(false);
            if (success) {
                if (properties.size() != 0) {
                    LinearLayoutFields.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                    layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setHasFixedSize(false);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());

                    recyclerView.setAdapter(new PropertiesAdapter(properties));
                } else {
                    Toast.makeText(FilterActivity.this, getString(R.string.no_result), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(FilterActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPostExecuteGetCars(ActionType actionType, boolean success, String message, ArrayList<Car> cars) {
            isLoading(false);
            if (success) {
                if (cars.size() != 0) {
                    LinearLayoutFields.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                    layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setHasFixedSize(false);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());

                    recyclerView.setAdapter(new CarsAdapter(cars));
                } else {
                    Toast.makeText(FilterActivity.this, getString(R.string.no_result), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(FilterActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
