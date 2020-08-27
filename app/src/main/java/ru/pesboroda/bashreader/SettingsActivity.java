package ru.pesboroda.bashreader;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class SettingsActivity extends AppCompatActivity {
    private final static String languagesUrl = "https://api.currentsapi.services/v1/available/languages";

    private String[][] languages;
    private String[] languagesNames;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;

    AlertDialog dialog;

    private TextView chooseLanguageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        chooseLanguageView = findViewById(R.id.chooseLanguageView);

        sharedPreferences = this.getSharedPreferences(getString(R.string.settings_file_name),
                MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();

        initLanguages();
        initDialog();

        chooseLanguageView.setText(getLanguageName());
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
}