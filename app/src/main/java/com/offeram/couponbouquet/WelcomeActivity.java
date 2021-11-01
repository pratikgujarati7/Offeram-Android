package com.offeram.couponbouquet;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class WelcomeActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    //    private ImageView[] dots;
    CircleImageView[] dots;
    private int[] layouts;
    private Button skipBtn, nextBtn;
    TextView skipTv, nextTv;
    View skipBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_welcome);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
//        skipBtn = (Button) findViewById(R.id.skipBtn);
//        nextBtn = (Button) findViewById(R.id.nextBtn);
        nextTv = (TextView) findViewById(R.id.nextTv);
        skipTv = (TextView) findViewById(R.id.skipTv);
        skipBottom = (View) findViewById(R.id.skipBottom);

        Config.saveSharedPreferences(WelcomeActivity.this, "showWelcomeScreen", "1");

        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.welcome_slide1,
                R.layout.welcome_slide2,
                R.layout.welcome_slide3,
                R.layout.welcome_slide4};

        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

//        skipBtn.bringToFront();
//        joinNowBtn.bringToFront();
        skipTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = null;
                if (Config.getSharedPreferences(WelcomeActivity.this, "user_id") != null) {
                    intent = new Intent(WelcomeActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(WelcomeActivity.this, ContactNumberActivity.class);
                    startActivity(intent);
                }
            }
        });

        nextTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    finish();
                    Intent intent = new Intent(WelcomeActivity.this, ContactNumberActivity.class);
                    startActivity(intent);
                }
//                finish();
//                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
//                startActivity(intent);
            }
        });
    }

    private void addBottomDots(int currentPage) {
        dots = new CircleImageView[layouts.length];

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new CircleImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(15, 15);
            layoutParams.setMargins(5, 5, 5, 5);
            dots[i].setLayoutParams(layoutParams);
            dots[i].setMaxWidth(20);
            dots[i].setMaxHeight(20);

//            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.radio_unselected));
            Drawable color = new ColorDrawable(getResources().getColor(R.color.lightPurple));
            dots[i].setImageDrawable(color);
            dotsLayout.addView(dots[i]);
        }

//        if (dots.length > 0) {
//            dots[currentPage].setImageDrawable(getResources().getDrawable(R.drawable.radio_selected));
//        }
        if (dots.length > 0) {
//            if (currentPage == 0) {
//                Drawable color = new ColorDrawable(getResources().getColor(R.color.white));
//                dots[currentPage].setImageDrawable(color);
//            } else {
                Drawable color = new ColorDrawable(getResources().getColor(R.color.darkPurple));
                dots[currentPage].setImageDrawable(color);
//            }
        }
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
//            if (position == 0) {
//                // still pages are left
//                skipBottom.setVisibility(View.GONE);
//                skipTv.setTextColor(getResources().getColor(R.color.white));
//                nextTv.setTextColor(getResources().getColor(R.color.white));
//                nextTv.setBackgroundColor(getResources().getColor(android.R.color.transparent));
//            } else
            if (position == layouts.length - 1) {
                skipBottom.setVisibility(View.VISIBLE);
                skipTv.setTextColor(getResources().getColor(R.color.darkGreyColor));
                nextTv.setTextColor(getResources().getColor(R.color.white));
                nextTv.setBackgroundColor(getResources().getColor(R.color.darkPurple));
                nextTv.setText("Register");
            } else {
                // still pages are left
                skipBottom.setVisibility(View.VISIBLE);
                skipTv.setTextColor(getResources().getColor(R.color.darkGreyColor));
                nextTv.setTextColor(getResources().getColor(R.color.white));
                nextTv.setBackgroundColor(getResources().getColor(R.color.darkPurple));
                nextTv.setText("Next");
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
