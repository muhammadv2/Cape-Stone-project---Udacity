package com.muhammadv2.going_somewhere.trips;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.muhammadv2.going_somewhere.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class TripsFragment extends Fragment {

    public TripsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trips, container, false);
    }
}
