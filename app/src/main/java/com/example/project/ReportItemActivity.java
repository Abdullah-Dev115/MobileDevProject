package com.example.project;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ReportItemActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FloatingActionButton addReportFab;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_item);

        // Initialize views
        recyclerView = findViewById(R.id.report_recycler_view);
        addReportFab = findViewById(R.id.add_report_fab);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // TODO: Add your ReportAdapter here

        // Set up FAB click listener
        addReportFab.setOnClickListener(v -> {
            startActivity(new Intent(ReportItemActivity.this, AddReportActivity.class));
        });

        // Set up bottom navigation
        bottomNavigationView.setSelectedItemId(R.id.report_item);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.events) {
                startActivity(new Intent(this, EventsActivity.class));
            } else if (itemId == R.id.lost_items) {
                startActivity(new Intent(this, LostItemsActivity.class));
            } else if (itemId == R.id.found_items) {
                startActivity(new Intent(this, FoundItemsActivity.class));
            } else if (itemId == R.id.profile) {
                startActivity(new Intent(this, UserProfileActivity.class));
            }
            finish();
            return true;
        });
    }


    private void showAddReportDialog() {
        // TODO: Implement dialog to add new report
    }
}