package iu9.bmstu.ru.lab11;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import iu9.bmstu.ru.lab11.receiver.MyBroadCastReceiver;
import iu9.bmstu.ru.lab11.service.MyService;

public class MainActivity extends AppCompatActivity {

    private MyBroadCastReceiver broadCastReceiver;
    private Intent service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        service = new Intent(this, MyService.class);
        service.putExtra("message","Привет из будущего");
        startService(service);

        broadCastReceiver = new MyBroadCastReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction("iu9.bmstu.ru.lab11.receiver.my_action");
        registerReceiver(broadCastReceiver,filter);
    }

    void OnClick(View view) {
        Intent intentReceiver = new Intent();
        intentReceiver.setAction("iu9.bmstu.ru.lab11.receiver.my_action");
        intentReceiver.putExtra("message","Привет из будущего");
        sendBroadcast(intentReceiver);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(broadCastReceiver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        stopService(service);
        super.onDestroy();
    }
}
