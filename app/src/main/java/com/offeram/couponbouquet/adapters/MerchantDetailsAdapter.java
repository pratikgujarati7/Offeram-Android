package com.offeram.couponbouquet.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
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

import com.google.gson.Gson;
import com.offeram.couponbouquet.Config;
import com.offeram.couponbouquet.OfferDetailsActivity;
import com.offeram.couponbouquet.PingActivity;
import com.offeram.couponbouquet.R;
import com.offeram.couponbouquet.RetroApiClient;
import com.offeram.couponbouquet.RetroApiInterface;
import com.offeram.couponbouquet.models.Coupon;
import com.offeram.couponbouquet.responses.Common;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MerchantDetailsAdapter extends RecyclerView.Adapter<MerchantDetailsAdapter.MyViewHolder> {
    List<Coupon> couponList = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;
    String companyName = "", from = "", merchantDetails = "", isStarred = "0";
    private String paymentId="";


    public MerchantDetailsAdapter(Context context, List<Coupon> couponList, String from, String companyName, String merchantDetails) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.couponList = couponList;
        this.companyName = companyName;
        this.merchantDetails = merchantDetails;
        this.from = from;
    }

    public void delete(int position) {
        couponList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_coupon_list_test, parent, false);
        final MyViewHolder holder = new MyViewHolder(view);

        paymentId = Config.getSharedPreferences(context, "paymentId");
        if (paymentId != null && paymentId.equals("0")) {
            holder.pingLayout.setVisibility(View.GONE);
        } else {
            holder.pingLayout.setVisibility(View.VISIBLE);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                final Coupon c = couponList.get(position);
                Intent intent = new Intent(context, OfferDetailsActivity.class);
                intent.putExtra("merchantDetails", merchantDetails);
                intent.putExtra("couponDetails", new Gson().toJson(c));
                intent.putExtra("position", position);
                intent.putExtra("from","merchantdetail");
//                if (companyName.equals("")) {  // From Search Results
//                    intent.putExtra("companyName", c.getCompanyName());
//                } else {  // From Fragment Home
//                    intent.putExtra("companyName", companyName);
//                }
////                intent.putExtra("merchantId", ((MerchantDetailsActivity)context).merchantId);
////                if(((MerchantDetailsActivity)context).searchText != null && !((MerchantDetailsActivity)context).searchText.equalsIgnoreCase("")){
////                    intent.putExtra("searchText", ((MerchantDetailsActivity)context).searchText);
////                }
////                Log.e("In MerchantDetailsAdptr", "In OnClick MerchantId n SearchText : " + intent.getStringExtra("merchantId")
////                        + ", " + intent.getStringExtra("searchText"));
                context.startActivity(intent);
            }
        });

        holder.starLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("In MerchantDetailsAdptr", "Star OnClick");
                int position = holder.getAdapterPosition();
                final Coupon c = couponList.get(position);
                if (c.getIsUsed().equals("0")) {
                    final DialogPlus dialog = DialogPlus.newDialog(context)
                            .setContentHolder(new ViewHolder(R.layout.dialog_confirmation_layout))
                            .setContentHeight(LinearLayout.LayoutParams.WRAP_CONTENT)
                            .setCancelable(false)
                            .setGravity(Gravity.CENTER)
                            .create();
                    dialog.show();

                    View errorView = dialog.getHolderView();
                    TextView titleTv = (TextView) errorView.findViewById(R.id.titleTv);
                    Button yesBtn = (Button) errorView.findViewById(R.id.yesBtn);
                    final Button noBtn = (Button) errorView.findViewById(R.id.noBtn);
                    String message = "";
                    if (c.getIsStarred() == 0) {
                        message = "Are you sure you want to add this coupon to your wishlist ? ";
                    } else if (c.getIsStarred() == 1) {
                        message = "Are you sure you want to remove this coupon from your wishlist ? ";
                    }
                    titleTv.setText(message);

                    yesBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            if (Config.isConnectedToInternet(context)) {
                                if (c.getIsStarred() == 0) {
                                    isStarred = "1";
                                } else if (c.getIsStarred() == 1) {
                                    isStarred = "0";
                                }
                                RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
                                Call<Common> call = apiInterface.addToFavorite(Config.getSharedPreferences(context, "userId"),
                                        c.getCouponId(), isStarred);
                                call.enqueue(new Callback<Common>() {
                                    @Override
                                    public void onResponse(Call<Common> call, Response<Common> response) {
                                        Common co = response.body();
                                        if (co.getSuccess() == 1) {
                                            c.setIsStarred(Integer.parseInt(isStarred));
                                            if (c.getIsStarred() == 0) {
                                                holder.starredIv.setImageResource(R.drawable.ic_star_unselected);
                                            } else if (c.getIsStarred() == 1) {
                                                holder.starredIv.setImageResource(R.drawable.ic_star_selected);
                                            }
                                        } else {
                                            Config.showDialog(context, Config.FailureMsg);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Common> call, Throwable t) {
                                        Log.e("In MerchantDetailsAdptr", "OnFailure Msg : " + t.getMessage());
                                        Config.showDialog(context, Config.FailureMsg);
                                    }
                                });
                            } else {
                                Config.showAlertForInternet((Activity) context);
                            }
                        }
                    });

                    noBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                }
            }
        });

        holder.pingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("In MerchantDetailsAdptr", "Ping OnClick");
                int position = holder.getAdapterPosition();
                final Coupon c = couponList.get(position);
                if (c.getIsUsed().equals("0")&& c.getIs_pinged().equals("0")) {
                    Intent intent = new Intent(context, PingActivity.class);
                    intent.putExtra("couponId", c.getCouponId());
                    context.startActivity(intent);
                }
            }
        });

        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Coupon c = couponList.get(position);
        Log.e("In MerchantDetails", "In OnBind CouponId : " + c.getCouponId());

        Log.e("In MerchantDetails", "In MerchantDetailsAdptr -> couponName n from: " + c.getCompanyName() + " n " + from);
//        if (from.equals("Search Results")) {
//            holder.merchantNameTv.setVisibility(View.VISIBLE);
//            holder.merchantNameTv.setText(c.getCompanyName());
//        } else {
//            holder.merchantNameTv.setVisibility(View.GONE);
//        }
        if (c.getIsUsed().equals("0")) {
            holder.idTv.setText((position + 1) + "");
            holder.ll_idTv.setBackground(context.getResources().getDrawable(R.drawable.bg_coupon_used));
            holder.starredIv.setImageResource(R.drawable.ic_star_selected);
            holder.pingedIv.setImageResource(R.drawable.ic_ping_selected);
            if (c.getIsStarred() == 0) {
                holder.starredIv.setImageResource(R.drawable.ic_star_unselected);

            } else if (c.getIsStarred() == 1) {
                holder.starredIv.setImageResource(R.drawable.ic_star_selected);

            }
            if (c.getIs_pinged().equals("0")){
                holder.pingedIv.setImageResource(R.drawable.ic_ping_selected);
            }else if (c.getIs_pinged().equals("1")){
                holder.pingedIv.setImageResource(R.drawable.ic_ping_used);
            }
            if (c.getIs_pinged().equals("1")){
                holder.statusIdTv.setVisibility(View.VISIBLE);
                holder.statusIdTv.setText("Pinged");
            }
//            holder.idTv.setTextSize(18);
//            holder.idTv.setBackgroundColor(context.getResources().getColor(R.color.textViewBgColor));
        } else if (c.getIsUsed().equals("1")) {
            holder.idTv.setText((position + 1) + "");
            holder.ll_idTv.setBackground(context.getResources().getDrawable(R.drawable.bg_coupon_unused));
            holder.statusIdTv.setVisibility(View.VISIBLE);
            holder.starredIv.setImageResource(R.drawable.ic_star_used);
            holder.pingedIv.setImageResource(R.drawable.ic_ping_used);
            holder.statusIdTv.setText("Used");
//            holder.idTv.setBackgroundColor(context.getResources().getColor(R.color.lightGrey));
            if (c.getIs_pinged().equals("1")){
                holder.statusIdTv.setVisibility(View.VISIBLE);
                holder.statusIdTv.setText("Pinged");
            }
        }
        holder.nameTv.setText(c.getCouponTitle());
        if(c.getNumOfRedeem() == 0)
        {
            holder.redeemTv.setVisibility(View.INVISIBLE);
        } else {
            holder.redeemTv.setVisibility(View.VISIBLE);
            holder.redeemTv.setText(c.getNumOfRedeem() + " redeemed");
        }


    }

    @Override
    public int getItemCount() {
        return (couponList.size());
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        //        TextView couponIdTv, couponNameTv, merchantNameTv;
        ImageView starredIv, pingedIv;
        TextView idTv, nameTv, redeemTv,statusIdTv;
        LinearLayout starLayout, pingLayout;
        RelativeLayout ll_idTv;

        public MyViewHolder(View itemView) {
            super(itemView);
            starredIv = (ImageView) itemView.findViewById(R.id.starredIv);
            pingedIv = (ImageView) itemView.findViewById(R.id.pingedIv);
            idTv = (TextView) itemView.findViewById(R.id.idTv);
            nameTv = (TextView) itemView.findViewById(R.id.nameTv);
            redeemTv = (TextView) itemView.findViewById(R.id.redeemTv);
            statusIdTv = (TextView) itemView.findViewById(R.id.statusIdTv);
            starLayout = (LinearLayout) itemView.findViewById(R.id.starLayout);
            pingLayout = (LinearLayout) itemView.findViewById(R.id.pingLayout);
            ll_idTv = (RelativeLayout) itemView.findViewById(R.id.ll_idTv);
        }


        public void getData() {

        }

    }

}
