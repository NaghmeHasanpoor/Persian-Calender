package com.arpitas.persiancalender.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.arpitas.persiancalender.util.UpdateUtils;
import com.arpitas.persiancalender.util.Utils;

import java.io.IOException;

public class ApplicationService extends Service {
    private BroadcastReceivers broadcastReceivers;

    @Override
    public IBinder onBind(Intent paramIntent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(ApplicationService.class.getName() , "start");
        UpdateUtils updateUtils = UpdateUtils.getInstance(getApplicationContext());
        broadcastReceivers=new BroadcastReceivers();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_DATE_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
//        registerReceiver(broadcastReceivers, intentFilter, );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(broadcastReceivers, intentFilter, RECEIVER_EXPORTED);
        }else{
            registerReceiver(broadcastReceivers, intentFilter);
        }

        Log.i("tag","register the broadcast");

        Utils utils = Utils.getInstance(getBaseContext());
        utils.loadApp();
        try {
            updateUtils.update(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Service.START_STICKY;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        try {
            unregisterReceiver(broadcastReceivers);
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.i("tag","unregister the broadcast");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (broadcastReceivers != null)
                unregisterReceiver(broadcastReceivers);
        }catch (IllegalArgumentException  e){
            e.printStackTrace();
        }
        Log.i("tag","unregister the broadcast");
    }
}
