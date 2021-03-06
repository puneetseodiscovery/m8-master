package com.m8.m8;

import com.m8.m8.Activities.itservices.ITServiceRequestModel;
import com.m8.m8.Activities.itservices.ITServiceResponseModel;
import com.m8.m8.Models.CommissionModel;
import com.m8.m8.Models.LegalIsPresentOrNot;
import com.m8.m8.Models.MyM8sReferralModel;
import com.m8.m8.Models.PayOutstandingModel;
import com.m8.m8.Models.SalesModel;
import com.m8.m8.Models.SearchResultResponseModel;
import com.m8.m8.Models.TokenResponseModel;
import com.m8.m8.RetrofitModel.AccountDetailsApi;
import com.m8.m8.RetrofitModel.AccpectedApi;
import com.m8.m8.RetrofitModel.AddMandataApi;
import com.m8.m8.RetrofitModel.BankDeatilsApi;
import com.m8.m8.RetrofitModel.BankDeatilsUploadApi;
import com.m8.m8.RetrofitModel.BulkUploadApi;
import com.m8.m8.RetrofitModel.BuyApi;
import com.m8.m8.RetrofitModel.ComissionListApi;
import com.m8.m8.RetrofitModel.ContactSellerApi;
import com.m8.m8.RetrofitModel.CurrecnyConvter;
import com.m8.m8.RetrofitModel.DescriptedApi;
import com.m8.m8.RetrofitModel.FreeGentApi;
import com.m8.m8.RetrofitModel.GetAllMandate;
import com.m8.m8.RetrofitModel.GetComissionPrice;
import com.m8.m8.RetrofitModel.GetMessage;
import com.m8.m8.RetrofitModel.GetMetaData;
import com.m8.m8.RetrofitModel.GetOtpApi;
import com.m8.m8.RetrofitModel.GetPlanApi;
import com.m8.m8.RetrofitModel.GetPotentailApi;
import com.m8.m8.RetrofitModel.GetPotentialEarn;
import com.m8.m8.RetrofitModel.GetPropertyApi;
import com.m8.m8.RetrofitModel.GetShareNumberApi;
import com.m8.m8.RetrofitModel.GetSharedApi;
import com.m8.m8.RetrofitModel.GetStartCategoryApi;
import com.m8.m8.RetrofitModel.ItemAllDeatilsApi;
import com.m8.m8.RetrofitModel.LoginApi;
import com.m8.m8.RetrofitModel.MapApi;
import com.m8.m8.RetrofitModel.PasswordOTP;
import com.m8.m8.RetrofitModel.PasswordReset;
import com.m8.m8.RetrofitModel.ProfileDataApi;
import com.m8.m8.RetrofitModel.ProfileImageApi;
import com.m8.m8.RetrofitModel.ReferCodeApi;
import com.m8.m8.RetrofitModel.ReferLinkApi;
import com.m8.m8.RetrofitModel.RegisterApi;
import com.m8.m8.RetrofitModel.SearchApi;
import com.m8.m8.RetrofitModel.SendPlanData;
import com.m8.m8.RetrofitModel.ShareLinkApi;
import com.m8.m8.RetrofitModel.TermsConditionApi;
import com.m8.m8.RetrofitModel.TreeListApi;
import com.m8.m8.RetrofitModel.UploadBulkMandateApi;
import com.m8.m8.RetrofitModel.UploadPropertyApi;


import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("login")
    Call<LoginApi> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("user_register")
    Call<RegisterApi> register(
            @Field("first_name") String firstName,
            @Field("last_name") String lastName,
            @Field("email") String email,
            @Field("password") String password,
            @Field("contact_no") String contact,
            @Field("c_password") String name,
            @Field("refer_code") String code,
            @Field("device_token") String deviceToken
    );

    //get the otp for registeration
    @POST("varify_otp")
    Call<GetOtpApi> getOtp(
            @Query("otp") String otp,
            @Query("user_id") String userId,
            @Query("isrefer") boolean isrefer
    );

    //search property
    @POST("itemsearch")
    Call<SearchApi> searchApi(
            @Query("location") String location,
            @Query("price_range") String priceRange,
            @Query("extra_data") JSONObject extraData,
            @Query("category_id") String category,
            @Query("country") String country
    );


    // add bank Details
    @POST("addbankdetails")
    Call<BankDeatilsUploadApi> bankDetailsApi(
            @Query("user_id") String user_id,
            @Query("bank_name") String bankName,
            @Query("address_1") String address1,
            @Query("town") String city,
            @Query("post_code") String postcode,
            @Query("country") String country,
            @Query("account_name") String accountName,
            @Query("account_number") String accountNumber,
            @Query("iban") String iban,
            @Query("swift_code") String swift,
            @Query("paypal_email") String paypalid
    );


    @POST("getbankdetails")
    Call<BankDeatilsApi> bankDetailsGet(
            @Query("user_id") String token
    );

    // get the map data
    @POST("getitems")
    Call<MapApi> getPropertyMap(
            @Query("category_id") String categoryId,
            @Query("latitude") String latitude,
            @Query("longitude") String longitude
    );


    // add the property or item here
    @Multipart
    @POST("additem")
    Call<UploadPropertyApi> uploadProperty(
            @Query("user_id") String userId,
            @Query("title") String title,
            @Query("category_id") String category_id,
            @Query("description") String property_description,
            @Query("price") String price,
            @Query("cur_price") String cur_price,
            @Query("address_1") String address1,
            @Query("address_2") String address2,
            @Query("country") String country,
            @Query("county") String county,
            @Query("town") String town,
            @Query("postcode") String postcode,
            @Query("currency") String currency,
            @Query("extra_data") JSONObject exrtadata,
            @Part ArrayList<MultipartBody.Part> images,
            @Part MultipartBody.Part video
    );


    // add profile image into server
    @Multipart
    @POST("profileimage")
    Call<ProfileImageApi> profileImage(
            @Query("user_id") String token,
            @Part MultipartBody.Part dp
    );

    //here add the mandate data part 0
    @POST("addmandate")
    Call<AddMandataApi> agentMandate(
            @Query("user_id") String UserId,
            @Query("step") String step,
            @Query("item_id") String id,
            @Query("total_commision") String totalCommision
    );

    //here add the for free user
    @POST("addmandate")
    Call<FreeGentApi> freeMandate(
            @Query("user_id") String UserId,
            @Query("step") String step,
            @Query("item_id") String id,
            @Query("total_commision") String totalCommision,
            @Query("mandate_date") String Date
    );

    //here add mandate part 1
    @POST("addmandate")
    Call<AddMandataApi> mandataApi(
            @Query("price_type") String price_type,
            @Query("price_value") String value,
            @Query("mandate_date") String mandate,
            @Query("user_id") String userid,
            @Query("step") String step,
            @Query("item_id") String id
    );


    //here add mandate part 2
    @Multipart
    @POST("addmandate")
    Call<AddMandataApi> mandataApi2(
            @Query("mandate_date") String mandate,
            @Query("user_id") String userid,
            @Query("step") String step,
            @Query("item_id") String id,
            @Part MultipartBody.Part sign
    );

    //get pdf from here
    @GET
    Call<ResponseBody> downloadPdf(
            @Url String url);


    // get the profile deatils
    @POST("userdetails")
    Call<GetMetaData> getMetaData(
            @Query("user_id") String userId
    );


    //get the all my item those upload

    @POST("myitems")
    Call<GetPropertyApi> getPropertyApi(
            @Query("user_id") String userId,
            @Query("type") String type,
            @Query("address") String address,
            @Query("category_id") String CatId
    );

    //get the share link from here
    @POST("shareurl")
    Call<ShareLinkApi> sharelink(
            @Query("user_id") String userId,
            @Query("item_id") String itemId
    );

    // get the deshcripted data from url
    @GET
    Call<DescriptedApi> descriptedApi(
            @Url String url);


    //get all item those shared from the user
    @POST("getmyshareitems")
    Call<GetSharedApi> getSharedApi(
            @Query("user_id") String userId,
            @Query("category_id") String catId
    );


    //get the sharer number
    @POST("getshares")
    Call<GetShareNumberApi> getShareNumber(
            @Query("user_id") String id
    );


    // add profile data
    @Multipart
    @POST("createprofile")
    Call<ProfileDataApi> getProfileApi(
            @Query("user_id") String user_id,
            @Part MultipartBody.Part logo,
            @Part MultipartBody.Part port
    );


    //add profile share data
    @POST("createprofile")
    Call<ProfileDataApi> getShareProfileApi(
            @Query("user_id") String user_id,
            @Query("profile_type") String type,
            @Query("name") String first_name,
            @Query("contact_name") String Contact_name,
            @Query("email") String email,
            @Query("phone") String phone,
            @Query("address") String address_1,
            @Query("address_2") String address_2,
            @Query("town") String city,
            @Query("postal_code") String postcode,
            @Query("country") String country,
            @Query("leagal_name") String r_name,
            @Query("leagal_email") String r_email,
            @Query("leagal_phone") String r_phone,
            @Query("leagal_address") String r_address1,
            @Query("leagal_address2") String r_address2,
            @Query("leagal_town") String r_city,
            @Query("leagal_postalcode") String r_postcode,
            @Query("leagal_country") String r_country,
            @Query("b_name") String b_name,
            @Query("b_phone") String b_phone,
            @Query("b_address") String b_address,
            @Query("b_email") String b_email
    );

    //add profile share data
    @POST("checklegalrep")
    Call<LegalIsPresentOrNot> checklegalrepApi(
            @Query("user_id") String user_id
    );


    //get the share plan
    @GET("getplans")
    Call<GetPlanApi> getPlanData();

    //post the buy share
    @POST("addshares")
    Call<SendPlanData> sendBuyshareDetails(
            @Query("user_id") String UserId,
            @Query("plan_id") String PlanId,
            @Query("transaction_id") String transation
    );

    //post the buy share
    @POST("buyitemdirect")
    Call<BuyApi> sendBuyshareDetailsDirect(
            @Query("user_id") String UserId,
            @Query("item_id") String PlanId,
            @Query("token") String transation
    );

    //accpected url here
    @POST("acceptshare")
    Call<AccpectedApi> accptedUrl(
            @Query("user_id") String userId,
            @Query("share_url_id") String shareId
    );

    //get the category list
    @GET("getcategories")
    Call<GetStartCategoryApi> startApi(
            @Query("user_id") String userId
    );


    //get the all item details
    @POST("getitemdetail")
    Call<ItemAllDeatilsApi> getAllDetails(
            @Query("itemid") String id
    );


    //get the commission list
    @POST("getcommisiontreelist")
    Call<ComissionListApi> getComissionList(
            @Query("user_id") String userId,
            @Query("category_id") String CategoryId
    );

    //get the commission list
    @POST("getviewlist")
    Call<ComissionListApi> getViewList(
            @Query("user_id") String userId
    );

    //get the comission tree data
    @POST("getcommisiontree")
    Call<TreeListApi> getTreeList(
            @Query("user_id") String userId,
            @Query("item_id") String CategoryId
    );

    //buy the property
    @POST("buyitem")
    Call<BuyApi> buyItem(
            @Query("user_id") String userId,
            @Query("item_id") String itemId
    );

    //buy the property
    @POST("buyitemcheck")
    Call<BuyApi> buyItemCheck(
            @Query("user_id") String userId,
            @Query("item_id") String itemId
    );

    //contct to seller the property
    @POST("contactseller")
    Call<ContactSellerApi> contactSeller(
            @Query("user_id") String userId,
            @Query("item_id") String itemId
    );

    //get the otp for password reset
    @POST("password/create")
    Call<PasswordOTP> passwrdOTP(
            @Query("email") String email
    );

    //send otp for verfication
    @POST("password/find")
    Call<PasswordOTP> sendPasswrodOtp(
            @Query("email") String email,
            @Query("otp") String Otp
    );


    //set a new password
    @POST("password/reset")
    Call<PasswordReset> passwordReset(
            @Query("email") String email,
            @Query("otp") String Otp,
            @Query("password") String password,
            @Query("password_confirmation") String cPassword

    );

    // get the csv file from server
    @GET
    Call<ResponseBody> downloadcsv(
            @Url String url);

    //get propert sell number
    @POST("getitemlimit")
    Call<GetShareNumberApi> getPropertyNumber(
            @Query("user_id") String userId
    );

    //get all my mandate
    @POST("mymandate")
    Call<GetAllMandate> getallMandat(
            @Query("user_id") String userId
    );

    //get All user details information
    @POST("accountdetails")
    Call<AccountDetailsApi> getAccount(
            @Query("user_id") String userId
    );


    @POST("getpotentialitems")
    Call<GetPotentailApi> getProtentail(
            @Query("user_id") String userId,
            @Query("category_id") String categoryId,
            @Query("type") String type
    );


    //get the potential earning
    @POST("mypotentialearnings")
    Call<GetPotentialEarn> getPotentialEarn(
            @Query("user_id") String userId,
            @Query("category_id") String category
    );


    //get refercode
    @POST("refercode")
    Call<ReferCodeApi> getReferApi(
            @Query("user_id") String userId
    );


    //send refer code inot mail
    @POST("refertouser")
    Call<ReferLinkApi> sendReferlcode(
            @Query("user_id") String user_id,
            @Query("category_id") String category_id,
            @Query("business_name") String business_name,
            @Query("business_owner_name") String business_owner_name,
            @Query("business_email") String business_email,
            @Query("business_phone") String business_phone,
            @Query("your_name") String your_name,
            @Query("your_email") String your_email,
            @Query("your_phone") String your_phone,
            @Query("share_app_url") String share_app_url
    );

    //get terms and condition and privecy
    @GET("pagecontent")
    Call<TermsConditionApi> getTermsCondition();

    //Bulk upload
    @Multipart
    @POST("bulkupload")
    Call<BulkUploadApi> bulkUpload(
            @Query("format") String format,
            @Query("user_id") String UserId,
            @Query("category_id") String categoryId,
            @Part MultipartBody.Part logo,
            @Part MultipartBody.Part file

    );

    //upload bulk mandate step 0
    @POST("bulkmandate")
    Call<UploadBulkMandateApi> bulkAgentMandate(
            @Query("bulk_id") String bulkId,
            @Query("user_id") String UserId,
            @Query("step") String step,
            @Query("total_commision") String total
    );


    //upload bulk mandate step 1
    @POST("bulkmandate")
    Call<UploadBulkMandateApi> bulkMandate(
            @Query("bulk_id") String bulkId,
            @Query("user_id") String UserId,
            @Query("step") String step,
            @Query("mandate_date") String date,
            @Query("price_type") String priceType,
            @Query("price_value") String priceValue
    );


    //upload bulk mandate step 2
    @Multipart
    @POST("bulkmandate")
    Call<UploadBulkMandateApi> bulkMandate2(
            @Query("bulk_id") String bulkId,
            @Query("user_id") String UserId,
            @Query("step") String step,
            @Query("mandate_date") String date,
            @Part MultipartBody.Part sign
    );

    //get the csv and xlsx file
    @POST("samplefiles")
    Call<GetMessage> getCsv(
            @Query("category_id") String categryId,
            @Query("user_id") String UserId
    );

    @POST("mycommisionitem")
    Call<GetComissionPrice> getComissionPrice(
            @Query("item_id") String itemId,
            @Query("user_id") String UserId
    );


    // covert the currency
    @GET
    Call<CurrecnyConvter> GetCurrency(
            @Url String url
    );

    //get the search result

    @POST("getitemssearch")
    Call<SearchResultResponseModel> getSearchResult(
            @Query("category_id") String category_id,
            @Query("location") String location
    );

    //add profile share data
    @Multipart
    @POST("createprofile")
    Call<ProfileDataApi> getShareProfileApiP(
            @Query("user_id") String user_id,
            @Query("profile_type") String type,
            @Query("name") String first_name,
            @Query("contact_name") String Contact_name,
            @Query("email") String email,
            @Query("phone") String phone,
            @Query("address") String address_1,
            @Query("address_2") String address_2,
            @Query("town") String city,
            @Query("postal_code") String postcode,
            @Query("country") String country,
            @Query("leagal_name") String r_name,
            @Query("leagal_email") String r_email,
            @Query("leagal_phone") String r_phone,
            @Query("leagal_address") String r_address1,
            @Query("leagal_address2") String r_address2,
            @Query("leagal_town") String r_city,
            @Query("leagal_postalcode") String r_postcode,
            @Query("leagal_country") String r_country,
            @Query("b_name") String b_name,
            @Query("b_phone") String b_phone,
            @Query("b_address1") String b_address,
            @Query("b_address2") String b_address2,
            @Query("b_town") String b_town,
            @Query("b_postalcode") String b_postalcode,
            @Query("b_contact_name") String b_contact_name,
            @Query("b_country") String b_country,
            @Query("b_email") String b_email,
            @Part MultipartBody.Part logo
    );

    @POST("payoutstandings")
    Call<PayOutstandingModel> payoutstandings(
            @Query("user_id") String user_id
    );

    //get terms and condition and privecy
    @GET("itservices")
    Call<ITServiceResponseModel> getITServices();

    @POST("itservicesrequest")
    Call<ITServiceRequestModel> sendITRequest(
            @Query("user_id") String user_id,
            @Query("category_id") String category_id
    );


    //get the all item details
    @POST("checklogin")
    Call<TokenResponseModel> getTokenDetails(
            @Header("Authorization") String auth,
            @Header("Accept") String accept
    );

    //get the referrals
    @POST("UserReferEarningDetails")
    Call<MyM8sReferralModel> getReferrals(
            @Query("user_id") String userId
    );

    //get the commission
    @POST("UserCommissionEarningDetails")
    Call<CommissionModel> getCommission(
            @Query("user_id") String userId
    );

    //get the sales
    @POST("UserSalesEarningDetails")
    Call<SalesModel> getSales(
            @Query("user_id") String userId
    );

}
