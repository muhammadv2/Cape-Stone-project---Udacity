package com.muhammadv2.going_somewhere.ui.tripDetails;


import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.muhammadv2.going_somewhere.Constants;
import com.muhammadv2.going_somewhere.R;
import com.muhammadv2.going_somewhere.model.CityPlace;
import com.muhammadv2.going_somewhere.ui.tripDetails.placeDetails.AddPlaceDialog;
import com.muhammadv2.going_somewhere.ui.tripDetails.placeDetails.PlaceDetailsDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.muhammadv2.going_somewhere.model.data.TravelsDbContract.PlaceEntry;
import static com.muhammadv2.going_somewhere.utils.NetworkUtils.isNetworkAvailable;


public class TripDetailsFragment extends Fragment implements TripDetailsAdapter.OnItemClickListener,
        LoaderManager.LoaderCallbacks<Cursor>, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.rv_trip_details)
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    TripDetailsAdapter adapter;

    @BindView(R.id.btn_edit_trip)
    TextView btnAddPlace;

    @BindView(R.id.adView)
    AdView adView;

    private Fragment fragment;

    private int tripPosition;

    private ArrayList<CityPlace> mPlaces;

    private GoogleApiClient mClient;

    private PlaceBuffer placeBuffer;


    @Override
    public void onResume() {
        super.onResume();
        initLoader();
    }

    public TripDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(Constants.KEY_RV_POSITION, layoutManager.onSaveInstanceState());
        outState.putParcelableArrayList(Constants.PLACES_ARRAY_REQ, mPlaces);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trip_details, container, false);

        ButterKnife.bind(this, view);

        createRecyclerView();
        Intent intent = getActivity().getIntent();
        tripPosition = intent.getIntExtra(Constants.TRIP_POSITION, 0);

        if (savedInstanceState != null) {
            mPlaces = savedInstanceState.getParcelableArrayList(Constants.TRIPS_ARRAY_ID);

            Parcelable lmState =
                    savedInstanceState.getParcelable(Constants.KEY_RV_POSITION);
            if (lmState != null) {
                layoutManager.onRestoreInstanceState(lmState);
                recyclerView.setLayoutManager(layoutManager);

            }

            adapter = new TripDetailsAdapter(mPlaces, this);
            recyclerView.setAdapter(adapter);
        }

        fragment = this;

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Build up the LocationServices API client
        // Uses the addApi method to request the LocationServices API
        // Also uses enableAutoManage to automatically when to connect/suspend the client
        mClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(getActivity(), this)
                .build();


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

        AdRequest adRequest = new AdRequest.Builder()
                .build();

        adView.loadAd(adRequest);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        restartLoader();
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

    public void restartLoader() {
        getActivity().getSupportLoaderManager()
                .restartLoader(Constants.TRIPS_LOADER_INIT, null, this);
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

        mPlaces = extractPlacesfromCursor(data);
        // When data done loading Use the helper method to extract the data from the cursor and then
        // instantiate new Adapter with that data and set the adapter on the recycler view
        adapter = new TripDetailsAdapter(mPlaces, this);
        recyclerView.setAdapter(adapter);

        /* Add a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         and uses callbacks to signal when a user is performing these actions.
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback
                (0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView,
                                  RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete

                //  Construct the URI for the item to delete
                //[Hint] Use getTag (from the adapter code) to get the id of the swiped item
                // Retrieve the id of the task to delete
                int id = (int) viewHolder.itemView.getTag();

                // Build appropriate uri with String row id appended
                String stringId = Integer.toString(mPlaces.get(id).getPlaceDbId());
                Uri uri = PlaceEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();

                //Delete a single row of data using a ContentResolver
                int deletedRows = getActivity().getContentResolver().delete(uri, null, null);
                if (deletedRows > 0) {
                    Toast.makeText(getContext(), R.string.place_deleted, Toast.LENGTH_SHORT).show();
                    restartLoader();

                } else {
                    Toast.makeText(getContext(), R.string.error_deleing, Toast.LENGTH_SHORT).show();
                }

            }
        }).attachToRecyclerView(recyclerView); // Attach this delete function to our recyclerView

        if (mPlaces.size() != 0) {
            if (mPlaces.get(0).getPlaceId() == null || mPlaces == null) return;

            ArrayList<String> strings = new ArrayList<>();
            for (CityPlace place : mPlaces) {
                strings.add(place.getPlaceId());
            }
            String[] placeIds = new String[mPlaces.size()];
            placeIds = strings.toArray(placeIds);

            for (int i = 0; i < mPlaces.size(); i++) {
                placeIds[i] = mPlaces.get(i).getPlaceId();
            }

            if (placeIds != null && placeIds.length > 0) {

                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mClient,
                        placeIds);
                placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(@NonNull PlaceBuffer places) {
                        placeBuffer = places;
                    }

                });
            }
        }

    }

    /**
     * @param cursor with the returned data from the database
     * @return ArrayList populated with this data
     */
    private ArrayList<CityPlace> extractPlacesfromCursor(Cursor cursor) {

        ArrayList<CityPlace> places = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                int nameColId = cursor.getColumnIndex(PlaceEntry.COLUMN_PLACE_NAME);
                int tripNameColId = cursor.getColumnIndex(PlaceEntry.COLUMN_TRIP_ID);
                int placeIdColId = cursor.getColumnIndex(PlaceEntry.COLUMN_PLACE_ID);

                int idColId = cursor.getColumnIndex(PlaceEntry._ID);
                int placeDbId = cursor.getInt(idColId);

                String placeTitle = cursor.getString(nameColId);
                int tripId = cursor.getInt(tripNameColId);
                String placeId = cursor.getString(placeIdColId);

                places.add(new CityPlace(placeId, placeTitle, tripId, placeDbId));

            } while (cursor.moveToNext());
        }
        return places;
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
//endregion

    @Override
    public void onClick(int position) {

        if (isNetworkAvailable(getContext())) {
            extractPlaceDetailsFromId(position);
        } else {
            Toast.makeText(getContext(), getString(R.string.need_connection), Toast.LENGTH_LONG).show();
        }
    }

    private void extractPlaceDetailsFromId(int placePosition) {
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

        Place place = placeBuffer.get(placePosition);
        String placeName = place.getName().toString();
        String placeAddress = place.getAddress().toString();
        float placeRating = place.getRating();

        int placeDbId = mPlaces.get(placePosition).getPlaceDbId();

        PlaceDetailsDialog detailsDialog =
                PlaceDetailsDialog.newInstance(
                        mPlaces.get(placePosition).getPlaceId(),
                        placeName,
                        placeAddress,
                        placeRating,
                        tripPosition,
                        placeDbId
                );

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        // Create and show the dialog.

        detailsDialog.show(ft, Constants.ADD_TRIP_DIALOG);
        getActivity().getSupportFragmentManager().executePendingTransactions();

        Dialog yourDialog = detailsDialog.getDialog();
        yourDialog.getWindow().setLayout((10 * width) / 10, (4 * height) / 5);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (placeBuffer != null)
            placeBuffer.release();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
