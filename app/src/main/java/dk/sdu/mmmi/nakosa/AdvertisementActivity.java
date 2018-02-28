package dk.sdu.mmmi.nakosa;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdvertisementActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private List<Map<String, Object>> ads;
    private ImageAdapter adapter;
    private ProgressBar initialLoadProgressBar;
    private User loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement);

        loggedInUser = (User) getIntent().getSerializableExtra("userObject");
        ((TextView) findViewById(R.id.textView4)).setText("Welcome " + loggedInUser.getFirstName());

        initialLoadProgressBar = findViewById(R.id.initialSpinner);

        ads = new ArrayList<>();

        GridView gridview = findViewById(R.id.gridview);
        adapter = new ImageAdapter(this, ads);
        gridview.setAdapter(adapter);


        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Advertisements");

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {
                };
                Map<String, String> product = dataSnapshot.getValue(genericTypeIndicator);
                Map<String, Object> entry = new HashMap<>();

                entry.put("Key", dataSnapshot.getKey());
                entry.put("Product", product.get("Product"));
                entry.put("Description", product.get("Description"));
                entry.put("Price", product.get("Price"));
                //entry.put("Seller", product.get("Seller"));
                entry.put("ImagePath", product.get("ImagePath"));
                entry.put("Image", null);
                downloadImage(dataSnapshot.getKey(), product.get("ImagePath"));

                ads.add(entry);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent intent = new Intent(getBaseContext(), ViewAdvertisementActivity.class);
                intent.putExtra("Advertisement", createAdvertisementData(position));
                startActivity(intent);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), NewAdActivity.class);
                intent.putExtra("User", loggedInUser);
                startActivity(intent);
            }
        });
    }

    private String downloadImage(final String ID, final String imageName) {
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
                        initialLoadProgressBar.setVisibility(View.GONE);
                        generateBitmapAndUpdateMap(ID, finalLocalFile.getAbsolutePath());
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle failed download
                // ...
            }
        });
        return localFile.getAbsolutePath();
    }

    private void generateBitmapAndUpdateMap(String id, String imagePath) {
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        for(Map<String, Object> ad: ads) {
            if(ad.get("Key").equals(id)) {
                ad.put("Image", bitmap);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private AdvertisementData createAdvertisementData(int position) {
        AdvertisementData data = new AdvertisementData();
        data.setProductName((String) ads.get(position).get("Product"));
        data.setSeller("Antjon");
        data.setDescription((String) ads.get(position).get("Description"));
        data.setImagePath((String) ads.get(position).get("ImagePath"));
        data.setPrice((String) ads.get(position).get("Price"));

        return data;
    }
}
