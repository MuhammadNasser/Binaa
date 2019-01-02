package com.binaa.company;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.binaa.company.server.ContentVolley;
import com.binaa.company.utils.ApplicationBase;
import com.binaa.company.utils.MyContextWrapper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * Created by Muhammad on 8/6/2017
 */

public class ReservationActivity extends AppCompatActivity {

    public static String TYPE = "type";
    public static String apartments = "apartments";
    public static String hotels = "hotels";
    public static String ID = "id";

    private final String TAG = this.toString();
    private Button buttonReserve;
    private EditText editTextName;
    private EditText editTextCountry;
    private EditText editTextPhone;
    private EditText editTextPassport;
    private EditText editTextEmail;
    private TextView textViewArrivalDate;
    private TextView textViewLeaveDate;
    private CheckBox checkboxWantDriver;
    private CheckBox checkboxWantTravel;
    private CheckBox checkboxPickupFromAirport;
    private CheckBox checkboxRentCar;
    private RelativeLayout relativeLayoutLoading;
    private DetailsActivity.DetailsType type;
    private int id;
    private int wantDriver;
    private int wantTravel;
    private int pickupFromAirport;
    private int rentCar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setToolBar();

        type = (DetailsActivity.DetailsType) getIntent().getSerializableExtra(TYPE);
        id = getIntent().getIntExtra(ID, 0);

        editTextName = findViewById(R.id.editTextName);
        editTextCountry = findViewById(R.id.editTextCountry);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextPassport = findViewById(R.id.editTextPassport);
        editTextEmail = findViewById(R.id.editTextEmail);
        textViewArrivalDate = findViewById(R.id.textViewArrivalDate);
        textViewLeaveDate = findViewById(R.id.textViewLeaveDate);
        checkboxWantDriver = findViewById(R.id.checkboxWantDriver);
        checkboxWantTravel = findViewById(R.id.checkboxWantTravel);
        checkboxPickupFromAirport = findViewById(R.id.checkboxPickupFromAirport);
        checkboxRentCar = findViewById(R.id.checkboxRentCar);
        TextView textViewTitle = findViewById(R.id.textViewTitle);
        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        LinearLayout linearLayoutCar = findViewById(R.id.linearLayoutCar);
        relativeLayoutLoading = findViewById(R.id.relativeLayoutLoading);

        buttonReserve = findViewById(R.id.buttonReserve);

        switch (type) {
            case Properties:
                linearLayout.setVisibility(View.VISIBLE);
                textViewTitle.setText(getString(R.string.bookApartment));
                break;
            case Hotels:
                linearLayout.setVisibility(View.VISIBLE);
                textViewTitle.setText(getString(R.string.bookHotel));
                break;
            case Cars:
                linearLayoutCar.setVisibility(View.VISIBLE);
                textViewTitle.setText(getString(R.string.bookCar));
                break;
        }

        buttonReserve.setOnClickListener(new Listeners());
        textViewArrivalDate.setOnClickListener(new Listeners());
        textViewLeaveDate.setOnClickListener(new Listeners());
        checkboxRentCar.setOnCheckedChangeListener(new Listeners());
        checkboxPickupFromAirport.setOnCheckedChangeListener(new Listeners());
        checkboxWantDriver.setOnCheckedChangeListener(new Listeners());
        checkboxWantTravel.setOnCheckedChangeListener(new Listeners());
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        Locale languageType = ApplicationBase.getInstance().getLocale();
        super.attachBaseContext(MyContextWrapper.wrap(newBase, languageType));
    }

    private void setToolBar() {

        Toolbar toolBar = findViewById(R.id.toolbar);
        View actionBarView = getLayoutInflater().inflate(R.layout.toolbar_customview, toolBar, false);

        setSupportActionBar(toolBar);
        //noinspection ConstantConditions
        getSupportActionBar().setTitle("");
        TextView textViewActivityTitle = actionBarView.findViewById(R.id.textViewActivityTitle);


        // Set up the drawer.
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(actionBarView);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        textViewActivityTitle.setText(getString(R.string.search));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void isLoading(boolean isLoading) {
        relativeLayoutLoading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    @SuppressLint("ValidFragment")
    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        TextView textView;

        public DatePickerFragment(TextView textView) {
            this.textView = textView;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);

            // Create a new instance of TimePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        @SuppressWarnings({"deprecation"})
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            SimpleDateFormat serverDateFormat = new SimpleDateFormat("YYYY-MM-dd", Locale.ENGLISH);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            Date date = calendar.getTime();
            textView.setText(serverDateFormat.format(date));
        }
    }

    private class Listeners implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        @Override
        public void onClick(View view) {
            DialogFragment dialogFragment;
            if (view == buttonReserve) {
                boolean dataIsSufficient;
                boolean userWarned = false;

                String name = editTextName.getText().toString();
                String country = editTextCountry.getText().toString();
                String phone = editTextPhone.getText().toString();
                String passport = editTextPassport.getText().toString();
                String email = editTextEmail.getText().toString();
                String arrivalDate = textViewArrivalDate.getText().toString();
                String leaveDate = textViewLeaveDate.getText().toString();

                dataIsSufficient = !name.isEmpty();
                if (!dataIsSufficient) {
                    userWarned = true;
                    Toast.makeText(ReservationActivity.this, getString(R.string.nameValidation), Toast.LENGTH_LONG).show();
                }

                dataIsSufficient &= !country.isEmpty();
                if (!dataIsSufficient && !userWarned) {
                    userWarned = true;
                    Toast.makeText(ReservationActivity.this, getString(R.string.countryValidation), Toast.LENGTH_LONG).show();
                }

                dataIsSufficient &= !phone.isEmpty();
                if (!dataIsSufficient && !userWarned) {
                    userWarned = true;
                    Toast.makeText(ReservationActivity.this, getString(R.string.phoneValidation), Toast.LENGTH_LONG).show();
                }

                dataIsSufficient &= !passport.isEmpty();
                if (!dataIsSufficient && !userWarned) {
                    userWarned = true;
                    Toast.makeText(ReservationActivity.this, getString(R.string.passportValidation), Toast.LENGTH_LONG).show();
                }

                dataIsSufficient &= email.matches("[a-zA-Z0-9._-]+@[a-zA-Z0-9_-]+([.][a-zA-Z0-9]+)+");
                if (!dataIsSufficient && !userWarned) {
                    userWarned = true;
                    Toast.makeText(ReservationActivity.this, getString(R.string.emailValidation), Toast.LENGTH_LONG).show();
                }

                dataIsSufficient &= !arrivalDate.isEmpty();
                if (!dataIsSufficient && !userWarned) {
                    userWarned = true;
                    Toast.makeText(ReservationActivity.this, getString(R.string.arrivalDateValidation), Toast.LENGTH_LONG).show();
                }

                dataIsSufficient &= !leaveDate.isEmpty();
                if (!dataIsSufficient && !userWarned) {
                    Toast.makeText(ReservationActivity.this, getString(R.string.leaveDateValidation), Toast.LENGTH_LONG).show();
                }


                if (dataIsSufficient) {
                    Content content = new Content();
                    switch (type) {
                        case Properties:
                            content.bookApartment(id, name, email, country, passport, arrivalDate, leaveDate, pickupFromAirport, rentCar, wantTravel, phone);
                            break;
                        case Hotels:
                            content.bookHotel(id, name, email, country, passport, arrivalDate, leaveDate, pickupFromAirport, rentCar, wantTravel, phone);
                            break;
                        case Cars:
                            content.bookCar(id, name, email, country, passport, arrivalDate, leaveDate, wantDriver, phone);
                            break;
                    }
                }
            } else if (view == textViewArrivalDate) {
                dialogFragment = new DatePickerFragment(textViewArrivalDate);
                dialogFragment.show(getFragmentManager(), "datePicker");
            } else if (view == textViewLeaveDate) {
                dialogFragment = new DatePickerFragment(textViewLeaveDate);
                dialogFragment.show(getFragmentManager(), "datePicker");
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
            if (compoundButton.equals(checkboxRentCar)) {
                if (checked) {
                    rentCar = 1;
                } else {
                    rentCar = 0;
                }
            } else if (compoundButton.equals(checkboxWantDriver)) {
                if (checked) {
                    wantDriver = 1;
                } else {
                    wantDriver = 0;
                }
            } else if (compoundButton.equals(checkboxWantTravel)) {
                if (checked) {
                    wantTravel = 1;
                } else {
                    wantTravel = 0;
                }
            } else if (compoundButton.equals(checkboxPickupFromAirport)) {
                if (checked) {
                    pickupFromAirport = 1;
                } else {
                    pickupFromAirport = 0;
                }
            }
        }
    }

    private class Content extends ContentVolley {

        public Content() {
            super(TAG, ReservationActivity.this);
        }

        @Override
        protected void onPreExecute(ActionType actionType) {
            isLoading(true);
        }

        @Override
        protected void onPostExecute(ActionType actionType, boolean success, String message) {
            isLoading(false);
            if (success) {
                switch (actionType) {
                    case bookApartment:
                        Toast.makeText(ReservationActivity.this, getString(R.string.messageApartment), Toast.LENGTH_SHORT).show();
                        break;
                    case bookCar:
                        Toast.makeText(ReservationActivity.this, getString(R.string.messageCar), Toast.LENGTH_SHORT).show();
                        break;
                    case bookHotel:
                        Toast.makeText(ReservationActivity.this, getString(R.string.messageHotel), Toast.LENGTH_SHORT).show();
                        break;
                }
                finish();
            } else {
                Toast.makeText(ReservationActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
