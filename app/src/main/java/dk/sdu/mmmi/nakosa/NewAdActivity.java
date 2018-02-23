package dk.sdu.mmmi.nakosa;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class NewAdActivity extends AppCompatActivity {

    private final int CAMERA_CODE = 1;
    private String picturePath;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ad);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Advertisements");
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
            Log.d("Photo", "Photo taken - "+ picturePath);
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

    public void saveAdvertisementInDb(View view) {
        if(validateFields()) {
            databaseReference.push().setValue(getFieldsValue());
            finish();
        } else {
            // Lav en toast eller lign.
        }
    }

    private boolean validateFields() {
        boolean noErrors = true;
        EditText product = findViewById(R.id.productName);
        EditText price = findViewById(R.id.price);
        EditText description = findViewById(R.id.description);

        if(product.getText().toString().equals("")) {
            noErrors = false;
        }

        if(price.getText().toString().equals("")) {
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
        if(picturePath != null) {
            File directoryToClean = new File(picturePath);
            for(File child : directoryToClean.getParentFile().listFiles()) {
                child.delete();
            }
        }
        super.onStop();
    }
}
