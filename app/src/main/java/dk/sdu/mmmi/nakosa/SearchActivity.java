package dk.sdu.mmmi.nakosa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    public void goBack(View view) {
        finish();
        overridePendingTransition(0, 0);
    }
}
