package com.josealex.granadacontributions;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.josealex.granadacontributions.modules.User;

public class PreferencesActivity extends AppCompatActivity {

    private User loggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();

        if(b != null) {
            loggedUser = (User) b.getSerializable(MainActivity.USER_BUNDLE_ID);
        }

        setContentView(R.layout.activity_preferences);

    }
}