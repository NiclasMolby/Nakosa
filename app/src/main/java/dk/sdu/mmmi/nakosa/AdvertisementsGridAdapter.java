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


public class AdvertisementsGridAdapter extends RecyclerView.Adapter<AdvertisementsGridAdapter.AdvertisementsGridViewHolder> {

    private List<AdvertisementData> dataSet;

    public static class AdvertisementsGridViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView cardText;
        private ImageView image;
        private AdvertisementData data;

        public AdvertisementsGridViewHolder(View itemView) {
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

    public AdvertisementsGridAdapter(List<AdvertisementData> dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public AdvertisementsGridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FrameLayout v = (FrameLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_cardview, parent, false);
        AdvertisementsGridViewHolder vh = new AdvertisementsGridViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(AdvertisementsGridViewHolder holder, int position) {
        holder.setData(dataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
