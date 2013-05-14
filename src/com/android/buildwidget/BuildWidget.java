/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.buildwidget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.*;
import android.content.res.Resources;
import android.net.Uri;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Totally based of : http://stackoverflow.com/a/2748723
 */
public class BuildWidget extends AppWidgetProvider {
    public static String YOUR_AWESOME_ACTION = "YourAwesomeAction";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.i("--> on update", "updating");
        Log.i("--> on update", "size: "+ appWidgetIds.length);
        setCount(context, 0);

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int appWidgetId : appWidgetIds) {

            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, BuildWidget.class);
            intent.setAction(YOUR_AWESOME_ACTION);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
            views.setOnClickPendingIntent(R.id.text, pendingIntent);
            // Tell the AppWidgetManager to perform an update on the current App Widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    private void setCount(Context context, int value) {
        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = p.edit();
        editor.putInt("count", value);
        editor.commit();
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(context);
        int count = p.getInt("count", 0) + 1;
        setCount(context, count);

        Log.i("--> message received", intent.getAction());
        if (intent.getAction().equals(YOUR_AWESOME_ACTION)) {
            Toast.makeText(context, "Touched view: " + count, Toast.LENGTH_SHORT).show();
        }
    }
}
