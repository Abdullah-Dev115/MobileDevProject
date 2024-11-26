package com.example.project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ReportFoundItemActivity extends AppCompatActivity {
    private EditText locationFoundInput;
    private EditText descriptionInput;
    private EditText contactInfoInput;
    private ImageView imagePreview;
    private Button submitButton;
    private Button selectImageButton;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private DatabaseHandler dbHandler;
    private String originalItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_found_item);

        // Get the original item ID from intent
        originalItemId = getIntent().getStringExtra("item_id");

        dbHandler = new DatabaseHandler(this);

        // Initialize views
        locationFoundInput = findViewById(R.id.found_location_input);
        descriptionInput = findViewById(R.id.found_description_input);
        contactInfoInput = findViewById(R.id.found_contact_input);
        imagePreview = findViewById(R.id.found_image_preview);
        submitButton = findViewById(R.id.submit_found_button);
        selectImageButton = findViewById(R.id.select_found_image_button);

        selectImageButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        submitButton.setOnClickListener(v -> submitFoundReport());
    }

    private void submitFoundReport() {
        String location = locationFoundInput.getText().toString();
        String description = descriptionInput.getText().toString();
        String contactInfo = contactInfoInput.getText().toString();

        if (location.isEmpty() || description.isEmpty() || contactInfo.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create found report and save to database
        Report foundReport = new Report(
                "Found Item Report", // title
                location,
                description,
                contactInfo,
                imageUri != null ? imageUri.toString() : null,
                true // indicates this is a found report
        );

        dbHandler.addReport(foundReport);

        // Move the original report to found items
        dbHandler.markItemAsFound(originalItemId);

        Toast.makeText(this, "Found report submitted successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, FoundItemsActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            if (imageUri != null) {
                // Take persistable URI permission
                final int takeFlags = data.getFlags() & Intent.FLAG_GRANT_READ_URI_PERMISSION;
                getContentResolver().takePersistableUriPermission(imageUri, takeFlags);
                imagePreview.setImageURI(imageUri);
            }
        }
    }
}