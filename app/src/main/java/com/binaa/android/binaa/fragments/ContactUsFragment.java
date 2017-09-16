package com.binaa.android.binaa.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.binaa.android.binaa.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Muhammad on 6/1/2017
 */

public class ContactUsFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.textViewAddress)
    TextView textViewAddress;

    @BindView(R.id.textViewPhone)
    TextView textViewPhone;

    @BindView(R.id.textViewEmail)
    TextView textViewEmail;

    @BindView(R.id.imageViewFacebook)
    ImageView imageViewFacebook;

    @BindView(R.id.imageViewTwitter)
    ImageView imageViewTwitter;

    @BindView(R.id.imageViewInstagram)
    ImageView imageViewInstagram;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);

        ButterKnife.bind(this,view);

        return view;
    }

    @Override
    public void onClick(View view) {

    }
}
