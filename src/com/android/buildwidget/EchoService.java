package com.android.buildwidget;

import android.app.Service;
import android.content.*;
import android.os.IBinder;
import android.util.Log;

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
        int c = p.getInt("count", -1);
        Log.i(" ---- > on service", "The shared count is: " + c);

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
