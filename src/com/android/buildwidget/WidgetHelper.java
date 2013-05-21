package com.android.buildwidget;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

public class WidgetHelper {
    public static void setClickEvents(Context context, RemoteViews views) {
        views.setOnClickPendingIntent(R.id.imageView1, serviceIntent(context));

    }

    public static void toggleView(RemoteViews views, int count) {
        // To do it dynamically: http://stackoverflow.com/questions/7948059/dynamic-loading-of-images-r-drawable-using-variable
        views.setImageViewResource(R.id.imageView1, getView(count));
    }


    public static void setCount(Context context, int value) {
        SharedPreferences p = context.getSharedPreferences("data", Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = p.edit();
        editor.putInt("count", value);
        editor.commit();
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
