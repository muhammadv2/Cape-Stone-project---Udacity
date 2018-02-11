package com.muhammadv2.going_somewhere.ui.tripDetails;


import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
import com.muhammadv2.going_somewhere.ui.tripDetails.placeDetails.AddPlaceDialog;
import com.muhammadv2.going_somewhere.ui.tripDetails.placeDetails.PlaceDetailsDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;
import static com.muhammadv2.going_somewhere.model.data.TravelsDbContract.PlaceEntry;


public class TripDetailsFragment extends Fragment implements TripDetailsAdapter.OnItemClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.rv_trip_details)
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    TripDetailsAdapter adapter;

    @BindView(R.id.btn_edit_trip)
    TextView btnAddPlace;

    private Fragment fragment;

    private int tripPosition;

    private String placeId;

    private String placeName;

    private String placeAddress;
    private float placeRating;


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
        Timber.plant(new Timber.DebugTree());

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
                Fragment prev = fm.findFragmentByTag(Constants.ADD_TRIP_DIALOG);
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                DisplayMetrics metrics = getResources().getDisplayMetrics();
                int width = metrics.widthPixels;
                int height = metrics.heightPixels;

                // Create and show the dialog.
                AddPlaceDialog dialog = AddPlaceDialog.newInstance(tripPosition);
                dialog.setTargetFragment(fragment, Constants.DIALOG_FRAGMENT_REQUEST);

                dialog.show(ft, Constants.ADD_TRIP_DIALOG);
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

        Uri queryUri = PlaceEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(tripPosition)).build();

        // Create cursor loader with the URI from trip table
        return new CursorLoader(getContext(),
                queryUri,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null) return; // If data is null don't go any further and return

        Timber.d("onLoadFinished " + extractTripsFromCursor(data));
        // When data done loading Use the helper method to extract the data from the cursor and then
        // instantiate new Adapter with that data and set the adapter on the recycler view
        adapter = new TripDetailsAdapter(extractTripsFromCursor(data), this);
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
                int nameColId = cursor.getColumnIndex(PlaceEntry.COLUMN_PLACE_NAME);
                int tripNameColId = cursor.getColumnIndex(PlaceEntry.COLUMN_TRIP_ID);

                String placeTitle = cursor.getString(nameColId);
                int tripId = cursor.getInt(tripNameColId);

                places.add(new CityPlace(placeId, placeTitle, tripId));

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

                placeId = data.getStringExtra(Constants.PLACE_ID);
                placeName = data.getStringExtra(Constants.PLACE_NAME);
                placeAddress = data.getStringExtra(Constants.PLACE_ADRESS);
                placeRating = data.getFloatExtra(Constants.PLACE_RATING, 2.5f);

                getActivity().getSupportLoaderManager()
                        .restartLoader(Constants.TRIPS_LOADER_INIT, null, this);
            }
        }

    }

    @Override
    public void onClick(int position) {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag(Constants.ADD_TRIP_DIALOG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        PlaceDetailsDialog detailsDialog =
                PlaceDetailsDialog.newInstance(placeName, placeAddress, placeRating, null);
        Timber.d("ss" + placeName);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        // Create and show the dialog.

        detailsDialog.show(ft, Constants.ADD_TRIP_DIALOG);
        getActivity().getSupportFragmentManager().executePendingTransactions();

        Dialog yourDialog = detailsDialog.getDialog();
        yourDialog.getWindow().setLayout((10 * width) / 10, (4 * height) / 5);
    }
}
