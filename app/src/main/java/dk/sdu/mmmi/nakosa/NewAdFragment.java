package dk.sdu.mmmi.nakosa;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class NewAdFragment extends Fragment {

    private final int CAMERA_CODE = 1;
    private String picturePath;
    private String compressedPath;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private View progress;
    private UserData loggedInUser;
    private View v;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_new_ad, container, false);
        ButterKnife.bind(this, v);

        loggedInUser = UserData.getInstance();
        progress = getActivity().findViewById(R.id.progress_overlay);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Advertisements");
        storageReference = FirebaseStorage.getInstance().getReference();

        return v;
    }

    @OnClick(R.id.addPicture)
    public void useCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_CODE);
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_CODE && resultCode == RESULT_OK) {
            Log.d("Photo", "Photo taken - " + picturePath);
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            double ratio = ((double) bitmap.getHeight()) / ((double) bitmap.getWidth());
            int targetWidth = 400;
            int targetHeight = (int) (targetWidth * ratio);
            Log.d("Source image", "Source height: " + bitmap.getHeight());
            Log.d("Source image", "Source width: " + bitmap.getWidth());
            Log.d("Ratio", "Ratio: " + ratio);
            Log.d("Target image", "Target Height: " + targetHeight);
            Log.d("Target image", "Target width: " + targetWidth);
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, false);
            Log.d("Source image", "Source size: " + bitmap.getAllocationByteCount());
            Log.d("Source image", "Target size: " + scaled.getByteCount());

            writeCompressedImage(scaled);
            ImageView image = v.findViewById(R.id.imageFromCamera);

            image.setImageBitmap(scaled);
            image.setVisibility(View.VISIBLE);
        }
    }

    private void writeCompressedImage(Bitmap bitmap) {
        FileOutputStream out = null;
        String compressedPath = picturePath.substring(0, picturePath.length() - 4) + "_compressed.jpg";
        File file = new File(compressedPath);
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            this.compressedPath = compressedPath;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        // Save a file: path for use with ACTION_VIEW intents
        picturePath = image.getAbsolutePath();
        return image;
    }

    @OnClick(R.id.addAd)
    public void createNewAd() {
        if (validateFields()) {
            progress.setVisibility(View.VISIBLE);
            uploadImageToStorageAndSaveInDB(getFieldsValue());
        } else {
            Toast.makeText(getContext(), "Produkt navn og pris skal udfyldes!", Toast.LENGTH_SHORT).show();
        }
    }

    public void uploadImageToStorageAndSaveInDB(final HashMap<String, String> dbEntry) {
        File image = new File(compressedPath);
        Uri file = Uri.fromFile(image);
        final String imageName = image.getName();
        StorageReference imageRef = storageReference.child("images/" + imageName);

        imageRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Log.d("URL", downloadUrl.toString());
                        dbEntry.put("ImagePath", imageName);
                        dbEntry.put("ImageDownloadPath", downloadUrl.toString());
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
        progress.setVisibility(View.GONE);
        AdvertisementsFragment fragment = new AdvertisementsFragment();
        getFragmentManager().beginTransaction().replace(R.id.fragment, fragment, fragment.getClass().toString()).commit();
    }

    private void uploadError() {
        progress.setVisibility(View.GONE);
        Toast.makeText(getContext(), "Der skete en fejl, prøv igen.", Toast.LENGTH_SHORT).show();
    }

    private boolean validateFields() {
        boolean noErrors = true;
        EditText product = v.findViewById(R.id.productName);
        EditText price = v.findViewById(R.id.price);

        if (product.getText().toString().equals("")) {
            noErrors = false;
        }

        if (price.getText().toString().equals("")) {
            noErrors = false;
        }

        if (compressedPath == null) {
            noErrors = false;
        }

        return noErrors;
    }

    private HashMap<String, String> getFieldsValue() {
        HashMap<String, String> databaseEntry = new HashMap<>();
        EditText product = v.findViewById(R.id.productName);
        EditText price = v.findViewById(R.id.price);
        EditText description = v.findViewById(R.id.description);
        databaseEntry.put("Seller", loggedInUser.getName());
        databaseEntry.put("Product", product.getText().toString());
        databaseEntry.put("Price", price.getText().toString());
        databaseEntry.put("Description", description.getText().toString().trim());

        return databaseEntry;
    }

    @Override
    public void onStop() {
        if (picturePath != null) {
            File directoryToClean = new File(picturePath);
            for (File child : directoryToClean.getParentFile().listFiles()) {
                child.delete();
            }
        }
        super.onStop();
    }
}
