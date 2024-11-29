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

        // Initialize database handler first
        dbHandler = new DatabaseHandler(this);
        
        // Initialize views
        recyclerView = findViewById(R.id.report_recycler_view);
        addReportFab = findViewById(R.id.add_report_fab);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        
        adapter = new ReportAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Set up bottom navigation
        bottomNavigationView.setSelectedItemId(R.id.report_item); // Set current tab as selected
        
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.events) {
                startActivity(new Intent(ReportItemActivity.this, LostItemsActivity.class)); //Event
                finish();
            } else if (itemId == R.id.lost_items) {
                startActivity(new Intent(ReportItemActivity.this, LostItemsActivity.class));
                finish();
            } else if (itemId == R.id.found_items) {
                startActivity(new Intent(ReportItemActivity.this, FoundItemsActivity.class));
                finish();
            } else if (itemId == R.id.report_item) {
                return true; // Do nothing, we're already here
            } else if (itemId == R.id.profile) {
                startActivity(new Intent(ReportItemActivity.this, UserProfileActivity.class));
                finish();
            }
            return true;
        });

        // Set up FAB click listener
        addReportFab.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddReportActivity.class);
            startActivity(intent);
        });

        // Check for permissions and load data if granted
        if (checkPermission()) {
            loadReports();
        } else {
            requestPermission();
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "Storage permission is needed to show images", Toast.LENGTH_SHORT).show();
        }
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadReports();
            } else {
                Toast.makeText(this, "Storage permission is required to display images", Toast.LENGTH_LONG).show();
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
        List<Report> reports = dbHandler.getAllLostReports();
        adapter.setReports(reports);
    }
}