package com.muhammadv2.going_somewhere.di.component;

import com.muhammadv2.going_somewhere.di.module.NetworkModule;
import com.muhammadv2.going_somewhere.model.network.UnsplashApi;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {NetworkModule.class})
public interface NetworkComponent {

    UnsplashApi unsplashApi();
}
