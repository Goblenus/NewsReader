package ru.pesboroda.bashreader;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.MenuItem;

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
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar navigationActionBar;

    private NewsRecyclerViewAdapter newsRecyclerViewAdapter;
    private LinearLayoutManager recyclerViewLinearLayoutManager;

    private ArrayList<News> news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        news = new ArrayList<News>();

        swipeRefreshLayout = (SwipeRefreshLayout) this.findViewById(R.id.swipeRefreshLayout);
        recyclerView = (RecyclerView) this.findViewById(R.id.recyclerView);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationActionBar = (Toolbar) findViewById(R.id.navigation_action_bar);

        setSupportActionBar(navigationActionBar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.open_drawer, R.string.close_drawer);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initRecyclerView();
        initUpdateListener();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    private void initUpdateListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                news.clear();
                newsRecyclerViewAdapter.notifyDataSetChanged();
                loadElements();
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

        loadElements();
    }

    private void loadElements() {
        swipeRefreshLayout.setRefreshing(true);
        String url = String.format(lastNews, apiKey);

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    int previousIndex = news.size();
                    List<News> loadedNews = new Gson().fromJson(response, LastNewsResponse.class).getNews();
                    news.addAll(loadedNews);
                    newsRecyclerViewAdapter.notifyItemRangeInserted(previousIndex, news.size());

                    swipeRefreshLayout.setRefreshing(false);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        );

        queue.add(stringRequest);
    }
}

