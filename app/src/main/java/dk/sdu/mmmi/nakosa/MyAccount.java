package dk.sdu.mmmi.nakosa;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyAccount extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View v;
    private UserData userData = UserData.getInstance();
    private DatabaseReference databaseReference;
    private List<DatabaseAdvertisement> datas;

    public MyAccount() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_my_account, container, false);
        datas = new ArrayList<>();

        String name = userData.getFirstName() + " " + userData.getLastName();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Advertisements");
        Query query = databaseReference.orderByChild("Seller").equalTo(name);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snap : dataSnapshot.getChildren()) {
                    //DatabaseAdvertisement ad = new DatabaseAdvertisement();
                    Log.d("Snap", snap.child("Product").getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mRecyclerView = v.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        String[] dataSet = {"Piklas", "Er", "En", "Lort"};
        mAdapter = new MyAdapter(dataSet);
        mRecyclerView.setAdapter(mAdapter);

        return v;
    }
}
