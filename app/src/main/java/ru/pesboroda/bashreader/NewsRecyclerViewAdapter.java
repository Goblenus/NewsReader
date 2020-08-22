package ru.pesboroda.bashreader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.pesboroda.bashreader.pojo.News;

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

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

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    List<News> news;

    NewsRecyclerViewAdapter(List<News> news){
        this.news = news;
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_layout, viewGroup, false);
            return new NewsViewHolder(v);
        }

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.loading_layout, viewGroup, false);
        return new LoadingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof NewsViewHolder) {
            NewsViewHolder newsViewHolder = (NewsViewHolder) viewHolder;
            newsViewHolder.authorView.setText(news.get(i).getAuthor());
            newsViewHolder.titleView.setText(news.get(i).getTitle());
            newsViewHolder.descriptionView.setText(news.get(i).getDescription());
            newsViewHolder.languageView.setText(news.get(i).getLanguage());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return news.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }
}
