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
import timber.log.Timber;

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

    private Intent intent;

    private int placePosition;

    private CityPlace cPlace;

    private int tripId;
    private Intent receivedIntent;

    public AddPlaceDialog() {
        // Required empty public constructor
    }

    public static AddPlaceDialog newInstance(int tripPosition) {
        AddPlaceDialog f = new AddPlaceDialog();

        // Supply tripPosition input as an argument.
        Bundle args = new Bundle();
        args.putInt(Constants.TRIP_POSITION, tripPosition);
        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_place, container, false);

        ButterKnife.bind(this, view);

        tripId = getArguments().getInt(Constants.TRIP_POSITION);

        //inject this fragment into the app component
        App.getInstance().getAppComponent().inject(this);

        Timber.plant(new Timber.DebugTree());

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnSearchLocation.setOnClickListener(this);
        btnSavePlace.setOnClickListener(this);

        intent = new Intent();

//        receivedIntent = getActivity().getIntent();
//        tripId = receivedIntent.getStringExtra(Constants.TRIPS_ARRAY_ID);
//
//        if (receivedIntent != null) {
//            etPlaceTitle.setText(cPlace.getPlaceName());
//        }
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_edit_trip:
                String placeTitle = etPlaceTitle.getText().toString();

                if (!placeTitle.isEmpty()) {
                    Timber.d("add place clicked");

                    cPlace = new CityPlace("", placeTitle, tripId);

                    if (receivedIntent == null) {
                        Uri uri = interactor.insertIntoPlaceTable(cPlace);
                        if (uri != null)
                            Toast.makeText(getActivity(), R.string.place_added, Toast.LENGTH_LONG).show();
                    } else {
                        int rowsAffected = interactor.updatePlaceTable(cPlace, placePosition);

                        // Show a toast message depending on whether or not the update was successful.
                        if (rowsAffected == 0) {
                            // If no rows were affected, then there was an error with the update.
                            Toast.makeText(getContext(), getString(R.string.error_updating_place),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Otherwise, the update was successful and we can display a toast.
                            Toast.makeText(getContext(), getString(R.string.update_place_successful),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    getTargetFragment().onActivityResult(Constants.DIALOG_FRAGMENT_REQUEST, RESULT_OK, intent);
                    this.dismiss();


                } else {
                    Toast.makeText(getContext(), R.string.err_add_title, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_search_location:
                try {
                    Timber.d("add search clicked");
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Timber.d("onActivity invoked");
        if (requestCode == Constants.PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getContext(), data);
                String placeID = place.getId();
                String placeName = place.getName().toString();
                String placeAddress = place.getAddress().toString();
                float placeRating = place.getRating();

                intent = new Intent();
                intent.putExtra(Constants.PLACE_NAME, placeName);
                intent.putExtra(Constants.PLACE_ID, placeID);
                intent.putExtra(Constants.PLACE_ADRESS, placeAddress);
                intent.putExtra(Constants.PLACE_RATING, placeRating);

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                getActivity().setResult(RESULT_CANCELED);

            }
        }
    }

}
