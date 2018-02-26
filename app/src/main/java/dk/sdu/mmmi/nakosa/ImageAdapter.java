package dk.sdu.mmmi.nakosa;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.List;
import java.util.Map;


public class ImageAdapter extends BaseAdapter {

    private Context context;
    private List<Map<String, Object>> ads;

    public ImageAdapter(Context mainActivity, List<Map<String, Object>> ads) {
        this.context = mainActivity;
        this.ads = ads;
    }

    @Override
    public int getCount() {
        return ads.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View newView;
        //if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            newView = inflater.inflate(R.layout.grid_item, null);

            ImageView img = newView.findViewById(R.id.imageView);
            TextView text = newView.findViewById(R.id.textView);
            ProgressBar progressBar = newView.findViewById(R.id.imageProgress);

            img.setClipToOutline(true);

            if (ads.get(i).get("Image") != null) {
                progressBar.setVisibility(View.INVISIBLE);
            }

            text.setText((String) ads.get(i).get("Product"));
            img.setImageBitmap((Bitmap) ads.get(i).get("Image"));
            /*ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = text;
            viewHolder.image = img;
            viewHolder.progressBar = progressBar;
            newView.setTag(viewHolder);

        } else {
            newView = view;
            ViewHolder viewHolder = (ViewHolder) newView.getTag();
            viewHolder.text.setText((String) ads.get(i).get("Product"));
            viewHolder.image.setImageBitmap((Bitmap) ads.get(i).get("Image"));

            if (ads.get(i).get("Image") != null) {
                viewHolder.progressBar.setVisibility(View.INVISIBLE);
            }
        }*/

        return newView;
    }

    private static class ViewHolder {
        TextView text;
        ImageView image;
        ProgressBar progressBar;
    }

}
