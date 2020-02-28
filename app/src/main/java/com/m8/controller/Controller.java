package com.m8.controller;

import com.m8.m8.Activities.HomeActivity;
import com.m8.m8.ApiInterface;
import com.m8.m8.RetrofitModel.GetShareNumberApi;
import com.m8.m8.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Controller {

    public GetPropertyNumber getPropertyNumber;


    /*++++++++++++++++++++Get the property number+++++++++++++++++++++++++*/

    public Controller(GetPropertyNumber getPropertyNumber1) {
        getPropertyNumber = getPropertyNumber1;
    }

    public void setGetPropertyNumber(String id) {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<GetShareNumberApi> call = apiInterface.getPropertyNumber(HomeActivity.userId);
        call.enqueue(new Callback<GetShareNumberApi>() {
            @Override
            public void onResponse(Call<GetShareNumberApi> call, Response<GetShareNumberApi> response) {
                if (response.isSuccessful()) {
                    getPropertyNumber.onSucessnumber(response);
                }
            }

            @Override
            public void onFailure(Call<GetShareNumberApi> call, Throwable t) {
                getPropertyNumber.onError(t.getMessage());
            }
        });
    }


    public interface GetPropertyNumber {
        void onSucessnumber(Response<GetShareNumberApi> response);

        void onError(String error);
    }
    /*+++++++++++++++++++END++++++++++++++++++++*/


}
