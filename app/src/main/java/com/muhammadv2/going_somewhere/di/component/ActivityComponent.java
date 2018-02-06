package com.muhammadv2.going_somewhere.di.component;

import android.app.Activity;
import android.content.Context;

import com.muhammadv2.going_somewhere.di.ActivityContext;
import com.muhammadv2.going_somewhere.di.PerActivity;
import com.muhammadv2.going_somewhere.di.module.ActivityModule;

import dagger.Component;

@PerActivity
@Component(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(Activity activity);

    @ActivityContext
    Context getContext();

}