package com.binaa.android.binaa.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.binaa.android.binaa.MainActivity;
import com.binaa.android.binaa.R;
import com.binaa.android.binaa.models.Contact;
import com.binaa.android.binaa.server.ContentVolley;


/**
 * Created by Muhammad on 6/1/2017
 */

public class ContactUsFragment extends Fragment {

    TextView textViewAddress;
    TextView textViewPhone;

    ImageView imageViewFacebook;
    ImageView imageViewTwitter;
    ImageView imageViewInstagram;

    MainActivity activity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);

        activity = (MainActivity) getActivity();

        textViewAddress = view.findViewById(R.id.textViewAddress);
        textViewPhone = view.findViewById(R.id.textViewPhone);

        imageViewFacebook = view.findViewById(R.id.imageViewFacebook);
        imageViewTwitter = view.findViewById(R.id.imageViewTwitter);
        imageViewInstagram = view.findViewById(R.id.imageViewInstagram);

        Content content = new Content();
        content.getContacts();

        return view;
    }

    private class Listeners implements View.OnClickListener {

        String link;

        public Listeners(String link) {
            this.link = link;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String facebook = "http://www.facebook.com";
            String twitter = "http://www.twitter.com";
            String instagram = "http://www.instagram.com";
            if (v == imageViewFacebook) {
                if (!link.isEmpty() || link != null) {
                    intent.setData(Uri.parse(link));
                    startActivity(intent);
                } else {
                    intent.setData(Uri.parse(facebook));
                    startActivity(intent);
                }
            } else if (v == imageViewTwitter) {
                if (!link.isEmpty() || link != null) {
                    intent.setData(Uri.parse(link));
                    startActivity(intent);
                } else {
                    intent.setData(Uri.parse(twitter));
                    startActivity(intent);
                }
            } else if (v == imageViewInstagram) {
                if (!link.isEmpty() || link != null) {
                    intent.setData(Uri.parse(link));
                    startActivity(intent);
                } else {
                    intent.setData(Uri.parse(instagram));
                    startActivity(intent);
                }
            }
        }
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
        protected void onPostExecuteGetContacts(ActionType actionType, boolean success, String message, Contact contact) {
            activity.isLoading(false);
            if (success) {
                textViewAddress.setText(String.format("%s %s", getResources().getString(R.string.address_), contact.getAddress()));
                textViewPhone.setText(String.format("%s %s", getResources().getString(R.string.phone_), contact.getPhone()));

                imageViewInstagram.setOnClickListener(new Listeners(contact.getInstagram()));
                imageViewFacebook.setOnClickListener(new Listeners(contact.getFacebook()));
                imageViewTwitter.setOnClickListener(new Listeners(contact.getTwitter()));
            } else {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
