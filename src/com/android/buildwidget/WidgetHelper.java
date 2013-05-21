package com.android.buildwidget;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

public class WidgetHelper {
    public static void setClickEvents(Context context, RemoteViews views) {
        views.setOnClickPendingIntent(R.id.imageView1, serviceIntent(context, R.id.imageView1));

    }

    public static void toggleView(RemoteViews views, int count, int view) {
        // To do it dynamically: http://stackoverflow.com/questions/7948059/dynamic-loading-of-images-r-drawable-using-variable
        views.setImageViewResource(view, getView(count, view));
    }


    public static void setCount(int view, Context context, int value) {
        SharedPreferences p = context.getSharedPreferences("data", Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = p.edit();
        editor.putInt("count" + view, value);
        editor.commit();
    }

    private static PendingIntent serviceIntent(Context context, int view) {
        Intent intent = new Intent();
        intent.setAction(EchoService.ACTION);
        intent.putExtra("view", view);
        // Get the layout for the App Widget and attach an on-click listener to the button
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static int getView(int count, int view) {
        if (view == R.id.imageView1) {
            if (count % 2 == 0) return R.drawable.button_pressed_yellow;
            return R.drawable.button_normal_green;
        }
        if (count % 2 == 0) return R.drawable.button_pressed_red;
        return R.drawable.button_normal_blue;

    }
}
