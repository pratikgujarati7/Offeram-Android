package com.offeram.couponbouquet.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.gson.Gson;
import com.offeram.couponbouquet.BuyNowActivity;
import com.offeram.couponbouquet.Config;
import com.offeram.couponbouquet.GridSpacingItemDecoration;
import com.offeram.couponbouquet.R;
import com.offeram.couponbouquet.RetroApiClient;
import com.offeram.couponbouquet.RetroApiInterface;
import com.offeram.couponbouquet.SearchResults;
import com.offeram.couponbouquet.WebViewActivity;
import com.offeram.couponbouquet.adapters.AreaListAdapter;
import com.offeram.couponbouquet.adapters.CityAdapter;
import com.offeram.couponbouquet.models.Area;
import com.offeram.couponbouquet.models.Category;
import com.offeram.couponbouquet.models.City;
import com.offeram.couponbouquet.models.CityModel;
import com.offeram.couponbouquet.responses.Common;
import com.offeram.couponbouquet.responses.GetAllOffers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar;


public class FragmentOffers extends Fragment {

    public ArrayList<String> selectedAreaList = new ArrayList<>();
    public ArrayList<String> tempList = new ArrayList<>();
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    SearchView searchView;
    SimpleCursorAdapter searchAdapter;
    AreaListAdapter adapter;
    CityAdapter cityadapter;
    int count = 0, inResume = 0, isSpinnerSelected = 0;
    List<Category> categoryList;
    List<String> seekIntervalsList = new ArrayList<>();
    List<Area> list = new ArrayList<>();
    List<City> citylist = new ArrayList<>();
    String splashResponse = "", selectedAreaStr = "";
    ArrayList<String> suggestItems = new ArrayList<>();
    Dialog filterDialog;
    String type = "", userId = "", versionId = "", paymentId = "", latitude = "", longitude = "";
    String add;
    private String cityID;
    private String strCityID;
    private String strCityName;
    private String suggetionList;
    private String strcityName;
    private TextView txt_cityname;
    private RelativeLayout rel_tamboola;
    private TextView txt_title, txt_desc, txt_button, txt_button_know_more;
    private ImageView img_close, tambola_image;
    private ProgressDialog pd;

    public FragmentOffers() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_offers, container, false);
        setHasOptionsMenu(true);

//        initToolbar();
        toolbar = rootView.findViewById(R.id.toolbar);
        txt_cityname = (TextView) toolbar.findViewById(R.id.txt_cityname);
        tabLayout = rootView.findViewById(R.id.tabLayout);
        viewPager = rootView.findViewById(R.id.viewPager);
        txt_title = (TextView) rootView.findViewById(R.id.txt_title);
        txt_desc = (TextView) rootView.findViewById(R.id.txt_desc);
        txt_button = (TextView) rootView.findViewById(R.id.txt_button);
        txt_button_know_more = (TextView) rootView.findViewById(R.id.txt_button_know_more);
        txt_button_know_more.setVisibility(View.GONE);
        img_close = (ImageView) rootView.findViewById(R.id.img_close);
        tambola_image = (ImageView) rootView.findViewById(R.id.tambola_image);
        rel_tamboola = (RelativeLayout) rootView.findViewById(R.id.rel_tamboola);

//        Config.saveSharedPreferences(getActivity(), "userId", "1");
//        Config.saveSharedPreferences(getActivity(), "versionId", "15");
//        Config.saveSharedPreferences(getActivity(), "paymentId", "0");
        paymentId = Config.getSharedPreferences(getActivity(), "paymentId");

        Config.saveSharedPreferences(getActivity(), "isFilterForCallApi", "1");

        splashResponse = Config.getSharedPreferences(getActivity(), "splashResponse");
        latitude = Config.getSharedPreferences(getActivity(), "latitude");
        longitude = Config.getSharedPreferences(getActivity(), "longitude");
        if (Config.getSharedPreferences(getActivity(), "cityID") == null) {
            if (!latitude.equals("0") && !longitude.equals("0")) {
                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1);
                    Address obj = addresses.get(0);
                    add = obj.getLocality();
                    txt_cityname.setText(add);
                    Log.e("Address:", add);
                    Common cityjson = new Gson().fromJson(Config.getSharedPreferences(getActivity(), "splashResponse"), Common.class);

                    if (cityjson.getAllCity().size() > 0) {
                        for (int i = 0; i < cityjson.getAllCity().size(); i++) {
                            City city = cityjson.getAllCity().get(i);
                            strcityName = city.getCity_name();
                            if (strcityName.equals(add)) {
                                cityID = city.getCity_id();
                                Config.saveSharedPreferences(getActivity(), "cityID", cityID);
                                Config.saveSharedPreferences(getActivity(), "cityName", add);
                            }
                        }
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                txt_cityname.setText("Surat");
                Common cityjson = new Gson().fromJson(Config.getSharedPreferences(getActivity(), "splashResponse"), Common.class);
                if (cityjson.getAllCity().size() > 0) {
                    for (int i = 0; i < cityjson.getAllCity().size(); i++) {
                        City city = cityjson.getAllCity().get(i);
                        strcityName = city.getCity_name();
                        if (strcityName.equals("Surat")) {
                            cityID = city.getCity_id();
                            Config.saveSharedPreferences(getActivity(), "cityID", cityID);
                            Config.saveSharedPreferences(getActivity(), "cityName", "Surat");
                        }
                    }
                }
            }

        } else {
            txt_cityname.setText(Config.getSharedPreferences(getActivity(), "cityName"));
        }

        //GetAllOffers merchantResponse = Config.getSharedPreferences(getActivity(), "splashResponse");
        selectedAreaStr = Config.getSharedPreferences(getActivity(), "selectedAreaStr");
        Common c = new Gson().fromJson(splashResponse, Common.class);

//        list = c.getAllAreas();
        citylist = c.getAllCity();
        for (int i = 0; i < citylist.size(); i++) {
            City city = citylist.get(i);
            strCityID = city.getCity_id();
            if (strCityID.equals(Config.getSharedPreferences(getActivity(), "cityID"))) {
                list = city.getAll_areas();
            }

        }

        categoryList = new ArrayList<>();
        if (c.getCategories().size() > 0) {
            // For static data
            /* Category cat = new Category();
            cat.setCategoryId("0");
            cat.setCategoryName("All");
            categoryList.add(cat);
            cat = new Category();
            cat.setCategoryId("0");
            cat.setCategoryName("Special Offers");
            categoryList.add(cat); */
            for (int i = 0; i < c.getCategories().size(); i++) {
                categoryList.add(c.getCategories().get(i));
            }
            count = categoryList.size();
            Log.e("In FrgmtOffers", "Count : " + count);
            setupViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager);
            setUpTabIcons();

        }

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.e("In FrgmtOffers", "In OnTabSelected");
                View view = tab.getCustomView();
                TextView tabTextTv = view.findViewById(R.id.tabTextTv);
                tabTextTv.setTextColor(getActivity().getResources().getColor(R.color.darkPurple));
                int tabPos = tabLayout.getSelectedTabPosition();
                if (tabPos != 0) {
                    Config.saveSharedPreferences(getActivity(), "isFilterForCallApi", "0");
                } else {
                    Config.saveSharedPreferences(getActivity(), "isFilterForCallApi", "1");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                TextView tabTextTv = view.findViewById(R.id.tabTextTv);
                tabTextTv.setTextColor(getActivity().getResources().getColor(R.color.textColorGrey));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle menu item click event
//                Log.e("In FrgmtOffers", "In Toolbar OnMenuClick");
                int itemId = item.getItemId();
                if (itemId == R.id.action_filter) {
//                    Log.e("In FrgmtOffers", "On Filter icon click");
                    showFilterDialog();
                } else if (itemId == R.id.action_search) {

                }
                return true;
            }
        });

//        if (filterDialog != null && !filterDialog.isShowing()) {
//            Log.e("In FrgmtOffers", "Filter Dialog not Showing");
//            getActivity().getSupportFragmentManager().beginTransaction().
//                    detach(FragmentOffers.this).attach(FragmentOffers.this).commit();
//        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
//        Log.e("In FrgmtOffers", "In OnResume");
//        ++inResume;
//        if(inResume > 1) {
//            getActivity().getSupportFragmentManager().beginTransaction().
//                detach(FragmentOffers.this).attach(FragmentOffers.this).commit();
//        }
        getTambolaStatus();
    }

    private void getTambolaStatus() {
        if (Config.isConnectedToInternet(getActivity())) {
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Call<Common> call = apiInterface.get_tambola(Config.getSharedPreferences(getActivity(), "userId"));
            call.enqueue(new Callback<Common>() {
                @Override
                public void onResponse(Call<Common> call, Response<Common> response) {
                    final Common c = response.body();
                    if (c != null) {
                        if (c.getSuccess() == 1) {

                            if (Config.getSharedPreferences(getActivity(), "IsClosedTambolaAlert") != null) {
                                if (Config.getSharedPreferences(getActivity(), "IsClosedTambolaAlert").equals("0") && c.getIs_tambola_active().equals("1")) {
                                    rel_tamboola.setVisibility(View.VISIBLE);
                                    txt_title.setText(c.getTambola_title());
                                    txt_desc.setText(c.getTambola_desc());
//                            if (Config.getSharedPreferences(getActivity(), "isForRegisterTambola").equals("0")) {
                                    if (c.getIs_tambola_register().equals("0")) {
                                        txt_button.setText("Click here to register for Tambola");
                                    } else {
                                        txt_button.setText("View your ticket");
                                    }
                                    txt_button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (txt_button.getText().toString().equals("View your ticket")) {
                                                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                                                intent.putExtra("title", "Tambola Ticket");
                                                intent.putExtra("url", c.getTicket_url());
                                                startActivity(intent);

                                            } else if (txt_button.getText().toString().equals("Click here to register for Tambola")) {
                                                addRegister();
                                            }
                                        }
                                    });
//                                    txt_button_know_more.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            Intent i = new Intent(Intent.ACTION_VIEW);
//                                            i.setData(Uri.parse(c.getTambola_detail_url()));
//                                            startActivity(i);
//                                        }
//                                    });
                                    Glide.with(getActivity())
                                            .load(c.getTambola_image_url())
                                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                                            .skipMemoryCache(true)
                                            .into(tambola_image);
                                    img_close.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Config.saveSharedPreferences(getActivity(), "IsClosedTambolaAlert", "1");
                                            collapse(rel_tamboola);
                                            //rel_tamboola.setVisibility(View.GONE);
                                        }
                                    });
                                } else if (Config.getSharedPreferences(getActivity(), "IsClosedTambolaAlert").equals("0") && c.getIs_ipl_active().equals("1")) {

                                    if (paymentId != null && paymentId.equals("0")) {

                                        rel_tamboola.setVisibility(View.VISIBLE);
//                                        txt_button_know_more.setVisibility(View.GONE);
                                        txt_button.setText("Buy Now");
                                        txt_title.setText(c.getTambola_title());
                                        txt_desc.setText(c.getTambola_desc());
//                            if (Config.getSharedPreferences(getActivity(), "isForRegisterTambola").equals("0")) {
                                        txt_button.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (txt_button.getText().toString().equals("Buy Now")) {
                                                    Intent intent = new Intent(getActivity(), BuyNowActivity.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                                        Glide.with(getActivity())
                                                .load(c.getTambola_image_url())
                                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                                .skipMemoryCache(true)
                                                .into(tambola_image);
                                        img_close.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Config.saveSharedPreferences(getActivity(), "IsClosedTambolaAlert", "1");
                                                collapse(rel_tamboola);
                                                //rel_tamboola.setVisibility(View.GONE);
                                            }
                                        });

                                    }else {

                                    }


                                } else {
                                    rel_tamboola.setVisibility(View.GONE);
                                }
                            } else {
                                rel_tamboola.setVisibility(View.GONE);
                            }

                        } else {

                        }
                    }
                }

                @Override
                public void onFailure(Call<Common> call, Throwable t) {

                }
            });
        } else {
            Config.showAlertForInternet(getActivity());
        }
    }

    private void addRegister() {
        if (Config.isConnectedToInternet(getActivity())) {
            pd = new ProgressDialog(getActivity());
            pd.setCancelable(false);
            pd.setMessage("Loading...");
            pd.show();
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Call<Common> call = apiInterface.register_for_tambola(Config.getSharedPreferences(getActivity(), "userId"));
            call.enqueue(new Callback<Common>() {
                @Override
                public void onResponse(Call<Common> call, Response<Common> response) {
                    pd.dismiss();
                    Common c = response.body();
                    if (c.getSuccess() == 1) {
                        txt_button.setText("View your ticket");
                        Intent intent = new Intent(getActivity(), WebViewActivity.class);
                        intent.putExtra("title", "Tambola Ticket");
                        intent.putExtra("url", c.getTicket_url());
                        startActivity(intent);
                    } else {

                    }
                }

                @Override
                public void onFailure(Call<Common> call, Throwable t) {
                    pd.dismiss();

                }
            });
        } else {
            Config.showAlertForInternet(getActivity());
        }
    }

    private void setUpTabIcons() {
        for (int i = 0; i < count; i++) {
            LinearLayout ll = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.layout_custom_tab, null);
            ImageView tabImageIv = ll.findViewById(R.id.tabImageIv);
            TextView tabTextTv = ll.findViewById(R.id.tabTextTv);
            tabTextTv.setText(categoryList.get(i).getCategoryName());
            if (i != 0) {
//                tabTextTv.setCompoundDrawablesWithIntrinsicBounds(categoryImage[i], 0, 0, 0);
                tabImageIv.setVisibility(View.VISIBLE);
                Glide.with(getActivity()).load(categoryList.get(i).getCategoryImage())
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .error(R.drawable.bird)
                        .into(tabImageIv);
            } else {
                tabImageIv.setVisibility(View.GONE);
            }
            tabLayout.getTabAt(i).setCustomView(ll);
        }
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        for (int i = 0; i < count; i++) {
            Bundle bundle = new Bundle();
//            if (i == 0 || i == 1) {  // For temporary special offers is removed as it is not implemented
            if (i == 0) {
                bundle.putString("type", categoryList.get(i).getCategoryName());
//            } else if (i < count && i > 1) {
            } else if (i < count && i > 0) {  // For temporary special offers is removed as it is not implemented
                bundle.putString("type", categoryList.get(i).getCategoryId());
            }
            Fragment fragment = new FragmentAllOffers();
            fragment.setArguments(bundle);
            adapter.addFrag(fragment, categoryList.get(i).getCategoryName());
        }
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        Log.e("In FrgmtOffers", "In OnCreateOptions");
        super.onCreateOptionsMenu(menu, inflater);
//        menu.clear();
//        inflater.inflate(R.menu.menu_home_activity, menu);
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_home_activity);
        searchView = (SearchView) toolbar.getMenu().findItem(R.id.action_search).getActionView();

        final int searchImgId = android.support.v7.appcompat.R.id.search_button;

        ImageView v = (ImageView) searchView.findViewById(searchImgId);
        v.setImageResource(R.drawable.ic_search);

        final String[] from = new String[]{"fishName"};
        final int[] to = new int[]{R.id.searchValueTv};
        searchAdapter = new SimpleCursorAdapter(getActivity(), R.layout.row_search_by_coupon_list, null,
                from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        searchView.setSuggestionsAdapter(searchAdapter);
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionClick(int position) {
                // Add clicked text to search box
                CursorAdapter ca = searchView.getSuggestionsAdapter();
                Cursor cursor = ca.getCursor();
                cursor.moveToPosition(position);
                Log.e("In FrgmtOffers", "OnSuggestClick : " + cursor.getString(cursor.getColumnIndex("fishName")));
                searchView.setQuery(cursor.getString(cursor.getColumnIndex("fishName")), false);
                searchView.setQuery("", false);
                searchView.clearFocus();
                searchView.setIconified(true);
                Intent intent = new Intent(getActivity(), SearchResults.class);
                intent.putExtra("searchText", cursor.getString(cursor.getColumnIndex("fishName")));
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onSuggestionSelect(int position) {
                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("In FrgmtOffers", "OnQueryTextSubmit : " + query);
                searchView.setQuery("", false);
                searchView.clearFocus();
                searchView.setIconified(true);
                Intent intent = new Intent(getActivity(), SearchResults.class);
                intent.putExtra("searchText", query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                final MatrixCursor mc = new MatrixCursor(new String[]{BaseColumns._ID, "fishName"});
                if (suggestItems.size() > 0) {
                    Log.e("In FrgmtOffers", "OnQueryTextChange : " + s);
                    for (int i = 0; i < suggestItems.size(); i++) {
                        if (suggestItems.get(i).toLowerCase().contains(s.toLowerCase()))
                            mc.addRow(new Object[]{i, suggestItems.get(i)});
                    }
                    searchAdapter.changeCursor(mc);
                }


                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Config.getSharedPreferences(getActivity(), "allMerchantsResponse") != null) {
                    suggestItems.clear();
                    suggetionList = Config.getSharedPreferences(getActivity(), "allMerchantsResponse");
                    GetAllOffers gao = new Gson().fromJson(suggetionList, GetAllOffers.class);
                    List<String> suggestList = gao.getAllSuggestions();
                    suggestItems.addAll(suggestList);
                    Log.e("In FrgmtOffers", "Suggest Items Count : " + suggestItems.size());
                }

                searchView.requestFocusFromTouch();
                toolbar.getMenu().findItem(R.id.action_filter).setVisible(false);
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                toolbar.getMenu().findItem(R.id.action_filter).setVisible(true);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        Log.e("In FrgmtOffers", "In OnOptions");
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        Log.e("In FrgmtOffers", "In OnOptions");
//        int itemId = item.getItemId();
//        if (itemId == R.id.action_filter) {
//            Log.e("In FrgmtOffers", "In Options Filter");
////            showFilterDialog();
//        } else {
//            getActivity().onBackPressed();
//        }
//        return true;
//    }

    public void showFilterDialog() {
//        Log.e("In FrgmtOffers", "In Filter Dialog");
//        DialogPlus dialog = DialogPlus.newDialog(getActivity())
//                .setGravity(Gravity.CENTER)
//                .setContentHolder(new ViewHolder(R.layout.dialog_filter_layout))
//                .create();
//        dialog.show();
//        View dialogView = dialog.getHolderView();

        isSpinnerSelected = 0;
        filterDialog = new Dialog(getActivity());
        filterDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        filterDialog.setContentView(R.layout.dialog_filter_layout);

        final RecyclerView areaList = filterDialog.findViewById(R.id.areaList);
        final TextView progressTv = filterDialog.findViewById(R.id.progressTv);
        final Spinner city_spinner = filterDialog.findViewById(R.id.city_spinner);
        TextView filterBtn = filterDialog.findViewById(R.id.filterBtn);
        LinearLayout clearLayout = filterDialog.findViewById(R.id.clearLayout);

        final Common cityjson = new Gson().fromJson(Config.getSharedPreferences(getActivity(), "splashResponse"), Common.class);
        //list = cityjson.getAllAreas();
        if (selectedAreaStr != null && !selectedAreaStr.equalsIgnoreCase("")) {
            selectedAreaList = new ArrayList<>(Arrays.asList(selectedAreaStr.split(",")));
        }
        Log.e("In FragmentOffers", "In Dialog Arraylist : " + selectedAreaList);
        adapter = new AreaListAdapter(getActivity(), list, FragmentOffers.this);
        areaList.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
        int selectedposition = 0;

        if (cityjson != null) {
            if (cityjson.getAllCity().size() > 0) {
                //Log.e("cityName",Config.getSharedPreferences(getActivity(),"cityName"));
                ArrayList<CityModel> objects = new ArrayList<CityModel>();
                for (int k = 0; k < cityjson.getAllCity().size(); k++) {
                    CityModel obj = new CityModel();
                    obj.setAll(cityjson.getAllCity().get(k).getCity_name());
                    objects.add(obj);
                    if (Config.getSharedPreferences(getActivity(), "cityName").equals(cityjson.getAllCity().get(k).getCity_name())) {
                        selectedposition = k;
                    }
                }

                cityadapter = new CityAdapter(getActivity(), objects);
                city_spinner.setAdapter(cityadapter);
                city_spinner.setSelection(selectedposition);
            }
        }

        city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ++isSpinnerSelected;
                if (isSpinnerSelected > 1) {
                    tempList.clear();
                    selectedAreaList.clear();
                }
                City obj = cityjson.getAllCity().get(position);
                cityID = obj.getCity_id();
                strCityName = obj.getCity_name();
//                Log.e("In FrgmtOffers", "In SpinnerSelected cityID : " + cityID + "");
                for (int i = 0; i < citylist.size(); i++) {
                    City city = citylist.get(i);
                    strCityID = city.getCity_id();

                    if (cityID.equals(strCityID)) {
                        list = city.getAll_areas();
                        adapter = new AreaListAdapter(getActivity(), list, FragmentOffers.this);
                        areaList.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        RangeSeekBar<Integer> rangeSeekBar = filterDialog.findViewById(R.id.rangeSeekBar);
//        seekIntervalsList = getIntervals();
//        seekBarInterval.setIntervals(seekIntervalsList);
//        RangeSeekBar<Integer> rangeSeekBar = new RangeSeekBar<Integer>(getActivity());
//        rangeSeekBar.setRangeValues(0, 100);
//        rangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
//            @Override
//            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
//                Log.e("In FrgmtOffers", "Min Val : " + minValue + " n Max Val : " + maxValue);
//            }
//        });

        final CrystalRangeSeekbar rangeSeekBar = filterDialog.findViewById(R.id.rangeSeekBar);
        rangeSeekBar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
//                int xPos = ((seekBar.getRight() - seekBar.getLeft()) * seekBar
//                        .getProgress()) / seekBar.getMax();
                Log.e("In FrgmtOffers", "Min Val : " + minValue + " n Max Val : " + maxValue);
                progressTv.setText(minValue + "");
            }
        });

        areaList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        areaList.addItemDecoration(new GridSpacingItemDecoration(2, 20, false));

        clearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempList.clear();
                selectedAreaList.clear();
                adapter = new AreaListAdapter(getActivity(), list, FragmentOffers.this);
                areaList.setAdapter(adapter);
            }
        });

        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Config.saveSharedPreferences(getActivity(), "cityID", cityID);
                Config.saveSharedPreferences(getActivity(), "cityName", strCityName);
                selectedAreaList = tempList;
                if (selectedAreaList.size() > 0) {
                    String selectedAreaStr = "";
                    for (String s : selectedAreaList) {
                        selectedAreaStr += s + ",";
                    }
                    selectedAreaStr = selectedAreaStr.substring(0, selectedAreaStr.length() - 1);
//                    Log.e("In FilterDialog", "On Btn Click : " + selectedAreaStr);
                    Config.saveSharedPreferences(getActivity(), "selectedAreaStr", selectedAreaStr);
                } else {
                    Config.saveSharedPreferences(getActivity(), "selectedAreaStr", null);
                }
                filterDialog.dismiss();

                Config.saveSharedPreferences(getActivity(), "isFilterForCallApi", "1");
                getActivity().getSupportFragmentManager().beginTransaction().
                        detach(FragmentOffers.this).attach(FragmentOffers.this).commit();


//                else {
//                    final Dialog dialog = new Dialog(getActivity());
//                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    dialog.setContentView(R.layout.dialog_ok_layout);
//
//                    TextView titleTv = dialog.findViewById(R.id.titleTv);
//                    TextView messageTv = dialog.findViewById(R.id.messageTv);
//                    Button okBtn = dialog.findViewById(R.id.okBtn);
//                    titleTv.setVisibility(View.GONE);
//
//                    messageTv.setText("Must select items to filter the data");
//                    okBtn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            dialog.dismiss();
//                        }
//                    });
//
//                    int width = (int) (getActivity().getResources().getDisplayMetrics().widthPixels * 0.8);
//                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
//                    dialog.getWindow().setLayout(width, height);
//
//                    dialog.show();
//                }
            }
        });

        int width = (int) (getActivity().getResources().getDisplayMetrics().widthPixels * 0.95);
        //int height = (int) (getActivity().getResources().getDisplayMetrics().heightPixels * 0.9);
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        filterDialog.getWindow().setLayout(width, height);

        filterDialog.show();
    }

    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }
        return 0;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

//    public List<String> getIntervals() {
//        return new ArrayList<String>() {{
//            add("1");
//            add("aaa");
//            add("3");
//            add("bbb");
//            add("5");
//            add("ccc");
//            add("7");
//            add("ddd");
//            add("9");
//        }};
//    }

//    class TouchListener implements View.OnTouchListener {
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            Log.e("In FrgmtOffers", "In If");
//            if (event.getAction() == MotionEvent.ACTION_UP) {
//                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT); //See note below
//                params.leftMargin = (int) event.getX() - v.getLeft();
//                params.topMargin = (int) event.getY() - v.getTop();
//                TextView textTv = new TextView(getActivity());
//                textTv.setLayoutParams(params);
//                textTv.setText("Hii");
//                textTv.setTextColor(getActivity().getResources().getColor(R.color.black));
//                filterDialog.getWindow().getContainer().addContentView(textTv, params);
//                return true;
//            }
//            return true;
//        }
//    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

}
