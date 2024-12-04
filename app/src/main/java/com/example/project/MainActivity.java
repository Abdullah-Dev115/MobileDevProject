package com.example.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;



public class MainActivity extends AppCompatActivity {
    private static final String PREF_NAME = "LoginPrefs";
    private SharedPreferences loginPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        loginPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        boolean isLoggedIn = loginPreferences.getBoolean("isLoggedIn", false);
        
        if (!isLoggedIn) {
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
            return;
        }
        
        startActivity(new Intent(MainActivity.this, EventsActivity.class));
        finish();
    }

    public void logout() {
        SharedPreferences.Editor editor = loginPreferences.edit();
        editor.clear();
        editor.apply();
        startActivity(new Intent(MainActivity.this, Login.class));
        finish();
    }
}