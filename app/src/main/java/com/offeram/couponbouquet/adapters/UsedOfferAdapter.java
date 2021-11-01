package com.offeram.couponbouquet.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.offeram.couponbouquet.Config;
import com.offeram.couponbouquet.HomeActivity;
import com.offeram.couponbouquet.MerchantDetailsActivity;
import com.offeram.couponbouquet.PingActivity;
import com.offeram.couponbouquet.R;
import com.offeram.couponbouquet.RetroApiClient;
import com.offeram.couponbouquet.RetroApiInterface;
import com.offeram.couponbouquet.RoundedCornersTransformation;
import com.offeram.couponbouquet.fragments.FragmentUsedOffers;
import com.offeram.couponbouquet.models.AllOffer;
import com.offeram.couponbouquet.models.Coupon;
import com.offeram.couponbouquet.models.Merchant;
import com.offeram.couponbouquet.models.OutletForOffer;
import com.offeram.couponbouquet.models.UsedOffer;
import com.offeram.couponbouquet.responses.Common;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsedOfferAdapter extends RecyclerView.Adapter<UsedOfferAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    List<UsedOffer> data = new ArrayList<>();
    String balance;
    private ProgressDialog pd;
    FragmentUsedOffers fragmentUsedOffers;

    public UsedOfferAdapter(Context context, List<UsedOffer> data, String balance, FragmentUsedOffers fragmentUsedOffers) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.balance = balance;
        this.fragmentUsedOffers = fragmentUsedOffers;
    }
    public UsedOfferAdapter(Context context, List<UsedOffer> data, String balance) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.balance = balance;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_used_offers_list, parent, false);
        final MyViewHolder holder = new MyViewHolder(view);

//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int position = holder.getAdapterPosition();
//                UsedOffer o = data.get(position);
//                Intent intent = new Intent(context, MerchantDetailsActivity.class);
//                intent.putExtra("merchantId", o.getMerchantId());
//                context.startActivity(intent);
//            }
//        });

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final UsedOffer o = data.get(position);

        if (o.getIs_reused().equals("1")) {
            holder.rel_reactivate.setBackgroundColor(context.getResources().getColor(R.color.green));
            holder.img_coin.setVisibility(View.GONE);
            holder.txt_reactivate.setVisibility(View.GONE);
            holder.txt_heading.setVisibility(View.GONE);
            holder.txt_reactivated.setVisibility(View.VISIBLE);
        } else {
            holder.rel_reactivate.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            holder.img_coin.setVisibility(View.VISIBLE);
            holder.txt_reactivate.setVisibility(View.VISIBLE);
            holder.txt_heading.setVisibility(View.VISIBLE);
            holder.txt_heading.setText(o.getReuse_price()+" To Reactivate this Coupon");
            holder.txt_reactivated.setVisibility(View.GONE);

            holder.txt_reactivate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!o.getReuse_price().equals("")) {
                        if (Integer.parseInt(o.getReuse_price()) <= Integer.parseInt(balance)) {
                            final DialogPlus dialog = DialogPlus.newDialog(context)
                                    .setContentHolder(new ViewHolder(R.layout.dialog_confirmation_layout))
                                    .setContentHeight(LinearLayout.LayoutParams.WRAP_CONTENT)
                                    .setCancelable(false)
                                    .setGravity(Gravity.CENTER)
                                    .create();
                            dialog.show();

                            View errorView = dialog.getHolderView();
                            TextView titleTv = (TextView) errorView.findViewById(R.id.titleTv);
                            Button okBtn = (Button) errorView.findViewById(R.id.yesBtn);
                            Button noBtn = (Button) errorView.findViewById(R.id.noBtn);
                            titleTv.setVisibility(View.VISIBLE);
                            titleTv.setText("Are you sure you want to Reactivate this coupon for "+o.getReuse_price()+" Offeram Coins?");
                            okBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    reactiveCoupon(o.getCouponId(),o.getPaymentId(),o.getRedemptionId());
                                }

                            });
                            noBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }

                            });
                        } else {
                            Config.showDialog(context, "You Don't have suficient Offeram Coins to reuse this Coupon.");
                        }
                    }
                }
            });

        }
        Glide.with(context).load(o.getCouponImage()).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .bitmapTransform(new RoundedCornersTransformation(context, 10, 0, RoundedCornersTransformation.CornerType.LEFT))
                .error(R.drawable.ic_bird)
                .into(holder.imageIv);
        holder.titleTv.setText(o.getCompanyName());
        holder.offerTv.setText(o.getCouponTitle());
        holder.textTv.setText("-");
        holder.outletTv.setText(o.getAreaName());
        holder.dateTv.setText(o.getDateUsed());

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

    private void reactiveCoupon(String couponId,String paymentId,String redumptionId) {
        if (Config.isConnectedToInternet(context)) {
            pd = new ProgressDialog(context);
            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.show();

            String userId = Config.getSharedPreferences(context, "userId");
            String cityId = Config.getSharedPreferences(context, "cityID");
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Call<Common> call = apiInterface.reactivate_coupon(userId,couponId,paymentId,cityId,redumptionId);
            call.enqueue(new Callback<Common>() {
                @Override
                public void onResponse(Call<Common> call, Response<Common> response) {
                    pd.dismiss();
                    Common c = response.body();
                    if (c.getSuccess() == 1) {
                        final DialogPlus dialog = DialogPlus.newDialog(context)
                                .setContentHolder(new ViewHolder(R.layout.dialog_ok_layout))
                                .setContentHeight(LinearLayout.LayoutParams.WRAP_CONTENT)
                                .setCancelable(false)
                                .setGravity(Gravity.CENTER)
                                .create();
                        dialog.show();

                        View errorView = dialog.getHolderView();
                        TextView titleTv = (TextView) errorView.findViewById(R.id.titleTv);
                        TextView messageTv = (TextView) errorView.findViewById(R.id.messageTv);
                        Button okBtn = (Button) errorView.findViewById(R.id.okBtn);
                        titleTv.setVisibility(View.GONE);
                        messageTv.setText(c.getMessage());
                        okBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                fragmentUsedOffers.getUsedOffer();
                            }
                        });
                    } else {
                        Config.showDialog(context, c.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<Common> call, Throwable t) {
                    pd.dismiss();
                    Log.e("In PingActivity", "OnFailure : " + t.getMessage());
                    Config.showDialog(context, Config.FailureMsg);
                }
            });

        } else {
            Config.showAlertForInternet((Activity) context);
        }
    }

    @Override
    public int getItemCount() {
        return (data.size());
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private final RelativeLayout rel_reactivate;
        ImageView imageIv, starredIv, pingedIv, img_coin;
        TextView numOffersTv, distanceTv, featureTv, titleTv, reviewTv, outletTv, textTv, dateTv, offerTv, redeemTv, txt_heading, txt_reactivate, txt_reactivated;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageIv = (ImageView) itemView.findViewById(R.id.imageIv);
//            numOffersTv = (TextView) itemView.findViewById(R.id.numOffersTv);
//            distanceTv = (TextView) itemView.findViewById(R.id.distanceTv);
//            featureTv = (TextView) itemView.findViewById(R.id.featureTv);
//            redeemTv = (TextView) itemView.findViewById(R.id.redeemTv);
            titleTv = (TextView) itemView.findViewById(R.id.titleTv);
            reviewTv = (TextView) itemView.findViewById(R.id.reviewTv);
            outletTv = (TextView) itemView.findViewById(R.id.outletTv);
            textTv = (TextView) itemView.findViewById(R.id.textTv);
            dateTv = (TextView) itemView.findViewById(R.id.dateTv);
            offerTv = (TextView) itemView.findViewById(R.id.offerTv);
            txt_heading = (TextView) itemView.findViewById(R.id.txt_heading);
            txt_reactivate = (TextView) itemView.findViewById(R.id.txt_reactivate);
            txt_reactivated = (TextView) itemView.findViewById(R.id.txt_reactivated);
            starredIv = (ImageView) itemView.findViewById(R.id.starredIv);
            pingedIv = (ImageView) itemView.findViewById(R.id.pingedIv);
            img_coin = (ImageView) itemView.findViewById(R.id.img_coin);
            rel_reactivate = (RelativeLayout) itemView.findViewById(R.id.rel_reactivate);
        }


        public void getData() {

        }

    }

}
