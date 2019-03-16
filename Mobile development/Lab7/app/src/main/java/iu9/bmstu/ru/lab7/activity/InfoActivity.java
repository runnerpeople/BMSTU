package iu9.bmstu.ru.lab7.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import iu9.bmstu.ru.lab7.R;
import iu9.bmstu.ru.lab7.model.Coin;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        TextView dateTextView  = findViewById(R.id.dateValue);
        TextView openTextView  = findViewById(R.id.openValue);
        TextView closeTextView = findViewById(R.id.closeValue);

        Locale locale = Locale.getDefault();
        if (Coin.getCurrency().equals("RUB"))
            locale = new Locale("ru","RU");
        else if (Coin.getCurrency().equals("EUR"))
            locale = Locale.FRANCE;
        else if (Coin.getCurrency().equals("USD"))
            locale = Locale.US;

        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

        dateTextView.setText(new SimpleDateFormat("EEE, dd MMM yyyy HH:mm", locale).format(new Date(getIntent().getLongExtra("date",0))));
        openTextView.setText(numberFormat.format(getIntent().getDoubleExtra("open",0)));
        closeTextView.setText(numberFormat.format(getIntent().getDoubleExtra("close",0)));
    }
}
