package com.muhammadv2.going_somewhere.ui.tripDetails.addPlace;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.muhammadv2.going_somewhere.R;

import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddPlaceFragment extends Fragment implements View.OnClickListener {


    public AddPlaceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_place, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

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
