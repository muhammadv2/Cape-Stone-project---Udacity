package com.muhammadv2.going_somewhere.ui.tripDetails.placeDetails;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.muhammadv2.going_somewhere.App;
import com.muhammadv2.going_somewhere.Constants;
import com.muhammadv2.going_somewhere.R;
import com.muhammadv2.going_somewhere.model.CityPlace;
import com.muhammadv2.going_somewhere.model.DataInteractor;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddPlaceDialog extends android.support.v4.app.DialogFragment
        implements View.OnClickListener {

    @BindView(R.id.et_place_title)
    EditText etPlaceTitle;
    @BindView(R.id.btn_search_location)
    Button btnSearchLocation;
    @BindView(R.id.btn_edit_trip)
    ImageView btnSavePlace;

    @Inject
    DataInteractor interactor;

    private int placeDbPosition;

    private int tripId;

    private String placeID;
    private String placeName;


    public AddPlaceDialog() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.PLACE_ID, placeID); //save the place id upon rotation
    }

    //region Instances
    // New instance called from the parent fragment to add new place
    public static AddPlaceDialog newInstance(int tripPosition) {
        AddPlaceDialog f = new AddPlaceDialog();

        // Supply tripPosition input as an argument.
        Bundle args = new Bundle();
        args.putInt(Constants.TRIP_POSITION, tripPosition);
        f.setArguments(args);

        return f;
    }

    // New instance called from place details dialog to edit the current place
    public static AddPlaceDialog newEditInstance(int tripPosition, int placePosition, String placeId,
                                                 String placeName) {
        AddPlaceDialog f = new AddPlaceDialog();

        // Supply tripPosition input as an argument.
        Bundle args = new Bundle();
        args.putInt(Constants.TRIP_POSITION, tripPosition);
        args.putInt(Constants.PLACE_DB_ID, placePosition);
        args.putString(Constants.PLACE_ID, placeId);
        args.putString(Constants.PLACE_NAME, placeName);
        f.setArguments(args);

        return f;
    }

    //endregion

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_place, container, false);

        ButterKnife.bind(this, view); // Bind butter knife to this dialog

        // Retrieve the data passed to this instance of dialog
        tripId = getArguments().getInt(Constants.TRIP_POSITION);
        placeDbPosition = getArguments().getInt(Constants.PLACE_DB_ID);
        placeID = getArguments().getString(Constants.PLACE_ID);
        placeName = getArguments().getString(Constants.PLACE_NAME);

        if (placeName != null) etPlaceTitle.setText(placeName);

        //inject this fragment into the app component
        App.getInstance().getAppComponent().inject(this);

        if (savedInstanceState != null) placeID = savedInstanceState.getString(Constants.PLACE_ID);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set listeners on this dialog buttons
        btnSearchLocation.setOnClickListener(this);
        btnSavePlace.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_edit_trip:

                // Extract the text form et and check the if any field null before work against the db
                String placeTitle = etPlaceTitle.getText().toString();
                if (!placeTitle.isEmpty() && placeID != null) {

                    CityPlace cPlace;
                    if (placeName == null) {
                        // If the title is null then create a new CityPlace and insert it into db
                        cPlace = new CityPlace(placeID, placeTitle, tripId, 0);
                        Uri uri = interactor.insertIntoPlaceTable(cPlace);
                        if (uri != null) // Successfully inserted show a toast
                            Toast.makeText(getActivity(), R.string.place_added, Toast.LENGTH_LONG).show();
                    } else {
                        // If the title not null then update the place with the ned data
                        cPlace = new CityPlace(placeID, placeName, tripId, placeDbPosition);
                        int rowsAffected = interactor.updatePlaceTable(cPlace, placeDbPosition);

                        // Show a toast message depending on whether or not the update was successful.
                        if (rowsAffected == 0) {
                            // If no rows were affected, then there was an error with the update.
                            Toast.makeText(getContext(), getString(R.string.error_updating_place),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Otherwise, the update was successful and we can display a toast.
                            Toast.makeText(getContext(), getString(R.string.update_place_successful),
                                    Toast.LENGTH_SHORT).show();

                            //Todo the updated placed not showing directly in the tripdetails rv
                        }
                    }
                    this.dismiss();

                } else { // When there's missing details notify the user to add them with a toast
                    Toast.makeText(getContext(), R.string.err_add_title, Toast.LENGTH_SHORT).show();
                }
                break;
            // On the search location button clicked use the google places api to let the user
            //choose a place
            case R.id.btn_search_location:
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .build(getActivity());
                    startActivityForResult(intent, Constants.PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException |
                        GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
        }
    }

    /**
     * When the user open the dialog to add new place this method will be a listener if successfully
     * choose place then the result will be okay if not the result will be canceled
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getContext(), data);
                placeID = place.getId(); // Update the member variable with the retrieved placeId

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                getActivity().setResult(RESULT_CANCELED);

            }
        }
    }

}
