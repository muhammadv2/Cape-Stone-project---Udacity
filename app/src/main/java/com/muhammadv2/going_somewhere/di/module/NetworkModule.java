package com.muhammadv2.going_somewhere.di.module;

import com.muhammadv2.going_somewhere.model.network.UnsplashApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class NetworkModule {

    private String userTripName;

    public NetworkModule(String userTripName) {
        this.userTripName = userTripName;
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        return new OkHttpClient();
    }

    @Provides
    @Singleton
    UnsplashApi provideUnsplashApi(OkHttpClient client) {
        return new UnsplashApi(client, userTripName);
    }
}
