package com.offeram.couponbouquet.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.offeram.couponbouquet.Config;
import com.offeram.couponbouquet.MerchantDetailsActivity;
import com.offeram.couponbouquet.R;
import com.offeram.couponbouquet.RoundedCornersTransformation;
import com.offeram.couponbouquet.models.OutletForOffer;
import com.offeram.couponbouquet.models.SearchItem;

import java.util.ArrayList;
import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    List<SearchItem> data = new ArrayList<>();


    public SearchResultAdapter(Context context, List<SearchItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_search_result_list, parent, false);
        final MyViewHolder holder = new MyViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                SearchItem si = data.get(position);
                Intent intent = new Intent(context, MerchantDetailsActivity.class);
                intent.putExtra("merchantId", si.getMerchantId());
                context.startActivity(intent);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final SearchItem si = data.get(position);
        List<OutletForOffer> ofo = si.getOutlets();
        Glide.with(context).load(si.getCouponImage()).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .bitmapTransform(new RoundedCornersTransformation(context, 20, 0, RoundedCornersTransformation.CornerType.LEFT))
                .error(R.drawable.ic_bird)
                .into(holder.imageIv);
        holder.titleTv.setText(si.getCompanyName());
        holder.offerTv.setText(si.getCouponTitle());
//        holder.cuisinesTv.setText(o.getCuisines());
//        holder.redeemTv.setText(o.getRedeem() + " redeemed");
        if(si.getNumOfRedeem() == 0)
        {
            holder.redeemTv.setVisibility(View.INVISIBLE);
        } else {
            holder.redeemTv.setVisibility(View.VISIBLE);
            holder.redeemTv.setText(si.getNumOfRedeem() + " redeemed");
        }

        holder.outletTv.setText("-");
        holder.distanceTv.setText("-");

        if (Config.getSharedPreferences(context, "latitude") != null &&
                !Config.getSharedPreferences(context, "latitude").equals("0")) {
            holder.distanceTv.setVisibility(View.VISIBLE);
        } else {
            holder.distanceTv.setVisibility(View.GONE);
        }
        if (ofo.size() > 0) {
            String outlet = ofo.get(0).getAreaName();
            double dist = ofo.get(0).getDistance();
            for (int i = 0; i < ofo.size(); i++) {
                OutletForOffer fo = ofo.get(i);
                if (dist > fo.getDistance()) {
                    dist = fo.getDistance();
                    outlet = fo.getAreaName();
                }
            }
            holder.distanceTv.setText(dist + " km");
            if (ofo.size() == 1) {
                holder.outletTv.setText(outlet);
            } else {
                holder.outletTv.setText(outlet + ", + " + (ofo.size() - 1) + " Outlets");
            }
        }

        if (si.getAverageRatings() != null && (si.getAverageRatings().equals("") || si.getAverageRatings().equals("0"))) {
            holder.reviewTv.setText("-");
        } else {
            holder.reviewTv.setText(si.getAverageRatings());
        }

    }

    @Override
    public int getItemCount() {
        return (data.size());
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageIv;
        TextView distanceTv, titleTv, reviewTv, outletTv, offerTv, redeemTv;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageIv = (ImageView) itemView.findViewById(R.id.imageIv);
            distanceTv = (TextView) itemView.findViewById(R.id.distanceTv);
            titleTv = (TextView) itemView.findViewById(R.id.titleTv);
            reviewTv = (TextView) itemView.findViewById(R.id.reviewTv);
            outletTv = (TextView) itemView.findViewById(R.id.outletTv);
            offerTv = (TextView) itemView.findViewById(R.id.offerTv);
            redeemTv = (TextView) itemView.findViewById(R.id.redeemTv);
        }


        public void getData() {

        }

    }

}
