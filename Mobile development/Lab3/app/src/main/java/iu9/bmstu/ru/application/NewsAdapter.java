package iu9.bmstu.ru.application;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> {

    public List<String> data;

    public NewsAdapter(List<String> data) {
        this.data = data;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public static class NewsHolder extends RecyclerView.ViewHolder{

        private TextView textView;

        public NewsHolder(View itemView) {
            super(itemView);

            textView = (TextView)itemView.findViewById(R.id.textView);
        }
    }

    @Override
    public NewsAdapter.NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        NewsHolder newsHolder = new NewsHolder(view);
        return newsHolder;
    }

    @Override
    public void onBindViewHolder(final NewsAdapter.NewsHolder holder, int position) {
        holder.textView.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
