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
import com.muhammadv2.going_somewhere.model.City;
import com.muhammadv2.going_somewhere.model.DataInteractor;
import com.muhammadv2.going_somewhere.model.Trip;
import com.muhammadv2.going_somewhere.model.data.TravelsDbContract;
import com.muhammadv2.going_somewhere.ui.trips.addTrip.AddTripActivity;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        App.getInstance().getAppComponent().inject(this);

        initLoader();
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
        getActivity().getSupportLoaderManager().initLoader(20222, null, this);
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
//        mTripsAdapter.swapCursor(data);
//        if (mPosition != ListView.INVALID_POSITION) {
//            // If we don't need to restart the loader, and there's a desired position to restore
//            // to, do so now.
//            mListView.smoothScrollToPosition(mPosition);

    }

    private void createRecyclerView(Cursor data) {
        recyclerView = getActivity().findViewById(R.id.rv_trip);
        layoutManager = new LinearLayoutManager(getActivity());
        adapter = new TripsAdapter(getActivity(), extractTripsFromCursor(data), this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

    }

    private ArrayList<Trip> extractTripsFromCursor(Cursor cursor) {

        ArrayList<Trip> trips = new ArrayList<>();
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            String tripTitle
                    = cursor.getString(cursor.getColumnIndex(TravelsDbContract.TripEntry.COLUMN_TRIP_NAME));
            trips.add(new Trip(tripTitle, 0, 0, new ArrayList<City>()));
            Timber.plant(new Timber.DebugTree());
            Timber.d(tripTitle);
        }

        return trips;

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(int position) {

    }
}
