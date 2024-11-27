//package com.example.project;
//
//import android.graphics.Typeface;
//import android.os.Bundle;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//import java.util.List;
//
//public class EventsActivity extends AppCompatActivity {
//    private LinearLayout linearLayoutEvents;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_events);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//    });
//        linearLayoutEvents = findViewById(R.id.linearLayoutEvents);
//
//        // Fetch events from the database
//        DatabaseHandler dbHandler = new DatabaseHandler(this);
//        List<Event> eventList = dbHandler.getAllEvents();
//
//        // Dynamically add events to the layout
//        for (Event event : eventList) {
//            addEventCard(event);
//        }
//    }
//
//    private void addEventCard(Event event) {
//        // Create a container for each event
//        LinearLayout eventCard = new LinearLayout(this);
//        eventCard.setOrientation(LinearLayout.VERTICAL);
//        eventCard.setPadding(16, 16, 16, 16);
//        eventCard.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
//        eventCard.setLayoutParams(new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//        ));
//
//        // Title
//        TextView title = new TextView(this);
//        title.setText(event.getTitle());
//        title.setTextSize(18);
//        title.setTextColor(getResources().getColor(android.R.color.black));
//        title.setTypeface(null, Typeface.BOLD);
//
//        // Description
//        TextView description = new TextView(this);
//        description.setText(event.getDescription());
//        description.setTextSize(14);
//        description.setTextColor(getResources().getColor(android.R.color.darker_gray));
//
//        // Timestamp
//        TextView timestamp = new TextView(this);
//        timestamp.setText(event.getTimestamp());
//        timestamp.setTextSize(12);
//        timestamp.setTextColor(getResources().getColor(android.R.color.darker_gray));
//
//        // Add views to the card
//        eventCard.addView(title);
//        eventCard.addView(description);
//        eventCard.addView(timestamp);
//
//        // Add the card to the parent layout
//        linearLayoutEvents.addView(eventCard);
//
//        // Add a margin between cards
//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) eventCard.getLayoutParams();
//        params.setMargins(0, 0, 0, 16);
//        eventCard.setLayoutParams(params);
//    }
//
//}
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

import java.util.ArrayList;
import java.util.List;
public class EventsActivity extends AppCompatActivity {
    DatabaseHandler databaseHandler;
    private RecyclerView recyclerView;
    private EventsAdapter adapter;
    private List<Event> eventList;
    FloatingActionButton addEventFab;

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
