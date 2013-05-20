package com.android.buildwidget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.*;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import static com.android.buildwidget.WidgetHelper.*;

public class EchoService extends Service {


    public static final String ACTION = "SendAnEchoToEchoService";
    private BroadcastReceiver yourReceiver;
    private int echoCount = 0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(" ---> started", "created");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(" ---> service created", "created");

        final IntentFilter theFilter = new IntentFilter();
        theFilter.addAction(ACTION);
        this.yourReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                doReceive(context, intent);
            }
        };
        // Registers the receiver so that your service will listen for
        // broadcasts
        this.registerReceiver(this.yourReceiver, theFilter);
    }

    protected void doReceive(Context context, Intent intent) {
        // Do whatever you need it to do when it receives the broadcast
        // Example show a Toast message...
        echoCount++;
        Log.i(" ---- > on service", "got a broadcast the following times:" + echoCount);
        SharedPreferences p = context.getSharedPreferences("data", Context.MODE_MULTI_PROCESS);
        int c = p.getInt("count", 0);
        Log.i(" ---- > on service", "The shared count is: " + c);
        Log.i("--> message received", "Executing action");
        setCount(context, c + 1);
        //
//            // Get the layout for the App Widget and attach an on-click listener to the button
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        setClickEvents(context, views);
        // Tell the AppWidgetManager to perform an update on the current App Widget
        toggleView(views, c);
        ComponentName thisWidget = new ComponentName(context, BuildWidget.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(thisWidget, views);

    }

    private void setCount(Context context, int value) {
        SharedPreferences p = context.getSharedPreferences("data", Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = p.edit();
        editor.putInt("count", value);
        editor.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Do not forget to unregister the receiver!!!
        Log.e("==>>", "destroyed");
        this.unregisterReceiver(this.yourReceiver);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
