package com.offeram.couponbouquet.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.offeram.couponbouquet.R;
import com.offeram.couponbouquet.models.RedeemersModel;

import java.util.List;

public class TopTenAdapter extends  RecyclerView.Adapter<TopTenAdapter.MyViewHolder> {
    Context context;
    private LayoutInflater inflater;
    List<RedeemersModel> list=null;
    RedeemersModel data;

    public TopTenAdapter(Context context, List<RedeemersModel> list) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_top_ten, parent, false);
        final MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final RedeemersModel rm=list.get(position);
        holder.number.setText(String.valueOf(position+1)+".");
        if(position %2 == 1)
        {
            // Set a background color for ListView regular row/item
            Glide.with(context).load(rm.getRedeemer_profile_image_url()).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .error(R.drawable.offeram_two)
                    .into(holder.img_profile);
        }
        else
        {
            // Set the background color for alternate row/item
            Glide.with(context).load(rm.getRedeemer_profile_image_url()).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .error(R.drawable.offeram_one)
                    .into(holder.img_profile);
        }

        holder.txt_top_name.setText(rm.getRedeemer_name());
        holder.txt_top_price.setText(rm.getRedeemed_amount());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {


        private final ImageView img_profile;
        private final TextView txt_top_name,txt_top_price,number;

        public MyViewHolder(View itemView) {
            super(itemView);
            img_profile=(ImageView)itemView.findViewById(R.id.img_profile);
            txt_top_name=(TextView)itemView.findViewById(R.id.txt_top_name);
            txt_top_price=(TextView)itemView.findViewById(R.id.txt_top_price);
            number=(TextView)itemView.findViewById(R.id.number);

        }

    }
}
