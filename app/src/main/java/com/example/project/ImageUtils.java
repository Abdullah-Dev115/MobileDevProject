package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;

public class ImageUtils {
    public static void loadImage(Context context, String imageUriString, ImageView imageView) {
        if (imageUriString != null) {
            try {
                Uri imageUri = Uri.parse(imageUriString);
                context.getContentResolver().takePersistableUriPermission(
                        imageUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                );
                imageView.setImageURI(imageUri);
            } catch (SecurityException e) {
                // Handle error - maybe set a default image
                imageView.setImageResource(R.drawable.ic_launcher_background);
            }
        } else {
            imageView.setImageResource(R.drawable.ic_launcher_background);
        }
    }
}