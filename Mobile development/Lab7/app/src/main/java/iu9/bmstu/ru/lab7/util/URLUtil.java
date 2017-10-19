package iu9.bmstu.ru.lab7.util;

import android.net.Uri;

public class URLUtil {

    public static Uri makeUri(String bitcoin, Integer period) {
        if (period == 2016 || period == 2017)
            return Uri.parse("https://coinmarketcap.northpole.ro/history.json").buildUpon()
                    .appendQueryParameter("coin", bitcoin)
                    .appendQueryParameter("period", String.valueOf(period))
                    .appendQueryParameter("format","array")
                    .build();
        else if (period == 14)
            return Uri.parse("https://coinmarketcap.northpole.ro/history.json").buildUpon()
                    .appendQueryParameter("coin", bitcoin)
                    .appendQueryParameter("period", String.valueOf(period).concat("days"))
                    .appendQueryParameter("format","array")
                    .build();
        else
            return null;
    }
}
