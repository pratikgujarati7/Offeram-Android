package com.offeram.couponbouquet.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.offeram.couponbouquet.AboutUsActivity;
import com.offeram.couponbouquet.CircleTransform;
import com.offeram.couponbouquet.Config;
import com.offeram.couponbouquet.ContactNumberActivity;
import com.offeram.couponbouquet.PingedOffers;
import com.offeram.couponbouquet.R;
import com.offeram.couponbouquet.RefferAndEarn;
import com.offeram.couponbouquet.UsedOffersActivity;
import com.offeram.couponbouquet.WebViewActivity;
import com.offeram.couponbouquet.responses.Common;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;

public class MyAccountListAdapter extends RecyclerView.Adapter<MyAccountListAdapter.MyViewHolder> {
    ArrayList<String> data = new ArrayList<>();
    int[] images = new int[]{};
    private LayoutInflater inflater;
    private Context context;
    String userdetail;


    public MyAccountListAdapter(Context context, ArrayList<String> data, int[] images,String userdetail) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.images = images;
        this.userdetail=userdetail;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_my_account_list, parent, false);
        final MyViewHolder holder = new MyViewHolder(view);

        final Common c=new Gson().fromJson(userdetail,Common.class);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Intent intent = null;
                if (data.get(position).toLowerCase().contains("refer")) {
                    intent = new Intent(context, RefferAndEarn.class);
                    intent.putExtra("refercode",c.getReferal_code());
                    context.startActivity(intent);
                }
                if (data.get(position).toLowerCase().contains("about")) {
                    intent = new Intent(context, AboutUsActivity.class);
                    context.startActivity(intent);
                }if (data.get(position).toLowerCase().contains("pinged")) {
                    intent = new Intent(context, PingedOffers.class);
                    context.startActivity(intent);
                }
                if (data.get(position).toLowerCase().contains("used")) {
                    intent = new Intent(context, UsedOffersActivity.class);
                    context.startActivity(intent);
                }
                if (data.get(position).toLowerCase().contains("terms")) {
                    intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra("title", "Terms & Conditions");
                    intent.putExtra("url", "http://offeram.com/Offeram_Terms_and_Conditions.html");
                    context.startActivity(intent);
                }
                if (data.get(position).toLowerCase().contains("rate")) {
                    intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id="
                                    + context.getPackageName()));
                    context.startActivity(intent);
                }
                if (data.get(position).toLowerCase().contains("customer")) {
                    intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "7055540333"));
                    context.startActivity(intent);
                }
                if (data.get(position).toLowerCase().contains("privacy")) {
                    intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra("title", "Privacy Policy");
                    intent.putExtra("url", "http://offeram.com/Offeram_Privacy_Policy.html");
                    context.startActivity(intent);
                }
                if (data.get(position).toLowerCase().contains("disclaimer")) {
                    intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra("title", "Disclaimer");
                    intent.putExtra("url", "http://offeram.com/Offeram_Disclaimer.html");
                    context.startActivity(intent);
                }
                if (data.get(position).toLowerCase().contains("refund")) {
                    intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra("title", "Refund & Cancellation");
                    intent.putExtra("url", "http://offeram.com/Offeram_Refund_and_Cancellation.html");
                    context.startActivity(intent);
                }if (data.get(position).toLowerCase().contains("logout")) {
                    /*intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra("title", "Refund & Cancellation");
                    intent.putExtra("url", "http://offeram.com/Offeram_Refund_and_Cancellation.html");
                    context.startActivity(intent);*/

                    final DialogPlus acceptDialog = DialogPlus.newDialog(context)
                            .setContentHolder(new ViewHolder(R.layout.dialog_confirmation_layout))
                            .setGravity(Gravity.CENTER)
                            .create();
                    acceptDialog.show();

                    View versionView = acceptDialog.getHolderView();
                    TextView titleTv = (TextView) versionView.findViewById(R.id.titleTv);
                    Button yesBtn = (Button) versionView.findViewById(R.id.yesBtn);
                    Button noBtn = (Button) versionView.findViewById(R.id.noBtn);
                    titleTv.setText("Are you sure you want to Logout ? ");

                    yesBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.e("In PingList", "OnClick Yes");
                            acceptDialog.dismiss();
                            Config.saveSharedPreferences(context,"userId",null);
                            Intent intent = new Intent(context, ContactNumberActivity.class);
                            context.startActivity(intent);
                            ((Activity)context).finish();
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

            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Glide.with(context).load(images[position]).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).transform(new CircleTransform(context)).error(R.mipmap.ic_launcher)
                .into(holder.imageIv);
        holder.titleTv.setText(data.get(position));
//        if (position == 0) {
//            holder.countTv.setVisibility(View.VISIBLE);
//            holder.countTv.setText("12");
//        } else {
//            holder.countTv.setVisibility(View.GONE);
//        }

        if (position == data.size() - 1) {
            holder.separatorView.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return (data.size());
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageIv;
        TextView titleTv, countTv;
        View separatorView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageIv = (ImageView) itemView.findViewById(R.id.imageIv);
            titleTv = (TextView) itemView.findViewById(R.id.titleTv);
            countTv = (TextView) itemView.findViewById(R.id.countTv);
            separatorView = (View) itemView.findViewById(R.id.separatorView);
        }


        public void getData() {

        }

    }

}
