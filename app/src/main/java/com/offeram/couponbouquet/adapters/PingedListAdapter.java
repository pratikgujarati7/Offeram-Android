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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.offeram.couponbouquet.Config;
import com.offeram.couponbouquet.HomeActivity;
import com.offeram.couponbouquet.MerchantDetailsActivity;
import com.offeram.couponbouquet.OfferDetailsActivity;
import com.offeram.couponbouquet.PingedOffers;
import com.offeram.couponbouquet.R;
import com.offeram.couponbouquet.RetroApiClient;
import com.offeram.couponbouquet.RetroApiInterface;
import com.offeram.couponbouquet.RoundedCornersTransformation;
import com.offeram.couponbouquet.fragments.FragmentPinged;
import com.offeram.couponbouquet.models.AllVersion;
import com.offeram.couponbouquet.models.Coupon;
import com.offeram.couponbouquet.models.Merchant;
import com.offeram.couponbouquet.models.OutletForOffer;
import com.offeram.couponbouquet.models.SearchItem;
import com.offeram.couponbouquet.responses.GetAllFavorites;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PingedListAdapter extends RecyclerView.Adapter<PingedListAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    List<Merchant> data = new ArrayList<>();
    String geallFavourite = "";
    private ProgressDialog pd;
    private String userId, paymentId, versionId, latitude, longitude;
    FragmentPinged fragmentPinged;


    public PingedListAdapter(Context context, List<Merchant> data, String geallFavourite, FragmentPinged fragmentPinged) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.fragmentPinged = fragmentPinged;
        this.geallFavourite = geallFavourite;

    }

    public PingedListAdapter(Context context, List<Merchant> data, String geallFavourite) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.geallFavourite = geallFavourite;

    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_pinged_list, parent, false);
        final MyViewHolder holder = new MyViewHolder(view);
        int position = holder.getAdapterPosition();


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Merchant si = data.get(position);
                List<Coupon> couponslist = si.getCoupons();
                Coupon c = couponslist.get(0);
                if (!c.getPing_status().equals("0")) {
                    Intent intent = new Intent(context, OfferDetailsActivity.class);
                    intent.putExtra("merchantId", si.getMerchantId());
                    intent.putExtra("merchantDetails", new Gson().toJson(si));
                    intent.putExtra("couponDetails", new Gson().toJson(c));
                    intent.putExtra("position", position);
                    intent.putExtra("from", "pinglist");
                    intent.putExtra("message", "You have already  redeemed this coupon erlier.");
                    intent.putExtra("is_ping", "1");
                    context.startActivity(intent);
                }

            }
        });


        holder.acceptLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int position = holder.getAdapterPosition();
                final Merchant si = data.get(position);

                final DialogPlus acceptDialog = DialogPlus.newDialog(context)
                        .setContentHolder(new ViewHolder(R.layout.dialog_confirmation_layout))
                        .setGravity(Gravity.CENTER)
                        .create();
                acceptDialog.show();

                View versionView = acceptDialog.getHolderView();
                TextView titleTv = (TextView) versionView.findViewById(R.id.titleTv);
                Button yesBtn = (Button) versionView.findViewById(R.id.yesBtn);
                Button noBtn = (Button) versionView.findViewById(R.id.noBtn);
                titleTv.setText("Are you sure you want to accept this offer ? ");

                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e("In PingList", "OnClick Yes");
                        acceptDialog.dismiss();
                        Log.e("PingID", si.getCoupons().get(0).getPing_id());
                        Log.e("CouponID", si.getCoupons().get(0).getCouponId());
                        acceptPing(si.getCoupons().get(0).getPing_id(), si.getCoupons().get(0).getCouponId());
                    }
                });

                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e("In PingList", "OnClick No");
                        acceptDialog.dismiss();
                    }
                });

            }
        });

        holder.declineLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int position = holder.getAdapterPosition();
                final Merchant si = data.get(position);

                final DialogPlus acceptDialog = DialogPlus.newDialog(context)
                        .setContentHolder(new ViewHolder(R.layout.dialog_confirmation_layout))
                        .setGravity(Gravity.CENTER)
                        .create();
                acceptDialog.show();

                View versionView = acceptDialog.getHolderView();
                TextView titleTv = (TextView) versionView.findViewById(R.id.titleTv);
                Button yesBtn = (Button) versionView.findViewById(R.id.yesBtn);
                Button noBtn = (Button) versionView.findViewById(R.id.noBtn);
                titleTv.setText("Are you sure you want to Decline this offer ? ");

                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e("In PingList", "OnClick Yes");
                        acceptDialog.dismiss();
                        declinePing(si.getCoupons().get(0).getPing_id(), si.getCoupons().get(0).getCouponId());
                    }
                });

                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e("In PingList", "OnClick No");
                        acceptDialog.dismiss();
                        //declinePing(si.getCoupons().get(position).getPing_id(),si.getCoupons().get(position).getCouponId());
                    }
                });

            }
        });

        return holder;
    }

    private void acceptPing(String ping_id, String coupon_id) {
        userId = Config.getSharedPreferences(context, "userId");
        versionId = Config.getSharedPreferences(context, "versionId");
        paymentId = Config.getSharedPreferences(context, "paymentId");
        latitude = Config.getSharedPreferences(context, "latitude");
        longitude = Config.getSharedPreferences(context, "longitude");
        if (Config.isConnectedToInternet(context)) {
            pd = new ProgressDialog(context);
            pd.setCancelable(false);
            pd.setMessage("Loading...");
            pd.show();
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Log.e("In FrgmtPinged", "Params : UserId -> " + userId + ", VersionId -> " + versionId
                    + ", PaymentId -> " + paymentId + ", Latitude -> " + latitude + " n Longitude -> " + longitude);
            Call<GetAllFavorites> call = apiInterface.update_pinged_offer_status(ping_id, coupon_id, "1", userId, versionId, paymentId, latitude, longitude, Config.getSharedPreferences(context, "cityID"));
            call.enqueue(new Callback<GetAllFavorites>() {
                @Override
                public void onResponse(Call<GetAllFavorites> call, Response<GetAllFavorites> response) {
                    pd.dismiss();
                    GetAllFavorites gf = response.body();
                    Log.e("In FrgmtPinged", "Response : " + new Gson().toJson(response));
                    if (gf.getSuccess() == 1) {
//                        Intent intent=new Intent(context,HomeActivity.class);
//                        intent.putExtra("from","pingedOffer");
//                        context.startActivity(intent);
//                        ((Activity)context).finish();
//                        ((Activity)context).overridePendingTransition(0,0);
                        fragmentPinged.getPinged();

                    } else {

                    }
                }

                @Override
                public void onFailure(Call<GetAllFavorites> call, Throwable t) {
                    pd.dismiss();
                    Log.e("In FrgmtPinged", "OnFailure Msg : " + t.getMessage());
                    Config.showDialog(context, Config.FailureMsg);
                }
            });
        } else {
            //Config.showAlertForInternet(this);
        }

    }

    private void declinePing(String ping_id, String coupon_id) {
        userId = Config.getSharedPreferences(context, "userId");
        versionId = Config.getSharedPreferences(context, "versionId");
        paymentId = Config.getSharedPreferences(context, "paymentId");
        latitude = Config.getSharedPreferences(context, "latitude");
        longitude = Config.getSharedPreferences(context, "longitude");
        if (Config.isConnectedToInternet(context)) {
            pd = new ProgressDialog(context);
            pd.setCancelable(false);
            pd.setMessage("Loading...");
            pd.show();
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Log.e("In FrgmtPinged", "Params : UserId -> " + userId + ", VersionId -> " + versionId
                    + ", PaymentId -> " + paymentId + ", Latitude -> " + latitude + " n Longitude -> " + longitude);
            Call<GetAllFavorites> call = apiInterface.update_pinged_offer_status(ping_id, coupon_id, "2", userId, versionId, paymentId, latitude, longitude, Config.getSharedPreferences(context, "cityID"));
            call.enqueue(new Callback<GetAllFavorites>() {
                @Override
                public void onResponse(Call<GetAllFavorites> call, Response<GetAllFavorites> response) {
                    pd.dismiss();
                    GetAllFavorites gf = response.body();
                    Log.e("In FrgmtPinged", "Response : " + new Gson().toJson(response));
                    if (gf.getSuccess() == 1) {
//                        Intent intent=new Intent(context,HomeActivity.class);
//                        intent.putExtra("from","pingedOffer");
//                        context.startActivity(intent);
//                        ((Activity)context).finish();
//                        ((Activity)context).overridePendingTransition(0,0);
                        fragmentPinged.getPinged();
                    } else {

                    }
                }

                @Override
                public void onFailure(Call<GetAllFavorites> call, Throwable t) {
                    pd.dismiss();
                    Log.e("In FrgmtPinged", "OnFailure Msg : " + t.getMessage());
                    Config.showDialog(context, Config.FailureMsg);
                }
            });
        } else {
            //Config.showAlertForInternet(this);
        }

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Merchant si = data.get(position);
        List<OutletForOffer> ofo = si.getOutlets();
        Glide.with(context).load(si.getCompanyBannerImage()).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .bitmapTransform(new RoundedCornersTransformation(context, 10, 0, RoundedCornersTransformation.CornerType.LEFT))
                .error(R.drawable.ic_bird)
                .into(holder.imageIv);
        holder.pingedIv.setImageResource(R.drawable.ic_ping_selected);
        holder.titleTv.setText(si.getCompanyName());
        holder.reviewTv.setText(si.getAverageRatings());
//        if (si.getOutlets() != null) {
            if (si.getOutlets().size() == 1) {
                holder.outletTv.setText(si.getOutlets().get(0).getAreaName() + "");
            } else {
                holder.outletTv.setText(si.getOutlets().get(0).getAreaName() + ", + " + (si.getOutlets().size() - 1) + " More");
            }
//        }

//        if (si.getCoupons().size() > 0)
            holder.couponTv.setText(si.getCoupons().get(0).getCouponTitle() + "");
        holder.fromNameTv.setText(si.getPinged_user_name() + "");

//        if (si.getCoupons() != null) {
            if (si.getCoupons().size() > 0) {
                if (si.getCoupons().get(0).getPing_status().equals("0")) {
                    holder.acceptLayout.setVisibility(View.VISIBLE);
                    holder.declineLayout.setVisibility(View.VISIBLE);
                    holder.statusTv.setVisibility(View.GONE);
                } else {
                    holder.acceptLayout.setVisibility(View.GONE);
                    holder.declineLayout.setVisibility(View.GONE);
                    holder.statusTv.setVisibility(View.VISIBLE);
                    String statusText = "";
                    if (si.getStatus().equals("1")) {
                        statusText = "Accepted";
                    } else if (si.getStatus().equals("2")) {
                        statusText = "Declined";
                    }
                    holder.statusTv.setText(statusText);
                }
            }
//        }

//        if (ofo != null) {
            double dist = ofo.get(0).getDistance();
            for (int i = 0; i < ofo.size(); i++) {
                OutletForOffer fo = ofo.get(i);
                if (dist > fo.getDistance()) {
                    dist = fo.getDistance();
                }
            }
            holder.distanceTv.setText(dist + " km");

//        }
    }

    @Override
    public int getItemCount() {
        return (data.size());
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageIv, pingedIv;
        TextView distanceTv, titleTv, reviewTv, outletTv, couponTv, fromNameTv, statusTv;
        LinearLayout acceptLayout, declineLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageIv = (ImageView) itemView.findViewById(R.id.imageIv);
            pingedIv = (ImageView) itemView.findViewById(R.id.pingedIv);
            distanceTv = (TextView) itemView.findViewById(R.id.distanceTv);
            titleTv = (TextView) itemView.findViewById(R.id.titleTv);
            reviewTv = (TextView) itemView.findViewById(R.id.reviewTv);
            outletTv = (TextView) itemView.findViewById(R.id.outletTv);
            couponTv = (TextView) itemView.findViewById(R.id.couponTv);
            fromNameTv = (TextView) itemView.findViewById(R.id.fromNameTv);
            statusTv = (TextView) itemView.findViewById(R.id.statusTv);
            acceptLayout = (LinearLayout) itemView.findViewById(R.id.acceptLayout);
            declineLayout = (LinearLayout) itemView.findViewById(R.id.declineLayout);
        }


        public void getData() {

        }

    }

}
