package com.binaa.android.binaa.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import com.binaa.android.binaa.models.Service;
import com.binaa.android.binaa.server.ContentVolley;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.binaa.android.binaa.DetailsActivity.ID_KEY;
import static com.binaa.android.binaa.DetailsActivity.ITEM_TYPE;

/**
 * Created by Muhammad on 7/29/2017
 */

public class ServicesFragment extends Fragment {

    private final String TAG = ServicesFragment.class.getSimpleName();

    RecyclerView recyclerView;
    private MainActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_properties, container, false);

        activity = (MainActivity) getActivity();

        recyclerView = view.findViewById(R.id.recycler);

        Content content = new Content();
        content.getServices();

        return view;
    }

    public class ServicesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private ArrayList<Service> services;


        private LayoutInflater inflater;

        public ServicesAdapter(Activity activity, ArrayList<Service> services) {

            inflater = activity.getLayoutInflater();
            this.services = services;
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
            itemHolder.setDetails(services.get(position));
        }

        @Override
        public int getItemCount() {
            return services.size();
        }


        public class ItemHolder extends RecyclerView.ViewHolder {

            ImageView imageViewCover;
            TextView textViewPrice;
            TextView textViewCode;
            TextView textViewDescription;

            Service service;

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
                        intent.putExtra(ID_KEY, service.getId());
                        intent.putExtra(ITEM_TYPE, DetailsActivity.DetailsType.Services);
                        startActivity(intent);
                    }
                });
            }

            public void setDetails(Service service) {
                this.service = service;

                if (!service.getImage().isEmpty()) {
                    Picasso.with(activity).load(service.getImage()).
                            placeholder(R.drawable.placeholder).fit().centerCrop().
                            error(R.drawable.ic_warning).
                            into(imageViewCover);
                } else {
                    imageViewCover.setImageResource(R.drawable.ic_warning);
                }
                textViewPrice.setVisibility(View.GONE);
                textViewCode.setVisibility(View.GONE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    textViewDescription.setText(Html.fromHtml(service.getDescription(), Html.FROM_HTML_OPTION_USE_CSS_COLORS));
                } else {
                    //noinspection deprecation
                    textViewDescription.setText(Html.fromHtml(service.getDescription()));
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
        protected void onPostExecuteGetServices(ActionType actionType, boolean success, String message, ArrayList<Service> services) {
            activity.isLoading(false);
            if (success) {
                StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(false);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(new ServicesAdapter(activity, services));

            } else {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
