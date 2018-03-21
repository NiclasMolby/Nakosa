package dk.sdu.mmmi.nakosa;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class ViewAdvertisementFragment extends Fragment {

    private StorageReference storageReference;

    private TextView productName;
    private TextView price;
    private TextView seller;
    private TextView description;
    private ImageView image;
    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v = inflater.inflate(R.layout.fragment_view_advertisement, container, false);
        AdvertisementData data = (AdvertisementData) getArguments().getSerializable("ProductData");

        if(!data.getSeller().equals(UserData.getInstance().getName())) {
            View delete_button = v.findViewById(R.id.delete_ad);
            delete_button.setVisibility(View.GONE);
        }

        storageReference = FirebaseStorage.getInstance().getReference();

        productName = v.findViewById(R.id.productName);
        price = v.findViewById(R.id.price);
        seller = v.findViewById(R.id.seller);
        description = v.findViewById(R.id.description);
        image = v.findViewById(R.id.image);


        productName.setText(data.getProductName());
        price.setText(data.getPrice());
        seller.setText(data.getSeller());
        description.setText(data.getDescription());

        Glide.with(v)
                .load(data.getDownloadPath())
                .into(image);
        v.findViewById(R.id.viewAdImageProgress).setVisibility(View.GONE);

        //downloadImage(data.getImagePath());

        return v;
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
        v.findViewById(R.id.viewAdImageProgress).setVisibility(View.GONE);
    }
}
