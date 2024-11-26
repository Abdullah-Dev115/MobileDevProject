package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.List;

public class LostItemsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ReportAdapter adapter;
    private DatabaseHandler dbHandler;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_items);
        
        // Initialize database
        dbHandler = new DatabaseHandler(this);
        
        // Initialize views
        recyclerView = findViewById(R.id.lost_recycler_view);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        
        // Set up RecyclerView
        adapter = new ReportAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        
        // Set up bottom navigation
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1); // Lost items is at index 1
        menuItem.setChecked(true);
        
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.events:
                        startActivity(new Intent(LostItemsActivity.this, MainActivity.class));
                        finish();
                        break;
                    case R.id.lost_items:
                        break;
                    case R.id.found_items:
                        startActivity(new Intent(LostItemsActivity.this, FoundItemsActivity.class));
                        finish();
                        break;
                    case R.id.report_item:
                        startActivity(new Intent(LostItemsActivity.this, ReportItemActivity.class));
                        finish();
                        break;
                    case R.id.profile:
                        startActivity(new Intent(LostItemsActivity.this, UserProfileActivity.class));
                        finish();
                        break;
                }
                return true;
            }
        });
        
        loadLostItems();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLostItems();
    }

    private void loadLostItems() {
        List<Report> reports = dbHandler.getAllReports();
        adapter.setReports(reports);
    }
}