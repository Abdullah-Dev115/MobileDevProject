package com.example.project;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Calendar;

public class AddEvent extends AppCompatActivity {

    private EditText eventTitleInput, eventDescriptionInput;
    private Button selectDateButton, selectTimeButton, submitEventButton;
    private TextView eventTimestampDisplay;
    private String selectedDate = "", selectedTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        // Initialize views
        eventTitleInput = findViewById(R.id.event_title_input);
        eventDescriptionInput = findViewById(R.id.event_description_input);
        selectDateButton = findViewById(R.id.select_date_button);
        selectTimeButton = findViewById(R.id.select_time_button);
        submitEventButton = findViewById(R.id.submit_event_button);
        eventTimestampDisplay = findViewById(R.id.event_timestamp_display);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Set listeners for buttons
        selectDateButton.setOnClickListener(v -> showDatePicker());
        selectTimeButton.setOnClickListener(v -> showTimePicker());
        submitEventButton.setOnClickListener(v -> submitEvent());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_OK);  // Notify that an event was added
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);  // Notify the previous activity to refresh
        super.onBackPressed();
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, yearSelected, monthOfYear, dayOfMonth) -> {
            selectedDate = String.format("%02d-%02d-%04d", dayOfMonth, monthOfYear + 1, yearSelected);
            eventTimestampDisplay.setText("Selected Timestamp: " + selectedDate + " " + selectedTime);
        }, year, month, day);

        datePickerDialog.show();
    }

    private void showTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minuteOfHour) -> {
            selectedTime = String.format("%02d:%02d", hourOfDay, minuteOfHour);
            eventTimestampDisplay.setText("Selected Timestamp: " + selectedDate + " " + selectedTime);
        }, hour, minute, true);

        timePickerDialog.show();
    }

    private void submitEvent() {
        String title = eventTitleInput.getText().toString().trim();
        String description = eventDescriptionInput.getText().toString().trim();

        if (title.isEmpty() || description.isEmpty() || selectedDate.isEmpty() || selectedTime.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String timestamp = selectedDate + " " + selectedTime;
        Event newEvent = new Event(title, description, timestamp);
        DatabaseHandler dbHandler = new DatabaseHandler(this);
        dbHandler.addEvent(newEvent);

        Toast.makeText(this, "Event added successfully!", Toast.LENGTH_SHORT).show();

        setResult(RESULT_OK);  // Notify the previous activity to refresh
        finish();
    }
}
