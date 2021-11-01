package com.offeram.couponbouquet;

import com.offeram.couponbouquet.models.AllOffer;
import com.offeram.couponbouquet.models.Merchant;
import com.offeram.couponbouquet.models.UsedOffer;
import com.offeram.couponbouquet.responses.AllMerchants;
import com.offeram.couponbouquet.responses.AllUsedCoupons;
import com.offeram.couponbouquet.responses.Common;
import com.offeram.couponbouquet.responses.GenerateChecksum;
import com.offeram.couponbouquet.responses.GetAllFavorites;
import com.offeram.couponbouquet.responses.GetAllNotifications;
import com.offeram.couponbouquet.responses.GetAllOffers;
import com.offeram.couponbouquet.responses.GetSearchedResult;
import com.offeram.couponbouquet.responses.Otp;
import com.offeram.couponbouquet.responses.PromoDiscount;
import com.offeram.couponbouquet.responses.PurchaseVersion;
import com.offeram.couponbouquet.responses.TransactionResponse;
import com.offeram.couponbouquet.responses.Verify;
import com.offeram.couponbouquet.responses.Versions;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface RetroApiInterface {

    @GET("splash.php")
    public Call<Common> getDataOnSplash(@Query("user_id") String userId);

    @GET("otp.php")
    public Call<Otp> getOtp(@Query("mobile") String mobileNumber, @Query("device_id") String deviceId,
                            @Query("device_type") String deviceType, @Query("is_resend") String isResend, @Query("city_id") String city_id);

    @GET("register_for_tambola.php")
    public Call<Common> register_for_tambola(@Query("user_id") String userId);

    @GET("get_tambola.php")
    public Call<Common> get_tambola(@Query("user_id") String userId);

    @GET("get_transactions.php")
    public Call<TransactionResponse> getTransactionHistory(@Query("user_id") String userId);

    @GET("verify.php")
    public Call<Verify> verifyOtp(@Query("user_id") String userId,@Query("otp") String otp);

    @GET("getallmerchants.php")
    public Call<AllMerchants> getAllMerchants(@Query("user_id") String userId, @Query("version_id") String versionId,
                                              @Query("payment_id") String paymentId);

    @FormUrlEncoded
    @POST("getallmerchants.php")
    public Call<AllMerchants> getAllMerchantsByArea(@Query("user_id") String userId, @Query("version_id") String versionId,
                                                    @Query("payment_id") String paymentId,
                                                    @Field("area_ids[]") String[] areaId);

    @GET("search.php")
    public Call<GetSearchedResult> searchByCouponOrCompany(@Query("user_id") String userId,
                                                           @Query("version_id") String versionId,
                                                           @Query("payment_id") String paymentId,
                                                           @Query("value") String searchValue,
                                                           @Query("latitude") String latitude,
                                                           @Query("longitude") String longitude,
                                                           @Query("city_id") String city_id);

    @GET("getmerchantdetails.php")
    public Call<Merchant> getMerchantDetails(@Query("user_id") String userId, @Query("version_id") String versionId,
                                             @Query("payment_id") String paymentId, @Query("merchant_id") String merchantId,
                                             @Query("latitude") String latitude,
                                             @Query("longitude") String longitude, @Query("city_id") String city_id);

    @GET("redeem.php")
    public Call<Common> redeemAmountByPin(@Query("user_id") String userId, @Query("coupon_id") String couponId,
                                          @Query("payment_id") String paymentId, @Query("pin") String pin,@Query("is_pinged") String is_pinged,@Query("ping_id") String ping_id);

    @GET("get_all_used_coupons_by_user_id.php")
    public Call<AllUsedCoupons> getAllUsedCoupons(@Query("user_id") String userId, @Query("payment_id") String paymentId, @Query("city_id") String city_id);

    @GET("get_all_versions_by_user_id.php")
    public Call<Versions> getVersions(@Query("user_id") String userId);

    @GET("get_promotional_code_discount.php")
    public Call<PromoDiscount> getPromoCodeDiscount(@Query("user_id") String userId,
                                                    @Query("version_id") String versionId,
                                                    @Query("promotional_code") String promoCode);

    @GET("logout.php")
    public Call<Common> logout(@Query("user_id") String userId);

    @GET("payment.php")
    public Call<Common> getOrderId(@Query("user_id") String userId, @Query("version_id") String versionId,
                                   @Query("amount") String payableAmount,
                                   @Query("promotional_code") String promotionalCode);

    @GET("generateChecksum.php")
    public Call<GenerateChecksum>
    generateChecksum(@Query("ORDER_ID") String orderId, @Query("TXN_AMOUNT") String amount,
                     @Query("CUST_ID") String custId, @Query("CONTACT_NUMBER") String contactNumber);

    @FormUrlEncoded
    @POST("generateChecksum.php")
    public Call<GenerateChecksum>
    generateChecksumWithPromoCode(@Field("ORDER_ID") String orderId, @Field("TXN_AMOUNT") String amount,
                                  @Field("CUST_ID") String custId, @Field("PROMO_CAMP_ID") String promoCode);

    @GET("purchase_version.php")
    public Call<PurchaseVersion> purchaseVersion(@Query("user_id") String userId, @Query("version_id") String versionId,
                                                 @Query("payment_amount") String payableAmount, @Query("promotional_code") String promotionalCode, @Query("order_id") String orderId, @Query("transaction_id") String transactionId);

    @GET("getallmerchants_new.php")
    public Call<GetAllOffers> getAllOffers(@Query("user_id") String userId, @Query("payment_id") String paymentId,
                                           @Query("version_id") String versionId, @Query("latitude") String latitude,
                                           @Query("longitude") String longitude, @Query("city_id") String city_id);

    @FormUrlEncoded
    @POST("getallmerchants_new.php")
    public Call<GetAllOffers> getAllOffersByArea(@Query("user_id") String userId, @Query("payment_id") String paymentId,
                                                 @Query("version_id") String versionId, @Query("latitude") String latitude,
                                                 @Query("longitude") String longitude,
                                                 @Field("area_ids[]") String[] area_ids, @Query("city_id") String city_id);


    @GET("add_rating.php")
    public Call<Common> addReview(@Query("user_id") String userId, @Query("rating") String rating,
                                  @Query("comment") String comment,
                                  @Query("merchant_id") String merchantId);

    @GET("addfavorites.php")
    public Call<Common> addToFavorite(@Query("user_id") String userId, @Query("coupon_id") String couponId,
                                      @Query("is_favorite") String isFavorite);

    @GET("getallfavorites.php")
    public Call<GetAllFavorites> getAllFavorites(@Query("user_id") String userId, @Query("version_id") String versionId,
                                                 @Query("payment_id") String paymentId,
                                                 @Query("latitude") String latitude,
                                                 @Query("longitude") String longitude, @Query("city_id") String city_id);

    @GET("my_pinged_offers.php")
    public Call<GetAllFavorites> my_pinged_offers(@Query("user_id") String userId, @Query("version_id") String versionId,
                                                 @Query("payment_id") String paymentId,
                                                 @Query("latitude") String latitude,
                                                 @Query("longitude") String longitude, @Query("city_id") String city_id);

    @GET("get_all_pinged_offers.php")
    public Call<GetAllFavorites> getAllPingedOffers(@Query("user_id") String userId, @Query("version_id") String versionId,
                                                    @Query("payment_id") String paymentId,
                                                    @Query("latitude") String latitude,
                                                    @Query("longitude") String longitude, @Query("city_id") String city_id);

    @GET("getallnotifications.php")
    public Call<GetAllNotifications> getAllNotifications(@Query("user_id") String userId, @Query("version_id") String versionId,
                                                         @Query("payment_id") String paymentId);

    @GET("mark_all_notifications_as_read.php")
    public Call<Common> markAsRead(@Query("user_id") String userId, @Query("notification_id") String notificationId);

    @GET("share_ping.php")
    public Call<Common> sharePing(@Query("from_user_id") String userId, @Query("coupon_id") String couponId,
                                  @Query("mobile") String mobile);

    @GET("update_pinged_offer_status.php")
    public Call<GetAllFavorites> update_pinged_offer_status(@Query("ping_id") String ping_id, @Query("coupon_id") String coupon_id,
                                                   @Query("status") String status,@Query("user_id") String userId, @Query("version_id") String version_id,
                                                   @Query("payment_id") String payment_id,@Query("latitude") String latitude,@Query("longitude") String longitude,@Query("city_id") String city_id);

    @GET("get_profile_details.php")
    public Call<Common> getProfileDetails(@Query("user_id") String userId);

    @Multipart
    @POST("update_profile.php")
    public Call<Common> updateProfile(@Part("user_id") RequestBody userId, @Part("user_name") RequestBody userName, @Part("referral_code") RequestBody referral_code,
                                      @Part("is_image_uploaded") RequestBody isImageUploaded,
                                      @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("get_contacts.php")
    public Call<Common> syncContacts(@Field("user_id") String userId, @Field("contact_details") String contactDetails);

    @FormUrlEncoded
    @POST("reactivate_coupon.php")
    public Call<Common> reactivate_coupon(@Field("user_id") String userId, @Field("coupon_id") String coupon_id,@Field("payment_id") String payment_id,@Field("city_id") String city_id,@Field("redemption_id") String redemption_id);

}
