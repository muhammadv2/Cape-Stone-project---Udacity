package com.muhammadv2.going_somewhere.ui.addTrip;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.muhammadv2.going_somewhere.R;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class AddTripFragment extends Fragment implements View.OnClickListener {

    // Associated views with this fragment
    @BindView(R.id.add_city_container)
    ViewGroup addCityContainer;
    @BindView(R.id.et_city_name)
    EditText etAddCity;
    @BindView(R.id.et_trip_title)
    EditText etAddTripTitle;
    @BindView(R.id.et_date_from)
    EditText etAddDateFrom;
    @BindView(R.id.et_date_to)
    EditText etAddDateTo;
    @BindView(R.id.btn_add_city)
    Button btnAddCity;

    private ArrayList<View> allAddedViews;

    private int cityCount;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        View view = inflater.inflate(R.layout.fragment_add_trip, container, false);

        // Bind ButterKnife library to this fragment
        ButterKnife.bind(this, view);

        //planting a tag for Timber
        Timber.plant(new Timber.DebugTree());

        //return the inflated view
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fab = view.findViewById(R.id.fab);

        allAddedViews = new ArrayList<>();

        // Set listener on all the needed views
        fab.setOnClickListener(this);
        btnAddCity.setOnClickListener(this);
        etAddDateFrom.setOnClickListener(this);
        etAddDateTo.setOnClickListener(this);

    }

    //region HandleOnClickMethods
    @Override
    public void onClick(View view) {

        int viewId = view.getId();

        switch (viewId) {
            // Handle Add city button by inflating the needed rowView and add it before the addButton
            case R.id.btn_add_city:
                addNewRowForCities();
                break;
            // Onclick the dates EditText open new dialog as a date picker
            case R.id.et_date_from:
            case R.id.et_date_to:
                openDatePickerForDateViews();
                break;
            // Upon fab clicked save all fields
            case R.id.fab:
                saveAllFieldsOnFabClick();
                break;
        }
    }

    private void addNewRowForCities() {

        // Every time the add button clicked add one to the city count
        cityCount += 1;

        LayoutInflater inflater = (LayoutInflater)
                getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") final View rowView =
                inflater.inflate(R.layout.dynamic_add_city_field, null);

        // Set id for every view to distinguish each generated view
        rowView.setId(cityCount);

        // Handle Deleting a row view from cities rows when click on delete button
        FrameLayout frameLayout = (FrameLayout) rowView;
        // Get ImageButton to be able to listen on it
        ImageButton deleteCity = (ImageButton) frameLayout.getChildAt(1);
        // Set onClickListener on the button and then get the parent for the clicked view and remove
        deleteCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), String.valueOf(view.getId()), Toast.LENGTH_SHORT).show();
                addCityContainer.removeView((View) view.getParent());
            }
        });

        // Add the new row before the add field button on the city field container.
        addCityContainer.addView(rowView, addCityContainer.getChildCount() - 1);

        // Add the generated row as a View to
        allAddedViews.add(rowView);
    }

    private void openDatePickerForDateViews() {
        android.support.v4.app.DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    void saveAllFieldsOnFabClick() {
        Timber.d(etAddCity.getText().toString());
        for (int i = 0; i < allAddedViews.size(); i++) {
            FrameLayout generatedFL = (FrameLayout) allAddedViews.get(i);
            EditText generatedET = (EditText) generatedFL.getChildAt(0);
            Timber.d("Added cities %s", generatedET.getText());
        }
        getActivity().finish();
    }
    //endregion

    //region datePicker
    public static class DatePickerFragment extends android.support.v4.app.DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            super.onCreateOptionsMenu(menu, inflater);
            getActivity().getMenuInflater().inflate(R.menu.menu_dialog, menu);

        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }
    }
    //endregion
}

