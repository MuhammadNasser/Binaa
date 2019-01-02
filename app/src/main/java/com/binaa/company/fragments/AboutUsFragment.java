package com.binaa.company.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.binaa.company.MainActivity;
import com.binaa.company.R;
import com.binaa.company.server.ContentVolley;


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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    textViewAbout.setText(Html.fromHtml(about, Html.FROM_HTML_OPTION_USE_CSS_COLORS));
                } else {
                    //noinspection deprecation
                    textViewAbout.setText(Html.fromHtml(about));
                }
            } else {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
