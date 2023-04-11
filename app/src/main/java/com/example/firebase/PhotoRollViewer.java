package com.example.firebase;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class PhotoRollViewer extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_IMAGE = 101;
    private ImageView imageView;
    private ActivityResultLauncher<Intent> pickImageLauncher;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_roll_viewer);

        // Initialize Firebase Storage
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        imageView = findViewById(R.id.imageView);
        Button button = findViewById(R.id.button);
        TextView textView = findViewById(R.id.textView2);

        //get note text from intent
        String text = getIntent().getStringExtra("text");
        textView.setText(text);

        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imageUri = result.getData().getData();
                        imageView.setImageURI(imageUri);
                    }
                });

        button.setOnClickListener(view -> {
            // Launch the image picker
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickImageLauncher.launch(intent);
        });

    }

    public void returnPressed(View view) {
        finish();
        //super.onBackPressed();
    }

    public void savePressed(View view) {
        if (imageUri != null) {
            // get the filename of the image from the uri
            String filename = imageUri.getLastPathSegment();

            // create a reference to the file in Firebase storage
            //saves the image to "images" folder in the storage, otherwise creates folder
            StorageReference imageRef = storageRef.child("images/" + UUID.randomUUID().toString());

            //note to self, remember to adjust storage rules timestamp if im not allowed to uploaded

            // upload the file to Firebase storage
            imageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // file uploaded successfully

                        //upload filename when img is uploaded
                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            //String downloadUrl = uri.toString();
                            String fileName = imageRef.getName(); // get the file name
                            // get the note id from the intent
                            String documentId = getIntent().getStringExtra("id");

                            FirebaseService fs = new FirebaseService();
                            fs.editImageNote(documentId,fileName);
                        });

                        Toast.makeText(PhotoRollViewer.this, "Image uploaded to Firebase storage", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        // an error occurred while uploading the file
                        Toast.makeText(PhotoRollViewer.this, "Error uploading image to Firebase storage", Toast.LENGTH_SHORT).show();
                        //e.printStackTrace();
                    });
        }
    }

}