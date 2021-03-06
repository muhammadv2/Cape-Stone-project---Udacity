package com.muhammadv2.going_somewhere.ui.trips;

import android.app.ActivityOptions;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.muhammadv2.going_somewhere.App;
import com.muhammadv2.going_somewhere.Constants;
import com.muhammadv2.going_somewhere.R;
import com.muhammadv2.going_somewhere.model.DataInteractor;
import com.muhammadv2.going_somewhere.model.Trip;
import com.muhammadv2.going_somewhere.model.data.TravelsDbContract.TripEntry;
import com.muhammadv2.going_somewhere.ui.tripDetails.TripDetailsActivity;
import com.muhammadv2.going_somewhere.ui.trips.addTrip.AddTripActivity;
import com.muhammadv2.going_somewhere.utils.FormattingUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * A placeholder fragment containing a simple view.
 */

public class TripsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
        , TripsAdapter.OnItemClickListener {

    @BindView(R.id.rv_trip)
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    TripsAdapter mAdapter;

    @BindView(R.id.empty_view_trips)
    View emptyView;

    @Inject
    DataInteractor interactor;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    private ArrayList<Trip> trips;

    public TripsFragment() {
    }

    private int tripId;

    private boolean isTablet;

    @Override
    public void onResume() {
        super.onResume();
        initLoader();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save The array list of trips as a bundle to be retrieved when rotation happens

        outState.putParcelableArrayList(Constants.TRIPS_ARRAY_ID, trips);
        outState.putParcelable(Constants.KEY_RV_POSITION, mLayoutManager.onSaveInstanceState());

    }

    //region CreateView
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trips, container, false);
        ButterKnife.bind(this, view);

        isTablet = getResources().getBoolean(R.bool.large_layout);

        // Injecting this fragment to the app component so the interactor class can be injected
        App.getInstance().getAppComponent().inject(this);

        trips = new ArrayList<>();

        createRecyclerView();

        if (savedInstanceState != null) {

            trips = savedInstanceState.getParcelableArrayList(Constants.TRIPS_ARRAY_ID);

            Parcelable lmState =
                    savedInstanceState.getParcelable(Constants.KEY_RV_POSITION);
            if (lmState != null) {
                mLayoutManager.onRestoreInstanceState(lmState);
                mRecyclerView.setLayoutManager(mLayoutManager);

            }

            mAdapter = new TripsAdapter(getContext(), trips, this);
            mRecyclerView.setAdapter(mAdapter);
        }
        return view;
    }

    /**
     * Method that create recycler view and set the mLayoutManager and an empty mAdapter on it
     */
    private void createRecyclerView() {

        if (isTablet) {
            mLayoutManager = new GridLayoutManager(getContext(), 2);
        } else {
            mLayoutManager = new LinearLayoutManager(getActivity());
        }
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new TripsAdapter(getContext(), null, this);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Timber.plant(new Timber.DebugTree());

        // Listen on the fab clicks and open AddTrip to add new Trip
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddTripActivity.class);
                startActivity(intent);
            }
        });
    }
    //endregion

    //region Loader

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
        return new CursorLoader(getContext(),
                TripEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null) return; // If data is null don't go any further and return

        //if the count of the data equals 0 set the empty view to be visible else set rv visible
        if (data.getCount() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            return;
        } else {
            emptyView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }

        // When data done loading Use the helper method to extract the data from the cursor and then
        // instantiate new Adapter with that data and set the mAdapter on the recycler view
        mAdapter = new TripsAdapter(getActivity(), extractTripsFromCursor(data), this);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * @param cursor with the returned data from the database
     * @return ArrayList populated with this data
     */
    private ArrayList<Trip> extractTripsFromCursor(Cursor cursor) {

        ArrayList<Trip> trips = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                int nameColumnInd = cursor.getColumnIndex(TripEntry.COLUMN_TRIP_NAME);
                int startColumnInd = cursor.getColumnIndex(TripEntry.COLUMN_TIME_START);
                int endColumnInd = cursor.getColumnIndex(TripEntry.COLUMN_TIME_END);
                int citiesColumnInd = cursor.getColumnIndex(TripEntry.COLUMN_CITIES_NAMES);
                int imageColumnInd = cursor.getColumnIndex(TripEntry.COLUMN_IMAGE_URL);

                int idColumnId = cursor.getColumnIndex(TripEntry._ID);
                tripId = cursor.getInt(idColumnId);

                String tripTitle = cursor.getString(nameColumnInd);
                long startTime = cursor.getLong(startColumnInd);
                long endTime = cursor.getLong(endColumnInd);
                String cities = cursor.getString(citiesColumnInd);
                String imageUrl = cursor.getString(imageColumnInd);

                Trip trip = new Trip(
                        tripTitle,
                        startTime,
                        endTime,
                        FormattingUtils.stringCitiesToArrayList(cities),
                        imageUrl,
                        tripId);

                trips.add(trip);
            } while (cursor.moveToNext());
        }

        this.trips = trips;
        return trips;
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
    //endregion


    // To reach details activity upon clicking on the card of each trip
    @Override
    public void onClick(int position, ImageView iv) {

        tripId = trips.get(position).getTripId();

        Bundle bundle = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            bundle = ActivityOptions
                    .makeSceneTransitionAnimation(
                            getActivity(),
                            iv,
                            iv.getTransitionName()
                    ).toBundle();
        }


        Intent intent = new Intent(getContext(), TripDetailsActivity.class);
        intent.putExtra(Constants.TRIP_POSITION, tripId);
        intent.putExtra(Constants.PLACE_PHOTO, trips.get(position).getImageUrl());
        intent.putExtra(Constants.ADD_TRIP_NAME, trips.get(position).getTripName());
        if (bundle != null) {
            getActivity().startActivity(intent, bundle);
        } else {
            getActivity().startActivity(intent);
        }
    }


}
