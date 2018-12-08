package com.binaa.android.binaa.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.binaa.android.binaa.DetailsActivity;
import com.binaa.android.binaa.R;
import com.binaa.android.binaa.models.PropertyOffer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.binaa.android.binaa.DetailsActivity.ID_KEY;
import static com.binaa.android.binaa.DetailsActivity.ITEM_TYPE;

/**
 * Created by Muhammad on 4/19/2018
 */
public class PropertiesViewPagerAdapter extends PagerAdapter {

    private ArrayList<PropertyOffer> propertyOffers;
    private LayoutInflater inflater;
    private Activity activity;

    public PropertiesViewPagerAdapter(ArrayList<PropertyOffer> propertyOffers, Activity activity) {
        this.propertyOffers = propertyOffers;
        this.activity = activity;
        this.inflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return propertyOffers.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        View view = inflater.inflate(R.layout.item_offer, container, false);
        TextView textViewHotelName = view.findViewById(R.id.textViewHotelName);
        TextView textViewDescription = view.findViewById(R.id.textViewDescription);
        ImageView imageView = view.findViewById(R.id.imageViewCover);
        if (!propertyOffers.get(position).getProperty().getCoverPic().isEmpty()) {
            Picasso.with(activity).load(propertyOffers.get(position).getProperty().getCoverPic()).
                    placeholder(R.drawable.placeholder).fit().centerCrop().fit().centerCrop().
                    error(R.drawable.ic_warning).
                    into(imageView);
        } else {
            imageView.setImageResource(R.drawable.ic_warning);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textViewDescription.setText(Html.fromHtml(propertyOffers.get(position).getText(), Html.FROM_HTML_OPTION_USE_CSS_COLORS));
        } else {
            //noinspection deprecation
            textViewDescription.setText(Html.fromHtml(propertyOffers.get(position).getText()));
        }

        textViewHotelName.setText(propertyOffers.get(position).getProperty().getTitle());

        container.addView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, DetailsActivity.class);
                intent.putExtra(ID_KEY, propertyOffers.get(position).getProperty().getId());
                intent.putExtra(ITEM_TYPE, DetailsActivity.DetailsType.Properties);
                activity.startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}

