package com.binaa.company.fragments;

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

import com.binaa.company.DetailsActivity;
import com.binaa.company.MainActivity;
import com.binaa.company.R;
import com.binaa.company.models.Car;
import com.binaa.company.server.ContentVolley;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.binaa.company.DetailsActivity.ID_KEY;
import static com.binaa.company.DetailsActivity.ITEM_TYPE;

/**
 * Created by Muhammad on 7/29/2017
 */

public class CarsFragment extends Fragment {

    final static String BUNDLE_RECYCLER_LAYOUT = "carsfragment.recycler.layout";

    private final String TAG = CarsFragment.class.getSimpleName();
    Parcelable savedRecyclerLayoutState;
    RecyclerView recyclerView;
    private MainActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cars, container, false);

        activity = (MainActivity) getActivity();
        recyclerView = view.findViewById(R.id.recycler);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        Content content = new Content();
        content.getCars();

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


    public class CarsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private ArrayList<Car> cars;


        private LayoutInflater inflater;

        public CarsAdapter(Activity activity, ArrayList<Car> cars) {
            inflater = activity.getLayoutInflater();
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
                        Intent intent = new Intent(activity, DetailsActivity.class);
                        intent.putExtra(ID_KEY, car.getId());
                        intent.putExtra(ITEM_TYPE, DetailsActivity.DetailsType.Cars);
                        startActivity(intent);
                    }
                });
            }

            private void setDetails(Car car) {
                this.car = car;

                if (!car.getImages().isEmpty()) {
                    Picasso.with(activity).load(car.getImages().get(0).getImageUrl()).
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
            super(TAG, activity);
        }

        @Override
        protected void onPreExecute(ActionType actionType) {
            activity.isLoading(true);
        }

        @Override
        protected void onPostExecuteGetCars(ActionType actionType, boolean success, String message, ArrayList<Car> cars) {
            activity.isLoading(false);
            if (success) {
                recyclerView.setAdapter(new CarsAdapter(activity, cars));
            } else {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
