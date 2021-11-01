package com.offeram.couponbouquet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.offeram.couponbouquet.models.CouponOutlet;
import com.offeram.couponbouquet.models.Merchant;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    Toolbar toolbar;
    private GoogleMap mMap;
    ImageView imageIv, starredIv;
    TextView toolbarTitleTv, titleTv1, reviewTv, outletTv, addressTv, timingsTv;
    LinearLayout googleMapsLayout;
    CouponOutlet co;
    Merchant m;
    String timings = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent intent = getIntent();
        if (intent != null) {
            m = new Gson().fromJson(intent.getStringExtra("merchantDetails"), Merchant.class);
            co = new Gson().fromJson(intent.getStringExtra("outletDetails"), CouponOutlet.class);
        }

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTitleTv = toolbar.findViewById(R.id.titleTv);
        toolbarTitleTv.setText(m.getCompanyName());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        googleMapsLayout = findViewById(R.id.googleMapsLayout);
        titleTv1 = findViewById(R.id.titleTv1);
        reviewTv = findViewById(R.id.reviewTv);
        outletTv = findViewById(R.id.outletTv);
        addressTv = findViewById(R.id.addressTv);
        timingsTv = findViewById(R.id.timingsTv);
        imageIv = findViewById(R.id.imageIv);
        starredIv = findViewById(R.id.starredIv);

        if (co.getStartTime() != null && !co.getStartTime().equals("") && co.getEndTime() != null
                && !co.getEndTime().equals("")) {
            timings = co.getStartTime() + "-" + co.getEndTime();
        }
        if (co.getStartTime2() != null && !co.getStartTime2().equals("") && co.getEndTime2() != null
                && !co.getEndTime2().equals("")) {
            String timingStr = co.getStartTime2() + "-" + co.getEndTime2();
            if (!timingStr.equals(timings))
                timings += ", " + timingStr;
        }
        Glide.with(MapsActivity.this).load(m.getCompanyLogo())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .bitmapTransform(new RoundedCornersTransformation(MapsActivity.this, 10, 0, RoundedCornersTransformation.CornerType.LEFT))
                .error(R.drawable.ic_bird)
                .into(imageIv);
//        Log.e("In MapsActivity", "Title : " + m.getCompanyName());
        titleTv1.setText(m.getCompanyName());
        reviewTv.setText(m.getAverageRatings());
        outletTv.setText(co.getAreaName());
        addressTv.setText(co.getAddress());
        timingsTv.setText(timings);

        googleMapsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + co.getLatitude() + "," + co.getLongitude() + "(" + m.getCompanyName() + ")");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);
        // Add a marker in Sydney and move the camera
        LatLng latLong = new LatLng(Double.parseDouble(co.getLatitude()), Double.parseDouble(co.getLongitude()));
        mMap.addMarker(new MarkerOptions().position(latLong).title(m.getCompanyName()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLong));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLong, 15));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
