package com.offeram.couponbouquet.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.offeram.couponbouquet.MapsActivity;
import com.offeram.couponbouquet.R;
import com.offeram.couponbouquet.models.CouponOutlet;

import java.util.ArrayList;
import java.util.List;

public class AllOutletAdapter extends RecyclerView.Adapter<AllOutletAdapter.MyViewHolder> {
    List<CouponOutlet> data = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;
    String merchantDetails;


    public AllOutletAdapter(Context context, List<CouponOutlet> data, String merchantDetails) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.merchantDetails = merchantDetails;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_outlet_list, parent, false);
        final MyViewHolder holder = new MyViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
//                CouponOutlet co = data.get(position);
//                Intent intent = new Intent(context, MerchantDetailsActivity.class);
//                intent.putExtra("merchantId", o.getMerchantId());
//                intent.putExtra("outlet", o.getNoOfOutlets());
//                context.startActivity(intent);
            }
        });

        holder.callLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                CouponOutlet co = data.get(position);
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + co.getPhoneNumber()));
                context.startActivity(callIntent);
            }
        });

        holder.mapLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                CouponOutlet co = data.get(position);
                if (co.getLatitude() != null && !co.getLatitude().equals("")
                        && co.getLongitude() != null && !co.getLongitude().equals("")) {
                    Intent intent = new Intent(context, MapsActivity.class);
                    intent.putExtra("merchantDetails", merchantDetails);
                    intent.putExtra("outletDetails", new Gson().toJson(co));
                    context.startActivity(intent);
                }
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final CouponOutlet co = data.get(position);
        holder.areaNameTv.setText(co.getAreaName());
        holder.areaAddTv.setText(co.getAddress());
        String timings = "";
        if (co.getStartTime() != null && !co.getStartTime().equals("") && co.getEndTime() != null
                && !co.getEndTime().equals("")) {
            timings = co.getStartTime() + "-" + co.getEndTime();
        }
        if (co.getStartTime2() != null && !co.getStartTime2().equals("") && co.getEndTime2() != null
                && !co.getEndTime2().equals("")) {
            timings += ", " + co.getStartTime2() + "-" + co.getEndTime2();
        }
        holder.timingsTv.setText(timings);
    }

    @Override
    public int getItemCount() {
        return (data.size());
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView areaNameTv, areaAddTv, timingsTv;
        LinearLayout callLayout, mapLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            areaNameTv = (TextView) itemView.findViewById(R.id.areaNameTv);
            areaAddTv = (TextView) itemView.findViewById(R.id.areaAddTv);
            timingsTv = (TextView) itemView.findViewById(R.id.timingsTv);
            callLayout = (LinearLayout) itemView.findViewById(R.id.callLayout);
            mapLayout = (LinearLayout) itemView.findViewById(R.id.mapLayout);
        }


        public void getData() {

        }

    }

}
