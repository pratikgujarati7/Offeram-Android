package com.offeram.couponbouquet.adapters;

import android.content.Context;
import android.graphics.Color;
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
import com.offeram.couponbouquet.models.transactionsModel;

import java.util.List;

public class TransactionHistoryAdapter extends RecyclerView.Adapter<TransactionHistoryAdapter.MyViewHolder>  {

    Context context;
    private LayoutInflater inflater;
    List<transactionsModel> list=null;
    transactionsModel data;

    public TransactionHistoryAdapter(Context context, List<transactionsModel> list) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_transaction_history, parent, false);
        final MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        data=list.get(position);
        if(position %2 == 1)
        {
            // Set a background color for ListView regular row/item
            Glide.with(context).load(R.drawable.offeram_two).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .error(R.drawable.offeram_two)
                    .into(holder.img_transaction_logo);
        }
        else
        {
            // Set the background color for alternate row/item
            Glide.with(context).load(R.drawable.offeram_one).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .error(R.drawable.offeram_one)
                    .into(holder.img_transaction_logo);
        }
        if (data.getTransaction_type().equals("0")){
            holder.txt_coin.setTextColor(context.getResources().getColor(R.color.red));
            holder.txt_coin.setText("- "+data.getTransaction_amount());
        }else {
            holder.txt_coin.setTextColor(context.getResources().getColor(R.color.green));
            holder.txt_coin.setText("+ "+data.getTransaction_amount());
        }
        holder.txt_transaction_reason.setText(data.getTransaction_reason());
        holder.txt_date.setText(data.getTransaction_date_time());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {


        private final ImageView img_transaction_logo;
        private final TextView txt_transaction_reason,txt_date,txt_coin;

        public MyViewHolder(View itemView) {
            super(itemView);
            img_transaction_logo=(ImageView)itemView.findViewById(R.id.img_transaction_logo);
            txt_transaction_reason=(TextView)itemView.findViewById(R.id.txt_transaction_reason);
            txt_date=(TextView)itemView.findViewById(R.id.txt_date);
            txt_coin=(TextView)itemView.findViewById(R.id.txt_coin);

        }

    }
}
