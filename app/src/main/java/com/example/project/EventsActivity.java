package com.example.project;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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
        databaseHandler = new DatabaseHandler(this);


        recyclerView = findViewById(R.id.recyclerView_events);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        addEventFab=findViewById(R.id.add_event_fb);
        addEventFab.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEvent.class);
            startActivity(intent);
        });

        // Initialize event list and adapter
        eventList = new ArrayList<>();
        adapter = new EventsAdapter(eventList);
        recyclerView.setAdapter(adapter);

        // Load events (add dummy data or fetch from DB)
        loadEvents();

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

    }