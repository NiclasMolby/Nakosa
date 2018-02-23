package dk.sdu.mmmi.nakosa.util;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;

/**
 * Created by niclasmolby on 23/02/2018.
 */

public class NewAdvertisementUtil {

    private StorageReference storageReference;
    HashMap<String, String> adEntry;
    private String uploadedPath = "";

    public NewAdvertisementUtil(StorageReference storageReference) {
        this.storageReference = storageReference;
    }

    public String uploadImageToStorage(String picturePath) {
        if(picturePath != null) {
            File image = new File(picturePath);
            Uri file = Uri.fromFile(image);
            String imageName = image.getName();
            StorageReference imageRef = storageReference.child("images/" + imageName);
            String fileStorageUri = "";

            imageRef.putFile(file)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            setUploadedPath(downloadUrl.getPath());
                            Log.d("Image Upload", "Success");
                            Log.d("Image path", downloadUrl.getPath());

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                            Log.d("Image Upload", "Failed");
                        }
                    });
        }

        return uploadedPath;
    }

    private void setUploadedPath(String path) {
        this.uploadedPath = path;
    }
}
