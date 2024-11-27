package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.List;

public class FoundItemsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BottomNavigationView bottomNavigationView;
    private FoundReportAdapter adapter;
    private DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_items);

        // Initialize database
        dbHandler = new DatabaseHandler(this);

        // Initialize views
        recyclerView = findViewById(R.id.found_recycler_view);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        // Set up RecyclerView
        adapter = new FoundReportAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Set up bottom navigation
        bottomNavigationView.setSelectedItemId(R.id.found_items);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.events) {
                startActivity(new Intent(FoundItemsActivity.this, EventsActivity.class)); //Event
                finish();
            } else if (itemId == R.id.lost_items) {
                startActivity(new Intent(FoundItemsActivity.this, LostItemsActivity.class));
                finish();
            } else if (itemId == R.id.found_items) {
                return true; // Do nothing, we're already here
            } else if (itemId == R.id.report_item) {
                startActivity(new Intent(FoundItemsActivity.this, ReportItemActivity.class));
                finish();
            } else if (itemId == R.id.profile) {
                startActivity(new Intent(FoundItemsActivity.this, UserProfileActivity.class));
                finish();
            }
            return true;
        });

        loadFoundItems();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFoundItems();
    }

    private void loadFoundItems() {
        List<Report> reports = dbHandler.getAllReports(true); // true for found items
        adapter.setReports(reports);
    }
}