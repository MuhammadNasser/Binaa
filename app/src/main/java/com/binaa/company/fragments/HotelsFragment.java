package com.binaa.company.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.binaa.company.DetailsActivity;
import com.binaa.company.MainActivity;
import com.binaa.company.R;
import com.binaa.company.models.BaseModel;
import com.binaa.company.models.Hotel;
import com.binaa.company.server.ContentVolley;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.binaa.company.DetailsActivity.ID_KEY;
import static com.binaa.company.DetailsActivity.ITEM_TYPE;

/**
 * Created by Muhammad on 7/29/2017
 */

public class HotelsFragment extends Fragment {

    final static String BUNDLE_RECYCLER_LAYOUT = "hotelsfragment.recycler.layout";
    private final String TAG = HotelsFragment.class.getSimpleName();

    RecyclerView recyclerView;
    RelativeLayout relativeLayoutComingSoon;
    public RelativeLayout relativeLayoutHeader;
    Parcelable savedRecyclerLayoutState;
    Spinner spinnerCities;
    Spinner spinnerGovs;
    //    EditText editTextBedrooms;
    EditText editTextMinPrice;
    EditText editTextMaxPrice;
    Button buttonSearch;
    private MainActivity activity;
    String cityId;
    String govId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_hotels, container, false);

        activity = (MainActivity) getActivity();

        recyclerView = view.findViewById(R.id.recycler);
        spinnerCities = view.findViewById(R.id.spinnerCities);
        spinnerGovs = view.findViewById(R.id.spinnerGovs);
        relativeLayoutHeader = view.findViewById(R.id.relativeLayoutHeader);
//        editTextBedrooms = view.findViewById(R.id.editTextBedrooms);
        editTextMinPrice = view.findViewById(R.id.editTextMinPrice);
        editTextMaxPrice = view.findViewById(R.id.editTextMaxPrice);
        buttonSearch = view.findViewById(R.id.buttonSearch);
        relativeLayoutComingSoon = view.findViewById(R.id.relativeLayoutComingSoon);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        final Content content = new Content();
        content.getHotels("", "", "", "");
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (govId == null) {
                    content.getHotels(editTextMinPrice.getText().toString(), editTextMaxPrice.getText().toString(), "", cityId);
                } else if (cityId == null) {
                    content.getHotels(editTextMinPrice.getText().toString(), editTextMaxPrice.getText().toString(), govId, "");
                } else {
                    content.getHotels(editTextMinPrice.getText().toString(), editTextMaxPrice.getText().toString(), govId, cityId);
                }
            }
        });

        Content content1 = new Content();
        content1.getCities();
        Content content2 = new Content();
        content2.getGovs();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (savedRecyclerLayoutState != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, recyclerView.getLayoutManager().onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    public class HotelsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private ArrayList<Hotel> hotels;


        private LayoutInflater inflater;

        public HotelsAdapter(ArrayList<Hotel> hotels) {

            inflater = getLayoutInflater();
            this.hotels = hotels;
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

            public ItemHolder(View itemView) {
                super(itemView);

                imageViewCover = itemView.findViewById(R.id.imageViewCover);
                textViewPrice = itemView.findViewById(R.id.textViewPrice);
                textViewCode = itemView.findViewById(R.id.textViewCode);
                textViewDescription = itemView.findViewById(R.id.textViewDescription);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(activity, DetailsActivity.class);
                        intent.putExtra(ID_KEY, hotel.getId());
                        intent.putExtra(ITEM_TYPE, DetailsActivity.DetailsType.Hotels);
                        startActivity(intent);
                    }
                });
            }

            public void setDetails(Hotel hotel) {
                this.hotel = hotel;

                if (!hotel.getCoverPic().isEmpty()) {
                    Picasso.with(activity).load(hotel.getCoverPic()).
                            placeholder(R.drawable.placeholder).fit().centerCrop().fit().centerCrop().
                            error(R.drawable.ic_warning).
                            into(imageViewCover);
                } else {
                    imageViewCover.setImageResource(R.drawable.ic_warning);
                }

                textViewPrice.setText(String.format("%s - %s %s %s", hotel.getPrice(), hotel.getPriceMonth(), getResources().getString(R.string.dolar), getResources().getString(R.string.including)));
                textViewCode.setText(String.format("%s", hotel.getTitle()));

                String styledText = "<font color='white'>" + hotel.getDescription() + "</font>.";
                textViewDescription.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);
            }

        }
    }

    private class Content extends ContentVolley implements AdapterView.OnItemSelectedListener {

        private ArrayList<BaseModel> govs = new ArrayList<>();
        private ArrayList<BaseModel> cities = new ArrayList<>();

        public Content() {
            super(TAG, activity);
        }

        @Override
        protected void onPreExecute(ActionType actionType) {
            activity.isLoading(true);
        }

        @Override
        protected void onPostExecuteGetHotels(ActionType actionType, boolean success, String message, ArrayList<Hotel> hotels) {
            activity.isLoading(false);
            if (success) {
                if (hotels.size() > 0) {
                    recyclerView.setAdapter(new HotelsAdapter(hotels));
                } else {
                    relativeLayoutComingSoon.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                relativeLayoutHeader.setVisibility(View.GONE);

            } else {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPostExecuteGetCities(ActionType actionType, boolean success, String message, ArrayList<BaseModel> cities) {
            this.cities = cities;
            activity.isLoading(false);
            if (success) {
                ArrayList<String> items = new ArrayList<>();

                for (int i = 0; i < cities.size(); i++) {
                    String item = cities.get(i).getName();
                    items.add(item);
                }

                ArrayAdapter spinnerAdapter = new ArrayAdapter<>(activity, R.layout.spinner_item, items);
                spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);

                spinnerCities.setAdapter(spinnerAdapter);
                spinnerCities.setOnItemSelectedListener(this);
            } else {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPostExecuteGetGovs(ActionType actionType, boolean success, String message, ArrayList<BaseModel> govs) {
            this.govs = govs;
            activity.isLoading(false);
            if (success) {
                ArrayList<String> items = new ArrayList<>();

                for (int i = 0; i < govs.size(); i++) {
                    String item = govs.get(i).getName();
                    items.add(item);
                }

                ArrayAdapter spinnerAdapter = new ArrayAdapter<>(activity, R.layout.spinner_item, items);
                spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);

                spinnerGovs.setAdapter(spinnerAdapter);
                spinnerGovs.setOnItemSelectedListener(this);
            } else {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (view == spinnerCities) {
                if (!cities.isEmpty()) {
                    cityId = String.valueOf(cities.get(i).getId());
                }
            } else {
                if (!govs.isEmpty()) {
                    govId = String.valueOf(govs.get(i).getId());
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
}
