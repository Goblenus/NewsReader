package ru.pesboroda.newsreader;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;


public class SettingsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private final static String languagesUrl = "https://api.currentsapi.services/v1/available/languages";

    private String[][] languages;
    private String[] languagesNames;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;

    AlertDialog dialog;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar navigationActionBar;
    private NavigationView navigationView;
    private TextView chooseLanguageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        chooseLanguageView = findViewById(R.id.chooseLanguageView);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationActionBar = (Toolbar) findViewById(R.id.navigation_action_bar);
        navigationView = (NavigationView) findViewById(R.id.navigationView);

        sharedPreferences = this.getSharedPreferences(getString(R.string.settings_file_name),
                MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();

        initLanguages();
        initDialog();
        initToolbar();

        chooseLanguageView.setText(getLanguageName());
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

    private String getLanguageName() {
        String languageCode = sharedPreferences.getString(
                getString(R.string.settings_language_code_id),
                getString(R.string.settings_language_code_default));

        String languageName = "";

        for (String[] language : languages) {
            if (!language[1].equals(languageCode))
                continue;

            languageName = language[0];
            break;
        }

        return languageName;
    }

    private void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.choose_language));

        builder.setItems(languagesNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sharedPreferencesEditor.putString(getString(R.string.settings_language_code_id),
                        languages[which][1]);
                sharedPreferencesEditor.apply();
                chooseLanguageView.setText(languages[which][0]);
            }
        });

        dialog = builder.create();
    }

    private void initLanguages() {
        Resources res = getResources();
        TypedArray ta = res.obtainTypedArray(R.array.languages);
        int n = ta.length();
        languages = new String[n][];
        languagesNames = new String[n];
        for (int i = 0; i < n; ++i) {
            int id = ta.getResourceId(i, 0);
            if (id > 0) {
                languages[i] = res.getStringArray(id);
                languagesNames[i] = languages[i][0];
            } else {
                // something wrong with the XML
            }
        }
        ta.recycle();
    }

    public void chooseLanguage(View view) {
        dialog.show();
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
            case R.id.nav_last_news:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }

        super.onBackPressed();
    }
}