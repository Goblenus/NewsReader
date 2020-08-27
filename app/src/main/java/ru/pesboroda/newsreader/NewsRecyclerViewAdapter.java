package ru.pesboroda.newsreader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.pesboroda.newsreader.pojo.News;

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private View.OnClickListener onClickListener;

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView authorView;
        TextView titleView;
        TextView descriptionView;
        TextView languageView;

        NewsViewHolder(@NonNull View itemView) {
            super(itemView);

            authorView = (TextView) itemView.findViewById(R.id.authorView);
            titleView = (TextView) itemView.findViewById(R.id.titleView);
            descriptionView = (TextView) itemView.findViewById(R.id.descriptionView);
            languageView = (TextView) itemView.findViewById(R.id.languageView);
        }
    }

    List<News> news;

    NewsRecyclerViewAdapter(List<News> news, View.OnClickListener onClickListener){
        this.news = news;
        this.onClickListener = onClickListener;
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_layout, viewGroup, false);
        v.setOnClickListener(onClickListener);
        return new NewsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        NewsViewHolder newsViewHolder = (NewsViewHolder) viewHolder;
        newsViewHolder.authorView.setText(news.get(i).getAuthor());
        newsViewHolder.titleView.setText(news.get(i).getTitle());
        newsViewHolder.descriptionView.setText(news.get(i).getDescription());
        newsViewHolder.languageView.setText(news.get(i).getLanguage());
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
