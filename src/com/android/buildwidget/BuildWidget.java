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
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Define a simple widget that shows the Wiktionary "Word of the day." To build
 * an update we spawn a background {@link Service} to perform the API queries.
 */
public class BuildWidget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
            int[] appWidgetIds) {
        // To prevent any ANR timeouts, we perform the update in a service
        context.startService(new Intent(context, UpdateService.class));
    }

    public static class UpdateService extends Service {
        private static final String ACTION = "com.android.samples.updateWidget";
        private int callledCount = 0;
        private BroadcastReceiver yourReceiver;


        private void setupService() {
            final UpdateService self = this;
            log("setting up service");

            super.onCreate();
            final IntentFilter theFilter = new IntentFilter();
            theFilter.addAction(ACTION);
            this.yourReceiver = new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {
                    log("received intent");
                    // Do whatever you need it to do when it receives the broadcast
                    // Example show a Toast message...
                    RemoteViews updateViews = new RemoteViews(
                            context.getPackageName(), R.layout.widget);
                    updateViews.setTextViewText(R.id.text, "update count" + callledCount);
                    // Push update for this widget to the home screen
                    ComponentName thisWidget = new ComponentName(self, BuildWidget.class);
                    AppWidgetManager manager = AppWidgetManager.getInstance(self);
                    manager.updateAppWidget(thisWidget, updateViews);
                }


            };
            // Registers the receiver so that your service will listen for
            // broadcasts
            this.registerReceiver(this.yourReceiver, theFilter);

        }

        private void log(String s) {
            Log.i("--> APP", s);
        }

        @Override
        public void onDestroy() {
            log("destroying");
            super.onDestroy();
            // Do not forget to unregister the receiver!!!
            this.unregisterReceiver(this.yourReceiver);
        }


        @Override
        public void onStart(Intent intent, int startId) {
            log("on start");
            setupService();
            // Build the widget update
            RemoteViews updateViews = buildUpdate(this);

            // Push update for this widget to the home screen
            ComponentName thisWidget = new ComponentName(this, BuildWidget.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(this);
            manager.updateAppWidget(thisWidget, updateViews);
        }

        public RemoteViews buildUpdate(Context context) {
            // Pick out month names from resources
            Resources res = context.getResources();
            RemoteViews updateViews = new RemoteViews(
                context.getPackageName(), R.layout.widget);

            PendingIntent pendingIntent = PendingIntent.getActivity(context,
                    0 /* no requestCode */, 
                    new Intent(ACTION),
                    0 /* no flags */);
            updateViews.setOnClickPendingIntent(R.id.text, pendingIntent);
            return updateViews;
        }

        @Override
        public IBinder onBind(Intent intent) {
            // We don't need to bind to this service
            return null;
        }
    }
}
