package com.offeram.couponbouquet.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.offeram.couponbouquet.R;
import com.offeram.couponbouquet.RoundedCornersTransformation;
import com.offeram.couponbouquet.models.Coupon;
import com.offeram.couponbouquet.models.Rating;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserReviewListAdapter extends RecyclerView.Adapter<UserReviewListAdapter.MyViewHolder> {
    List<Rating> data = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;


    public UserReviewListAdapter(Context context, List<Rating> data) {
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
        View view = inflater.inflate(R.layout.row_user_review_list, parent, false);
        final MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Rating r = data.get(position);
        holder.userNameTv.setText(r.getName());
        holder.commentTv.setText(r.getComment());
        holder.ratingBar.setRating(Float.parseFloat(r.getRating()));

        LayerDrawable stars = (LayerDrawable) holder.ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(context.getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(context.getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(context.getResources().getColor(R.color.lightGrey), PorterDuff.Mode.SRC_ATOP);

    }

    @Override
    public int getItemCount() {
        return (data.size());
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        RatingBar ratingBar;
        TextView userNameTv, commentTv;

        public MyViewHolder(View itemView) {
            super(itemView);
            userNameTv = (TextView) itemView.findViewById(R.id.userNameTv);
            commentTv = (TextView) itemView.findViewById(R.id.commentTv);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
        }


        public void getData() {

        }

    }

}
