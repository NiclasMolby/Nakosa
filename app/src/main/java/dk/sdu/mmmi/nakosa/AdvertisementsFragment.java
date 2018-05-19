package dk.sdu.mmmi.nakosa;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Antonio on 05-03-2018.
 */

public class AdvertisementsFragment extends Fragment {

    private DatabaseReference databaseReference;
    private List<AdvertisementData> ads;
    private ProgressBar initialLoadProgressBar;
    private UserData loggedInUser;
    private View v;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        loggedInUser = UserData.getInstance();
        v = inflater.inflate(R.layout.fragment_advertisement, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Advertisements");

        Log.d("Saved", ""+savedInstanceState);

        initialLoadProgressBar = v.findViewById(R.id.initialSpinner);

        ads = new ArrayList<>();

        setupRecycler();
        downloadData();

        FloatingActionButton fab = v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewAdFragment fragment = new NewAdFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment, fragment, fragment.getClass().toString()).addToBackStack(null).commit();
            }
        });

        return v;
    }

    private void setupRecycler() {
        mRecyclerView = v.findViewById(R.id.advertisement_content_recycler);
        mRecyclerView.setHasFixedSize(true);


        mLayoutManager = new GridLayoutManager(v.getContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    private void downloadData() {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                DatabaseAdvertisement databaseAdvertisement = dataSnapshot.getValue(DatabaseAdvertisement.class);
                databaseAdvertisement.setKey(dataSnapshot.getKey());

                ads.add(mapDatabaseResult(databaseAdvertisement));
                mAdapter = new AdvertisementsGridAdapter(ads);
                mRecyclerView.setAdapter(mAdapter);
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
        advertisementData.setKey(data.getKey());
        advertisementData.setSeller(data.Seller);
        advertisementData.setProductName(data.Product);
        advertisementData.setDescription(data.Description);
        advertisementData.setPrice(data.Price);
        advertisementData.setDownloadPath(data.ImageDownloadPath);

        return advertisementData;
    }
}
