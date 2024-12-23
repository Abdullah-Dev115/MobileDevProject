package com.example.project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddReportActivity extends AppCompatActivity {
    private EditText titleInput;
    private EditText descriptionInput;
    private EditText locationInput;
    private ImageView imagePreview;
    private Button submitButton;
    private Button selectImageButton;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private DatabaseHandler dbHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_report);

        dbHandler = new DatabaseHandler(this);


        // Initialize views
        titleInput = findViewById(R.id.report_title_input);
        descriptionInput = findViewById(R.id.report_description_input);
        locationInput = findViewById(R.id.report_location_input);
        imagePreview = findViewById(R.id.report_image_preview);
        submitButton = findViewById(R.id.submit_report_button);
        selectImageButton = findViewById(R.id.select_image_button);

        selectImageButton.setOnClickListener(v -> openImagePicker());
        submitButton.setOnClickListener(v -> submitReport());
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imagePreview.setImageURI(imageUri);
        }
    }

    private void submitReport() {
        String title = titleInput.getText().toString();
        String description = descriptionInput.getText().toString();
        String location = locationInput.getText().toString();
        String imagePath = imageUri != null ? imageUri.toString() : null;

        if (title.isEmpty() || description.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }


        // Create a new Report object with isFound set to false
        Report report = new Report(
                title,
                location,
                description,
                "",  // No contact info for lost items
                imagePath
        );

        // Save to database using the Report object version
        long id = dbHandler.addReport(report);
        
        if (id != -1) {
            Toast.makeText(this, "Report submitted successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error submitting report", Toast.LENGTH_SHORT).show();
        }
    }
}