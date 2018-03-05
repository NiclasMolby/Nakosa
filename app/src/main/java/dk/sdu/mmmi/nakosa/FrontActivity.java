package dk.sdu.mmmi.nakosa;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class FrontActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AdvertisementsFragment fragment = new AdvertisementsFragment();
        fragment.setArguments(getIntent().getExtras());
        getFragmentManager().beginTransaction().replace(R.id.fragment, fragment).commit();

        setContentView(R.layout.activity_front);
    }
}
