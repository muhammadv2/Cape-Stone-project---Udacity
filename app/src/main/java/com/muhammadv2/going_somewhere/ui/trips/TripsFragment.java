package com.muhammadv2.going_somewhere.ui.trips;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.muhammadv2.going_somewhere.Constants;
import com.muhammadv2.going_somewhere.R;
import com.muhammadv2.going_somewhere.ui.trips.addTrip.AddTripActivity;

import javax.inject.Inject;

import static com.muhammadv2.going_somewhere.model.data.TravelsDbContract.TripEntry;

/**
 * A placeholder fragment containing a simple view.
 */

//Todo(3) Implement MVP for this view and then connect it with add Trip fragment and implement its RecyclerView
//Todo(4) Hook the UI with the adapter and return the needed data from the presenter
public class TripsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    @Inject
    RecyclerView recyclerView;
    @Inject
    RecyclerView.LayoutManager layoutManager;
    @Inject
    RecyclerView.Adapter adapter;
    @Inject
    TripsPresenter presenter;

    public TripsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(),
                TripEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
//        mTripsAdapter.swapCursor(data);
//        if (mPosition != ListView.INVALID_POSITION) {
//            // If we don't need to restart the loader, and there's a desired position to restore
//            // to, do so now.
//            mListView.smoothScrollToPosition(mPosition);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
