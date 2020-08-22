package ru.pesboroda.bashreader;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.security.cert.TrustAnchor;
import java.util.ArrayList;
import java.util.List;

import ru.pesboroda.bashreader.pojo.LastNewsResponse;
import ru.pesboroda.bashreader.pojo.News;

public class MainActivity extends AppCompatActivity {

    private String lastNews ="https://api.currentsapi.services/v1/latest-news?apiKey=%1$s";
    private String apiKey = "036U5Me7c6mfOn_EoEPvvSZupTa61fN-I4bOuPnJqqUcAInQ";

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private NewsRecyclerViewAdapter newsRecyclerViewAdapter;
    private LinearLayoutManager recyclerViewLinearLayoutManager;

    private ArrayList<News> news;
    private boolean isLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        news = new ArrayList<News>();
        news.add(null);

        swipeRefreshLayout = (SwipeRefreshLayout) this.findViewById(R.id.swipeRefreshLayout);
        recyclerView = (RecyclerView) this.findViewById(R.id.recyclerView);

        initRecyclerView();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                news.clear();
                news.add(null);
                newsRecyclerViewAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void initRecyclerView() {

        this.recyclerView.setHasFixedSize(true);

        this.newsRecyclerViewAdapter = new NewsRecyclerViewAdapter(news);
        this.recyclerView.setAdapter(this.newsRecyclerViewAdapter);
        recyclerViewLinearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        this.recyclerView.setLayoutManager(recyclerViewLinearLayoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (isLoading
                        || recyclerViewLinearLayoutManager == null
                        || recyclerViewLinearLayoutManager.findLastCompletelyVisibleItemPosition()
                        != news.size() - 1)
                    return;

                loadElements();
            }
        });
    }

    private void loadElements() {
        isLoading = true;
        String url = String.format(lastNews, apiKey);

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (news.size() > 0) {
                        int removePosition = news.size() - 1;
                        news.remove(removePosition);
                        newsRecyclerViewAdapter.notifyItemRemoved(removePosition);
                    }

                    int previousIndex = news.size();

                    List<News> loadedNews = new Gson().fromJson(response, LastNewsResponse.class).getNews();
                    loadedNews.add(null);

                    news.addAll(loadedNews);

                    newsRecyclerViewAdapter.notifyItemRangeInserted(previousIndex, news.size());

                    isLoading = false;
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    isLoading = false;
                }
            }
        );

        queue.add(stringRequest);
    }
}

