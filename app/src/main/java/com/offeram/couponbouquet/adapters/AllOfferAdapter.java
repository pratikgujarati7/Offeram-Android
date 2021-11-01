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
import com.offeram.couponbouquet.models.AllOffer;
import com.offeram.couponbouquet.models.OutletForOffer;

import java.util.ArrayList;
import java.util.List;

public class AllOfferAdapter extends RecyclerView.Adapter<AllOfferAdapter.MyViewHolder> {
    List<AllOffer> data = new ArrayList<>();
    // Note :- Both of the below variables are used for swipe in view pager and filter data according to tabs
    String offersResponse;
    String type;
    private LayoutInflater inflater;
    private Context context;
    private String city;


    public AllOfferAdapter(Context context, List<AllOffer> data, String offersResponse, String type) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.offersResponse = offersResponse;
        this.type = type;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_all_offers_home_list, parent, false);
        final MyViewHolder holder = new MyViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                AllOffer o = data.get(position);
                Intent intent = new Intent(context, MerchantDetailsActivity.class);
//                intent.putExtra("offersResponse", offersResponse);
//                intent.putExtra("type", type);
//                intent.putExtra("position", position);
                intent.putExtra("merchantId", o.getMerchantId());
//                intent.putExtra("outlet", holder.outletTv.getText().toString());
                context.startActivity(intent);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final AllOffer o = data.get(position);
        List<OutletForOffer> ofo = o.getOutlets();
        Glide.with(context).load(o.getCompanyBannerImage()).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .bitmapTransform(new RoundedCornersTransformation(context, 10, 0, RoundedCornersTransformation.CornerType.LEFT))
                .error(R.drawable.ic_bird)
                .into(holder.imageIv);
        holder.numOffersTv.setText(o.getUserCoupons() + " Offers");
//        if(o.getIsFeatured().equalsIgnoreCase("1")){
//            holder.featureTv.setVisibility(View.VISIBLE);
//            holder.featureTv.setText("Featured");
//        } else {
        holder.featureTv.setVisibility(View.GONE);
//        }
        holder.titleTv.setText(o.getCompanyName());
        holder.offerTv.setText(o.getOfferText());
        holder.cuisinesTv.setText(o.getCuisines());
        if (o.getNumOfRedeem() == 0) {
            holder.redeemTv.setVisibility(View.INVISIBLE);
        } else {
            holder.redeemTv.setVisibility(View.VISIBLE);
            holder.redeemTv.setText(o.getNumOfRedeem() + " redeemed");
        }

        holder.outletTv.setText("-");
        holder.distanceTv.setText("-");
        // Note :- Below is the code for hiding distance field when latLong is 0 or null
        /*if (Config.getSharedPreferences(context, "latitude") != null &&
                !Config.getSharedPreferences(context, "latitude").equals("0")) {
            holder.distanceTv.setVisibility(View.VISIBLE);
            if (ofo.size() > 0) {
                String outlet = ofo.get(0).getAreaName();
                double dist = ofo.get(0).getDistance();
                for (int i = 0; i < ofo.size(); i++) {
                    OutletForOffer fo = ofo.get(i);
                    city = fo.getCity_name();
                    if (dist > fo.getDistance()) {
                        dist = fo.getDistance();
                        outlet = fo.getAreaName();
                    }
                }
                holder.distanceTv.setText(dist + " km");

                //  Below is the code for displaying nearest outlet first depending on the distance
                if (ofo.size() == 1) {
                    holder.outletTv.setText(outlet);
                } else {
                    holder.outletTv.setText(outlet + ", + " + (ofo.size() - 1) + " Outlets");
                }
            }
        } else {
            holder.distanceTv.setVisibility(View.GONE);
            if (ofo.size() > 0) {
                if (ofo.size() == 1) {
                    holder.outletTv.setText(ofo.get(0).getAreaName());
                } else {
                    holder.outletTv.setText(ofo.get(0).getAreaName() + ", + " + (ofo.size() - 1) + " Outlets");
                }
            } else {
                holder.outletTv.setText("-");
            }
        }*/

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

        if (o.getAverageRatings() != null && (o.getAverageRatings().equals("") || o.getAverageRatings().equals("0"))) {
            holder.reviewTv.setText("-");
        } else {
            holder.reviewTv.setText(o.getAverageRatings());
        }
        if (o.getIsStarred() == 0) {
            holder.starredIv.setVisibility(View.GONE);
        } else if (o.getIsStarred() == 1) {
            holder.starredIv.setVisibility(View.VISIBLE);
        }
        holder.pingedIv.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return (data.size());
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageIv, starredIv, pingedIv;
        TextView numOffersTv, distanceTv, featureTv, titleTv, reviewTv, outletTv, cuisinesTv, offerTv, redeemTv;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageIv = (ImageView) itemView.findViewById(R.id.imageIv);
            numOffersTv = (TextView) itemView.findViewById(R.id.numOffersTv);
            distanceTv = (TextView) itemView.findViewById(R.id.distanceTv);
            featureTv = (TextView) itemView.findViewById(R.id.featureTv);
            titleTv = (TextView) itemView.findViewById(R.id.titleTv);
            reviewTv = (TextView) itemView.findViewById(R.id.reviewTv);
            outletTv = (TextView) itemView.findViewById(R.id.outletTv);
            cuisinesTv = (TextView) itemView.findViewById(R.id.cuisinesTv);
            offerTv = (TextView) itemView.findViewById(R.id.offerTv);
            redeemTv = (TextView) itemView.findViewById(R.id.redeemTv);
            starredIv = (ImageView) itemView.findViewById(R.id.starredIv);
            pingedIv = (ImageView) itemView.findViewById(R.id.pingedIv);
        }


        public void getData() {

        }

    }

}
