package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
        bottomNavigationView.setSelectedItemId(R.id.lost_items);
        
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.events) {
                startActivity(new Intent(LostItemsActivity.this, EventsActivity.class));
                finish();
            } else if (itemId == R.id.lost_items) {
                return true; // Do nothing
            } else if (itemId == R.id.found_items) {
                startActivity(new Intent(LostItemsActivity.this, FoundItemsActivity.class));
                finish();
            } else if (itemId == R.id.report_item) {
                startActivity(new Intent(LostItemsActivity.this, ReportItemActivity.class));
                finish();
            } else if (itemId == R.id.profile) {
                startActivity(new Intent(LostItemsActivity.this, UserProfileActivity.class));
                finish();
            }
            return true;
        });
        
        loadLostItems();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLostItems();
    }

    private void loadLostItems() {
        Log.d("LostItemsActivity", "Loading lost items");
        List<Report> reports = dbHandler.getAllLostReports();  // false for unfound items
        Log.d("LostItemsActivity", "Loaded " + reports.size() + " lost items");
        adapter.setReports(reports);
    }
}