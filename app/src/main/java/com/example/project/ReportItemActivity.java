package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ReportItemActivity extends AppCompatActivity {
    private static final int STORAGE_PERMISSION_CODE = 1;
    private RecyclerView recyclerView;
    private FloatingActionButton addReportFab;
    private BottomNavigationView bottomNavigationView;
    private ReportAdapter adapter;
    private DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_item);

        // Check for permissions first
        if (checkPermission()) {
            initializeViews();
        } else {
            requestPermission();
        }
    }

    private void initializeViews() {
        // Initialize database handler
        dbHandler = new DatabaseHandler(this);

        // Initialize views
        recyclerView = findViewById(R.id.report_recycler_view);
        addReportFab = findViewById(R.id.add_report_fab);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        // Set up RecyclerView
        adapter = new ReportAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Load reports
        loadReports();

        // Set up FAB click listener
        addReportFab.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddReportActivity.class);
            startActivity(intent);
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

    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) 
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeViews();
            } else {
                Toast.makeText(this, "Storage permission is required to display images", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            loadReports();
        }
    }

    private void loadReports() {
        List<Report> reports = dbHandler.getAllReports();
        adapter.setReports(reports);
    }
}