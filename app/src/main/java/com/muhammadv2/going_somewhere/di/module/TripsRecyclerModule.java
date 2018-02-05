package com.muhammadv2.going_somewhere.di.module;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.muhammadv2.going_somewhere.di.ActivityContext;
import com.muhammadv2.going_somewhere.ui.trips.TripsAdapter;
import com.muhammadv2.going_somewhere.ui.trips.TripsPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class TripsRecyclerModule {

    @Provides
    public RecyclerView providesRecyclerView(@ActivityContext Context context) {
        return new RecyclerView(context);
    }

    @Provides
    public RecyclerView.LayoutManager providesLayoutManager(@ActivityContext Context context) {
        return new LinearLayoutManager(context);
    }

    @Provides
    public RecyclerView.Adapter providesAdapter(TripsAdapter adapter) {
        return adapter;
    }

    @Provides
    public TripsPresenter providesPresenter(TripsPresenter presenter) {
        return presenter;
    }
}
