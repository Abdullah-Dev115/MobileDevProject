package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class EventsActivity extends AppCompatActivity {
    DatabaseHandler databaseHandler;
    private RecyclerView recyclerView;
    private EventsAdapter adapter;
    private List<Event> eventList;
    FloatingActionButton addEventFab;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        addEventFab=findViewById(R.id.add_event_fb);
        databaseHandler = new DatabaseHandler(this);
        int loggedInUserId = getLoggedInUserId(this);  // Get the logged-in user's ID

        if (loggedInUserId != -1) {
            // Now you can check if the logged-in user is an admin
            if (databaseHandler.isAdmin(loggedInUserId)) {
                // Show the FAB for admin users
                addEventFab.setVisibility(View.VISIBLE);
            } else {
                // Hide the FAB for non-admin users
                addEventFab.setVisibility(View.GONE);
            }
        } else {
            // No user is logged in, handle accordingly (e.g., show login screen)
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
        }

        recyclerView = findViewById(R.id.recyclerView_events);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addEventFab.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEvent.class);
            startActivityForResult(intent,1);
        });

        // Initialize event list and adapter
        eventList = new ArrayList<>();
        adapter = new EventsAdapter(eventList);
        recyclerView.setAdapter(adapter);

        // Load events (add dummy data or fetch from DB)
        loadEvents();

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setSelectedItemId(R.id.events);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.events) {
                return true; // Already on events page
            } else if (itemId == R.id.lost_items) {
                startActivity(new Intent(EventsActivity.this, LostItemsActivity.class));
                finish();
            } else if (itemId == R.id.found_items) {
                startActivity(new Intent(EventsActivity.this, FoundItemsActivity.class));
                finish();
            } else if (itemId == R.id.report_item) {
                startActivity(new Intent(EventsActivity.this, ReportItemActivity.class));
                finish();
            } else if (itemId == R.id.profile) {
                startActivity(new Intent(EventsActivity.this, UserProfileActivity.class));
                finish();
            }
            return true;
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Refresh the event list
            loadEvents();  // Call the method to reload events from the database
        }
    }

    private void loadEvents() {
        // Dummy data for testing
        List<Event> events = databaseHandler.getAllEvents();

        // Clear the list before adding new events
        eventList.clear();

        // Add the fetched events to the eventList
        if (events != null && !events.isEmpty()) {
            eventList.addAll(events);
            // Notify the adapter of data changes
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "No events available", Toast.LENGTH_SHORT).show();
        }
    }

    public int getLoggedInUserId(@NonNull Context context) {
        SharedPreferences loginPreferences = context.getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        return loginPreferences.getInt("userId", -1);  // Return -1 if no userId is stored
    }
}
