package com.muhammadv2.going_somewhere.di.component;

import com.muhammadv2.going_somewhere.di.module.ApplicationModule;
import com.muhammadv2.going_somewhere.model.data.TravelsProvider;

import javax.inject.Singleton;

import dagger.Component;

/**
 * App Component which links App class dependency and the ApplicationModule
 */
@Component(modules = {ApplicationModule.class})
@Singleton
public interface AppComponent {

    //inject the ContentProvider sub class to the application component
    void inject(TravelsProvider provider);

}
