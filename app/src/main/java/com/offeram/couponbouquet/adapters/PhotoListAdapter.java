package com.offeram.couponbouquet.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.offeram.couponbouquet.FullImageActivity;
import com.offeram.couponbouquet.R;
import com.offeram.couponbouquet.models.Merchant;
import com.offeram.couponbouquet.models.Photo;

import java.util.ArrayList;
import java.util.List;

public class PhotoListAdapter extends RecyclerView.Adapter<PhotoListAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    List<Photo> data = new ArrayList<>();
    String isMenu = "";
    Merchant m = null;


    public PhotoListAdapter(Context context, List<Photo> data, String isMenu, Merchant m) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.isMenu = isMenu;
        this.m = m;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_photo_list, parent, false);
        final MyViewHolder holder = new MyViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Log.e("In PhotoListAdptr", "Position : " + position + "");
                Intent intent = new Intent(context, FullImageActivity.class);
                intent.putExtra("merchantDetails", new Gson().toJson(m));
                intent.putExtra("imagePos", position + "");
                intent.putExtra("isMenu", isMenu);
                context.startActivity(intent);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Photo p = data.get(position);
        Glide.with(context).load(p.getUrl()).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .thumbnail(0.5f)
                .override(100, 200)
                .error(R.drawable.ic_bird)
                .into(holder.imageIv);

//        Note :- Static image
//        Glide.with(context).load(R.drawable.img12).diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)
//                .thumbnail(0.5f)
//                .override(100, 200)
//                .error(R.drawable.ic_bird)
//                .into(holder.imageIv);
    }

    @Override
    public int getItemCount() {
        return (data.size());
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageIv;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageIv = (ImageView) itemView.findViewById(R.id.imageIv);
        }

        public void getData() {
        }

    }

}
