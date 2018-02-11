package com.muhammadv2.going_somewhere.ui.tripDetails;


import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.muhammadv2.going_somewhere.Constants;
import com.muhammadv2.going_somewhere.R;
import com.muhammadv2.going_somewhere.model.CityPlace;
import com.muhammadv2.going_somewhere.ui.tripDetails.addPlace.AddPlaceDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;
import static com.muhammadv2.going_somewhere.model.data.TravelsDbContract.PlaceEntry;
import static com.muhammadv2.going_somewhere.model.data.TravelsDbContract.TripEntry;


//Todo Show the places names and make a dialog to show every place details and refactor the dialog fragment
//Todo to only return the place id and show the place data using that id
//Make a wdiget for the application and finish design for tablet .... ... . .. فات الكتير ماباقي الي القليل
public class TripDetailsFragment extends Fragment implements TripDetailsAdapter.OnItemClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.rv_trip_details)
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    TripDetailsAdapter adapter;

    @BindView(R.id.btn_add_place)
    TextView btnAddPlace;

    private Fragment fragment;

    private int tripPosition;

    @Override
    public void onResume() {
        super.onResume();
        initLoader();
    }

    public TripDetailsFragment() {
        // Required empty public constructor
    }

//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        // Save The array list of trips as a bundle to be retrieved when rotation happens
//        Bundle bundle = new Bundle();
//        bundle.putParcelableArrayList(Constants.TRIPS_ARRAY_ID, trips);
//        outState.putBundle(Constants.TRIPS_ARRAY_ID, bundle);
//    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trip_details, container, false);

        ButterKnife.bind(this, view);

        // Injecting this fragment to the app component so the interactor class can be injected
//        App.getInstance().getAppComponent().inject(this);
        createRecyclerView();
        Intent intent = getActivity().getIntent();
        tripPosition = intent.getIntExtra(Constants.TRIP_POSITION, 0);

        // Retrieve the bundle and extract the ArrayList and restart the loader using it
//        if (savedInstanceState != null) {
//            Bundle bundle = savedInstanceState.getBundle(Constants.TRIPS_ARRAY_ID);
//        }

        fragment = this;

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnAddPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // DialogFragment.show() will take care of adding the fragment
                // in a transaction.  We also want to remove any currently showing
                // dialog, so make our own transaction and take care of that here.
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment prev = fm.findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                DisplayMetrics metrics = getResources().getDisplayMetrics();
                int width = metrics.widthPixels;
                int height = metrics.heightPixels;

                // Create and show the dialog.
                AddPlaceDialog dialog = new AddPlaceDialog();
                dialog.setTargetFragment(fragment, Constants.DIALOG_FRAGMENT_REQUEST);

                dialog.show(ft, "dialog");
                getActivity().getSupportFragmentManager().executePendingTransactions();

                Dialog yourDialog = dialog.getDialog();
                yourDialog.getWindow().setLayout((6 * width) / 7, (2 * height) / 5);

            }
        });
    }

    /**
     * Method that create recycler view and set the layoutManager and an empty adapter on it
     */
    private void createRecyclerView() {
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new TripDetailsAdapter(null, this);
        recyclerView.setAdapter(adapter);

    }


    /**
     * Init the loader to start query the database and retrieve a cursor object
     */
    private void initLoader() {
        getActivity().getSupportLoaderManager()
                .initLoader(Constants.TRIPS_LOADER_INIT, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Create cursor loader with the URI from trip table
        String[] selectionArgs = {String.valueOf(tripPosition)};
        return new CursorLoader(getContext(),
                PlaceEntry.CONTENT_URI,
                null,
                PlaceEntry.COLUMN_TRIP_ID + "=?",
                selectionArgs,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null) return; // If data is null don't go any further and return

        // When data done loading Use the helper method to extract the data from the cursor and then
        // instantiate new Adapter with that data and set the adapter on the recycler view
        adapter = new TripDetailsAdapter(extractTripsFromCursor(data), this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    /**
     * @param cursor with the returned data from the database
     * @return ArrayList populated with this data
     */
    private ArrayList<CityPlace> extractTripsFromCursor(Cursor cursor) {

        ArrayList<CityPlace> places = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                int nameColId = cursor.getColumnIndex(TripEntry.COLUMN_TRIP_NAME);
                int cityNameCoId = cursor.getColumnIndex(TripEntry.COLUMN_TIME_START);
                int tripNameColId = cursor.getColumnIndex(TripEntry.COLUMN_TIME_START);

                String placeTitle = cursor.getString(nameColId);
                String cityName = cursor.getString(cityNameCoId);
                String tripName = cursor.getString(tripNameColId);

                places.add(new CityPlace(placeTitle, cityName, tripName));

            } while (cursor.moveToNext());
        }
        return places;
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
    //endregion

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.DIALOG_FRAGMENT_REQUEST) {
            if (resultCode == RESULT_OK) {

                String placeAdress = data.getStringExtra(Constants.PLACE_ADRESS);
                String placeName = data.getStringExtra(Constants.PLACE_NAME);
                String placeId = data.getStringExtra(Constants.PLACE_ID);
                float placeRating = data.getFloatExtra(Constants.PLACE_RATING, 2.5f);

                getActivity().getSupportLoaderManager()
                        .restartLoader(Constants.TRIPS_LOADER_INIT, null, this);

            }
        }


    }

    @Override
    public void onClick(int position) {

    }
}
