package ru.pesboroda.bashreader;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import ru.pesboroda.bashreader.pojo.LastNewsResponse;
import ru.pesboroda.bashreader.pojo.News;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final static String lastNews ="https://api.currentsapi.services/v1/latest-news?apiKey=%1$s&language=%2$s";
    private final static String apiKey = "036U5Me7c6mfOn_EoEPvvSZupTa61fN-I4bOuPnJqqUcAInQ";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar navigationActionBar;
    private NavigationView navigationView;

    private NewsRecyclerViewAdapter newsRecyclerViewAdapter;
    private LinearLayoutManager recyclerViewLinearLayoutManager;

    private ArrayList<News> news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = this.getSharedPreferences(getString(R.string.settings_file_name),
                MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();

        news = new ArrayList<News>();

        swipeRefreshLayout = (SwipeRefreshLayout) this.findViewById(R.id.swipeRefreshLayout);
        recyclerView = (RecyclerView) this.findViewById(R.id.recyclerView);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationActionBar = (Toolbar) findViewById(R.id.navigation_action_bar);
        navigationView = (NavigationView) findViewById(R.id.navigationView);

        String settingsLanguage = sharedPreferences.getString(getString(R.string.settings_language_code_id),
                getString(R.string.settings_language_code_default));
        sharedPreferencesEditor.putString(getString(R.string.settings_language_code_id),
                settingsLanguage);
        sharedPreferencesEditor.apply();

        initToolbar();
        initRecyclerView();
        initUpdateListener();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }

        // TODO:
        // Close NavBar
        return true;
    }

    private void initToolbar() {
        setSupportActionBar(navigationActionBar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.open_drawer, R.string.close_drawer);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initUpdateListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadLastNews();
            }
        });
    }

    private void initRecyclerView() {
        this.recyclerView.setHasFixedSize(true);

        this.newsRecyclerViewAdapter = new NewsRecyclerViewAdapter(news, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemPosition = recyclerView.getChildLayoutPosition(view);
                // TODO:
                // open webview
                Toast.makeText(MainActivity.this, "Id: " + itemPosition, Toast.LENGTH_SHORT).show();
            }
        });
        this.recyclerView.setAdapter(this.newsRecyclerViewAdapter);
        recyclerViewLinearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        this.recyclerView.setLayoutManager(recyclerViewLinearLayoutManager);

        loadLastNews();
    }

    private void loadLastNews() {
        String settingsLanguage = sharedPreferences.getString(
                getString(R.string.settings_language_code_id),
                getString(R.string.settings_language_code_default));

        swipeRefreshLayout.setRefreshing(true);
        String url = String.format(lastNews, apiKey, settingsLanguage);

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    news.clear();
                    List<News> loadedNews = new Gson().fromJson(response, LastNewsResponse.class).getNews();
                    news.addAll(loadedNews);
                    newsRecyclerViewAdapter.notifyDataSetChanged();

                    swipeRefreshLayout.setRefreshing(false);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(MainActivity.this,
                            "Request error. Please ty later.", Toast.LENGTH_SHORT).show();
                }
            }
        );

        queue.add(stringRequest);
    }
}

