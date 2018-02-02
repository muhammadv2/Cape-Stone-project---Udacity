package com.muhammadv2.going_somewhere.ui.tripDetails;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.muhammadv2.going_somewhere.Constants;
import com.muhammadv2.going_somewhere.R;

import timber.log.Timber;

import static android.app.Activity.RESULT_OK;

//Todo(4) This Fragment needs Activity as a holder
//Todo(5) Make the view for this fragment and also the add new place fragment
//Todo(6) implement MVP for this fragment
public class TripDetailsFragment extends Fragment {


    public TripDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(getActivity(), data);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trip_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
        Intent intent = null;
        try {
            intent = intentBuilder.build(getActivity());
        } catch (GooglePlayServicesRepairableException |
                GooglePlayServicesNotAvailableException e) {
            Timber.d("Builder not successeded");
            e.printStackTrace();
        }
        if (intent != null) {
            startActivityForResult(intent, 401);
        }
    }
}
