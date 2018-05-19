package dk.sdu.mmmi.nakosa;

import android.app.DialogFragment;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewAdvertisementFragment extends Fragment implements AlertDialog.AlertDialogNotifier {

    private DatabaseReference databaseReference;

    private AdvertisementData data;
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
        ButterKnife.bind(this, v);

        data = (AdvertisementData) getArguments().getSerializable("ProductData");

        Log.d("SellerName", data.getSeller());
        Log.d("SellerName", UserData.getInstance().getName());
        Log.d("SellerName",""+data.getSeller().equals(UserData.getInstance().getName()));
        if(!data.getSeller().equals(UserData.getInstance().getName())) {
            Button delete_button = v.findViewById(R.id.delete_ad);
            delete_button.setVisibility(View.GONE);
        }

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

        return v;
    }

    @OnClick(R.id.delete_ad)
    public void deleteAd() {
        AlertDialog d = new AlertDialog();
        d.show(getFragmentManager(), "alert");
    }

    @Override
    public void onPositiveClick(DialogFragment dialogFragment) {
        Log.d("Delete ad", data.getKey());
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Advertisements").child(data.getKey());
        databaseReference.removeValue();

        // Start fragment
        Fragment fragment = new AdvertisementsFragment();
        getFragmentManager().beginTransaction().replace(R.id.fragment, fragment, fragment.getClass().toString()).commit();
    }
}
