package com.android.buildwidget;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class WidgetHelper {
    public static void setClickEvents(Context context, RemoteViews views) {
        views.setOnClickPendingIntent(R.id.imageView, serviceIntent(context));

    }

    public static void toggleView(RemoteViews views, int count) {
        views.setImageViewResource(R.id.imageView, getView(count));
    }

    private static PendingIntent serviceIntent(Context context) {
        Intent intent = new Intent();
        intent.setAction(EchoService.ACTION);
        // Get the layout for the App Widget and attach an on-click listener to the button
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    private static int getView(int count) {
        if (count % 2 == 0) return R.drawable.button_pressed_yellow;
        return R.drawable.button_normal_green;
    }
}
