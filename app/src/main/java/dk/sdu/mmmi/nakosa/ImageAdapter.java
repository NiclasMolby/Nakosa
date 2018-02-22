package dk.sdu.mmmi.nakosa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


public class ImageAdapter extends BaseAdapter {

    private Context context;

    public ImageAdapter(Context mainActivity) {
        this.context = mainActivity;
    }

    @Override
    public int getCount() {
        return 10;
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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        View newView;
        newView = inflater.inflate(R.layout.grid_item, null);
        ImageView img = newView.findViewById(R.id.imageView);
        img.setClipToOutline(true);
        newView.findViewById(R.id.textView);
        return newView;
    }

}
