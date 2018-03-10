package dk.sdu.mmmi.nakosa;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Antonio on 05-03-2018.
 */

public class AdvertisementsFragment extends Fragment {

    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private List<AdvertisementData> ads;
    private ProgressBar initialLoadProgressBar;
    private UserData loggedInUser;
    private View v;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_advertisement, container, false);
        ((TextView) v.findViewById(R.id.textView4)).setText(getString(R.string.welcome_text) + " " + loggedInUser.getFirstName());

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Advertisements");

        loggedInUser = UserData.getInstance();
        Log.d("Saved", ""+savedInstanceState);

        initialLoadProgressBar = v.findViewById(R.id.initialSpinner);

        ads = new ArrayList<>();

        /*final GridView gridview = v.findViewById(R.id.gridview);
        adapter = new ImageAdapter(getContext(), ads);
        gridview.setAdapter(adapter);
*/
        setupRecycler();
        downloadData();

        mAdapter = new AdvertisementsGridAdapter(ads);
        mRecyclerView.setAdapter(mAdapter);

        /*gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                ViewAdvertisementFragment fragment = new ViewAdvertisementFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("ProductData", createAdvertisementData(position));
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.fragment, fragment).addToBackStack(null).commit();
            }
        });*/

        FloatingActionButton fab = v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewAdFragment fragment = new NewAdFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment, fragment).addToBackStack(null).commit();
            }
        });

        return v;
    }

    private void setupRecycler() {
        mRecyclerView = v.findViewById(R.id.advertisement_content_recycler);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    private void downloadData() {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                DatabaseAdvertisement databaseAdvertisement = dataSnapshot.getValue(DatabaseAdvertisement.class);
                //Map<String, Object> entry = new HashMap<>();

                //entry.put("Key", dataSnapshot.getKey());
//                entry.put("Product", databaseAdvertisement.Product);
//                entry.put("Description", databaseAdvertisement.Description);
//                entry.put("Price", databaseAdvertisement.Price);
//                entry.put("Seller", databaseAdvertisement.Seller);
//                entry.put("ImagePath", databaseAdvertisement.ImagePath);
//                entry.put("DownloadPath", databaseAdvertisement.ImageDownloadPath);
//                entry.put("Image", null);
                //downloadImage(dataSnapshot.getKey(), databaseAdvertisement.ImagePath);

                ads.add(mapDatabaseResult(databaseAdvertisement));
                //adapter.notifyDataSetChanged();
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
    }

    private AdvertisementData mapDatabaseResult(DatabaseAdvertisement data) {
        AdvertisementData advertisementData = new AdvertisementData();
        advertisementData.setSeller(data.Seller);
        advertisementData.setProductName(data.Product);
        advertisementData.setDescription(data.Description);
        advertisementData.setPrice(data.Price);
        advertisementData.setDownloadPath(data.ImageDownloadPath);

        return advertisementData;
    }
/*
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
        data.setSeller((String) ads.get(position).get("Seller"));
        data.setDescription((String) ads.get(position).get("Description"));
        data.setImagePath((String) ads.get(position).get("ImagePath"));
        data.setPrice((String) ads.get(position).get("Price"));
        data.setDownloadPath((String) ads.get(position).get("ImageDownloadPath"));

        return data;
    }*/
}
