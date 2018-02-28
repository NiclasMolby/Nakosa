package dk.sdu.mmmi.nakosa;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class ViewAdvertisementActivity extends AppCompatActivity {

    private StorageReference storageReference;

    private TextView productName;
    private TextView price;
    private TextView seller;
    private TextView description;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_advertisement);

        storageReference = FirebaseStorage.getInstance().getReference();

        productName = findViewById(R.id.productName);
        price = findViewById(R.id.price);
        seller = findViewById(R.id.seller);
        description = findViewById(R.id.description);
        image = findViewById(R.id.image);

        AdvertisementData data = (AdvertisementData) getIntent().getSerializableExtra("Advertisement");

        productName.setText(data.getProductName());
        price.setText(data.getPrice());
        seller.setText(data.getSeller());
        description.setText(data.getDescription());

        downloadImage(data.getImagePath());
    }

    private void downloadImage(final String imageName) {
        Log.d("Download", "Download image " + imageName + " from firebase storage");
        File localFile = null;
        StorageReference imageRef = storageReference.child("images/" + imageName);

        try {
            localFile = File.createTempFile("images" + imageName, "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        final File finalLocalFile = localFile;
        imageRef.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        generateBitmapAndUpdateImage(finalLocalFile.getAbsolutePath());
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle failed download
                // ...
            }
        });
    }

    private void generateBitmapAndUpdateImage(String imagePath) {
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        image.setImageBitmap(bitmap);
        findViewById(R.id.viewAdImageProgress).setVisibility(View.GONE);
    }
}
