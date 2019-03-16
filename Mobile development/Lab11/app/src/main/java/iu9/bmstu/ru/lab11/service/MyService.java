package iu9.bmstu.ru.lab11.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import org.jetbrains.annotations.Nullable;

import iu9.bmstu.ru.lab11.receiver.MyBroadCastReceiver;

public class MyService extends IntentService {

    private static final String TAG = MyService.class.getName();

    private MyBroadCastReceiver broadCastReceiver;


    public MyService() {
        super(MyService.class.getName());
    }

    public MyService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG,"Начало обработки Intent");
        if (intent != null && intent.hasExtra("message"))
            Log.d(TAG, intent.getStringExtra("message"));
        else
            Log.d(TAG,"Сообщения нет");
        try {
            Log.d(TAG,"Сервис еще работает");
            Thread.sleep(5000);
        }
        catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        Intent intentReceiver = new Intent();
        intentReceiver.setAction("iu9.bmstu.ru.lab11.receiver.my_action");
        intentReceiver.putExtra("message","Привет из будущего");
        sendBroadcast(intentReceiver);
        Log.d(TAG,"Закончили обработку intent");
    }

    @Override
    public void onCreate() {
        Log.d(TAG,"onCreate()");
        broadCastReceiver = new MyBroadCastReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction("iu9.bmstu.ru.lab11.receiver.my_action");
        registerReceiver(broadCastReceiver,filter);
        super.onCreate();
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Toast.makeText(this, "Сервис начал работать", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"onDestroy()");
        unregisterReceiver(broadCastReceiver);
        super.onDestroy();
    }
}
