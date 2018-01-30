package com.muhammadv2.going_somewhere.trips;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.muhammadv2.going_somewhere.R;

public class AddTripDialog extends Dialog {

    public AddTripDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_trip);
    }
}
