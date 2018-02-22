package dk.sdu.mmmi.nakosa;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class NewAdActivity extends AppCompatActivity {

    private String picturePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ad);


    }

    public void useCamera(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 1);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Log.d("Camera", "Width: "+imageBitmap.getWidth());
            Log.d("Camera", "Height: " + imageBitmap.getHeight());
            ImageView image = findViewById(R.id.imageFromCamera);
            image.setImageBitmap(imageBitmap);
            image.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onStop() {
        Log.d("OnStop", "Stopped");
        super.onStop();
    }

    @Override
    protected void onPause() {
        Log.d("OnPause", "Paused");
        super.onPause();
    }
}
