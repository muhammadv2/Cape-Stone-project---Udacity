package com.muhammadv2.going_somewhere.di.component;

import com.muhammadv2.going_somewhere.di.module.ActivityModule;
import com.muhammadv2.going_somewhere.di.module.TripsRecyclerModule;
import com.muhammadv2.going_somewhere.ui.tripDetails.TripDetailsFragment;
import com.muhammadv2.going_somewhere.ui.trips.TripsFragment;

import dagger.Component;

@Component(modules = {TripsRecyclerModule.class, ActivityModule.class})
public interface TripsRecyclerComponent {
    void inject(TripsFragment fragment);

    void inject(TripDetailsFragment fragment);

}
