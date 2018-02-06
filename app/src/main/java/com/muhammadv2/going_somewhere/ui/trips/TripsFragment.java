package com.muhammadv2.going_somewhere.ui.trips;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.muhammadv2.going_somewhere.App;
import com.muhammadv2.going_somewhere.Constants;
import com.muhammadv2.going_somewhere.R;
import com.muhammadv2.going_somewhere.model.DataInteractor;
import com.muhammadv2.going_somewhere.model.Trip;
import com.muhammadv2.going_somewhere.model.data.TravelsDbContract;
import com.muhammadv2.going_somewhere.ui.trips.addTrip.AddTripActivity;
import com.muhammadv2.going_somewhere.utils.FormattingUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * A placeholder fragment containing a simple view.
 */

public class TripsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
        , TripsAdapter.OnItemClickListener {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    TripsAdapter adapter;

    @Inject
    DataInteractor interactor;

    public TripsFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        initLoader();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        App.getInstance().getAppComponent().inject(this);

        return inflater.inflate(R.layout.fragment_trips, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), AddTripActivity.class);
                startActivityForResult(intent, Constants.OPEN_ADD_TRIP_REQ);
            }
        });
    }

    private void initLoader() {
        getActivity().getSupportLoaderManager()
                .initLoader(Constants.TRIPS_LOADER_INIT, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return interactor.createTripsCursorLoader();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Timber.plant(new Timber.DebugTree());
        Timber.d("onLoadFinished called");
        if (data == null) return;
        createRecyclerView(data);

    }

    private void createRecyclerView(Cursor data) {

        View emptyView = getActivity().findViewById(R.id.empty_view_trips);
        recyclerView = getActivity().findViewById(R.id.rv_trip);

        if (data.getCount() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            return;
        }
        emptyView.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);

        layoutManager = new LinearLayoutManager(getActivity());
        adapter = new TripsAdapter(getActivity(), extractTripsFromCursor(data), this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<Trip> extractTripsFromCursor(Cursor cursor) {

        int nameColumnInd = cursor.getColumnIndex(TravelsDbContract.TripEntry.COLUMN_TRIP_NAME);
        int startColumnInd = cursor.getColumnIndex(TravelsDbContract.TripEntry.COLUMN_TIME_START);
        int endColumnInd = cursor.getColumnIndex(TravelsDbContract.TripEntry.COLUMN_TIME_END);
        int citiesColumnInd = cursor.getColumnIndex(TravelsDbContract.TripEntry.COLUMN_CITIES_NAMES);

        ArrayList<Trip> trips = new ArrayList<>();

        while (cursor.moveToNext()) {
            String tripTitle = cursor.getString(nameColumnInd);
            long startTime = cursor.getLong(startColumnInd);
            long endTime = cursor.getLong(endColumnInd);
            String cities = cursor.getString(citiesColumnInd);
            trips.add(new Trip(tripTitle,
                    startTime,
                    endTime,
                    FormattingUtils.stringCitiesToArrayList(cities)));
            Timber.plant(new Timber.DebugTree());
            Timber.d(tripTitle);
        }

        return trips;

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        adapter = new TripsAdapter(null, null, null);
    }

    @Override
    public void onClick(int position) {

    }
}
