package iu9.bmstu.ru.lab7.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import iu9.bmstu.ru.lab7.R;
import iu9.bmstu.ru.lab7.activity.InfoActivity;
import iu9.bmstu.ru.lab7.model.Coin;

public class CoinAdapter extends RecyclerView.Adapter<CoinAdapter.CoinHolder> {

    private List<Coin> data;

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
        }
    }

    @Override
    public CoinAdapter.CoinHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coin_item, parent, false);
        return new CoinHolder(view);
    }

    @Override
    public void onBindViewHolder(final CoinAdapter.CoinHolder holder, final int position) {
        Locale locale = Locale.getDefault();
        if (Coin.getCurrency().equals("RUB"))
            locale = new Locale("ru","RU");
        else if (Coin.getCurrency().equals("EUR"))
            locale = Locale.FRANCE;
        else if (Coin.getCurrency().equals("USD"))
            locale = Locale.US;

        Date time = data.get(position).getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm", locale);
        int index = 0;
        String valueResultStr = "";

        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

        holder.dateTextView.setText(dateFormat.format(time));
        holder.valueTextView.setText(numberFormat.format(data.get(position).getLow()));

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(v.getContext(), InfoActivity.class);
                intent.putExtra("open", data.get(position).getOpen());
                intent.putExtra("close", data.get(position).getClose());
                intent.putExtra("date", data.get(position).getTime().getTime());
                v.getContext().startActivity(intent);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}

