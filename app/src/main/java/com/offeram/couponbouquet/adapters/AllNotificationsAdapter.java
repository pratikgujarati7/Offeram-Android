package com.offeram.couponbouquet.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.offeram.couponbouquet.MerchantDetailsActivity;
import com.offeram.couponbouquet.R;
import com.offeram.couponbouquet.models.Notification;

import java.util.ArrayList;
import java.util.List;

public class AllNotificationsAdapter extends RecyclerView.Adapter<AllNotificationsAdapter.MyViewHolder> {
    List<Notification> data = new ArrayList<>();
    String isRead;
    private LayoutInflater inflater;
    private Context context;


    public AllNotificationsAdapter(Context context, List<Notification> data, String isRead) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.isRead = isRead;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_all_notification_list, parent, false);
        final MyViewHolder holder = new MyViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Notification n = data.get(position);
                Log.e("In AllNotiAdptr", "MerchantId : " + n.getMerchantId());
                if (!n.getMerchantId().equals("0")) {
                    Intent intent = new Intent(context, MerchantDetailsActivity.class);
                    intent.putExtra("merchantId", n.getMerchantId());
                    context.startActivity(intent);
                }
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Notification n = data.get(position);
        Glide.with(context).load(n.getCompanyLogo()).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(R.drawable.ic_launcher)
                .into(holder.imageIv);
        holder.titleTv.setText(n.getNotificationTitle());
        holder.fromNameTv.setText(n.getFromName());
        holder.timeTv.setText(n.getTime());
//        Drawable color = new ColorDrawable(context.getResources().getColor(R.color.darkPurple));
//        LayerDrawable ld = new LayerDrawable(new Drawable[]{color});
//        holder.unreadIv.setImageDrawable(ld);
        holder.unreadIv.setImageDrawable(new ColorDrawable(context.getResources().getColor(R.color.darkPurple)));
        if (isRead.equals("0")) {
            holder.unreadIv.setVisibility(View.VISIBLE);
        } else if (isRead.equals("1")) {
            holder.unreadIv.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return (data.size());
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageIv, unreadIv;
        TextView titleTv, fromNameTv, timeTv;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageIv = (ImageView) itemView.findViewById(R.id.imageIv);
            unreadIv = (ImageView) itemView.findViewById(R.id.unreadIv);
            titleTv = (TextView) itemView.findViewById(R.id.titleTv);
            fromNameTv = (TextView) itemView.findViewById(R.id.fromNameTv);
            timeTv = (TextView) itemView.findViewById(R.id.timeTv);
        }


        public void getData() {

        }

    }

}
