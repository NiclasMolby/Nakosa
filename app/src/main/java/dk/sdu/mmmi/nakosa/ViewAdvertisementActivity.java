package dk.sdu.mmmi.nakosa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ViewAdvertisementActivity extends AppCompatActivity {

    //View root = findViewById(R.id.rootLayoutView);

    TextView productName;
    TextView price;
    TextView seller;
    TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_advertisement);

        productName = findViewById(R.id.productName);
        price = findViewById(R.id.price);
        seller = findViewById(R.id.seller);
        description = findViewById(R.id.description);

        AdvertisementData data = (AdvertisementData) getIntent().getSerializableExtra("Advertisement");

        productName.setText(data.getProductName());
        price.setText(data.getPrice());
        seller.setText(data.getSeller());
        description.setText(data.getDescription());
    }
}
