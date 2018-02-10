package com.muhammadv2.going_somewhere.di.component;

import com.muhammadv2.going_somewhere.di.module.ApplicationModule;
import com.muhammadv2.going_somewhere.model.data.TravelsProvider;
import com.muhammadv2.going_somewhere.ui.tripDetails.addPlace.AddPlaceDialog;
import com.muhammadv2.going_somewhere.ui.trips.TripsFragment;
import com.muhammadv2.going_somewhere.ui.trips.addTrip.AddTripFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * App Component which links App class dependency and the ApplicationModule
 */
@Component(modules = {ApplicationModule.class})
@Singleton
public interface AppComponent {

    // Tell the Dagger to scan this class through the implementation of this interface.
    void inject(TravelsProvider provider);

    void inject(AddTripFragment addTrip);

    void inject(TripsFragment tripsFragment);

    void inject(AddPlaceDialog placeFragment);

}
