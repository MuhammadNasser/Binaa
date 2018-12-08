package com.binaa.android.binaa.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.binaa.android.binaa.MainActivity;
import com.binaa.android.binaa.R;
import com.binaa.android.binaa.models.HotelOffer;
import com.binaa.android.binaa.models.PropertyOffer;
import com.binaa.android.binaa.server.ContentVolley;
import com.binaa.android.binaa.utils.HotelsViewPagerAdapter;
import com.binaa.android.binaa.utils.PropertiesViewPagerAdapter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Muhammad on 7/29/2017
 */

public class OffersFragment extends Fragment {

    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.
    int currentPage = 0;
    Timer timer;
    private final String TAG = OffersFragment.class.getSimpleName();
    private MainActivity activity;
    private ViewPager pagerHotelsOffers;
    private ViewPager pagerPropertiesOffers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_offers, container, false);
        pagerHotelsOffers = view.findViewById(R.id.pagerHotelsOffers);
        pagerPropertiesOffers = view.findViewById(R.id.pagerPropertyOffers);

        activity = (MainActivity) getActivity();

        Content hotelOffers = new Content();
        hotelOffers.getHotelOffers();

        Content propertyOffers = new Content();
        propertyOffers.getPropertyOffers();

        return view;
    }


    private class Content extends ContentVolley {

        public Content() {
            super(TAG, activity);
        }

        @Override
        protected void onPreExecute(ActionType actionType) {
            activity.isLoading(true);
        }

        protected void onPostExecuteHotelOffers(ActionType actionType, boolean success, String message, final ArrayList<HotelOffer> hotelOffers) {
            activity.isLoading(false);
            if (success) {
                pagerHotelsOffers.setAdapter(new HotelsViewPagerAdapter(hotelOffers, activity));
                final Handler handler = new Handler();
                final Runnable Update = new Runnable() {
                    public void run() {
                        if (currentPage == hotelOffers.size() - 1) {
                            currentPage = 0;
                        }
                        pagerHotelsOffers.setCurrentItem(currentPage++, true);
                    }
                };

                timer = new Timer(); // This will create a new Thread
                timer.schedule(new TimerTask() { // task to be scheduled
                    @Override
                    public void run() {
                        handler.post(Update);
                    }
                }, DELAY_MS, PERIOD_MS);
            } else {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        }

        protected void onPostExecutePropertyOffers(ActionType actionType, boolean success, String message, final ArrayList<PropertyOffer> propertyOffers) {
            activity.isLoading(false);
            if (success) {
                pagerPropertiesOffers.setAdapter(new PropertiesViewPagerAdapter(propertyOffers, activity));
                final Handler handler = new Handler();
                final Runnable Update = new Runnable() {
                    public void run() {
                        if (currentPage == propertyOffers.size() - 1) {
                            currentPage = 0;
                        }
                        pagerPropertiesOffers.setCurrentItem(currentPage++, true);
                    }
                };

                timer = new Timer(); // This will create a new Thread
                timer.schedule(new TimerTask() { // task to be scheduled
                    @Override
                    public void run() {
                        handler.post(Update);
                    }
                }, DELAY_MS, PERIOD_MS);
            } else {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
