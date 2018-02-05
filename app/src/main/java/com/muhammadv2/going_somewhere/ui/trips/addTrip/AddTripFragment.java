package com.muhammadv2.going_somewhere.ui.trips.addTrip;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.muhammadv2.going_somewhere.App;
import com.muhammadv2.going_somewhere.Constants;
import com.muhammadv2.going_somewhere.R;
import com.muhammadv2.going_somewhere.model.City;
import com.muhammadv2.going_somewhere.model.DataInteractor;
import com.muhammadv2.going_somewhere.model.Trip;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * This fragment responsible of get the trip details from the user as Trip name and how many cities
 * in each trip and these cities names and also the date of the trip when will start and end
 */
public class AddTripFragment extends Fragment implements View.OnClickListener {

    // Associated views with this fragment
    @BindView(R.id.add_city_container)
    ViewGroup addCityContainer;
    @BindView(R.id.et_city_name)
    EditText etAddCity;
    @BindView(R.id.et_place_title)
    EditText etAddTripTitle;
    @BindView(R.id.et_date_from_place)
    EditText etAddDateFrom;
    @BindView(R.id.et_date_to_place)
    EditText etAddDateTo;
    @BindView(R.id.btn_add_city)
    Button btnAddCity;

    //Inject the dataInteractor object into this fragment
    @Inject
    DataInteractor interactor;

    // Field of ArrayList to keep eye on all generated views
    private ArrayList<FrameLayout> allAddedViews;

    // Counter for how many cities has been added
    private int cityCount;

    // Field holding the current inflated view of the fragment
    private View layoutView;

    // String to hold the date from and to
    private String dateFrom;
    private String dateTo;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save how many city fields have been added and save they names if found
        outState.putInt("cityCount", cityCount);
        outState.putParcelableArrayList("cityNames", extractCityNames());
    }

    /**
     * Method that help iterate through Array of generated views and extract all cities added and
     * make an ArrayList out of them
     *
     * @return Array of those strings
     */
    private ArrayList<City> extractCityNames() {
        if (allAddedViews == null) {
            return null;
        }
        ArrayList<City> cityNames = new ArrayList<>();
        cityNames.add(new City(
                etAddCity.getText().toString(),
                0));//use 0 cuz this the first field already there

        int cityId = 1; //start counting from 1 for the generated fields
        for (int i = 0; i < allAddedViews.size(); i++) {
            FrameLayout generatedFL = allAddedViews.get(i);
            EditText generatedET = (EditText) generatedFL.getChildAt(0);
            City city = new City(generatedET.getText().toString(), cityId);
            cityNames.add(city);
            cityId++;
        }
        return cityNames;
    }

    //region Date

    /**
     * The result that come back from Date picker will invoke this method and then
     * check if its valid request if yes call the @formatTheSelectedDate method to insert the
     * selected date into the corresponding field
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Constants.DATE_PICKER_FRAGMENT:
                if (resultCode == Activity.RESULT_OK) {
                    formatTheSelectedDate(data);
                }
                break;
        }
    }

    private void formatTheSelectedDate(Intent data) {
        Bundle bundle = data.getExtras();
        int day = bundle.getInt(Constants.DAY_PICKER);
        int month = bundle.getInt(Constants.MONTH_PICKER);
        int year = bundle.getInt(Constants.YEAR_PICKER);
        boolean bool = bundle.getBoolean(Constants.BOOL_PICKER);

        String strDate = String.valueOf(day + "/" + month + "/" + year);

        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date date;
        String dateNeeded = null;
        try {
            date = df.parse(strDate);
            dateNeeded = df.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (bool) {
            etAddDateFrom.setText(dateNeeded);
            dateFrom = dateNeeded;
        } else {
            etAddDateTo.setText(dateNeeded);
            dateTo = dateNeeded;
        }
    }

    private long parseDateToMiSeconds(String stringDate) {

        if (stringDate == null) {
            return 0;
        }

        @SuppressLint("SimpleDateFormat") java.text.DateFormat formatter =
                new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = formatter.parse(stringDate);
            return date.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
    //endregion

    //region CreateTheView
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        layoutView = inflater.inflate(R.layout.fragment_add_trip, container, false);

        // Bind ButterKnife library to this fragment
        ButterKnife.bind(this, layoutView);

        //planting a tag for Timber
        Timber.plant(new Timber.DebugTree());

        allAddedViews = new ArrayList<>();

        //inject this fragment into the app component
        App.getInstance().getAppComponent().inject(this);

        //return the inflated view
        return layoutView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // If the bundle not null re instantiate the view with the populated data
        if (savedInstanceState != null) {
            for (int i = 1; i < savedInstanceState.getInt("cityCount"); i++) {
                addNewRowForCities
                        (true
                                , savedInstanceState.getStringArrayList("cityNames").get(i));
            }
        }

        FloatingActionButton fab = view.findViewById(R.id.fab);

        // Set listener on all the needed views
        fab.setOnClickListener(this);
        btnAddCity.setOnClickListener(this);
        etAddDateFrom.setOnClickListener(this);
        etAddDateTo.setOnClickListener(this);
    }
    //endregion

    //region HandleOnClickMethods
    @Override
    public void onClick(View view) {

        int viewId = view.getId();

        switch (viewId) {
            // Handle Add city button by inflating the needed rowView and add it before the addButton
            case R.id.btn_add_city:
                addNewRowForCities(false, null);
                break;
            // Onclick the dates EditText open new dialog as a date picker
            case R.id.et_date_from_place:
                openDatePickerForDateViews(true);
                break;
            case R.id.et_date_to_place:
                openDatePickerForDateViews(false);
                break;
            // Upon fab clicked save all fields
            case R.id.fab:
                saveAllFieldsOnFabClick();
                break;
        }
    }

    private void addNewRowForCities(boolean orientation, String cityName) {

        // Animate(Rotate) The button for adding when clicked
        Animation rotateAnim = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_btn);
        btnAddCity.setAnimation(rotateAnim);

        // Every time the add button clicked add one to the city count
        cityCount += 1;

        LayoutInflater inflater = (LayoutInflater)
                getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") final View rowView =
                inflater.inflate(R.layout.dynamic_add_city_field, null);

        // Set id for every view to distinguish each generated view
        rowView.setId(cityCount);

        // Handle Deleting a row view from cities rows when click on delete button
        FrameLayout fieldContainer = (FrameLayout) rowView;
        if (orientation) {
            EditText generatedET = (EditText) fieldContainer.getChildAt(0);
            generatedET.setText(cityName);
        }

        // Get ImageButton to be able to listen on it
        ImageButton deleteCity = (ImageButton) fieldContainer.getChildAt(1);
        // Set onClickListener on the button and then get the parent for the clicked view and remove
        deleteCity.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), String.valueOf(view.getId()), Toast.LENGTH_SHORT).show();
                addCityContainer.removeView((View) view.getParent());
                cityCount -= 1;
            }
        });

        // Add the new row before the add field button on the city field container.
        addCityContainer.addView(rowView, addCityContainer.getChildCount() - 1);

        // Add the generated row as a View to
        allAddedViews.add(fieldContainer);
    }

    // Method working for both FROM & TO EditText fields to help open date picker fragment.
    private void openDatePickerForDateViews(boolean from) {
        DialogFragment dialogFragment = new DatePickerFragment();
        dialogFragment.setTargetFragment(this, Constants.DATE_PICKER_FRAGMENT);
        Bundle args = new Bundle();
        args.putBoolean(Constants.FROM_BOOL_REQ, from);
        dialogFragment.setArguments(args);
        dialogFragment.show(getActivity().getSupportFragmentManager(), Constants.DATE_PICKER_REQUEST);
    }


    // When FAB button clicked save all added fields and save it in the db
    void saveAllFieldsOnFabClick() {

        // Extract the data from fields
        String title = etAddTripTitle.getText().toString();
        long timeStart = parseDateToMiSeconds(dateFrom.trim());
        long timeEnd = parseDateToMiSeconds(dateTo.trim());
        ArrayList<City> cities = extractCityNames();

        // Check if there's any field not populated
        if (title != null && cities != null && timeStart != 0 && timeEnd != 0) {
            // Everything is fine use the interactor and insert the data using its helper method
            interactor.insertIntoTripTable(new Trip(title, timeStart, timeEnd, cities));
            Toast.makeText(getActivity(), "Add Trip Successfully", Toast.LENGTH_LONG).show();
            getActivity().finish();
        } else {
            // There still fields not populated show SnackBar
            Snackbar.make(layoutView, R.string.error_add_fields, Snackbar.LENGTH_LONG).show();
        }
    }

//endregion

}

