package com.muhammadv2.going_somewhere.ui.tripDetails.placeDetails;

import android.app.DialogFragment;
import android.os.Bundle;

import com.muhammadv2.going_somewhere.Constants;


public class PlaceDetailsDialog extends DialogFragment {

    public static PlaceDetailsDialog newInstance(int tripPosition) {
        PlaceDetailsDialog f = new PlaceDetailsDialog();

        // Supply tripPosition input as an argument.
        Bundle args = new Bundle();
        args.putInt(Constants.TRIP_POSITION, tripPosition);
        f.setArguments(args);

        return f;
    }

}
