package dk.sdu.mmmi.nakosa;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Antonio on 07-03-2018.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private String[] dataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CardView card;
        public TextView cardText;
        public ImageView imageTest;

        public ViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card_view);
            cardText = itemView.findViewById(R.id.info_text);
            imageTest = itemView.findViewById(R.id.image_test);
        }
    }

    public MyAdapter(String[] dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FrameLayout v = (FrameLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_cardview, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.cardText.setText(dataSet[position]);
    }

    @Override
    public int getItemCount() {
        return dataSet.length;
    }
}
