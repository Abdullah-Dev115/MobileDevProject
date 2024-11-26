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