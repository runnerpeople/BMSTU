package iu9.bmstu.ru.lab7.adapter;

import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Currency;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import iu9.bmstu.ru.lab7.R;
import iu9.bmstu.ru.lab7.activity.MainActivity;
import iu9.bmstu.ru.lab7.model.Coin;

public class CoinAdapter extends RecyclerView.Adapter<CoinAdapter.CoinHolder> {

    private List<Coin> data;
    public static Typeface font;

    public CoinAdapter(List<Coin> data) {
        this.data = data;
    }

    public List<Coin> getData() {
        return data;
    }

    public void setData(List<Coin> data) {
        this.data = data;
    }

    public static class CoinHolder extends RecyclerView.ViewHolder{

        private TextView dateTextView;
        private TextView valueTextView;

        public CoinHolder(View itemView) {
            super(itemView);

            dateTextView = (TextView)itemView.findViewById(R.id.dateTextView);
            valueTextView = (TextView)itemView.findViewById(R.id.valueTextView);

            dateTextView.setTypeface(font);
            valueTextView.setTypeface(font);
        }
    }

    @Override
    public CoinAdapter.CoinHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coin_item, parent, false);
        return new CoinHolder(view);
    }

    @Override
    public void onBindViewHolder(final CoinAdapter.CoinHolder holder, int position) {
        Date date = data.get(position).getDate();
        Double value = data.get(position).getValue();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        numberFormat.setCurrency(Coin.getValue_symbol());
        holder.dateTextView.setText(dateFormat.format(date));
        holder.valueTextView.setText(numberFormat.format(value));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}

