package com.offeram.couponbouquet.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.offeram.couponbouquet.R;
import com.offeram.couponbouquet.TouchImageView;
import com.offeram.couponbouquet.models.Photo;

import java.util.List;

/**
 * Created by User on 24-04-2018.
 */

public class FullScreenAdapter extends PagerAdapter {

    Context context;
    LayoutInflater inflater;
    int position;
    List<Photo> data;


    public FullScreenAdapter(Context context, List<Photo> data, int position) {
        this.context = context;
        this.data = data;
        this.position = position;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }


    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        Log.e("In FullScreenAdptr", "Image Position : " + data.get(position).getUrl());
        final TouchImageView fullIv;
        Button closeBtn;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.row_full_image, container, false);

        fullIv = (TouchImageView) viewLayout.findViewById(R.id.fullIv);
        closeBtn = (Button) viewLayout.findViewById(R.id.closeBtn);

//        fullIv.setImageResource(img[position]);
        Glide.with(context).load(data.get(position).getUrl())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(R.drawable.ic_bird)
                .into(fullIv);

//        Note :- Static image
//        Glide.with(context).load(R.drawable.img12)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)
//                .error(R.drawable.ic_bird)
//                .into(fullIv);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) context).finish();
                fullIv.setImageResource(0);
            }
        });

        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);
    }
}
