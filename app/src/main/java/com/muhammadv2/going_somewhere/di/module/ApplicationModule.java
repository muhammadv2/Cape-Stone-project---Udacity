package com.muhammadv2.going_somewhere.di.module;

import android.app.Application;
import android.content.Context;

import com.muhammadv2.going_somewhere.model.DataInteractor;
import com.muhammadv2.going_somewhere.model.data.TravelsDbHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    private final Application mApp;

    // Passing the application instance, this instance used to to provide other dependencies
    public ApplicationModule(Application app) {
        mApp = app;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return mApp.getApplicationContext();
    }

    @Provides
    @Singleton
    TravelsDbHelper provideDatabaseHelper() {
        return new TravelsDbHelper(mApp.getApplicationContext());
    }

    @Provides
    @Singleton
    DataInteractor provideDataInteractor() {
        return new DataInteractor(mApp.getApplicationContext());
    }

}
