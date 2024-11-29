package com.example.project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
    private Long originalItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_found_item);

        // Get the original item ID from intent
        originalItemId = getIntent().getLongExtra("item_id",-1);

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
        Log.d("ReportFoundItemActivity", "Original Item ID: " + originalItemId);

        String location = locationFoundInput.getText().toString();
        String description = descriptionInput.getText().toString();
        String contactInfo = contactInfoInput.getText().toString();

        if (location.isEmpty() || description.isEmpty() || contactInfo.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // First mark the original item as found


        // Then create the found report with isFound set to true
        Report foundReport = new Report(
            "Found Item Report",
            location,
            description,  // These parameters were in wrong order
            contactInfo,
            imageUri != null ? imageUri.toString() : null
                     );

        long newReportId = dbHandler.addFoundReport(foundReport);
        if (newReportId == -1) {
            Toast.makeText(this, "Error creating found report", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Found report submitted successfully", Toast.LENGTH_SHORT).show();
        dbHandler.deleteReport(originalItemId.toString());
        startActivity(new Intent(this, FoundItemsActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            try {
                imageUri = data.getData();
                if (imageUri != null) {
                    // Take persistable URI permission
                    final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                    getContentResolver().takePersistableUriPermission(imageUri, takeFlags);
                    imagePreview.setImageURI(imageUri);
                }
            } catch (SecurityException e) {
                Log.e("ReportFoundItemActivity", "Security Exception: " + e.getMessage());
                Toast.makeText(this, "Unable to access image", Toast.LENGTH_SHORT).show();
            }
        }
    }
}