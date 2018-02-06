package com.muhammadv2.going_somewhere;

import android.app.Application;
import android.content.Context;

import com.muhammadv2.going_somewhere.di.component.AppComponent;
import com.muhammadv2.going_somewhere.di.component.DaggerAppComponent;
import com.muhammadv2.going_somewhere.di.module.ApplicationModule;

public class App extends Application {

    public static App sApp;
    private AppComponent mAppComponent;

    public static App getInstance() {
        return sApp;
    }

    //return application module which takes application as parameter
    ApplicationModule getApplicationModule() {
        return new ApplicationModule(this);
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    void initComponent() {
        mAppComponent = DaggerAppComponent.builder()
                .applicationModule(getApplicationModule())
                .build();
    }

    //overriding this method rather than onCreate because ContentProvider initialize before
    //onCreate gets called
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        sApp = this;
        initComponent();

    }
}
