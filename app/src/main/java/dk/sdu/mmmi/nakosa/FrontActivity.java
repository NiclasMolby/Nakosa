package dk.sdu.mmmi.nakosa;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

public class FrontActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front);



        if(getFragmentManager().getBackStackEntryCount() == 0) {
            AdvertisementsFragment fragment = new AdvertisementsFragment();
            fragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).commit();
        }
        else {
            Log.d("BackStackItem", ""+getFragmentManager().getBackStackEntryAt(1));
        }
        setContentView(R.layout.activity_front);
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.action_home:
                                selectedFragment = new AdvertisementsFragment();
                                break;
                            case R.id.action_myAccount:
                                selectedFragment = new MyAccount();
                                break;
                            case R.id.action_settings:
                                selectedFragment = new Settings();
                                break;
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, selectedFragment).commit();
                        return true;
                    }
                });

        AdvertisementsFragment fragment = new AdvertisementsFragment();
        fragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).commit();
    }
}
