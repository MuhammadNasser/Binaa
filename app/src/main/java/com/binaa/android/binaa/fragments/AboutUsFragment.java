package com.binaa.android.binaa.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.binaa.android.binaa.MainActivity;
import com.binaa.android.binaa.R;
import com.binaa.android.binaa.server.ContentVolley;


/**
 * Created by Muhammad on 7/29/2017
 */

public class AboutUsFragment extends Fragment {

    private MainActivity activity;
    private TextView textViewAbout;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);
        textViewAbout = view.findViewById(R.id.textViewAbout);
        activity = (MainActivity) getActivity();

        Content content = new Content();
        content.getAbout();

        return view;
    }

    private class Content extends ContentVolley {

        public Content() {
            super(activity.getPackageName(), activity);
        }

        @Override
        protected void onPreExecute(ActionType actionType) {
            activity.isLoading(true);
        }

        @Override
        protected void onPostExecuteAbout(ActionType actionType, boolean success, String message, String about) {
            activity.isLoading(false);
            if (success) {
                textViewAbout.setText(about);
            } else {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
