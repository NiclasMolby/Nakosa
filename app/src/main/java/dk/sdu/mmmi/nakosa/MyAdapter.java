package dk.sdu.mmmi.nakosa;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Antonio on 07-03-2018.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyAdsViewHolder> {

    private List<AdvertisementData> dataSet;

    public static class MyAdsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView cardText;
        private ImageView image;
        private AdvertisementData data;

        public MyAdsViewHolder(View itemView) {
            super(itemView);
            cardText = itemView.findViewById(R.id.info_text);
            image = itemView.findViewById(R.id.my_ad_image);

            itemView.setOnClickListener(this);
        }

        public void setData(AdvertisementData data) {
            this.data = data;

            this.cardText.setText(this.data.getProductName());
            if(this.data.getDownloadPath() != null) {
                Glide.with(itemView)
                        .load(this.data.getDownloadPath())
                        .into(image);
                Log.d("DownloadCard", this.data.getDownloadPath());
            }
        }

        @Override
        public void onClick(View view) {
            Log.d("On card click", "Get adapter pos: " + getAdapterPosition() + "; Name: " + cardText.getText().toString());
            ViewAdvertisementFragment fragment = new ViewAdvertisementFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("ProductData", data);
            fragment.setArguments(bundle);
            ((Activity) view.getContext()).getFragmentManager().beginTransaction().replace(R.id.fragment, fragment).addToBackStack(null).commit();
        }
    }

    public MyAdapter(List<AdvertisementData> dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public MyAdsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FrameLayout v = (FrameLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_cardview, parent, false);
        MyAdsViewHolder vh = new MyAdsViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyAdsViewHolder holder, int position) {
        holder.setData(dataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
