package com.example.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.example.project.R;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private static final String PREF_NAME = "LoginPrefs";
    private SharedPreferences loginPreferences;
    private BottomNavigationView bottomNavigationView;

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
        
        setContentView(R.layout.activity_main);
        
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.events) {
                startActivity(new Intent(MainActivity.this, EventsActivity.class)); //EventsActivity
            } else if (itemId == R.id.lost_items) {
                startActivity(new Intent(MainActivity.this, LostItemsActivity.class));
            } else if (itemId == R.id.found_items) {
                startActivity(new Intent(MainActivity.this, FoundItemsActivity.class));
            } else if (itemId == R.id.report_item) {
                startActivity(new Intent(MainActivity.this, ReportItemActivity.class));
            } else if (itemId == R.id.profile) {
                startActivity(new Intent(MainActivity.this, UserProfileActivity.class));
            }
            return true;
        });
    }

    public void logout() {
        SharedPreferences.Editor editor = loginPreferences.edit();
        editor.clear();
        editor.apply();
        startActivity(new Intent(MainActivity.this, Login.class));
        finish();
    }
}