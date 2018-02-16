package com.muhammadv2.going_somewhere.ui.trips.addTrip;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.muhammadv2.going_somewhere.App;
import com.muhammadv2.going_somewhere.Constants;
import com.muhammadv2.going_somewhere.R;
import com.muhammadv2.going_somewhere.model.City;
import com.muhammadv2.going_somewhere.model.DataInteractor;
import com.muhammadv2.going_somewhere.model.Trip;
import com.muhammadv2.going_somewhere.model.network.ImageryAsyncTask;
import com.muhammadv2.going_somewhere.utils.DatePickerFragment;
import com.muhammadv2.going_somewhere.utils.FormattingUtils;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    @BindView(R.id.pb_add_trip)
    ProgressBar progressBar;
    @BindView(R.id.add_trip_container)
    ScrollView container;

    //Inject the dataInteractor object into this fragment
    @Inject
    DataInteractor interactor;

    // Field of ArrayList to keep eye on all generated views
    private ArrayList<FrameLayout> allAddedViews;

    // Counter for how many cities has been added
    private int cityCount;

    // Field holding the current inflated view of the fragment
    private View layoutView;

    private Trip trip;
    private int tripId;

    //Todo on rotation the cities names been a miss see why and fix
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save how many city fields have been added and save they names if found
        outState.putInt(Constants.CITY_COUNT_ID, cityCount);
        outState.putParcelableArrayList(Constants.CITY_NAMES_ID, extractCityNames());
        outState.putParcelable(Constants.TRIPS_ARRAY_ID, trip);
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

    //region CreateTheView
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        layoutView = inflater.inflate(R.layout.fragment_add_trip, container, false);

        // Bind ButterKnife library to this fragment
        ButterKnife.bind(this, layoutView);

        allAddedViews = new ArrayList<>();

        // Inject this fragment into the app component
        App.getInstance().getAppComponent().inject(this);

        //return the inflated view
        return layoutView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // If the bundle not null re instantiate the view with the populated data
        if (savedInstanceState != null) {
            trip = savedInstanceState.getParcelable(Constants.TRIPS_ARRAY_ID);
            ArrayList<City> cities = savedInstanceState.getParcelableArrayList(Constants.CITY_NAMES_ID);
            for (int i = 0; i < savedInstanceState.getInt(Constants.CITY_COUNT_ID); i++) {
                String cityName = cities.get(i).getCityName();
                addNewRowForCities(true, cityName);
            }
        } else {
            newTrip_EditTrip_Checker();
        }

        FloatingActionButton fab = view.findViewById(R.id.fab);

        // Set listener on all the needed views
        fab.setOnClickListener(this);
        btnAddCity.setOnClickListener(this);
        etAddDateFrom.setOnClickListener(this);
        etAddDateTo.setOnClickListener(this);
    }

    private void newTrip_EditTrip_Checker() {
        Intent intent = getActivity().getIntent();
        trip = intent.getParcelableExtra(Constants.TRIPS_ARRAY_ID);
        if (trip != null) {
            City city = trip.getCities().get(0);
            etAddCity.setText(city.getCityName());
            tripId = trip.getTripId();

            for (int i = 1; i < trip.getCities().size(); i++) {
                city = trip.getCities().get(i);
                addNewRowForCities(true, city.getCityName());
            }
            etAddTripTitle.setText(trip.getTripName());
            etAddDateFrom.setText(FormattingUtils.milliSecToString(trip.getStartTime()));
            etAddDateTo.setText(FormattingUtils.milliSecToString(trip.getEndTime()));
        }
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

        long timeStart = FormattingUtils.parseDateToMiSeconds(etAddDateFrom.getText().toString());
        long timeEnd = FormattingUtils.parseDateToMiSeconds(etAddDateTo.getText().toString());

        // Extract the data from fields
        String title = etAddTripTitle.getText().toString();

        ArrayList<City> cities = new ArrayList<>();
        City cityFromFirstField = new City(etAddCity.getText().toString(), 0);
        cities.add(cityFromFirstField);
        cities.addAll(extractCityNames());


        if (timeStart != 0 && timeEnd != 0 && !title.isEmpty() &&
                !etAddCity.getText().toString().isEmpty()) {

            String imageUrl = requestImage(title);
            if (trip == null) {
                // Check if there's any field not populated
                Trip trip = new Trip(title, timeStart, timeEnd, cities, imageUrl, 0);

                // Everything is fine use the interactor and insert the data using its helper method
                Uri uri = interactor.insertIntoTripTable(trip);
                if (uri != null)
                    Toast.makeText(getActivity(), R.string.trip_added, Toast.LENGTH_LONG).show();
                getActivity().finish();

            } else {
                Trip trip = new Trip(title, timeStart, timeEnd, cities, imageUrl, tripId);
                // Otherwise this is an EXISTING Trip, so update the trip
                int rowsAffected = interactor.updateTripTable(trip, tripId);

                // Show a toast message depending on whether or not the update was successful.
                if (rowsAffected == 0) {
                    // If no rows were affected, then there was an error with the update.
                    Toast.makeText(getContext(), getString(R.string.error_updating_trip),
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Otherwise, the update was successful and we can display a toast.
                    Toast.makeText(getContext(), getString(R.string.editor_update_trip_successful),
                            Toast.LENGTH_SHORT).show();
                }

                getActivity().finish();
            }
        } else {
            // There still fields not populated show SnackBar
            Snackbar.make(layoutView, R.string.error_add_fields, Snackbar.LENGTH_LONG).show();
        }

    }

    private String requestImage(String tripName) {
        try {
            // When call AT show the progress bar and hide everything else.
            progressBar.setVisibility(View.VISIBLE);
            container.setVisibility(View.INVISIBLE);

            return new ImageryAsyncTask().execute(tripName).get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }
//endregion

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_settings)
            showDialogForDeleting();
        return super.onOptionsItemSelected(item);
    }

    private void showDialogForDeleting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.sure_delete);
        builder.setPositiveButton(getString(R.string.yes_delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAllData();
            }
        });
        builder.setNegativeButton(getString(R.string.dont_delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteAllData() {
        int id = interactor.deleteFromTripTable(tripId);
        if (id > 0) {
            Toast.makeText(getContext(), getString(R.string.trip_deleted), Toast.LENGTH_SHORT).show();
            getActivity().finish();
        } else {
            Toast.makeText(getContext(), getString(R.string.error_deleting), Toast.LENGTH_SHORT).show();
        }
    }

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
                    FormattingUtils.formatTheSelectedDate(data, etAddDateFrom, etAddDateTo);
                }
                break;
        }
    }
}

