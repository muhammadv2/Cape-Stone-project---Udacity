package com.muhammadv2.going_somewhere.ui.tripDetails;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.muhammadv2.going_somewhere.Constants;
import com.muhammadv2.going_somewhere.R;
import com.muhammadv2.going_somewhere.ui.tripDetails.addPlace.AddPlaceDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;

public class TripDetailsFragment extends Fragment implements TripDetailsAdapter.OnItemClickListener {

    @BindView(R.id.rv_trip_details)
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    TripDetailsAdapter adapter;

    private Fragment fragment;

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
//        trips = new ArrayList<>();

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

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
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
        adapter = new TripDetailsAdapter(getContext(), null, null, this);
        recyclerView.setAdapter(adapter);

    }


//    /**
//     * @param cursor with the returned data from the database
//     * @return ArrayList populated with this data
//     */
//    private ArrayList<Trip> extractTripsFromCursor(Cursor cursor) {
//
//        ArrayList<Trip> trips = new ArrayList<>();
//        if (cursor.moveToFirst()) {
//            do {
//                int nameColumnInd = cursor.getColumnIndex(TripEntry.COLUMN_TRIP_NAME);
//                int startColumnInd = cursor.getColumnIndex(TripEntry.COLUMN_TIME_START);
//                int endColumnInd = cursor.getColumnIndex(TripEntry.COLUMN_TIME_END);
//                int citiesColumnInd = cursor.getColumnIndex(TripEntry.COLUMN_CITIES_NAMES);
//                int imageColumnInd = cursor.getColumnIndex(TripEntry.COLUMN_IMAGE_URL);
//                String tripTitle = cursor.getString(nameColumnInd);
//                long startTime = cursor.getLong(startColumnInd);
//                long endTime = cursor.getLong(endColumnInd);
//                String cities = cursor.getString(citiesColumnInd);
//                String imageUrl = cursor.getString(imageColumnInd);
//
//                Trip trip = new Trip(
//                        tripTitle,
//                        startTime,
//                        endTime,
//                        FormattingUtils.stringCitiesToArrayList(cities),
//                        imageUrl);
//
//                trips.add(trip);
//            } while (cursor.moveToNext());
//        }
//
//        this.trips = trips;
//        return trips;
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.DIALOG_FRAGMENT_REQUEST) {
            if (resultCode == RESULT_OK) {

                String placeName = data.getStringExtra(Constants.PLACE_ADRESS);

                Timber.plant(new Timber.DebugTree());
                Timber.d("place det " + placeName);
            }
        }
    }

    @Override
    public void onClick(int position) {

    }
}
