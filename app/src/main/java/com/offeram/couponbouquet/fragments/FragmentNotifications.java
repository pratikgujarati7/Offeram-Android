package com.offeram.couponbouquet.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.offeram.couponbouquet.Config;
import com.offeram.couponbouquet.HomeActivity;
import com.offeram.couponbouquet.R;
import com.offeram.couponbouquet.RetroApiClient;
import com.offeram.couponbouquet.RetroApiInterface;
import com.offeram.couponbouquet.adapters.AllNotificationsAdapter;
import com.offeram.couponbouquet.responses.Common;
import com.offeram.couponbouquet.responses.GetAllNotifications;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentNotifications extends Fragment {

    Toolbar toolbar;
    NestedScrollView mainLayout;
    TextView titleTv, readTv, unreadTv;
    CardView nodataCard;
    RecyclerView readList, unreadList;
    String userId = "", versionId = "", paymentId = "";
    ProgressDialog pd;
    AllNotificationsAdapter readAdapter;
    AllNotificationsAdapter unreadAdapter;
    String jsonResponse = "{\"success\":1, \"notify_unread_list\":[ { \"notification_id\":1, \"notification_title\":\"Check New Valentine's Day Offer at RestroBistro\", \"company_logo\":\"http://accreteit.com/offeram/backyard/uploads/c164.jpg\", \"from_name\":\"Team Offeram\", \"time\":\"Today, 4:56 pm\" }, { \"notification_id\":2, \"notification_title\":\"Check New Interesting Offer at Shott\", \"company_logo\":\"http://accreteit.com/offeram/backyard/uploads/c164.jpg\", \"from_name\":\"Team Offeram\", \"time\":\"Yesterday, 2:25 pm\" }, { \"notification_id\":3, \"notification_title\":\"Test just pinged you an Offer!!\", \"company_logo\":\"http://accreteit.com/offeram/backyard/uploads/c164.jpg\", \"from_name\":\"Test\", \"time\":\"Yesterday, 2:25 pm\" } ], \"notify_read_list\":[ { \"notification_id\":4, \"notification_title\":\"Check New Valentine's Day Offer at RestroBistro\", \"company_logo\":\"http://accreteit.com/offeram/backyard/uploads/c164.jpg\", \"from_name\":\"Team Offeram\", \"time\":\"Today, 4:56 pm\" }, { \"notification_id\":5, \"notification_title\":\"Check New Interesting Offer at Shott\", \"company_logo\":\"http://accreteit.com/offeram/backyard/uploads/c164.jpg\", \"from_name\":\"Team Offeram\", \"time\":\"Yesterday, 2:25 pm\" }, { \"notification_id\":6, \"notification_title\":\"Test just pinged you an Offer!!\", \"company_logo\":\"http://accreteit.com/offeram/backyard/uploads/c164.jpg\", \"from_name\":\"Test\", \"time\":\"Yesterday, 2:25 pm\" } ] }";

    public FragmentNotifications() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);
        setHasOptionsMenu(true);

        toolbar = rootView.findViewById(R.id.toolbar);
        titleTv = toolbar.findViewById(R.id.titleTv);
        titleTv.setText("Notifications");

        mainLayout = rootView.findViewById(R.id.mainLayout);
        readTv = rootView.findViewById(R.id.readTv);
        unreadTv = rootView.findViewById(R.id.unreadTv);
        nodataCard = rootView.findViewById(R.id.nodataCard);
        readList = rootView.findViewById(R.id.readList);
        unreadList = rootView.findViewById(R.id.unreadList);
        readList.setLayoutManager(new LinearLayoutManager(getActivity()));
        unreadList.setLayoutManager(new LinearLayoutManager(getActivity()));

//        ((HomeActivity) getActivity()).bottomView.findViewById(R.id.unreadIv).setVisibility(View.INVISIBLE);
//        ((ImageView) ((HomeActivity) getActivity()).bottomView.findViewById(R.id.notificationIv)).setImageResource(R.drawable.notifications_active);
        userId = Config.getSharedPreferences(getActivity(), "userId");
        versionId = Config.getSharedPreferences(getActivity(), "versionId");
        paymentId = Config.getSharedPreferences(getActivity(), "paymentId");

//        For Dynamic Data
        if (Config.isConnectedToInternet(getActivity())) {
            pd = new ProgressDialog(getActivity());
            pd.setCancelable(false);
            pd.setMessage("Loading...");
            pd.show();
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Log.e("In FrgmtNotification", "Params : UserId -> " + userId + ", VersionId -> " + versionId
                    + ", PaymentId -> " + paymentId);
            Call<GetAllNotifications> call = apiInterface.getAllNotifications(userId, versionId, paymentId);
            call.enqueue(new Callback<GetAllNotifications>() {
                @Override
                public void onResponse(Call<GetAllNotifications> call, Response<GetAllNotifications> response) {
                    pd.dismiss();
                    GetAllNotifications gn = response.body();
                    Log.e("In FrgmtNotification", "Response : " + new Gson().toJson(response));
                    if (gn.getSuccess() == 1) {
                        mainLayout.setVisibility(View.VISIBLE);
                        nodataCard.setVisibility(View.GONE);
                        if (gn.getNotifyUnreadList().size() > 0) {
                            unreadTv.setVisibility(View.VISIBLE);
                            unreadList.setVisibility(View.VISIBLE);
                            unreadAdapter = new AllNotificationsAdapter(getActivity(), gn.getNotifyUnreadList(), "0");
                            unreadList.setAdapter(unreadAdapter);
                        } else {
                            unreadTv.setVisibility(View.GONE);
                            unreadList.setVisibility(View.GONE);
                        }
                        if (gn.getNotifyReadList().size() > 0) {
                            readTv.setVisibility(View.VISIBLE);
                            readList.setVisibility(View.VISIBLE);
                            readAdapter = new AllNotificationsAdapter(getActivity(), gn.getNotifyReadList(), "1");
                            readList.setAdapter(readAdapter);
                        } else {
                            readTv.setVisibility(View.GONE);
                            readList.setVisibility(View.GONE);
                        }
                        markAsRead();

                    } else {
                        mainLayout.setVisibility(View.GONE);
                        nodataCard.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<GetAllNotifications> call, Throwable t) {
                    pd.dismiss();
                    Log.e("In FrgmtNotification", "OnFailure Msg : " + t.getMessage());
                    Config.showDialog(getActivity(), Config.FailureMsg);
                }
            });
        } else {
            Config.showAlertForInternet(getActivity());
        }

//        For static data
//        GetAllNotifications gn = new Gson().fromJson(jsonResponse, GetAllNotifications.class);
//        if (gn.getSuccess() == 1) {
//            mainLayout.setVisibility(View.VISIBLE);
//            noDataTv.setVisibility(View.GONE);
//            if (gn.getNotifyUnreadList().size() > 0) {
//                unreadTv.setVisibility(View.VISIBLE);
//                unreadList.setVisibility(View.VISIBLE);
//                unreadAdapter = new AllNotificationsAdapter(getActivity(), gn.getNotifyUnreadList(), "0");
//                unreadList.setAdapter(unreadAdapter);
//            } else {
//                unreadTv.setVisibility(View.GONE);
//                unreadList.setVisibility(View.GONE);
//            }
//            if (gn.getNotifyReadList().size() > 0) {
//                readTv.setVisibility(View.VISIBLE);
//                readList.setVisibility(View.VISIBLE);
//                readAdapter = new AllNotificationsAdapter(getActivity(), gn.getNotifyReadList(), "1");
//                readList.setAdapter(readAdapter);
//            } else {
//                readTv.setVisibility(View.GONE);
//                readList.setVisibility(View.GONE);
//            }
//        } else {
//            mainLayout.setVisibility(View.GONE);
//            noDataTv.setVisibility(View.VISIBLE);
//        }

        return rootView;
    }

    public void markAsRead() {
        if (Config.isConnectedToInternet(getActivity())) {
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Call<Common> call = apiInterface.markAsRead(userId, "0");
            call.enqueue(new Callback<Common>() {
                @Override
                public void onResponse(Call<Common> call, Response<Common> response) {
                    Common c = response.body();
                    if (c.getSuccess() == 1) {
                        Log.e("In FrgmtNotification", "Success : " + c.getSuccess());
                    } else {
                        Config.showDialog(getActivity(), c.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<Common> call, Throwable t) {
                    Log.e("In FrgmtNotification", "OnFailure Msg : " + t.getMessage());
                    Config.showDialog(getActivity(), Config.FailureMsg);
                }
            });
        } else {
            Config.showAlertForInternet(getActivity());
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.e("In FrgmtNotification", "In OnCreateOptions");
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        getActivity().onBackPressed();
        return true;
    }

}
