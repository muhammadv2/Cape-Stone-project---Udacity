package com.muhammadv2.going_somewhere.model.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.CursorLoader;

import com.muhammadv2.going_somewhere.di.ApplicationContext;

import javax.inject.Inject;


public class QueryCursorLoader extends CursorLoader {
    //Todo (6) Implement the cursor loader to query the data from all the tables

    @Inject
    public QueryCursorLoader(@ApplicationContext @NonNull Context context) {
        super(context);
    }


}
