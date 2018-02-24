package dk.sdu.mmmi.nakosa;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class NewAdActivity extends AppCompatActivity {

    private final int CAMERA_CODE = 1;
    private String picturePath;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private View progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ad);

        progress = findViewById(R.id.progress_overlay);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Advertisements");
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    public void useCamera(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_CODE);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_CODE && resultCode == RESULT_OK) {
            Log.d("Photo", "Photo taken - " + picturePath);
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            ImageView image = findViewById(R.id.imageFromCamera);
            image.setImageBitmap(bitmap);
            image.setVisibility(View.VISIBLE);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        picturePath = image.getAbsolutePath();
        return image;
    }

    public void createNewAd(View view) {
        if (validateFields()) {
            progress.setVisibility(View.VISIBLE);
            uploadImageToStorageAndSaveInDB(getFieldsValue());
        } else {
            Toast.makeText(this, "Produkt navn og pris skal udfyldes!", Toast.LENGTH_SHORT).show();
        }
    }

    public void uploadImageToStorageAndSaveInDB(final HashMap<String, String> dbEntry) {
        File image = new File(picturePath);
        Uri file = Uri.fromFile(image);
        final String imageName = image.getName();
        StorageReference imageRef = storageReference.child("images/" + imageName);

        imageRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        //dbEntry.put("ImagePath", downloadUrl.getPath());
                        dbEntry.put("ImagePath", imageName);
                        saveInDB(dbEntry);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        uploadError();
                    }
                });

    }

    private void saveInDB(HashMap<String, String> dbEntry) {
        databaseReference.push().setValue(dbEntry);
        finish();
    }

    private void uploadError() {
        progress.setVisibility(View.GONE);
        Toast.makeText(this, "Der skete en fejl, prøv igen.", Toast.LENGTH_SHORT).show();
    }

    private boolean validateFields() {
        boolean noErrors = true;
        EditText product = findViewById(R.id.productName);
        EditText price = findViewById(R.id.price);

        if (product.getText().toString().equals("")) {
            noErrors = false;
        }

        if (price.getText().toString().equals("")) {
            noErrors = false;
        }

        if (picturePath == null) {
            noErrors = false;
        }

        return noErrors;
    }

    private HashMap<String, String> getFieldsValue() {
        HashMap<String, String> databaseEntry = new HashMap<>();
        EditText product = findViewById(R.id.productName);
        EditText price = findViewById(R.id.price);
        EditText description = findViewById(R.id.description);
        databaseEntry.put("Product", product.getText().toString());
        databaseEntry.put("Price", price.getText().toString());
        databaseEntry.put("Description", description.getText().toString());

        return databaseEntry;
    }

    @Override
    protected void onStop() {
        if (picturePath != null) {
            File directoryToClean = new File(picturePath);
            for (File child : directoryToClean.getParentFile().listFiles()) {
                child.delete();
            }
        }
        super.onStop();
    }
}
