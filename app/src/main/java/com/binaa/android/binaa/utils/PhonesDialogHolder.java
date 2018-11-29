package com.binaa.android.binaa.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.binaa.android.binaa.R;

public class PhonesDialogHolder extends BaseDialogHolder {

    private Activity activity;
    private String[] phoneNumbers;

    public PhonesDialogHolder(Activity activity, String[] phoneNumbers) {

        super(activity, R.layout.phones_dialog_view, 4f / 5);
        this.activity = activity;
        this.phoneNumbers = phoneNumbers;
        LinearLayout linearLayout = findViewById(R.id.linearLayout);

        for (int i = 0; i < phoneNumbers.length; i++) {
            View child = activity.getLayoutInflater().inflate(R.layout.phones_dialog_item, null);
            linearLayout.addView(child);

            TextView textView1 = child.findViewById(R.id.textView1);
            View view = child.findViewById(R.id.view);
            textView1.setText(phoneNumbers[i].trim());
            textView1.setOnClickListener(new Listener(i));

            if (i == phoneNumbers.length - 1) {
                view.setVisibility(View.GONE);
            }
        }
    }

    private class Listener implements View.OnClickListener {
        int i;

        public Listener(int i) {
            this.i = i;
        }

        @Override
        public void onClick(View v) {
            Intent intentCall = new Intent(Intent.ACTION_DIAL);
            intentCall.setData(Uri.parse("tel:" + phoneNumbers[i].trim()));
            activity.startActivity(intentCall);
        }
    }
}