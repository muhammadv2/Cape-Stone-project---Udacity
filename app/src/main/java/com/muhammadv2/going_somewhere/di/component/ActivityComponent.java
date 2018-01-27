package com.muhammadv2.going_somewhere.di.component;

import com.muhammadv2.going_somewhere.MainActivity;
import com.muhammadv2.going_somewhere.di.PerActivity;
import com.muhammadv2.going_somewhere.di.module.ActivityModule;

import dagger.Component;

@PerActivity
@Component(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

}