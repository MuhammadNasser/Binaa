package com.binaa.android.binaa.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.binaa.android.binaa.DetailsActivity;
import com.binaa.android.binaa.MainActivity;
import com.binaa.android.binaa.R;
import com.binaa.android.binaa.models.Property;
import com.binaa.android.binaa.server.ContentVolley;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.binaa.android.binaa.DetailsActivity.ID_KEY;
import static com.binaa.android.binaa.DetailsActivity.ITEM_TYPE;


/**
 * Created by Muhammad on 7/29/2017
 */

public class PropertiesFragment extends Fragment {

    final static String BUNDLE_RECYCLER_LAYOUT = "propertiesfragment.recycler.layout";

    private final String TAG = PropertiesFragment.class.getSimpleName();

    RecyclerView recyclerView;
    Parcelable savedRecyclerLayoutState;
    private MainActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_properties, container, false);

        activity = (MainActivity) getActivity();

        recyclerView = view.findViewById(R.id.recycler);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        Content content = new Content();
        content.getProperties();

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

    public class PropertiesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private ArrayList<Property> properties;

        private LayoutInflater inflater;

        public PropertiesAdapter(Activity activity, ArrayList<Property> properties) {
            inflater = activity.getLayoutInflater();
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
                        Intent intent = new Intent(activity, DetailsActivity.class);
                        intent.putExtra(ID_KEY, property.getId());
                        intent.putExtra(ITEM_TYPE, DetailsActivity.DetailsType.Properties);
                        startActivity(intent);
                    }
                });
            }

            public void setDetails(Property property) {
                this.property = property;

                if (!property.getCoverPic().isEmpty()) {
                    Picasso.with(activity).load(property.getCoverPic()).
                            placeholder(R.drawable.placeholder).fit().centerCrop().
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

    private class Content extends ContentVolley {

        public Content() {
            super(TAG, activity);
        }

        @Override
        protected void onPreExecute(ActionType actionType) {
            activity.isLoading(true);
        }

        @Override
        protected void onPostExecuteGetProperties(ActionType actionType, boolean success, String message, ArrayList<Property> properties) {
            activity.isLoading(false);
            if (success) {

                recyclerView.setAdapter(new PropertiesAdapter(activity, properties));
            } else {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        }
    }
}

