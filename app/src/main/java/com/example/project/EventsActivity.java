package com.example.project;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    private RecyclerView recyclerView;
    private EventsAdapter adapter;
    private List<Event> eventList;
    FloatingActionButton addEventFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);


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
        eventList.add(new Event("Meeting", "Discuss project progress", "2024-11-25 10:00 AM"));
        eventList.add(new Event("Workshop", "Learn Android Development", "2024-11-26 02:00 PM"));
        eventList.add(new Event("Conference", "Tech Innovations 2024", "2024-11-27 09:00 AM"));

        // Notify the adapter of data changes
        adapter.notifyDataSetChanged();
    }
}
