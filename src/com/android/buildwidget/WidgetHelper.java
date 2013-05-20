package com.android.buildwidget;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class WidgetHelper {
    public static void setClickEvents(Context context, RemoteViews views) {
        views.setOnClickPendingIntent(R.id.text, awesomeIntent(context));
        views.setOnClickPendingIntent(R.id.imageView, serviceIntent(context));

    }

    public static PendingIntent awesomeIntent(Context context) {
        Intent intent = new Intent(context, BuildWidget.class);
        intent.setAction(BuildWidget.YOUR_AWESOME_ACTION);
        // Get the layout for the App Widget and attach an on-click listener to the button
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    public static PendingIntent serviceIntent(Context context) {
        Intent intent = new Intent();
        intent.setAction(EchoService.ACTION);
        // Get the layout for the App Widget and attach an on-click listener to the button
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
}
