package com.muhammadv2.going_somewhere.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.muhammadv2.going_somewhere.Constants;
import com.muhammadv2.going_somewhere.R;
import com.muhammadv2.going_somewhere.ui.tripDetails.TripDetailsActivity;
import com.squareup.picasso.Picasso;

/**
 * Implementation of App Widget functionality.
 */
public class TripWidget extends AppWidgetProvider {

    private static String tripName;
    private static String imageUrl;
    private static int tripPosition;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.trip_widget);

        if (tripName == null) {
            views.setTextViewText(R.id.widget_trip_name, context.getString(R.string.specify_trip));
        }
        if (imageUrl != null)
            Picasso.with(context)
                    .load(imageUrl)
                    .into(views, R.id.widget_photo, new int[]{appWidgetId});

        views.setTextViewText(R.id.widget_trip_name, tripName);

        Intent intent = new Intent(context, TripDetailsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(Constants.TRIP_POSITION, tripPosition);
        intent.putExtra(Constants.ADD_TRIP_NAME, tripName);
        intent.putExtra(Constants.ADD_TRIP_IMAGE, imageUrl);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_container, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(intent.getAction())) {

            if (intent != null) {
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                ComponentName thisAppWidget =
                        new ComponentName(context.getPackageName(), TripWidget.class.getName());
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);

                tripName = intent.getStringExtra(Constants.ADD_TRIP_NAME);
                tripPosition = intent.getIntExtra(Constants.TRIP_POSITION, 0);
                imageUrl = intent.getStringExtra(Constants.ADD_TRIP_IMAGE);

                Toast.makeText(context, "Widget updated", Toast.LENGTH_LONG).show();
                onUpdate(context, appWidgetManager, appWidgetIds);
            }
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

