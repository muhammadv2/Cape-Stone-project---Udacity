package com.muhammadv2.going_somewhere.ui.tripDetails.addPlace;


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
    @BindView(R.id.btn_add_place)
    ImageView btnSavePlace;

    @Inject
    DataInteractor interactor;

    private Intent intent;

    private CityPlace cPlace;
    private int placePosition;

    private String tripName;
    private String cityName;

    private Intent receivedIntent;


    //Todo turn it into dialog and it have only place to add name and choose from the button
    public AddPlaceDialog() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_place, container, false);

        ButterKnife.bind(this, view);

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
//        cPlace = receivedIntent.getParcelableExtra(Constants.Place_OBJ_EXTRA);
//        tripName = receivedIntent.getStringExtra(Constants.TRIPS_ARRAY_ID);
//        cityName = receivedIntent.getStringExtra(Constants.CITIES_ARRAY_ID);
//
//        if (receivedIntent != null) {
//            etPlaceTitle.setText(cPlace.getPlaceName());
//        }
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_add_place:
                String placeTitle = etPlaceTitle.getText().toString();

                if (!placeTitle.isEmpty()) {
                    Timber.d("add place clicked");

                    cPlace = new CityPlace(placeTitle, cityName, tripName);

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
                    getTargetFragment().onActivityResult(RESULT_OK, Constants.DIALOG_FRAGMENT_REQUEST, intent);


                } else {
                    Toast.makeText(getContext(), R.string.err_add_title, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_search_location:
                try {
                    Timber.d("add search clicked");
                    Fragment callingFragment = getActivity().getSupportFragmentManager().getFragments().get(0);

                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .build(getActivity());
                    callingFragment.startActivityForResult(intent, Constants.PLACE_AUTOCOMPLETE_REQUEST_CODE);
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

                Timber.d("choosen place " + placeName);

                intent = new Intent();
                intent.putExtra(Constants.PLACE_NAME, placeName);
                intent.putExtra(Constants.PLACE_ID, placeID);
                intent.putExtra(Constants.PLACE_ADRESS, placeAddress);
                intent.putExtra(Constants.PLACE_RATING, placeRating);

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                getActivity().setResult(RESULT_CANCELED);

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation simply do nothing.
            }
        }
    }

}
