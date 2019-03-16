package iu9.bmstu.ru.lab11.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NotificationCompat;

import iu9.bmstu.ru.lab11.R;

public class MyBroadCastReceiver extends BroadcastReceiver {

    private static final String TAG = MyBroadCastReceiver.class.getName();
    private static final int mId = 1;

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null && intent.hasExtra("message")) {
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context, context.getString(R.string.notification_id_channel))
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setContentTitle("Получено сообщение")
                            .setContentText(intent.getStringExtra("message"));
            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(mId, mBuilder.build());
        }
        else if (!isNetworkAvailable(context)) {
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context, context.getString(R.string.notification_id_channel))
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setContentTitle("Псс")
                            .setContentText("Я вижу, что кто-то выключил интернет...");
            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(mId, mBuilder.build());
        }
    }

}
